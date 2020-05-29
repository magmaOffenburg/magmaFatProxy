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
package magma.agent.model.worldmodel;

import java.util.Map;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import hso.autonomy.agent.model.worldmodel.IMoveableObject;
import hso.autonomy.util.geometry.Angle;

/**
 * Interface to access visible player information
 * @author Klaus Dorer
 */
public interface IPlayer extends IMoveableObject {
	/**
	 * @return the player number
	 */
	int getID();

	/**
	 * @return the team name of this player
	 */
	String getTeamname();

	/**
	 * @return true if this is a player of our team
	 */
	boolean isOwnTeam();

	/**
	 * @return estimated maximum speed of this player in m/s
	 */
	double getMaxSpeed();

	/**
	 * @return true if this is the goal keeper
	 */
	boolean isGoalie();

	/**
	 * Retrieves the horizontal angle of the player with respect to the global
	 * coordinate system. The horizontal angle is basically a 2D projection of
	 * the view axis of this player.
	 *
	 * @return the horizontal angle relative to the global coordinate system
	 */
	Angle getHorizontalAngle();

	/**
	 * @return the player's global orientation relative to the global field
	 *         system
	 */
	Rotation getGlobalOrientation();

	/**
	 * Calculates the time a player is expected to take to get from its current
	 * position to the passed position heading in the passed direction
	 * @param position the destination position (global)
	 * @param directionAtTarget desired body direction (global, rad) at the
	 *        destination position
	 * @return time in seconds we estimate is needed to get to the specified
	 *         position with the specified direction
	 */
	float getTimeToTurnAndRun(Vector3D position, Angle directionAtTarget);

	/**
	 * Calculates the time a player is expected to take to get from its current
	 * position to the passed position heading in the passed direction by side
	 * stepping
	 * @param position the destination position (global)
	 * @param directionAtTarget desired body direction (global, rad) at the
	 *        destination position
	 * @param left true if we want to step left
	 * @return time in seconds we estimate is needed to get to the specified
	 *         position with the specified direction
	 */
	float getTimeForSideStep(Vector3D position, Angle directionAtTarget, boolean left);

	/**
	 * Calculates the time a player is expected to take to get from its current
	 * position to the passed position heading in the passed direction by back
	 * stepping
	 * @param position the destination position (global)
	 * @param directionAtTarget desired body direction (global, rad) at the
	 *        destination position
	 * @return time in seconds we estimate is needed to get to the specified
	 *         position with the specified direction
	 */
	float getTimeForBackStep(Vector3D position, Angle directionAtTarget);

	/**
	 * @return the map of bodyParts
	 */
	Map<String, Vector3D> getBodyParts();

	/**
	 * @return whether the player lies on the ground
	 */
	boolean isLying();

	void resetTTL();

	boolean decreaseTTL();
}