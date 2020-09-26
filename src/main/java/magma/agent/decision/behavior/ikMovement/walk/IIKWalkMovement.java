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
package magma.agent.decision.behavior.ikMovement.walk;

import magma.agent.decision.behavior.ikMovement.IIKMovement;
import magma.agent.decision.behavior.ikMovement.StepParameters;

public interface IIKWalkMovement extends IIKMovement {
	/**
	 * @return the parameters of the walk
	 */
	IKWalkMovementParametersBase getWalkParameters();

	/**
	 * @return the currently performed step parameters
	 */
	StepParameters getCurrentStep();

	/**
	 * Called to set the next step parameter.
	 *
	 * @param nextStep - the intended next step parameter
	 */
	void setNextStep(StepParameters nextStep);

	/**
	 * @return the current internal speed assumption of the walk model. This
	 *         method is just for temporary use, as long as we don't have a
	 *         proper speed determination. The value returned is between 0
	 *         indicating no speed and 1 for full speed.
	 */
	double getSpeed();
}
