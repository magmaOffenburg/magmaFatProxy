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
package magma.agent.communication.perception;

import hso.autonomy.agent.communication.perception.IPerception;
import java.util.List;

/**
 * The perception represents all input an agent can get from the outside.
 *
 * @author Simon Raffeiner
 *
 */
public interface IRoboCupPerception extends IPerception {
	/** object name constants */
	String BALL = "B";

	String GOAL_LEFT_LEFTPOST = "G1L";

	String GOAL_LEFT_RIGHTPOST = "G2L";

	String GOAL_RIGHT_LEFTPOST = "G1R";

	String GOAL_RIGHT_RIGHTPOST = "G2R";

	String LINE_POINT = "LP";

	String REFERENCE_POINT = "RP";

	String CAMERA_IMU_DATA = "IMU";

	/**
	 * Get Agent State
	 *
	 * @return AgentState perceptor
	 */
	IAgentStatePerceptor getAgentState();

	/**
	 * Get the Game State perceptor
	 *
	 * @return perceptor
	 */
	IGameStatePerceptor getGameState();

	/**
	 * Get the Hear perceptor
	 *
	 * @return perceptor
	 */
	List<IHearPerceptor> getHearPerceptors();

	/**
	 * Get camera tilt
	 *
	 * @return perceptor
	 */
	ICameraTiltPerceptor getCameraTiltPerceptor();

	IActionPerceptor getActionPerceptor();

	List<IPlayerPos> getVisiblePlayers();

	IProxyPerceptor getProxyPerceptor();
}
