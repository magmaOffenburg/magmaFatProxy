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
package magma.agent.agentruntime;

import kdo.util.parameter.ParameterMap;
import magma.agent.IMagmaConstants;
import magma.agent.communication.channel.impl.ChannelParameters;
import magma.common.spark.IConnectionConstants;
import magma.util.roboviz.RoboVizParameters;

/**
 * Stores all necessary player parameters (Server connection, decision maker
 * etc.)
 *
 * @author Klaus Dorer
 */
public class PlayerParameters
{
	private final ChannelParameters channelParams;

	private final ParameterMap parameterMap;

	private final int serverVersion;

	private final String decisionMakerName;

	private final ComponentFactory factory;

	private RoboVizParameters roboVizParams;

	private final boolean reportStats;

	private final boolean switchDecisionmaker;

	/**
	 * Instantiates and initializes a new PlayerParameters object
	 *
	 * @param teamname Team name
	 * @param playerNumber Player number
	 * @param host Server host
	 * @param port Server port
	 * @param behaviorParams Behavior parameters
	 * @param serverVersion Server version
	 * @param factory Reference to the component factory
	 * @param decisionMakerName Name of the decision maker
	 */
	public PlayerParameters(String teamname, byte teamID, int playerNumber, String host, int port,
			ParameterMap behaviorParams, int serverVersion, ComponentFactory factory, String decisionMakerName,
			boolean reportStats)
	{
		this(new ChannelParameters(teamname, teamID, playerNumber, host, port), behaviorParams, serverVersion, factory,
				decisionMakerName, reportStats, false);
	}

	public PlayerParameters(String teamname, byte teamID, int playerNumber, String host, int port,
			ParameterMap behaviorParams, int serverVersion, ComponentFactory factory, String decisionMakerName,
			RoboVizParameters roboVizParams, boolean reportStats)
	{
		this(new ChannelParameters(teamname, teamID, playerNumber, host, port), behaviorParams, serverVersion, factory,
				decisionMakerName, reportStats, false);
		this.roboVizParams = roboVizParams;
	}

	public PlayerParameters(ChannelParameters channelParams, ParameterMap behaviorParams, int serverVersion,
			ComponentFactory factory, String decisionMakerName, boolean reportStats, boolean switchDecisionmaker)
	{
		this.channelParams = channelParams;
		if (behaviorParams == null) {
			behaviorParams = new ParameterMap();
		}
		this.parameterMap = behaviorParams;
		this.serverVersion = serverVersion;
		this.factory = factory;
		this.decisionMakerName = decisionMakerName;
		this.reportStats = reportStats;
		this.switchDecisionmaker = switchDecisionmaker;
	}

	public PlayerParameters(ChannelParameters channelParams, ParameterMap behaviorParams, int serverVersion,
			ComponentFactory factory, String decisionMakerName, RoboVizParameters roboVizParams, boolean reportStats,
			boolean switchDecisionmaker)
	{
		this(channelParams, behaviorParams, serverVersion, factory, decisionMakerName, reportStats,
				switchDecisionmaker);
	}

	public PlayerParameters(ComponentFactory factory)
	{
		this(IMagmaConstants.DEFAULT_TEAMNAME, IMagmaConstants.DEFAULT_TEAMID, 8, IConnectionConstants.SERVER_IP,
				IConnectionConstants.AGENT_PORT, null, IMagmaConstants.DEFAULT_SERVER_VERSION, factory,
				IMagmaConstants.DEFAULT_DECISION_MAKER, false);
	}

	/**
	 * @return the parameters that are used to setup channels
	 */
	public ChannelParameters getChannelParams()
	{
		return channelParams;
	}

	/**
	 * Retrieve the team name
	 *
	 * @return Team name
	 */
	public String getTeamname()
	{
		return channelParams.getTeamname();
	}

	public byte getTeamID()
	{
		return channelParams.getTeamID();
	}

	/**
	 * Retrieve the player number
	 *
	 * @return Player number
	 */
	public int getPlayerNumber()
	{
		return channelParams.getPlayerNumber();
	}

	/**
	 * Retrieve the server host
	 *
	 * @return Server host string
	 */
	public String getHost()
	{
		return channelParams.getHost();
	}

	/**
	 * Retrieve the server port
	 *
	 * @return Server port
	 */
	public int getPort()
	{
		return channelParams.getPort();
	}

	/**
	 * Retrieve the behavior parameters
	 *
	 * @return Behavior parameters
	 */
	public ParameterMap getParameterMap()
	{
		return parameterMap;
	}

	/**
	 * Retrieve the server version
	 *
	 * @return Server version
	 */
	public int getServerVersion()
	{
		return serverVersion;
	}

	/**
	 * Retrieve the reference to the component factory
	 *
	 * @return Component factory reference
	 */
	public ComponentFactory getComponentFactory()
	{
		return factory;
	}

	/**
	 * Retrieve the decision maker name
	 *
	 * @return Decision maker name
	 */
	public String getDecisionMakerName()
	{
		return decisionMakerName;
	}

	public RoboVizParameters getRoboVizParams()
	{
		return roboVizParams;
	}

	public boolean getReportStats()
	{
		return reportStats;
	}

	public boolean isSwitchDecisionmaker()
	{
		return switchDecisionmaker;
	}
}