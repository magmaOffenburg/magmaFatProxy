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
package magma.agent.model.worldmeta.impl;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class RCServerMetaModelV63 extends RCServerMetaModelV62
{
	public static final RCServerMetaModelV63 INSTANCE = new RCServerMetaModelV63();

	@Override
	public int getVersion()
	{
		return 63;
	}

	@Override
	protected Vector2D initFieldDimensions()
	{
		return new Vector2D(18, 12);
	}

	@Override
	protected Vector3D initGoalDimensions()
	{
		return new Vector3D(0.6, 2.1, 0.8);
	}

	@Override
	protected Vector2D initPenaltyAreaDimensions()
	{
		return new Vector2D(1.8, 3.9);
	}

	@Override
	public float getMiddleCircleRadius()
	{
		return 1.8f;
	}
}
