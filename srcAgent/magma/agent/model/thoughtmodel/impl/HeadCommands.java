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

/**
 * Enum for mapping specific behavior names to head command numbers.
 *
 * @author Stefan Glaser
 */
public enum HeadCommands {
	NONE("", 0),

	ZERO_POS("", 1),

	LOOK_AROUND("", 2),

	LOOK_TO("FocusBall", 3),

	FLIRT("", 4),

	FULL_SEARCH("fullSearch", 5);

	private final String name;

	private final int id;

	private HeadCommands(String behaviorName, int id)
	{
		this.name = behaviorName;
		this.id = id;
	}

	/**
	 * Retrieve the enum value to a behavior name
	 *
	 * @param behaviorName - the name of the behavior
	 * @return Resulting enum value
	 */
	public static HeadCommands getCommandFor(String behaviorName)
	{
		for (HeadCommands command : values()) {
			if (command.name.equals(behaviorName))
				return command;
		}
		return NONE;
	}

	/**
	 * Retrieve the behavior name by Id
	 *
	 * @param behaviorId - the Id of the behavior
	 * @return Resulting behavior
	 */
	public static HeadCommands getCommandFor(int behaviorId)
	{
		if (behaviorId < values().length) {
			return values()[behaviorId];
		}
		return NONE;
	}

	/**
	 * Retrieve the name of the behavior corresponding to this enum value.
	 *
	 * @return The name of the corresponding behavior
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Retrieve the id of the command.
	 *
	 * @return the command id
	 */
	public int getId()
	{
		return id;
	}
}
