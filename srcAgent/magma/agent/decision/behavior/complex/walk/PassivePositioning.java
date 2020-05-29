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
package magma.agent.decision.behavior.complex.walk;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.PoseSpeed2D;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.complex.RoboCupSingleComplexBehavior;
import magma.agent.decision.behavior.ikMovement.walk.IKDynamicWalkMovement;

/**
 * Behavior we use when we're not actively running to the ball with Attack.
 *
 * @author Klaus Dorer
 */
public class PassivePositioning extends RoboCupSingleComplexBehavior
{
	public PassivePositioning(IThoughtModel thoughtModel, BehaviorMap behaviors)
	{
		super(IBehaviorConstants.PASSIVE_POSITIONING, thoughtModel, behaviors);
	}

	@Override
	public IBehavior decideNextBasicBehavior()
	{
		IPose2D pose = getThoughtModel().getRole().getTargetPose();
		Angle direction = pose.getAngle();
		Vector2D newPosition = pose.getPosition();

		WalkToPosition walkToPosition = (WalkToPosition) behaviors.get(IBehaviorConstants.WALK_TO_POSITION);
		Pose2D newPose = new Pose2D(newPosition, direction);
		return walkToPosition.setPosition(
				new PoseSpeed2D(newPose, Vector2D.ZERO), 90, true, true, 0.8, IKDynamicWalkMovement.NAME_LOW_ACC);
	}
}
