package magma.fatproxy.impl;

import magma.tools.proxy.impl.SimsparkAgentProxyServer;

public class SimsparkAgentFatProxyServerParameter extends SimsparkAgentProxyServer.SimsparkAgentProxyServerParameter
{
	private final int ssMonitorPort;

	public SimsparkAgentFatProxyServerParameter(
			int proxyPort, String ssHost, int ssPort, int ssMonitorPort, boolean showMessages, boolean daemon)
	{
		super(proxyPort, ssHost, ssPort, showMessages, daemon);
		this.ssMonitorPort = ssMonitorPort;
	}

	public int getSsMonitorPort()
	{
		return ssMonitorPort;
	}
}
