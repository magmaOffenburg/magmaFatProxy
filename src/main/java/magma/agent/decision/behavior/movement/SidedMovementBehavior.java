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
package magma.agent.decision.behavior.movement;

import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;

/**
 * A MovementBehavior which supports two versions - one for the left, one for
 * the right side.
 */
public class SidedMovementBehavior extends MovementBehavior
{
	public enum Side { LEFT, RIGHT }

	protected Side side;

	private String baseName;

	protected boolean flipSides;

	/**
	 * Creates a MovementBehavior that supports both a left and a right side.
	 * @param side which side
	 * @param baseName the name without "Left" or "Right"
	 * @param initialMovement the movement - assumed to the right version, will
	 *        be flipped if side == LEFT
	 */
	public SidedMovementBehavior(Side side, String baseName, IThoughtModel thoughtModel, Movement initialMovement)
	{
		super(null, thoughtModel, initialMovement);
		this.side = side;
		this.baseName = baseName;

		name = baseName + (side == Side.LEFT ? "Left" : "Right");
		// the getLeftVersion() call is delayed so subclasses can still change
		// initialMovement
		flipSides = side == Side.LEFT;
	}

	@Override
	public void perform()
	{
		if (flipSides) {
			initialMovement = currentMovement = initialMovement.getLeftVersion();
			flipSides = false;
		}

		super.perform();
	}

	public String getBaseName()
	{
		return baseName;
	}
}
