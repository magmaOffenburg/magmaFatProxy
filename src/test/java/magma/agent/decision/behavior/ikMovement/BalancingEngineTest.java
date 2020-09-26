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
package magma.agent.decision.behavior.ikMovement;

import static org.junit.Assert.assertEquals;

import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.Pose6D;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Stefan Glaser
 */
public class BalancingEngineTest
{
	private static final float COM_HEIGHT = -0.075f;

	Pose6D poseLeft;

	Pose6D poseRight;

	Vector3D com;

	Vector3D intendedLeaning;

	float saggitalAdjustmentFactor;

	float maxAbsSaggitalAdjustment;

	float coronalAdjustmentFactor;

	float maxAbsCoronalAdjustment;

	@Before
	public void setUp()
	{
		poseLeft = new Pose6D(-0.12, 0.01, -0.67, 0, 0, 15);
		poseRight = new Pose6D(0.12, 0.01, -0.61, 0, 0, -15);
		com = new Vector3D(0, 0, COM_HEIGHT);

		// Set default values for dynamic adjustment
		saggitalAdjustmentFactor = 0.2f;
		maxAbsSaggitalAdjustment = 100;
		coronalAdjustmentFactor = 0.2f;
		maxAbsCoronalAdjustment = 100;
	}

	@Test
	public void testEngineStaticNoIntendedLeaning()
	{
		intendedLeaning = Vector3D.PLUS_K;

		Pose6D expectedPoseLeft = new Pose6D(-0.12, 0.01, -0.67 + COM_HEIGHT, 0, 0, 15);
		Pose6D expectedPoseRight = new Pose6D(0.12, 0.01, -0.61 + COM_HEIGHT, 0, 0, -15);

		checkEngineStaticInRange(expectedPoseLeft, expectedPoseRight);
	}

	@Test
	public void testEngineStaticWithIntendedLeaning()
	{
		Rotation intendedLeaningRotation = new Rotation(Vector3D.PLUS_I, Math.toRadians(10));
		intendedLeaning = intendedLeaningRotation.applyTo(Vector3D.PLUS_K);

		Vector3D expectedLeftPos = com.add(intendedLeaningRotation.applyInverseTo(poseLeft.getPosition()));
		Vector3D expectedRightPos = com.add(intendedLeaningRotation.applyInverseTo(poseRight.getPosition()));

		Pose6D expectedPoseLeft = new Pose6D(expectedLeftPos, new Vector3D(-10, 0, poseLeft.zAngle));
		Pose6D expectedPoseRight = new Pose6D(expectedRightPos, new Vector3D(-10, 0, poseRight.zAngle));

		checkEngineStaticInRange(expectedPoseLeft, expectedPoseRight);
	}

	@Test
	@Ignore
	public void testEngineDynamicNoIntendedLeaning()
	{
		intendedLeaning = Vector3D.PLUS_K;

		// int range = 66;
		// int step = 33;
		// for (int xDeg = -range; xDeg <= range; xDeg += step) {
		// for (int yDeg = -range; yDeg <= range; yDeg += step) {
		int xDeg = 10;
		int yDeg = -5;

		Rotation zFreeOrientation = new Rotation(RotationOrder.ZXY, 0, Math.toRadians(xDeg), Math.toRadians(yDeg));

		Rotation adjustmentRotation = zFreeOrientation.applyTo(new Rotation(RotationOrder.YXZ,
				Math.toRadians(-yDeg * saggitalAdjustmentFactor), Math.toRadians(-xDeg * coronalAdjustmentFactor), 0));

		Vector3D expectedLeftPos = com.add(adjustmentRotation.applyInverseTo(poseLeft.getPosition()));
		Vector3D expectedRightPos = com.add(adjustmentRotation.applyInverseTo(poseRight.getPosition()));

		// TODO: Check Engine with respect to the resulting foot angles. So far,
		// it is not really clear what's calculated.
		Pose6D expectedPoseLeft = new Pose6D(expectedLeftPos, new Vector3D(-8.02392, 3.93913, poseLeft.zAngle));
		Pose6D expectedPoseRight = new Pose6D(expectedRightPos, new Vector3D(-8.02392, 3.93913, poseRight.zAngle));

		// Pose6D expectedPoseLeft = new Pose6D(expectedLeftPos, //
		// new Vector3D(//
		// -xDeg * (1 - coronalAdjustmentFactor),//
		// -yDeg * (1 - saggitalAdjustmentFactor), //
		// poseLeft.zAngle));
		// Pose6D expectedPoseRight = new Pose6D(expectedRightPos, new Vector3D(//
		// -xDeg * (1 - coronalAdjustmentFactor), //
		// -yDeg * (1 - saggitalAdjustmentFactor), //
		// poseRight.zAngle));

		checkEngineInZRange(expectedPoseLeft, expectedPoseRight, xDeg, yDeg);
		// }
		// }
	}

	@Test
	@Ignore
	public void testEngineDynamicWithIntendedLeaning()
	{
		Rotation intendedLeaningRotation = new Rotation(Vector3D.PLUS_I, Math.toRadians(10));
		intendedLeaning = intendedLeaningRotation.applyTo(Vector3D.PLUS_K);

		// int range = 66;
		// int step = 33;
		// for (int xDeg = -range; xDeg <= range; xDeg += step) {
		// for (int yDeg = -range; yDeg <= range; yDeg += step) {
		int xDeg = 10;
		int yDeg = -5;

		Rotation zFreeOrientation = new Rotation(RotationOrder.ZXY, //
				0,													//
				Math.toRadians(xDeg),								//
				Math.toRadians(yDeg));

		Rotation adjustmentRotation = zFreeOrientation.applyTo(new Rotation(RotationOrder.YXZ, //
				Math.toRadians(-yDeg * saggitalAdjustmentFactor),							   //
				Math.toRadians(-(10 - xDeg) * coronalAdjustmentFactor),						   //
				0));

		Vector3D expectedLeftPos = com.add(adjustmentRotation.applyInverseTo(poseLeft.getPosition()));
		Vector3D expectedRightPos = com.add(adjustmentRotation.applyInverseTo(poseRight.getPosition()));

		// TODO: Check Engine with respect to the resulting foot angles. So far,
		// it is not really clear what's calculated.
		Pose6D expectedPoseLeft = new Pose6D(expectedLeftPos, new Vector3D(-10.02392, 3.93913, poseLeft.zAngle));
		Pose6D expectedPoseRight = new Pose6D(expectedRightPos, new Vector3D(-10.02392, 3.93913, poseRight.zAngle));

		// Pose6D expectedPoseLeft = new Pose6D(expectedLeftPos, //
		// new Vector3D(//
		// -xDeg * (1 - coronalAdjustmentFactor),//
		// -yDeg * (1 - saggitalAdjustmentFactor), //
		// poseLeft.zAngle));
		// Pose6D expectedPoseRight = new Pose6D(expectedRightPos, new Vector3D(//
		// -xDeg * (1 - coronalAdjustmentFactor), //
		// -yDeg * (1 - saggitalAdjustmentFactor), //
		// poseRight.zAngle));

		checkEngineInZRange(expectedPoseLeft, expectedPoseRight, xDeg, yDeg);
		// }
		// }
	}

	/**
	 * Check method used to ensure the independence of the z-, x, and y-rotation
	 * in the provided orientation in the <b>static parameterization case</b>.
	 * This method calls the
	 * {@link BalancingEngine#adjustTargetPoses(IBalancingEngineParameters,
	 * Pose6D...)} in a nested loop with a set of z-, x- and y-rotations and
	 * compares its results to the provided expected poses. In the static case,
	 * the expected poses should be met regardless of the provided base
	 * orientation. <br> <b>Note:</b> This Method sets adjustment values and
	 * limits to meet the static case.
	 *
	 * @param expectedPoseLeft - the expected left pose
	 * @param expectedPoseRight - the expected left pose
	 */
	protected void checkEngineStaticInRange(Pose6D expectedPoseLeft, Pose6D expectedPoseRight)
	{
		int range = 66;
		int step = 33;
		saggitalAdjustmentFactor = 1f;
		maxAbsSaggitalAdjustment = 360;
		coronalAdjustmentFactor = 1f;
		maxAbsCoronalAdjustment = 360;

		for (int xDeg = -range; xDeg <= range; xDeg += step) {
			for (int yDeg = -range; yDeg <= range; yDeg += step) {
				checkEngineInZRange(expectedPoseLeft, expectedPoseRight, xDeg, yDeg);
			}
		}
	}

	/**
	 * Check method used to ensure the independence of the z-rotation in the
	 * provided orientation. This method calls the
	 * {@link BalancingEngine#adjustTargetPoses(IBalancingEngineParameters,
	 * Pose6D...)} in a loop with a set of z-rotations and the given x- and
	 * y-rotations and compares its results to the provided expected poses.
	 *
	 * @param expectedPoseLeft - the expected left pose
	 * @param expectedPoseRight - the expected left pose
	 * @param xDeg - the additional x-rotation in degrees
	 * @param yDeg - the additional y-rotation in degrees
	 */
	protected void checkEngineInZRange(Pose6D expectedPoseLeft, Pose6D expectedPoseRight, int xDeg, int yDeg)
	{
		int zRange = 175;
		int zStep = 35;
		BalancingEngineParameters params = new BalancingEngineParameters(intendedLeaning, com, saggitalAdjustmentFactor,
				maxAbsSaggitalAdjustment, coronalAdjustmentFactor, maxAbsCoronalAdjustment);
		Pose6D[] resultingPoses;
		Rotation orientationEstimation;

		for (int zDdeg = -zRange; zDdeg <= zRange; zDdeg += zStep) {
			orientationEstimation =
					new Rotation(RotationOrder.ZXY, Math.toRadians(zDdeg), Math.toRadians(xDeg), Math.toRadians(yDeg));

			resultingPoses = BalancingEngine.adjustTargetPoses(orientationEstimation, params, poseLeft, poseRight);

			comparePoses(expectedPoseLeft, resultingPoses[0]);
			comparePoses(expectedPoseRight, resultingPoses[1]);
		}
	}

	protected void checkEngine(Pose6D expectedPoseLeft, Pose6D expectedPoseRight, Rotation orientationEstimation)
	{
		BalancingEngineParameters params = new BalancingEngineParameters(intendedLeaning, com, saggitalAdjustmentFactor,
				maxAbsSaggitalAdjustment, coronalAdjustmentFactor, maxAbsCoronalAdjustment);
		Pose6D[] resultingPoses = BalancingEngine.adjustTargetPoses(orientationEstimation, params, poseLeft, poseRight);

		comparePoses(expectedPoseLeft, resultingPoses[0]);
		comparePoses(expectedPoseRight, resultingPoses[1]);
	}

	protected void comparePoses(Pose6D pose1, Pose6D pose2)
	{
		assertEquals("BalancingEngine x", pose1.x, pose2.x, 0.00001);
		assertEquals("BalancingEngine y", pose1.y, pose2.y, 0.00001);
		assertEquals("BalancingEngine z", pose1.z, pose2.z, 0.00001);
		assertEquals("BalancingEngine xAngle", pose1.xAngle, pose2.xAngle, 0.00001);
		assertEquals("BalancingEngine yAngle", pose1.yAngle, pose2.yAngle, 0.00001);
		assertEquals("BalancingEngine zAngle", pose1.zAngle, pose2.zAngle, 0.00001);
	}

	@Test
	@Ignore
	public void testRotations()
	{
		double xAngle = Math.toRadians(10);
		double yAngle = Math.toRadians(20);
		double zAngle = Math.toRadians(0);

		Rotation torsoTarget = new Rotation(RotationOrder.ZXY, zAngle, xAngle, yAngle);

		System.out.println("Torso Target:");
		Geometry.printRotationMatrix(torsoTarget);

		Rotation rotX = new Rotation(Vector3D.PLUS_I, -xAngle / 4.5);
		Rotation rotY = new Rotation(Vector3D.PLUS_J, -yAngle / 10.4);

		Rotation rotYFull = new Rotation(Vector3D.PLUS_J, yAngle);
		Rotation rotYNegFull = new Rotation(Vector3D.PLUS_J, -yAngle);

		Rotation rot2 = rotYNegFull.applyTo(rotX).applyTo(rotYFull).applyTo(rotY);
		Rotation rot3 = rotYNegFull.applyTo(rotX).applyTo(rotYNegFull.applyInverseTo(rotY));

		System.out.println(Rotation.distance(rot2, rot3));

		Rotation result = torsoTarget.applyTo(rot2);

		System.out.println("Result:");
		Geometry.printRotationMatrix(result);
	}
}