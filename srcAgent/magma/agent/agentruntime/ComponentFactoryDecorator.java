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
package magma.agent.agentruntime;

import hso.autonomy.agent.communication.action.IActionPerformer;
import hso.autonomy.agent.communication.channel.IChannelManager;
import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.decisionmaker.IDecisionMaker;
import hso.autonomy.agent.model.agentmeta.IAgentMetaModel;
import hso.autonomy.agent.model.agentmodel.IAgentModel;
import hso.autonomy.agent.model.agentmodel.impl.ik.IAgentIKSolver;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.agent.model.worldmodel.IWorldModel;
import kdo.util.parameter.ParameterMap;
import magma.agent.communication.action.IRoboCupAction;
import magma.agent.communication.channel.impl.ChannelParameters;
import magma.agent.communication.perception.IRoboCupPerception;
import magma.agent.decision.behavior.IWalkEstimator;
import magma.agent.model.agentmeta.IRoboCupAgentMetaModel;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.flags.IFlagModel;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.strategy.IRoleManager;
import magma.agent.model.worldmeta.IRoboCupWorldMetaModel;
import magma.monitor.general.impl.MonitorRuntime;
import magma.util.roboviz.RoboVizDraw;

public class ComponentFactoryDecorator extends ComponentFactory
{
	protected final ComponentFactory decoratee;

	public ComponentFactoryDecorator(ComponentFactory decoratee)
	{
		this.decoratee = decoratee;
	}

	@Override
	public IAgentIKSolver createAgentIKSolver()
	{
		return decoratee.createAgentIKSolver();
	}

	@Override
	public IRoboCupWorldMetaModel createRCServerMetaModel(int serverVersion)
	{
		return decoratee.createRCServerMetaModel(serverVersion);
	}

	@Override
	public IRoboCupPerception createPerception()
	{
		return decoratee.createPerception();
	}

	@Override
	public IRoboCupAction createAction(IActionPerformer actionPerformer, IAgentMetaModel metaModel)
	{
		return decoratee.createAction(actionPerformer, metaModel);
	}

	@Override
	public IRoboCupAgentModel createAgentModel(IRoboCupAgentMetaModel metaModel)
	{
		return decoratee.createAgentModel(metaModel);
	}

	@Override
	public IWorldModel createWorldModel(
			IRoboCupAgentModel agentModel, IRoboCupWorldMetaModel metaModel, String teamName, int playerNumber)
	{
		return decoratee.createWorldModel(agentModel, metaModel, teamName, playerNumber);
	}

	@Override
	public IThoughtModel createThoughtModel(IAgentModel agentModel, IWorldModel worldModel, IFlagModel flags,
			RoboVizDraw roboVizDraw, MonitorRuntime monitor)
	{
		return decoratee.createThoughtModel(agentModel, worldModel, flags, roboVizDraw, monitor);
	}

	@Override
	public IRoleManager createRoleManager(IRoboCupThoughtModel thoughtModel, IWorldModel worldModel)
	{
		return decoratee.createRoleManager(thoughtModel, worldModel);
	}

	@Override
	public IFlagModel createFlags()
	{
		return decoratee.createFlags();
	}

	@Override
	public IDecisionMaker createDecisionMaker(BehaviorMap behaviors, IThoughtModel thoughtModel, int playerNumber,
			String decisionMakerName, ParameterMap learningParam)
	{
		return decoratee.createDecisionMaker(behaviors, thoughtModel, playerNumber, decisionMakerName, learningParam);
	}

	@Override
	public BehaviorMap createBehaviors(IThoughtModel thoughtModel, ParameterMap params)
	{
		return decoratee.createBehaviors(thoughtModel, params);
	}

	@Override
	public ParameterMap createParameters(ParameterMap fromExtern)
	{
		return decoratee.createParameters(fromExtern);
	}

	@Override
	public IRoboCupAgentMetaModel getAgentMetaModel()
	{
		return decoratee.getAgentMetaModel();
	}

	@Override
	public IChannelManager createChannelManager(ChannelParameters info)
	{
		return decoratee.createChannelManager(info);
	}

	@Override
	protected String getBehaviorFilesBasePath()
	{
		return "";
	}

	@Override
	protected void createSpecificBehaviors(IThoughtModel thoughtModel, ParameterMap params, BehaviorMap behaviors)
	{
	}

	@Override
	protected ParameterMap createSpecificParameters()
	{
		return new ParameterMap();
	}

	@Override
	public IWalkEstimator createWalkEstimator()
	{
		return decoratee.createWalkEstimator();
	}

	@Override
	public void loadProperties()
	{
		decoratee.loadProperties();
	}
}
