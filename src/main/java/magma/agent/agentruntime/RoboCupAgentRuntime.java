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

import hso.autonomy.agent.agentruntime.AgentRuntime;
import hso.autonomy.agent.communication.perception.IPerceptionLogger;
import hso.autonomy.agent.communication.perception.IPerceptor;
import hso.autonomy.agent.model.worldmodel.IWorldModel;
import java.util.Map;
import kdo.util.parameter.ParameterMap;
import magma.agent.communication.action.IRoboCupAction;
import magma.agent.communication.perception.IRoboCupPerception;
import magma.agent.decision.evaluator.IDecisionEvaluator;
import magma.agent.decision.evaluator.impl.DecisionEvaluationManager;
import magma.agent.model.agentmeta.IRoboCupAgentMetaModel;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.flags.Flag;
import magma.agent.model.flags.IFlagModel;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.impl.RoboCupThoughtModel;
import magma.agent.model.worldmeta.IRoboCupWorldMetaModel;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.monitor.general.impl.MonitorRuntime;
import magma.util.roboviz.RoboVizDraw;

/**
 * The AgentRuntime is the core orchestrating component in the magma
 * agent-framework.
 * <h5>Tasks:</h5>
 * <ul>
 * <li>Create and setup components based on ComponentFactory</li>
 * <li>Manage internal triggering of all components during a "agent-cycle"</li>
 * </ul>
 *
 * @author Stefan Glaser
 */
public class RoboCupAgentRuntime extends AgentRuntime
{
	/** the meta model describing the rc-server */
	protected transient IRoboCupWorldMetaModel worldMetaModel;

	private IPerceptionLogger perceptionLogger;

	IDecisionEvaluator evaluator;

	private boolean switchDecisionmaker;

	/**
	 * @param params - the configuration parameters to setup the client
	 * @param monitor
	 */
	public RoboCupAgentRuntime(PlayerParameters params, MonitorRuntime monitor)
	{
		ComponentFactory factory = params.getComponentFactory();

		factory.loadProperties();

		// meta models
		agentMetaModel = factory.getAgentMetaModel();
		worldMetaModel = factory.createRCServerMetaModel(params.getServerVersion());

		// protocol layer
		channelManager = factory.createChannelManager(params.getChannelParams());

		perception = factory.createPerception();

		action = factory.createAction(channelManager, agentMetaModel);

		// model layer
		IRoboCupAgentModel agentModel = factory.createAgentModel(getAgentMetaModel());

		IWorldModel worldModel =
				factory.createWorldModel(agentModel, worldMetaModel, params.getTeamname(), params.getPlayerNumber());

		IFlagModel flags = factory.createFlags();

		RoboVizDraw roboVizDraw = new RoboVizDraw(params.getRoboVizParams());

		thoughtModel = factory.createThoughtModel(agentModel, worldModel, flags, roboVizDraw, monitor);
		((RoboCupThoughtModel) thoughtModel)
				.setRoleManager(factory.createRoleManager((IRoboCupThoughtModel) thoughtModel, worldModel));

		// control layer
		ParameterMap parameterMap = factory.createParameters(params.getParameterMap());
		behaviors = factory.createBehaviors(thoughtModel, parameterMap);

		// decision making layer
		decisionMaker = factory.createDecisionMaker(
				behaviors, thoughtModel, params.getPlayerNumber(), params.getDecisionMakerName(), parameterMap);

		reportStats = params.getReportStats();
		evaluator = new DecisionEvaluationManager(decisionMaker, (RoboCupThoughtModel) thoughtModel);
		switchDecisionmaker = params.isSwitchDecisionmaker();

		if (params.getChannelParams().isLogPerception()) {
			perceptionLogger = channelManager.getPerceptionLogger();
			perceptionLogger.start();
		}
	}

	@Override
	public void update(Map<String, IPerceptor> content)
	{
		if (switchDecisionmaker) {
			// switchDecisionMaker(content);
		}
		if (perceptionLogger != null) {
			perceptionLogger.log(content);
		}
		onEndUpdateLoop();
		super.update(content);
	}

	@Override
	protected void onClientStopped()
	{
		if (reportStats) {
			new GameSeriesReporter(behaviors, getWorldModel().getPlaySide(), cycles).report();
		}
		if (perceptionLogger != null) {
			perceptionLogger.stop();
		}
	}

	public IRoboCupWorldMetaModel getWorldMetaModel()
	{
		return worldMetaModel;
	}

	public IRoboCupAgentMetaModel getAgentMetaModel()
	{
		return (IRoboCupAgentMetaModel) agentMetaModel;
	}

	@Override
	public IRoboCupPerception getPerception()
	{
		return (IRoboCupPerception) perception;
	}

	@Override
	public IRoboCupAction getAction()
	{
		return (IRoboCupAction) action;
	}

	@Override
	public IRoboCupAgentModel getAgentModel()
	{
		return (IRoboCupAgentModel) thoughtModel.getAgentModel();
	}

	@Override
	public IRoboCupWorldModel getWorldModel()
	{
		return (IRoboCupWorldModel) thoughtModel.getWorldModel();
	}

	@Override
	public IRoboCupThoughtModel getThoughtModel()
	{
		return (IRoboCupThoughtModel) super.getThoughtModel();
	}

	public void setPaused(boolean paused)
	{
		getThoughtModel().getFlags().set(Flag.SOFT_PAUSE, paused);
	}

	public float getCycleTime()
	{
		return getAgentModel().getCycleTime();
	}

	public int getGoalPredictionTime()
	{
		return getAgentModel().getGoalPredictionTime();
	}

	public float getTorsoZUpright()
	{
		return getAgentModel().getTorsoZUpright();
	}

	@Override
	protected void onEndUpdateLoop()
	{
		evaluator.evaluate();
	}
}
