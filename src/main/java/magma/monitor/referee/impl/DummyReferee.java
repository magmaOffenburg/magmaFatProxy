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
package magma.monitor.referee.impl;

import magma.monitor.command.IServerCommander;
import magma.monitor.worldmodel.IMonitorWorldModel;

public class DummyReferee extends RefereeBase
{
	public DummyReferee(IMonitorWorldModel mWorldModel, IServerCommander serverCommander)
	{
		super(mWorldModel, serverCommander, null);
	}

	@Override
	public boolean decide()
	{
		// Dummy Referee does nothing
		return false;
	}
}
