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

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class RCServerMetaModelV66 extends RCServerMetaModelV64
{
	public static final RCServerMetaModelV66 INSTANCE = new RCServerMetaModelV66();

	@Override
	public int getVersion()
	{
		return 66;
	}

	@Override
	public float getBallDecay()
	{
		return 0.987f;
	}

	@Override
	protected Vector2D initFieldDimensions()
	{
		return new Vector2D(30, 20);
	}

	@Override
	public float getMiddleCircleRadius()
	{
		return 2;
	}
}
