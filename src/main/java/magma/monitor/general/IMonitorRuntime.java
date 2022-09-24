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
package magma.monitor.general;

import hso.autonomy.util.connection.IServerConnection;
import magma.monitor.command.IServerCommander;
import magma.monitor.messageparser.IMonitorMessageParser;
import magma.monitor.referee.IReferee;
import magma.monitor.worldmodel.IMonitorWorldModel;

public interface IMonitorRuntime
{
	IServerConnection getServerConnection();

	IMonitorMessageParser getMessageParser();

	IServerCommander getServerCommander();

	IMonitorWorldModel getWorldModel();

	IReferee getReferee();

	boolean addRuntimeListener(IMonitorRuntimeListener listener);

	boolean removeRuntimeListener(IMonitorRuntimeListener listener);
}
