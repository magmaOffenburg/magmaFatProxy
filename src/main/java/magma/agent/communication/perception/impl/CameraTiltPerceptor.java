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
package magma.agent.communication.perception.impl;

import hso.autonomy.agent.communication.perception.impl.Perceptor;
import magma.agent.communication.perception.ICameraTiltPerceptor;

/**
 * @author Stefan Glaser
 */
public class CameraTiltPerceptor extends Perceptor implements ICameraTiltPerceptor
{
	private String tiltIndicator;

	/**
	 * Assignment constructor
	 *
	 * @param tiltIndicator - the camera tilt indicator
	 */
	public CameraTiltPerceptor(String tiltIndicator)
	{
		super("cameraTilt");
		this.tiltIndicator = tiltIndicator;
	}

	@Override
	public String getCameraTilt()
	{
		return tiltIndicator;
	}
}