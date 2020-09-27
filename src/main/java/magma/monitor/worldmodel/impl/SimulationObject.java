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
package magma.monitor.worldmodel.impl;

import magma.monitor.worldmodel.ISimulationObject;
import magma.util.scenegraph.IBaseNode;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public abstract class SimulationObject implements ISimulationObject
{
	protected IBaseNode graphRoot;

	protected Vector3D position;

	public SimulationObject()
	{
		position = Vector3D.ZERO;
	}

	public SimulationObject(IBaseNode graphRoot)
	{
		this.graphRoot = graphRoot;
	}

	public void setGraphRoot(IBaseNode graphRoot)
	{
		this.graphRoot = graphRoot;
		refresh(0);
	}

	@Override
	public IBaseNode getGraphRoot()
	{
		return graphRoot;
	}

	@Override
	public Vector3D getPosition()
	{
		return position;
	}

	public abstract void refresh(float deltaT);
}
