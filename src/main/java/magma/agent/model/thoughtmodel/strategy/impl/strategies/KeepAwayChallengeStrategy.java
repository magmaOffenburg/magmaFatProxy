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
import magma.agent.model.thoughtmodel.strategy.impl.roles.KeepAwayChallengeBallAcceptor;

public class KeepAwayChallengeStrategy extends BaseStrategy
{
	public static final String NAME = "KeepAwayChallenge";

	public KeepAwayChallengeStrategy(IRoboCupThoughtModel thoughtModel)
	{
		super(NAME, thoughtModel);

		availableRoles.add(new KeepAwayChallengeBallAcceptor("Acceptor1", 1, thoughtModel));
		availableRoles.add(new KeepAwayChallengeBallAcceptor("Acceptor2", 1, thoughtModel));
	}
}
