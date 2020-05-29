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
package magma.agent.decision.behavior.movement;

import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import magma.agent.decision.behavior.basic.RoboCupBehavior;

/**
 * @author dorer
 */
public abstract class MovementBehavior extends RoboCupBehavior
{
	protected Movement initialMovement;

	/** true if this behavior is over */
	protected boolean isFinished;

	/** true if this behavior is performed the first time */
	protected boolean justStarted;

	/** the current movement of this behavior */
	protected Movement currentMovement;

	public MovementBehavior(String name, IThoughtModel thoughtModel, Movement initialMovement)
	{
		super(name, thoughtModel);
		this.initialMovement = initialMovement;

		isFinished = false;
		justStarted = true;
		currentMovement = initialMovement;
	}

	protected void switchTo(Movement newMovement)
	{
		currentMovement = newMovement;
		currentMovement.init();
	}

	@Override
	public void perform()
	{
		super.perform();

		isFinished = false;
		justStarted = false;
		currentMovement.perform(getAgentModel());
		if (currentMovement.isFinished()) {
			Movement nextMovement = getNextMovement();
			if (nextMovement == null) {
				isFinished = true;
				nextMovement = initialMovement;
			}
			switchTo(nextMovement);
		}
	}

	protected Movement getNextMovement()
	{
		return null;
	}

	@Override
	public boolean isFinished()
	{
		return isFinished;
	}

	public boolean justStarted()
	{
		return justStarted;
	}

	@Override
	public void init()
	{
		super.init();
		isFinished = false;
		justStarted = true;
		switchTo(initialMovement);
	}

	public Movement getInitialMovement()
	{
		return initialMovement;
	}
}
