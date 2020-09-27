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
package magma.util.scenegraph;

import java.util.ArrayList;

public interface IBaseNode extends Cloneable {
	NodeType getNodeType();

	void setParent(IBaseNode parent);

	IBaseNode getParent();

	ArrayList<IBaseNode> getChildren();

	IBaseNode clone();

	boolean structurallyEquals(IBaseNode other);

	void update(IBaseNode other);

	/**
	 * Fetch the first node in the child structure by a depth first search that
	 * corresponds to the given NodeType and fulfills the property constraint. A
	 * node fulfills the property constraint, if the specified property has or
	 * contains the given value.
	 *
	 * @param <T> - the intended node type
	 * @param nodeType - the intended node type
	 * @param property - a property of the node that should be checked or null if
	 *        no check should be performed
	 * @param value - the value which the specified property should contain
	 * @return the first node of type nodeType with the given property having the
	 *         given value or null if no such node was found
	 */
	<T extends IBaseNode> T getNode(Class<T> nodeType, String property, String value);
}
