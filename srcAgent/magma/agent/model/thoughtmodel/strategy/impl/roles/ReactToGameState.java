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
package magma.agent.model.thoughtmodel.strategy.impl.roles;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.GameState;
import magma.agent.model.worldmodel.IPlayer;

import hso.autonomy.util.geometry.Area2D;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;

public class ReactToGameState extends Role
{
	private final IRoboCupThoughtModel thoughtModel;

	private final Area2D.Float ownCornerKickArea;

	private final Role defaultRole;

	private final FieldArea fieldArea;

	private float opponentKickOffTime;

	public ReactToGameState(Role defaultRole, IRoboCupThoughtModel thoughtModel, FieldArea fieldArea)
	{
		super(defaultRole.worldModel, defaultRole.name, defaultRole.basePriority, defaultRole.minX, defaultRole.maxX);

		this.thoughtModel = thoughtModel;
		this.defaultRole = defaultRole;
		this.fieldArea = fieldArea;

		ownCornerKickArea = createOwnCornerKickArea(fieldArea);
	}

	private Area2D.Float createOwnCornerKickArea(FieldArea fieldArea)
	{
		switch (fieldArea) {
		case UPPER:
			return new Area2D.Float(worldModel.fieldHalfLength() - 6, worldModel.fieldHalfLength() - 2, 0,
					worldModel.fieldHalfWidth() / 2);
		case LOWER:
			return new Area2D.Float(worldModel.fieldHalfLength() - 6, worldModel.fieldHalfLength() - 2,
					-worldModel.fieldHalfWidth() / 2, 0);
		default:
			throw new IllegalArgumentException("unexpected enum value: " + fieldArea);
		}
	}

	@Override
	protected IPose2D determinePosition()
	{
		GameState gameState = worldModel.getGameState();

		if (gameState == GameState.OPPONENT_KICK_OFF) {
			opponentKickOffTime = worldModel.getGameTime();
		}

		if (opponentKickOffTime != 0 && worldModel.getGameTime() - opponentKickOffTime < 10 &&
				worldModel.getBall().getPosition().getX() > 0) {
			return worldModel.getThisPlayer().getPose2D();
		}

		if (gameState == GameState.OWN_CORNER_KICK) {
			return determinePosition(ownCornerKickArea);
		}

		if (gameState == GameState.OWN_KICK_IN || gameState == GameState.OWN_GOAL_KICK ||
				gameState == GameState.OWN_FREE_KICK ||
				(!thoughtModel.getOpponentsAtBallList().isEmpty() &&
						thoughtModel.getOpponentsAtBallList().get(0).getDistanceToXY(worldModel.getBall()) > 5)) {
			return determinePosition(calculateArea(true));
		}

		if (gameState == GameState.OPPONENT_KICK_IN || gameState == GameState.OPPONENT_GOAL_KICK ||
				gameState == GameState.OPPONENT_FREE_KICK) {
			return determinePosition(calculateArea(false));
		}

		return defaultRole.determinePosition();
	}

	private Area2D.Float calculateArea(boolean ownKick)
	{
		final double ballX = worldModel.getBall().getPosition().getX();
		final double targetX;
		if (ownKick) {
			targetX = (ballX + worldModel.fieldHalfLength()) / 2;
		} else {
			targetX = (ballX - worldModel.fieldHalfLength()) / 2;
		}

		final double minY;
		final double maxY;
		if (fieldArea == FieldArea.UPPER) {
			minY = 0;
			maxY = worldModel.fieldHalfWidth() / 2;
		} else {
			minY = -worldModel.fieldHalfWidth() / 2;
			maxY = 0;
		}

		return new Area2D.Float(targetX - 2, targetX + 2, minY, maxY);
	}

	private Pose2D determinePosition(Area2D.Float area)
	{
		double maxUtility = Double.MIN_VALUE;
		Pose2D targetPos = null;

		for (float x = area.getMinX(); x <= area.getMaxX(); x++) {
			for (float y = area.getMinY(); y <= area.getMaxY(); y++) {
				double utility = calculateUtility(x, y);
				if (utility > maxUtility) {
					maxUtility = utility;
					targetPos = new Pose2D(x, y);
				}
			}
		}
		return targetPos;
	}

	private double calculateUtility(float x, float y)
	{
		double minDistance = Double.MAX_VALUE;
		List<IPlayer> players = new ArrayList<>(worldModel.getVisiblePlayers());
		for (IPlayer player : players) {
			double distance = player.getPosition().distance(new Vector3D(x, y, 0));
			if (distance < minDistance) {
				minDistance = distance;
			}
		}
		return minDistance;
	}
}
