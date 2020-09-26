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

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import magma.agent.decision.behavior.SupportFoot;
import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Ignore;
import org.junit.Test;

public class IKWalkBehaviorTest
{
	@Ignore
	@Test
	public void testCalculateFreeFootPose()
	{
		IKWalkBehavior testee = new IKWalkBehavior(null, null, null);
		double Y_OFFSET = 2 * new IKWalkMovementParametersBase().getWalkWidth();

		// test zero pose
		Pose2D globalSupportFootPose = new Pose2D();
		StepParameters step = new StepParameters();
		Pose2D result = testee.calculateFreeFootPose(globalSupportFootPose, SupportFoot.LEFT, step);

		assertEquals(0, result.x, 0.0001);
		assertEquals(-Y_OFFSET, result.y, 0.0001);
		assertEquals(0, result.angle.degrees(), 0.0001);

		// test some random pose with no rotation in the step
		globalSupportFootPose.x = 5;
		globalSupportFootPose.y = 1;
		globalSupportFootPose.angle = Angle.deg(90);
		step.xTargetDistance = 0;
		step.yTargetDistance = 0.1;
		step.turnAngle = 0;
		result = testee.calculateFreeFootPose(globalSupportFootPose, SupportFoot.LEFT, step);

		assertEquals(globalSupportFootPose.x + Y_OFFSET, result.x, 0.0001);
		assertEquals(globalSupportFootPose.y + 2 * step.yTargetDistance, result.y, 0.0001);
		assertEquals(90, result.angle.degrees(), 0.0001);

		// test the same with the alternative foot
		result = testee.calculateFreeFootPose(globalSupportFootPose, SupportFoot.RIGHT, step);

		assertEquals(globalSupportFootPose.x - Y_OFFSET, result.x, 0.0001);
		assertEquals(globalSupportFootPose.y + 2 * step.yTargetDistance, result.y, 0.0001);
		assertEquals(90, result.angle.degrees(), 0.0001);

		// test the full banana where no turn is possible
		globalSupportFootPose.x = -8;
		globalSupportFootPose.y = 3;
		globalSupportFootPose.angle = Angle.deg(-40);
		step.xTargetDistance = 0.03;
		step.yTargetDistance = 0.1;
		step.turnAngle = 30;
		result = testee.calculateFreeFootPose(globalSupportFootPose, SupportFoot.LEFT, step);
		Vector2D stepVector =
				globalSupportFootPose.angle.applyTo(2 * step.yTargetDistance, -Y_OFFSET - 2 * step.xTargetDistance);

		assertEquals(globalSupportFootPose.x + stepVector.getX(), result.x, 0.0001);
		assertEquals(globalSupportFootPose.y + stepVector.getY(), result.y, 0.0001);
		assertEquals(-40, result.angle.degrees(), 0.0001);

		// test the same with the alternative support foot where the turn is
		// possible, but the side step is restricted
		result = testee.calculateFreeFootPose(globalSupportFootPose, SupportFoot.RIGHT, step);
		stepVector = globalSupportFootPose.angle.applyTo(
				Angle.deg(step.turnAngle).applyTo(2 * step.yTargetDistance, Y_OFFSET - 0.04)); // 0.04 is
																							   // the
		// internal limit

		assertEquals(globalSupportFootPose.x + stepVector.getX(), result.x, 0.0001);
		assertEquals(globalSupportFootPose.y + stepVector.getY(), result.y, 0.0001);
		assertEquals(20, result.angle.degrees(), 0.0001);
	}
}
