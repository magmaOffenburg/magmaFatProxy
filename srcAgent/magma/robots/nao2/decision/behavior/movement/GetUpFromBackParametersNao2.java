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
package magma.robots.nao2.decision.behavior.movement;

import magma.robots.nao.decision.behavior.movement.getup.GetUpFromBackParameters;

public class GetUpFromBackParametersNao2 extends GetUpFromBackParameters
{
	@Override
	protected void setValues()
	{
		super.setValues();

		put(Param.TIME1, 12);
		put(Param.TIME2, 11.133841f);
		put(Param.TIME3, 16.155367f);
		put(Param.TIME4, 24.848993f);
		put(Param.HipYawPitch, -102.07887f);
		put(Param.HipPitch, 97.28174f);
		put(Param.HipRoll, 25.376493f);
		put(Param.KneePitch, -127.013535f);
		put(Param.FootPitch, -56.13343f);
		put(Param.HipPitchSpeed, 4.6851935f);
		put(Param.FootPitchSpeed, 3.3894556f);
		// Average utility: 2.733 averaged: 10 [ 2.733 -0.734 91.000 ]
	}
}
