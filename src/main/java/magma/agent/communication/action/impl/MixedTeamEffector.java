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
import magma.agent.communication.action.IMixedTeamEffector;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class MixedTeamEffector extends Effector implements IMixedTeamEffector
{
	private Vector3D globalPlayerPos;
	private double globalZOrientation;
	private Vector3D localBallPos;

	public MixedTeamEffector()
	{
		super(NAME);
	}

	@Override
	public Vector3D getGlobalPlayerPos()
	{
		return globalPlayerPos;
	}

	public void setGlobalPlayerPos(Vector3D globalPlayerPos)
	{
		this.globalPlayerPos = globalPlayerPos;
	}

	@Override
	public double getGlobalZOrientation()
	{
		return globalZOrientation;
	}

	public void setGlobalZOrientation(double globalZOrientation)
	{
		this.globalZOrientation = globalZOrientation;
	}

	@Override
	public Vector3D getLocalBallPos()
	{
		return localBallPos;
	}

	public void setLocalBallPos(Vector3D localBallPos)
	{
		this.localBallPos = localBallPos;
	}

	@Override
	public void setEffectorValues(float... values)
	{
		// not used here
	}
}
