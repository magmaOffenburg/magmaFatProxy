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

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.util.geometry.Pose3D;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.basic.RoboCupBehavior;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.robots.nao.INaoJoints;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class FocusBallGoalie extends RoboCupBehavior
{
	public FocusBallGoalie(IThoughtModel thoughtModel, BehaviorMap behaviors)
	{
		super(IBehaviorConstants.FOCUS_BALL_GOALIE, thoughtModel);
	}

	@Override
	public void perform()
	{
		super.perform();

		Vector3D ballPos = getWorldModel().getBall().getLocalPosition();

		// Calculate position of ball relative to camera body
		// IBodyPart head = agentModel.getBodyPart(INaoConstants.Head);
		// Pose3D headPose = head.getPose();
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
