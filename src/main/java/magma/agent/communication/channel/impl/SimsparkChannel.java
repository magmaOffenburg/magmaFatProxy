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
package magma.agent.communication.channel.impl;

import hso.autonomy.agent.communication.channel.IChannelManager;
import hso.autonomy.agent.communication.channel.IChannelState.ConnectionState;
import hso.autonomy.agent.communication.channel.impl.InputOutputChannel;
import hso.autonomy.util.connection.ConnectionException;
import hso.autonomy.util.connection.impl.ServerConnection;
import java.util.List;
import magma.agent.communication.action.impl.ServerMessageEncoder;
import magma.agent.communication.channel.IRoboCupChannel;
import magma.agent.communication.perception.impl.ServerMessageParser;

/**
 *
 * @author kdorer
 */
public class SimsparkChannel extends InputOutputChannel implements IRoboCupChannel
{
	/** the scene string to send as first command */
	private String scene;

	/** the init message to be sent as second message */
	private String initMessage;

	public SimsparkChannel(IChannelManager manager, ChannelParameters info)
	{
		super(manager, new ServerConnection(info.getHost(), info.getPort()), new ServerMessageParser(),
				new ServerMessageEncoder());

		initMessage = "(init (unum " + info.getPlayerNumber() + ")(teamname " + info.getTeamname() + "))";
	}

	@Override
	public void init(List<String> initParams)
	{
		this.scene = "(scene " + initParams.get(0) + ")";
	}

	@Override
	protected boolean startReceiveLoop()
	{
		Thread ourThread = new Thread(() -> {
			try {
				connection.sendMessage(scene.getBytes());
				connection.startReceiveLoop();
				state.setConnectionState(ConnectionState.NOT_CONNECTED);
			} catch (ConnectionException e) {
				state.setConnectionState(ConnectionState.DISCONNECTED);
			} catch (RuntimeException e) {
				state.setConnectionState(ConnectionState.DISCONNECTED);
				e.printStackTrace();
			}
			getManager().lostConnection(SimsparkChannel.this);
		});
		ourThread.start();
		return true;
	}

	/**
	 * Notification when the first message was received on this channel, before
	 * processing it
	 * @throws ConnectionException
	 */
	@Override
	protected void onFirstMessage(byte[] message) throws ConnectionException
	{
		// After initially sending the scene string (for choosing the
		// model), we are forced to send a further (soccer specific)
		// initialization string to the server as the reply of the very first
		// received perception-message.
		connection.sendMessage(initMessage.getBytes());
	}
}
