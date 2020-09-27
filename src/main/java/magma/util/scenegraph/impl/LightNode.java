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
package magma.util.scenegraph.impl;

import magma.util.scenegraph.IBaseNode;
import magma.util.scenegraph.ILightNode;
import magma.util.scenegraph.NodeType;

public class LightNode extends BaseNode implements ILightNode
{
	private float[] diffuse;

	private float[] ambient;

	private float[] specular;

	public LightNode()
	{
	}

	@Override
	public NodeType getNodeType()
	{
		return NodeType.LIGHT;
	}

	@Override
	public float[] getDiffuse()
	{
		return diffuse;
	}

	@Override
	public float[] getAmbient()
	{
		return ambient;
	}

	@Override
	public float[] getSpecular()
	{
		return specular;
	}

	public void setDiffuse(float[] diffuse)
	{
		this.diffuse = diffuse;
	}

	public void setAmbient(float[] ambient)
	{
		this.ambient = ambient;
	}

	public void setSpecular(float[] specular)
	{
		this.specular = specular;
	}

	@Override
	public void update(IBaseNode other)
	{
		if (other.getNodeType() == NodeType.LIGHT) {
			ILightNode newLight = (ILightNode) other;

			if (newLight.getDiffuse() != null) {
				diffuse = newLight.getDiffuse();
			}

			if (newLight.getAmbient() != null) {
				ambient = newLight.getAmbient();
			}

			if (newLight.getSpecular() != null) {
				specular = newLight.getSpecular();
			}
		}
	}

	@Override
	public <T extends IBaseNode> T getNode(Class<T> nodeType, String property, String value)
	{
		return super.getNode(nodeType, property, value);
	}

	@Override
	public String toString()
	{
		String ret = "(" + getNodeType();
		ret += " diffuse=" + diffuse;
		ret += " ambient=" + ambient;
		ret += " specular=" + specular;

		return ret + ")";
	}
}
