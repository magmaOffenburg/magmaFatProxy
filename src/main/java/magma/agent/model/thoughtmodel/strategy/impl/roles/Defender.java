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

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.misc.ValueUtil;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * This role primarily protects our goal. We try to be positioned relative to
 * the ball but our defensive player stays in the back.
 *
 * @author Stephan Kammerer
 */
public class Defender extends Role
{
	public Defender(String name, IRoboCupWorldModel worldModel, float priority)
	{
		super(worldModel, name, priority,			//
				-worldModel.fieldHalfLength(),		// minX
				-worldModel.fieldHalfLength() + 5); // maxX
	}

	@Override
	protected IPose2D determinePosition()
	{
		Vector3D ownGoalPos = worldModel.getOwnGoalPosition();
		Vector3D ballPos = worldModel.getBall().getPosition();

		double deltaBallGoalX = Math.abs(ownGoalPos.getX() - ballPos.getX());

		double targetY = ballPos.getY() / 3;
		targetY = ValueUtil.limitAbs(targetY, 3.4);

		double targetX = keepXLimits(minX + 0.3 * deltaBallGoalX);

		Vector3D target = new Vector3D(targetX, targetY, 0);
		Angle ballDirection = calculateBallDirection(target);
		return new Pose2D(target, ballDirection);
	}

	@Override
	protected IPose2D avoidGoal(IPose2D target)
	{
		return keepMinDistanceToGoal(target, 1.9);
	}
}
