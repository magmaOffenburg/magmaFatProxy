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
package magma.agent.decision.behavior.basic;

import hso.autonomy.agent.decision.behavior.basic.Behavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IRoboCupWorldModel;

/**
 * Abstract base class for all "simple" behaviors.
 *
 * @author Klaus Dorer
 */
public abstract class RoboCupBehavior extends Behavior
{
	public RoboCupBehavior(String name, IThoughtModel thoughtModel)
	{
		super(name, thoughtModel);
	}

	public IRoboCupThoughtModel getThoughtModel()
	{
		return (IRoboCupThoughtModel) super.getThoughtModel();
	}

	public IRoboCupWorldModel getWorldModel()
	{
		return (IRoboCupWorldModel) super.getWorldModel();
	}

	public IRoboCupAgentModel getAgentModel()
	{
		return (IRoboCupAgentModel) super.getAgentModel();
	}
}
