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
package magma.agent.decision.behavior.complex;

import java.util.Collections;
import java.util.List;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.decision.behavior.complex.ComplexBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;

/**
 * Convenience class for complex behaviors which decide only for a single next
 * behavior.
 *
 * @see ComplexBehavior
 * @author Stefan Glaser
 */
public abstract class RoboCupSingleComplexBehavior extends RoboCupComplexBehavior
{
	public RoboCupSingleComplexBehavior(
			String name, IThoughtModel thoughtModel, BehaviorMap behaviors, String defaultBehaviorName)
	{
		super(name, thoughtModel, behaviors, defaultBehaviorName);
	}

	public RoboCupSingleComplexBehavior(String name, IThoughtModel thoughtModel, BehaviorMap behaviors)
	{
		super(name, thoughtModel, behaviors);
	}

	@Override
	protected List<IBehavior> decideNextBasicBehaviors()
	{
		return Collections.singletonList(decideNextBasicBehavior());
	}

	/**
	 * Decide for a single next behavior.
	 */
	protected abstract IBehavior decideNextBasicBehavior();
}
