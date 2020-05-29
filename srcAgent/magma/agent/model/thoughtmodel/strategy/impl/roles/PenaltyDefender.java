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

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.VectorUtils;
import magma.agent.model.worldmodel.IRoboCupWorldModel;

/**
 * This role stays in the back and defends our goal near the penalty area.
 * It should stay not to close to not interfere with the ManToManMarker inside the panlty area.
 * @author Sebastian Eppinger
 *
 */
public class PenaltyDefender extends Role
{
	/**
	 * Offset to the right/left of ball to goal line when player inside the penalty area
	 */
	private static final float ORTHOGONAL_OFFSET = 1.2f;

	public PenaltyDefender(String name, IRoboCupWorldModel worldModel, float priority)
	{
		super(worldModel, name, priority,			   //
				-worldModel.fieldHalfLength() + 2.6f,  // minX
				-worldModel.fieldHalfLength() + 5.0f); // maxX
	}

	@Override
	protected IPose2D determinePosition()
	{
		Vector3D ballPosition = worldModel.getBall().getPosition();
		Vector3D ownGoalPosition = worldModel.getOwnGoalPosition();
		Angle goalBallAngle = VectorUtils.getDirectionTo(worldModel.getOwnGoalPosition(), ballPosition);
		double orthogonalOffset =
				Geometry.getLinearFuzzyValue(0, 60, false, Math.abs(goalBallAngle.degrees())) * ORTHOGONAL_OFFSET;

		// min x-distance from field line
		double fieldLineOffset = worldModel.fieldHalfLength() - 0.3f;

		// if ball in upper field half negate offset
		if (ballPosition.getY() > 0) {
			orthogonalOffset = -orthogonalOffset;
		}

		Vector3D position = Geometry.getPointOnOrthogonal2D(ballPosition, ownGoalPosition, 3.4, orthogonalOffset);

		// if calculated position is outside field
		if (Math.abs(position.getX()) > fieldLineOffset) {
			position = new Pose2D(-fieldLineOffset, position.getY()).getPosition3D();
		}

		Angle ballDirection = calculateBallDirection(position);
		return new Pose2D(position, ballDirection);
	}
}
