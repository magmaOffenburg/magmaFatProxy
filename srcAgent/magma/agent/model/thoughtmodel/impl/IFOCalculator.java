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
package magma.agent.model.thoughtmodel.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import hso.autonomy.agent.model.worldmodel.IMoveableObject;
import hso.autonomy.agent.model.worldmodel.IVisibleObject;
import magma.agent.IMagmaConstants;
import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.agent.model.worldmodel.IThisPlayer;

/**
 * Calculates indexical functional objects (Agre and Chapman) using filters and
 * comparators
 * @author Klaus Dorer
 */
public class IFOCalculator
{
	/** order criteria is distance to ball */
	private final Comparator<IPlayer> distanceToBallComparator;

	/** order criteria is distance to this player */
	private final Comparator<IVisibleObject> distanceToMeComparator;

	/** accepts only teammates and not the goalie (+base checks) */
	private final Predicate<IPlayer> teammateNotGoalieFilter;

	/** accepts only teammates (+base checks) */
	private final Predicate<IPlayer> teammateFilter;

	/** accepts only opponents (+base checks) */
	private final Predicate<IPlayer> opponentFilter;

	/** checking for age of player and that it is not this player */
	private final Predicate<IPlayer> baseFilter;

	/** list of the four goal posts for collision avoidance */
	private final List<IVisibleObject> goalPosts;

	/**
	 * Single instance created once at startup
	 */
	public IFOCalculator(IRoboCupWorldModel worldModel)
	{
		distanceToBallComparator = new DistanceToBallComparator(worldModel);
		distanceToMeComparator = new DistanceToMeComparator(worldModel);
		teammateNotGoalieFilter = new TeammateFilter(worldModel, new NotGoalieFilter(worldModel));
		teammateFilter = new TeammateFilter(worldModel);
		opponentFilter = new OpponentFilter(worldModel);
		baseFilter = new BaseFilter(worldModel);
		goalPosts = worldModel.getGoalPostObstacles();
	}

	/**
	 * @param sourceList the list containing the players to sort
	 * @return a filtered list sorted ascending by the distance of the own team's
	 *         players not including goalie to the ball
	 */
	public List<IPlayer> getTeammatesAtBall(List<IPlayer> sourceList)
	{
		return getFilteredAndSortedList(sourceList, teammateNotGoalieFilter, distanceToBallComparator);
	}

	public List<IPlayer> getTeammatesAtBallWithGoalie(List<IPlayer> sourceList)
	{
		return getFilteredAndSortedList(sourceList, teammateFilter, distanceToBallComparator);
	}

	/**
	 * @param sourceList the list containing the players to sort
	 * @return a filtered list sorted ascending by the distance of the opponent
	 *         team's players to the ball
	 */
	public List<IPlayer> getOpponentsAtBall(List<IPlayer> sourceList)
	{
		return getFilteredAndSortedList(sourceList, opponentFilter, distanceToBallComparator);
	}

	/**
	 * Filter all players currently near the ball out of the given list
	 *
	 * @param sourceList Source player list
	 * @return Filtered list
	 */
	public List<IPlayer> getPlayersAtBall(List<IPlayer> sourceList)
	{
		return getFilteredAndSortedList(sourceList, baseFilter, distanceToBallComparator);
	}

	/**
	 * @param sourceList the list containing the players to sort
	 * @return a filtered list sorted ascending by the distance of the own team's
	 *         players including goalie to me
	 */
	public List<IPlayer> getPlayersAtMe(List<IPlayer> sourceList)
	{
		return getFilteredAndSortedList(sourceList, baseFilter, distanceToMeComparator);
	}

	/**
	 * @param sourceList the list containing the players to sort
	 * @return a filtered list sorted ascending by the distance of the own team's
	 *         players including goalie to me
	 */
	public List<IPlayer> getTeammatesAtMe(List<IPlayer> sourceList)
	{
		return getFilteredAndSortedList(sourceList, teammateFilter, distanceToMeComparator);
	}

	/**
	 * @param sourceList the list containing the players to sort
	 * @return a filtered list sorted ascending by the distance of the other
	 *         team's players including goalie to me
	 */
	public List<IPlayer> getOpponentsAtMe(List<IPlayer> sourceList)
	{
		return getFilteredAndSortedList(sourceList, opponentFilter, distanceToMeComparator);
	}

	/**
	 * Retrieve a list of obstacles
	 *
	 * @param sourceList the list containing the players to sort
	 * @param ball Reference to ball object
	 * @return a filtered list sorted ascending by the distance of the other
	 *         team's players including goalie to me
	 */
	public List<IVisibleObject> getObstacles(List<IPlayer> sourceList, IMoveableObject ball)
	{
		List<IPlayer> result = filter(sourceList, baseFilter);
		List<IVisibleObject> obstacles = new ArrayList<IVisibleObject>(result);

		obstacles.addAll(goalPosts);
		if (!ball.isMoving()) {
			// ball is only obstacle if not moving
			obstacles.add(ball);
		}
		Collections.sort(obstacles, distanceToMeComparator);
		return obstacles;
	}

	/**
	 * Returns a new list containing only the elements of the source list that
	 * were accepted by the filter and are sorted using the passed comparator.
	 * Filter or comparator may be null, but it does not make sense to pass both
	 * as null since the list will then be unchanged.
	 * @param sourceList the list with the data to filter and sort
	 * @param filter the filter to use for each element of the source list, null
	 *        if no filter should be used
	 * @param comparator for sorting, null if order should not be changed
	 * @return the filtered and sorted list
	 */
	public List<IPlayer> getFilteredAndSortedList(
			List<IPlayer> sourceList, Predicate<IPlayer> filter, Comparator<? super IPlayer> comparator)
	{
		List<IPlayer> result = filter(sourceList, filter);
		if (comparator != null) {
			Collections.sort(result, comparator);
		}
		return result;
	}

	/**
	 * @param sourceList the list of players to filter
	 * @param filter the filter to use
	 * @return a new list that only contains the elements of the passed list that
	 *         passed the filter
	 */
	private List<IPlayer> filter(List<IPlayer> sourceList, Predicate<IPlayer> filter)
	{
		List<IPlayer> result = new ArrayList<>(sourceList.size());
		result.addAll(sourceList.stream()
							  .filter(player -> filter == null || filter.test(player))
							  .collect(Collectors.toList()));
		return result;
	}

	private class BaseFilter implements Predicate<IPlayer>
	{
		/** link to the world model */
		protected final IRoboCupWorldModel worldModel;

		/** decorated filter, null if none is decorated */
		protected final Predicate<IPlayer> decoratedFilter;

		public BaseFilter(IRoboCupWorldModel worldModel)
		{
			this.worldModel = worldModel;
			this.decoratedFilter = null;
		}

		public BaseFilter(IRoboCupWorldModel worldModel, Predicate<IPlayer> decoratee)
		{
			this.worldModel = worldModel;
			this.decoratedFilter = decoratee;
		}

		/**
		 * @param player the player to check
		 * @return true if the player is accepted by the filter, false if rejected
		 */
		@Override
		public boolean test(IPlayer player)
		{
			if (decoratedFilter != null) {
				boolean result = decoratedFilter.test(player);
				if (!result) {
					return false;
				}
			}

			// move this into its own filter, once this player should not be
			// filtered in one case
			if (player == null || (player.isOwnTeam() && player.getID() == worldModel.getThisPlayer().getID())) {
				// never take ourselves into account
				return false;
			}

			if (player.getAge(worldModel.getGlobalTime()) > 4.0) {
				// only take into account recently seen players
				return false;
			}

			return localAccept(player);
		}

		/**
		 * Overwrite this method in subclasses to add filter criteria
		 * @param player the player to check for acceptance
		 * @return true if the player should remain in the list
		 */
		protected boolean localAccept(IPlayer player)
		{
			return true;
		}
	}

	/**
	 * Accepts only players of own team
	 */
	private class TeammateFilter extends BaseFilter
	{
		public TeammateFilter(IRoboCupWorldModel worldModel)
		{
			super(worldModel);
		}

		public TeammateFilter(IRoboCupWorldModel worldModel, Predicate<IPlayer> decoratee)
		{
			super(worldModel, decoratee);
		}

		@Override
		public boolean localAccept(IPlayer player)
		{
			return !(player == null || !player.isOwnTeam());
		}
	}

	/**
	 * Accepts only players of other team
	 */
	private class OpponentFilter extends BaseFilter
	{
		public OpponentFilter(IRoboCupWorldModel worldModel)
		{
			super(worldModel);
		}

		public OpponentFilter(IRoboCupWorldModel worldModel, Predicate<IPlayer> decoratee)
		{
			super(worldModel, decoratee);
		}

		@Override
		public boolean localAccept(IPlayer player)
		{
			return !(player == null || player.isOwnTeam());
		}
	}

	/**
	 * Accepts only players that are not goalies
	 */
	private class NotGoalieFilter extends BaseFilter
	{
		public NotGoalieFilter(IRoboCupWorldModel worldModel)
		{
			super(worldModel);
		}

		public NotGoalieFilter(IRoboCupWorldModel worldModel, Predicate<IPlayer> decoratee)
		{
			super(worldModel, decoratee);
		}

		@Override
		public boolean localAccept(IPlayer player)
		{
			return !player.isGoalie();
		}
	}

	private abstract class IFOComparator<T> implements Comparator<T>
	{
		protected final IRoboCupWorldModel worldModel;

		public IFOComparator(IRoboCupWorldModel worldModel)
		{
			this.worldModel = worldModel;
		}
	}

	/**
	 * For player at ball calculation
	 * @author Klaus Dorer
	 */
	private class DistanceToBallComparator extends IFOComparator<IPlayer>
	{
		public DistanceToBallComparator(IRoboCupWorldModel worldModel)
		{
			super(worldModel);
		}

		@Override
		public int compare(IPlayer player1, IPlayer player2)
		{
			Vector3D ballPosition = worldModel.getBall().getPosition();
			double distance1 = player1.getDistanceToXY(ballPosition);
			if (player1.isLying()) {
				distance1 += IMagmaConstants.DISTANCE_PENALTY_LYING;
			}
			double distance2 = player2.getDistanceToXY(ballPosition);
			if (player2.isLying()) {
				distance2 += IMagmaConstants.DISTANCE_PENALTY_LYING;
			}

			return Double.compare(distance1, distance2);
		}
	}

	/**
	 * Criteria is the distance to this player
	 */
	private class DistanceToMeComparator extends IFOComparator<IVisibleObject>
	{
		public DistanceToMeComparator(IRoboCupWorldModel worldModel)
		{
			super(worldModel);
		}

		@Override
		public int compare(IVisibleObject player1, IVisibleObject player2)
		{
			IThisPlayer thisPlayer = worldModel.getThisPlayer();
			double distance1 = thisPlayer.getDistanceToXY(player1.getPosition());
			double distance2 = thisPlayer.getDistanceToXY(player2.getPosition());

			return Double.compare(distance1, distance2);
		}
	}
}
