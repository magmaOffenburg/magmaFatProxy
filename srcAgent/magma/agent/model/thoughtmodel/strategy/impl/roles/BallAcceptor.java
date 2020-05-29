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

import magma.agent.model.worldmodel.IRoboCupWorldModel;

import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.misc.ValueUtil;

/**
 * @author Stephan Kammerer and Luisa Andre
 */
public class BallAcceptor extends Role implements IWaitForOpponentGoalKick
{
	private final float xOffset;
	private final float yOffset;
	private final float yLimit;

	private static final int BALL_PREDICTION_TIME = 100;

	public BallAcceptor(
			String name, IRoboCupWorldModel worldModel, float xOffset, float yOffset, float yLimit, float priority)
	{
		super(worldModel, name, priority,				   //
				(-worldModel.fieldHalfLength() + 1.8) / 2, // minX
				worldModel.fieldHalfLength() - 5);		   // maxX
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.yLimit = yLimit;
	}

	public BallAcceptor(String name, IRoboCupWorldModel worldModel, float xOffset, float yLimit, float priority)
	{
		super(worldModel, name, priority,				   //
				(-worldModel.fieldHalfLength() + 1.8) / 2, // minX
				worldModel.fieldHalfLength() - 4);		   // maxX
		this.xOffset = xOffset;
		this.yOffset = 0;
		this.yLimit = yLimit;
	}

	@Override
	protected IPose2D determinePosition()
	{
		Optional<Vector3D> opponentGoalKickPosition = waitForOpponentGoalKick(worldModel, true);
		if (opponentGoalKickPosition.isPresent()) {
			return new Pose2D(opponentGoalKickPosition.get(), calculateBallDirection(opponentGoalKickPosition.get()));
		}

		Vector3D ballPos = worldModel.getBall().getPosition();
		Vector3D futureBallPos = worldModel.getBall().getFuturePosition(BALL_PREDICTION_TIME);

		// Stay xm in front of the ball
		double ballX = ballPos.getX();
		double ballY = ballPos.getY();
		double offsetX = Math.max(xOffset, Math.abs(ballX - futureBallPos.getX()));
		double offsetY = calculateYOffset(futureBallPos, ballY);

		double targetX = keepXLimits(ballX + offsetX);
		double targetY = futureBallPos.getY() + offsetY;
		double limitY = this.yLimit;

		// move to the side in our defense
		if (ballX < -9.6) {
			// we are in deep defense
			double newX = ballX + offsetX - 1;
			double newY = worldModel.fieldHalfWidth() - 4.2;
			limitY = 10;

			if (newX > targetX) {
				newX = targetX;
			}

			if (ballY < -0.2) {
				// kick towards the right side
				targetX = newX;
				targetY = -newY;
			} else if (ballY > 0.2) {
				// kick towards the left side
				targetX = newX;
				targetY = newY;
			}
		}

		// Don't be in our way if we want to kick
		if (targetX > worldModel.fieldHalfLength() - 6) {
			limitY = 5;
			if ("BallAcceptorTwo".equals(this.name)) {
				if (ballY > 0) {
					targetY -= 2;
				} else {
					targetY += 2;
				}
			} else {
				if (ballY > 0) {
					targetY += 1.3;
				} else {
					targetY -= 1.3;
				}
			}
		}

		// Indicates how far our agent moves in y-direction
		targetY = ValueUtil.limitAbs(targetY, limitY);
		Vector3D target = new Vector3D(targetX, targetY, 0);

		return new Pose2D(target);
	}

	private double calculateYOffset(Vector3D futureBallPos, double ballY)
	{
		double offsetY;
		if (ballY > 0) {
			offsetY = Math.min(-yOffset, Math.abs(ballY - futureBallPos.getY()));
		} else {
			offsetY = Math.max(yOffset, Math.abs(ballY - futureBallPos.getY()));
		}

		return offsetY;
	}
}
