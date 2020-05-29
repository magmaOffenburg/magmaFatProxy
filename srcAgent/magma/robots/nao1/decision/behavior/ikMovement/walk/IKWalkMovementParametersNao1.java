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
package magma.robots.nao1.decision.behavior.ikMovement.walk;

import magma.agent.decision.behavior.ikMovement.walk.IKDynamicWalkMovement;
import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase;

/**
 * Represents the parameter set necessary for walking with walk movements
 *
 * @author Stefan Glaser
 */
public class IKWalkMovementParametersNao1 extends IKWalkMovementParametersBase
{
	public IKWalkMovementParametersNao1()
	{
		super(IKDynamicWalkMovement.NAME);
	}

	@Override
	public void setValues(String name)
	{
		super.setValues(name);

		// if (IKDynamicWalkMovement.NAME.equals(name)) {
		// // Dynamic walk parameter set
		// put(Param.WALK_HEIGHT, -0.275f);
		// put(Param.MAX_STEP_LENGTH, 0.09f);
		// put(Param.MAX_STEP_WIDTH, 0.08f);
		// } else {
		// // Static walk parameter set (default)
		// put(Param.WALK_HEIGHT, -0.275f);
		// put(Param.MAX_STEP_LENGTH, 0.09f);
		// put(Param.MAX_STEP_WIDTH, 0.1f);
		// }
		put(Param.CYCLE_PER_STEP, 11.0f);
		put(Param.WALK_HEIGHT, -0.275f);
		put(Param.WALK_WIDTH, 0.055000003f);
		put(Param.WALK_OFFSET, -0.003f);
		put(Param.MAX_STEP_LENGTH, 0.095000006f);
		put(Param.MAX_STEP_WIDTH, 0.08f);
		put(Param.MAX_STEP_HEIGHT, 0.021f);
		put(Param.STEP_SWING, 0.0f);
		put(Param.MAX_TURN_ANGLE, 50.0f);
		put(Param.PUSHDOWN_FACTOR, 0.514f);
		put(Param.FOOT_SLANT_ANGLE, 0.0f);
		put(Param.MAX_FORWARD_LEANING, 0.0f);
		put(Param.MAX_SIDEWARDS_LEANING, 0.0f);
		put(Param.ACCELERATION, 0.0035f);
		put(Param.DECELERATION, 0.0035f);
		put(Param.TURN_ACCELERATION, 2.0f);
		put(Param.TURN_DECELERATION, 3.0f);
		put(Param.SWING_ARMS, 1.0f);
		put(Param.DYNAMIC_WALK, 1.0f);
		put(Param.SAGGITAL_ADJUSTMENT_FACTOR, 0.3f);
		put(Param.MAX_ABS_SAGGITAL_ADJUSTMENT, 100.0f);
		put(Param.CORONAL_ADJUSTMENT_FACTOR, 0.3f);
		put(Param.MAX_ABS_CORONAL_ADJUSTMENT, 100.0f);
	}
}
