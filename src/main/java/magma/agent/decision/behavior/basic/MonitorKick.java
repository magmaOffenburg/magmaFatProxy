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
package magma.agent.decision.behavior.basic;

import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose3D;
import hso.autonomy.util.misc.ValueUtil;
import java.util.List;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.model.worldmodel.IThisPlayer;
import magma.common.spark.PlayMode;
import magma.monitor.command.IServerCommander;
import magma.monitor.general.impl.MonitorRuntime;
import magma.monitor.worldmodel.ISoccerAgent;
import magma.monitor.worldmodel.SoccerAgentBodyPart;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Behavior using the monitor command to kick the ball
 *
 * @author Klaus Dorer
 */
public class MonitorKick extends RoboCupBehavior
{
	/** how far a ball might be to kick it. Is 0.7 in 2D league */
	public static final double KICKABLE_MARGIN = 0.44;

	/** the maximum distance we should be able to kick */
	public static final double MAX_KICK_DISTANCE = 15;

	private static final String NAME = IBehaviorConstants.MONITOR_KICK;

	enum Phase
	{
		PREPARE_TOUCH,
		TOUCH,
		KICK
	}

	/** the current execution cycle count */
	private int cycle;

	private Vector3D oldBallPos;

	private float[] values;

	private boolean isFinished;

	public MonitorKick(IThoughtModel thoughtModel)
	{
		super(NAME, thoughtModel);
		cycle = 0;
		isFinished = false;
	}

	public int getCycle()
	{
		return cycle;
	}

	@Override
	public void perform()
	{
		if (getCycle() == 0) {
			monitorKick(Phase.PREPARE_TOUCH);
		} else if (getCycle() < 4) {
			monitorKick(Phase.TOUCH);
		} else if (getCycle() < 7) {
			monitorKick(Phase.KICK);
		} else {
			isFinished = true;
		}
		cycle++;
		super.perform();
	}

	@Override
	public void init()
	{
		super.init();
		cycle = 0;
		isFinished = false;
	}

	@Override
	public boolean isFinished()
	{
		return isFinished;
	}

	public void setValues(float[] values)
	{
		this.values = values;
	}

	private void monitorKick(Phase phase)
	{
		if (values == null) {
			// no monitor kick
			System.out.println("null");
			return;
		}

		MonitorRuntime monitorRuntime = getThoughtModel().getMonitorRuntime();
		IServerCommander serverCommander = monitorRuntime.getServerCommander();
		Vector3D ballPos = monitorRuntime.getWorldModel().getBall().getPosition();
		Vector3D kickStartPos = ballPos;

		IThisPlayer thisPlayer = getWorldModel().getThisPlayer();
		ISoccerAgent monitorPlayer = getPlayer(thisPlayer.getTeamname(), thisPlayer.getID());
		Vector3D kickDirection = Vector3D.ZERO;
		if (monitorPlayer != null) {
			Pose3D rightFoot = monitorPlayer.getBodyPartPose(SoccerAgentBodyPart.RIGHT_FOOT);
			if (rightFoot != null) {
				if (phase == Phase.PREPARE_TOUCH || phase == Phase.TOUCH) {
					// first phase is to let the player touch the ball
					if (phase == Phase.PREPARE_TOUCH) {
						// called first time this kick
						oldBallPos = ballPos;
					}
					kickStartPos = rightFoot.getPosition();
				} else {
					// second phase is to kick the ball from somewhere away from the players foot
					// this kick model is roughly taken from 2D simulator
					// values[0] is the distance we want to achieve and which will be achieved with factor 1.5
					kickStartPos = oldBallPos;

					// Get horizontal angle of player (global) and correct offset caused by using another coordinate
					// system internally
					Angle globalHorizontalPlayerAngle =
							monitorPlayer.getBodyPartPose(SoccerAgentBodyPart.BODY).getHorizontalAngle();
					globalHorizontalPlayerAngle = globalHorizontalPlayerAngle.add(Angle.ANGLE_90);

					double kickPower = ValueUtil.limitValue(values[0] * 1.5, 0, MAX_KICK_DISTANCE);
					double relativeHorizontalAngle = ValueUtil.limitValue(values[1], -180, 180);
					Angle globalHorizontalAngle = globalHorizontalPlayerAngle.add(Angle.deg(relativeHorizontalAngle));
					double verticalAngle = ValueUtil.limitValue(values[2], 0, 70);
					kickDirection = new Vector3D(globalHorizontalAngle.radians(), Angle.deg(verticalAngle).radians());
					Pose3D torso = monitorPlayer.getBodyPartPose(SoccerAgentBodyPart.BODY);
					Pose3D ball = new Pose3D(oldBallPos);
					double deltaAngle = Math.abs(torso.getHorizontalAngleTo(ball).degrees()) / 180;
					double distanceBall = getBallDistance();
					if (!checkBallDistance(distanceBall)) {
						// should actually not happen, should be checked before and no kick triggered
						distanceBall = KICKABLE_MARGIN;
					}
					// reduce the kick power depending on the distance and direction of the ball
					double factor = 1 - 0.25 * deltaAngle - 0.25 * distanceBall / KICKABLE_MARGIN;
					double effectiveKickPower = kickPower * factor;
					kickDirection = kickDirection.scalarMultiply(effectiveKickPower);
				}
			}
		}

		float time = monitorRuntime.getWorldModel().getTime();
		PlayMode playMode = monitorRuntime.getWorldModel().getPlayMode();
		if ((time < 299.97 || (time > 299.97 && time < 599.97)) && shouldSetPlayOn(playMode)) {
			// Prevent causing another kick-in or a goal kick
			serverCommander.setPlaymode(PlayMode.PLAY_ON);
		}

		serverCommander.beamBall((float) kickStartPos.getX(), (float) kickStartPos.getY(), (float) kickStartPos.getZ(),
				(float) kickDirection.getX(), (float) kickDirection.getY(), (float) kickDirection.getZ());
	}

	private boolean shouldSetPlayOn(PlayMode playMode)
	{
		switch (playMode) {
		case BEFORE_KICK_OFF:
		case GAME_OVER:
		case GOAL_KICK_LEFT:
		case GOAL_KICK_RIGHT:
		case GOAL_LEFT:
		case GOAL_RIGHT:
			return false;
		default:
			return true;
		}
	}

	/**
	 *
	 * @return distance: player to ball
	 */
	public double getBallDistance()
	{
		MonitorRuntime monitorRuntime = getThoughtModel().getMonitorRuntime();
		IThisPlayer thisPlayer = getWorldModel().getThisPlayer();
		ISoccerAgent monitorPlayer = getPlayer(thisPlayer.getTeamname(), thisPlayer.getID());
		if (monitorPlayer != null) {
			Pose3D torso = monitorPlayer.getBodyPartPose(SoccerAgentBodyPart.BODY);
			Vector3D ballPos = monitorRuntime.getWorldModel().getBall().getPosition();
			Vector3D playerPos = torso.getPosition();
			return ballPos.distance(new Vector3D(playerPos.getX(), playerPos.getY(), 0));
		}
		// player is (not yet) visible in the monitor protocol
		return 1000;
	}

	/**
	 * compare ballDistance to KickableMargin
	 *
	 * @return true, if Ball is in Kickable_Margin
	 */
	public boolean checkBallDistance()
	{
		return checkBallDistance(getBallDistance());
	}

	public boolean checkBallDistance(double distance)
	{
		return distance <= KICKABLE_MARGIN;
	}

	/**
	 * check if kicking is allowed in the current play mode
	 *
	 * @return true, if the kick is currently allowed
	 */
	public boolean checkPlaymode()
	{
		switch (getThoughtModel().getMonitorRuntime().getWorldModel().getPlayMode()) {
		case BEFORE_KICK_OFF:
		case GOAL_LEFT:
		case GOAL_RIGHT:
			return false;
		case KICK_OFF_LEFT:
		case KICK_OFF_RIGHT:
			IThisPlayer thisPlayer = getWorldModel().getThisPlayer();
			if (getPlayer(thisPlayer.getTeamname(), thisPlayer.getID()).getPosition().distance(new Vector3D(0, 0, 0)) >
					2) {
				// Assumption: the kick off always happens at (0, 0) -> a bit hacky, but it should work for our purposes
				// If the player is far away from the middle of the field, he's not going to do the kick off.
				// -> Forbid the kick. The goalie may otherwise be able to execute a kick in the first cycle of the
				// kick off because he may have missed the update of the ball position or something similar.
				return false;
			}
		default:
			return true;
		}
	}

	// TODO: this method rather belongs into magmaMonitor!
	public ISoccerAgent getPlayer(String team, int number)
	{
		List<? extends ISoccerAgent> soccerAgents =
				getThoughtModel().getMonitorRuntime().getWorldModel().getSoccerAgents();
		for (ISoccerAgent iSoccerAgent : soccerAgents) {
			if (team.equals(iSoccerAgent.getTeamName()) && number == iSoccerAgent.getPlayerID()) {
				return iSoccerAgent;
			}
		}
		return null;
	}
}
