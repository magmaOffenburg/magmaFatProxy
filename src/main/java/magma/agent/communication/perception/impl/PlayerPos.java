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
package magma.agent.communication.perception.impl;

import hso.autonomy.agent.communication.perception.impl.VisibleObjectPerceptor;
import hso.autonomy.util.geometry.VectorUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import magma.agent.communication.perception.IPlayerPos;
import magma.agent.model.worldmodel.IPlayer;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Represents an agent on the field (position, player number, team name, body
 * parts if applicable)
 *
 * @author Simon Raffeiner
 */
public class PlayerPos extends VisibleObjectPerceptor implements IPlayerPos
{
	public static final String PLAYER_POS = "playerPos";

	private final int id;

	private final Map<String, Vector3D> bodyPartMap;

	private final String teamname;

	/**
	 * Assignment constructor
	 *
	 * @param position Player position
	 * @param id Player ID
	 * @param teamname Team name
	 * @param bodyPartMap List of visible body parts
	 */
	public PlayerPos(String name, Vector3D position, int id, String teamname, Map<String, Vector3D> bodyPartMap,
			boolean hasDepth)
	{
		super(name, position, hasDepth, 1.0);
		this.id = id;
		this.teamname = teamname;
		this.bodyPartMap = bodyPartMap;
		if (bodyPartMap != null && bodyPartMap.size() > 1) {
			// when seeing multiple body parts we average the position
			Vector3D result = Vector3D.ZERO;
			for (Vector3D part : bodyPartMap.values()) {
				result = result.add(part);
			}
			result = new Vector3D(1.0d / bodyPartMap.size(), result);
			setPosition(result);
		}
	}

	@Override
	public int getId()
	{
		return id;
	}

	@Override
	public String getTeamname()
	{
		return teamname;
	}

	@Override
	public Map<String, Vector3D> getAllBodyParts()
	{
		return this.bodyPartMap;
	}

	/**
	 * Set the position of a given body part, add it to the list if it didn't
	 * exist before
	 *
	 * @param partName Body part name
	 * @param position New position
	 */
	public void setBodyPartPosition(String partName, Vector3D position)
	{
		this.bodyPartMap.put(partName, position);
	}

	@Override
	public Vector3D getBodyPartPosition(String partName)
	{
		return this.bodyPartMap.get(partName);
	}

	@Override
	public void averagePosition(List<IPlayer> playersPos)
	{
		List<Vector3D> positions = new ArrayList<>();
		positions.add(getPosition());
		for (IPlayer curPlayerPos : playersPos) {
			positions.add(curPlayerPos.getPosition());
		}

		setPosition(VectorUtils.average(positions));
	}
}
