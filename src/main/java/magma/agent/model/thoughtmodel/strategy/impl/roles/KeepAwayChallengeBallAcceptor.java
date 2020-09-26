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

import hso.autonomy.util.geometry.Area2D;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.VectorUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IPlayer;
import magma.common.challenge.KeepAwayArea;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class KeepAwayChallengeBallAcceptor extends Role
{
	private final IRoboCupThoughtModel thoughtModel;

	public KeepAwayChallengeBallAcceptor(String name, float priority, IRoboCupThoughtModel thoughtModel)
	{
		super(thoughtModel.getWorldModel(), name, priority, 0, 0);
		this.thoughtModel = thoughtModel;
	}

	@Override
	protected IPose2D determinePosition()
	{
		Area2D.Float area = KeepAwayArea.calculate(worldModel.getGameTime()).applyBorder(1);
		List<Vector2D> cornersByBallDistance = getCornersByBallDistance(area);
		Vector2D corner1 = cornersByBallDistance.get(0);
		Vector2D corner2 = cornersByBallDistance.get(1);

		IPlayer otherAcceptor = null;
		IPlayer closestToBall = thoughtModel.getClosestOwnPlayerAtBall();
		for (IPlayer player : worldModel.getVisiblePlayers()) {
			if (player != closestToBall) {
				otherAcceptor = player;
				break;
			}
		}

		if (otherAcceptor == null) {
			return new Pose2D(corner1);
		}

		Vector2D ownPos = VectorUtils.to2D(worldModel.getThisPlayer().getPosition());
		Vector2D otherAcceptorPos = VectorUtils.to2D(otherAcceptor.getPosition());
		if (ownPos.distance(corner1) < otherAcceptorPos.distance(corner1)) {
			return new Pose2D(corner1);
		}
		return new Pose2D(corner2);
	}

	private List<Vector2D> getCornersByBallDistance(Area2D.Float area)
	{
		final Vector2D ballPos = VectorUtils.to2D(worldModel.getBall().getPosition());

		List<Vector2D> corners = new ArrayList<>();
		corners.add(area.getTopLeft());
		corners.add(area.getTopRight());
		corners.add(area.getBottomLeft());
		corners.add(area.getBottomRight());

		Collections.sort(corners, (p1, p2) -> Double.compare(p2.distance(ballPos), p1.distance(ballPos)));
		return corners;
	}
}
