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
package magma.agent.model.worldmodel.localizer.impl;

import static java.lang.Math.toRadians;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Before;
import org.junit.Test;

import hso.autonomy.agent.model.worldmodel.localizer.impl.LocalizerTriangulation;
import hso.autonomy.util.geometry.Pose3D;
import hso.autonomy.util.misc.FuzzyCompare;

/**
 * Unit test for {@link LocalizerTriangulation}
 *
 * @author Klaus Dorer
 */
public class LocalizerTriangulationTest extends LocalizerBaseTest
{
	@Override
	@Before
	public void setUp()
	{
		super.setUp();
		testee = new LocalizerTriangulation();
	}

	/**
	 * Test for
	 * {@link LocalizerTriangulation#localize(java.util.HashMap, float, float, magma.agent.agentmodel.IGyroRate)}
	 */
	@Test
	public void testLocalize()
	{
		Vector3D flagVecF1R = new Vector3D(7.21110, new Vector3D(toRadians(33.6900), 0));
		when(F1R.getLocalPosition()).thenReturn(flagVecF1R);
		when(F1R.isVisible()).thenReturn(true);

		Vector3D flagVecF2R = new Vector3D(7.21110, new Vector3D(toRadians(-33.6900), 0));
		when(F2R.getLocalPosition()).thenReturn(flagVecF2R);
		when(F2R.isVisible()).thenReturn(true);

		when(F1R.compareTo(F2R)).thenReturn(-1);
		when(F2R.compareTo(F1R)).thenReturn(1);

		flags.add(F1R);
		flags.add(F2R);

		Pose3D localizeInfo = testee.localize(environment, null, null);
		assertNotNull(localizeInfo);
		assertTrue(FuzzyCompare.eq(new Vector3D(0, 0, 0), localizeInfo.getPosition(), 0.001f));
	}

	/**
	 * Test for
	 * {@link LocalizerTriangulation#localize(java.util.HashMap, float, float, magma.agent.agentmodel.IGyroRate)}
	 */
	@Test
	public void testLocalizeMoreFlagsRight()
	{
		Vector3D flagVecF1R = new Vector3D(7.21110, new Vector3D(toRadians(33.6900), 0));
		when(F1R.getLocalPosition()).thenReturn(flagVecF1R);
		when(F1R.isVisible()).thenReturn(true);

		Vector3D flagVecF2R = new Vector3D(7.21110, new Vector3D(toRadians(-33.6900), 0));
		when(F2R.getLocalPosition()).thenReturn(flagVecF2R);
		when(F2R.isVisible()).thenReturn(true);

		Vector3D flagVecG1R = new Vector3D(6.22, new Vector3D(toRadians(15.94), 0));
		when(G1R.getLocalPosition()).thenReturn(flagVecG1R);
		when(G1R.isVisible()).thenReturn(true);

		Vector3D flagVecG2R = new Vector3D(6.22, new Vector3D(toRadians(-15.94), 0));
		when(G2R.getLocalPosition()).thenReturn(flagVecG2R);
		when(G2R.isVisible()).thenReturn(true);

		when(F1R.compareTo(any())).thenReturn(-1);
		when(G1R.compareTo(F1R)).thenReturn(1);
		when(G1R.compareTo(G2R)).thenReturn(-1);
		when(G1R.compareTo(F2R)).thenReturn(-1);
		when(G2R.compareTo(F1R)).thenReturn(1);
		when(G2R.compareTo(G1R)).thenReturn(1);
		when(G2R.compareTo(F2R)).thenReturn(-1);
		when(F2R.compareTo(any())).thenReturn(1);

		// player is at position -4,-1 looking 45 deg
		flags.add(G1R);
		flags.add(G2R);
		flags.add(F1R);
		flags.add(F2R);

		Pose3D localizeInfo = testee.localize(environment, null, null);
		assertNotNull(localizeInfo);
		assertTrue(FuzzyCompare.eq(new Vector3D(-0.149, -0.001, 0), localizeInfo.getPosition(), 0.01f));
		// assertEquals(-1.592629, localizeInfo.getOrientationX().degrees(),
		// 0.001);
	}

	/**
	 * Test for
	 * {@link LocalizerTriangulation#localize(java.util.HashMap, float, float, magma.agent.agentmodel.IGyroRate)}
	 */
	@Test
	public void testLocalizeMoreFlagsLeft()
	{
		Vector3D flagVecF1L = new Vector3D(7.21110, new Vector3D(toRadians(-33.6900), 0));
		when(F1L.getLocalPosition()).thenReturn(flagVecF1L);
		when(F1L.isVisible()).thenReturn(true);

		Vector3D flagVecF2L = new Vector3D(7.21110, new Vector3D(toRadians(33.6900), 0));
		when(F2L.getLocalPosition()).thenReturn(flagVecF2L);
		when(F2L.isVisible()).thenReturn(true);

		Vector3D flagVecG1L = new Vector3D(6.08, new Vector3D(toRadians(-15.94), 0));
		when(G1L.getLocalPosition()).thenReturn(flagVecG1L);
		when(G1L.isVisible()).thenReturn(true);

		Vector3D flagVecG2L = new Vector3D(6.08, new Vector3D(toRadians(15.94), 0));
		when(G2L.getLocalPosition()).thenReturn(flagVecG2L);
		when(G2L.isVisible()).thenReturn(true);

		when(F1L.compareTo(any())).thenReturn(1);
		when(G1L.compareTo(F1L)).thenReturn(-1);
		when(G1L.compareTo(G2L)).thenReturn(1);
		when(G1L.compareTo(F2L)).thenReturn(1);
		when(G2L.compareTo(F1L)).thenReturn(-1);
		when(G2L.compareTo(G1L)).thenReturn(-1);
		when(G2L.compareTo(F2L)).thenReturn(1);
		when(F2L.compareTo(any())).thenReturn(-1);

		flags.add(G1L);
		flags.add(G2L);
		flags.add(F1L);
		flags.add(F2L);

		Pose3D localizeInfo = testee.localize(environment, null, null);
		assertNotNull(localizeInfo);
		assertTrue(FuzzyCompare.eq(new Vector3D(0.0336, 0.0, 0), localizeInfo.getPosition(), 0.01f));
		// assertEquals(3.1144, localizeInfo.getOrientationX().radians(), 0.001);
	}

	/**
	 * Test for
	 * {@link LocalizerTriangulation#localize(java.util.HashMap, float, float, magma.agent.agentmodel.IGyroRate)}
	 */
	@Test
	public void testLocalizeTurned()
	{
		Vector3D flagVecF1L = new Vector3D(5.385164, new Vector3D(1.16590, 0));
		when(F1L.getLocalPosition()).thenReturn(flagVecF1L);
		when(F1L.isVisible()).thenReturn(true);

		Vector3D flagVecF1R = new Vector3D(11.18034, new Vector3D(-0.32175, 0));
		when(F1R.getLocalPosition()).thenReturn(flagVecF1R);
		when(F1R.isVisible()).thenReturn(true);

		when(F1L.compareTo(F1R)).thenReturn(-1);
		when(F1R.compareTo(F1L)).thenReturn(1);

		flags.add(F1L);
		flags.add(F1R);

		Pose3D localizeInfo = testee.localize(environment, null, null);
		assertNotNull(localizeInfo);
		assertTrue(FuzzyCompare.eq(new Vector3D(-4.0, -1.0, 0), localizeInfo.getPosition(), 0.001f));
		// assertEquals(45, localizeInfo.getOrientationX().degrees(), 0.001);
	}

	/**
	 * Test for
	 * {@link LocalizerTriangulation#localize(java.util.HashMap, float, float, magma.agent.agentmodel.IGyroRate)}
	 */
	@Test
	public void testLocalizeGoalPosts()
	{
		Vector3D flagVecG1R = new Vector3D(6.22, new Vector3D(toRadians(15.94), 0));
		when(G1R.getLocalPosition()).thenReturn(flagVecG1R);
		when(G1R.isVisible()).thenReturn(true);

		Vector3D flagVecF2R = new Vector3D(7.35, new Vector3D(toRadians(-23.67), 0));
		when(F2R.getLocalPosition()).thenReturn(flagVecF2R);
		when(F2R.isVisible()).thenReturn(true);

		when(G1R.compareTo(F2R)).thenReturn(-1);
		when(F2R.compareTo(G1R)).thenReturn(1);

		flags.add(G1R);
		flags.add(F2R);

		Pose3D localizeInfo = testee.localize(environment, null, null);
		assertNotNull(localizeInfo);
		assertTrue(FuzzyCompare.eq(new Vector3D(-0.178, -0.0187, 0), localizeInfo.getPosition(), 0.01f));
		// assertEquals(-0.162, localizeInfo.getOrientationX().radians(), 0.001);
	}

	/**
	 * Special test case for a bug
	 * {@link LocalizerTriangulation#localize(java.util.HashMap, float, float, magma.agent.agentmodel.IGyroRate)}
	 * that ocurred at some time
	 */
	@Test
	public void testLocalizeBug1()
	{
		Vector3D flagVecG2R = new Vector3D(6.22, new Vector3D(toRadians(48.779998), 0));
		when(G2R.getLocalPosition()).thenReturn(flagVecG2R);
		when(G2R.isVisible()).thenReturn(true);

		Vector3D flagVecF2R = new Vector3D(7.34, new Vector3D(toRadians(21.93000), 0));
		when(F2R.getLocalPosition()).thenReturn(flagVecF2R);
		when(F2R.isVisible()).thenReturn(true);

		when(G2R.compareTo(F2R)).thenReturn(-1);
		when(F2R.compareTo(G2R)).thenReturn(1);

		flags.add(G2R);
		flags.add(F2R);

		Pose3D localizeInfo = testee.localize(environment, null, null);
		assertNotNull(localizeInfo);
		assertTrue(FuzzyCompare.eq(new Vector3D(-0.185829, -0.0489, 0), localizeInfo.getPosition(), 0.01f));
		// assertEquals(-0.95624, localizeInfo.getOrientationX().radians(),
		// 0.001);
	}
}
