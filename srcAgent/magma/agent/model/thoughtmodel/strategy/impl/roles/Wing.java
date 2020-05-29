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

import java.util.Optional;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import hso.autonomy.util.geometry.Pose2D;
import magma.agent.model.worldmodel.IRoboCupWorldModel;

/**
 * @author Stephan Kammerer
 */
public class Wing extends Role implements IWaitForOpponentGoalKick
{
	// how far in front of the ball
	private final float xOffset;

	public Wing(String name, IRoboCupWorldModel worldModel, float xOffset, float priority)
	{
		super(worldModel, name, priority,			 //
				-1.5,								 // minX
				worldModel.fieldHalfLength() - 2.8); // maxX

		this.xOffset = xOffset;
	}

	@Override
	protected Pose2D determinePosition()
	{
		Optional<Vector3D> waitForOpponentGoalKickPosition = waitForOpponentGoalKick(worldModel, false);
		if (waitForOpponentGoalKickPosition.isPresent()) {
			return new Pose2D(waitForOpponentGoalKickPosition.get(),
					calculateBallDirection(waitForOpponentGoalKickPosition.get()));
		}

		Vector3D ballPos = worldModel.getBall().getPosition();

		// we try to be positioned relative to the ball goal distance x
		double targetX = ballPos.getX() + xOffset;
		double targetY = 0;

		double value = 0;
		double ballPosX = ballPos.getX();
		if (ballPosX < -4) {
			targetX += 2;
			value = 4;
		} else if (ballPosX < 2) {
			value = 2;
		} else {
			value = 0;
		}

		double ballPosY = ballPos.getY();

		if (ballPosY < -0.2) {
			targetY = -value;
		} else if (ballPosY > 0.2) {
			targetY = value;
		}

		targetX = keepXLimits(targetX);
		Vector3D target = new Vector3D(targetX, targetY, 0);

		return new Pose2D(target);
	}
}
