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

import magma.monitor.worldmodel.ISoccerBall;
import magma.util.scenegraph.NodeType;
import magma.util.scenegraph.impl.TransformNode;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class SoccerBall extends SimulationObject implements ISoccerBall
{
	private float radius;

	private float mass;

	public SoccerBall()
	{
	}

	@Override
	public float getRadius()
	{
		return radius;
	}

	@Override
	public float getMass()
	{
		return mass;
	}

	public void setRadius(float radius)
	{
		this.radius = radius;
	}

	public void setMass(float mass)
	{
		this.mass = mass;
	}

	@Override
	public void refresh(float deltaT)
	{
		if (graphRoot != null && graphRoot.getNodeType() == NodeType.TRANSFORM) {
			float[] matrix = ((TransformNode) graphRoot).getLocalTransformation();

			position = new Vector3D(matrix[12], matrix[13], matrix[14]);
		}
	}
}
