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
import magma.common.spark.PlaySide;
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
					globalHorizontalPlayerAngle = globalHorizontalPlayerAngle.add(
							getWorldModel().getPlaySide().equals(PlaySide.LEFT) ? Angle.ANGLE_90
																				: Angle.ANGLE_90.negate());

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

		if (monitorRuntime.getWorldModel().getTime() < 300.0 ||
				(monitorRuntime.getWorldModel().getTime() > 300.0 &&
						monitorRuntime.getWorldModel().getTime() < 600.0)) {
			PlayMode playMode = monitorRuntime.getWorldModel().getPlayMode();
			switch (playMode) {
			case CORNER_KICK_LEFT:
			case CORNER_KICK_RIGHT:
			case KICK_IN_LEFT:
			case KICK_IN_RIGHT:
				// Prevent causing another kick-in
				serverCommander.setPlaymode(PlayMode.PLAY_ON);
				break;
			default:
				break;
			}
		}

		serverCommander.beamBall((float) kickStartPos.getX(), (float) kickStartPos.getY(), (float) kickStartPos.getZ(),
				(float) kickDirection.getX(), (float) kickDirection.getY(), (float) kickDirection.getZ());
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
