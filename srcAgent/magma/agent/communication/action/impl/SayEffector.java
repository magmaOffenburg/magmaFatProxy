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
package magma.agent.communication.action.impl;

import hso.autonomy.agent.communication.action.impl.Effector;

/**
 * Implementation of the SimSpark "say" effector, used to communicate with other
 * agents over a simulated voice communication
 *
 * @author Klaus Dorer
 */
public class SayEffector extends Effector
{
	// the message to shout out to others
	private String message;

	public SayEffector()
	{
		super("say");
		message = "";
	}

	@Override
	public void setEffectorValues(float... values)
	{
		// handled by setMessage
	}

	/**
	 * Set the message to be transmitted
	 *
	 * @param message Message string
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}

	/**
	 * Retrieve the message string to be transmitted
	 *
	 * @return Message String
	 */
	public String getMessage()
	{
		return message;
	}

	@Override
	public void resetAfterAction()
	{
		message = "";
	}
}
