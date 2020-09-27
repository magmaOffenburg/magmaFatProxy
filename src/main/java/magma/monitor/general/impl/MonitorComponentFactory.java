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
package magma.monitor.general.impl;

import hso.autonomy.util.connection.IServerConnection;
import hso.autonomy.util.connection.impl.ServerConnection;
import magma.monitor.command.IServerCommander;
import magma.monitor.command.impl.ServerCommander;
import magma.monitor.messageparser.IMonitorMessageParser;
import magma.monitor.messageparser.impl.MonitorMessageParser;
import magma.monitor.referee.IReferee;
import magma.monitor.referee.impl.DummyReferee;
import magma.monitor.worldmodel.IMonitorWorldModel;
import magma.monitor.worldmodel.impl.MonitorWorldModel;

public class MonitorComponentFactory
{
	protected FactoryParameters params;

	public MonitorComponentFactory(FactoryParameters parameterObject)
	{
		this.params = parameterObject;
	}

	/**
	 * Create a ServerConnection
	 *
	 * @param host Host name/IP
	 * @param port Server port
	 * @return New Server connection
	 */
	public IServerConnection createConnection(String host, int port)
	{
		return new ServerConnection(host, port);
	}

	public IMonitorMessageParser createMessageParser()
	{
		return new MonitorMessageParser();
	}

	/**
	 * Create a ServerCommander
	 *
	 * @param connection - the server connection
	 * @return New ServerCommander
	 */
	public IServerCommander createServerCommander(IServerConnection connection)
	{
		return new ServerCommander(connection);
	}

	public IMonitorWorldModel createWorldModel()
	{
		return new MonitorWorldModel();
	}

	/**
	 * Create a Referee
	 *
	 * @param worldModel - the world model of the monitor
	 * @param serverCommander - the command interface to send server commands
	 * @return New Referee
	 */
	public IReferee createReferee(IMonitorWorldModel worldModel, IServerCommander serverCommander)
	{
		return new DummyReferee(worldModel, serverCommander);
	}
}
