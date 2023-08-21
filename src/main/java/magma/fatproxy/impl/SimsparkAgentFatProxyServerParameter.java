package magma.fatproxy.impl;

import magma.tools.proxy.impl.SimsparkAgentProxyServer;

public record SimsparkAgentFatProxyServerParameter(
		SimsparkAgentProxyServer.SimsparkAgentProxyServerParameter proxyParameter, int ssMonitorPort)
{
}
