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

import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import magma.agent.decision.behavior.IBehaviorConstants;

/**
 * This Behavior is to beam the agent somewhere to the field
 *
 * @author Ingo Schindler
 */
public class BeamHome extends BeamToPosition
{
	public BeamHome(IThoughtModel thoughtModel)
	{
		super(IBehaviorConstants.BEAM_HOME, thoughtModel);
	}

	@Override
	public void perform()
	{
		// the position is based on if we play left to right or right to left
		setPose(getThoughtModel().getHomePose());

		super.perform();

		// Perform initial pose
		getAgentModel().getFutureBodyModel().performInitialPose();
	}

	@Override
	public IBehavior switchFrom(IBehavior actualBehavior)
	{
		// the beam behavior has the highest decision priority, therefore either
		// leave or abort the actual behavior
		if (actualBehavior.isFinished()) {
			actualBehavior.onLeavingBehavior(this);
		} else {
			actualBehavior.abort();
		}

		return this;
	}
}
