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

import hso.autonomy.agent.model.worldmodel.IMoveableObject;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Geometry;
import java.util.Comparator;
import magma.agent.IMagmaConstants;
import magma.agent.model.worldmodel.GameState;
import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * For player at ball calculation
 * @author Klaus Dorer
 */
public class DistanceToBallComparator implements Comparator<IPlayer>
{
	private final IRoboCupWorldModel worldModel;

	public DistanceToBallComparator(IRoboCupWorldModel worldModel)
	{
		this.worldModel = worldModel;
	}

	@Override
	public int compare(IPlayer player1, IPlayer player2)
	{
		IMoveableObject ball = worldModel.getBall();
		Vector3D ballPosition = ball.getPosition();

		// No predictions if ball speed is too low
		if (ball.isMoving()) {
			ballPosition = ball.getFuturePosition(150);
		}

		Vector3D otherGoalPos = worldModel.getOtherGoalPosition();
		double distance1 = getDistance(player1, ballPosition, otherGoalPos);
		double distance2 = getDistance(player2, ballPosition, otherGoalPos);

		return Double.compare(distance1, distance2);
	}

	public double getDistance(IPlayer player, Vector3D ballPos, Vector3D otherGoalPos)
	{
		double distance = player.getDistanceToXY(ballPos);
		distance += getLyingPenalty(player);
		distance += getBallBehindPlayerPenalty(player, ballPos, otherGoalPos);
		distance += getMyselfBonus(player);
		return distance;
	}

	public double getBallBehindPlayerPenalty(IPlayer player, Vector3D ballPos, Vector3D otherGoalPos)
	{
		if (worldModel.getGameState() == GameState.OWN_KICK_OFF) {
			// no behind ball penalty at kickoff
			return 0;
		}

		final double DISTANCE_PENALTY_BEHIND_BALL = 2.0;

		Angle angleToGoal = player.getDirectionTo(otherGoalPos);
		Angle angleToBall = player.getDirectionTo(ballPos);
		double delta = Math.abs(angleToBall.subtract(angleToGoal).degrees());

		return Geometry.getLinearFuzzyValue(30, 150, true, delta) * DISTANCE_PENALTY_BEHIND_BALL;
	}

	private double getLyingPenalty(IPlayer player)
	{
		if (player.isLying()) {
			return IMagmaConstants.DISTANCE_PENALTY_LYING;
		}
		return 0;
	}

	private double getMyselfBonus(IPlayer player)
	{
		final double MYSELF_BONUS = 0.1;

		if (player.getID() == worldModel.getThisPlayer().getID()) {
			return -MYSELF_BONUS;
		}
		return 0;
	}
}