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
package magma.monitor.command;

import magma.common.spark.PlayMode;

public interface IServerCommander {
	/**
	 * Method to beam the ball to the specified position and velocity.
	 *
	 * @param x position x on field
	 * @param y position y on field
	 * @param z position z on field
	 * @param vx velocity in x-direction
	 * @param vy velocity in y-direction
	 * @param vz velocity in z-direction
	 */
	void beamBall(float x, float y, float z, float vx, float vy, float vz);

	/**
	 * Convenient-method to beam the ball on the ground to the specified 2D
	 * position.
	 *
	 * @param x - the x-position on the field
	 * @param y - the y-position on the field
	 */
	void beamBall(float x, float y);

	/**
	 * Drops the ball at its current position and move all players away by the
	 * free kick radius. If the ball is off the field, it is brought back within
	 * bounds.
	 */
	void dropBall();

	/**
	 * Give kickoff to a random team
	 */
	void kickOff();

	/**
	 * Give kickoff to the specified team.
	 *
	 * @param k - the team which gets the kickoff
	 */
	void kickOff(Team k);

	/**
	 * Move a player to a position on the field. The team and playerNumber must
	 * be specified. The RANDOM team results in no player movement.
	 *
	 * @param team - the team to which the player belongs (only Team.LEFT and
	 *        Team.RIGHT are allowed)
	 * @param playerNumber - the player number
	 * @param x - the x-position
	 * @param y - the y-position
	 * @param z - the z-position
	 */
	void movePlayer(Team team, int playerNumber, float x, float y, float z);

	/**
	 * Move a player to a position on the field. The team and playerNumber must
	 * be specified. The RANDOM team results in no player movement.
	 *
	 * @param team - the team to which the player belongs (only Team.LEFT and
	 *        Team.RIGHT are allowed)
	 * @param playerNumber - the player number
	 * @param x - the x-position
	 * @param y - the y-position
	 * @param z - the z-position
	 * @param rot - the rotation around the z-axis
	 */
	void moveRotatePlayer(Team team, int playerNumber, float x, float y, float z, float rot);

	/**
	 * Sets the current playmode of the simulation.
	 *
	 * @param playmode - the intended playmode
	 */
	void setPlaymode(PlayMode playmode);

	/**
	 * Kills the simulation server. This will most likely result in a connection
	 * loss.
	 */
	void killServer();

	/**
	 * @param msg allows to send any message to the server
	 */
	void sendMessage(String msg);

	/**
	 * Sets the game time of the server
	 * @param time the game time in seconds.
	 */
	void setTime(float time);

	/**
	 * Sets the current score of the game.
	 * @param left score of left team
	 * @param right score of right team
	 */
	void setScore(int left, int right);
}
