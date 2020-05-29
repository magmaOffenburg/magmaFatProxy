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
package magma.agent.decision.evaluator.impl;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.decision.decisionmaker.IDecisionMaker;
import hso.autonomy.agent.model.worldmodel.IMoveableObject;
import hso.autonomy.util.geometry.Geometry;
import magma.agent.decision.behavior.IKick;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.agent.model.worldmodel.IThisPlayer;

public class KickEvaluator extends DecisionEvaluator
{
	enum DecisionState
	{
		NO_KICK,
		DECIDED_FOR_KICK,
		WAIT_FOR_KICK_END
	}

	private static boolean isFirstTime = true;

	private DecisionState state;
	private int cyclesLeft;
	private Vector3D maxBallSpeed;

	// decision state
	private Vector3D localBallPosition;
	private String behaviorName;
	private Vector2D localBallSpeed;
	private double[] upVector;
	private Vector3D playerSpeed;

	public KickEvaluator(IDecisionMaker decisionMaker, IRoboCupThoughtModel thoughtModel)
	{
		super(decisionMaker, thoughtModel);
		state = DecisionState.NO_KICK;
	}

	@Override
	public void evaluate()
	{
		IBehavior rootBehavior = decisionMaker.getCurrentBehavior().getRootBehavior();
		switch (state) {
		case NO_KICK:
			if (rootBehavior instanceof IKick) {
				cyclesLeft = ((IKick) rootBehavior).getBallHitCycles() + 100;
				storeDecisionState(rootBehavior);
				state = DecisionState.DECIDED_FOR_KICK;
			}
			break;

		case DECIDED_FOR_KICK:
			cyclesLeft--;
			if (cyclesLeft == 0) {
				printResult();
				state = DecisionState.WAIT_FOR_KICK_END;
			} else {
				waitForKickResult();
			}
			break;

		case WAIT_FOR_KICK_END:
			if (!(rootBehavior instanceof IKick)) {
				state = DecisionState.NO_KICK;
			}
			break;
		}
	}

	/**
	 * Called to remember state information to be printed in case of event
	 */
	private void storeDecisionState(IBehavior rootBehavior)
	{
		IRoboCupWorldModel worldModel = thoughtModel.getWorldModel();
		IThisPlayer thisPlayer = worldModel.getThisPlayer();
		IMoveableObject ball = worldModel.getBall();
		behaviorName = rootBehavior.getName();
		localBallPosition = thisPlayer.calculateLocalPosition(ball.getPosition());
		localBallSpeed = Geometry.getLocalHorizontalSpeed(thisPlayer.getGlobalOrientation(), ball.getSpeed());
		upVector = thisPlayer.getOrientation().getMatrix()[2];
		playerSpeed = thisPlayer.getSpeed();

		maxBallSpeed = Vector3D.ZERO;
	}

	/**
	 * Called each cycle during which we wait for the kick result
	 */
	private void waitForKickResult()
	{
		IRoboCupWorldModel worldModel = thoughtModel.getWorldModel();
		Vector3D ballSpeed = worldModel.getBall().getSpeed();
		double speed = ballSpeed.getNorm();
		if (speed > maxBallSpeed.getNorm()) {
			if (speed < 0.2) {
				// plausible new highest speed
				maxBallSpeed = ballSpeed;
			}
		}
	}

	/**
	 * Called when the result of a kick should be finalized
	 */
	private void printResult()
	{
		StringBuffer buffer = new StringBuffer(200);

		// heading
		if (isFirstTime) {
			System.out.println(
					"evaluator;kickName;type;localBallPositionX;localBallPositionY;localBallSpeedX;localBallSpeedY;"
					+ "upVectorX;upVectorY;upVectorZ;playerSpeedX;playerSpeedY;evaluation");
			isFirstTime = false;
		}

		// the state
		buffer.append(this.getClass().getSimpleName());
		buffer.append(";" + behaviorName);
		buffer.append(";" + thoughtModel.getAgentModel().getModelName());
		buffer.append(String.format(";%4.2f;%4.2f", localBallPosition.getX(), localBallPosition.getY()));
		buffer.append(String.format(";%4.3f;%4.3f", localBallSpeed.getX(), localBallSpeed.getY()));
		buffer.append(String.format(";%4.2f;%4.2f;%4.2f", upVector[0], upVector[1], upVector[2]));
		buffer.append(String.format(";%4.3f;%4.3f", playerSpeed.getX(), playerSpeed.getY()));

		// the result
		buffer.append(String.format(";%4.3f", maxBallSpeed.getNorm()));

		System.out.println(buffer.toString());
	}
}
