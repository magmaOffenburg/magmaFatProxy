/*******************************************************************************
 * Copyright 2008, 2012 Hochschule Offenburg
 * Klaus Dorer, Mathias Ehret, Stefan Glaser, Thomas Huber, Fabian Korak,
 * Simon Raffeiner, Srinivasa Ragavan, Thomas Rinklin,
 * Joachim Schilling, Ingo Schindler, Rajit Shahi, Bjoern Weiler
 *
 * This file is part of magmaOffenburg.
 *
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
package magma.monitor.server;

import hso.autonomy.util.misc.UnixCommandUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import magma.common.spark.IConnectionConstants;

/**
 * @author Klaus Dorer
 */
public class ServerController
{
	/** the process handle for the soccer server process */
	private Process serverProcess;

	/** port for the agents to connect to */
	private int agentPort;

	/** port for the monitor to connect to */
	private int monitorPort;

	private boolean useRandomPorts;

	/** path where we lookup scripts for server, null if not used */
	private String scriptPath;

	private int id;

	public ServerController()
	{
		this(false, 0);
	}

	public ServerController(boolean useRandomPorts, int id)
	{
		this(IConnectionConstants.AGENT_PORT, IConnectionConstants.MONITOR_PORT, useRandomPorts, null, id);
	}

	public ServerController(int agentPort, int monitorPort, boolean useRandomPorts, String scriptPath, int id)
	{
		this.agentPort = agentPort;
		this.monitorPort = monitorPort;
		this.useRandomPorts = useRandomPorts;
		this.scriptPath = scriptPath;
		this.id = id;
		serverProcess = null;
	}

	/**
	 * @return the handle for the server process
	 */
	public Process startServer(long waitTime) throws ServerException
	{
		Process server = null;
		try {
			if (useRandomPorts) {
				agentPort = (int) (Math.random() * 55000 + 10000);
				monitorPort = agentPort + 1;
			}

			List<String> command = new ArrayList<>();
			command.add("rcssserver3d");
			command.add("--agent-port");
			command.add(agentPort + "");
			command.add("--server-port");
			command.add(monitorPort + "");

			if (scriptPath != null) {
				command.add("--script-path");
				command.add(scriptPath + "");
			}

			// IMPORTANT: output must be consumed to avoid hanging
			server = UnixCommandUtil.launchAndConsume(command);
			Thread.sleep(waitTime);

		} catch (IOException e) {
			throw new ServerException("Can not start server! " + e.getMessage());

		} catch (InterruptedException e) {
			throw new ServerException(e.getMessage());
		}
		serverProcess = server;
		return server;
	}

	public boolean stopServer()
	{
		if (serverProcess == null) {
			return false;
		}

		// we have started the server, so we also stop it
		try {
			int pid = UnixCommandUtil.getPID(serverProcess);
			if (pid > 0) {
				UnixCommandUtil.killProcessConditional("" + pid, "rcssserver3d");
			}
			return true;

		} catch (Throwable e) {
			System.err.println("Could not stop server: " + e);
			e.printStackTrace();
		}
		serverProcess.destroy();
		serverProcess = null;
		return false;
	}

	public void killAllServer()
	{
		UnixCommandUtil.killAll("rcssserver3d");
	}

	public int getMonitorPort()
	{
		return monitorPort;
	}

	public int getAgentPort()
	{
		return agentPort;
	}

	public int getId()
	{
		return id;
	}

	public boolean isServerProcessAlive()
	{
		return serverProcess != null && serverProcess.isAlive();
	}

	public Process getServerProcess()
	{
		return serverProcess;
	}
}
