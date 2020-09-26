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
package magma.agent.decision.behavior;

import hso.autonomy.util.geometry.PoseSpeed2D;
import java.util.List;
import magma.agent.model.worldmodel.IThisPlayer;

public interface IWalkEstimator {
	enum WalkMode
	{
		FORWARD,
		BACKWARD,
		LEFT_SIDE,
		RIGHT_SIDE,
		DIAGONAL_LEFT,
		DIAGONAL_RIGHT,
		DIAGONAL_BACK_LEFT,
		DIAGONAL_BACK_RIGHT
	}

	/**
	 * Calculates if it is best to walk forward, backward, sideward or diagonal
	 * @param thisPlayer information about this player's state in the world
	 * @param poses list of (at most 2) poses where to go to
	 * @return the best walk mode given the passed poses
	 */
	WalkMode getFastestWalkMode(IThisPlayer thisPlayer, List<PoseSpeed2D> poses);

	/**
	 * Calculates the estimated time it requires to walk
	 * @param thisPlayer information about this player's state in the world
	 * @param poses list of (at most 2) poses where to go to
	 * @return the time to walk the poses with the best walk mode
	 */
	float getFastestWalkTime(IThisPlayer thisPlayer, List<PoseSpeed2D> poses);

	/**
	 * @return the speed we are able to turn (in degrees per second)
	 */
	float getTurningSpeed();

	/**
	 * @param walkMode the walk mode for which to get the speed
	 * @return the speed of the walkMode in m/s
	 */
	float getWalkSpeed(WalkMode walkMode);

	/**
	 * @param walkMode the walk mode for which to set the speed
	 * @param speed the speed to set in m/s
	 */
	void setWalkSpeed(WalkMode walkMode, float speed);
}