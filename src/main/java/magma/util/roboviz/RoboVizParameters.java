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
package magma.util.roboviz;

public class RoboVizParameters
{
	private final boolean enabled;

	private final String server;

	private final int port;

	private final int agentNum;

	public RoboVizParameters(boolean enabled, int agentNum)
	{
		this(enabled, RoboVizDraw.DEFAULT_HOST, RoboVizDraw.DEFAULT_PORT, agentNum);
	}

	public RoboVizParameters(boolean enabled, String server, int port, int agentNum)
	{
		this.enabled = enabled;
		this.server = server;
		this.port = port;
		this.agentNum = agentNum;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public String getServer()
	{
		return server;
	}

	public int getPort()
	{
		return port;
	}

	public int getAgentNum()
	{
		return agentNum;
	}
}
