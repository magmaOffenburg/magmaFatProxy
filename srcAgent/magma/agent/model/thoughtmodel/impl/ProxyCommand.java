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
package magma.agent.model.thoughtmodel.impl;

/**
 * A class representing the proxy command to execute
 * @author Hannes Braun
 */
public class ProxyCommand
{
	/** The behavior name */
	private String name;

	private float[] values;

	/**
	 * Constructs the proxy command object.
	 * @param name The name of the proxy command (as stored in the perceptor)
	 * @param values The matching values for the command to represent
	 */
	public ProxyCommand(String name, float[] values)
	{
		this.name = name;
		this.values = values;
	}

	public String getName()
	{
		return name;
	}

	public float[] getValues()
	{
		return values;
	}
}
