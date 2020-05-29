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
package magma.agent.communication.channel.impl;

/**
 * Stores all necessary player parameters (Server connection, decision maker
 * etc.)#
 *
 * @author Klaus Dorer
 */
public class ChannelParameters
{
	private final String teamname;

	private byte teamID;

	private final int playerNumber;

	private final String host;

	private final int port;

	private boolean logPerception;

	/**
	 * Instantiates and initializes a new PlayerParameters object
	 */
	public ChannelParameters(String teamname, byte teamID, int playerNumber, String host, int port)
	{
		this.teamname = teamname;
		this.teamID = teamID;
		this.playerNumber = playerNumber;
		this.host = host;
		this.port = port;
		this.logPerception = false;
	}

	public ChannelParameters(
			String teamname, byte teamID, int playerNumber, String host, int port, boolean logPerception)
	{
		this.teamname = teamname;
		this.teamID = teamID;
		this.playerNumber = playerNumber;
		this.host = host;
		this.port = port;
		this.logPerception = logPerception;
	}

	/**
	 * Retrieve the team name
	 *
	 * @return Team name
	 */
	public String getTeamname()
	{
		return teamname;
	}

	/**
	 * Retrieve the player number
	 *
	 * @return Player number
	 */
	public int getPlayerNumber()
	{
		return playerNumber;
	}

	/**
	 * Retrieve the server host
	 *
	 * @return Server host string
	 */
	public String getHost()
	{
		return host;
	}

	/**
	 * Retrieve the server port
	 *
	 * @return Server port
	 */
	public int getPort()
	{
		return port;
	}

	public byte getTeamID()
	{
		return teamID;
	}

	public void setTeamID(byte teamID)
	{
		this.teamID = teamID;
	}

	public boolean isLogPerception()
	{
		return logPerception;
	}
}