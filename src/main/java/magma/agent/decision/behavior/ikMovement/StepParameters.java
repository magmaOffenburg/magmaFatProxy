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

import java.io.Serializable;

/**
 * @author Stefan Glaser
 */
public class StepParameters implements Serializable
{
	/**
	 * amplitude of the side step function (how much we want to walk sidewise)
	 */
	public double xTargetDistance;

	/** amplitude of the forward function (how much we want to walk forward) */
	public double yTargetDistance;

	/** amplitude of the up function (how much we want to lift the leg) */
	public double zTargetDistance;

	/** number of degrees we are currently turning */
	public double turnAngle;

	public StepParameters()
	{
		this.xTargetDistance = 0;
		this.yTargetDistance = 0;
		this.zTargetDistance = 0;
		this.turnAngle = 0;
	}

	public StepParameters(double xTargetDistance, double yTargetDistance, double zTargetDistance, double turnAngle)
	{
		this.xTargetDistance = xTargetDistance;
		this.yTargetDistance = yTargetDistance;
		this.zTargetDistance = zTargetDistance;
		this.turnAngle = turnAngle;
	}

	/**
	 * Copies the values of the other StepParameter instance into this instance.
	 *
	 * @param other - the instance to copy
	 * @return this
	 */
	public StepParameters from(StepParameters other)
	{
		this.xTargetDistance = other.xTargetDistance;
		this.yTargetDistance = other.yTargetDistance;
		this.zTargetDistance = other.zTargetDistance;
		this.turnAngle = other.turnAngle;
		return this;
	}

	/**
	 * Resets all values to 0.
	 * @return this
	 */
	public StepParameters clear()
	{
		this.xTargetDistance = 0;
		this.yTargetDistance = 0;
		this.zTargetDistance = 0;
		this.turnAngle = 0;
		return this;
	}

	/**
	 * @return a copy of this instance
	 */
	public StepParameters copy()
	{
		return new StepParameters(xTargetDistance, yTargetDistance, zTargetDistance, turnAngle);
	}

	@Override
	public String toString()
	{
		return "f/b: " + String.format("%.4f", yTargetDistance) + ", l/r: " + String.format("%.4f", xTargetDistance) +
				", height: " + String.format("%.4f", zTargetDistance) +
				", turn l/r: " + String.format("%.4f", turnAngle);
	}
}
