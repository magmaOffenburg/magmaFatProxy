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
package magma.agent.decision.behavior.complex.walk;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.model.worldmodel.IMoveableObject;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Area2D;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose3D;
import magma.agent.IHumanoidConstants;
import magma.agent.decision.behavior.IBaseWalk;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IKick;
import magma.agent.decision.behavior.IWalkEstimator;
import magma.agent.decision.behavior.SupportFoot;
import magma.agent.decision.behavior.base.KickDistribution;
import magma.agent.decision.behavior.base.KickEstimator;
import magma.agent.decision.behavior.base.KickParameters;
import magma.agent.decision.behavior.complex.RoboCupSingleComplexBehavior;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.IThisPlayer;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Dribbling extends RoboCupSingleComplexBehavior implements IKick
{
	/** length of the dribble area */
	private static final float MAX_X_DRIBBLE = 0.35f;

	/** width of the dribble area */
	private static final float MAX_Y_DRIBBLE = 0.10f;

	public static final Area2D.Float DRIBBLEABLE_AREA =
			new Area2D.Float(0, MAX_X_DRIBBLE, -MAX_Y_DRIBBLE, MAX_Y_DRIBBLE);

	private transient KickParameters kickParams;

	private IBaseWalk walk;

	private Angle intendedKickOffset;

	private transient KickEstimator kickEstimator;

	private KickDistribution distribution;

	public Dribbling(String name, IRoboCupThoughtModel thoughtModel, BehaviorMap behaviors, SupportFoot kickingFoot,
			IPose2D relativeRunToPose, float opponentMaxDistance, Angle intendedKickOffset,
			IWalkEstimator walkEstimator)
	{
		super(name, thoughtModel, behaviors);

		kickParams = new KickParameters(kickingFoot, (Pose2D) relativeRunToPose, new Vector2D(0.2, 0), Angle.ZERO,
				intendedKickOffset, 3, 3, -10, opponentMaxDistance, 0.05f, 1, 50);
		this.intendedKickOffset = intendedKickOffset;
		this.walk = (IBaseWalk) behaviors.get(IBehaviorConstants.IK_WALK);
		kickEstimator = new KickEstimator(thoughtModel, walkEstimator);
		distribution = createKickDistribution(intendedKickOffset);
	}

	private KickDistribution createKickDistribution(Angle intendedKickOffset)
	{
		// values are guessed, but should ideally be measured!
		double[] distanceDistribution = new double[] {0, 0, 0, 0, 0.05, 0.2, 0.5, 0.2, 0.05};

		int maximum = (int) Math.round(Math.abs(intendedKickOffset.degrees()) / KickDistribution.ANGLE_INTERVAL);
		int distributionLength = maximum + 3;
		double[] angleDistribution = new double[distributionLength];
		for (int i = 0; i < distributionLength; i++) {
			int offset = Math.abs(maximum - i);
			double value = 0;
			switch (offset) {
			case 0:
				value = 0.5;
				break;
			case 1:
				value = 0.2;
				break;
			case 2:
				value = 0.05;
				break;
			}
			angleDistribution[i] = value;
		}

		return new KickDistribution(distanceDistribution, angleDistribution);
	}

	@Override
	protected IBehavior decideNextBasicBehavior()
	{
		walk.setMovement(100, 0, 0);
		// System.out.println(getWorldModel().getThisPlayer().getTeamname() + " Dribbling ...");
		return walk;
	}

	@Override
	public void setIntendedKickDirection(Angle intendedKickDirection)
	{
		// value is ignored by Dribbling
		kickParams.setIntendedKickDirection(intendedKickOffset);
	}

	@Override
	public Angle getIntendedKickDirection()
	{
		return kickParams.getIntendedKickDirection();
	}

	@Override
	public SupportFoot getKickingFoot()
	{
		return kickParams.getKickingFoot();
	}

	@Override
	public IPose2D getRelativeRunToPose()
	{
		return kickParams.getRelativeRunToPose();
	}

	@Override
	public Vector2D getSpeedAtRunToPose()
	{
		return kickParams.getSpeedAtRunToPose();
	}

	@Override
	public IPose2D getAbsoluteRunToPose()
	{
		// dribblings are fixed direction and ignore intended direction
		Vector3D ballPos = getExpectedBallPosition();
		return new Pose2D(ballPos.getX() + kickParams.getRelativeRunToPose().getX(),
				ballPos.getY() + kickParams.getRelativeRunToPose().getY(), intendedKickOffset);
	}

	@Override
	public float getKickUtility()
	{
		if (kickEstimator.getKickUtility(this) < 0) {
			return -1;
		}

		if (getApplicability() < 0) {
			return -1;
		}

		Vector3D ballPosition = getWorldModel().getBall().getPosition();
		int sideFactor = kickParams.getKickingFoot() == SupportFoot.RIGHT ? 1 : -1;
		Pose2D ballPose = new Pose2D(ballPosition, kickParams.getKickDirection());
		String otherFootName =
				kickParams.getKickingFoot() == SupportFoot.RIGHT ? IHumanoidConstants.LFoot : IHumanoidConstants.RFoot;
		Pose3D currentOtherFootPose = getAgentModel().getBodyPart(otherFootName).getPose();
		Pose2D globalOtherFootPose = getWorldModel().getThisPlayer().calculateGlobalBodyPose2D(currentOtherFootPose);

		Pose2D diffPose = globalOtherFootPose.applyInverseTo(ballPose);
		diffPose.y += sideFactor * 0.11;

		Vector3D otherGoalPos = getWorldModel().getOtherGoalPosition();
		double distanceToOpponentGoal = getWorldModel().getBall().getDistanceToXY(otherGoalPos);

		double minDeg = -20;
		double maxDeg = 20;

		if (distanceToOpponentGoal < 4) {
			Vector3D leftGoalPost = otherGoalPos.add(new Vector3D(0, getWorldModel().goalHalfWidth(), 0));
			Vector3D rightGoalPost = otherGoalPos.add(new Vector3D(0, -getWorldModel().goalHalfWidth(), 0));

			maxDeg = Angle.rad(leftGoalPost.subtract(ballPosition).getAlpha())
							 .subtract(kickParams.getKickDirection())
							 .degrees();
			minDeg = Angle.rad(rightGoalPost.subtract(ballPosition).getAlpha())
							 .subtract(kickParams.getKickDirection())
							 .degrees();

			if (minDeg > -5) {
				minDeg = -5;
			}

			if (maxDeg < 5) {
				maxDeg = 5;
			}
		}

		// Return failure if angle deviation is bigger than 10 degrees
		if (diffPose.getAngle().degrees() < minDeg || diffPose.getAngle().degrees() > maxDeg) {
			return -1;
		}

		// Return failure if y deviation is bigger than 3 cm
		if (Math.abs(diffPose.y) > 0.07) {
			return -1;
		}

		// Return failure if x distance is bigger than 7 cm
		if (diffPose.x > 0.50 || diffPose.x < 0.0) {
			return -1;
		}

		return 10 + (float) (Math.abs(Math.abs(diffPose.x) - 0.11) + Math.abs(diffPose.y));
	}

	@Override
	public double getMaxKickDistance()
	{
		return kickParams.getMaxKickDistance();
	}

	@Override
	public double getMinKickDistance()
	{
		return kickParams.getMinKickDistance();
	}

	@Override
	public void setKickPower(float kickPower)
	{
	}

	@Override
	public boolean isUnstable()
	{
		return false;
	}

	public static Dribbling getLeftVersion(String name, IRoboCupThoughtModel thoughtModel, BehaviorMap behaviors,
			float opponentMaxDistance, Angle intendedKickOffset, IWalkEstimator walkEstimator)
	{
		return new Dribbling(name, thoughtModel, behaviors, SupportFoot.LEFT, new Pose2D(-0.12, -0.055),
				opponentMaxDistance, intendedKickOffset, walkEstimator);
	}

	public static Dribbling getRightVersion(String name, IRoboCupThoughtModel thoughtModel, BehaviorMap behaviors,
			float opponentMaxDistance, Angle intendedKickOffset, IWalkEstimator walkEstimator)
	{
		return new Dribbling(name, thoughtModel, behaviors, SupportFoot.RIGHT, new Pose2D(-0.12, 0.055),
				opponentMaxDistance, intendedKickOffset, walkEstimator);
	}

	@Override
	public IBehavior switchFrom(IBehavior actualBehavior)
	{
		if (getCurrentBehavior().isFinished()) {
			return super.switchFrom(actualBehavior);
		} else {
			return this;
		}
	}

	@Override
	public void setIntendedKickDistance(float intendedKickDistance)
	{
		kickParams.setIntendedKickDistance(intendedKickDistance);
	}

	@Override
	public float getApplicability()
	{
		IMoveableObject ball = getWorldModel().getBall();
		Vector3D ballPosition = ball.getPosition();
		Angle kickDirection = kickParams.getKickDirection();
		// do not dribble towards our own goal
		double maxAngle = Geometry.getLinearFuzzyValue(-4, 8, true, ballPosition.getX()) * 40 + 80;
		if (Math.abs(kickDirection.degrees()) > maxAngle) {
			return -1;
		}

		// do not dribble too far away from other goal
		Vector3D otherGoalPosition = getWorldModel().getOtherGoalPosition();
		Angle goalDirection = ball.getDirectionTo(otherGoalPosition);
		double delta = Math.abs((goalDirection.subtract(kickDirection)).degrees());
		if (delta > 90) {
			return -1;
		}

		// do not dribble in vicinity of the other goal
		// (dribbling into goal is unaffected, it is another dribbling routine in Attack)
		IThisPlayer thisPlayer = getWorldModel().getThisPlayer();
		if (thisPlayer.getDistanceToXY(otherGoalPosition) < 6) {
			return -1;
		}

		// do not dribble if lateral ball speed is too high
		Vector2D speed2D = Geometry.getLocalHorizontalSpeed(thisPlayer.getGlobalOrientation(), ball.getSpeed());
		if (Math.abs(speed2D.getY()) > 0.008) {
			// System.out.println("No dribbling, ball lateral speed: " + speed2D);
			return -1;
		}

		// do not dribble if not upright
		double up = thisPlayer.getOrientation().getMatrix()[2][2];
		if (up < 0.990) {
			// System.out.println("Not upright: " + up);
			return -1.0f;
		}

		// do not dribble into opponent
		// if an opponent is nearer to the ball than our own player we don't want to dribble
		IPlayer opponentAtBall = getThoughtModel().getOpponentAtBall();
		if (opponentAtBall != null) {
			double opponentDistance = opponentAtBall.getDistanceToXY(ballPosition);
			double ownDistance = getWorldModel().getThisPlayer().getDistanceToXY(ballPosition);
			if (opponentDistance <= ownDistance) {
				return -1;
			}
			// if the opponent is in front of our own player, we don't want to dribble
			Angle directionBallMe = ball.getDirectionTo(getWorldModel().getThisPlayer());
			Angle directionBallOpponent = ball.getDirectionTo(opponentAtBall.getPosition());
			double deltaDirection = Math.abs((directionBallMe.subtract(directionBallOpponent)).degrees());
			if (deltaDirection > 150 && opponentDistance <= 1) {
				return -1;
			}
		}

		return kickEstimator.getApplicability(this);
	}

	@Override
	public int getBallHitCycles()
	{
		return kickParams.getBallHitCycles();
	}

	@Override
	public KickDistribution getDistribution()
	{
		return distribution;
	}

	@Override
	public KickParameters getKickParameters()
	{
		return kickParams;
	}

	@Override
	public void setExpectedBallPosition(Vector3D pos)
	{
		kickParams.setExpectedBallPosition(pos);
	}

	@Override
	public Vector3D getExpectedBallPosition()
	{
		return kickParams.getExpectedBallPosition();
	}
}