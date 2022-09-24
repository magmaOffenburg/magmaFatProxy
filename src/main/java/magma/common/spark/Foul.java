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
package magma.common.spark;

public class Foul
{
	public enum FoulType
	{
		CROWDING(0, "crowding"),
		TOUCHING(1, "touching"),
		ILLEGAL_DEFENCE(2, "illegal defence"),
		ILLEGAL_ATTACK(3, "illegal attack"),
		INCAPABLE(4, "incapable"),
		ILLEGAL_KICK_OFF(5, "illegal kickoff"),
		CHARGING(6, "charging"),
		SELF_COLLISION(7, "self collision");

		private int index;

		private String name;

		FoulType(int index, String name)
		{
			this.index = index;
			this.name = name;
		}

		public String toString()
		{
			return name;
		}

		public String getName()
		{
			return name;
		}
	}

	public final float time;

	public final int index;

	public final FoulType type;

	public final PlaySide playSide;

	public final int agentID;

	public Foul(float time, int index, FoulType type, int team, int agentID)
	{
		this.time = time;
		this.index = index;
		this.type = type;
		this.agentID = agentID;
		playSide = getPlaySide(team);
	}

	private PlaySide getPlaySide(int team)
	{
		switch (team) {
		case 1:
			return PlaySide.LEFT;
		case 2:
			return PlaySide.RIGHT;
		default:
			return PlaySide.UNKNOWN;
		}
	}
}
