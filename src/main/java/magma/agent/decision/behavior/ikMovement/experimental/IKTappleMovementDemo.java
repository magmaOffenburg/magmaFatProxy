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

import magma.agent.decision.behavior.ikMovement.walk.IKStaticWalkMovement;
import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase;
import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase.Param;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class IKTappleMovementDemo extends IKStaticWalkMovement
{
	public IKTappleMovementDemo(IRoboCupThoughtModel thoughtModel, IKWalkMovementParametersBase params)
	{
		super("TappleDemo", thoughtModel, params);

		setMovementCycles(10);

		params.put(Param.WALK_WIDTH, 0.065f);
		params.put(Param.WALK_HEIGHT, -0.255f);
		params.put(Param.WALK_OFFSET, 0.035f);
		params.put(Param.PUSHDOWN_FACTOR, 0.5f);
		params.put(Param.FOOT_SLANT_ANGLE, 5);

		currentStep.zTargetDistance = 0.02;
	}

	@Override
	public Vector3D getIntendedLeaningVector()
	{
		return new Rotation(Vector3D.PLUS_I, Math.toRadians(-5), RotationConvention.VECTOR_OPERATOR)
				.applyTo(Vector3D.PLUS_K);
	}
}
