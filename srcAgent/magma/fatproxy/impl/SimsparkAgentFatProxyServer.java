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
package magma.fatproxy.impl;

import java.net.Socket;

import hso.autonomy.util.connection.ConnectionException;
import magma.monitor.general.IMonitorRuntimeListener;
import magma.monitor.general.impl.MonitorComponentFactory;
import magma.monitor.general.impl.MonitorParameters;
import magma.monitor.general.impl.MonitorRuntime;
import magma.tools.proxy.impl.AgentProxy;
import magma.tools.proxy.impl.SimsparkAgentProxyServer;

/**
 * @author kdorer
 *
 */
public class SimsparkAgentFatProxyServer extends SimsparkAgentProxyServer implements IMonitorRuntimeListener
{
	protected MonitorRuntime monitor;

	/**
	 * @param parameterObject
	 */
	public SimsparkAgentFatProxyServer(SimsparkAgentProxyServerParameter parameterObject)
	{
		super(parameterObject);
	}

	/**
	 * Factory method to create agent proxy
	 * @param clientSocket the socket the agent proxy works on
	 * @return a new instance of agent proxy
	 */
	@Override
	protected synchronized AgentProxy createAgentProxy(Socket clientSocket)
	{
		if (monitor == null || !monitor.isConnected()) {
			startMonitor();
		}
		AgentProxy agentProxy = new AgentFatProxy(clientSocket, ssHost, ssPort, showMessages, monitor);
		agentProxy.start(clientSocket, ssHost, ssPort, showMessages);
		return agentProxy;
	}

	private void startMonitor()
	{
		try {
			new MonitorThread().start();
			Thread.sleep(1000);
			if (monitor == null || !monitor.isConnected()) {
				System.err.println("could not connect monitor port.");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private class MonitorThread extends Thread
	{
		@Override
		public void run()
		{
			monitor =
					new MonitorRuntime(new MonitorParameters(ssHost, ssPort + 100, new MonitorComponentFactory(null)));
			try {
				monitor.addRuntimeListener(SimsparkAgentFatProxyServer.this);
				monitor.startMonitor();
			} catch (ConnectionException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void monitorUpdated()
	{
	}
}
