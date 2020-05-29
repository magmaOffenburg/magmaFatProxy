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
package magma.agent.decision.decisionmaker.impl;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.decisionmaker.impl.DecisionMakerBase;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.flags.Flag;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IRoboCupWorldModel;

/**
 * Base class for all decision makers
 * @author Klaus Dorer
 */
public abstract class RoboCupDecisionMakerBase extends DecisionMakerBase
{
	public RoboCupDecisionMakerBase(BehaviorMap behaviors, IThoughtModel thoughtModel)
	{
		super(behaviors, thoughtModel);
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

	@Override
	protected void onBehaviorPerformed()
	{
		getThoughtModel().setMovementCommand(currentBehavior.getRootBehavior().getName());
	}

	protected boolean isPaused()
	{
		IRoboCupThoughtModel thoughtModel = getThoughtModel();
		return thoughtModel.getFlags().isSet(Flag.PAUSE) || thoughtModel.getFlags().isSet(Flag.SOFT_PAUSE);
	}
}
