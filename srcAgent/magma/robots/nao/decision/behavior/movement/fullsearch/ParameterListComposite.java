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
package magma.robots.nao.decision.behavior.movement.fullsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kdo.util.parameter.IParameterList;
import kdo.util.parameter.Parameter;

public class ParameterListComposite implements IParameterList
{
	private List<IParameterList> list = new ArrayList<>();

	public static ParameterListComposite fromSingle(IParameterList singleParam)
	{
		ParameterListComposite result = new ParameterListComposite();
		result.add(singleParam);
		return result;
	}

	public void add(IParameterList newList)
	{
		list.add(newList);
	}

	public List<IParameterList> getList()
	{
		return list;
	}

	@Override
	public Map<String, Parameter> getParameters()
	{
		return list.get(0).getParameters();
	}

	@Override
	public float get(String key)
	{
		return 0;
	}

	@Override
	public void put(String key, float value)
	{
	}

	@Override
	public String getParamsString()
	{
		return null;
	}
}
