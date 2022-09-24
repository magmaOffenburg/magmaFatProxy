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
package magma.common.spark;

/**
 * Identifier for the team's color
 * @author kdorer
 */
public enum TeamColor
{
	BLUE,
	RED,
	UNKNOWN;

	public TeamColor getOpponent()
	{
		switch (this) {
		case BLUE:
			return RED;
		case RED:
			return BLUE;
		default:
			return UNKNOWN;
		}
	}
}
