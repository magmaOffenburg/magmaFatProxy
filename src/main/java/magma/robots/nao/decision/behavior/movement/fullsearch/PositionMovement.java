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
package magma.robots.nao.decision.behavior.movement.fullsearch;

import magma.agent.decision.behavior.movement.Movement;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class PositionMovement
{
	private Vector3D ballPos;

	private Movement movement;

	public PositionMovement(Vector3D ballPos, Movement movement)
	{
		super();
		this.ballPos = ballPos;
		this.movement = movement;
	}

	public Vector3D getBallPos()
	{
		return ballPos;
	}

	public Movement getMovement()
	{
		return movement;
	}

	public void setMovement(Movement movement)
	{
		this.movement = movement;
	}

	public void setBallPosition(Vector3D position)
	{
		this.ballPos = position;
	}
}