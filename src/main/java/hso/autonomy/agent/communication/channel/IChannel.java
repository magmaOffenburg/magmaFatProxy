/*******************************************************************************
 * Copyright 2008 - 2020 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under MIT License (see LICENSE).
 *******************************************************************************/
package hso.autonomy.agent.communication.channel;

/**
 *
 * @author kdorer
 */
public interface IChannel
{
	boolean startChannel();

	void stopChannel();

	IChannelState getConnectionState();

	boolean isConnected();
}
