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
import magma.agent.decision.behavior.IBehaviorConstants;

/**
 * @author Klaus Dorer This is a class which sends each effector a command to
 *         stop
 */
public class StopBehavior extends Behavior
{
	public StopBehavior(IThoughtModel thoughtModel)
	{
		super(IBehaviorConstants.STOP, thoughtModel);
	}

	protected StopBehavior(String name, IThoughtModel thoughtModel)
	{
		super(name, thoughtModel);
	}

	@Override
	public void perform()
	{
		// we send a 0.0 to each effector
		getAgentModel().getFutureBodyModel().resetAllMovements();
		super.perform();
	}
}
