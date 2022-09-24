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
package magma.agent.decision.behavior;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.IPose2D;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public interface IKick extends IKickMovement
{
	/**
	 * Set the intended global kick direction.
	 *
	 * @param intendedKickDirection - the intended kick direction in the global
	 *        system
	 */
	void setIntendedKickDirection(Angle intendedKickDirection);

	Angle getIntendedKickDirection();

	void setIntendedKickDistance(float intendedKickDistance);

	/**
	 * Sets the global expected position of the ball at kick
	 * @param pos the global expected position of the ball at kick
	 */
	void setExpectedBallPosition(Vector3D pos);

	/**
	 * @return the global expected position of the ball at kick
	 */
	Vector3D getExpectedBallPosition();

	/**
	 * Retrieve the foot with which this kick kicks.
	 *
	 * @return the kicking foot
	 */
	SupportFoot getKickingFoot();

	/**
	 * Retrieve the pose relative to the ball and intended kick direction, to
	 * which we should navigate in order to be able to perform this kick.
	 *
	 * @return the target pose relative to the ball
	 */
	IPose2D getRelativeRunToPose();

	/**
	 * @return the speed (local coordinates of the run to pose) we want to have
	 *         at the kicking position
	 */
	Vector2D getSpeedAtRunToPose();

	/**
	 * Retrieve the absolute pose and intended kick direction, to which we should navigate in order to be able to
	 * perform this kick. Make sure that intended kick direction and expected ball position has been set to the kick
	 * before.
	 * @return the target pose relative to the ball
	 */
	IPose2D getAbsoluteRunToPose();

	/**
	 * Check if the ball is kickable with this kick into the intended kick
	 * direction set via {@link #setIntendedKickDirection(Angle)}.<br>
	 * This method works like a kind of measure how well we are currently able to
	 * perform this kick in a certain direction. A negative value indicates that
	 * the kick will certainly fail and should not be performed. Values grater
	 * than zero represent the deviation from the current to the optimal kick
	 * situation. With old kicks, this deviation is represented by the deviation
	 * from the kick direction. New kicks return a size measure of the last step
	 * that has to be performed before the kick. This way, old and new kicks can
	 * be prioritized by the same system. If some wants to mix old and new kicks,
	 * he has to ensure a proper weighting.
	 *
	 * @return < 0 if the ball is not kickable<br>
	 *         >= 0 the deviation of the optimal situation
	 */
	float getKickUtility();

	/**
	 * @return how applicable the kick is in the current situation<br>
	 *         < 0 if not applicable<br>
	 *         >= 0 the deviation of the optimal situation
	 */
	float getApplicability();

	/**
	 * @return the meta parameters of this kick
	 */
	IKickParameters getKickParameters();
}
