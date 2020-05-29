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
package magma.agent.decision.behavior;

/**
 * @author sturmflut
 */
public interface IBehaviorConstants {
	// General Behaviors
	String STOP = "Stop";

	String STOP_INSTANTLY = "StopInstantly";

	String BEAM_TO_POSITION = "BeamToPosition";

	String BEAM_HOME = "BeamHome";

	String GET_READY = "GetReady";

	String MONITOR_KICK = "MonitorKick";

	// Say behaviors
	String SAY_POSITIONS = "SayPositions";

	String SAY_SOMETHING = "SaySomething";

	// Get Up behaviors
	String MOVE_ARM_TO_FALL_BACK = "MoveArmToFallBack";

	String GET_UP_BACK = "GetUpBack";

	String GET_UP_FRONT = "GetUpFront";

	String FALL_FORWARD = "FallForward";

	String FALL_BACKWARD = "FallBackward";

	String FALL_SIDE = "FallSide";

	// Keep behaviors
	String KEEP_CENTER = "KeepCenter";

	SidedBehaviorConstants KEEP_SIDE = new SidedBehaviorConstants("KeepSide");

	// Celebrate/despair behaviors
	String WIN = "Win";

	String LOSE = "Lose";

	// Movement behaviors
	String WALK = "Walk";

	String WALK_TO_POSITION = "WalkToPosition";

	String WALK_PATH = "WalkPath";

	String RUN_PATH = "RunPath";

	String IK_WALK = "IKWalk";

	String IK_WALK_STEP = "IKWalkStep";

	String IK_STEP_PLAN = "IKStepPlan";

	String IK_MOVEMENT = "IKMovement";

	SidedBehaviorConstants STABILIZE = new SidedBehaviorConstants("Stabilize");

	// Positioning behaviors
	String PASSIVE_POSITIONING = "PassivePositioning";

	String GOALIE_POSITIONING = "GoaliePositioning";

	// Vision control behavior
	String TURN_HEAD = "TurnHead";

	String FOCUS_BALL = "FocusBall";

	String FOCUS_BALL_GOALIE = "FocusBallGoalie";

	String POINT_TO_BALL = "PointToBall";

	String SEARCH_BALL = "SearchBall";

	class SidedBehaviorConstants
	{
		public final String LEFT;

		public final String RIGHT;

		/** The name without the "Left" / "Right" suffix */
		public final String BASE_NAME;

		public SidedBehaviorConstants(String baseName)
		{
			BASE_NAME = baseName;
			LEFT = baseName + "Left";
			RIGHT = baseName + "Right";
		}
	}

	class StabilizedKickConstants
	{
		/** The stabilization movement */
		public final SidedBehaviorConstants STABILIZE;

		/** The kick movement */
		public final SidedBehaviorConstants KICK;

		/** The full behavior (stabilization + kick) */
		public final SidedBehaviorConstants FULL;

		public StabilizedKickConstants(String suffix)
		{
			STABILIZE = new SidedBehaviorConstants("Stabilize" + suffix);
			KICK = new SidedBehaviorConstants("Kick" + suffix);
			FULL = new SidedBehaviorConstants("StabilizeAndKick" + suffix);
		}
	}
}
