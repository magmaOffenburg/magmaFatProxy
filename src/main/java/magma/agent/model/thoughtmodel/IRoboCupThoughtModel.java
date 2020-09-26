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
package magma.agent.model.thoughtmodel;

import hso.autonomy.agent.model.agentmodel.IAgentModel;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.agent.model.worldmodel.IVisibleObject;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.logging.PropertyMap;
import java.util.List;
import java.util.Map;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.flags.IFlagModel;
import magma.agent.model.thoughtmodel.impl.KickPositionEstimation;
import magma.agent.model.thoughtmodel.impl.KickPositionProfiler;
import magma.agent.model.thoughtmodel.impl.ProxyCommand;
import magma.agent.model.thoughtmodel.strategy.IRole;
import magma.agent.model.thoughtmodel.strategy.IRoleManager;
import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.monitor.general.impl.MonitorRuntime;
import magma.util.roboviz.RoboVizDraw;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Represents all information related to thoughts of the agent.<br>
 * While the {@link hso.autonomy.agent.model.worldmodel.IWorldModel} and {@link IAgentModel}
 * represent the measured states based on sensor information, the IThoughtModel
 * is meant to represent a higher level of information abstraction. Furthermore,
 * it is the central model to store all information that comes from inside the
 * agent and cannot be measured.<br>
 * To give a simple example: <i>IFOCalculator</i>:<br>
 * The location information for each player is a measured value and contained in
 * the {@link hso.autonomy.agent.model.worldmodel.IWorldModel}. The question which player is
 * closest to the ball can be calculated based on this information but
 * represents a thought which is not directly related to a sensor value. This
 * "thought" (which player is closest to the ball) can be integrated and
 * buffered in the IThoughtModel, which improves reusability of common
 * calculations and therefore reduces runtime.
 *
 * @author Stefan Glaser
 */
public interface IRoboCupThoughtModel extends IThoughtModel {
	@Override
	IRoboCupAgentModel getAgentModel();

	@Override
	IRoboCupWorldModel getWorldModel();

	MonitorRuntime getMonitorRuntime();

	IFlagModel getFlags();

	RoboVizDraw getRoboVizDraw();

	void log(String name, Object value);

	void disableLogging();

	PropertyMap getProperties();

	Angle getIntendedKickDirection();

	float getIntendedKickDistance();

	List<KickPositionEstimation> getKickOptions();

	Vector3D getDesiredKickPosition();

	void setKickPositionProfiler(KickPositionProfiler kickPositionProfiler);

	IRoleManager getRoleManager();

	/**
	 * @return the current role of the agent
	 */
	IRole getRole();

	boolean shouldBeam();

	Pose2D getHomePose();

	/**
	 * @return the opponent that is closest to ball
	 */
	IPlayer getOpponentAtBall();

	/**
	 * Get the team-mate which is closest to the ball
	 *
	 * @return the team-mate that is closest to the ball, null if we see no
	 *         team-mates
	 */
	IPlayer getTeammateAtBall();

	/**
	 * Get the player of the own team (including myself) which is closest to the
	 * ball
	 *
	 * @return the own player that is closest to the ball, ourself if we see no
	 *         team-mates
	 */
	IPlayer getClosestOwnPlayerAtBall();

	boolean isClosestToBall();

	boolean isOpponentNearBall();

	boolean isBallVisible();

	boolean isInSoccerPosition();

	/**
	 * @return the obstacles to avoid
	 */
	List<IVisibleObject> getObstacles();

	/**
	 * Get a list of all players, sorted by distance from the own position
	 *
	 * @return a list of all players sorted by the distance to me
	 */
	List<IPlayer> getPlayersAtMeList();

	/**
	 * Get a list of all opponents, sorted by distance from the own position
	 *
	 * @return a list of all opponents sorted by the distance to me
	 */
	List<IPlayer> getOpponentsAtMeList();

	/**
	 * Get a list of all opponents, sorted by distance from the ball position
	 *
	 * @return a list of all opponents sorted by the distance to ball
	 */
	List<IPlayer> getOpponentsAtBallList();

	/**
	 * Get a list of all team-mates, sorted by distance from the ball position
	 *
	 * @return list of team-mates
	 */
	List<IPlayer> getTeammatesAtBall();

	/**
	 * Get a list of all players, sorted by distance from the ball position
	 *
	 * @return List of players
	 */
	List<IPlayer> getPlayersAtBallList();

	String getCameraTilt();

	/**
	 * Set the command for the agent head.
	 *
	 * @param command - the command for the head
	 */
	void setHeadCommand(String command);

	/**
	 * @return the command currently performed by the head
	 */
	String getHeadCommand();

	/**
	 * Set the intended view direction.<br>
	 *
	 * @param horizontalAngle - the horizontal view angle
	 * @param verticalAngle - the vertical view angle
	 */
	void setIntendedViewDirection(Angle horizontalAngle, Angle verticalAngle);

	/**
	 * Set the command for the agent body.
	 *
	 * @param command - the command for the body
	 */
	void setMovementCommand(String command);

	/**
	 * Set the command for the text-to-speech server.
	 *
	 * @param command - the command for the TTS server
	 * @param sayParams - the say command parameters
	 */
	void setSayCommand(String command, Map<String, String> sayParams);

	/**
	 * Set the intended walk parameters.
	 *
	 * @param intendedX - the intended sidewards speed
	 * @param intendedY - the intended forwards speed
	 * @param intendedTurn - the intended turn angle
	 */
	void setIntendedWalk(double intendedX, double intendedY, double intendedTurn);

	Vector3D getIntendedWalk();

	/**
	 * Set the intended kick parameters.
	 *
	 * @param relBallKickPos - the ball position relative to the support foot
	 * @param relBallKickTargetPos - the kick target position relative to the ball
	 */
	void setIntendedKick(Vector2D relBallKickPos, Vector2D relBallKickTargetPos);

	/**
	 * Checks if the ball is in a dribbling position in front of the goal
	 * @param maxDistanceFromGoal at which x-distance the ball may be from goal
	 * @return true if the ball is in a position in which we should dribble into goal
	 */
	boolean ballIsInGoalDribblingPosition(double maxXDistanceFromGoal);

	/**
	 * @param limitAngle the maximum abs horizontal angle (in deg) we can face
	 * @return true if we are in a position in which we should dribble into goal
	 */
	boolean canDribbleIntoGoal(double limitAngle);

	/**
	 * Calculates target position for dribbling, based on our current position
	 * and orientation.
	 *
	 * @return the target pose for dribbling (usually 10 cm in front of us)
	 */
	Pose2D getDribblePose();

	/**
	 * @return true if the ball is passing in keep short distance from us
	 */
	boolean isBallPassingShort();

	ProxyCommand getProxyCommand();
}
