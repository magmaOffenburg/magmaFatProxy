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

/**
 * Implementation of the SimSpark "beam" effector
 *
 * @author Klaus Dorer
 */
class BeamEffector extends Effector
{
	private final float[] beam = new float[3]; // all zero no beam action

	public BeamEffector()
	{
		super("beam");
		beam[0] = 0;
		beam[1] = 0;
		beam[2] = 0;
	}

	/**
	 * Set beam coordinates
	 */
	@Override
	public void setEffectorValues(float... values)
	{
		beam[0] = values[0];
		beam[1] = values[1];
		beam[2] = values[2];
	}

	/**
	 * Retrieve beam coordinates
	 *
	 * @return Coordinate array
	 */
	public float[] getBeam()
	{
		return beam;
	}

	@Override
	public void resetAfterAction()
	{
		setEffectorValues(0.0f, 0.0f, 0.0f);
	}
}
