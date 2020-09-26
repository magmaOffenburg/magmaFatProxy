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
package magma.robots;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.decisionmaker.IDecisionMaker;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import magma.agent.IMagmaConstants;
import magma.agent.decision.decisionmaker.impl.GoalieDecisionMaker;
import magma.agent.decision.decisionmaker.impl.SoccerDecisionMaker;
import magma.agent.decision.decisionmaker.impl.testing.DoNothingDecisionMaker;
import magma.agent.decision.decisionmaker.impl.testing.SimpleDecisionMaker;
import magma.agent.decision.decisionmaker.impl.testing.TrainingDecisionMaker;
import magma.agent.decision.decisionmaker.impl.testing.TrainingDecisionMaker2;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;

public class DecisionMakerConfigurationHelper
{
	@FunctionalInterface
	public interface DecisionMakerConstructor {
		IDecisionMaker create(BehaviorMap behaviors, IRoboCupThoughtModel thoughtModel);
	}

	public static final Map<String, DecisionMakerConstructor> NAO_DECISION_MAKERS;

	static
	{
		Map<String, DecisionMakerConstructor> nao = new LinkedHashMap<>();
		nao.put(IMagmaConstants.DEFAULT_DECISION_MAKER, null);
		nao.put("DoNothing", DoNothingDecisionMaker::new);
		nao.put("Goalie", GoalieDecisionMaker::new);
		nao.put("Soccer", SoccerDecisionMaker::new);
		nao.put("Simple", SimpleDecisionMaker::new);
		nao.put("Training", TrainingDecisionMaker::new);
		nao.put("Training2", TrainingDecisionMaker2::new);
		NAO_DECISION_MAKERS = Collections.unmodifiableMap(nao);
	}

	public static Collection<String> getDecisionMakerNames(String robotModel)
	{
		switch (robotModel) {
		default:
			return NAO_DECISION_MAKERS.keySet();
		}
	}
}
