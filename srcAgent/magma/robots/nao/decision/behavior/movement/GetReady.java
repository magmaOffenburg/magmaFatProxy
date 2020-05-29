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
package magma.robots.nao.decision.behavior.movement;

import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.movement.Movement;
import magma.agent.decision.behavior.movement.MovementBehavior;
import magma.agent.decision.behavior.movement.MovementPhase;
import magma.robots.nao.INaoJoints;

public class GetReady extends MovementBehavior implements INaoJoints
{
	private static Movement createMovement()
	{
		return new Movement("getReady")
				.add(new MovementPhase("getReady", 30)
								.add(RShoulderPitch, -50, 3)
								.add(RShoulderYaw, 0, 7)
								.add(RArmRoll, 0, 7)
								.add(RArmYaw, 0, 7)
								.add(RHipYawPitch, 0, 7)
								.add(RHipRoll, 0, 7)
								.add(RHipPitch, 30, 7)
								.add(RKneePitch, -60, 7)
								.add(RFootPitch, 30, 7)
								.add(RFootRoll, 0, 7)

								.add(LShoulderPitch, -50, 3)
								.add(LShoulderYaw, 0, 7)
								.add(LArmRoll, 0, 7)
								.add(LArmYaw, 0, 7)
								.add(LHipYawPitch, 0, 7)
								.add(LHipRoll, 0, 7)
								.add(LHipPitch, 30, 7)
								.add(LKneePitch, -60, 7)
								.add(LFootPitch, 30, 7)
								.add(LFootRoll, 0, 7));
	}

	public GetReady(IThoughtModel thoughtModel)
	{
		super(IBehaviorConstants.GET_READY, thoughtModel, createMovement());
	}

	@Override
	public boolean isFinished()
	{
		return super.isFinished() || getThoughtModel().isInSoccerPosition();
	}
}
