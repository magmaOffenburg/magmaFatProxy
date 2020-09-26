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

import java.util.ArrayList;
import java.util.List;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.strategy.IFormation;
import magma.agent.model.thoughtmodel.strategy.IRole;
import magma.agent.model.thoughtmodel.strategy.ITeamStrategy;
import magma.agent.model.thoughtmodel.strategy.impl.formations.OpponentKickOffFormation;
import magma.agent.model.thoughtmodel.strategy.impl.formations.OwnKickOffFormation;
import magma.agent.model.worldmodel.GameState;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.common.spark.PlaySide;

/**
 * @author Stephan Kammerer
 */
public abstract class BaseStrategy implements ITeamStrategy
{
	/** All available roles which can be applied */
	protected final ArrayList<IRole> availableRoles = new ArrayList<>();

	private String name;

	protected final IRoboCupThoughtModel thoughtModel;

	protected final IRoboCupWorldModel worldModel;

	protected IFormation ownKickOffFormation = new OwnKickOffFormation();

	protected IFormation opponentKickOffFormation;

	public BaseStrategy(String name, IRoboCupThoughtModel thoughtModel)
	{
		this.name = name;
		this.thoughtModel = thoughtModel;
		this.worldModel = this.thoughtModel.getWorldModel();
		opponentKickOffFormation = new OpponentKickOffFormation(thoughtModel);
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public List<IRole> getAvailableRoles()
	{
		return availableRoles;
	}

	@Override
	public IRole getRoleForPlayerID(int playerID)
	{
		if (playerID < 1 || availableRoles.size() < playerID) {
			return null;
		}
		return availableRoles.get(playerID - 1);
	}

	@Override
	public IFormation getFormation()
	{
		// we assume that the left team always has kickoff because
		// the server is usually restarted for each half in competitions
		GameState gameState = worldModel.getGameState();
		if ((gameState == GameState.BEFORE_KICK_OFF && worldModel.getPlaySide() == PlaySide.LEFT) ||
				gameState == GameState.OPPONENT_GOAL || gameState == GameState.OWN_KICK_OFF) {
			return ownKickOffFormation;
		}
		return opponentKickOffFormation;
	}
}
