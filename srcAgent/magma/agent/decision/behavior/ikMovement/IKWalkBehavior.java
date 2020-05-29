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

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.model.agentmodel.IBodyModel;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.VectorUtils;
import hso.autonomy.util.misc.ValueUtil;
import kdo.util.parameter.ParameterMap;
import magma.agent.IHumanoidJoints;
import magma.agent.decision.behavior.IBaseWalk;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.SupportFoot;
import magma.agent.decision.behavior.ikMovement.walk.IKDynamicWalkMovement;
import magma.agent.decision.behavior.ikMovement.walk.IKStaticWalkMovement;
import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;

/**
 * @author Stefan Glaser
 */
public class IKWalkBehavior extends IKMovementBehaviorBase implements IBaseWalk
{
	protected Vector3D intendedWalk = Vector3D.ZERO;

	/** how much we intend to turn in degrees*/
	protected float intendedTurn;

	/** how much we currently actually turn in degrees*/
	protected float currentTurn;

	/** The walk movement */
	protected IKStaticWalkMovement walkMovement;

	/** The intended step parameter */
	protected StepParameters intendedStep;

	private boolean useArms;

	private Vector2D currentWalkVector = Vector2D.ZERO;

	private Vector2D intendedWalkVector = Vector2D.ZERO;

	protected String paramSetName;

	private Vector2D realSpeed;

	public IKWalkBehavior(IRoboCupThoughtModel thoughtModel, ParameterMap params)
	{
		this(IBehaviorConstants.IK_WALK, thoughtModel, params);
	}

	public IKWalkBehavior(String name, IRoboCupThoughtModel thoughtModel, ParameterMap params)
	{
		super(name, thoughtModel);
		IKWalkMovementParametersBase param = (IKWalkMovementParametersBase) params.get(name);

		walkMovement = new IKDynamicWalkMovement(thoughtModel, param);
		intendedStep = new StepParameters();
		useArms = param.getSwingArms();
		paramSetName = IKDynamicWalkMovement.NAME_STABLE;
	}

	@Override
	public void setMovement(double forwardsBackwards, double stepLeftRight, double turnLeftRight)
	{
		setMovement(forwardsBackwards, stepLeftRight, turnLeftRight, IKDynamicWalkMovement.NAME_STABLE);
	}

	@Override
	public void setMovement(double forwardsBackwards, double stepLeftRight, double turnLeftRight, String paramSetName)
	{
		this.paramSetName = paramSetName;
		intendedWalk = new Vector3D(forwardsBackwards / 100, stepLeftRight / 100, 0);
		this.intendedTurn = (float) turnLeftRight;
	}

	@Override
	public void perform()
	{
		calculateStepParameter(intendedWalk, intendedTurn, intendedStep, paramSetName);
		performWithoutCalc();
		// super.perform();
		// if (useArms) {
		// swingArms(agentModel.getFutureBodyModel());
		// }

		int cycles = currentMovement.getMovementCycles();
		realSpeed = new Vector2D(
				2 * intendedStep.yTargetDistance / cycles, 2 * 0.569274541 * -intendedStep.xTargetDistance / cycles);
		currentTurn = (float) intendedStep.turnAngle;
		getWorldModel().getThisPlayer().setIntendedGlobalSpeed(VectorUtils.to3D(realSpeed));
	}

	public void performWithoutCalc()
	{
		super.perform();
		if (useArms) {
			swingArms(getAgentModel().getFutureBodyModel());
		}
	}

	/**
	 * Calculates und updates the StepParameters based on passed parameters,
	 * speed where to go and intended turn angle
	 * @param intendedWalkVector the vector (local coordinates) where to go
	 * @param intendedTurnAngle the desired turn angle (in degrees)
	 * @param inOutStep the parameters on which to base calculations
	 * @return the new step parameters
	 */
	public StepParameters calculateStepParameter(
			Vector3D intendedWalkVector, double intendedTurnAngle, StepParameters inOutStep, String paramSetName)
	{
		IKWalkMovementParametersBase walkParams = walkMovement.getWalkParameters();

		if (intendedWalkVector.getNorm() > 1) {
			intendedWalkVector = intendedWalkVector.normalize();
		}
		this.intendedWalkVector = VectorUtils.to2D(intendedWalkVector);

		currentWalkVector =
				new Vector2D(inOutStep.yTargetDistance / getMaxYFactor(), -inOutStep.xTargetDistance / getMaxXFactor());
		if (currentWalkVector.getNorm() > 1) {
			currentWalkVector = currentWalkVector.normalize();
		}

		// KDO: comment in case of learning (find a better way to switch this off
		// during learning)
		walkParams.setValues(paramSetName);

		// Calculate next movement
		double sidewise = -intendedWalkVector.getY();
		double forward = intendedWalkVector.getX();

		double destXFactor = getMaxXFactor() * sidewise;
		double destYFactor = getMaxYFactor() * forward;

		double celeration[] = new double[6];
		celeration[0] = walkParams.getAcceleration();
		celeration[1] = walkParams.getDeceleration();
		celeration[2] = walkParams.getSideAcceleration();
		celeration[3] = walkParams.getSideDeceleration();
		celeration[4] = walkParams.getTurnAcceleration();
		celeration[5] = walkParams.getTurnDeceleration();

		double adjustment[] = new double[3];
		adjustment[0] =
				ValueUtil.getValueAdjustment(inOutStep.yTargetDistance, destYFactor, celeration[0], celeration[1]);
		adjustment[1] =
				ValueUtil.getValueAdjustment(inOutStep.xTargetDistance, destXFactor, celeration[2], celeration[3]);
		adjustment[2] =
				ValueUtil.getValueAdjustment(inOutStep.turnAngle, intendedTurnAngle, celeration[4], celeration[5]);

		// scale turn adjustment to forward adjustment
		// adjustment[2] = adjustment[2] / celeration[4] * celeration[0];
		//
		// double scale = Math.max(
		// 0,
		// 1.5 - 1.5 * Math.acos(worldModel.getThisPlayer().getOrientation()
		// .getMatrix()[2][2]));
		//
		// // limit the abs sum of accelerations to be at most forward
		// acceleration
		// double adjustmentFactor = ValueUtil.getScale(adjustment, celeration[0]
		// * scale);

		inOutStep.xTargetDistance += adjustment[1];
		inOutStep.yTargetDistance += adjustment[0];
		inOutStep.turnAngle += adjustment[2];

		// calculate how high the step should be performed
		inOutStep.zTargetDistance = calculateStepHeight(inOutStep);

		// if (currentMovement == null || currentMovement.isFinished()) {
		//		System.out.printf("%4.2f; %4.2f; %4.2f; %4.2f; %4.2f; %4.2f\n", intendedWalkVector.getX(),
		//				intendedWalkVector.getY(), intendedTurnAngle, inOutStep.yTargetDistance * getMaxYFactor() * 100,
		//				inOutStep.xTargetDistance * getMaxXFactor() * 100, inOutStep.turnAngle);
		// }

		return inOutStep;
	}

	/**
	 * Calculates how high the step should be performed
	 * @param inOutStep the step parameters to change
	 */
	protected double calculateStepHeight(StepParameters inOutStep)
	{
		return getMaxStepHeight();
	}

	protected void swingArms(IBodyModel bodyModel)
	{
		// Arms
		IRoboCupAgentModel agentModel = getAgentModel();
		if (walkMovement.getSupportFoot() == SupportFoot.RIGHT) {
			agentModel.getWriteableHJ(IHumanoidJoints.RShoulderPitch).performAxisPosition(-60, 4);
			agentModel.getWriteableHJ(IHumanoidJoints.RShoulderYaw).performAxisPosition(-20, 4);
			agentModel.getWriteableHJ(IHumanoidJoints.RArmRoll).performAxisPosition(25, 4);
			agentModel.getWriteableHJ(IHumanoidJoints.RArmYaw).performAxisPosition(90, 4);
			agentModel.getWriteableHJ(IHumanoidJoints.LShoulderPitch).performAxisPosition(-120, 4);
			agentModel.getWriteableHJ(IHumanoidJoints.LShoulderYaw).performAxisPosition(15, 4);
			agentModel.getWriteableHJ(IHumanoidJoints.LArmRoll).performAxisPosition(-68, 4);
			agentModel.getWriteableHJ(IHumanoidJoints.LArmYaw).performAxisPosition(-51, 4);
		} else {
			agentModel.getWriteableHJ(IHumanoidJoints.RShoulderPitch).performAxisPosition(-120, 4);
			agentModel.getWriteableHJ(IHumanoidJoints.RShoulderYaw).performAxisPosition(-15, 4);
			agentModel.getWriteableHJ(IHumanoidJoints.RArmRoll).performAxisPosition(68, 4);
			agentModel.getWriteableHJ(IHumanoidJoints.RArmYaw).performAxisPosition(51, 4);
			agentModel.getWriteableHJ(IHumanoidJoints.LShoulderPitch).performAxisPosition(-60, 4);
			agentModel.getWriteableHJ(IHumanoidJoints.LShoulderYaw).performAxisPosition(20, 4);
			agentModel.getWriteableHJ(IHumanoidJoints.LArmRoll).performAxisPosition(-25, 4);
			agentModel.getWriteableHJ(IHumanoidJoints.LArmYaw).performAxisPosition(-90, 4);
		}
	}

	protected void adjustIntendedStepParameter()
	{
		Vector3D currentLeaning = getWorldModel().getThisPlayer().getOrientation().applyTo(Vector3D.PLUS_K);

		if (currentLeaning.getZ() < 0.7) {
			// Clear actual movement if current leaning is too high (around 45
			// degrees)
			intendedStep.xTargetDistance = 0;
			intendedStep.yTargetDistance = 0;
			intendedStep.turnAngle = 0;

		} else if (currentLeaning.getZ() < 0.9) {
			// Decelerate actual movement if current leaning is too high (around
			// 25 degrees)
			if (currentLeaning.getY() > 0.1) {
				// no backward if leaning forward
				intendedStep.yTargetDistance = Math.max(0, intendedStep.yTargetDistance);
			} else if (currentLeaning.getY() < -0.1) {
				// no forward if leaning backward
				intendedStep.yTargetDistance = Math.min(0, intendedStep.yTargetDistance);
			}
		}
	}

	@Override
	protected IIKMovement decideNextMovement()
	{
		adjustIntendedStepParameter();
		walkMovement.setNextStep(intendedStep);

		return walkMovement;
	}

	@Override
	public void init()
	{
		super.init();
		intendedStep.clear();
		currentMovement = null;
	}

	public double getMaxYFactor()
	{
		return walkMovement.getWalkParameters().getMaxStepLength();
	}

	public double getMaxXFactor()
	{
		return walkMovement.getWalkParameters().getMaxStepWidth();
	}

	public double getMaxStepHeight()
	{
		return walkMovement.getWalkParameters().getMaxStepHeight();
	}

	/**
	 * Calculates the 2D pose of the free foot resulting from the application of
	 * the given step parameter, if the robot stands on the given support foot
	 * and the support foot is the the specified pose.
	 *
	 * @param supportFootPose the global pose of the support foot
	 * @param supportFoot the support foot of the step
	 * @param step the planning step parameter
	 * @return the resulting global pose of the free foot
	 */
	public Pose2D calculateFreeFootPose(Pose2D supportFootPose, SupportFoot supportFoot, StepParameters step)
	{
		Pose2D relativeFreeFootPose = walkMovement.calculateRelativeFreeFootPose(step, supportFoot);

		return supportFootPose.applyTo(relativeFreeFootPose);
	}

	@Override
	public boolean isFinished()
	{
		return currentMovement == null || currentMovement.isFinished();
	}

	@Override
	public IBehavior switchFrom(IBehavior actualBehavior)
	{
		IBehavior realBehavior = actualBehavior.getRootBehavior();

		// Switching case to ourself
		if (realBehavior == this) {
			return this;
		}

		if (realBehavior instanceof IKKickBehavior) {
			if (realBehavior.isFinished()) {
				walkMovement.setSupportFoot(((IKKickBehavior) realBehavior).getCurrentMovement().getNextSupportFoot());
				actualBehavior.onLeavingBehavior(this);
				return this;
			} else {
				return actualBehavior;
			}
		}

		return super.switchFrom(actualBehavior);
	}

	public void setSupportFoot(SupportFoot supportFoot)
	{
		walkMovement.setSupportFoot(supportFoot);
	}

	public void setParameter(IKWalkMovementParametersBase.Param which, float value)
	{
		walkMovement.getWalkParameters().put(which, value);
	}

	public StepParameters getStepParameterCopy()
	{
		return new StepParameters().from(intendedStep);
	}

	@Override
	public double getMaxTurnAngle()
	{
		return walkMovement.getWalkParameters().getMaxTurnAngle();
	}

	@Override
	public Vector2D getIntendedWalk()
	{
		return intendedWalkVector;
	}

	@Override
	public Vector2D getCurrentSpeed()
	{
		return realSpeed;
	}

	@Override
	public float getIntendedTurn()
	{
		return intendedTurn;
	}

	@Override
	public float getCurrentTurn()
	{
		return currentTurn;
	}

	@Override
	public boolean isNewStep()
	{
		return newMovementStarted;
	}
}
