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

import hso.autonomy.agent.model.worldmodel.IVisibleObject;
import hso.autonomy.agent.model.worldmodel.IWorldModel;
import java.util.List;
import magma.common.spark.PlayMode;
import magma.common.spark.PlaySide;
import magma.common.spark.TeamColor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Represents all environment data available to the agent
 */
public interface IRoboCupWorldModel extends IWorldModel, ISoccerPitchDescription {
	public enum BallPassing { CENTER, SHORT_LEFT, SHORT_RIGHT, FAR_LEFT, FAR_RIGHT, UNREACHABLE }

	/**
	 * Get a reference to the ball object
	 *
	 * @return The ball object
	 */
	IBall getBall();

	/**
	 * @return the goal post landmarks used as obstacles (do not exactly match
	 *         goal posts)
	 */
	List<IVisibleObject> getGoalPostObstacles();

	/**
	 * Get a list of all visible players
	 *
	 * @return An unmodifiable list of the currently visible players
	 */
	List<IPlayer> getVisiblePlayers();

	/**
	 * Get a reference to the player object of this player
	 *
	 * @return This players information of position etc
	 */
	IThisPlayer getThisPlayer();

	/**
	 * Get the running time of the current game
	 *
	 * @return the time the game is running now
	 */
	float getGameTime();

	TeamColor getTeamColor();

	PlayMode getPlaymode();

	PlaySide getPlaySide();

	/**
	 * Get the current play mode
	 * @return the current mode of the game as String
	 */
	GameState getGameState();

	/**
	 * Get the number of goals our own team scored
	 *
	 * @return number of goals scored by us
	 */
	int getGoalsWeScored();

	/**
	 * Get the number of goals the opponent team scored
	 *
	 * @return number of goals scored by opponent
	 */
	int getGoalsTheyScored();

	/**
	 * @return the serverVersion used for playing
	 */
	int getServerVersion();

	/**
	 * @param id the player's number (1-11)
	 * @param ownTeam true if we want the player of our own team
	 * @return the referenced player, null if not in list
	 */
	IPlayer getVisiblePlayer(int id, boolean ownTeam);

	/**
	 * Checks whether the ball is dangerously close to the own goal. If this is
	 * the case, the goalie runs to the ball.
	 */
	boolean isBallInCriticalArea();

	/**
	 * Checks if the ball is passing through us, slightly next to us or reachable with arms
	 * @param futureBallPosition the position where we expect the ball to be
	 * @return different conditions how the ball may pass us
	 */
	BallPassing ballIsPassing(Vector3D futureBallPosition);

	/**
	 * Checks if a line between the two passed points intersects the goal line between the goal posts
	 * @param point1 first point of the line to check
	 * @param point2 second point of the line to check
	 * @param offset distance to stay away from goal post
	 * @return the intersection point, null if they do not intersect
	 */
	Vector2D goalLineIntersection(Vector2D point1, Vector2D point2, double offset);

	enum GameMode
	{
		DEFAULT,
		PENALTY
	}

	void setGameMode(GameMode mode);
}