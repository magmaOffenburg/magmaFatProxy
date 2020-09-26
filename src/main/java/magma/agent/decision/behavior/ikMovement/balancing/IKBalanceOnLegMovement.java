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
package magma.agent.decision.behavior.ikMovement.balancing;

import hso.autonomy.util.geometry.Pose6D;
import magma.agent.decision.behavior.SupportFoot;
import magma.agent.decision.behavior.ikMovement.IIKMovement;
import magma.agent.decision.behavior.ikMovement.basic.IKCosineMovement;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * @author Stefan Glaser
 */
public class IKBalanceOnLegMovement extends IKCosineMovement
{
	/** The target pose of the support foot */
	protected Pose6D supportFootTargetPose;

	/** The target pose of the free foot */
	protected Pose6D freeFootTargetPose;

	public IKBalanceOnLegMovement(IRoboCupThoughtModel thoughtModel)
	{
		this(thoughtModel, new Pose6D(), new Pose6D(), Vector3D.PLUS_K);
	}

	public IKBalanceOnLegMovement(IRoboCupThoughtModel thoughtModel, Pose6D supportFootTargetPose,
			Pose6D freeFootTargetPose, Vector3D targetLeaning)
	{
		this(thoughtModel, supportFootTargetPose, freeFootTargetPose, targetLeaning, 18, 0);
	}

	public IKBalanceOnLegMovement(IRoboCupThoughtModel thoughtModel, Pose6D supportFootTargetPose,
			Pose6D freeFootTargetPose, Vector3D targetLeaning, int movementCycles, int holdCycles)
	{
		super("BalanceOnLegMovement", thoughtModel, movementCycles, holdCycles);

		this.supportFootTargetPose = supportFootTargetPose;
		this.freeFootTargetPose = freeFootTargetPose;
		this.intendedLeaningVector = targetLeaning;

		isStatic = false;
	}

	@Override
	public void init(IIKMovement other)
	{
		if (other != null) {
			// If we are morphing from an existing movement, determine the next
			// support foot based on the previously performed movement. Otherwise
			// keep the current support foot (since it may have been set from
			// outside).
			if (other.getNextSupportFoot() == SupportFoot.LEFT) {
				supportFoot = SupportFoot.LEFT;
			} else {
				supportFoot = SupportFoot.RIGHT;
			}
		}

		// // HACK //////////////////////////////////////////////////
		// if (supportFoot == SupportFoot.LEFT) {
		// freeFootTargetPose = new Pose6D(0.075, -0.16, -0.17f, -70, 0, 0);
		// supportFootTargetPose = new Pose6D(-0.015, 0.02, -0.3);
		// intendedLeaningVector = new Rotation(Vector3D.PLUS_J,
		// Math.toRadians(-12)).applyTo(Vector3D.PLUS_K);
		// } else {
		// freeFootTargetPose = new Pose6D(-0.075, -0.16, -0.17f, -70, 0, 0);
		// supportFootTargetPose = new Pose6D(0.015, 0.02, -0.3);
		// intendedLeaningVector = new Rotation(Vector3D.PLUS_J,
		// Math.toRadians(12)).applyTo(Vector3D.PLUS_K);
		// }
		// // END HACK //////////////////////////////////////////////

		if (supportFoot == SupportFoot.LEFT) {
			leftFootTargetPose.set(supportFootTargetPose);
			rightFootTargetPose.set(freeFootTargetPose);
		} else {
			leftFootTargetPose.set(freeFootTargetPose);
			rightFootTargetPose.set(supportFootTargetPose);
		}

		super.init(other);
	}

	public void setTargets(Pose6D supportFootTargetPose, Pose6D freeFootTargetPose, Vector3D intendedLeaning)
	{
		this.supportFootTargetPose.set(supportFootTargetPose);
		this.freeFootTargetPose.set(freeFootTargetPose);
		this.intendedLeaningVector = intendedLeaning;
	}
}
