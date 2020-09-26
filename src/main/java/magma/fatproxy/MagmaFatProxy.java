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

import magma.fatproxy.impl.SimsparkAgentFatProxyServer;
import magma.tools.proxy.MagmaProxy;
import magma.tools.proxy.impl.SimsparkAgentProxyServer;
import magma.tools.proxy.impl.SimsparkAgentProxyServer.SimsparkAgentProxyServerParameter;

/**
 * Proxy for RoboCup games.
 * @author Stefan Glaser
 */
public class MagmaFatProxy
{
	private static final String PROXY_VERSION = "1.0.0 ";

	private SimsparkAgentProxyServer proxy;

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
	 *        </table>
	 */
	public static void main(String[] args)
	{
		SimsparkAgentProxyServerParameter parameterObject = MagmaProxy.parseParameters(args);
		SimsparkAgentProxyServer proxy = new SimsparkAgentFatProxyServer(parameterObject);
		new MagmaProxy(proxy).run();
	}
}
