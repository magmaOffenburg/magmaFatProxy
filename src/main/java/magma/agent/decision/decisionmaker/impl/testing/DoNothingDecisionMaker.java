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
package magma.agent.decision.decisionmaker.impl.testing;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import magma.agent.decision.decisionmaker.impl.SoccerDecisionMaker;

/**
 * Basic decision making class used for testing mainly that just beams and then
 * stands still
 *
 * @author Klaus Dorer
 */
public class DoNothingDecisionMaker extends SoccerDecisionMaker
{
	public DoNothingDecisionMaker(BehaviorMap behaviors, IThoughtModel thoughtModel)
	{
		super(behaviors, thoughtModel);
	}

	/**
	 * Called to decide if we should wait for game start or not. Default
	 * implementation waits.
	 * @return the behavior to execute, null if this part of decision does not
	 *         trigger a behavior currently
	 */
	@Override
	protected String waitForGameStart()
	{
		// this makes sure that after beaming we do nothing
		return IBehavior.NONE;
	}

	@Override
	protected String performFocusBall()
	{
		return null;
	}

	@Override
	protected String performSay()
	{
		return null;
	}
}
