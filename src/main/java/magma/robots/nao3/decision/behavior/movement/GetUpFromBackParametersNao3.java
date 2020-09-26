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
package magma.robots.nao3.decision.behavior.movement;

import magma.robots.nao.decision.behavior.movement.getup.GetUpFromBackParameters;

public class GetUpFromBackParametersNao3 extends GetUpFromBackParameters
{
	@Override
	protected void setValues()
	{
		super.setValues();

		put(Param.HipYawPitch, -97.96448f);
		put(Param.HipPitch, 99.048546f);
		put(Param.HipRoll, 25.762905f);
		put(Param.KneePitch, -126.554405f);
		put(Param.FootPitch, -56.232098f);
		put(Param.HipPitchSpeed, 6.409752f);
		put(Param.FootPitchSpeed, 1.0555235f);
		// Average utility: 2.356 averaged: 10 [ 2.356 -0.983 51.200 ]
	}
}
