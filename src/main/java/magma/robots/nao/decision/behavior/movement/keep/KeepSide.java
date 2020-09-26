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
package magma.robots.nao.decision.behavior.movement.keep;

import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IKeepBehavior;
import magma.agent.decision.behavior.movement.Movement;
import magma.agent.decision.behavior.movement.MovementPhase;
import magma.agent.decision.behavior.movement.SidedMovementBehavior;
import magma.robots.nao.INaoJoints;

/**
 * @author Rajit Shahi, Ingo Schindler
 */
public class KeepSide extends SidedMovementBehavior implements IKeepBehavior, INaoJoints
{
	public KeepSide(Side side, IThoughtModel thoughtModel)
	{
		super(side, IBehaviorConstants.KEEP_SIDE.BASE_NAME, thoughtModel, createMovement());
	}

	private static Movement createMovement()
	{
		Movement keep = new Movement("keepRight");
		keep.add(new MovementPhase("phase1", 200)
						 .add(LShoulderPitch, 90, 7f)
						 .add(LShoulderYaw, 0, 7f)
						 .add(LArmYaw, 0, 7f)
						 .add(LArmRoll, 0, 7f)

						 .add(LHipPitch, 0, 7f)
						 .add(LKneePitch, 0, 7f)
						 .add(LFootPitch, 0, 7f)
						 .add(LFootRoll, -20, 7f)

						 .add(RShoulderPitch, 90, 7f)
						 .add(RShoulderYaw, 0, 7f)
						 .add(RArmYaw, 0, 7f)
						 .add(RArmRoll, 0, 7f)

						 .add(RHipPitch, 60, 7f)
						 .add(RKneePitch, -120, 7f)
						 .add(RFootPitch, 60, 7f)
						 .add(RFootRoll, -20, 7f));
		return keep;
	}

	@Override
	public void perform()
	{
		// if the ball is not moving, we stop the behavior
		if (!currentMovement.isFinished()) {
			if (!getWorldModel().getBall().isMoving() || getWorldModel().getGameState().isOwnKick()) {
				isFinished = true;
				switchTo(initialMovement);
				return;
			}
		}

		super.perform();
	}
}
