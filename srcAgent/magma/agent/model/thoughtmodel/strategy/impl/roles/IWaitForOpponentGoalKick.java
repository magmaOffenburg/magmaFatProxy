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

import java.util.Optional;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import magma.agent.model.worldmodel.GameState;
import magma.agent.model.worldmodel.IRoboCupWorldModel;

public interface IWaitForOpponentGoalKick {
default Optional
	<Vector3D> waitForOpponentGoalKick(IRoboCupWorldModel worldModel, boolean leftSide)
	{
		if (worldModel.getGameState() == GameState.OPPONENT_GOAL_KICK) {
			// we should wait for the ball at the corner
			final float cornerX = worldModel.fieldHalfLength() - 2.9f;
			final float cornerY = leftSide ? 5.4f : -5.4f;
			return Optional.of(new Vector3D(cornerX, cornerY, 0));
		}
		return Optional.empty();
	}
}
