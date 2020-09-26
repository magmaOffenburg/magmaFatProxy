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
package magma.robots.nao.decision.behavior.movement.fall;

import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.movement.Movement;
import magma.agent.decision.behavior.movement.MovementBehavior;
import magma.agent.decision.behavior.movement.MovementPhase;
import magma.robots.nao.INaoJoints;

/**
 * @author Klaus Dorer
 */
public class FallBack extends MovementBehavior
{
	private static Movement createMovement()
	{
		return new Movement("fallBack")
				.add(new MovementPhase("phase1", 10)
								.add(INaoJoints.LFootPitch, -60, 7f)
								.add(INaoJoints.RFootPitch, -60, 7f));
	}

	public FallBack(IThoughtModel thoughtModel)
	{
		super(IBehaviorConstants.FALL_BACKWARD, thoughtModel, createMovement());
	}
}
