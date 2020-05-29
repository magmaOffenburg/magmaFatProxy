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

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import hso.autonomy.util.geometry.Area2D;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.GameState;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.agent.model.worldmodel.ISoccerPitchDescription;

/**
 * @author kdorer
 *
 */
public class KickPositionProfilerGoal extends KickPositionProfiler
{
	private static final float GOAL_KICK_DISTANCE = 8;

	protected float goalKickDistance;

	public KickPositionProfilerGoal(ISoccerPitchDescription soccerPitch)
	{
		super(soccerPitch);

		this.goalKickDistance = GOAL_KICK_DISTANCE;
	}

	public KickPositionProfilerGoal(ISoccerPitchDescription soccerPitch, float maxEvaluationDistance,
			float kickableAreaBorder, double opponentBlockDistance, float goalKickDistance)
	{
		super(soccerPitch, maxEvaluationDistance, kickableAreaBorder, opponentBlockDistance);

		this.goalKickDistance = goalKickDistance;
	}

	@Override
	protected void doEstimate(
			IRoboCupThoughtModel thoughtModel, Area2D.Float playableArea, List<KickPositionEstimation> result)
	{
		// check if we are in goal kick distance
		Vector3D otherGoalPosition = thoughtModel.getWorldModel().getOtherGoalPosition();
		Vector3D ballPosition = thoughtModel.getWorldModel().getBall().getPosition();
		double distance = Vector3D.distance(ballPosition, otherGoalPosition);

		if (distance < goalKickDistance) {
			// we focus on goal kicks if close to the goal
			Vector3D position = otherGoalPosition;

			double ballX = ballPosition.getX();
			double ballY = ballPosition.getY();
			if (Math.abs(ballY) > 3.0 &&
					(ballX > thoughtModel.getWorldModel().fieldHalfLength() - (Math.abs(ballY) - 2) * 0.3)) {
				// we are close to opponent corner so kick back
				position = otherGoalPosition.add(new Vector3D(-2, 0, 0));
			}

			result.add(new KickPositionEstimation(position, 1));
			// TODO: make special calculation for goal kick direction
			// position = otherGoalPosition.add(new Vector3D(0, 0.6, 0));
			// utility = estimateUtility(thoughtModel, playableArea, position,
			// 0.0);
			// result.add(new KickPositionEstimation(position, utility));
			// position = otherGoalPosition.add(new Vector3D(0, -0.6, 0));
			// utility = estimateUtility(thoughtModel, playableArea, position,
			// 0.0);
			// result.add(new KickPositionEstimation(position, utility));
		} else {
			super.doEstimate(thoughtModel, playableArea, result);
		}
	}

	@Override
	protected float estimateUtility(IRoboCupThoughtModel thoughtModel, Area2D.Float playableArea, Vector3D position,
			double opponentBlockDistance)
	{
		float utility = super.estimateUtility(thoughtModel, playableArea, position, opponentBlockDistance);

		utility += getGoalUtility(thoughtModel, position);

		return utility;
	}

	protected float getGoalUtility(IRoboCupThoughtModel thoughtModel, Vector3D position)
	{
		IRoboCupWorldModel worldModel = thoughtModel.getWorldModel();
		float fieldHalfLength = thoughtModel.getWorldModel().fieldHalfLength();
		Vector3D targetPos = worldModel.getOtherGoalPosition();
		Vector3D ballPos = worldModel.getBall().getPosition();
		if (worldModel.getGameState() == GameState.OWN_KICK_OFF) {
			// position at kickoff
			targetPos = new Vector3D(5, -6, 0);
			// Kick off to own half
			// targetPos = new Vector3D(-5, -2, 0);

		} else if (ballPos.getX() < -4) {
			// we are in defense
			double newX = ballPos.getX() + 14;
			double newY = worldModel.fieldHalfWidth() - 5;
			if (ballPos.getY() < -0.2) {
				// kick towards the right side
				targetPos = new Vector3D(newX, -newY, 0);
			} else if (ballPos.getY() > 0.2) {
				// kick towards the left side
				targetPos = new Vector3D(newX, newY, 0);
			}
		}
		double distance = Vector3D.distance(targetPos, position);

		return (float) ((fieldHalfLength - distance) / 1.0);
	}

	@Override
	protected float getDistanceUtility(IRoboCupThoughtModel thoughtModel, Vector3D position)
	{
		// we prefer short passes for kick
		Vector3D otherGoalPosition = thoughtModel.getWorldModel().getOtherGoalPosition();
		float fieldHalfLength = thoughtModel.getWorldModel().fieldHalfLength();
		Vector3D ballPosition = thoughtModel.getWorldModel().getBall().getPosition();
		double distanceGoal = Vector3D.distance(ballPosition, otherGoalPosition);

		if (distanceGoal < goalKickDistance) {
			double distance = Vector3D.distance(ballPosition, position);
			return (float) ((fieldHalfLength - distance) / fieldHalfLength);
		} else {
			return super.getDistanceUtility(thoughtModel, position);
		}
	}
}
