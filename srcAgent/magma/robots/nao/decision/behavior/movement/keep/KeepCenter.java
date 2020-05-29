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
import magma.agent.decision.behavior.movement.MovementBehavior;
import magma.agent.decision.behavior.movement.MovementPhase;
import magma.robots.nao.INaoJoints;

public class KeepCenter extends MovementBehavior implements IKeepBehavior, INaoJoints
{
	public KeepCenter(IThoughtModel thoughtModel)
	{
		super(IBehaviorConstants.KEEP_CENTER, thoughtModel, createMovement());
	}

	private static Movement createMovement()
	{
		Movement keep = new Movement("keepCenter");

		keep.add(new MovementPhase("phase1", 200)
						 .add(RKneePitch, 0, 7.03f)
						 .add(LKneePitch, 0, 7.03f)

						 .add(RArmYaw, 0, 7.03f)
						 .add(LArmYaw, 0, 7.03f)

						 .add(RShoulderPitch, 0, 7.03f)
						 .add(LShoulderPitch, 0, 7.03f)

						 .add(RShoulderYaw, -50, 7.03f)
						 .add(LShoulderYaw, 50, 7.03f)

						 .add(RHipPitch, 75, 7.03f)
						 .add(LHipPitch, 75, 7.03f)

						 .add(RFootPitch, -45, 7.03f)
						 .add(LFootPitch, -45, 7.03f)

						 .add(RHipYawPitch, -90, 7.03f)
						 .add(LHipYawPitch, -90, 7.03f));
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
