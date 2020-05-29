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
/**
 *
 */
package magma.agent.decision.behavior.base;

import java.util.Collections;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.PoseSpeed2D;
import hso.autonomy.util.geometry.VectorUtils;
import magma.agent.decision.behavior.IKick;
import magma.agent.decision.behavior.IKickParameters;
import magma.agent.decision.behavior.IWalkEstimator;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IBall;
import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.IRoboCupWorldModel;

/**
 * @author kdorer
 */
public class KickEstimator
{
	private IRoboCupThoughtModel thoughtModel;

	private IRoboCupWorldModel worldModel;

	private transient IWalkEstimator walkEstimator;

	public KickEstimator(IRoboCupThoughtModel thoughtModel, IWalkEstimator walkEstimator)
	{
		super();
		this.thoughtModel = thoughtModel;
		this.worldModel = thoughtModel.getWorldModel();
		this.walkEstimator = walkEstimator;
	}

	public float getKickUtility(IKick kick)
	{
		IKickParameters kickParams = kick.getKickParameters();
		// fail if we are nowhere near the ball
		if (worldModel.getThisPlayer().getDistanceToXY(worldModel.getBall()) > 1) {
			return -1;
		}

		// fail if ball is rolling
		if (worldModel.getBall().getSpeed().getNorm() > kickParams.getBallMaxSpeed()) {
			//			System.out.println(kick.getName() + ": "
			//							   + "Ball too fast: " + worldModel.getBall().getSpeed().getNorm());
			return -1;
		}

		if (getApplicability(kick) < 0) {
			//			System.out.println(kick.getName() + ": not applicable.");
			return -1;
		}

		return 0;
	}

	public float getApplicability(IKick kick)
	{
		IKickParameters kickParams = kick.getKickParameters();
		if (!isOpponentDistanceFitting(kickParams)) {
			//			System.out.println(kick.getName() + ": opponent too close.");
			return -1;
		}

		// HACK: do not use long kicks if short kick is intended
		if (kickParams.getMaxKickDistance() > 12 && kickParams.getIntendedKickDistance() < 8) {
			return -1;
		}

		// Do not kick outside the field
		IBall ball = worldModel.getBall();
		Vector2D ballPosition2D = VectorUtils.to2D(ball.getPosition());
		Angle kickDirection = kickParams.getKickDirection();
		Vector2D resultingPos = ballPosition2D.add(kickDirection.applyTo(kickParams.getMaxKickDistance(), 0));
		if (Math.abs(resultingPos.getX()) > worldModel.fieldHalfLength() ||
				Math.abs(resultingPos.getY()) > worldModel.fieldHalfWidth()) {
			// Allow kicking into the opponent's goal
			if (Math.abs(kickDirection.degrees()) > 95) {
				// it is certainly away from goal, so outside
				//				System.out.println(kick.getName() + ": would end up outside the field.");
				return -1;
			}
			if (Math.abs(ballPosition2D.getY() +
						 Math.tan(kickDirection.radians()) * (worldModel.fieldHalfLength() - ballPosition2D.getX())) >
					worldModel.goalHalfWidth()) {
				//				System.out.println(kick.getName() + ": would end up outside the field 2.");
				return -1;
			}
		}

		// measure how well it fits the desired distance
		float distanceMalus = 0;
		if (kickParams.getMinKickDistance() > kickParams.getIntendedKickDistance()) {
			// The minimum kick distance is further than the intended kick distance
			distanceMalus = (float) (kickParams.getMinKickDistance() - kickParams.getIntendedKickDistance());
		} else if (kickParams.getIntendedKickDistance() > kickParams.getMaxKickDistance()) {
			distanceMalus = (float) (kickParams.getIntendedKickDistance() - kickParams.getMaxKickDistance());
		}

		// measure how hard it is to get there
		IPose2D target = kick.getAbsoluteRunToPose();
		PoseSpeed2D targetSpeed = new PoseSpeed2D(target, Vector2D.ZERO);
		float walkTime =
				walkEstimator.getFastestWalkTime(worldModel.getThisPlayer(), Collections.singletonList(targetSpeed));

		// measure how much this position is blocking the own goal
		float positionMalus = 0;
		IPlayer opponentAtBall = thoughtModel.getOpponentAtBall();
		if (opponentAtBall != null) {
			// only check in own half
			if (ball.getPosition().getX() < 0) {
				double opponentBallDistance = opponentAtBall.getDistanceToXY(ball);
				if (opponentBallDistance < 2.0) {
					Vector2D point1 = VectorUtils.to2D(worldModel.getOwnGoalPosition());
					Vector2D point2 = VectorUtils.to2D(kick.getExpectedBallPosition());
					Vector2D thePoint = target.getPosition();
					double distance = Geometry.getDistanceToLine(point1, point2, thePoint);
					positionMalus = (float) (distance * 10);
				}
			}
		}

		return kickParams.getPriority() - distanceMalus - walkTime - positionMalus;
	}

	private boolean isOpponentDistanceFitting(IKickParameters kickParams)
	{
		IPlayer opponentAtBall = thoughtModel.getOpponentAtBall();
		if (opponentAtBall == null) {
			// assume always applicable if there's no opponents (mainly for testing)
			return true;
		}

		double myBallDistance = worldModel.getThisPlayer().getDistanceToXY(worldModel.getBall());
		double opponentBallDistance = opponentAtBall.getDistanceToXY(worldModel.getBall());

		// check max distance
		if (opponentBallDistance >= myBallDistance + kickParams.getOpponentMaxDistance()) {
			return false;
		}

		// minDistance is only applicable if opponent can attack us
		if (worldModel.getGameState().isOwnKick()) {
			return true;
		}

		// check min distance
		if (opponentBallDistance < myBallDistance + kickParams.getOpponentMinDistance()) {
			return false;
		}
		return true;
	}

	public IPose2D getAbsoluteRunToPose(IKick kick)
	{
		Angle intendedKickDirection = kick.getIntendedKickDirection();
		IKickParameters kickParams = kick.getKickParameters();
		Vector3D globalRelativeTargetPos =
				intendedKickDirection.applyTo(kickParams.getRelativeRunToPose().getPosition3D());

		Angle dir = intendedKickDirection.add(kickParams.getRelativeRunToPose().getAngle());

		Angle ourDirection = worldModel.getThisPlayer().getHorizontalAngle();

		double delta = Math.abs(dir.subtract(ourDirection).degrees());

		if (delta > 20) {
			// to be able to run round the ball we have to stay away a bit
			float maxKeepAwayFactor = 0.5f;
			globalRelativeTargetPos = globalRelativeTargetPos.scalarMultiply(
					1 + (Geometry.getLinearFuzzyValue(20, 80, true, delta) * maxKeepAwayFactor));
		}

		Vector3D ballPos = kick.getExpectedBallPosition();
		return new Pose2D(
				ballPos.getX() + globalRelativeTargetPos.getX(), ballPos.getY() + globalRelativeTargetPos.getY(), dir);
	}

	public Vector3D getBallPosAtKick(IKickParameters kickParams)
	{
		int cycles = Math.min(kickParams.getBallHitCycles(), 150);
		return worldModel.getBall().getFuturePosition(cycles);
	}
}
