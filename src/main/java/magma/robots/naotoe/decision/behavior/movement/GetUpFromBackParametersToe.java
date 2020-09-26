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
package magma.robots.naotoe.decision.behavior.movement;

import magma.robots.nao.decision.behavior.movement.getup.GetUpFromBackParameters;

public class GetUpFromBackParametersToe extends GetUpFromBackParameters
{
	@Override
	protected void setValues()
	{
		super.setValues();

		put(Param.TIME1, 0);
		put(Param.TIME2, 9.863538f);
		put(Param.TIME3, 16.11139f);
		put(Param.TIME4, 23.806534f);
		put(Param.HipYawPitch, -94.805695f);
		put(Param.HipPitch, 97.308495f);
		put(Param.HipRoll, 29.511276f);
		put(Param.KneePitch, -122.90438f);
		put(Param.FootPitch, -55.082344f);
		put(Param.HipPitchSpeed, 5.433256f);
		put(Param.FootPitchSpeed, 1.004598f);
		// Average utility: 2.798 averaged: 10 [ 2.798 -2.025 94.900 ]
	}
}
