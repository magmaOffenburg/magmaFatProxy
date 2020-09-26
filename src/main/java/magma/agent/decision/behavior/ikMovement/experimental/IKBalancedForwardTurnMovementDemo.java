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
package magma.agent.decision.behavior.ikMovement.experimental;

import magma.agent.decision.behavior.ikMovement.walk.IKDynamicWalkMovement;
import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase;
import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase.Param;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class IKBalancedForwardTurnMovementDemo extends IKDynamicWalkMovement
{
	public IKBalancedForwardTurnMovementDemo(IRoboCupThoughtModel thoughtModel, IKWalkMovementParametersBase params)
	{
		// super("BalancedForwardDemo", null);
		super(thoughtModel, params);

		params.put(Param.WALK_WIDTH, 0.065f);
		params.put(Param.WALK_HEIGHT, -0.255f);
		params.put(Param.WALK_OFFSET, 0f);
		params.put(Param.PUSHDOWN_FACTOR, 0.5f);
		params.put(Param.FOOT_SLANT_ANGLE, 0);

		currentStep.zTargetDistance = 0;
		currentStep.yTargetDistance = 0;

		isStatic = false;
	}

	@Override
	public boolean update()
	{
		if (currentStep.yTargetDistance < 0.06) {
			currentStep.yTargetDistance += 0.0005;
		} else {
			currentStep.turnAngle = -25;
		}

		if (currentStep.zTargetDistance < 0.02) {
			currentStep.zTargetDistance += 0.0005;
		}

		return super.update();
	}

	@Override
	public Vector3D getIntendedLeaningVector()
	{
		// return Vector3D.PLUS_K;
		return new Rotation(Vector3D.PLUS_J, Math.toRadians(3)).applyTo(Vector3D.PLUS_K);
	}
}
