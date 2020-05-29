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

public class HumanoidMetaModelV2017 extends HumanoidMetaModelV2014
{
	public static final HumanoidMetaModelV2017 INSTANCE = new HumanoidMetaModelV2017();

	@Override
	protected Vector3D initGoalDimensions()
	{
		return new Vector3D(0.6, 2.6, 1.8);
	}
}
