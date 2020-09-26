/*******************************************************************************
 * Copyright 2008 - 2020 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under MIT License (see LICENSE).
 *******************************************************************************/
package hso.autonomy.util.geometry;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Test;

public class Pose3DTest
{
	/**
	 * Test for {@link Pose3D#applyTo(IPose3D)}, {@link Pose3D#applyTo(Vector3D)}
	 * , {@link Pose3D#applyInverseTo(IPose3D)} and
	 * {@link Pose3D#applyInverseTo(Vector3D)}
	 */
	@Test
	public void testApplyTo()
	{
		Pose3D testee = new Pose3D(new Vector3D(3, 1, 0), new Rotation(Vector3D.PLUS_K, Math.toRadians(90)));

		// Test applyTo other pose
		Pose3D otherPose = new Pose3D(new Vector3D(1, 0, 0), new Rotation(Vector3D.PLUS_K, Math.toRadians(20)));
		Pose3D resPose = testee.applyTo(otherPose);

		assertEquals(3, resPose.getX(), 0.0001);
		assertEquals(2, resPose.getY(), 0.0001);
		assertEquals(0, Rotation.distance(new Rotation(Vector3D.PLUS_K, Math.toRadians(110)), resPose.getOrientation()),
				0.0001);

		// Test applyInverseTo other pose
		Pose3D resPoseInverse = testee.applyInverseTo(resPose);

		assertEquals(otherPose.getX(), resPoseInverse.getX(), 0.0001);
		assertEquals(otherPose.getY(), resPoseInverse.getY(), 0.0001);
		assertEquals(0, Rotation.distance(otherPose.getOrientation(), resPoseInverse.getOrientation()), 0.0001);

		// Test applyTo other position
		Vector3D otherPosition = new Vector3D(2, 1, 0);
		Vector3D resPos = testee.applyTo(otherPosition);

		assertEquals(2, resPos.getX(), 0.0001);
		assertEquals(3, resPos.getY(), 0.0001);

		// Test applyInverseTo other position
		Vector3D resPosInverse = testee.applyInverseTo(resPos);

		assertEquals(otherPosition.getX(), resPosInverse.getX(), 0.0001);
		assertEquals(otherPosition.getY(), resPosInverse.getY(), 0.0001);
	}

	/**
	 * Test for {@link Pose3D#applyInverseTo(IPose3D)} and
	 * {@link Pose3D#applyInverseTo(Vector3D)}
	 */
	@Test
	public void testApplyInverseTo()
	{
		Pose3D testee = new Pose3D(new Vector3D(3, 1, 0), new Rotation(Vector3D.PLUS_K, Math.toRadians(90)));

		// Test applyTo other pose
		Pose3D otherPose = new Pose3D(new Vector3D(1, 0, 0), new Rotation(Vector3D.PLUS_K, Math.toRadians(20)));
		Pose3D resPose = testee.applyInverseTo(otherPose);

		assertEquals(-1, resPose.getX(), 0.0001);
		assertEquals(2, resPose.getY(), 0.0001);
		assertEquals(0, Rotation.distance(new Rotation(Vector3D.PLUS_K, Math.toRadians(-70)), resPose.getOrientation()),
				0.0001);

		// Test applyTo other position
		Vector3D otherPosition = new Vector3D(2, 1, 0);
		Vector3D resPos = testee.applyInverseTo(otherPosition);

		assertEquals(0, resPos.getX(), 0.0001);
		assertEquals(1, resPos.getY(), 0.0001);
	}

	@Test
	public void testGet2DPose()
	{
		// only horizontally rotated
		Pose3D testee = new Pose3D(new Vector3D(3, 1, 2), new Rotation(Vector3D.PLUS_K, Math.toRadians(45)));
		IPose2D resPose = testee.get2DPose();
		assertEquals(3, resPose.getX(), 0.0001);
		assertEquals(1, resPose.getY(), 0.0001);
		assertEquals(45, resPose.getAngle().degrees(), 0.0001);

		// 3D rotated
		Pose3D otherPose = new Pose3D(Vector3D.ZERO, new Rotation(Vector3D.PLUS_J, Math.toRadians(90)));
		Pose3D newPose = testee.applyTo(otherPose);
		resPose = newPose.get2DPose();
		assertEquals(3, resPose.getX(), 0.0001);
		assertEquals(1, resPose.getY(), 0.0001);
		assertEquals(45, resPose.getAngle().degrees(), 0.0001);
	}

	@Test
	public void testGetHorizontalAngleTo()
	{
		Pose3D testee = new Pose3D(new Vector3D(3, 1, 2), new Rotation(Vector3D.PLUS_K, Math.toRadians(0)));
		Pose3D other = new Pose3D(new Vector3D(4, 2, 2), new Rotation(Vector3D.PLUS_K, Math.toRadians(45)));
		Angle result = testee.getHorizontalAngleTo(other);
		assertEquals(45, result.degrees(), 0.0001);

		testee = new Pose3D(new Vector3D(3, 1, 2), new Rotation(Vector3D.PLUS_K, Math.toRadians(45)));
		other = new Pose3D(new Vector3D(4, 2, 2), new Rotation(Vector3D.PLUS_K, Math.toRadians(45)));
		result = testee.getHorizontalAngleTo(other);
		assertEquals(0, result.degrees(), 0.0001);
	}
}
