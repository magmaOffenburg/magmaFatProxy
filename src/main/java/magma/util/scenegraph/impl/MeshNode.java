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
import magma.util.scenegraph.IMeshNode;
import magma.util.scenegraph.NodeType;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class MeshNode extends BaseNode implements IMeshNode
{
	private Boolean isVisible;

	private Boolean isTransparent;

	private String objName;

	private Vector3D scale;

	private String[] materials;

	public MeshNode()
	{
	}

	@Override
	public NodeType getNodeType()
	{
		return NodeType.MESH;
	}

	@Override
	public Boolean isVisible()
	{
		return isVisible;
	}

	@Override
	public Boolean isTransparent()
	{
		return isTransparent;
	}

	@Override
	public String getObjName()
	{
		return objName;
	}

	@Override
	public Vector3D getScale()
	{
		return scale;
	}

	@Override
	public String[] getMaterials()
	{
		return materials;
	}

	public void setVisible(Boolean isVisible)
	{
		this.isVisible = isVisible;
	}

	public void setTransparent(Boolean isTransparent)
	{
		this.isTransparent = isTransparent;
	}

	public void setObjName(String objName)
	{
		this.objName = objName;
	}

	public void setScale(Vector3D scale)
	{
		this.scale = scale;
	}

	public void setMaterials(String[] materials)
	{
		this.materials = materials;
	}

	@Override
	public void update(IBaseNode other)
	{
		if (other.getNodeType() == NodeType.MESH) {
			IMeshNode newMesh = (IMeshNode) other;

			if (newMesh.isVisible() != null) {
				isVisible = newMesh.isVisible();
			}

			if (newMesh.isTransparent() != null) {
				isTransparent = newMesh.isTransparent();
			}

			if (newMesh.getObjName() != null) {
				objName = newMesh.getObjName();
			}

			if (newMesh.getScale() != null) {
				scale = newMesh.getScale();
			}

			if (newMesh.getMaterials() != null) {
				materials = newMesh.getMaterials();
			}
		}
	}

	@Override
	public <T extends IBaseNode> T getNode(Class<T> nodeType, String property, String value)
	{
		if (!nodeType.isInstance(this)) {
			return null;
		} else if (property == null) {
			return nodeType.cast(this);
		}

		if ("objName".equals(property)) {
			if (objName == null) {
				if (value == null) {
					return nodeType.cast(this);
				}
			} else {
				if (objName.matches(value)) {
					return nodeType.cast(this);
				}
			}
		} else if ("materials".equals(property)) {
			if (materials == null) {
				if (value == null) {
					return nodeType.cast(this);
				}
			} else {
				for (int i = 0; i < materials.length; i++) {
					if (materials[i].matches(value)) {
						return nodeType.cast(this);
					}
				}
			}
		}

		return null;
	}

	@Override
	public String toString()
	{
		String ret = "(" + getNodeType();
		ret += " isVisible=" + isVisible;
		ret += " isTransparent=" + isTransparent;
		ret += " objName=" + objName;
		ret += " scale=" + scale;
		ret += " materials=" + materials;

		return ret + ")";
	}
}
