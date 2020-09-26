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
package magma.agent.decision.behavior.ikMovement.walk;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose3D;
import kdo.util.parameter.IParameterList;
import magma.agent.IHumanoidConstants;
import magma.agent.decision.behavior.SupportFoot;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IThisPlayer;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class IKFinalBallStepMovement extends IKCoMShiftingStepMovement
{
	/** The intended kick direction in the global system */
	protected Angle intendedKickDirection;

	/**
	 * The target pose of the stabilizing foot relative to the ball and intended
	 * kick direction. This pose is used to calculate the parameters of the final
	 * step in order to place the stabilizing foot accurately before kicking.<br>
	 * (The pose is interpreted in the global system)
	 */
	protected final Pose2D relativeStabilizingFootPose;

	private Pose2D targetBallPose;

	public IKFinalBallStepMovement(IRoboCupThoughtModel thoughtModel, Pose2D relativeStabilizingFootPose,
			SupportFoot supportFoot, IParameterList params)
	{
		super(thoughtModel, params);

		this.relativeStabilizingFootPose = relativeStabilizingFootPose;
		this.supportFoot = supportFoot;
	}

	public void setIntendedKickDirection(Angle intendedKickDirection)
	{
		this.intendedKickDirection = intendedKickDirection;
	}

	public Angle getIntendedKickDirection()
	{
		return intendedKickDirection;
	}

	@Override
	public boolean update()
	{
		if (isFinished) {
			return false;
		}

		// recalculate the target pose in the first 6 cycles of the step
		if (cycleProgress > 0 && cycleProgress <= 6) {
			calculateMovementTrajectory();
		}

		return super.update();
	}

	@Override
	protected void calculateMovementTrajectory()
	{
		targetPose.copy(targetBallPose);
		super.calculateMovementTrajectory();
	}

	/**
	 * Called to calculate the target pose of the free foot relative to the
	 * support foot.
	 *
	 * @return the target pose for the free leg in the final step
	 */
	public Pose2D calculateStabilizationLegTargetPose(Vector3D ballPos)
	{
		Pose2D ballHitPose = new Pose2D(ballPos, intendedKickDirection);

		// transform the ball-relative target pose of the stabilizing foot into
		// the global system
		Pose2D globalStabilizingFootTargetPose = ballHitPose.applyTo(relativeStabilizingFootPose);

		// fetch the current pose of the not stabilizing foot and transform it
		// into the global system
		IThisPlayer thisPlayer = worldModel.getThisPlayer();
		String otherFootName = supportFoot == SupportFoot.LEFT ? IHumanoidConstants.LFoot : IHumanoidConstants.RFoot;
		Pose3D currentOtherFootPose = agentModel.getBodyPart(otherFootName).getPose();
		Pose2D globalOtherFootPose = thisPlayer.calculateGlobalBodyPose2D(currentOtherFootPose);

		// transform the global target pose of the stabilizing foot into the other
		// foot system
		Pose2D targetPose = globalOtherFootPose.applyInverseTo(globalStabilizingFootTargetPose);

		targetBallPose = new Pose2D(-targetPose.getY(), targetPose.getX(), //
				targetPose.getAngle());
		return targetBallPose;
	}
}
