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
package magma.robots.nao.decision.behavior.dynamic;

import java.util.Collection;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.agent.model.worldmodel.ILandmark;
import hso.autonomy.util.geometry.Pose3D;
import hso.autonomy.util.misc.FuzzyCompare;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.basic.RoboCupBehavior;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.robots.nao.INaoJoints;

/**
 * Implements a behavior which lets the robot head focus on the ball
 *
 * @author Ingo Schindler
 */
public class FocusBall extends RoboCupBehavior
{
	private static final float TIME_TO_LOOK_AROUND = 2;

	private static final float LOOK_AROUND_DELAY = 8;

	private final IBehavior turnHead;

	private float timeToTurnHead;

	public FocusBall(IThoughtModel thoughtModel, BehaviorMap behaviors)
	{
		super(IBehaviorConstants.FOCUS_BALL, thoughtModel);
		turnHead = behaviors.get(IBehaviorConstants.TURN_HEAD);
		timeToTurnHead = getWorldModel().getGlobalTime();
	}

	@Override
	public void perform()
	{
		super.perform();

		boolean seeLandMark = false;
		Collection<ILandmark> landmarks = getWorldModel().getLandmarks();
		for (ILandmark landmark : landmarks) {
			float time = landmark.getAge(getWorldModel().getGlobalTime());
			if (time < 0.1) {
				seeLandMark = true;
				break;
			}
		}

		Vector3D ballPos = getWorldModel().getBall().getLocalPosition();

		float timeDelta = getWorldModel().getGlobalTime() - timeToTurnHead;

		if (getWorldModel().getBall().getAge(getWorldModel().getGlobalTime()) > 0.1 || !seeLandMark ||
				(ballPos.getNorm() > 1.5 &&
						FuzzyCompare.gte(timeDelta, LOOK_AROUND_DELAY + TIME_TO_LOOK_AROUND, TIME_TO_LOOK_AROUND))) {
			turnHead.perform();
			if (timeDelta > LOOK_AROUND_DELAY + TIME_TO_LOOK_AROUND) {
				timeToTurnHead = getWorldModel().getGlobalTime();
			}
			// we have to init this behavior manually since we return null here to allow to perform this behavior in
			// parallel
			turnHead.stayIn();

		} else {
			// Calculate position of ball relative to camera body
			IRoboCupAgentModel agentModel = getAgentModel();
			Pose3D headPose = agentModel.getCameraPose();

			Vector3D ballVec = new Vector3D(-ballPos.getY(), ballPos.getX(), ballPos.getZ());

			ballVec = headPose.getPosition().negate().add(headPose.getOrientation().applyInverseTo(ballVec));
			ballPos = new Vector3D(ballVec.getY(), -ballVec.getX(), ballVec.getZ());

			// Adjust head joints to focus the ball
			agentModel.getWriteableHJ(INaoJoints.NeckYaw).adjustAxisPosition(Math.toDegrees(ballPos.getAlpha() / 3));
			agentModel.getWriteableHJ(INaoJoints.NeckPitch).adjustAxisPosition(Math.toDegrees(ballPos.getDelta() / 3));
		}
	}
}
