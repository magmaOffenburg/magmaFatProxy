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
package magma.agent.model.flags;

import magma.agent.IFlagConstants;

/**
 * Enumeration defining all available boolean flags.
 *
 * @author Stefan Glaser
 */
public enum Flag
{
	/** Flag for penalty shoot-out situation */
	PENALTY(IFlagConstants.PENALTY, false),

	/** Flag for indicating a pausing situation by hardware */
	PAUSE(IFlagConstants.PAUSE, false),

	/** Flag for indicating a pausing situation by software */
	SOFT_PAUSE(IFlagConstants.PREFIX + "softPause", false);

	/** The name of the flag. */
	private final String name;

	/** The default value to a flag. */
	private final boolean defaultValue;

	private Flag(String name, boolean defaultValue)
	{
		this.name = name;
		this.defaultValue = defaultValue;
	}

	/**
	 * @return The name of this flag.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return The default value of this flag
	 */
	public boolean getDefault()
	{
		return defaultValue;
	}

	/**
	 * Search for a Flag with a given name.
	 *
	 * @param name - The name of the flag in question
	 * @return the flag having the specified name, or null if no such flag exists
	 */
	public static Flag byName(String name)
	{
		for (Flag f : values()) {
			if (f.name.equals(name)) {
				return f;
			}
		}

		return null;
	}
}
