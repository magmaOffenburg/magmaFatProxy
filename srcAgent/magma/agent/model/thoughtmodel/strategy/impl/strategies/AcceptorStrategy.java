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
import magma.agent.model.thoughtmodel.strategy.impl.roles.BallAcceptor;
import magma.agent.model.thoughtmodel.strategy.impl.roles.Ballguard;
import magma.agent.model.thoughtmodel.strategy.impl.roles.Defender;
import magma.agent.model.thoughtmodel.strategy.impl.roles.DefensiveMidfielder;
import magma.agent.model.thoughtmodel.strategy.impl.roles.Wing;
import magma.agent.model.thoughtmodel.strategy.impl.roles.Ballguard.BallguardPosition;

/**
 * @author Stephan Kammerer
 */
public class AcceptorStrategy extends BaseStrategy
{
	public static final String NAME = "Acceptor";

	public AcceptorStrategy(IRoboCupThoughtModel thoughtModel)
	{
		super(NAME, thoughtModel);

		// acceptor for passes
		availableRoles.add(new BallAcceptor("BallAcceptor", worldModel, 4.5f, 6.0f, 8f));

		// defender
		availableRoles.add(new Defender("Defender", worldModel, 7.0f));

		// defensive midfielder
		availableRoles.add(new DefensiveMidfielder("DefensiveMidfielder", worldModel, 1.5f, 6.0f));

		availableRoles.add(new Wing("Wing", worldModel, 9.0f, 5.0f));

		// guards
		availableRoles.add(new Ballguard("LeftBallguard", worldModel, BallguardPosition.LEFT, 1.5f, 2.0f, 4f));
		availableRoles.add(new Ballguard("RightBallguard", worldModel, BallguardPosition.RIGHT, 1.5f, 2.0f, 4f));
		availableRoles.add(new Ballguard("CenterSupportguard", worldModel, BallguardPosition.CENTER, 3.5f, 1f, 3.0f));
		availableRoles.add(new Ballguard("LeftSupportguard", worldModel, BallguardPosition.LEFT, 5.0f, 0.0f, 2.0f));
		availableRoles.add(new Ballguard("RightSupportguard", worldModel, BallguardPosition.RIGHT, 5.0f, 0.0f, 2.0f));
		availableRoles.add(new Ballguard("CenterBallguard", worldModel, BallguardPosition.CENTER, 2.3f, 1.5f, 1.0f));
	}
}
