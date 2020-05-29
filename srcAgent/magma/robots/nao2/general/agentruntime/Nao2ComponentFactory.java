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
package magma.robots.nao2.general.agentruntime;

import kdo.util.parameter.ParameterMap;
import magma.agent.model.agentmeta.IRoboCupAgentMetaModel;
import magma.robots.nao.general.agentruntime.NaoComponentFactory;
import magma.robots.nao2.decision.behavior.movement.GetUpFromBackParametersNao2;
import magma.robots.nao2.model.agentmeta.Nao2AgentMetaModel;

public class Nao2ComponentFactory extends NaoComponentFactory
{
	@Override
	public IRoboCupAgentMetaModel getAgentMetaModel()
	{
		return Nao2AgentMetaModel.INSTANCE;
	}

	@Override
	protected ParameterMap createSpecificParameters()
	{
		ParameterMap result = super.createSpecificParameters();
		result.put(GET_UP_BACK, new GetUpFromBackParametersNao2());
		return result;
	}
}
