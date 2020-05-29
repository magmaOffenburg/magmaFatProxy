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

import hso.autonomy.agent.communication.perception.IVisibleObjectPerceptor;
import magma.agent.model.worldmodel.IPlayer;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.List;
import java.util.Map;

/**
 * The Player Position Perceptor represents a single player as reported by the
 * visual sensor sub-system. Provides read-only access only.
 *
 * @author Simon Raffeiner
 */
public interface IPlayerPos extends IVisibleObjectPerceptor {
	int getId();

	String getTeamname();

	/**
	 * Retrieve a list of all visible body parts
	 *
	 * @return List of body parts
	 */
	Map<String, Vector3D> getAllBodyParts();

	/**
	 * Retrieve the 3-dimensional position of a given body part
	 *
	 * @param partname Body part name
	 * @return Body part position or null if no matching body part was found
	 */
	Vector3D getBodyPartPosition(String partname);

	void averagePosition(List<IPlayer> playersPos);
}