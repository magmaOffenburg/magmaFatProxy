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
package magma.agent.communication.action;

import hso.autonomy.agent.communication.action.IAction;
import hso.autonomy.agent.communication.action.IEffector;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose3D;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Interface to all actions an agent can perform on the server
 */
public interface IRoboCupAction extends IAction
{
	/**
	 * For initialization to be called after object creation
	 */
	void init(Map<String, IEffector> effectors);

	/**
	 * Beams the player to specified position with specified direction
	 * @param pose position of the player on the field
	 */
	void sendBeamString(Pose2D pose);

	/**
	 * shouts out a message to other players
	 * @param message the message to send (max 8 bytes)
	 */
	void sayMessage(String message);

	/**
	 * Set the state of the decision effector.
	 *
	 * @param headCommand - the command for the head
	 * @param headTargetYaw - the intended head yaw angle
	 * @param headTargetPitch - the intended head pitch angle
	 * @param movementCommand - the command for the movement behavior
	 * @param intendedX - the intended sidewards movement speed
	 * @param intendedY - the intended forward movement speed
	 * @param intendedTurn - the intended turn angle (deg)
	 * @param relBallPosX - the ball position relative to the support leg
	 * @param relBallPosY - the ball position relative to the support leg
	 * @param relBallTargetPosX - the ball target position relative to the support leg
	 * @param relBallTargetPosY - the ball target position relative to the support leg
	 * @param remoteControlled - true if in direct control
	 * @param sayCommand - the command for the TTS server
	 * @param sayParams - the parameters for the current say command
	 */
	void setDecision(int headCommand, float headTargetYaw, float headTargetPitch, int movementCommand, float intendedX,
			float intendedY, float intendedTurn, float relBallPosX, float relBallPosY, float relBallTargetPosX,
			float relBallTargetPosY, boolean remoteControlled, int sayCommand, Map<String, String> sayParams);

	/**
	 * Sets the poses for the CameraPosition effector and the head behavior
	 * @param ballWorld current position from the ball
	 * @param cameraTorso current camera pose based on torso
	 * @param cameraWorld current camera pose in worldModel
	 * @param headCommand name of the current headBehavior
	 */
	void setCameraPosition(Pose3D ballWorld, Pose3D cameraTorso, Pose3D cameraWorld, String headCommand);

	void setCups(Vector2D cup1, Vector2D cup2, Vector2D cup3);

	void setMixedTeamMessage(Vector3D globalPos, Vector3D localBallPos, double zOrientation);

	void setVisionState(Pose2D ownPose, Vector2D ballPos, List<Vector2D> opponents, Pose2D teamMatePose,
			String behavior, String gameState, Pose2D desiredPos, Vector2D desiredKickPos);
}