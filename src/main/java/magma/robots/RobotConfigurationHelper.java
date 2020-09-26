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
package magma.robots;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import magma.agent.IMagmaConstants;
import magma.agent.agentruntime.ComponentFactory;
import magma.agent.model.agentmeta.impl.RoboCupAgentMetaModel;
import magma.robots.nao.general.agentruntime.NaoComponentFactory;
import magma.robots.nao.model.agentmeta.NaoAgentMetaModel;
import magma.robots.nao1.general.agentruntime.Nao1ComponentFactory;
import magma.robots.nao1.model.agentmeta.Nao1AgentMetaModel;
import magma.robots.nao2.general.agentruntime.Nao2ComponentFactory;
import magma.robots.nao2.model.agentmeta.Nao2AgentMetaModel;
import magma.robots.nao3.general.agentruntime.Nao3ComponentFactory;
import magma.robots.nao3.model.agentmeta.Nao3AgentMetaModel;
import magma.robots.naoGazebo.general.agentruntime.NaoGazeboComponentFactory;
import magma.robots.naotoe.general.agentruntime.NaoToeComponentFactory;
import magma.robots.naotoe.model.agentmeta.NaoToeAgentMetaModel;

public class RobotConfigurationHelper
{
	@FunctionalInterface
	public interface ComponentFactoryConstructor {
		ComponentFactory create();
	}

	/**
	 * A read-only list of all available robot model names.
	 */
	public static final Map<String, ComponentFactoryConstructor> ROBOT_MODELS;

	static
	{
		Map<String, ComponentFactoryConstructor> models = new LinkedHashMap<>();
		models.put(IMagmaConstants.DEFAULT_FACTORY, null);
		models.put(NaoAgentMetaModel.NAME, NaoComponentFactory::new);
		models.put(Nao1AgentMetaModel.NAME, Nao1ComponentFactory::new);
		models.put(Nao2AgentMetaModel.NAME, Nao2ComponentFactory::new);
		models.put(Nao3AgentMetaModel.NAME, Nao3ComponentFactory::new);
		models.put(NaoToeAgentMetaModel.NAME, NaoToeComponentFactory::new);
		models.put(NaoGazeboComponentFactory.NAME, NaoGazeboComponentFactory::new);
		ROBOT_MODELS = Collections.unmodifiableMap(models);
	}

	public static ComponentFactory getComponentFactory(String robotModel)
	{
		return getComponentFactory(robotModel, -1);
	}

	public static ComponentFactory getComponentFactory(String robotModel, int playerNumber)
	{
		if (robotModel == null || robotModel.equals(IMagmaConstants.DEFAULT_FACTORY)) {
			return getComponentFactory(playerNumber);
		}

		return ROBOT_MODELS.get(robotModel).create();
	}

	private static ComponentFactory getComponentFactory(int playerNumber)
	{
		String name;

		switch (playerNumber) {
		case 1:
		case 3:
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
			name = NaoToeAgentMetaModel.NAME;
			break;
		case 4:
		case 6:
			name = Nao2AgentMetaModel.NAME;
			break;
		case 2:
		case 5:
		default:
			name = NaoAgentMetaModel.NAME;
			break;
		}

		return ROBOT_MODELS.get(name).create();
	}

	public static RoboCupAgentMetaModel getAgentMetaModel(String robotModel)
	{
		switch (robotModel) {
		case Nao1AgentMetaModel.NAME:
			return Nao1AgentMetaModel.INSTANCE;
		case Nao2AgentMetaModel.NAME:
			return Nao2AgentMetaModel.INSTANCE;
		case Nao3AgentMetaModel.NAME:
			return Nao3AgentMetaModel.INSTANCE;
		case NaoToeAgentMetaModel.NAME:
			return NaoToeAgentMetaModel.INSTANCE;
		default:
			return NaoAgentMetaModel.INSTANCE;
		}
	}
}
