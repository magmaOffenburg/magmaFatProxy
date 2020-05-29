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
package magma.util.logging;

import java.io.Serializable;

public class BehaviorData implements Serializable
{
	public static final long serialVersionUID = 4295507372474569249L;

	private final String name;

	private final double performs;

	private final double performedPercentage;

	public BehaviorData(String name, int performs, double performedPercentage)
	{
		this.name = name;
		this.performs = performs;
		this.performedPercentage = performedPercentage;
	}

	public String getName()
	{
		return name;
	}

	public double getPerforms()
	{
		return performs;
	}

	public double getPerformedPercentage()
	{
		return performedPercentage;
	}

	@Override
	public String toString()
	{
		return name + " " + performs + " " + performedPercentage + "%";
	}
}
