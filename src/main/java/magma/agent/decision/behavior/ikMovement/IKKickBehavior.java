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
package magma.agent.decision.behavior.ikMovement;

import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Area2D;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose6D;
import kdo.util.parameter.IParameterList;
import kdo.util.parameter.ParameterMap;
import magma.agent.decision.behavior.IBehaviorConstants.SidedBehaviorConstants;
import magma.agent.decision.behavior.IKick;
import magma.agent.decision.behavior.IWalkEstimator;
import magma.agent.decision.behavior.SupportFoot;
import magma.agent.decision.behavior.base.KickDistribution;
import magma.agent.decision.behavior.base.KickEstimator;
import magma.agent.decision.behavior.base.KickParameters;
import magma.agent.decision.behavior.ikMovement.KickMovementParameters.Param;
import magma.agent.decision.behavior.ikMovement.balancing.IKBalanceOnLegMovement;
import magma.agent.decision.behavior.ikMovement.walk.IKFinalBallStepMovement;
import magma.agent.decision.behavior.ikMovement.walk.IKGetOnLegStepMovement;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * @author Stefan Glaser
 */
public class IKKickBehavior extends IKMovementBehaviorBase implements IKick
{
	/** The movement for the final step. */
	protected final IKFinalBallStepMovement finalStepMvmt;

	/** The movement to get on one leg after the final step */
	protected final IKGetOnLegStepMovement getOnLegMvmt;

	/**
	 * The movement to balance on the leg, which we just got on via the
	 * {@link #getOnLegMvmt} and to reach the balancing target posture.
	 */
	protected final IKBalanceOnLegMovement balanceOnLegMvmt;

	private final float minXOffset;

	/**
	 * The movement which finally kicks, after we successfully balanced on one
	 * leg and reached the initial posture.
	 */
	protected IIKMovement kickMvmt;

	/** kick specific parameters that define the kick setup */
	private transient KickParameters kickParams;

	private transient KickEstimator kickEstimator;

	/**
	 * @param name The name of the kick behavior.
	 * @param thoughtModel the thought model.
	 * @param kickingFoot The kicking foot.
	 * @param relativeSupportFootPose The target pose of the stabilizing foot
	 *        relative to the ball and intended kick direction. This pose is used
	 *        to calculate the parameters of the final step in order to place the
	 *        stabilizing foot accurately before kicking. (global system)
	 * @param supportFootBalancingPose The target balancing pose of the support
	 *        foot. This pose is used as target pose for the support foot during
	 *        the balancing process (after the final step is performed). Together
	 *        with the freeFootBalancingPose and the balancingLeaning vector,
	 *        this pose defines an initial body posture.
	 * @param freeFootBalancingPose The target balancing pose of the free foot.
	 *        This pose is used as target pose for the free foot during the
	 *        balancing process (after the final step is performed). Together
	 *        with the supportFootBalancingPose and the balancingLeaning vector,
	 *        this pose defines an initial body posture.
	 * @param balancingLeaning The target balancing leaning. This leaning vector
	 *        is used as the intended leaning during the balancing process (after
	 *        the final step is performed). Together with the
	 *        supportFootBalancingPose and the freeFootBalancingPose, this vector
	 *        defines an initial body posture.
	 * @param relativeRunToPose The pose relative to the ball and intended kick
	 *        direction, to which we should navigate in order to be able to
	 *        perform this kick. (global system)
	 * @param relativeKickDirection The relative kick direction when stabilized
	 *        (used to rotate the relativeSupportFootPose and relativeRunToPose
	 *        with respect to the relative kick direction)
	 * @param maxKickDistance The maximum kick distance.
	 * @param kickMvmt The kick movement.
	 * @param stabilizeTime time for stabilization phase in cycles
	 */
	public IKKickBehavior(String name, IRoboCupThoughtModel thoughtModel, SupportFoot kickingFoot,
			Pose2D relativeSupportFootPose, Pose6D supportFootBalancingPose, Pose6D freeFootBalancingPose,
			Vector3D balancingLeaning, Pose2D relativeRunToPose, Angle relativeKickDirection, double maxKickDistance,
			IIKMovement kickMvmt, int stabilizeTime, float opponentMinDistance, int ballHitCycles, float minXOffset,
			IParameterList params, IWalkEstimator walkEstimator)
	{
		super(name, thoughtModel);
		this.minXOffset = minXOffset;

		kickParams = new KickParameters(kickingFoot, relativeRunToPose, new Vector2D(0.02, 0), relativeKickDirection,
				Angle.ZERO, maxKickDistance, maxKickDistance, opponentMinDistance,
				KickParameters.DEFAULT_MAX_OPP_DISTANCE, 0.007f, ballHitCycles, 1000);

		Pose2D kickDirPose = new Pose2D(0, 0, relativeKickDirection);

		this.kickMvmt = kickMvmt;
		finalStepMvmt = new IKFinalBallStepMovement(
				thoughtModel, kickDirPose.applyInverseTo(relativeSupportFootPose), kickingFoot, params);
		getOnLegMvmt = new IKGetOnLegStepMovement(thoughtModel, params);
		balanceOnLegMvmt = new IKBalanceOnLegMovement(
				thoughtModel, supportFootBalancingPose, freeFootBalancingPose, balancingLeaning, stabilizeTime, 0);
		kickEstimator = new KickEstimator(thoughtModel, walkEstimator);
	}

	@Override
	public void init()
	{
		super.init();
		currentMovement = null;
	}

	@Override
	public void setIntendedKickDirection(Angle intendedKickDirection)
	{
		kickParams.setIntendedKickDirection(intendedKickDirection);
		kickParams.setKickDirection(intendedKickDirection);
	}

	@Override
	public Angle getIntendedKickDirection()
	{
		return kickParams.getIntendedKickDirection();
	}

	@Override
	public void setIntendedKickDistance(float intendedKickDistance)
	{
		kickParams.setIntendedKickDistance(intendedKickDistance);
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
		return kickEstimator.getAbsoluteRunToPose(this);
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

	@Override
	protected IIKMovement decideNextMovement()
	{
		IIKMovement nextMovement;

		if (currentMovement == null) {
			// Do final step
			finalStepMvmt.setSupportFoot(kickParams.getKickingFoot());
			finalStepMvmt.setIntendedKickDirection(kickParams.getIntendedKickDirection());

			nextMovement = finalStepMvmt;
		} else if (currentMovement == finalStepMvmt) {
			// Do get on leg
			nextMovement = getOnLegMvmt;
		} else if (currentMovement == getOnLegMvmt) {
			// Do balance on leg
			nextMovement = balanceOnLegMvmt;
		} else if (kickMvmt != null) {
			// Do kick
			nextMovement = kickMvmt;
		} else {
			nextMovement = currentMovement;
		}

		return nextMovement;
	}

	@Override
	public boolean isFinished()
	{
		if (currentMovement == null || !currentMovement.isFinished()) {
			return false;
		}

		return (currentMovement == kickMvmt) || (kickMvmt == null && currentMovement == balanceOnLegMvmt);
	}

	@Override
	public float getKickUtility()
	{
		// TODO: Not nice! Again it is not nice that we have to call getKickUtility() for its side effects
		// We have situations in which the FatProxy would return here while the Thin client wants a kick!
		//		if (kickEstimator.getKickUtility(this) < 0) {
		//			return -1;
		//		}

		// calculate stabilizing foot target pose
		finalStepMvmt.setSupportFoot(kickParams.getKickingFoot());
		finalStepMvmt.setIntendedKickDirection(kickParams.getKickDirection());
		Pose2D targetPose =
				finalStepMvmt.calculateStabilizationLegTargetPose(kickEstimator.getBallPosAtKick(kickParams));

		final int MIN_ANGLE = 5;
		final int MAX_ANGLE = 45;
		boolean isKickingFootLeft = kickParams.getKickingFoot() == SupportFoot.LEFT;
		int maxAngle = isKickingFootLeft ? MIN_ANGLE : MAX_ANGLE;
		int minAngle = isKickingFootLeft ? -MAX_ANGLE : -MIN_ANGLE;

		if (targetPose.getAngle().degrees() > maxAngle || targetPose.getAngle().degrees() < minAngle) {
			// If the turn degrees are outside the allowed range, we can't reach
			// the preferred position exactly so we can't kick here
			return -1;
		}

		// Create a polygon describing the area of reachable positions based on
		// the intended turn angle
		double MAX_HALF_STEP_WIDTH = 0.03;
		double MAX_STEP_LENGTH = 0.07;
		int sideFactor = isKickingFootLeft ? 1 : -1;
		double angleFactor = Math.abs(targetPose.getAngle().degrees()) / MAX_ANGLE;

		float minX = (float) ((sideFactor * minXOffset) - MAX_HALF_STEP_WIDTH);
		float maxX = (float) ((sideFactor * minXOffset) + MAX_HALF_STEP_WIDTH);
		float minY = (float) -MAX_STEP_LENGTH;
		float maxY = (float) (minY + Math.abs(minY) * (2 - angleFactor));

		Area2D.Float kickableArea = new Area2D.Float(minX, maxX, minY, maxY);
		if (kickableArea.contains(targetPose.getPosition())) {
			// If the last step is within the range we think is accurate and
			// stable, return the sum of absolute distances to the optimal last
			// step as utility value (the rotation is weighted by 0.1 times the
			// radian angle deviation; this weights a 60 degrees angle deviation
			// roughly equal to a 10 cm position deviation)
			return kickParams.getPriority() +
					(float) (Math.abs(Math.abs(targetPose.getX()) - 0.11) + Math.abs(targetPose.getY()) +
							 Math.abs(targetPose.angle.radians() / 10));
		}

		// if it wasn't possible to kick the ball until here, it'll never be the
		// case and thus return a negative value
		return -1;
	}

	@Override
	public float getApplicability()
	{
		return kickEstimator.getApplicability(this);
	}

	@Override
	public int getBallHitCycles()
	{
		// not implemented, we currently only use the stabilization part of
		// IKKickBehavior anyway
		return 0;
	}

	@Override
	public KickDistribution getDistribution()
	{
		return null;
	}

	public int getStabilizeCycles()
	{
		return finalStepMvmt.getMovementCycles() + getOnLegMvmt.getMovementCycles() +
				balanceOnLegMvmt.getMovementCycles();
	}

	@Override
	public IBehavior switchFrom(IBehavior actualBehavior)
	{
		IBehavior realBehavior = actualBehavior.getRootBehavior();

		if (realBehavior instanceof IKWalkBehavior) {
			SupportFoot nextSupportFoot = ((IKWalkBehavior) realBehavior).getCurrentMovement().getNextSupportFoot();

			if (realBehavior.isFinished() && nextSupportFoot == kickParams.getKickingFoot()) {
				double upright = getWorldModel().getThisPlayer().getOrientation().getMatrix()[2][2];
				if (upright > 0.99) {
					actualBehavior.onLeavingBehavior(this);
					return this;
				} else {
					return actualBehavior;
				}
			} else {
				return actualBehavior;
			}
		}

		return super.switchFrom(actualBehavior);
	}

	public static IKKickBehavior getKickStabilizationLeft(String kickName, SidedBehaviorConstants stabilize,
			IRoboCupThoughtModel thoughtModel, ParameterMap params, float opponentMinDistance, float maxKickDistance,
			IWalkEstimator walkEstimator)
	{
		KickMovementParameters kickParams = (KickMovementParameters) params.get(kickName);
		StabilizeParams stabilizeParams = StabilizeParams.getLeftKickStraightParams();

		return new IKKickBehavior(stabilize.LEFT, thoughtModel, SupportFoot.LEFT,
				new Pose2D(kickParams.getPosY(), -kickParams.getPosX()),
				new Pose6D(stabilizeParams.supportFootStabilizationPosition),
				new Pose6D(stabilizeParams.freeFootTargetPosition, stabilizeParams.freeFootTargetAngles),
				new Rotation(RotationOrder.XYZ, RotationConvention.VECTOR_OPERATOR,
						Math.toRadians(stabilizeParams.intendedTargetLeaningForwards),
						Math.toRadians(stabilizeParams.intendedTargetLeaningSidewards), 0)
						.applyTo(Vector3D.PLUS_K),
				new Pose2D(kickParams.get(Param.RUN_TO_X), -kickParams.get(Param.RUN_TO_Y)),
				Angle.deg(-kickParams.getKickAngle()), maxKickDistance, null,
				(int) kickParams.get(Param.STABILIZE_TIME), opponentMinDistance, 1, kickParams.get(Param.MIN_X_OFFSET),
				params.get(stabilize.BASE_NAME), walkEstimator);
	}

	public static IKKickBehavior getKickStabilizationRight(String kickName, SidedBehaviorConstants stabilize,
			IRoboCupThoughtModel thoughtModel, ParameterMap params, float opponentMinDistance, float maxKickDistance,
			IWalkEstimator walkEstimator)
	{
		KickMovementParameters kickParams = (KickMovementParameters) params.get(kickName);
		StabilizeParams stabilizeParams = StabilizeParams.getRightKickStraightParams();

		return new IKKickBehavior(stabilize.RIGHT, thoughtModel, SupportFoot.RIGHT,
				new Pose2D(kickParams.getPosY(), kickParams.getPosX()),
				new Pose6D(stabilizeParams.supportFootStabilizationPosition),
				new Pose6D(stabilizeParams.freeFootTargetPosition, stabilizeParams.freeFootTargetAngles),
				new Rotation(RotationOrder.XYZ, RotationConvention.VECTOR_OPERATOR,
						Math.toRadians(stabilizeParams.intendedTargetLeaningForwards),
						Math.toRadians(stabilizeParams.intendedTargetLeaningSidewards), 0)
						.applyTo(Vector3D.PLUS_K),
				new Pose2D(kickParams.get(Param.RUN_TO_X), kickParams.get(Param.RUN_TO_Y)),
				Angle.deg(kickParams.getKickAngle()), maxKickDistance, null, (int) kickParams.get(Param.STABILIZE_TIME),
				opponentMinDistance, 1, kickParams.get(Param.MIN_X_OFFSET), params.get(stabilize.BASE_NAME),
				walkEstimator);
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
