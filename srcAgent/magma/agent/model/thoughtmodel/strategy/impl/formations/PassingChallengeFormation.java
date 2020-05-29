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
package magma.agent.model.thoughtmodel.strategy.impl.formations;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;

public class PassingChallengeFormation extends Formation
{
	public PassingChallengeFormation()
	{
		poses.put(7, new Pose2D(4, 1));
		poses.put(8, new Pose2D(-0.2, -0.1, Angle.deg(35)));
		poses.put(9, new Pose2D(8, 0));
		poses.put(10, new Pose2D(13, 1));
	}
}