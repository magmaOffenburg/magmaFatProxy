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
package magma.agent.decision.evaluator.impl;

import hso.autonomy.agent.decision.decisionmaker.IDecisionMaker;
import magma.agent.decision.evaluator.IDecisionEvaluator;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;

public abstract class DecisionEvaluator implements IDecisionEvaluator
{
	protected IDecisionMaker decisionMaker;
	protected IRoboCupThoughtModel thoughtModel;

	public DecisionEvaluator(IDecisionMaker decisionMaker, IRoboCupThoughtModel thoughtModel)
	{
		this.decisionMaker = decisionMaker;
		this.thoughtModel = thoughtModel;
	}
}
