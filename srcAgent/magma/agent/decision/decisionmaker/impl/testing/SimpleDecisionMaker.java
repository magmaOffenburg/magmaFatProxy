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
package magma.agent.decision.decisionmaker.impl.testing;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.decisionmaker.impl.DecisionMakerBase;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IWalk;
import magma.agent.decision.behavior.ikMovement.IKStabilizeOnLegBehavior;

/**
 * Basic decision making class used for testing mainly
 *
 * @author Klaus Dorer
 */
public class SimpleDecisionMaker extends DecisionMakerBase
{
	public SimpleDecisionMaker(BehaviorMap behaviors, IThoughtModel thoughtModel)
	{
		super(behaviors, thoughtModel);
	}

	@Override
	public String decideNextBehavior()
	{
		if (numberOfDecisions < 50) {
			return IBehaviorConstants.GET_READY;
		} else if (numberOfDecisions < 200) {
			IWalk walk = (IWalk) behaviors.get(IBehaviorConstants.WALK);
			walk.walk(60, 0, 0);
			return walk.getName();
		} else {
			boolean left = true;
			String stabilizeName = "Stabilize" + (left ? "Left" : "Right"); // "StabilizeRight"
			int sideFactor = left ? -1 : 1;

			IKStabilizeOnLegBehavior stabilizeBehavior = (IKStabilizeOnLegBehavior) behaviors.get(stabilizeName);
			stabilizeBehavior.setFreeFootTargetPose(
					new Pose2D(sideFactor * 0.18, -0.02, Angle.deg(-1 * sideFactor * 90)));

			return stabilizeName;
		}
	}
}
