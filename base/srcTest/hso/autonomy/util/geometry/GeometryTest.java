/*******************************************************************************
 * Copyright 2008 - 2020 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under MIT License (see LICENSE).
 *******************************************************************************/
package hso.autonomy.util.geometry;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Test;

/**
 * @author Klaus Dorer
 *
 */
public class GeometryTest
{
	/**
	 * Test for
	 * {@link Geometry#getLinearFuzzyValue(double, double, boolean, double)}
	 */
	@Test
	public void testGetLinearFuzzyValue()
	{
		assertEquals(0.0, Geometry.getLinearFuzzyValue(10, 20, true, 0), 0.0001);
		assertEquals(0.1, Geometry.getLinearFuzzyValue(10, 20, true, 11), 0.0001);
		assertEquals(1.0, Geometry.getLinearFuzzyValue(10, 20, true, 25), 0.0001);

		assertEquals(1.0, Geometry.getLinearFuzzyValue(10, 20, false, 0), 0.0001);
		assertEquals(0.9, Geometry.getLinearFuzzyValue(10, 20, false, 11), 0.0001);
		assertEquals(0.0, Geometry.getLinearFuzzyValue(10, 20, false, 25), 0.0001);

		assertEquals(0.0, Geometry.getLinearFuzzyValue(20, 10, false, 25), 0.0001);
	}

	/**
	 * Test for
	 * {@link Geometry#linearInterpolation(double, double, double, double,
	 * double)}
	 */
	@Test
	public void testLinearInterpolation()
	{
		assertEquals(0.0, Geometry.linearInterpolation(10, 0, 20, 1, 10), 0.0001);
		assertEquals(-1.0, Geometry.linearInterpolation(10, 0, 20, 1, 0), 0.0001);
		assertEquals(0.5, Geometry.linearInterpolation(10, 0, 20, 1, 15), 0.0001);
		assertEquals(1.0, Geometry.linearInterpolation(10, 0, 20, 1, 20), 0.0001);
		assertEquals(1.5, Geometry.linearInterpolation(10, 0, 20, 1, 25), 0.0001);

		assertEquals(0.5, Geometry.linearInterpolation(20, 0, 10, 1, 15), 0.0001);

		assertEquals(-0.5, Geometry.linearInterpolation(10, 1, 20, 0, 25), 0.0001);
	}

	/**
	 * Test for
	 * {@link Geometry#getPointInterpolation(Vector3D, Vector3D, double)}
	 */
	@Test
	public void testGetPointInterpolation()
	{
		Pose2D result = Geometry.getPointInterpolation(new Vector3D(0, 0, 0), new Vector3D(1, 0, 0), 0.5);
		assertEquals(0.5, result.getPosition().getX(), 0.0001);
		assertEquals(0, result.getPosition().getY(), 0.0001);
		assertEquals(0, result.getAngle().degrees(), 0.0001);

		result = Geometry.getPointInterpolation(new Vector3D(2, 2, 0), new Vector3D(1, 1, 0), 0.5 * Math.sqrt(2));
		assertEquals(1.5, result.getPosition().getX(), 0.0001);
		assertEquals(1.5, result.getPosition().getY(), 0.0001);
		assertEquals(-135, result.getAngle().degrees(), 0.0001);
	}

	@Test
	public void testGetPointOnLineAbsoluteEnd()
	{
		Vector3D start = new Vector3D(0, 0, 0);
		Vector3D end = new Vector3D(1, 0, 0);
		Vector3D pos = Geometry.getPointOnLineAbsoluteEnd(start, end, 0.7);
		assertEquals(0.3, pos.getX(), 0.0001);
		assertEquals(0.0, pos.getY(), 0.0001);
		assertEquals(0.0, pos.getZ(), 0.0001);

		start = new Vector3D(0, 2, 0);
		end = new Vector3D(0, 4, 0);
		pos = Geometry.getPointOnLineAbsoluteEnd(start, end, 0.5);
		assertEquals(0.0, pos.getX(), 0.0001);
		assertEquals(3.5, pos.getY(), 0.0001);
	}

	@Test
	public void testGetPointOnOrthogonal2D()
	{
		Vector3D start = new Vector3D(0, 0, 0);
		Vector3D end = new Vector3D(1, 0, 1);
		Vector3D pos = Geometry.getPointOnOrthogonal2D(start, end, 0.4, 0.4);
		assertEquals(0.6, pos.getX(), 0.0001);
		assertEquals(0.4, pos.getY(), 0.0001);
		assertEquals(0.0, pos.getZ(), 0.0001);
	}

	@Test
	public void testGetInterceptionPoint() throws Exception
	{
		double cycleTime = 0.02f;
		double cycleSpeed = 0.7 * 3 * cycleTime;
		Vector3D startPosition = Vector3D.ZERO;
		Vector3D[] targetPositions = new Vector3D[4];
		targetPositions[0] = new Vector3D(0.01, 0, 0);
		targetPositions[1] = new Vector3D(0.05, 0, 0);
		targetPositions[2] = new Vector3D(0.08, 0, 0);
		targetPositions[3] = new Vector3D(0.10, 0, 0);
		Vector3D interceptionPoint = Geometry.getInterceptionPoint(startPosition, targetPositions, cycleSpeed, 4);
		assertEquals(targetPositions[2], interceptionPoint);
	}

	@Test
	public void testGetAverageRotation() throws Exception
	{
		ArrayList<Rotation> rotations = new ArrayList<>();
		Rotation targetRot = new Rotation(Vector3D.PLUS_K, Math.toRadians(90));

		rotations.add(new Rotation(Vector3D.PLUS_K, Math.toRadians(89)));
		rotations.add(new Rotation(Vector3D.PLUS_K, Math.toRadians(91)));
		rotations.add(new Rotation(Vector3D.PLUS_K, Math.toRadians(90)));
		rotations.add(new Rotation(Vector3D.PLUS_K, Math.toRadians(92)));
		rotations.add(new Rotation(Vector3D.PLUS_K, Math.toRadians(88)));

		Rotation avgRot = Geometry.getAverageRotation(rotations);

		assertEquals(0, Rotation.distance(avgRot, targetRot), 0.001);
	}

	@Test
	public void testGetLocalHorizontalSpeed()
	{
		double angle = Angle.deg(30).radians();
		Rotation orientation = new Rotation(Vector3D.PLUS_K, angle);
		Vector3D speed = new Vector3D(1, 0, 0.5);
		Vector2D result = Geometry.getLocalHorizontalSpeed(orientation, speed);
		assertEquals(Math.cos(angle), result.getX(), 0.0001f);
		assertEquals(Math.sin(angle), result.getY(), 0.0001f);
	}

	@Test
	public void testGetDistanceToLine()
	{
		assertEquals(0, Geometry.getDistanceToLine(Vector2D.ZERO, new Vector2D(10, 0), new Vector2D(5, 0)), 0.0001f);
		assertEquals(0, Geometry.getDistanceToLine(Vector2D.ZERO, new Vector2D(10, 0), new Vector2D(20, 0)), 0.0001f);
		assertEquals(1, Geometry.getDistanceToLine(Vector2D.ZERO, new Vector2D(10, 0), new Vector2D(5, 1)), 0.0001f);
		assertEquals(1, Geometry.getDistanceToLine(Vector2D.ZERO, new Vector2D(-10, 0), new Vector2D(5, 1)), 0.0001f);
		assertEquals(
				0, Geometry.getDistanceToLine(new Vector2D(-1, -2), new Vector2D(1, 2), new Vector2D(0, 0)), 0.0001f);
		assertEquals(
				0, Geometry.getDistanceToLine(new Vector2D(-1, -2), new Vector2D(1, 2), new Vector2D(1, 2)), 0.0001f);
		assertEquals(
				5, Geometry.getDistanceToLine(new Vector2D(-3, -4), new Vector2D(3, 4), new Vector2D(4, -3)), 0.0001f);
	}
}