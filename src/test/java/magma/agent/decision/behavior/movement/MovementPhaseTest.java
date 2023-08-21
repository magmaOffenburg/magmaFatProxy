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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author kdorer
 *
 */
public class MovementPhaseTest
{
	private MovementPhase testee;

	private MovementPhase previous;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception
	{
		previous = new MovementPhase("previous", 10).add("J1", 10);
		testee = new MovementPhase("testee", 10).add("J1", 30).add("J2", 40);
	}

	/**
	 * Test method for
	 * {@link
	 * magma.agent.decision.behavior.movement.MovementPhase#setSpeeds(magma.agent.decision.behavior.movement.MovementPhase)}
	 * .
	 */
	@Test
	public void testSetSpeedsWithPreviousPhase()
	{
		testee.setSpeeds(previous);
		assertEquals(2.0f, testee.find("J1").getSpeed(), 0.001);
		assertEquals(4.0f, testee.find("J2").getSpeed(), 0.001);
	}

	@Test
	public void testSetSpeedsNoPreviousPhase()
	{
		testee.setSpeeds(null);
		assertEquals(3.0f, testee.find("J1").getSpeed(), 0.001);
	}

	@Test
	public void testSetSpeedsEmptyCycle()
	{
		testee = new MovementPhase("testee", 0).add("J1", 30);
		testee.setSpeeds(previous);
		assertEquals(1.0f, testee.find("J1").getSpeed(), 0.001);
	}

	@Test
	public void testSetSpeedsNegative()
	{
		testee = new MovementPhase("testee", 10).add("J1", -10);
		testee.setSpeeds(previous);
		assertEquals(2.0f, testee.find("J1").getSpeed(), 0.001);
	}
}
