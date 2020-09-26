/*******************************************************************************
 * Copyright 2008 - 2020 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under MIT License (see LICENSE).
 *******************************************************************************/
package hso.autonomy.util.misc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.Test;

/**
 * @author Ingo Schindler
 *
 */
public class FuzzyCompareTest
{
	/**
	 * Test method for {@link hso.autonomy.util.misc.FuzzyCompare#eq(double, double, double)}
	 * .
	 */
	@Test
	public void testEq()
	{
		assertEquals(true, FuzzyCompare.eq(10, 10, 0), "EQ 10 = 10 | Range 0");
		assertEquals(true, FuzzyCompare.eq(10, 10, 5), "EQ 10 = 10 | Range 5");
		assertEquals(true, FuzzyCompare.eq(10, 9, 1), "EQ 10 = 9 | Range 1");
		assertEquals(true, FuzzyCompare.eq(10, 11, 1), "EQ 10 = 11 | Range 1");
		assertEquals(false, FuzzyCompare.eq(10, 8, 1), "EQ 10 = 8 | Range 1");
		assertEquals(false, FuzzyCompare.eq(10, 12, 1), "EQ 10 = 12 | Range 1");
	}

	/**
	 * Test method for {@link hso.autonomy.util.misc.FuzzyCompare#gt(double, double, double)}
	 * .
	 */
	@Test
	public void testGt()
	{
		assertEquals(true, FuzzyCompare.gt(10, 10, 0), "GT 10 > 10 | Range 0");
		assertEquals(true, FuzzyCompare.gt(10, 10, 5), "GT 10 > 10 | Range 5");
		assertEquals(true, FuzzyCompare.gt(10, 9, 1), "GT 10 > 9 | Range 1");
		assertEquals(true, FuzzyCompare.gt(10, 11, 1), "GT 10 > 11 | Range 1");
		assertEquals(true, FuzzyCompare.gt(10, 8, 1), "GT 10 > 8 | Range 1");
		assertEquals(false, FuzzyCompare.gt(10, 12, 1), "GT 10 > 12 | Range 1");
	}

	/**
	 * Test method for {@link hso.autonomy.util.misc.FuzzyCompare#lt(double, double, double)}
	 * .
	 */
	@Test
	public void testLt()
	{
		assertEquals(true, FuzzyCompare.lt(10, 10, 0), "LT 10 < 10 | Range 0");
		assertEquals(true, FuzzyCompare.lt(10, 10, 5), "LT 10 < 10 | Range 5");
		assertEquals(true, FuzzyCompare.lt(10, 9, 1), "LT 10 < 9 | Range 1");
		assertEquals(true, FuzzyCompare.lt(10, 11, 1), "LT 10 < 11 | Range 1");
		assertEquals(false, FuzzyCompare.lt(10, 8, 1), "LT 10 < 8 | Range 1");
		assertEquals(true, FuzzyCompare.lt(10, 12, 1), "LT 10 < 12 | Range 1");
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.util.misc.FuzzyCompare#gte(double, double, double)}.
	 */
	@Test
	public void testGte()
	{
		assertEquals(true, FuzzyCompare.gte(10, 10, 0), "GTE 10 > 10 | Range 0");
		assertEquals(true, FuzzyCompare.gte(10, 10, 5), "GTE 10 > 10 | Range 5");
		assertEquals(true, FuzzyCompare.gte(10, 9, 1), "GTE 10 > 9 | Range 1");
		assertEquals(true, FuzzyCompare.gte(10, 11, 1), "GTE 10 > 11 | Range 1");
		assertEquals(true, FuzzyCompare.gte(10, 8, 1), "GTE 10 > 8 | Range 1");
		assertEquals(false, FuzzyCompare.gte(10, 12, 1), "GTE 10 > 12 | Range 1");
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.util.misc.FuzzyCompare#lte(double, double, double)}.
	 */
	@Test
	public void testLte()
	{
		assertEquals(true, FuzzyCompare.lte(10, 10, 0), "LTE 10 < 10 | Range 0");
		assertEquals(true, FuzzyCompare.lte(10, 10, 5), "LTE 10 < 10 | Range 5");
		assertEquals(true, FuzzyCompare.lte(10, 9, 1), "LTE 10 < 9 | Range 1");
		assertEquals(true, FuzzyCompare.lte(10, 11, 1), "LTE 10 < 11 | Range 1");
		assertEquals(false, FuzzyCompare.lte(10, 8, 1), "LTE 10 < 8 | Range 1");
		assertEquals(true, FuzzyCompare.lte(10, 12, 1), "LTE 10 < 12 | Range 1");
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.util.misc.FuzzyCompare#eq(Vector3D, Vector3D, double)}.
	 */
	@Test
	public void testEq3D()
	{
		// Difference on the right side
		assertEquals(false, FuzzyCompare.eq(new Vector3D(1.0, 2.0, 3.0), new Vector3D(2.0, 2.0, 3.0), 0.0),
				"EQ (1, 2, 3) == (2, 2, 3) | Range 0");
		assertEquals(true, FuzzyCompare.eq(new Vector3D(1.0, 2.0, 3.0), new Vector3D(2.0, 2.0, 3.0), 1.0),
				"EQ (1, 2, 3) == (2, 2, 3) | Range 1");

		assertEquals(false, FuzzyCompare.eq(new Vector3D(1.0, 2.0, 3.0), new Vector3D(1.0, 3.0, 3.0), 0.0),
				"EQ (1, 2, 3) == (1, 3, 3) | Range 0");
		assertEquals(true, FuzzyCompare.eq(new Vector3D(1.0, 2.0, 3.0), new Vector3D(1.0, 3.0, 3.0), 1.0),
				"EQ (1, 2, 3) == (1, 3, 3) | Range 1");

		assertEquals(false, FuzzyCompare.eq(new Vector3D(1.0, 2.0, 3.0), new Vector3D(1.0, 2.0, 4.0), 0.0),
				"EQ (1, 2, 3) == (1, 2, 4) | Range 0");
		assertEquals(true, FuzzyCompare.eq(new Vector3D(1.0, 2.0, 3.0), new Vector3D(1.0, 2.0, 4.0), 1.0),
				"EQ (1, 2, 3) == (1, 2, 4) | Range 1");

		// Difference on the left side
		assertEquals(false, FuzzyCompare.eq(new Vector3D(2.0, 2.0, 3.0), new Vector3D(1.0, 2.0, 3.0), 0.0),
				"EQ (2, 2, 3) == (1, 2, 3) | Range 0");
		assertEquals(true, FuzzyCompare.eq(new Vector3D(2.0, 2.0, 3.0), new Vector3D(1.0, 2.0, 3.0), 1.0),
				"EQ (2, 2, 3) == (1, 2, 3) | Range 1");

		assertEquals(false, FuzzyCompare.eq(new Vector3D(1.0, 3.0, 3.0), new Vector3D(1.0, 2.0, 3.0), 0.0),
				"EQ (1, 3, 3) == (1, 2, 3) | Range 0");
		assertEquals(true, FuzzyCompare.eq(new Vector3D(1.0, 3.0, 3.0), new Vector3D(1.0, 2.0, 3.0), 1.0),
				"EQ (1, 3, 3) == (1, 2, 3) | Range 1");

		assertEquals(false, FuzzyCompare.eq(new Vector3D(1.0, 2.0, 4.0), new Vector3D(1.0, 2.0, 3.0), 0.0),
				"EQ (1, 2, 4) == (1, 2, 3) | Range 0");
		assertEquals(true, FuzzyCompare.eq(new Vector3D(1.0, 2.0, 4.0), new Vector3D(1.0, 2.0, 3.0), 1.0),
				"EQ (1, 2, 4) == (1, 2, 3) | Range 1");
	}
}
