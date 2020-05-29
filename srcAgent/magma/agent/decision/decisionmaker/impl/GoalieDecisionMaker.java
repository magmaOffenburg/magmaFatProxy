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
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IKeepBehavior;
import magma.agent.decision.behavior.base.KeepEstimator;
import magma.agent.model.worldmodel.IRoboCupWorldModel;

public class GoalieDecisionMaker extends SoccerDecisionMaker
{
	private transient KeepEstimator keepEstimator;

	public GoalieDecisionMaker(BehaviorMap behaviors, IThoughtModel thoughtModel)
	{
		super(behaviors, thoughtModel);
		keepEstimator = new KeepEstimator(thoughtModel);
		getWorldModel().setGameMode(IRoboCupWorldModel.GameMode.DEFAULT);
	}

	@Override
	protected String performFocusBall()
	{
		behaviors.get(IBehaviorConstants.FOCUS_BALL_GOALIE).perform();
		return null;
	}

	@Override
	protected String move()
	{
		if (currentBehavior instanceof IKeepBehavior) {
			return null;
		}

		String keepBehavior = keepEstimator.decideKeepBehavior();
		if (keepBehavior != null) {
			currentBehavior.abort();
			return keepBehavior;
		}

		return super.move();
	}
}
