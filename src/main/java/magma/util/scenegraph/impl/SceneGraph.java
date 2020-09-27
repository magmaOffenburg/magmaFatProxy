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

import magma.util.scenegraph.ISceneGraph;

public class SceneGraph implements ISceneGraph
{
	private final SceneGraphHeader header;

	private final BaseNode rootNode;

	public SceneGraph(ISceneGraph other)
	{
		header = new SceneGraphHeader(other.getHeader());
		rootNode = new BaseNode(other.getRootNode());
	}

	public SceneGraph(SceneGraphHeader header, BaseNode rootNode)
	{
		this.header = header;
		this.rootNode = rootNode;
	}

	@Override
	public BaseNode getRootNode()
	{
		return rootNode;
	}

	@Override
	public SceneGraphHeader getHeader()
	{
		return header;
	}

	@Override
	public String toString()
	{
		String ret = "SceneGraph";

		if (header != null) {
			ret += " " + header.getType() + " " + header.getMajorVersion() + "." + header.getMinorVersion();
		}

		ret += ":\n RSG-Graph:";

		if (rootNode != null) {
			ret += rootNode.toString();
		}

		return ret;
	}

	public void update(ISceneGraph sceneGraphUpdate)
	{
		header.update(sceneGraphUpdate.getHeader());
		rootNode.update(sceneGraphUpdate.getRootNode());
	}
}
