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
 * Enum for mapping specific say-identifiers to say command numbers.
 *
 * @author Stefan Glaser
 */
public enum SayCommands {
	NONE("None", 0),

	WELCOME("welcome", 1),

	GOODBYE("goodbye", 2),

	READY_FOR_CUP_TRICK("ready-for-cup-trick", 10),

	DAMN_CUP("damn-cup", 11),

	DAMN_BALL("damn-ball", 12),

	SORRY_SWEATY_HANDS("sorry-sweaty-hands", 13),

	SAY_CARD1("say-card1", 20),

	SAY_CARD2("say-card2", 21),

	SAY_CARD3("say-card3", 22),

	SHOW_CARDS_AGAIN("show-cards-again", 23),

	HEAD_TINGLING("head-tingling", 30),

	LOOK_AT_HAT("look-at-hat", 31);

	private final String name;

	private final int id;

	private SayCommands(String sayName, int id)
	{
		this.name = sayName;
		this.id = id;
	}

	/**
	 * Retrieve the enum value to a behavior name
	 *
	 * @param behaviorName - the name of the behavior
	 * @return Resulting enum value
	 */
	public static SayCommands getCommandFor(String behaviorName)
	{
		for (SayCommands command : values()) {
			if (command.name.equals(behaviorName))
				return command;
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
