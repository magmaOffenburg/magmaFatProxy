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

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import magma.agent.decision.behavior.ikMovement.walk.IKStaticWalkMovement;
import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase;
import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase.Param;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;

public class IKSidewardsMovementDemo extends IKStaticWalkMovement
{
	public IKSidewardsMovementDemo(IRoboCupThoughtModel thoughtModel, IKWalkMovementParametersBase params)
	{
		super("SidewardsDemo", thoughtModel, params);

		setMovementCycles(10);

		params.put(Param.WALK_WIDTH, 0.065f);
		params.put(Param.WALK_HEIGHT, -0.255f);
		params.put(Param.WALK_OFFSET, 0.035f);
		params.put(Param.PUSHDOWN_FACTOR, 0.5f);
		params.put(Param.FOOT_SLANT_ANGLE, 0);

		currentStep.xTargetDistance = 0.1;
		currentStep.zTargetDistance = 0.02;
	}

	@Override
	public Vector3D getIntendedLeaningVector()
	{
		return Vector3D.PLUS_K;
	}
}
