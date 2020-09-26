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
package magma.agent.decision.behavior.base;

import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import java.awt.geom.Line2D;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.agent.model.worldmodel.IRoboCupWorldModel.BallPassing;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class KeepEstimator
{
	private IRoboCupWorldModel worldModel;

	private IRoboCupAgentModel agentModel;

	public KeepEstimator(IThoughtModel thoughtModel)
	{
		worldModel = (IRoboCupWorldModel) thoughtModel.getWorldModel();
		agentModel = (IRoboCupAgentModel) thoughtModel.getAgentModel();
	}

	public String decideKeepBehavior()
	{
		// don't try to keep if the ball will just bounce over us - better chances if we stand still
		if (worldModel.getBall().isBouncing(worldModel.getGlobalTime())) {
			return null;
		}

		Vector3D futureBallPosition = worldModel.getBall().getFuturePosition(agentModel.getGoalPredictionTime());
		if (!isPossibleGoal(futureBallPosition)) {
			return null;
		}

		return decideKeepBehavior(futureBallPosition);
	}

	private String decideKeepBehavior(Vector3D futureBallPosition)
	{
		BallPassing ballIsPassing = worldModel.ballIsPassing(futureBallPosition);
		switch (ballIsPassing) {
		case CENTER:
		case SHORT_LEFT:
		case SHORT_RIGHT:
			return IBehaviorConstants.KEEP_CENTER;
		case FAR_LEFT:
			return IBehaviorConstants.KEEP_SIDE.LEFT;
		case FAR_RIGHT:
			return IBehaviorConstants.KEEP_SIDE.RIGHT;
		case UNREACHABLE:
		default:
			return null;
		}
	}

	public boolean isBallKeepable()
	{
		Vector3D futureBallPosition = worldModel.getBall().getFuturePosition(agentModel.getGoalPredictionTime());

		return decideKeepBehavior(futureBallPosition) != null;
	}

	public boolean isPossibleGoal()
	{
		Vector3D futureBallPosition = worldModel.getBall().getFuturePosition(agentModel.getGoalPredictionTime());
		return isPossibleGoal(futureBallPosition);
	}

	private boolean isPossibleGoal(Vector3D futureBallPosition)
	{
		Vector3D ballPosition = worldModel.getBall().getPosition();

		Line2D ballLine = new Line2D.Double(
				futureBallPosition.getX(), futureBallPosition.getY(), ballPosition.getX(), ballPosition.getY());

		double x1 = -worldModel.fieldHalfLength();
		double y1 = worldModel.goalHalfWidth();

		double x2 = -worldModel.fieldHalfLength();
		double y2 = -worldModel.goalHalfWidth();

		final double X_EXTENSION = 0.3;
		final double Y_EXTENSION = 0.4;
		Line2D goalLine = new Line2D.Double(x1 + X_EXTENSION, y1 + Y_EXTENSION, x2 + X_EXTENSION, y2 - Y_EXTENSION);

		return goalLine.intersectsLine(ballLine);
	}
}
