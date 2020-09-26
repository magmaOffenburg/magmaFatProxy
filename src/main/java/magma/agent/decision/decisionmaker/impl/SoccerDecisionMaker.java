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
package magma.agent.decision.decisionmaker.impl;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.basic.MonitorKick;
import magma.agent.decision.behavior.complex.walk.Walk;
import magma.agent.model.thoughtmodel.impl.ProxyCommand;
import magma.agent.model.worldmodel.GameState;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.agent.model.worldmodel.IThisPlayer;

/**
 * Simple decision maker for primitive field player
 *
 * @author Klaus Dorer
 */
public class SoccerDecisionMaker extends RoboCupDecisionMakerBase
{
	/**
	 * Methods that are called to determine the next behavior to be performed,
	 * sorted by priority.
	 */
	protected transient List<Supplier<String>> behaviorSuppliers = new ArrayList<>(Arrays.asList(this::performSay,
			this::beamHome, this::getUp, this::reactToGameEnd, this::performFocusBall, this::getReady,
			this::waitForGameStart, this::waitForOpponentActions, this::searchBall, this::move));

	public SoccerDecisionMaker(BehaviorMap behaviors, IThoughtModel thoughtModel)
	{
		super(behaviors, thoughtModel);
	}

	@Override
	public String decideNextBehavior()
	{
		for (Supplier<String> supplier : behaviorSuppliers) {
			String behavior = supplier.get();
			if (behavior != null) {
				// System.out.println("Desired Behavior: " + behavior + " currentBehavior: " +
				// currentBehavior.getName());
				return behavior;
			}
		}

		// stop any running motors and do nothing
		return IBehavior.NONE;
	}

	/**
	 * Tries to keep the ball in the visible area.
	 */
	protected String performFocusBall()
	{
		IBehavior focusBall = behaviors.get(IBehaviorConstants.FOCUS_BALL);
		focusBall.perform();
		getThoughtModel().setHeadCommand(focusBall.getRootBehavior().getName());
		return null;
	}

	/**
	 * Say something.
	 */
	protected String performSay()
	{
		behaviors.get(IBehaviorConstants.SAY_POSITIONS).perform();
		return null;
	}

	/**
	 * Called to decide if we should beam us to our home position
	 * @return the behavior to execute, null if this part of decision does not
	 *         trigger a behavior currently
	 */
	protected String beamHome()
	{
		// fat proxy never should beam, client has to
		return null;
	}

	protected String reactToGameEnd()
	{
		IRoboCupWorldModel worldModel = getWorldModel();
		if (worldModel.getGameState() == GameState.GAME_OVER) {
			int ourGoals = worldModel.getGoalsWeScored();
			int opponentGoals = worldModel.getGoalsTheyScored();

			if (ourGoals > opponentGoals) {
				return IBehaviorConstants.WIN;
			} else if (opponentGoals > ourGoals) {
				return IBehaviorConstants.LOSE;
			}
		}

		return null;
	}

	/**
	 * Called to decide if we should wait for game start or not. Default
	 * implementation waits.
	 * @return the behavior to execute, null if this part of decision does not
	 *         trigger a behavior currently
	 */
	protected String waitForGameStart()
	{
		if (!getWorldModel().getGameState().isGameRunning()) {
			return IBehaviorConstants.GET_READY;
		}
		return null;
	}

	/**
	 * Called to decide if we should wait for opponent actions or not. Default
	 * implementation waits only on OpponentKickOff.
	 * @return the behavior to execute, null if this part of decision does not
	 *         trigger a behavior currently
	 */
	protected String waitForOpponentActions()
	{
		if (getWorldModel().getGameState() == GameState.OPPONENT_KICK_OFF) {
			return IBehaviorConstants.GET_READY;
		}
		return null;
	}

	/**
	 * Called to decide if we should stand up from lying on ground
	 * @return the behavior to execute, null if this part of decision does not
	 *         trigger a behavior currently
	 */
	protected String getUp()
	{
		IThisPlayer thisPlayer = getWorldModel().getThisPlayer();

		boolean tryGetUpBack = thisPlayer.isLyingOnBack();
		boolean tryGetUpFront = thisPlayer.isLyingOnFront();

		int consecutivePerformsFront = getBehavior(IBehaviorConstants.GET_UP_FRONT).getConsecutivePerforms();
		if (tryGetUpFront && consecutivePerformsFront > 0 && consecutivePerformsFront % 3 == 0) {
			// if GET_UP_FRONT isn't working, our orientation might simply be wrong
			// - just try GET_UP_BACK instead (see #112)
			tryGetUpBack = true;
			tryGetUpFront = false;
		}

		if (tryGetUpBack) {
			return IBehaviorConstants.GET_UP_BACK;
		}
		if (tryGetUpFront) {
			return IBehaviorConstants.GET_UP_FRONT;
		}

		if (thisPlayer.isLeaningToSide()) {
			if (thisPlayer.isLying()) {
				// straighten legs
				getBehavior(IBehaviorConstants.GET_READY).perform();
			}
			return IBehaviorConstants.MOVE_ARM_TO_FALL_BACK;
		}

		return null;
	}

	/**
	 * Called to decide if we should get into ready position
	 * @return the behavior to execute, null if this part of decision does not
	 *         trigger a behavior currently
	 */
	protected String getReady()
	{
		if (getThoughtModel().isInSoccerPosition()) {
			return null;
		}
		return IBehaviorConstants.GET_READY;
	}

	protected String searchBall()
	{
		if (getThoughtModel().isBallVisible()) {
			return null;
		}
		return IBehaviorConstants.SEARCH_BALL;
	}

	/**
	 * Called to decide if movement is necessary
	 * @return walk behavior if dash command was executed
	 */
	protected String move()
	{
		ProxyCommand proxyCommand = getThoughtModel().getProxyCommand();

		if (proxyCommand.getName().equals("kick")) {
			return determineKick(proxyCommand.getValues());
		}

		if (proxyCommand.getName().equals("dash")) {
			// Execute walk
			Walk walk = (Walk) behaviors.get(IBehaviorConstants.WALK);
			float[] walkParameters = proxyCommand.getValues();
			walk.walk(walkParameters[0], walkParameters[1], walkParameters[2]);
			return IBehaviorConstants.WALK;
		}

		return IBehaviorConstants.GET_READY;
	}

	private String determineKick(float[] values)
	{
		MonitorKick kick = (MonitorKick) behaviors.get(IBehaviorConstants.MONITOR_KICK);
		if (!kick.checkBallDistance()) {
			Walk walk = (Walk) behaviors.get(IBehaviorConstants.WALK);
			walk.walk(10, 0, 0);
			return IBehaviorConstants.WALK;
		}
		kick.setValues(values);
		return kick.getName();
	}
}
