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

import hso.autonomy.util.connection.ConnectionException;
import hso.autonomy.util.connection.IServerConnection;
import hso.autonomy.util.observer.IObserver;
import java.util.ArrayList;
import magma.monitor.command.IServerCommander;
import magma.monitor.general.IMonitorRuntime;
import magma.monitor.general.IMonitorRuntimeListener;
import magma.monitor.messageparser.IMonitorMessageParser;
import magma.monitor.referee.IReferee;
import magma.monitor.worldmodel.IMonitorWorldModel;

public class MonitorRuntime implements IObserver<byte[]>, IMonitorRuntime
{
	private ArrayList<IMonitorRuntimeListener> listeners;

	protected IServerConnection connection;

	protected IMonitorMessageParser messageParser;

	protected IServerCommander commander;

	protected IMonitorWorldModel worldModel;

	protected IReferee referee;

	public MonitorRuntime(MonitorParameters parameter)
	{
		MonitorComponentFactory factory = parameter.getFactory();

		// communication layer
		connection = factory.createConnection(parameter.getHost(), parameter.getPort());

		// protocol layer
		messageParser = factory.createMessageParser();
		commander = factory.createServerCommander(connection);

		// model layer
		worldModel = factory.createWorldModel();

		// decision making layer
		referee = factory.createReferee(worldModel, commander);

		// attach runtime to connection
		connection.attach(this);
	}

	@Override
	public void update(byte[] content)
	{
		// 1. Parse the message and build internal update structure
		messageParser.update(content);

		// Update state with new information
		worldModel.update(messageParser);

		// Call referee to decide next action
		if (referee.decide()) {
			stopMonitor();
		}

		// publish end of cycle
		publishMonitorUpdated();
	}

	/**
	 * Starts the connection to the server, will only return after disconnection
	 * Uses default IP and port
	 */
	public void startMonitor() throws ConnectionException
	{
		connection.establishConnection();
		connection.startReceiveLoop();
	}

	/**
	 * Check if the monitor is connected to the server
	 *
	 * @return True if connected, false if not
	 */
	public boolean isConnected()
	{
		return connection.isConnected();
	}

	/**
	 * Stops the connection to the server after the next message was received
	 */
	public void stopMonitor()
	{
		connection.stopReceiveLoop();
	}

	@Override
	public IServerConnection getServerConnection()
	{
		return connection;
	}

	@Override
	public IMonitorMessageParser getMessageParser()
	{
		return messageParser;
	}

	@Override
	public IServerCommander getServerCommander()
	{
		return commander;
	}

	@Override
	public IMonitorWorldModel getWorldModel()
	{
		return worldModel;
	}

	@Override
	public IReferee getReferee()
	{
		return referee;
	}

	protected void publishMonitorUpdated()
	{
		if (listeners == null) {
			return;
		}

		listeners.forEach(IMonitorRuntimeListener::monitorUpdated);
	}

	@Override
	public boolean addRuntimeListener(IMonitorRuntimeListener listener)
	{
		if (listeners == null) {
			listeners = new ArrayList<>();
		}

		return listeners.add(listener);
	}

	@Override
	public boolean removeRuntimeListener(IMonitorRuntimeListener listener)
	{
		if (listeners == null) {
			return false;
		}

		return listeners.remove(listener);
	}
}
