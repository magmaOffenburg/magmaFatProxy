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
package magma.agent.decision.behavior.ikMovement.basic;

import hso.autonomy.util.geometry.Pose6D;
import hso.autonomy.util.geometry.interpolation.pose.PoseInterpolator;
import hso.autonomy.util.geometry.interpolation.progress.CosineProgress;
import hso.autonomy.util.geometry.interpolation.value.LinearValueInterpolator;
import magma.agent.decision.behavior.SupportFoot;
import magma.agent.decision.behavior.ikMovement.IKMovementBase;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * @author Stefan Glaser
 */
public class IKCosineMovement extends IKMovementBase
{
	/** The target pose of the left foot */
	protected Pose6D leftFootTargetPose;

	/** The target pose of the right foot */
	protected Pose6D rightFootTargetPose;

	public IKCosineMovement(String name, IRoboCupThoughtModel thoughtModel, int movementCycles, int holdCycles)
	{
		this(name, thoughtModel, movementCycles, holdCycles, new Pose6D(), new Pose6D(), Vector3D.PLUS_K);
	}

	public IKCosineMovement(String name, IRoboCupThoughtModel thoughtModel, int movementCycles, int holdCycles,
			Pose6D leftFootTargetPose, Pose6D rightFootTargetPose, Vector3D targetLeaning)
	{
		this(name, thoughtModel, movementCycles, holdCycles, leftFootTargetPose, rightFootTargetPose, targetLeaning,
				false);
	}

	public IKCosineMovement(String name, IRoboCupThoughtModel thoughtModel, int movementCycles, int holdCycles,
			Pose6D leftFootTargetPose, Pose6D rightFootTargetPose, Vector3D targetLeaning, boolean isStatic)
	{
		super(name, thoughtModel, movementCycles, holdCycles);

		this.leftFootTargetPose = leftFootTargetPose;
		this.rightFootTargetPose = rightFootTargetPose;
		this.intendedLeaningVector = targetLeaning;

		supportFoot = SupportFoot.LEFT;
	}

	@Override
	protected void createTrajectoryInterpolators()
	{
		super.createTrajectoryInterpolators();

		leftFootInterpolator = new PoseInterpolator(new LinearValueInterpolator(new CosineProgress()),
				new LinearValueInterpolator(new CosineProgress()), new LinearValueInterpolator(new CosineProgress()),
				new LinearValueInterpolator(new CosineProgress()), new LinearValueInterpolator(new CosineProgress()),
				new LinearValueInterpolator(new CosineProgress()));

		rightFootInterpolator = new PoseInterpolator(new LinearValueInterpolator(new CosineProgress()),
				new LinearValueInterpolator(new CosineProgress()), new LinearValueInterpolator(new CosineProgress()),
				new LinearValueInterpolator(new CosineProgress()), new LinearValueInterpolator(new CosineProgress()),
				new LinearValueInterpolator(new CosineProgress()));
	}

	@Override
	protected void calculateMovementTrajectory()
	{
		interpolateMovement(leftFootTargetPose, rightFootTargetPose);
	}
}
