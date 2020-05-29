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

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import magma.agent.model.worldmodel.IRoboCupWorldModel;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.misc.ValueUtil;

public class DefensiveMidfielder extends Role
{
	public DefensiveMidfielder(String name, IRoboCupWorldModel worldModel, float safetyDistanceOffset, float priority)
	{
		super(worldModel, name, priority, -worldModel.fieldHalfLength(), 0, safetyDistanceOffset);
	}

	@Override
	protected IPose2D determinePosition()
	{
		Vector3D ballPos = worldModel.getBall().getPosition();
		Vector3D ownGoalPos = worldModel.getOwnGoalPosition();

		double targetX = (ownGoalPos.getX() + ballPos.getX()) / 2;
		double targetY = ballPos.getY() - 1;
		targetX = keepXLimits(targetX);
		targetY = ValueUtil.limitAbs(targetY, worldModel.fieldHalfWidth() - 6);

		Vector3D target = new Vector3D(targetX, targetY, 0);
		Angle ballDirection = calculateBallDirection(target);

		return new Pose2D(target, ballDirection);
	}

	@Override
	protected IPose2D avoidGoal(IPose2D target)
	{
		return keepMinDistanceToGoal(target, 4);
	}
}
