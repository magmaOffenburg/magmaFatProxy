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
import magma.util.scenegraph.ITransformNode;
import magma.util.scenegraph.NodeType;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class TransformNode extends BaseNode implements ITransformNode
{
	/** The local transformation of the node to its parent node */
	private float[] localTransform;

	private Vector3D position;

	private Rotation orientation;

	public TransformNode()
	{
	}

	public TransformNode(TransformNode other)
	{
		setLocalTransform(other.getLocalTransformation());
	}

	@Override
	public NodeType getNodeType()
	{
		return NodeType.TRANSFORM;
	}

	@Override
	public float[] getLocalTransformation()
	{
		return localTransform;
	}

	@Override
	public Rotation getOrientation()
	{
		return orientation;
	}

	@Override
	public Vector3D getPosition()
	{
		return position;
	}

	public void setLocalTransform(float[] localTransform)
	{
		this.localTransform = localTransform;
		updatePosition();
		updateOrientation();
	}

	@Override
	public void update(IBaseNode other)
	{
		if (other.getNodeType() == NodeType.TRANSFORM) {
			ITransformNode newTransform = (ITransformNode) other;

			if (newTransform.getLocalTransformation() != null) {
				setLocalTransform(newTransform.getLocalTransformation());
			}
		}

		super.update(other);
	}

	private void updatePosition()
	{
		if (localTransform != null) {
			float[] f = localTransform;
			position = new Vector3D(f[12], f[13], f[14]);
		}
	}

	private void updateOrientation()
	{
		if (localTransform != null) {
			float[] f = localTransform;
			double[][] rotation = {		//
					{f[0], f[4], f[8]}, //
					{f[1], f[5], f[9]}, //
					{f[2], f[6], f[10]}};

			orientation = new Rotation(rotation, 0.1);
		}
	}
}
