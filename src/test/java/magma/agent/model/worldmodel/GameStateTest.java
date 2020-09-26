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

import static org.junit.jupiter.api.Assertions.assertEquals;

import magma.common.spark.PlayMode;
import magma.common.spark.PlaySide;
import org.junit.jupiter.api.Test;

/**
 * Test case for GameState enum
 *
 * @author Simon Raffeiner
 */
public class GameStateTest
{
	@Test
	public void testDetermineGameStateLeftSide()
	{
		assertEquals(GameState.BEFORE_KICK_OFF, GameState.determineGameState(PlayMode.BEFORE_KICK_OFF, PlaySide.LEFT));
		assertEquals(GameState.PLAY_ON, GameState.determineGameState(PlayMode.PLAY_ON, PlaySide.LEFT));
		assertEquals(GameState.GAME_OVER, GameState.determineGameState(PlayMode.GAME_OVER, PlaySide.LEFT));

		assertEquals(GameState.OWN_KICK_OFF, GameState.determineGameState(PlayMode.KICK_OFF_LEFT, PlaySide.LEFT));
		assertEquals(GameState.OPPONENT_KICK_OFF, GameState.determineGameState(PlayMode.KICK_OFF_RIGHT, PlaySide.LEFT));

		assertEquals(GameState.OWN_KICK_IN, GameState.determineGameState(PlayMode.KICK_IN_LEFT, PlaySide.LEFT));
		assertEquals(GameState.OPPONENT_KICK_IN, GameState.determineGameState(PlayMode.KICK_IN_RIGHT, PlaySide.LEFT));

		assertEquals(GameState.OWN_CORNER_KICK, GameState.determineGameState(PlayMode.CORNER_KICK_LEFT, PlaySide.LEFT));
		assertEquals(GameState.OPPONENT_CORNER_KICK,
				GameState.determineGameState(PlayMode.CORNER_KICK_RIGHT, PlaySide.LEFT));

		assertEquals(GameState.OWN_FREE_KICK, GameState.determineGameState(PlayMode.FREE_KICK_LEFT, PlaySide.LEFT));
		assertEquals(
				GameState.OPPONENT_FREE_KICK, GameState.determineGameState(PlayMode.FREE_KICK_RIGHT, PlaySide.LEFT));

		assertEquals(GameState.OWN_GOAL_KICK, GameState.determineGameState(PlayMode.GOAL_KICK_LEFT, PlaySide.LEFT));
		assertEquals(
				GameState.OPPONENT_GOAL_KICK, GameState.determineGameState(PlayMode.GOAL_KICK_RIGHT, PlaySide.LEFT));

		assertEquals(GameState.OWN_OFFSIDE, GameState.determineGameState(PlayMode.OFFSIDE_LEFT, PlaySide.LEFT));
		assertEquals(GameState.OPPONENT_OFFSIDE, GameState.determineGameState(PlayMode.OFFSIDE_RIGHT, PlaySide.LEFT));

		assertEquals(GameState.OWN_GOAL, GameState.determineGameState(PlayMode.GOAL_LEFT, PlaySide.LEFT));
		assertEquals(GameState.OPPONENT_GOAL, GameState.determineGameState(PlayMode.GOAL_RIGHT, PlaySide.LEFT));
	}

	@Test
	public void testDetermineGameStateRightSide()
	{
		assertEquals(GameState.BEFORE_KICK_OFF, GameState.determineGameState(PlayMode.BEFORE_KICK_OFF, PlaySide.RIGHT));
		assertEquals(GameState.PLAY_ON, GameState.determineGameState(PlayMode.PLAY_ON, PlaySide.RIGHT));
		assertEquals(GameState.GAME_OVER, GameState.determineGameState(PlayMode.GAME_OVER, PlaySide.RIGHT));

		assertEquals(GameState.OWN_KICK_OFF, GameState.determineGameState(PlayMode.KICK_OFF_RIGHT, PlaySide.RIGHT));
		assertEquals(GameState.OPPONENT_KICK_OFF, GameState.determineGameState(PlayMode.KICK_OFF_LEFT, PlaySide.RIGHT));

		assertEquals(GameState.OWN_KICK_IN, GameState.determineGameState(PlayMode.KICK_IN_RIGHT, PlaySide.RIGHT));
		assertEquals(GameState.OPPONENT_KICK_IN, GameState.determineGameState(PlayMode.KICK_IN_LEFT, PlaySide.RIGHT));

		assertEquals(
				GameState.OWN_CORNER_KICK, GameState.determineGameState(PlayMode.CORNER_KICK_RIGHT, PlaySide.RIGHT));
		assertEquals(GameState.OPPONENT_CORNER_KICK,
				GameState.determineGameState(PlayMode.CORNER_KICK_LEFT, PlaySide.RIGHT));

		assertEquals(GameState.OWN_FREE_KICK, GameState.determineGameState(PlayMode.FREE_KICK_RIGHT, PlaySide.RIGHT));
		assertEquals(
				GameState.OPPONENT_FREE_KICK, GameState.determineGameState(PlayMode.FREE_KICK_LEFT, PlaySide.RIGHT));

		assertEquals(GameState.OWN_GOAL_KICK, GameState.determineGameState(PlayMode.GOAL_KICK_RIGHT, PlaySide.RIGHT));
		assertEquals(
				GameState.OPPONENT_GOAL_KICK, GameState.determineGameState(PlayMode.GOAL_KICK_LEFT, PlaySide.RIGHT));

		assertEquals(GameState.OWN_OFFSIDE, GameState.determineGameState(PlayMode.OFFSIDE_RIGHT, PlaySide.RIGHT));
		assertEquals(GameState.OPPONENT_OFFSIDE, GameState.determineGameState(PlayMode.OFFSIDE_LEFT, PlaySide.RIGHT));

		assertEquals(GameState.OWN_GOAL, GameState.determineGameState(PlayMode.GOAL_RIGHT, PlaySide.RIGHT));
		assertEquals(GameState.OPPONENT_GOAL, GameState.determineGameState(PlayMode.GOAL_LEFT, PlaySide.RIGHT));
	}

	@Test
	public void testGameStateNone()
	{
		assertEquals(GameState.NONE, GameState.determineGameState(PlayMode.NONE, PlaySide.LEFT));
		assertEquals(GameState.NONE, GameState.determineGameState(PlayMode.NONE, PlaySide.RIGHT));
	}
}
