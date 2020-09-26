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
package magma.agent.decision.behavior.complex.walk;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.misc.ValueUtil;
import kdo.util.parameter.ParameterMap;
import magma.agent.decision.behavior.IBaseWalk;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IWalk;
import magma.agent.decision.behavior.complex.RoboCupSingleComplexBehavior;
import magma.agent.decision.behavior.ikMovement.walk.IKDynamicWalkMovement;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Implements a behavior which performs bi-pedal walking based on the "Balance"
 * behavior.
 *
 * @author Ingo Schindler
 */
public class Walk extends RoboCupSingleComplexBehavior implements IWalk
{
	public Walk(String name, IThoughtModel thoughtModel, ParameterMap params, BehaviorMap behaviors, IBaseWalk baseWalk)
	{
		super(name, thoughtModel, behaviors, baseWalk.getName());
	}

	public Walk(IThoughtModel thoughtModel, ParameterMap params, BehaviorMap behaviors, IBaseWalk baseWalk)
	{
		this(IBehaviorConstants.WALK, thoughtModel, params, behaviors, baseWalk);
	}

	/**
	 * Set parameters for Walk. It is possible to combine all different
	 * parameters, e.g. forwards and sidesteps.
	 *
	 * @param forwardsBackwards positive = forwards; negative = backwards (-100
	 *        .. 100)
	 * @param stepLeftRight positive = right; negative = left (-100 .. 100)
	 * @param turnLeftRight positive = right; negative = left (-30 .. 30)
	 */
	@Override
	public void walk(double forwardsBackwards, double stepLeftRight, double turnLeftRight)
	{
		walk(forwardsBackwards, stepLeftRight, turnLeftRight, IKDynamicWalkMovement.NAME_STABLE);
	}
	/**
	 * Set parameters for Walk. It is possible to combine all different
	 * parameters, e.g. forwards and sidesteps.
	 *
	 * @param forwardsBackwards positive = forwards; negative = backwards (-100
	 *        .. 100)
	 * @param stepLeftRight positive = right; negative = left (-100 .. 100)
	 * @param turnLeftRight positive = right; negative = left (-30 .. 30)
	 */
	@Override
	public void walk(double forwardsBackwards, double stepLeftRight, double turnLeftRight, String paramSetName)
	{
		// Limit turn angle by the current speed
		IBaseWalk base = (IBaseWalk) getCurrentBehavior();
		double maxTurn = base.getMaxTurnAngle() * 2;
		double turnLimit = maxTurn;
		double actualSpeed = base.getIntendedWalk().getNorm() * 100;
		turnLimit = Math.min(turnLimit, 100 - actualSpeed + 7);
		turnLimit = (actualSpeed > 60) ? 9 : turnLimit;

		// limit turn if we are hanging
		//		double up = getWorldModel().getThisPlayer().getOrientation().getMatrix()[2][2];
		//		double upLimit = Geometry.getLinearFuzzyValue(0.92, 0.99, true, up) * maxTurn;
		//		turnLimit = Math.min(turnLimit, upLimit);

		double turnAmount = ValueUtil.limitAbs(turnLeftRight, turnLimit) / 2;

		base.setMovement(forwardsBackwards, stepLeftRight, turnAmount, paramSetName);

		getThoughtModel().setIntendedWalk(stepLeftRight, forwardsBackwards, turnAmount);
	}

	@Override
	public void globalWalk(
			Pose2D walkTo, Vector3D speedThere, double slowDownDistance, double maxSpeedLimit, String paramSetName)
	{
		Vector3D fieldVector = walkTo.getPosition3D();
		Angle horizontalDirection = getWorldModel().getThisPlayer().getHorizontalAngle();

		// rotate into local coordinate system
		Vector3D playerVector = Geometry.createZRotation(-(float) horizontalDirection.radians()).applyTo(fieldVector);

		double desiredTurn = walkTo.angle.degrees();

		// pretend we have done the wanted turn
		Vector3D virtualPosition = Geometry.createZRotation((float) Math.toRadians(-desiredTurn)).applyTo(playerVector);

		// account for that we are slower sideward
		Vector2D realSpeed = new Vector2D(virtualPosition.getX(), virtualPosition.getY() * 1.5);
		double absSpeed = realSpeed.getNorm();

		if (absSpeed < 0.001) {
			// avoid vector normalization exception, assume walk forward
			realSpeed = new Vector2D(1, 0);
		} else {
			realSpeed = realSpeed.normalize();
		}
		absSpeed = 1.0;

		// limit speed if diagonal
		double reduction = Math.toDegrees(Math.abs(Math.atan2(realSpeed.getY(), realSpeed.getX())));
		if (reduction > 90) {
			reduction = 180 - reduction;
		}
		reduction = Geometry.getLinearFuzzyValue(0, 20, false, Math.abs(45 - reduction)) * 0.3;
		double maxSpeed = Math.min(1.0 - reduction, maxSpeedLimit / 100.0);

		// limit speed, if we have to turn
		double turnFactor = Geometry.getLinearFuzzyValue(10, 60, true, Math.abs(desiredTurn));
		turnFactor = 1 - turnFactor; // * turnFactor;
		maxSpeed = Math.min(maxSpeed, turnFactor);

		if (absSpeed > maxSpeed) {
			realSpeed = realSpeed.normalize().scalarMultiply(maxSpeed);
		}

		// limit speed if close to destination (separate for x and y)
		double finalSpeedX = speedThere.getX();
		finalSpeedX += Geometry.getLinearFuzzyValue(0, slowDownDistance, true, virtualPosition.getNorm());
		double finalSpeedY = speedThere.getY();
		finalSpeedY += Geometry.getLinearFuzzyValue(0, slowDownDistance, true, virtualPosition.getNorm());
		double finalX = ValueUtil.limitAbs(realSpeed.getX(), finalSpeedX);
		double finalY = ValueUtil.limitAbs(realSpeed.getY(), finalSpeedY);
		realSpeed = new Vector2D(finalX, finalY);

		// translate to percent
		realSpeed = realSpeed.scalarMultiply(100);

		walk(realSpeed.getX(), realSpeed.getY(), desiredTurn, paramSetName);
	}

	@Override
	public IBehavior decideNextBasicBehavior()
	{
		return getCurrentBehavior();
	}
}