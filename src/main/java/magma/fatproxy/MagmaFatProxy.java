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
package magma.fatproxy;

import java.util.ArrayList;
import java.util.List;
import magma.fatproxy.impl.SimsparkAgentFatProxyServer;
import magma.fatproxy.impl.SimsparkAgentFatProxyServerParameter;
import magma.tools.proxy.MagmaProxy;
import magma.tools.proxy.impl.SimsparkAgentProxyServer;

/**
 * Proxy for RoboCup games.
 * @author Stefan Glaser
 */
public class MagmaFatProxy
{
	private static final String PROXY_VERSION = "1.3.1";

	/**
	 * Instantiates and starts the Simspark agent proxy.
	 *
	 * @param args Command line arguments <br>
	 *        <table>
	 *        <tr>
	 *        <td>--proxyport=</td>
	 *        <td>Proxy server port</td>
	 *        </tr>
	 *        <tr>
	 *        <td>--server=</td>
	 *        <td>Simspark server IP</td>
	 *        </tr>
	 *        <tr>
	 *        <td>--serverport=</td>
	 *        <td>Simspark server Port</td>
	 *        </tr>
	 *        <tr>
	 *        <td>--verbose</td>
	 *        <td>Shows the messages</td>
	 *        </tr>
	 *        <tr>
	 *        <td>--daemon</td>
	 *        <td>Disables the command line interface</td>
	 *        </tr>
	 *        </table>
	 */
	public static void main(String[] args)
	{
		SimsparkAgentFatProxyServerParameter parameterObject = parseParameters(args);
		SimsparkAgentProxyServer proxy = new SimsparkAgentFatProxyServer(parameterObject);
		System.out.println("Starting magmaFatProxy version " + PROXY_VERSION);
		new MagmaProxy(proxy).run(parameterObject.isDaemon());
	}

	public static SimsparkAgentFatProxyServerParameter parseParameters(String[] args)
	{
		List<String> unparsedParameters = new ArrayList<>();
		SimsparkAgentProxyServer.SimsparkAgentProxyServerParameter magmaProxyParameters =
				MagmaProxy.parseParameters(args, unparsedParameters);

		int ssMonitorPort = 3200;
		boolean printUsage = false;

		for (String arg : unparsedParameters) {
			if (arg.startsWith("--monitorport=")) {
				ssMonitorPort = Integer.parseInt(arg.replaceFirst("--monitorport=", ""));
			} else {
				System.out.println("Unknown parameter: " + arg);
				printUsage = true;
			}
		}
		if (printUsage) {
			System.out.println("Usage example: --server=127.0.0.1 --serverport=3100 --proxyport=3120");
		}

		return new SimsparkAgentFatProxyServerParameter(magmaProxyParameters.getProxyPort(),
				magmaProxyParameters.getSsHost(), magmaProxyParameters.getSsPort(), ssMonitorPort,
				magmaProxyParameters.isShowMessages(), magmaProxyParameters.isDaemon());
	}
}
