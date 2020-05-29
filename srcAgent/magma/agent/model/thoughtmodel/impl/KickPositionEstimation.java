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
package magma.agent.model.thoughtmodel.impl;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * An estimation for utility to kick to a single point on the field
 * @author kdorer
 */
public class KickPositionEstimation implements Comparable<KickPositionEstimation>
{
	/** the represented position */
	private Vector3D position;

	/** the utility of this point */
	private float utility;

	public KickPositionEstimation(Vector3D position, float utility)
	{
		super();
		this.position = position;
		this.utility = utility;
	}

	public Vector3D getPosition()
	{
		return position;
	}

	public float getUtility()
	{
		return utility;
	}

	@Override
	public int compareTo(KickPositionEstimation o)
	{
		float delta = o.utility - utility;
		if (delta < 0) {
			return -1;
		} else if (delta > 0) {
			return 1;
		}
		return 0;
	}

	@Override
	public String toString()
	{
		return "utility: " + utility + " pos: " + position;
	}
}
