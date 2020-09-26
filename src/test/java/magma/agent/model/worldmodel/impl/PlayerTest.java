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

import static org.junit.Assert.assertEquals;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Geometry;
import java.util.HashMap;
import java.util.Map;
import magma.agent.IMagmaConstants;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Klaus Dorer, korak
 *
 */
public class PlayerTest
{
	private static final float TEST_RUN_SPEED = 0.5f;

	private static final float TEST_TURN_SPEED = 20.0f;

	private static final float TEST_SIDE_SPEED = 0.1f;

	private Player testee;

	@Before
	public void setUp() throws Exception
	{
		testee = new Player(0, "testTeam", true, 0.02f);
		testee.forwardSpeed = TEST_RUN_SPEED;
		testee.turnSpeed = TEST_TURN_SPEED;
		testee.sideStepSpeed = TEST_SIDE_SPEED;
		testee.setPosition(new Vector3D(0.0, 0.0, 0.0));
		testee.setLastSeenTime(5.0f);
		testee.setGlobalOrientation(Rotation.IDENTITY);
	}

	/**
	 * Test for {@link Player#getTimeToTurnAndRun(Vector3D, Angle)}
	 */
	@Test
	public void testGetTimeToTurnAndRunAlreadyThere()
	{
		Vector3D position = new Vector3D(0.0, 0.0, 0.0);
		Angle directionAtTarget = Angle.deg(0);
		assertEquals(0.0, testee.getTimeToTurnAndRun(position, directionAtTarget), 0.001);
	}

	/**
	 * Test for {@link Player#getTimeToTurnAndRun(Vector3D, Angle)}
	 */
	@Test
	public void testGetTimeToTurnAndRunNoTurnButRun()
	{
		Vector3D position = new Vector3D(3.0, 0.0, 0.0);
		Angle directionAtTarget = Angle.deg(0);
		assertEquals(3.0 / TEST_RUN_SPEED, testee.getTimeToTurnAndRun(position, directionAtTarget), 0.001);
	}

	/**
	 * Test for {@link Player#getTimeToTurnAndRun(Vector3D, Angle)}
	 */
	@Test
	public void testGetTimeToTurnAndRunTurnAtStart()
	{
		Vector3D position = new Vector3D(0.0, 0.0, 0.0);
		Angle directionAtTarget = Angle.deg(0);
		testee.setGlobalOrientation(Geometry.createZRotation(Math.toRadians(-60)));
		assertEquals(60 / TEST_TURN_SPEED, testee.getTimeToTurnAndRun(position, directionAtTarget), 0.001);
	}

	/**
	 * Test for {@link Player#getTimeToTurnAndRun(Vector3D, Angle)}
	 */
	@Test
	public void testGetTimeToTurnAndRunTurnAtEnd()
	{
		Vector3D position = new Vector3D(0.0, 0.0, 0.0);
		Angle directionAtTarget = Angle.deg(80);
		assertEquals(80 / TEST_TURN_SPEED, testee.getTimeToTurnAndRun(position, directionAtTarget), 0.001);
	}

	/**
	 * Test for {@link Player#getTimeToTurnAndRun(Vector3D, Angle)}
	 */
	@Test
	public void testGetTimeToTurnAndRunAll()
	{
		Vector3D position = new Vector3D(-3.0, 0.0, 0.0);
		Angle directionAtTarget = Angle.deg(160);
		testee.setGlobalOrientation(Geometry.createZRotation(Math.toRadians(-160)));
		float expected = (float) (20 / TEST_TURN_SPEED + 3.0 / TEST_RUN_SPEED + 20 / TEST_TURN_SPEED);
		assertEquals(expected, testee.getTimeToTurnAndRun(position, directionAtTarget), 0.001);
	}

	/**
	 * Test for {@link Player#getTimeForSideStep(Vector3D, Angle, boolean)}
	 */
	@Test
	public void testGetTimeForSideStep()
	{
		Vector3D position = new Vector3D(3.0, 0.0, 0.0);
		Angle directionAtTarget = Angle.deg(80);
		float expected = (float) (90 / TEST_TURN_SPEED + 3.0 / TEST_SIDE_SPEED + 170 / TEST_TURN_SPEED);
		assertEquals(expected, testee.getTimeForSideStep(position, directionAtTarget, true), 0.001);
	}

	/*
	 * Test for {@Link Player#updateLying(float time)}
	 */
	@Test
	public void testSetLying()
	{
		// Test the case in which we're lying and no time has passed
		Map<String, Vector3D> allBodyParts = new HashMap<>();
		allBodyParts.put("a", new Vector3D(0, 0, 0));
		allBodyParts.put("b", new Vector3D(0, 0, 0));
		allBodyParts.put("c", new Vector3D(0, 0, 0));
		testee.setBodyParts(allBodyParts);
		testee.updateLying(0);
		assertEquals(true, testee.isLying());

		// Test the case in which we're standing and little time has passed
		allBodyParts = new HashMap<>();
		allBodyParts.put("a", new Vector3D(0, 0, 0.4));
		allBodyParts.put("b", new Vector3D(0, 0, 0));
		allBodyParts.put("c", new Vector3D(0, 0, 0));
		testee.setBodyParts(allBodyParts);
		testee.updateLying(0.2f);
		assertEquals(true, testee.isLying());

		// Test the case in which we're standing and the time has almost passed
		allBodyParts = new HashMap<>();
		allBodyParts.put("a", new Vector3D(0, 0, 0.4));
		allBodyParts.put("b", new Vector3D(0, 0, 0));
		allBodyParts.put("c", new Vector3D(0, 0, 0));
		testee.setBodyParts(allBodyParts);
		testee.updateLying(1.9f);
		assertEquals(true, testee.isLying());

		// Test the case in which we're standing and the time has passed
		allBodyParts = new HashMap<>();
		allBodyParts.put("a", new Vector3D(0, 0, 0.4));
		allBodyParts.put("b", new Vector3D(0, 0, 0));
		allBodyParts.put("c", new Vector3D(0, 0, 0));
		testee.setBodyParts(allBodyParts);
		testee.updateLying(IMagmaConstants.TIME_DELAY_LYING);
		assertEquals(false, testee.isLying());
	}

	/*
	 * anotherTest for {@Link Player#updateLying(float time)}
	 */
	@Test
	public void testIsLying()
	{
		// Test the case in which we're standing and no time has passed
		Map<String, Vector3D> allBodyParts = new HashMap<>();
		allBodyParts.put("a", new Vector3D(0, 0, 0.4));
		allBodyParts.put("b", new Vector3D(0, 0, 0));
		allBodyParts.put("c", new Vector3D(0, 0, 0));
		testee.setBodyParts(allBodyParts);
		testee.updateLying(0);
		assertEquals(false, testee.isLying());

		// Test the case in which we're standing and little time has passed
		allBodyParts = new HashMap<>();
		allBodyParts.put("a", new Vector3D(0, 0, 0.0));
		allBodyParts.put("b", new Vector3D(0, 0, 0));
		allBodyParts.put("c", new Vector3D(0, 0, 0));
		testee.setBodyParts(allBodyParts);
		testee.updateLying(0.2f);
		assertEquals(true, testee.isLying());
	}
}
