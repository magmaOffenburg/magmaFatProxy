package magma.monitor.messageparser.impl;

import hso.autonomy.util.symboltreeparser.SymbolNode;
import hso.autonomy.util.symboltreeparser.SymbolTreeParser;
import java.io.UnsupportedEncodingException;
import magma.monitor.messageparser.IMonitorMessageParser;
import magma.monitor.messageparser.ISimulationState;
import magma.util.scenegraph.impl.BaseNode;
import magma.util.scenegraph.impl.SceneGraph;
import magma.util.scenegraph.impl.SceneGraphHeader;

public class MonitorMessageParser implements IMonitorMessageParser
{
	private SimulationState simulationState;

	private SceneGraph sceneGraph;

	private SymbolTreeParser treeParser;

	private RSGConverter rsgConverter;

	public MonitorMessageParser()
	{
		treeParser = new SymbolTreeParser();
		rsgConverter = new RSGConverter();
	}

	@Override
	public ISimulationState getSimulationState()
	{
		return simulationState;
	}

	@Override
	public SceneGraph getSceneGraph()
	{
		return sceneGraph;
	}

	@Override
	public void update(byte[] message)
	{
		try {
			update(new String(message, 0, message.length, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void update(String content)
	{
		simulationState = null;
		sceneGraph = null;

		try {
			SymbolNode root = treeParser.parse(content);

			// We expect three top level children:
			// 1. the simulation state
			// 2. the scene graph header
			// 3. the scene graph
			SimulationState currentState = rsgConverter.convertSimulationState((SymbolNode) root.children.get(0));
			SceneGraphHeader graphHeader = rsgConverter.convertSceneGraphHeader((SymbolNode) root.children.get(1));
			BaseNode graphRoot = rsgConverter.convertSceneGraphNode((SymbolNode) root.children.get(2));

			simulationState = currentState;
			sceneGraph = new SceneGraph(graphHeader, graphRoot);

			// System.out.println(sceneGraph);
			// System.out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
