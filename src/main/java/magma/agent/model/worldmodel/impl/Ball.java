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
package magma.agent.model.worldmodel.impl;

import hso.autonomy.agent.model.worldmodel.impl.MovableObject;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.positionFilter.IPositionFilter;
import hso.autonomy.util.geometry.positionFilter.LowFrequencyPositionFilter;
import magma.agent.model.worldmodel.IBall;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Represents the ball of the game
 */
public class Ball extends MovableObject implements IBall
{
	/** The radius of the ball in m */
	private final float radius;

	/** The decay of the ball speed */
	private final float ballDecay;

	/** The filter used to smoothen the position estimation */
	// protected final transient PositionSpeedKalmanFilter posFilter = new
	// PositionSpeedKalmanFilter();

	/** The time when the z position of the ball was "in the air" */
	public Float lastAirTime = null;

	protected transient IPositionFilter posFilter;

	// at which distance we collide with the ball
	private float collisionDistance;

	public Ball(float radius, float ballDecay, Vector3D initialPosition, float collisionDistance, float cycleTime)
	{
		super("Ball", cycleTime);
		this.radius = radius;
		this.ballDecay = ballDecay;
		this.collisionDistance = collisionDistance;
		// posFilter = new NoFilter();
		posFilter = new LowFrequencyPositionFilter(5);
		position = initialPosition;
		previousPosition = position;
	}

	@Override
	protected Vector3D[] calculateFuturePositions(int howMany)
	{
		return Geometry.getFuturePositions(position, getSpeed(), ballDecay, radius, howMany, cycleTime);
	}

	@Override
	public double getPossibleSpeed()
	{
		return 6.0;
	}

	@Override
	public void updateFromVision(Vector3D seenPosition, Vector3D localPosition, Vector3D globalPosition, float time)
	{
		if (getAge(time) > 5.0f) {
			// If our last update is older than 5 seconds, reset the position
			// filter
			posFilter.reset();
		}

		if (globalPosition.getZ() >= 0.7) {
			lastAirTime = time;
		}
		// for Kalman we need the speed in m/s
		// Vector3D observedSpeed = calculateSpeed(globalPosition, time,
		// getPosition(), lastSeenTime).scalarMultiply(50);
		// Vector3D filterPosition = posFilter.filterPosition(globalPosition,
		// observedSpeed);
		Vector3D filterPosition = posFilter.filterPosition(globalPosition, globalPosition);
		super.updateFromVision(seenPosition, localPosition, filterPosition, time);
	}

	@Override
	public double getCollisionDistance()
	{
		return collisionDistance;
	}

	@Override
	public void updateNoVision(float globalTime)
	{
		super.updateNoVision(globalTime);
		oldSpeed = speed;
		speed = speed.scalarMultiply(ballDecay);
	}

	@Override
	public float getRadius()
	{
		return radius;
	}

	@Override
	public void resetPosition(Vector3D position)
	{
		this.position = position;
		speed = oldSpeed = Vector3D.ZERO;
		posFilter.reset();
		futurePositions = null;
		lastAirTime = null;
	}

	@Override
	public boolean isBouncing(float time)
	{
		return lastAirTime != null && time - lastAirTime < 5;
	}
}