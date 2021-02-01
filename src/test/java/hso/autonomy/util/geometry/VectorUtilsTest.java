/*******************************************************************************
 * Copyright 2008 - 2020 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under MIT License (see LICENSE).
 *******************************************************************************/
package hso.autonomy.util.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.jupiter.api.Test;

public class VectorUtilsTest
{
	@Test
	public void testAverage()
	{
		assertEquals(VectorUtils.average(new Vector3D(1, 2, 3)), new Vector3D(1, 2, 3));
		assertEquals(VectorUtils.average(new Vector3D(-1, 2, 3), new Vector3D(2, 3, 4)), new Vector3D(0.5, 2.5, 3.5));
	}

	@Test
	public void testAverageEmptyList()
	{
		assertEquals(VectorUtils.average(new ArrayList<>()), Vector3D.ZERO);
		assertEquals(VectorUtils.average(), Vector3D.ZERO);
	}

	@Test
	public void testGetDirectionTo()
	{
		assertEquals(45, VectorUtils.getDirectionTo(new Vector2D(1, 1), new Vector2D(2, 2)).degrees(), 0.0001);
		assertEquals(-45, VectorUtils.getDirectionTo(new Vector2D(1, 1), new Vector2D(2, 0)).degrees(), 0.0001);
		assertEquals(90, VectorUtils.getDirectionTo(new Vector2D(1, 2), new Vector2D(1, 3)).degrees(), 0.0001);
		assertEquals(-90, VectorUtils.getDirectionTo(new Vector2D(0, 0), new Vector2D(0, -3)).degrees(), 0.0001);
		assertEquals(-180, VectorUtils.getDirectionTo(new Vector2D(0, 0), new Vector2D(-4, 0)).degrees(), 0.0001);
	}
}
