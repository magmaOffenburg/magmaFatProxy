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

import java.io.Serializable;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import hso.autonomy.util.geometry.Pose6D;

public class MovementTrajectories implements Serializable
{
	/** the target poses to the left foot in the CoM-System */
	Pose6D[] leftFootTrajectory;

	/** the target poses to the right foot in the CoM-System */
	Pose6D[] rightFootTrajectory;

	/** the target poses to the left arm in the torso-System */
	Pose6D[] leftArmTrajectory;

	/** the target poses to the right arm in the torso-System */
	Pose6D[] rightArmTrajectory;

	/** the target adjustment factors */
	Vector2D[] adjustmentFactorTrajectory;

	public MovementTrajectories(int movementCycles)
	{
		leftFootTrajectory = new Pose6D[movementCycles];
		leftArmTrajectory = new Pose6D[movementCycles];
		rightFootTrajectory = new Pose6D[movementCycles];
		rightArmTrajectory = new Pose6D[movementCycles];
		adjustmentFactorTrajectory = new Vector2D[movementCycles];
	}

	public Pose6D[] getLeftFootTrajectory()
	{
		return leftFootTrajectory;
	}

	public Pose6D[] getRightFootTrajectory()
	{
		return rightFootTrajectory;
	}

	public Pose6D[] getLeftArmTrajectory()
	{
		return leftArmTrajectory;
	}

	public Pose6D[] getRightArmTrajectory()
	{
		return rightArmTrajectory;
	}

	public Vector2D[] getAdjustmentFactorTrajectory()
	{
		return adjustmentFactorTrajectory;
	}

	public void set(MovementTrajectories movementTrajectories)
	{
		leftFootTrajectory = movementTrajectories.leftFootTrajectory;
		leftArmTrajectory = movementTrajectories.leftArmTrajectory;
		rightFootTrajectory = movementTrajectories.rightFootTrajectory;
		rightArmTrajectory = movementTrajectories.rightArmTrajectory;
		adjustmentFactorTrajectory = movementTrajectories.adjustmentFactorTrajectory;
	}
}