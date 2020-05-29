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
/**
 *
 */
package magma.agent.decision.behavior.base;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import magma.agent.decision.behavior.IKickParameters;
import magma.agent.decision.behavior.SupportFoot;

/**
 * @author kdorer
 *
 */
public class KickParameters implements IKickParameters
{
	public final static float DEFAULT_MAX_OPP_DISTANCE = 10000;

	/** The intended kick direction in the global system */
	private Angle intendedKickDirection;

	/** The direction this kick will go to in the global system */
	private Angle kickDirection;

	/** the direction the ball will go in player relative system */
	private Angle relativeKickDirection;

	/** the distance we want to kick */
	private float intendedKickDistance;

	/** The kicking foot. */
	private final SupportFoot kickingFoot;

	/**
	 * The pose relative to the ball, to which we should navigate in order to be
	 * able to perform this kick.<br>
	 * (The pose is interpreted in the global system)
	 */
	private final Pose2D relativeRunToPose;

	/**
	 * the speed (local coordinates of the run to pose) we want to have at the
	 * kicking position
	 */
	private final Vector2D speedAtRunToPose;

	/** The maximum kick distance of the kick. */
	private final double maxKickDistance;

	/** The minimum kick distance of the kick. */
	private final double minKickDistance;

	/** How far an opponent has to be so that we kick */
	private double opponentMinDistance;

	/** we do not consider a dribbling if opponent is too far */
	private double opponentMaxDistance;

	/** max speed (in m/cycle) the ball may have to perform the kick */
	private float ballMaxSpeed;

	/** the number of cycles this kick needs to hit the ball from starting */
	private int ballHitCycles;

	/** the relative priority compared to other kicks */
	private float priority;

	/** the global position we expect the ball to be for kick */
	private Vector3D expectedBallPosition;

	public KickParameters(
			SupportFoot kickingFoot, Pose2D relativeRunToPose, Angle relativeKickDirection, double maxKickDistance)
	{
		this(kickingFoot, relativeRunToPose, Vector2D.ZERO, relativeKickDirection, relativeKickDirection,
				maxKickDistance, maxKickDistance, 0, DEFAULT_MAX_OPP_DISTANCE, 0.007f, 1, 10);
	}

	public KickParameters(SupportFoot kickingFoot, Pose2D relativeRunToPose, Vector2D speedAtRunToPose,
			Angle relativeKickDirection, Angle kickDirection, double maxKickDistance, double minKickDistance,
			float opponentMinDistance, float opponentMaxDistance, float ballMaxSpeed, int ballHitCycles, float priority)
	{
		this.relativeKickDirection = relativeKickDirection;
		Pose2D kickDirPose = new Pose2D(0, 0, relativeKickDirection);
		this.kickingFoot = kickingFoot;
		this.relativeRunToPose = kickDirPose.applyInverseTo(relativeRunToPose);
		this.speedAtRunToPose = speedAtRunToPose;
		this.maxKickDistance = maxKickDistance;
		this.minKickDistance = minKickDistance;
		this.ballMaxSpeed = ballMaxSpeed;
		this.ballHitCycles = ballHitCycles;
		this.opponentMinDistance = opponentMinDistance;
		this.opponentMaxDistance = opponentMaxDistance;
		this.priority = priority;
		this.kickDirection = kickDirection;
		intendedKickDirection = Angle.ZERO;
		expectedBallPosition = Vector3D.ZERO;
	}

	@Override
	public Angle getIntendedKickDirection()
	{
		return intendedKickDirection;
	}

	@Override
	public void setIntendedKickDirection(Angle intendedKickDirection)
	{
		this.intendedKickDirection = intendedKickDirection;
	}

	@Override
	public float getIntendedKickDistance()
	{
		return intendedKickDistance;
	}

	@Override
	public void setIntendedKickDistance(float intendedKickDistance)
	{
		this.intendedKickDistance = intendedKickDistance;
	}

	@Override
	public void setExpectedBallPosition(Vector3D pos)
	{
		expectedBallPosition = pos;
	}

	@Override
	public Vector3D getExpectedBallPosition()
	{
		return expectedBallPosition;
	}

	@Override
	public SupportFoot getKickingFoot()
	{
		return kickingFoot;
	}

	@Override
	public Pose2D getRelativeRunToPose()
	{
		return relativeRunToPose;
	}

	@Override
	public double getMaxKickDistance()
	{
		return maxKickDistance;
	}

	@Override
	public double getMinKickDistance()
	{
		return minKickDistance;
	}

	@Override
	public double getOpponentMinDistance()
	{
		return opponentMinDistance;
	}

	public void setOpponentMinDistance(double opponentMinDistance)
	{
		this.opponentMinDistance = opponentMinDistance;
	}

	@Override
	public float getBallMaxSpeed()
	{
		return ballMaxSpeed;
	}

	public void setBallMaxSpeed(float ballMaxSpeed)
	{
		this.ballMaxSpeed = ballMaxSpeed;
	}

	@Override
	public double getOpponentMaxDistance()
	{
		return opponentMaxDistance;
	}

	public void setOpponentMaxDistance(double opponentMaxDistance)
	{
		this.opponentMaxDistance = opponentMaxDistance;
	}

	@Override
	public float getPriority()
	{
		return priority;
	}

	public void setPriority(float priority)
	{
		this.priority = priority;
	}

	@Override
	public Angle getKickDirection()
	{
		return kickDirection;
	}

	public void setKickDirection(Angle kickDirection)
	{
		this.kickDirection = kickDirection;
	}

	@Override
	public int getBallHitCycles()
	{
		return ballHitCycles;
	}

	public void setBallHitCycles(int ballHitCycles)
	{
		this.ballHitCycles = ballHitCycles;
	}

	@Override
	public Vector2D getSpeedAtRunToPose()
	{
		return speedAtRunToPose;
	}

	@Override
	public Angle getRelativeKickDirection()
	{
		return relativeKickDirection;
	}
}
