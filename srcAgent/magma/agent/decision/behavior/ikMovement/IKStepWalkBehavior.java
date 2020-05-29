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

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose3D;
import hso.autonomy.util.geometry.VectorUtils;
import kdo.util.parameter.ParameterMap;
import magma.agent.IHumanoidConstants;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.SupportFoot;
import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IThisPlayer;

/**
 * @author Stefan Glaser
 */
public class IKStepWalkBehavior extends IKWalkBehavior
{
	private Pose2D target;

	private Vector3D speedAtTarget;

	public IKStepWalkBehavior(IRoboCupThoughtModel thoughtModel, ParameterMap params)
	{
		super(IBehaviorConstants.IK_WALK_STEP, thoughtModel, params);
	}

	/**
	 *
	 * @param target the target position and orientation for any leg
	 * @param speedAtTarget the speed we want to have at the target
	 */
	public void setStepTarget(Pose2D target, Vector3D speedAtTarget)
	{
		this.target = target;
		this.speedAtTarget = speedAtTarget;
	}

	@Override
	public void perform()
	{
		calculateStepPlan(intendedStep);
		super.perform();
	}

	protected void calculateStepPlan(StepParameters intendedStep)
	{
		Vector3D stopSpeed = speedAtTarget.negate();
		StepParameters stopStepParams = new StepParameters();
		// initialize stopStep with speed at target
		// can we assume that walk is symmetric? That we can plan backward walk
		// and perform the same thing forward?

		Pose2D currentTarget = target;
		IThisPlayer thisPlayer = getWorldModel().getThisPlayer();
		SupportFoot supportFoot = walkMovement.supportFoot;
		String otherFootName = supportFoot == SupportFoot.LEFT ? IHumanoidConstants.LFoot : IHumanoidConstants.RFoot;
		Pose3D currentOtherFootPose = getAgentModel().getBodyPart(otherFootName).getPose();
		Pose2D origin = thisPlayer.calculateGlobalBodyPose2D(currentOtherFootPose);

		// last step with desired speed
		calculateStepParameter(stopSpeed, 0, stopStepParams, paramSetName);
		currentTarget = newPositionFromStepParams(currentTarget, stopStepParams, supportFoot);
		supportFoot = switchSupportFoot(supportFoot);

		// calculate the position to which forward run has to aim
		Angle intendedTurn;
		do {
			// calculate new position of leg and new speed
			intendedTurn = currentTarget.getAngleTo(origin).getAdjacencyAngle().negate();
			Vector3D intendedWalk = getVectorTo(currentTarget, origin);
			stopStepParams = calculateStepParameter(intendedWalk, intendedTurn.degrees(), stopStepParams, paramSetName);
			currentTarget = newPositionFromStepParams(currentTarget, stopStepParams, supportFoot);
			supportFoot = switchSupportFoot(supportFoot);

		} while (Math.abs(intendedTurn.degrees()) > 20);

		// now calculate forward from origin until facing current target
		StepParameters startStepParams = getStepParameterCopy();
		Pose2D currentPose = origin;
		do {
			// calculate new position of leg and new speed
			intendedTurn = currentPose.getAngleTo(currentTarget);
			Vector3D intendedWalk = getVectorTo(currentPose, currentTarget);
			startStepParams =
					calculateStepParameter(intendedWalk, intendedTurn.degrees(), startStepParams, paramSetName);
			currentPose = newPositionFromStepParams(currentPose, startStepParams, supportFoot);
			supportFoot = switchSupportFoot(supportFoot);

		} while (Math.abs(intendedTurn.degrees()) > 20);

		// now calculate the step params that will lead to the current target
		IKWalkMovementParametersBase walkParams = walkMovement.getWalkParameters();
		// double distance = origin.getDistanceTo(currentTarget);
		Vector3D intendedWalk = getVectorTo(currentPose, currentTarget);

		intendedWalk = thisPlayer.getOrientation().applyInverseTo(intendedWalk);
		double y = intendedWalk.getY();
		double x = intendedWalk.getX();
		int stepsForward = (int) (y / walkParams.getMaxStepLength());

		if (stepsForward > 2) {
			// intendedStep.y_targetDistance = walkParams.getMaxStepLength();
			intendedStep.yTargetDistance = y / stepsForward;
			intendedStep.xTargetDistance = x / stepsForward;
		} else if (stepsForward > 0) {
			intendedStep.yTargetDistance = y / stepsForward;
			intendedStep.xTargetDistance = x / stepsForward;
		} else {
			System.out.println(" should not get here ");
			intendedStep.yTargetDistance = y / stepsForward;
			intendedStep.xTargetDistance = x / stepsForward;
		}

		// do we need turnPriority for calculateStepParameter?
		// what if backward calculation ends 'behind' origin?
	}

	private SupportFoot switchSupportFoot(SupportFoot supportFoot)
	{
		return (supportFoot == SupportFoot.LEFT) ? SupportFoot.RIGHT : SupportFoot.LEFT;
	}

	private Pose2D newPositionFromStepParams(
			Pose2D currentTarget, StepParameters stopStepParams, SupportFoot supportFoot)
	{
		return calculateFreeFootPose(currentTarget, supportFoot, stopStepParams);
	}

	private Vector3D getVectorTo(Pose2D source, Pose2D destination)
	{
		return VectorUtils.to3D(destination.getPosition().subtract(source.getPosition()));
	}
}
