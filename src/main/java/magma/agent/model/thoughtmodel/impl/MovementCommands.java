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

import magma.agent.decision.behavior.IBehaviorConstants;

/**
 * Enum for mapping specific behavior names to movement command numbers.
 *
 * @author Stefan Glaser
 */
public enum MovementCommands
{
	NONE("None", 0),

	GET_READY(IBehaviorConstants.GET_READY, 1),

	WALK(IBehaviorConstants.WALK, 2),

	WAVE_HANDS("WaveHands", 9),

	WAVE("Wave", 10),

	GRIND("Grind", 11),

	BOW("Bow", 12),

	NOD("Nod", 13),

	SHAKE_HEAD("ShakeHead", 14),

	Look_AROUND("LookAround", 15),

	FOCUS_CROWD("FocusCrowd", 16),

	INIT_CUP_STACK("InitCupStack", 20),

	UNSTACK_CUPS("UnstackCups", 21),

	STACK_CUPS("StackCups", 22),

	RESET_CUPS("ResetCupStack", 23),

	CUP_SIMSALABIM1("CupSimsalabim1", 24),

	CUP_SIMSALABIM2("CupSimsalabim2", 25),

	CUP_SIMSALABIM3("CupSimsalabim3", 26),

	RECOGNIZED_GAME_CARD("RecognizedCard", 30),

	FOCUS_WIZARD("FocusWizard", 31),

	FOCUS_VOLUNTEER("FocusVolunteer", 32),

	UNFOCUS_VOLUNTEER("UnfocusVolunteer", 37),

	RISE_INDEX_FINGER("RiseIndexFinger", 33),

	HIDE_EYES("HideEyes", 34),

	CHEAT("Cheat", 35),

	REVEAL_EYES("RevealEyes", 36),

	REMOVE_HAT("RemoveHat", 40),

	READY_AFTER_REMOVE_HAT("RemoveHatDone", 41);

	//	GOALIE_READY("", 8),
	//
	//	GOALIE_READY_ALIVE("", 9),
	//
	//	SIDE_STEP_LEFT("GoalieMove30cmLeft", 10),
	//
	//	SIDE_STEP_RIGHT("GoalieMove30cmRight", 11),
	//
	//	SQUAT("", 12),
	//
	//	JUMP("", 13);

	private final String name;

	private final int id;

	private MovementCommands(String behaviorName, int id)
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
	public static MovementCommands getCommandFor(String behaviorName)
	{
		for (MovementCommands command : values()) {
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
