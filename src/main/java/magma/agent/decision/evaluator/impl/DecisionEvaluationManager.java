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
import java.util.ArrayList;
import java.util.List;
import magma.agent.decision.evaluator.IDecisionEvaluator;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;

/**
 * Evaluator composite
 * @author kdorer
 */
public class DecisionEvaluationManager implements IDecisionEvaluator
{
	/** all evaluators that should be triggered */
	private List<IDecisionEvaluator> evaluators;

	public DecisionEvaluationManager(IDecisionMaker decisionMaker, IRoboCupThoughtModel thoughtModel)
	{
		evaluators = new ArrayList<>();

		// comment all these in normal games
		// evaluators.add(new KickEvaluator(decisionMaker, thoughtModel));
		// evaluators.add(new FallEvaluator(decisionMaker, thoughtModel));
	}

	@Override
	public void evaluate()
	{
		for (IDecisionEvaluator evaluator : evaluators) {
			evaluator.evaluate();
		}
	}
}