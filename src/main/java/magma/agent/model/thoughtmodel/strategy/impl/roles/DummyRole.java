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
package magma.agent.model.thoughtmodel.strategy.impl.roles;

import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;

/**
 * @author Stephan Kammerer
 */
public class DummyRole extends Role
{
	public static final DummyRole INSTANCE = new DummyRole();

	public DummyRole()
	{
		super(null, "Dummy", 0.0f, -100, 100);
	}

	@Override
	protected IPose2D determinePosition()
	{
		return new Pose2D();
	}
}
