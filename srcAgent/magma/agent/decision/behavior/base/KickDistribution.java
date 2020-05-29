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
package magma.agent.decision.behavior.base;

import java.io.Serializable;

/**
 * Probability distribution of kick distance and angle. Usually measured over
 * 1000 avgOutRuns with magmaLearning's KickProblem.
 */
public class KickDistribution implements Serializable
{
	public static final double DISTANCE_INTERVAL = 0.5;

	public static final double ANGLE_INTERVAL = 2;

	private final double[] distance;

	private final double[] angle;

	public KickDistribution(double[] distance, double[] angle)
	{
		this.distance = distance;
		this.angle = angle;
	}

	/** @param randomValue random value between 0 and 1 */
	public double getDistanceSample(double randomValue)
	{
		return sample(randomValue, distance) * DISTANCE_INTERVAL;
	}

	/** @param randomValue random value between 0 and 1 */
	public double getAngleSample(double randomValue)
	{
		return sample(randomValue, angle) * ANGLE_INTERVAL;
	}

	private double sample(double randomValue, double[] probabilities)
	{
		double sum = 0;

		for (int i = 0; i < probabilities.length; i++) {
			sum += probabilities[i];
			if (randomValue < sum) {
				return i;
			}
		}

		return probabilities.length - 1;
	}
}
