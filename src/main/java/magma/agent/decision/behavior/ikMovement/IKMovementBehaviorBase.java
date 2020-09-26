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
package magma.agent.decision.behavior.ikMovement;

import hso.autonomy.agent.model.agentmodel.IBodyModel;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.util.geometry.Pose6D;
import magma.agent.IHumanoidConstants;
import magma.agent.decision.behavior.basic.RoboCupBehavior;
import magma.agent.model.agentmodel.IRoboCupAgentModel;

/**
 * @author Stefan Glaser
 */
public abstract class IKMovementBehaviorBase extends RoboCupBehavior
{
	protected IIKMovement currentMovement;

	protected transient BalancingEngine balancingEngine;

	protected boolean newMovementStarted;

	public IKMovementBehaviorBase(String name, IThoughtModel thoughtModel)
	{
		super(name, thoughtModel);
		balancingEngine = new BalancingEngine();
	}

	public IIKMovement getCurrentMovement()
	{
		return currentMovement;
	}

	@Override
	public void perform()
	{
		super.perform();

		IRoboCupAgentModel agentModel = getAgentModel();

		newMovementStarted = false;
		// if the current movement is finished, decide for the next one and
		// initialize it
		if (currentMovement == null || currentMovement.isFinished()) {
			IIKMovement previousMovement = currentMovement;
			currentMovement = decideNextMovement();
			currentMovement.init(previousMovement);
			newMovementStarted = true;
		}

		// if the current movement indicates an abort, decide for the next
		// movement, initialize and update it
		if (!currentMovement.update()) {
			IIKMovement previousMovement = currentMovement;
			currentMovement = decideNextMovement();
			currentMovement.init(previousMovement);
			currentMovement.update();
			newMovementStarted = true;
		}

		// transform foot poses via the balancing engine
		Pose6D[] adjustedPoses = BalancingEngine.adjustTargetPoses(getWorldModel().getThisPlayer().getOrientation(),
				currentMovement, currentMovement.getLeftFootPose(), currentMovement.getRightFootPose());

		// perform movement poses
		IBodyModel bodyModel = agentModel.getFutureBodyModel();
		bodyModel.moveBodyPartToPose(IHumanoidConstants.LFoot, adjustedPoses[0]);
		bodyModel.moveBodyPartToPose(IHumanoidConstants.RFoot, adjustedPoses[1]);
	}

	/**
	 * Decide for the next movement to perform.
	 *
	 * @return the next movement to perform
	 */
	protected abstract IIKMovement decideNextMovement();

	@Override
	public void abort()
	{
		super.abort();
		currentMovement = null;
	}
}