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
package magma.robots.nao3.decision.behavior.ikMovement.walk;

import magma.agent.decision.behavior.ikMovement.walk.IKDynamicWalkMovement;
import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase;

/**
 * Represents the parameter set necessary for walking with walk movements
 *
 * @author Stefan Glaser
 */
public class IKWalkMovementParametersNao3 extends IKWalkMovementParametersBase
{
	public IKWalkMovementParametersNao3()
	{
		super(IKDynamicWalkMovement.NAME);
	}

	@Override
	public void setValues(String name)
	{
		super.setValues(name);

		if (IKDynamicWalkMovement.NAME.equals(name)) {
			// Dynamic walk parameter set
			put(Param.WALK_HEIGHT, -0.275f);
			put(Param.MAX_STEP_LENGTH, 0.09f);
			put(Param.MAX_STEP_WIDTH, 0.08f);

		} else {
			// Static walk parameter set (default)
			put(Param.WALK_HEIGHT, -0.275f);
			put(Param.MAX_STEP_LENGTH, 0.09f);
			put(Param.MAX_STEP_WIDTH, 0.1f);
		}
	}
}
