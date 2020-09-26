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
package magma.robots.nao1.decision.behavior.movement;

import magma.robots.nao.decision.behavior.movement.getup.GetUpFromBackParameters;

public class GetUpFromBackParametersNao1 extends GetUpFromBackParameters
{
	@Override
	protected void setValues()
	{
		super.setValues();

		put(Param.HipYawPitch, -94.974976f);
		put(Param.HipPitch, 101.52334f);
		put(Param.HipRoll, 28.040258f);
		put(Param.KneePitch, -129.80742f);
		put(Param.FootPitch, -53.971676f);
		put(Param.HipPitchSpeed, 4.121893f);
		put(Param.FootPitchSpeed, 6.704934f);
		// Average utility: 2.459 averaged: 10 [ 2.459 -2.856 50.000 ]
	}
}
