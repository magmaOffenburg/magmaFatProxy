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

import hso.autonomy.agent.communication.channel.IChannelManager;
import hso.autonomy.agent.communication.channel.IOutputChannel;
import hso.autonomy.agent.communication.channel.impl.ChannelManager;
import hso.autonomy.util.connection.IServerConnection;
import magma.agent.agentruntime.ComponentFactory;
import magma.agent.agentruntime.ComponentFactoryDecorator;
import magma.agent.communication.channel.IRoboCupChannel;
import magma.agent.communication.channel.impl.ChannelParameters;

/**
 * @author kdorer
 *
 */
public class FatproxyComponentFactoryDecorator extends ComponentFactoryDecorator
{
	private IServerConnection connection;

	public FatproxyComponentFactoryDecorator(ComponentFactory decoratee, IServerConnection connection)
	{
		super(decoratee);
		this.connection = connection;
	}

	@Override
	public IChannelManager createChannelManager(ChannelParameters info)
	{
		IChannelManager channelManager = new ChannelManager();
		IRoboCupChannel channel = new FatproxyChannel(channelManager, connection);
		channelManager.addInputChannel(channel, true);
		channelManager.addOutputChannel((IOutputChannel) channel);
		return channelManager;
	}
}
