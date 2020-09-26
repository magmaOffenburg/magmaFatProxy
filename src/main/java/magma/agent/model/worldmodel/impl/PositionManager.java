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
package magma.agent.model.worldmodel.impl;

import hso.autonomy.util.geometry.PoseSpeed2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import magma.agent.model.worldmodel.IPositionManager;

/**
 * Manages desired positions of a player
 *
 * @author Klaus Dorer
 */
public class PositionManager implements IPositionManager, Serializable
{
	/** positions this player wants to be at */
	private List<PoseSpeed2D> desiredPositions;

	/** true if the desired Position is set from outside the agent */
	private boolean desiredPositionEnforcedFromOutside;

	public PositionManager()
	{
		desiredPositions = new ArrayList<>();
		desiredPositionEnforcedFromOutside = false;
	}

	@Override
	public List<PoseSpeed2D> getDesiredPositions()
	{
		// creating a new list to avoid changes and concurrent modifications
		return new ArrayList<>(desiredPositions);
	}

	/**
	 * @return the last position orientation in the list
	 */
	@Override
	public PoseSpeed2D getFinalPositionSpeed()
	{
		if (desiredPositions.isEmpty()) {
			return null;
		}

		return desiredPositions.get(desiredPositions.size() - 1);
	}

	/**
	 * @param posOrientation the position and orientation of desired position
	 * @param enforceFromOutside true if the position should not be changed
	 */
	@Override
	public void setDesiredPosition(PoseSpeed2D posOrientation, boolean enforceFromOutside)
	{
		if (!desiredPositionEnforcedFromOutside || enforceFromOutside) {
			this.desiredPositions.clear();
			if (posOrientation != null) {
				desiredPositions.add(posOrientation);
			}
		}
		if (enforceFromOutside) {
			desiredPositionEnforcedFromOutside = true;
		}
	}

	/**
	 * adds an intermediate position (with orientation) to the list of positions
	 * @param index the index where to add the new position
	 * @param pos the position orientation to add
	 */
	@Override
	public void addDesiredPosition(int index, PoseSpeed2D pos)
	{
		if (pos != null) {
			desiredPositions.add(index, pos);
			if (desiredPositions.size() > 2) {
				System.err.println("desiredPositions size too large: " + desiredPositions.size());
			}
		}
	}

	/**
	 * Removes all desired positions from the list
	 */
	@Override
	public void clear()
	{
		desiredPositions.clear();
	}
}
