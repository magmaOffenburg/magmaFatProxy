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
package magma.common.challenge;

import hso.autonomy.util.geometry.Area2D;

public class KeepAwayArea
{
	private static final float CENTER_X = 0;

	private static final float CENTER_Y = 0;

	private static final float WIDTH = 20;

	private static final float LENGTH = 20;

	private static final float WIDTH_REDUCTION_RATE = 4;

	private static final float LENGTH_REDUCTION_RATE = 4;

	private static final Area2D.Float area = new Area2D.Float(
			CENTER_X - LENGTH / 2.0f, CENTER_X + LENGTH / 2.0f, CENTER_Y - WIDTH / 2.0f, CENTER_Y + WIDTH / 2.0f);

	public static Area2D.Float calculate(float time)
	{
		time /= 60;
		float lengthReduction = LENGTH_REDUCTION_RATE / 2.0f * time;
		float widthReduction = WIDTH_REDUCTION_RATE / 2.0f * time;
		return area.applyBorder(Math.min(area.getMaxX(), lengthReduction), Math.min(area.getMaxY(), widthReduction));
	}
}
