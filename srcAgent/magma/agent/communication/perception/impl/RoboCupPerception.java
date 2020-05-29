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
package magma.agent.communication.perception.impl;

import java.util.List;
import java.util.stream.Collectors;

import hso.autonomy.agent.communication.perception.impl.Perception;
import magma.agent.communication.perception.IActionPerceptor;
import magma.agent.communication.perception.IAgentStatePerceptor;
import magma.agent.communication.perception.ICameraTiltPerceptor;
import magma.agent.communication.perception.IGameStatePerceptor;
import magma.agent.communication.perception.IHearPerceptor;
import magma.agent.communication.perception.IPlayerPos;
import magma.agent.communication.perception.IProxyPerceptor;
import magma.agent.communication.perception.IRoboCupPerception;

/**
 * Represents all data the agent is able to perceive from its environment.
 * Should be updated in every simulation cycle.
 *
 * @author Klaus Dorer, Simon Raffeiner, Stefan Glaser
 */
public class RoboCupPerception extends Perception implements IRoboCupPerception
{
	@Override
	public IAgentStatePerceptor getAgentState()
	{
		return (IAgentStatePerceptor) perceptors.get("AgentState");
	}

	@Override
	public IGameStatePerceptor getGameState()
	{
		return (IGameStatePerceptor) perceptors.get(IGameStatePerceptor.NAME);
	}

	@Override
	public List<IHearPerceptor> getHearPerceptors()
	{
		return perceptors.values()
				.stream()
				.filter(perceptor -> perceptor instanceof IHearPerceptor)
				.map(perceptor -> (IHearPerceptor) perceptor)
				.collect(Collectors.toList());
	}

	@Override
	public ICameraTiltPerceptor getCameraTiltPerceptor()
	{
		return (ICameraTiltPerceptor) perceptors.get("cameraTilt");
	}

	@Override
	public IActionPerceptor getActionPerceptor()
	{
		return (IActionPerceptor) perceptors.get("action");
	}

	@Override
	public List<IPlayerPos> getVisiblePlayers()
	{
		return perceptors.values()
				.stream()
				.filter(perceptor -> perceptor instanceof IPlayerPos)
				.map(perceptor -> (IPlayerPos) perceptor)
				.collect(Collectors.toList());
	}

	@Override
	public IProxyPerceptor getProxyPerceptor()
	{
		List<IProxyPerceptor> commands = perceptors.values()
												 .stream()
												 .filter(perceptor -> perceptor instanceof IProxyPerceptor)
												 .map(perceptor -> (IProxyPerceptor) perceptor)
												 .collect(Collectors.toList());
		if (commands.isEmpty()) {
			return null;
		} else {
			return commands.get(0);
		}
	}
}