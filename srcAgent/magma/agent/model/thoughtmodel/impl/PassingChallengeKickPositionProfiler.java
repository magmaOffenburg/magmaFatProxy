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

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.ISoccerPitchDescription;

import hso.autonomy.util.geometry.Area2D;

public class PassingChallengeKickPositionProfiler extends KickPositionProfiler
{
	public PassingChallengeKickPositionProfiler(ISoccerPitchDescription soccerPitch)
	{
		super(soccerPitch);
	}

	protected void doEstimate(
			IRoboCupThoughtModel thoughtModel, Area2D.Float playableArea, List<KickPositionEstimation> result)
	{
		Vector3D playerPos = thoughtModel.getWorldModel().getThisPlayer().getPosition();
		Vector3D nearestPlayerPos = thoughtModel.getPlayersAtMeList().get(0).getPosition();
		Vector3D otherGoalPos = thoughtModel.getWorldModel().getOtherGoalPosition();

		if (playerPos.distance(otherGoalPos) < nearestPlayerPos.distance(otherGoalPos)) {
			result.add(new KickPositionEstimation(otherGoalPos, 1));
		} else {
			result.add(new KickPositionEstimation(
					new Vector3D(nearestPlayerPos.getX(), (nearestPlayerPos.getY() + playerPos.getY()) / 2,
							nearestPlayerPos.getZ()),
					1));
		}
	}
}
