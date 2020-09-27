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

import java.util.ArrayList;
import magma.util.scenegraph.IBaseNode;
import magma.util.scenegraph.NodeType;

public class BaseNode implements IBaseNode
{
	/** The parent node (= null corresponds to a root node) */
	private IBaseNode parent;

	/** The child nodes (= null corresponds to a leaf node) */
	private ArrayList<IBaseNode> children;

	public BaseNode()
	{
	}

	/**
	 * Copy Constructor.
	 *
	 * @param other - the BaseNode to copy
	 */
	public BaseNode(IBaseNode other)
	{
		ArrayList<IBaseNode> otherChildren = other.getChildren();

		if (otherChildren != null) {
			for (IBaseNode child : otherChildren) {
				addChildNode(child.clone());
			}
		}
	}

	@Override
	public NodeType getNodeType()
	{
		return NodeType.BASE;
	}

	@Override
	public void setParent(IBaseNode parent)
	{
		this.parent = parent;
	}

	@Override
	public IBaseNode getParent()
	{
		return parent;
	}

	@Override
	public ArrayList<IBaseNode> getChildren()
	{
		return children;
	}

	public boolean addChildNode(IBaseNode childNode)
	{
		if (children == null) {
			children = new ArrayList<>();
		}

		childNode.setParent(this);

		return children.add(childNode);
	}

	@Override
	public IBaseNode clone()
	{
		BaseNode copy = null;
		try {
			copy = (BaseNode) super.clone();

			if (children != null) {
				copy.children = new ArrayList<>();

				for (IBaseNode child : children) {
					copy.addChildNode(child.clone());
				}
			}
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		return copy;
	}

	@Override
	public boolean structurallyEquals(IBaseNode other)
	{
		if (children == null && other.getChildren() == null) {
			return true;
		} else if (children != null && other.getChildren() != null) {
			return children.size() == other.getChildren().size();
		} else {
			return false;
		}
	}

	@Override
	public void update(IBaseNode other)
	{
		ArrayList<IBaseNode> otherChildren = other.getChildren();

		if (children == null && otherChildren == null) {
			return;
		}

		if (children != null && otherChildren != null && children.size() == otherChildren.size()) {
			for (int i = 0; i < children.size(); i++) {
				children.get(i).update(otherChildren.get(i));
			}
		} else {
			// System.out.println("Detected structural difference in node: " + this
			// + " with update: " + other);
		}
	}

	@Override
	public <T extends IBaseNode> T getNode(Class<T> nodeType, String property, String value)
	{
		if (nodeType.isInstance(this)) {
			return nodeType.cast(this);
		} else if (children != null) {
			T tempNode;

			for (IBaseNode child : children) {
				tempNode = child.getNode(nodeType, property, value);
				if (tempNode != null) {
					return tempNode;
				}
			}
		}

		return null;
	}

	@Override
	public String toString()
	{
		if (children == null) {
			return "(" + getNodeType() + ")";
		}

		String ret = "";

		for (IBaseNode child : children) {
			ret += child.toString();
		}

		return "(" + ret + ")";
	}
}
