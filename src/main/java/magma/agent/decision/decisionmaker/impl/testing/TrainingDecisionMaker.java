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
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.PoseSpeed2D;
import magma.agent.decision.behavior.IBaseWalk;
import magma.agent.decision.behavior.IBeam;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.complex.walk.WalkToPosition;
import magma.agent.decision.behavior.ikMovement.walk.IKDynamicWalkMovement;
import magma.agent.decision.decisionmaker.impl.SoccerDecisionMaker;
import magma.agent.model.worldmodel.IBall;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Decision maker used to trigger training behaviors. Feel free to change it for
 * your own convenience.
 *
 * @author Klaus Dorer
 */
public class TrainingDecisionMaker extends SoccerDecisionMaker
{
	protected boolean haveBeamed;

	// private boolean doneGetReady;

	public TrainingDecisionMaker(BehaviorMap behaviors, IThoughtModel thoughtModel)
	{
		super(behaviors, thoughtModel);
		haveBeamed = false;
	}

	@Override
	protected String beamHome()
	{
		if (!haveBeamed) {
			haveBeamed = true;
			((IBeam) behaviors.get(IBehaviorConstants.BEAM_TO_POSITION)).setPose(new Pose2D(-2f, 0.0f));
			return IBehaviorConstants.BEAM_TO_POSITION;
		}
		return null;
	}

	@Override
	protected String reactToGameEnd()
	{
		return null;
	}

	@Override
	protected String waitForGameStart()
	{
		return null;
	}

	@Override
	protected String searchBall()
	{
		return null;
	}

	@Override
	protected String getReady()
	{
		return null;
	}

	@Override
	protected String move()
	{
		IBaseWalk behavior = (IBaseWalk) behaviors.get(IBehaviorConstants.IK_WALK);
		behavior.setMovement(100, 0, 0, IKDynamicWalkMovement.NAME_STABLE);
		return IBehaviorConstants.IK_WALK;

		// return dribbleToBall();
	}

	protected String dribbleToBall()
	{
		IBall ball = getWorldModel().getBall();
		Vector3D goal = getWorldModel().getOtherGoalPosition();
		Angle dirGoal = ball.getDirectionTo(goal);
		Vector3D pos = Geometry.getPointOnLineAbsoluteEnd(goal, ball.getPosition(), -0.1);

		double maxSpeed = 30;
		if (getWorldModel().getThisPlayer().getDistanceToXY(ball) > 0.6) {
			maxSpeed = 90;
		}

		WalkToPosition walk = (WalkToPosition) behaviors.get(IBehaviorConstants.WALK_TO_POSITION);
		walk.setPosition(new PoseSpeed2D(new Pose2D(pos, dirGoal), new Vector2D(0.2, 0)), maxSpeed, true, 0.05);
		return IBehaviorConstants.WALK_TO_POSITION;
	}
}
