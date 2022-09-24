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
package magma.agent.model.worldmodel;

import hso.autonomy.util.geometry.PoseSpeed2D;
import java.util.List;

/**
 * Interface to access desired positions
 * @author Klaus Dorer
 */
public interface IPositionManager
{
	List<PoseSpeed2D> getDesiredPositions();

	/**
	 * @return the last position orientation in the list
	 */
	PoseSpeed2D getFinalPositionSpeed();

	/**
	 * @param posOrientation the position and orientation of desired position
	 * @param enforceFromOutside true if the position should not be changed
	 */
	void setDesiredPosition(PoseSpeed2D posOrientation, boolean enforceFromOutside);

	/**
	 * adds an intermediate position (with orientation) to the list of positions
	 * @param index the index where to add the new position
	 * @param pos the position orientation to add
	 */
	void addDesiredPosition(int index, PoseSpeed2D pos);

	/**
	 * Removes all desired positions from the list
	 */
	void clear();
}