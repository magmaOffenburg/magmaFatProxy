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
import hso.autonomy.agent.decision.behavior.basic.None;
import hso.autonomy.agent.decision.decisionmaker.IDecisionMaker;
import hso.autonomy.agent.model.agentmeta.IAgentMetaModel;
import hso.autonomy.agent.model.agentmodel.IAgentModel;
import hso.autonomy.agent.model.agentmodel.impl.ik.IAgentIKSolver;
import hso.autonomy.agent.model.agentmodel.impl.ik.impl.JacobianTransposeAgentIKSolver;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.agent.model.worldmodel.IWorldModel;
import hso.autonomy.agent.model.worldmodel.localizer.ILocalizer;
import hso.autonomy.agent.model.worldmodel.localizer.impl.CompositeLocalizer;
import hso.autonomy.agent.model.worldmodel.localizer.impl.LocalizerFieldNormal;
import hso.autonomy.agent.model.worldmodel.localizer.impl.LocalizerGyro;
import hso.autonomy.util.file.FileUtil;
import java.io.IOException;
import java.net.URISyntaxException;
import kdo.util.parameter.ParameterMap;
import magma.agent.communication.action.IRoboCupAction;
import magma.agent.communication.action.impl.RoboCupAction;
import magma.agent.communication.channel.impl.ChannelParameters;
import magma.agent.communication.perception.IRoboCupPerception;
import magma.agent.communication.perception.impl.RoboCupPerception;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IWalkEstimator;
import magma.agent.decision.behavior.basic.BeamHome;
import magma.agent.decision.behavior.basic.BeamToPosition;
import magma.agent.decision.behavior.basic.MonitorKick;
import magma.agent.decision.behavior.basic.SayPositions;
import magma.agent.decision.behavior.basic.StopInstantly;
import magma.agent.decision.behavior.supportPoint.FunctionBehavior;
import magma.agent.decision.behavior.supportPoint.FunctionBehaviorParameters;
import magma.agent.decision.decisionmaker.impl.GoalieDecisionMaker;
import magma.agent.decision.decisionmaker.impl.SoccerDecisionMaker;
import magma.agent.model.agentmeta.IRoboCupAgentMetaModel;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.agentmodel.impl.RoboCupAgentModel;
import magma.agent.model.flags.IFlagModel;
import magma.agent.model.flags.impl.FlagModel;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.impl.RoboCupThoughtModel;
import magma.agent.model.thoughtmodel.strategy.IRoleManager;
import magma.agent.model.thoughtmodel.strategy.impl.RoleManager;
import magma.agent.model.thoughtmodel.strategy.impl.strategies.StrategyConfigurationHelper;
import magma.agent.model.worldmeta.IRoboCupWorldMetaModel;
import magma.agent.model.worldmeta.RCServerConfigurationHelper;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.agent.model.worldmodel.impl.RoboCupWorldModel;
import magma.monitor.general.impl.MonitorRuntime;
import magma.util.roboviz.RoboVizDraw;

/**
 * Factory class for the components of our RoboCup player
 *
 * @author Klaus Dorer
 */
public abstract class ComponentFactory implements IBehaviorConstants
{
	public abstract IRoboCupAgentMetaModel getAgentMetaModel();

	/**
	 * @return the channel manager including the channels
	 */
	public abstract IChannelManager createChannelManager(ChannelParameters info);

	public IAgentIKSolver createAgentIKSolver()
	{
		return new JacobianTransposeAgentIKSolver();
	}

	/**
	 * Create a new rc-server meta model based on serverVersion.
	 *
	 * @param serverVersion - the version of the meta model
	 */
	public IRoboCupWorldMetaModel createRCServerMetaModel(int serverVersion)
	{
		return RCServerConfigurationHelper.getRCServerMetalModel(serverVersion);
	}

	public IRoboCupPerception createPerception()
	{
		return new RoboCupPerception();
	}

	/**
	 * Create an new Action
	 *
	 * @param actionPerformer Server connection
	 */
	public IRoboCupAction createAction(IActionPerformer actionPerformer, IAgentMetaModel metaModel)
	{
		IRoboCupAction action = new RoboCupAction(actionPerformer);
		action.init(metaModel.createEffectors());
		return action;
	}

	public IRoboCupAgentModel createAgentModel(IRoboCupAgentMetaModel metaModel)
	{
		return new RoboCupAgentModel(metaModel, createAgentIKSolver());
	}

	public IWorldModel createWorldModel(
			IRoboCupAgentModel agentModel, IRoboCupWorldMetaModel worldMetaModel, String teamName, int playerNumber)
	{
		return new RoboCupWorldModel(agentModel, createLocalizer(), worldMetaModel, teamName, playerNumber);
	}

	public IThoughtModel createThoughtModel(IAgentModel agentModel, IWorldModel worldModel, IFlagModel flags,
			RoboVizDraw roboVizDraw, MonitorRuntime monitor)
	{
		return new RoboCupThoughtModel(agentModel, (IRoboCupWorldModel) worldModel, flags, roboVizDraw, monitor);
	}

	public IRoleManager createRoleManager(IRoboCupThoughtModel thoughtModel, IWorldModel worldModel)
	{
		return new RoleManager(
				worldModel, StrategyConfigurationHelper.STRATEGIES.get(StrategyConfigurationHelper.DEFAULT_STRATEGY)
									.create(thoughtModel));
	}

	protected ILocalizer createLocalizer()
	{
		// Note: localizer order matters! The localizer needing the highest number
		// of visible reference points should be first, the one that needs the
		// least last
		return new CompositeLocalizer(new ILocalizer[] {new LocalizerFieldNormal(), new LocalizerGyro()});
	}

	/**
	 * Create a new model holding flags.
	 */
	public IFlagModel createFlags()
	{
		return new FlagModel();
	}

	public IDecisionMaker createDecisionMaker(BehaviorMap behaviors, IThoughtModel thoughtModel, int playerNumber,
			String decisionMakerName, ParameterMap learningParam)
	{
		if (playerNumber == 1) {
			return new GoalieDecisionMaker(behaviors, thoughtModel);
		} else {
			return new SoccerDecisionMaker(behaviors, thoughtModel);
		}
	}

	/**
	 * Create all behavior objects that are used during the game
	 *
	 * @param thoughtModel Reference to the ThoughtModel
	 * @param params Behavior parameterization, null if default should be used
	 * @return A map of available behaviors
	 */
	public BehaviorMap createBehaviors(IThoughtModel thoughtModel, ParameterMap params)
	{
		BehaviorMap behaviors = new BehaviorMap();

		readFunctionBehaviors(getBehaviorFilesBasePath(), thoughtModel, behaviors);

		behaviors.put(new None(thoughtModel));
		behaviors.put(new StopInstantly(thoughtModel));
		behaviors.put(new BeamHome(thoughtModel));
		behaviors.put(new BeamToPosition(thoughtModel));
		behaviors.put(new SayPositions(thoughtModel));
		behaviors.put(new MonitorKick(thoughtModel));

		createSpecificBehaviors(thoughtModel, params, behaviors);

		return behaviors;
	}

	/**
	 * Return the path to the behavior files base folder. This method is used to
	 * dynamically read all function behaviors.
	 *
	 * @return the path to the behavior files base folder
	 */
	protected abstract String getBehaviorFilesBasePath();

	/**
	 * Create all specific behavior objects.
	 *
	 * @param thoughtModel - the thought model
	 * @param params - behavior parametrization, null if default should be used
	 * @param behaviors - a map for storing the created specific behaviors
	 */
	protected abstract void createSpecificBehaviors(
			IThoughtModel thoughtModel, ParameterMap params, BehaviorMap behaviors);

	/**
	 * Reads function behavior files, creates corresponding objects and adds them
	 * to the map of behaviors.
	 */
	protected void readFunctionBehaviors(String pathName, IThoughtModel thoughtModel, BehaviorMap behaviors)
	{
		try {
			String[] files = FileUtil.getResourceListing(pathName);
			for (String fileName : files) {
				createFunctionBehavior(thoughtModel, behaviors, pathName + fileName);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Function behavior file not found" + pathName);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			System.err.println("URI exception when reading: " + pathName);
		}
	}

	private void createFunctionBehavior(IThoughtModel thoughtModel, BehaviorMap behaviors, String filePath)
			throws IOException
	{
		if (!filePath.endsWith(FunctionBehaviorParameters.EXTENSION)) {
			return;
		}

		FunctionBehaviorParameters parameter;
		try {
			parameter = FunctionBehaviorParameters.readBehaviorFile(filePath);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		String name = parameter.getName();

		if (parameter.getMirror()) {
			// assume the file contains the right version
			behaviors.put(new FunctionBehavior(name + "Right", thoughtModel, parameter, filePath, false));
			behaviors.put(new FunctionBehavior(name + "Left", thoughtModel, parameter, filePath, true));
		} else {
			behaviors.put(new FunctionBehavior(name, thoughtModel, parameter, filePath, false));
		}
	}

	/**
	 * Creates behavior or decision maker parameters. fromExtern parameters
	 * overwrite default parameters.
	 * @param fromExtern parametrization from an external source like learning
	 * @return a parameter map that has to contain all ParameterLists needed for
	 *         default behaviors
	 */
	public ParameterMap createParameters(ParameterMap fromExtern)
	{
		ParameterMap result = new ParameterMap();

		// specific parameters have higher priority than general and are put after
		// them in order to allow overwriting
		result.putAll(createSpecificParameters());

		// external parameters (e.g. from learning) have higher priority and are
		// therefore overwriting parameters defined here
		result.putAll(fromExtern);
		return result;
	}

	/**
	 * Overwrite this to create robot specific parametrization
	 */
	protected abstract ParameterMap createSpecificParameters();

	/**
	 * Instance for estimating times it takes to walk to
	 */
	public abstract IWalkEstimator createWalkEstimator();

	public abstract void loadProperties();
}
