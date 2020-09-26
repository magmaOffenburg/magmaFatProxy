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
package magma.agent.communication.action.impl;

import hso.autonomy.agent.communication.action.IActionPerformer;
import hso.autonomy.agent.communication.action.IEffector;
import hso.autonomy.agent.communication.action.impl.Action;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose3D;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import magma.agent.communication.action.IMixedTeamEffector;
import magma.agent.communication.action.IRoboCupAction;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * This class generates server messages from Effector objects.
 */
public class RoboCupAction extends Action implements IRoboCupAction
{
	private final SayEffector say;

	private final BeamEffector beam;

	private final DecisionEffector decision;

	private final CameraPositionEffector cameraPos;

	private final CupsPositionEffector cupsPos;

	private final MixedTeamEffector mixedTeam;

	/**
	 * Initializes all effectors that occur in the simulation
	 *
	 * @param actionPerformer the component that can send the actions
	 */
	public RoboCupAction(IActionPerformer actionPerformer)
	{
		super(actionPerformer);
		beam = new BeamEffector();
		say = new SayEffector();
		decision = new DecisionEffector();
		cameraPos = new CameraPositionEffector();
		cupsPos = new CupsPositionEffector();
		mixedTeam = new MixedTeamEffector();
	}

	/**
	 * Initializes the effectors
	 */
	@Override
	public void init(Map<String, IEffector> effectors)
	{
		this.effectors = effectors;
		actionEffectors = new HashMap<>(effectors);
		actionEffectors.put(say.getName(), say);
		actionEffectors.put(decision.getName(), decision);
		actionEffectors.put(cameraPos.getName(), cameraPos);
		actionEffectors.put(cupsPos.getName(), cupsPos);
		actionEffectors.put(mixedTeam.getName(), mixedTeam);
	}

	@Override
	public void sendBeamString(Pose2D pose)
	{
		beam.setEffectorValues((float) pose.x, (float) pose.y, (float) pose.angle.degrees());
		send(Collections.singletonMap(beam.getName(), beam));
		beam.resetAfterAction();
	}

	@Override
	public void sayMessage(String message)
	{
		say.setMessage(message);
	}

	@Override
	public void setDecision(int headCommand, float headTargetYaw, float headTargetPitch, int movementCommand,
			float intendedX, float intendedY, float intendedTurn, float relBallPosX, float relBallPosY,
			float relBallTargetPosX, float relBallTargetPosY, boolean remoteControlled, int sayCommand,
			Map<String, String> sayParams)
	{
		decision.setEffectorValues(headCommand, headTargetYaw, headTargetPitch, movementCommand, intendedX, intendedY,
				intendedTurn, relBallPosX, relBallPosY, relBallTargetPosX, relBallTargetPosY,
				(remoteControlled) ? 3 : 2);
		decision.setSayCommand(sayCommand, sayParams);
	}

	@Override
	public void setCameraPosition(Pose3D ballWorld, Pose3D cameraTorso, Pose3D cameraWorld, String headCommand)
	{
		CameraPositionEffector effector = (CameraPositionEffector) actionEffectors.get("CameraPositionEffector");
		if (effector != null) {
			effector.setBallWorld(ballWorld);
			effector.setCameraTorso(cameraTorso);
			effector.setCameraWorld(cameraWorld);
			effector.setHeadBehavior(headCommand);
		}
	}

	@Override
	public void setCups(Vector2D cup1, Vector2D cup2, Vector2D cup3)
	{
		CupsPositionEffector effector = (CupsPositionEffector) actionEffectors.get("cups");
		if (effector != null) {
			effector.setEffectorValues((float) cup1.getX(), (float) cup1.getY(), (float) cup2.getX(),
					(float) cup2.getY(), (float) cup3.getX(), (float) cup3.getY());
		}
	}

	@Override
	public void setMixedTeamMessage(Vector3D globalPos, Vector3D localBallPos, double zOrientation)
	{
		MixedTeamEffector effector = (MixedTeamEffector) actionEffectors.get(IMixedTeamEffector.NAME);
		if (effector != null) {
			effector.setGlobalPlayerPos(globalPos);
			effector.setLocalBallPos(localBallPos);
			effector.setGlobalZOrientation(zOrientation);
		}
	}

	@Override
	public void setVisionState(Pose2D ownPose, Vector2D ballPos, List<Vector2D> opponents, Pose2D teamMatePose,
			String behavior, String gameState, Pose2D desiredPos, Vector2D desiredKickPos)
	{
	}
}
