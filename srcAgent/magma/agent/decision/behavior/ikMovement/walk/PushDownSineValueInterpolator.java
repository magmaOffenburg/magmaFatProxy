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
package magma.agent.decision.behavior.ikMovement.walk;

import hso.autonomy.util.geometry.interpolation.progress.LinearProgress;
import hso.autonomy.util.geometry.interpolation.progress.ProgressFunction;
import hso.autonomy.util.geometry.interpolation.value.ValueInterpolatorBase;

/**
 * @author Stefan Glaser
 */
public class PushDownSineValueInterpolator extends ValueInterpolatorBase
{
	public double amplitude;

	public PushDownSineValueInterpolator()
	{
		this(new LinearProgress(), 0);
	}

	public PushDownSineValueInterpolator(ProgressFunction progress, double amplitude)
	{
		super(progress);

		this.amplitude = amplitude;
	}

	@Override
	protected double calculateInterpolationValue(double initial, double target, float t)
	{
		if (t > 0.98) {
			return target;
		}

		double sin = Math.sin(t * Math.PI / 2) * amplitude;

		return initial + sin;
	}
}
