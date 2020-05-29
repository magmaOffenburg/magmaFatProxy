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
package magma.robots.nao3.general.agentruntime;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import kdo.util.parameter.ParameterMap;
import magma.agent.decision.behavior.IBaseWalk;
import magma.agent.decision.behavior.ikMovement.IKWalkBehavior;
import magma.agent.decision.behavior.ikMovement.walk.IKDynamicWalkMovement;
import magma.agent.model.agentmeta.IRoboCupAgentMetaModel;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.robots.nao.general.agentruntime.NaoComponentFactory;
import magma.robots.nao3.decision.behavior.ikMovement.walk.IKWalkMovementParametersNao3;
import magma.robots.nao3.decision.behavior.movement.GetUpFromBackParametersNao3;
import magma.robots.nao3.model.agentmeta.Nao3AgentMetaModel;

public class Nao3ComponentFactory extends NaoComponentFactory
{
	@Override
	public IRoboCupAgentMetaModel getAgentMetaModel()
	{
		return Nao3AgentMetaModel.INSTANCE;
	}

	@Override
	protected IBaseWalk createWalk(IRoboCupThoughtModel thoughtModel, ParameterMap params, BehaviorMap behaviors)
	{
		params.put(IKDynamicWalkMovement.NAME, new IKWalkMovementParametersNao3());
		return (IBaseWalk) behaviors.put(new IKWalkBehavior(thoughtModel, params));
	}

	@Override
	protected ParameterMap createSpecificParameters()
	{
		ParameterMap result = super.createSpecificParameters();
		result.put(GET_UP_BACK, new GetUpFromBackParametersNao3());
		return result;
	}
}
