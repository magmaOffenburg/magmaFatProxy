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
package magma.agent.model.thoughtmodel.strategy.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import hso.autonomy.agent.model.worldmodel.IWorldModel;
import magma.agent.model.thoughtmodel.strategy.IRole;
import magma.agent.model.thoughtmodel.strategy.IRoleManager;
import magma.agent.model.thoughtmodel.strategy.ITeamStrategy;
import magma.agent.model.thoughtmodel.strategy.impl.roles.DummyRole;
import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.agent.model.worldmodel.IThisPlayer;

/**
 * The RolePositionManager sets up a strategy for the whole team. Here several
 * strategies could be set up.
 *
 * @author Stephan Kammerer
 */
public class RoleManager implements IRoleManager
{
	private final RolePriorityComparator comparator = new RolePriorityComparator();

	private final ITeamStrategy strategy;

	private final IRoboCupWorldModel worldModel;

	/**
	 * This unit cares about the different roles and how they work together or
	 * with each other. Calculation of priorities.
	 */
	public RoleManager(IWorldModel worldModel, ITeamStrategy strategy)
	{
		this.worldModel = (IRoboCupWorldModel) worldModel;
		this.strategy = strategy;
	}

	@Override
	public IRole determineRole(IPlayer closestPlayer, int playerID)
	{
		// If we are goalie or closest player we can immediately return.
		IThisPlayer thisPlayer = worldModel.getThisPlayer();
		if (thisPlayer.isGoalie() || thisPlayer == closestPlayer) {
			return DummyRole.INSTANCE;
		}

		// TODO: implement dynamic strategy changes based on ball position

		HashMap<IPlayer, IRole> roleAssignments = assignRoles(getAssignablePlayers(closestPlayer), getAvailableRoles());
		IRole result = roleAssignments.get(thisPlayer);
		if (result == null) {
			System.err.println("Unexpected null role: " + roleAssignments);
			return DummyRole.INSTANCE;
		}
		return result;
	}

	@Override
	public ITeamStrategy getStrategy()
	{
		return strategy;
	}

	private List<IPlayer> getAssignablePlayers(IPlayer closestPlayer)
	{
		List<IPlayer> remainingPlayers =
				worldModel.getVisiblePlayers()
						.stream()
						.filter(player -> player.isOwnTeam() && !player.isGoalie() && player != closestPlayer)
						.collect(Collectors.toList());

		if (!remainingPlayers.contains(worldModel.getThisPlayer())) {
			remainingPlayers.add(worldModel.getThisPlayer());
		}
		return remainingPlayers;
	}

	private List<IRole> getAvailableRoles()
	{
		List<IRole> availableRoles = new ArrayList<>(strategy.getAvailableRoles());
		availableRoles.forEach(IRole::update);
		availableRoles.sort(comparator);
		return availableRoles;
	}

	private HashMap<IPlayer, IRole> assignRoles(List<IPlayer> assignablePlayers, List<IRole> availableRoles)
	{
		HashMap<IPlayer, IRole> roleAssignments = new HashMap<>();
		while (assignablePlayers.size() > 0) {
			IPlayer closestPlayer = null;
			double closestDistance = 0;
			IRole currentRole = availableRoles.get(0);
			Vector2D position = currentRole.getIndependentTargetPose().getPosition();
			for (IPlayer remainingPlayer : assignablePlayers) {
				double currentDistance = remainingPlayer.getDistanceToXY(position);
				if (closestPlayer == null || currentDistance < closestDistance) {
					closestPlayer = remainingPlayer;
					closestDistance = currentDistance;
				}
			}

			roleAssignments.put(closestPlayer, currentRole);
			assignablePlayers.remove(closestPlayer);
			availableRoles.remove(0);
		}
		return roleAssignments;
	}

	private class RolePriorityComparator implements Comparator<IRole>
	{
		@Override
		public int compare(IRole o1, IRole o2)
		{
			return Float.compare(o2.getPriority(), o1.getPriority());
		}
	}
}
