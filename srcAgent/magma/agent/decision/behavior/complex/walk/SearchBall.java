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
package magma.agent.decision.behavior.complex.walk;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.decision.behavior.complex.SingleComplexBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IWalk;

/**
 * Search for the ball
 * @author Maximilian Kroeg
 */
public class SearchBall extends SingleComplexBehavior
{
	public SearchBall(IThoughtModel thoughtModel, BehaviorMap behaviors)
	{
		super(IBehaviorConstants.SEARCH_BALL, thoughtModel, behaviors);
	}

	@Override
	public IBehavior decideNextBasicBehavior()
	{
		IWalk walk = (IWalk) behaviors.get(IBehaviorConstants.WALK);
		walk.walk(0, 0, 60);
		return walk;
	}
}
