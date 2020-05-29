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
import magma.agent.communication.perception.IHearPerceptor;

/**
 * Hear Perceptor
 *
 * @author Ingo Schindler
 * @author Simon Raffeiner
 *
 */
class HearPerceptor extends Perceptor implements IHearPerceptor
{
	// Time the message was heard
	private float time;

	// Target the message is directed to
	private String target;

	// The actual message
	private String message;

	private String team;

	/**
	 * Default constructor, nothing heard
	 */
	public HearPerceptor()
	{
		this(0.00f, "", "", "");
	}

	public HearPerceptor(float time, String team, String target, String message)
	{
		super("hear" + time + target);

		this.time = time;
		this.team = team;
		this.target = target;
		this.message = message;
	}

	@Override
	public String getTeam()
	{
		return this.team;
	}

	@Override
	public float getTime()
	{
		return this.time;
	}

	@Override
	public String getTarget()
	{
		return this.target;
	}

	@Override
	public String getMessage()
	{
		return this.message;
	}

	/**
	 * Sets back message to be empty
	 */
	public void init()
	{
		message = "";
	}
}
