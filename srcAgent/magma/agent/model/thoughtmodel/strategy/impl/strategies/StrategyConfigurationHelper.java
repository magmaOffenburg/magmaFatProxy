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
package magma.agent.model.thoughtmodel.strategy.impl.strategies;

import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.strategy.ITeamStrategy;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class StrategyConfigurationHelper
{
	public static final String DEFAULT_STRATEGY = ReactToGameStateStrategy.NAME;

	@FunctionalInterface
	public interface StrategyConstructor {
		ITeamStrategy create(IRoboCupThoughtModel thoughtModel);
	}

	public static final Map<String, StrategyConstructor> STRATEGIES;

	static
	{
		Map<String, StrategyConstructor> strategies = new LinkedHashMap<>();
		strategies.put(ManToManMarkerStrategy.NAME, ManToManMarkerStrategy::new);
		strategies.put(AcceptorStrategy.NAME, AcceptorStrategy::new);
		strategies.put(ReactToGameStateStrategy.NAME, ReactToGameStateStrategy::new);
		strategies.put(KeepAwayChallengeStrategy.NAME, KeepAwayChallengeStrategy::new);
		strategies.put(NoWingJustAcceptorStrategy.NAME, NoWingJustAcceptorStrategy::new);
		strategies.put(TwoBallAcceptorStrategy.NAME, TwoBallAcceptorStrategy::new);
		strategies.put(PenaltyDefenderStrategy.NAME, PenaltyDefenderStrategy::new);
		STRATEGIES = Collections.unmodifiableMap(strategies);
	}
}
