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

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import hso.autonomy.agent.decision.behavior.IBehavior;

/**
 * Interface for all underlying basic walk behaviors
 * @author kdorer
 */
public interface IBaseWalk extends IBehavior {
	/**
	 * Instrumentation for the walk
	 * @param forwardsBackwards percentage of forward backward speed (-100 is
	 *        backward)
	 * @param stepLeftRight percentage of sideward speed (-100 is right)
	 * @param turnLeftRight degrees to turn (-values turn right)
	 */
	void setMovement(double forwardsBackwards, double stepLeftRight, double turnLeftRight);

	/**
	 * Instrumentation for the walk
	 * @param forwardsBackwards percentage of forward backward speed (-100 is
	 *        backward)
	 * @param stepLeftRight percentage of sideward speed (-100 is right)
	 * @param turnLeftRight degrees to turn (-values turn right)
	 * @param paramSetName the name of the walk parameter set to use
	 */
	void setMovement(double forwardsBackwards, double stepLeftRight, double turnLeftRight, String paramSetName);

	/**
	 * @return the maximal angle to turn in one step
	 */
	double getMaxTurnAngle();

	/**
	 * @return the desired x-y walk speed (relative to max speed)
	 */
	Vector2D getIntendedWalk();

	/**
	 * @return the current x-y walk speed (in m/s)
	 */
	Vector2D getCurrentSpeed();

	/**
	 * @return the intended turn angle in degrees
	 */
	float getIntendedTurn();

	/**
	 * @return the real turn angle in degrees
	 */
	float getCurrentTurn();

	/**
	 * @return true if this is the start of a new step
	 */
	boolean isNewStep();
}
