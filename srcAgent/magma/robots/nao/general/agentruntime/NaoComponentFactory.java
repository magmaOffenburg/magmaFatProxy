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
package magma.robots.nao.general.agentruntime;

import hso.autonomy.agent.communication.channel.IChannelManager;
import hso.autonomy.agent.communication.channel.IOutputChannel;
import hso.autonomy.agent.communication.channel.impl.ChannelManager;
import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.decisionmaker.IDecisionMaker;
import hso.autonomy.agent.model.agentmodel.impl.ik.IAgentIKSolver;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.util.properties.PropertyManager;
import java.util.ArrayList;
import java.util.List;
import kdo.util.parameter.ParameterMap;
import magma.agent.IMagmaConstants;
import magma.agent.agentruntime.ComponentFactory;
import magma.agent.communication.channel.IRoboCupChannel;
import magma.agent.communication.channel.impl.ChannelParameters;
import magma.agent.communication.channel.impl.SimsparkChannel;
import magma.agent.decision.behavior.IBaseWalk;
import magma.agent.decision.behavior.IWalkEstimator;
import magma.agent.decision.behavior.base.WalkEstimator;
import magma.agent.decision.behavior.complex.goalie.GoaliePositioning;
import magma.agent.decision.behavior.complex.walk.PassivePositioning;
import magma.agent.decision.behavior.complex.walk.SearchBall;
import magma.agent.decision.behavior.complex.walk.Walk;
import magma.agent.decision.behavior.complex.walk.WalkToPosition;
import magma.agent.decision.behavior.ikMovement.IKStepWalkBehavior;
import magma.agent.decision.behavior.ikMovement.IKWalkBehavior;
import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase;
import magma.agent.decision.behavior.movement.SidedMovementBehavior.Side;
import magma.agent.model.agentmeta.IRoboCupAgentMetaModel;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.robots.DecisionMakerConfigurationHelper;
import magma.robots.nao.decision.behavior.dynamic.FocusBall;
import magma.robots.nao.decision.behavior.dynamic.FocusBallGoalie;
import magma.robots.nao.decision.behavior.movement.GetReady;
import magma.robots.nao.decision.behavior.movement.fall.FallBack;
import magma.robots.nao.decision.behavior.movement.fall.FallForward;
import magma.robots.nao.decision.behavior.movement.fall.FallSide;
import magma.robots.nao.decision.behavior.movement.fall.MoveArmsToFall;
import magma.robots.nao.decision.behavior.movement.getup.GetUpFromBack;
import magma.robots.nao.decision.behavior.movement.getup.GetUpFromBackParameters;
import magma.robots.nao.decision.behavior.movement.getup.GetUpFromFront;
import magma.robots.nao.decision.behavior.movement.keep.KeepCenter;
import magma.robots.nao.decision.behavior.movement.keep.KeepSide;
import magma.robots.nao.model.agentmeta.NaoAgentMetaModel;
import magma.robots.nao.model.agentmodel.ik.impl.NaoLegCalculator;

public class NaoComponentFactory extends ComponentFactory
{
	@Override
	public IRoboCupAgentMetaModel getAgentMetaModel()
	{
		return NaoAgentMetaModel.INSTANCE;
	}

	protected List<String> createDefaultAvailableKicks(BehaviorMap behaviors)
	{
		List<String> kicks = new ArrayList<>();

		return kicks;
	}

	protected void addKick(BehaviorMap behaviors, List<String> kicks, SidedBehaviorConstants constants)
	{
		addKick(behaviors, kicks, constants.LEFT);
		addKick(behaviors, kicks, constants.RIGHT);
	}

	private void addKick(BehaviorMap behaviors, List<String> kicks, String name)
	{
		if (behaviors.get(name) != null) {
			kicks.add(name);
		} else {
			System.err.println("Behavior not existing: " + name);
		}
	}
	protected void removeKick(SidedBehaviorConstants constants, List<String> kicks)
	{
		kicks.remove(constants.LEFT);
		kicks.remove(constants.RIGHT);
	}

	@Override
	public IChannelManager createChannelManager(ChannelParameters info)
	{
		IChannelManager channelManager = new ChannelManager();
		IRoboCupChannel channel = new SimsparkChannel(channelManager, info);
		List<String> initParams = new ArrayList<>();
		initParams.add(getAgentMetaModel().getSceneString());
		channel.init(initParams);
		channelManager.addInputChannel(channel, true);
		channelManager.addOutputChannel((IOutputChannel) channel);
		return channelManager;
	}

	@Override
	public IAgentIKSolver createAgentIKSolver()
	{
		return new NaoLegCalculator();
	}

	@Override
	public IDecisionMaker createDecisionMaker(BehaviorMap behaviors, IThoughtModel thoughtModel, int playerNumber,
			String decisionMakerName, ParameterMap learningParam)
	{
		if (decisionMakerName.equals(IMagmaConstants.DEFAULT_DECISION_MAKER)) {
			return super.createDecisionMaker(behaviors, thoughtModel, playerNumber, decisionMakerName, learningParam);
		}

		return DecisionMakerConfigurationHelper.NAO_DECISION_MAKERS.get(decisionMakerName)
				.create(behaviors, (IRoboCupThoughtModel) thoughtModel);
	}

	@Override
	protected String getBehaviorFilesBasePath()
	{
		return "behaviors/nao/";
	}

	@Override
	protected void createSpecificBehaviors(IThoughtModel theThoughtModel, ParameterMap params, BehaviorMap behaviors)
	{
		IRoboCupThoughtModel thoughtModel = (IRoboCupThoughtModel) theThoughtModel;
		// General behaviors
		behaviors.put(new GetReady(thoughtModel));
		behaviors.put(new SearchBall(thoughtModel, behaviors));
		behaviors.put(new FocusBall(thoughtModel, behaviors));
		behaviors.put(new FocusBallGoalie(thoughtModel, behaviors));

		// GetUp behaviors
		behaviors.put(new GetUpFromBack(thoughtModel, params));
		behaviors.put(new GetUpFromFront(thoughtModel));
		behaviors.put(new MoveArmsToFall(thoughtModel));
		behaviors.put(new FallBack(thoughtModel));
		behaviors.put(new FallForward(thoughtModel));
		behaviors.put(new FallSide(thoughtModel));

		// Movement behaviors
		IBaseWalk walkBehavior = createWalk(thoughtModel, params, behaviors);
		behaviors.put(new Walk(thoughtModel, params, behaviors, walkBehavior));

		behaviors.put(new WalkToPosition(thoughtModel, behaviors, createWalkEstimator()));
		behaviors.put(new IKStepWalkBehavior(thoughtModel, params));

		// Positioning behaviors
		behaviors.put(new PassivePositioning(thoughtModel, behaviors));
		behaviors.put(new GoaliePositioning(thoughtModel, behaviors));

		// Kick behaviors

		// Keep behaviors
		behaviors.put(new KeepSide(Side.LEFT, thoughtModel));
		behaviors.put(new KeepSide(Side.RIGHT, thoughtModel));
		behaviors.put(new KeepCenter(thoughtModel));
	}

	protected IBaseWalk createWalk(IRoboCupThoughtModel thoughtModel, ParameterMap params, BehaviorMap behaviors)
	{
		return (IBaseWalk) behaviors.put(new IKWalkBehavior(thoughtModel, params));
	}

	@Override
	protected ParameterMap createSpecificParameters()
	{
		ParameterMap result = new ParameterMap();

		result.put(IK_WALK_STEP, new IKWalkMovementParametersBase());
		result.put(IK_WALK, new IKWalkMovementParametersBase());
		result.put(IK_STEP_PLAN, new IKWalkMovementParametersBase());
		result.put(IK_MOVEMENT, new IKWalkMovementParametersBase());

		result.put(STABILIZE.BASE_NAME, new IKWalkMovementParametersBase());

		result.put(GET_UP_BACK, new GetUpFromBackParameters());

		return result;
	}

	@Override
	public IWalkEstimator createWalkEstimator()
	{
		float[] speeds = {0.8f, 0.7f, 0.5f, 0.5f, 0.65f, 0.65f, 0.65f, 0.65f, 180};
		return new WalkEstimator(speeds);
	}

	@Override
	public void loadProperties()
	{
		PropertyManager.load("/properties/common.properties", "/properties/nao/nao.properties");
	}
}
