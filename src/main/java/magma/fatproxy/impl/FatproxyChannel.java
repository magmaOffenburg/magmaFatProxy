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
import hso.autonomy.agent.communication.channel.impl.InputOutputChannel;
import hso.autonomy.util.connection.IServerConnection;
import java.util.List;
import magma.agent.communication.action.impl.ServerMessageEncoder;
import magma.agent.communication.channel.IRoboCupChannel;
import magma.agent.communication.perception.impl.ServerMessageParser;

/**
 *
 * @author kdorer
 */
public class FatproxyChannel extends InputOutputChannel implements IRoboCupChannel
{
	public FatproxyChannel(IChannelManager manager, IServerConnection connection)
	{
		// TODO: here we need a parser instance that is able to parse dash commands
		super(manager, connection, new ServerMessageParser(), new ServerMessageEncoder());
	}

	@Override
	public void init(List<String> initParams)
	{
	}
}
