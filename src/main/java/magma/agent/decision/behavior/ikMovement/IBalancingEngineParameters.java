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

/**
 * Interface for parameters related to the {@link BalancingEngine}.
 *
 * @author Stefan Glaser
 */
public interface IBalancingEngineParameters
{
	/**
	 * The intended leaning vector the balancing engine is adjusting to.
	 *
	 * @return the intended leaning vector
	 */
	Vector3D getIntendedLeaningVector();

	/**
	 * The pivot point of the adjustment. This position is relative to the torso
	 * system and describes around which local point the target poses are rotated
	 * by the adjustment of the {@link BalancingEngine}.<br>
	 * In the general case, this position is defined as the current estimation of
	 * the Center of Mass of the robot. By using the current CoM estimation, the
	 * target poses are also automatically adjusted to CoM shifts, forcing the
	 * origin of the motion system to match the local CoM position.<br>
	 * However, by supplying a static local pivot point, e.g. in the pelvis of
	 * the robot, a behavior can command the {@link BalancingEngine} to produce a
	 * static adjustment.
	 *
	 * @return the torso-local pivot point (CoM in the dynamic case or an
	 *         arbitrary determined position in the static case)
	 */
	Vector3D getPivotPoint();

	/**
	 * The adjustment factor for the saggital (x-) axis, in a range from 0 to 1
	 * for 0% to 100% adjustment to the intended leaning in forward/backward
	 * direction.
	 *
	 * @return the adjustment factor for the saggital axis
	 */
	float getSaggitalAdjustmentFactor();

	/**
	 * The maximum absolute allowed adjustment angle for the saggital (x-) axis
	 * in degrees.
	 *
	 * @return the maximum absolute allowed saggital adjustment angle
	 */
	float getMaxAbsSaggitalAdjustment();

	/**
	 * The adjustment factor for the coronal (y-) axis, in a range from 0 to 1
	 * for 0% to 100% adjustment to the intended leaning in sideways (left/right)
	 * direction.
	 *
	 * @return the adjustment factor for the coronal axis
	 */
	float getCoronalAdjustmentFactor();

	/**
	 * The maximum absolute allowed adjustment angle for the coronal (y-) axis in
	 * degrees.
	 *
	 * @return the maximum absolute allowed coronal adjustment angle
	 */
	float getMaxAbsCoronalAdjustment();
}