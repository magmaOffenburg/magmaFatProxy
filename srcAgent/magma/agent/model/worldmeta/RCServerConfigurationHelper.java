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
package magma.agent.model.worldmeta;

import magma.agent.model.worldmeta.impl.RCServerMetaModelV62;
import magma.agent.model.worldmeta.impl.RCServerMetaModelV63;
import magma.agent.model.worldmeta.impl.RCServerMetaModelV64;
import magma.agent.model.worldmeta.impl.RCServerMetaModelV66;

public class RCServerConfigurationHelper
{
	public static IRoboCupWorldMetaModel getRCServerMetalModel(int serverVersion)
	{
		if (serverVersion < 62) {
			serverVersion = 62;
		}

		switch (serverVersion) {
		case 62:
			return RCServerMetaModelV62.INSTANCE;
		case 63:
			return RCServerMetaModelV63.INSTANCE;
		case 64:
		case 65:
			return RCServerMetaModelV64.INSTANCE;
		case 66:
		default:
			return RCServerMetaModelV66.INSTANCE;
		}
	}
}
