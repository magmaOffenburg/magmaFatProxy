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

import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import hso.autonomy.util.geometry.Pose2D;

public class OpponentKickOffFormation extends Formation
{
	private IRoboCupThoughtModel thoughtModel;

	public OpponentKickOffFormation(IRoboCupThoughtModel thoughtModel)
	{
		this.thoughtModel = thoughtModel;

		poses.put(1, new Pose2D(-14.5, 0));
		poses.put(2, new Pose2D(-11.4, 0));
		poses.put(3, new Pose2D(-11, -4));
		poses.put(4, new Pose2D(-0.5, -6));
		poses.put(5, new Pose2D(-0.5, 6));
		poses.put(6, new Pose2D(-4, -1.3));
		poses.put(7, new Pose2D(-2.25, 0));
		poses.put(8, new Pose2D(-0.3, 3));
		poses.put(9, new Pose2D(-0.3, -3));
		poses.put(10, new Pose2D(-4, 1.5));
		poses.put(11, new Pose2D(-11, 4));
	}

	//	@Override
	//	public Pose2D getPlayerPose(int id)
	//	{
	//		if (id == 4 || id == 5) {
	//			Optional<IPlayer> opponent = thoughtModel.getOpponentsAtMeList()
	//												 .stream()
	//												 .filter(id == 4 ? player
	//														 -> player.getPosition().getY() < -5
	//																 : player -> player.getPosition().getY() > 5)
	//												 .findFirst();
	//			if (opponent.isPresent()) {
	//				return new Pose2D(-3, opponent.get().getPosition().getY(), Angle.ZERO);
	//			}
	//		}
	//		return super.getPlayerPose(id);
	//	}
}
