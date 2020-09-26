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
package magma.robots.naoGazebo.decision.behavior.ikMovement.walk;

import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase;

public class IKWalkMovementParametersNaoGazebo extends IKWalkMovementParametersBase
{
	@Override
	public void setValues(String name)
	{
		super.setValues(name);

		put(Param.DYNAMIC_WALK, 0);
		put(Param.CYCLE_PER_STEP, 13);
		put(Param.SWING_ARMS, 0);
		put(Param.WALK_WIDTH, 0.045f);
		put(Param.PUSHDOWN_FACTOR, 0);
		put(Param.SAGGITAL_ADJUSTMENT_FACTOR, 0f);
		put(Param.CORONAL_ADJUSTMENT_FACTOR, 0f);
	}
}
