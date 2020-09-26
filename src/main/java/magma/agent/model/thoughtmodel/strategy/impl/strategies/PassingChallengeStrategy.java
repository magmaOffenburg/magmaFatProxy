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
import magma.agent.model.thoughtmodel.strategy.impl.formations.PassingChallengeFormation;
import magma.agent.model.thoughtmodel.strategy.impl.roles.BallAcceptor;

public class PassingChallengeStrategy extends BaseStrategy
{
	public static final String NAME = "PassingChallenge";

	public PassingChallengeStrategy(IRoboCupThoughtModel thoughtModel)
	{
		super(NAME, thoughtModel);

		ownKickOffFormation = new PassingChallengeFormation();

		availableRoles.add(new BallAcceptor("dummy1", worldModel, 0.0f, 6.0f, 1.0f));
		availableRoles.add(new BallAcceptor("dummy2", worldModel, 0.0f, 6.0f, 2.0f));
		availableRoles.add(new BallAcceptor("dummy3", worldModel, 0.0f, 6.0f, 3.0f));
		availableRoles.add(new BallAcceptor("dummy4", worldModel, 0.0f, 6.0f, 4.0f));
	}
}
