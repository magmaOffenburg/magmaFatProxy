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
package magma.agent.communication.action.impl;

import hso.autonomy.agent.communication.action.impl.Effector;
import magma.agent.communication.action.ICupsPositionEffector;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class CupsPositionEffector extends Effector implements ICupsPositionEffector
{
	Vector2D[] cupPositions;

	public CupsPositionEffector()
	{
		super("cups");
		cupPositions = new Vector2D[] {Vector2D.ZERO, Vector2D.ZERO, Vector2D.ZERO};
	}

	@Override
	public Vector2D[] getCupPositions()
	{
		return cupPositions;
	}

	@Override
	public void setEffectorValues(float... values)
	{
		cupPositions[0] = Vector2D.ZERO;
		cupPositions[1] = Vector2D.ZERO;
		cupPositions[2] = Vector2D.ZERO;

		if (values.length > 1) {
			cupPositions[0] = new Vector2D(values[0], values[1]);
		}

		if (values.length > 3) {
			cupPositions[1] = new Vector2D(values[2], values[3]);
		}

		if (values.length > 5) {
			cupPositions[2] = new Vector2D(values[4], values[5]);
		}
	}

	@Override
	public void resetAfterAction()
	{
		cupPositions[0] = Vector2D.ZERO;
		cupPositions[1] = Vector2D.ZERO;
		cupPositions[2] = Vector2D.ZERO;
	}

	@Override
	public float getCup1X()
	{
		return (float) cupPositions[0].getX();
	}

	@Override
	public float getCup1Y()
	{
		return (float) cupPositions[0].getY();
	}

	@Override
	public float getCup2X()
	{
		return (float) cupPositions[1].getX();
	}

	@Override
	public float getCup2Y()
	{
		return (float) cupPositions[1].getY();
	}

	@Override
	public float getCup3X()
	{
		return (float) cupPositions[2].getX();
	}
	@Override
	public float getCup3Y()
	{
		return (float) cupPositions[2].getY();
	}
}
