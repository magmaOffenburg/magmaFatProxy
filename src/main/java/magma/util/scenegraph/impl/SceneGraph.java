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
