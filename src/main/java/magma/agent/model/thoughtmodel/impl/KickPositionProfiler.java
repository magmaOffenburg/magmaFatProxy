/*******************************************************************************
 * Copyright 2008 - 2020 Hochschule Offenburg
 *
 * This file is part of magmaOffenburg.
 * magmaOffenburg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * magmaOffenburg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with magmaOffenburg. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package magma.agent.model.thoughtmodel.impl;

import hso.autonomy.agent.model.worldmodel.IMoveableObject;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Area2D;
import hso.autonomy.util.geometry.VectorUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.agent.model.worldmodel.ISoccerPitchDescription;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * @author kdorer
 */
public class KickPositionProfiler
{
	public static final float DEFAULT_AREA_BORDER = 2;

	protected List<KickPositionEstimation> estimations;

	protected Area2D.Float area;

	private boolean refreshProfile;

	protected Vector3D filteredPosition;

	protected float maxEvaluationDistance;

	protected double defaultOpponentBlockDistance;

	public KickPositionProfiler(ISoccerPitchDescription soccerPitch)
	{
		this(soccerPitch, 16, DEFAULT_AREA_BORDER, 0.5);
	}

	public KickPositionProfiler(ISoccerPitchDescription soccerPitch, float maxEvaluationDistance,
			float kickableAreaBorder, double opponentBlockDistance)
	{
		estimations = Collections.emptyList();
		area = new Area2D
					   .Float(-soccerPitch.fieldHalfLength(), soccerPitch.fieldHalfLength(),
							   -soccerPitch.fieldHalfWidth(), soccerPitch.fieldHalfWidth())
					   .applyBorder(kickableAreaBorder);
		this.maxEvaluationDistance = maxEvaluationDistance;
		this.defaultOpponentBlockDistance = opponentBlockDistance;
		filteredPosition = Vector3D.ZERO;
		refreshProfile = false;
	}

	public List<KickPositionEstimation> getEstimations(IRoboCupThoughtModel thoughtModel)
	{
		estimations = estimatePositions(thoughtModel, area);
		return estimations;
	}

	public void resetProfile()
	{
		refreshProfile = true;
	}

	public void setArea(Area2D.Float area)
	{
		this.area = area;
	}

	public Vector3D getFilteredPosition()
	{
		return filteredPosition;
	}

	public Angle getIntendedKickDirection(IRoboCupThoughtModel thoughtModel)
	{
		refreshProfile(thoughtModel);
		IMoveableObject ball = thoughtModel.getWorldModel().getBall();
		return ball.getDirectionTo(filteredPosition);
	}

	public float getIntendedKickDistance(IRoboCupThoughtModel thoughtModel)
	{
		refreshProfile(thoughtModel);
		IMoveableObject ball = thoughtModel.getWorldModel().getBall();
		return (float) ball.getDistanceToXY(filteredPosition);
	}

	private void refreshProfile(IRoboCupThoughtModel thoughtModel)
	{
		List<KickPositionEstimation> result = estimations;
		if (refreshProfile) {
			result = estimatePositions(thoughtModel, area);
			if (!result.isEmpty()) {
				refreshProfile = false;
				filteredPosition = filterKickPosition(filteredPosition, result);
			}
		}
	}

	protected Vector3D filterKickPosition(Vector3D previousKickTarget, List<KickPositionEstimation> kickEstimations)
	{
		final float FILTER_WEIGHT = 1;
		// final float FILTER_WEIGHT = 50;

		Vector3D targetPos = previousKickTarget.scalarMultiply(FILTER_WEIGHT);
		targetPos = targetPos.add(kickEstimations.get(0).getPosition());
		return targetPos.scalarMultiply(1.0 / (FILTER_WEIGHT + 1));
	}

	public List<KickPositionEstimation> estimatePositions(IRoboCupThoughtModel thoughtModel, Area2D.Float playableArea)
	{
		if (refreshProfile) {
			List<KickPositionEstimation> result = new ArrayList<>();
			doEstimate(thoughtModel, playableArea, result);
			Collections.sort(result);
			estimations = result;
		}
		return estimations;
	}

	protected void doEstimate(
			IRoboCupThoughtModel thoughtModel, Area2D.Float playableArea, List<KickPositionEstimation> result)
	{
		IRoboCupWorldModel worldModel = thoughtModel.getWorldModel();
		IMoveableObject ball = worldModel.getBall();
		final int angleStep = getEvaluationAngleStep(thoughtModel);

		for (int angle = 0; angle < 360; angle += angleStep) {
			Angle direction = Angle.deg(angle);
			float maxDistance = getMaxEvaluationDistance(thoughtModel, direction);
			float distanceStep = getEvaluationDistanceStep(thoughtModel, direction);
			float initialDistance = getInitialEvaluationDistance(angle, angleStep, distanceStep, maxDistance);

			if (initialDistance < distanceStep || initialDistance > maxDistance) {
				continue;
			}

			for (float distance = initialDistance; distance < maxDistance; distance += distanceStep) {
				Vector3D position =
						ball.getPosition().add((new Vector3D(direction.radians(), 0)).scalarMultiply(distance));

				if (!playableArea.contains(position)) {
					Vector2D intersection = worldModel.goalLineIntersection(
							VectorUtils.to2D(ball.getPosition()), VectorUtils.to2D(position), 0.1);
					if (intersection == null || ball.getPosition().getX() > position.getX()) {
						// we left the playable area
						if (!playableArea.contains(ball.getPosition())) {
							continue;
						} else {
							// if the ball is in kickable area, all further points are not
							break;
						}
					}
				}

				float utility = estimateUtility(thoughtModel, playableArea, position, defaultOpponentBlockDistance);
				if (utility < -100000) {
					// the point should not be added nor further points on the ray
					break;
				}
				KickPositionEstimation estimate = new KickPositionEstimation(position, utility);
				result.add(estimate);

				// System.out.println("estimate: " + utility + " pos: " + position);
			}
		}
	}

	protected float getMaxEvaluationDistance(IRoboCupThoughtModel thoughtModel, Angle direction)
	{
		if (thoughtModel.getOpponentsAtMeList().isEmpty()) {
			// No opponents around...
			return maxEvaluationDistance;
		}

		// If we have opponents
		Vector3D closestOpponentPos = thoughtModel.getOpponentsAtMeList().get(0).getPosition();
		double distanceToOpponent = thoughtModel.getWorldModel().getThisPlayer().getDistanceToXY(closestOpponentPos);

		return distanceToOpponent < 1.5 ? (1 + maxEvaluationDistance / 3) : maxEvaluationDistance;
	}

	protected float getInitialEvaluationDistance(int angle, int angleStep, float distanceStep, float maxDistance)
	{
		return 1;
	}

	protected float getEvaluationDistanceStep(IRoboCupThoughtModel thoughtModel, Angle direction)
	{
		return 1;
	}

	protected int getEvaluationAngleStep(IRoboCupThoughtModel thoughtModel)
	{
		return 10;
	}

	protected float estimateUtility(IRoboCupThoughtModel thoughtModel, Area2D.Float playableArea, Vector3D position,
			double opponentBlockDistance)
	{
		float utility = 0;

		utility += getPlayerUtility(thoughtModel, position, true, opponentBlockDistance);
		utility += getPlayerUtility(thoughtModel, position, false, opponentBlockDistance);
		utility += getDistanceUtility(thoughtModel, position);

		return utility;
	}

	protected float getPlayerUtility(
			IRoboCupThoughtModel thoughtModel, Vector3D position, boolean ownTeam, double opponentBlockDistance)
	{
		// find smallest distance player
		double minDistance = Double.POSITIVE_INFINITY;
		for (IPlayer player : thoughtModel.getWorldModel().getVisiblePlayers()) {
			Vector3D playerPos = player.getPosition();
			if (ownTeam) {
				// we want to pass in front of our players
				playerPos.add(new Vector3D(1, 0, 0));
			}
			if (player.isOwnTeam() == ownTeam) {
				double distance = Vector3D.distance(playerPos, position);
				if (distance < minDistance) {
					minDistance = distance;
				}
			}
		}
		int maxEvaluation = 5;
		if (minDistance > maxEvaluation) {
			return 0;
		}

		if (ownTeam) {
			if (minDistance < 1) {
				return maxEvaluation;
			}
			return (float) (maxEvaluation - minDistance + 1);
		}
		// opponent
		if (minDistance <= opponentBlockDistance) {
			// if the opponent is very close, we do not want to add this point nor
			// other points on this ray
			return Float.NEGATIVE_INFINITY;
		}
		return (float) -(maxEvaluation - minDistance);
	}

	protected float getDistanceUtility(IRoboCupThoughtModel thoughtModel, Vector3D position)
	{
		// we prefer long passes over short passes
		IMoveableObject ball = thoughtModel.getWorldModel().getBall();
		float fieldHalfLength = thoughtModel.getWorldModel().fieldHalfLength();
		double distance = Vector3D.distance(ball.getPosition(), position);
		if (distance < (1 + fieldHalfLength / 4)) {
			return -5;
		}
		return (float) (distance / fieldHalfLength);
	}
}
