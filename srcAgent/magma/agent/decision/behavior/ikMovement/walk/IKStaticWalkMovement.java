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

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose6D;
import magma.agent.decision.behavior.SupportFoot;
import magma.agent.decision.behavior.ikMovement.IIKMovement;
import magma.agent.decision.behavior.ikMovement.StepParameters;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;

/**
 * @author Stefan Glaser
 */
public class IKStaticWalkMovement extends IKStepMovementBase implements IIKWalkMovement
{
	/** static walk movement name and identifier for parameter set */
	public static final String NAME = "StaticWalkMovement";

	/** the current step parameter */
	protected StepParameters currentStep;

	/** the current step parameter */
	protected StepParameters previousStep;

	/** variable to toggle force dependent leg switch */
	protected boolean checkForceDependentLegSwitch = true;

	public IKStaticWalkMovement(IRoboCupThoughtModel thoughtModel, IKWalkMovementParametersBase params)
	{
		this(NAME, thoughtModel, params);
	}

	public IKStaticWalkMovement(String name, IRoboCupThoughtModel thoughtModel, IKWalkMovementParametersBase params)
	{
		super(name, thoughtModel, params);

		currentStep = new StepParameters();
		previousStep = new StepParameters();

		isStatic = true;
	}

	@Override
	public void init(IIKMovement other)
	{
		// Adjust interpolator-amplitudes to the current step
		freeFootHeightInterpolator.amplitude = currentStep.zTargetDistance;
		supportFootHeightInterpolator.amplitude = -1 * currentStep.zTargetDistance * params.getPushDownFactor();

		super.init(other);
	}

	@Override
	protected void calculateMovementTrajectory()
	{
		// target poses
		Pose6D leftFootTargetPose = calculateFootTargetPose(currentStep, SupportFoot.LEFT, supportFoot, 0.0);
		Pose6D rightFootTargetPose = calculateFootTargetPose(currentStep, SupportFoot.RIGHT, supportFoot, 0.0);

		// interpolate movement
		interpolateMovement(leftFootTargetPose, rightFootTargetPose, new Vector2D(1, 1));
	}

	/**
	 * Calculate the target pose based on the given step parameter.<br>
	 * The default implementation calculates and limits the target pose
	 * symmetrically for both legs.
	 * @param widthOffset TODO
	 *
	 * @param step: the step parameter
	 * @return the target pose
	 */
	protected Pose6D calculateFootTargetPose(
			StepParameters step, SupportFoot targetFoot, SupportFoot supportFoot, double widthOffset)
	{
		int side = targetFoot == SupportFoot.LEFT ? -1 : 1;
		int support = supportFoot == SupportFoot.LEFT ? -1 : 1;

		// Limit x-target distance, since we can't move too much to the side
		double targetX = -1 * side * support * step.xTargetDistance;
		double limit = -0.02;
		limit = -0.02;
		// EXPERIMENT: adjust step limit if turning
		// + Geometry.getLinearFuzzyValue(5, 50, true,
		// Math.abs(step.turnAngle)) * 0.04;

		if (side * targetX < limit) {
			targetX = limit * side;
		}
		double walkWidth = params.getWalkWidth() + widthOffset;
		targetX += side * walkWidth;

		double targetY = params.getWalkOffset() - side * support * step.yTargetDistance;

		// EXPERIMENT: make bigger side steps if hanging (capture steps)
		//		Rotation orientation = worldModel.getThisPlayer().getOrientation();
		//		double[][] matrix = orientation.getMatrix();
		//		double hangingSide = matrix[2][0];
		//		double adjust = 0.015;
		//		if (Math.abs(hangingSide) > 0.1) {
		//			if (supportFoot == SupportFoot.LEFT && /*targetFoot == SupportFoot.RIGHT &&*/ hangingSide > 0) {
		//				// make capture step right
		//				double adjustment = adjust * Geometry.getLinearFuzzyValue(0.1, 0.2, true, hangingSide);
		//				if (targetFoot == SupportFoot.RIGHT) {
		//					targetX += adjustment;
		//				} else {
		//					targetX -= adjustment;
		//				}
		//			} else if (supportFoot == SupportFoot.RIGHT /*&& targetFoot == SupportFoot.LEFT && hangingSide <
		//0*/)
		//{
		//				// make capture step left
		//				double adjustment = adjust * Geometry.getLinearFuzzyValue(0.1, 0.2, true, -hangingSide);
		//				if (targetFoot == SupportFoot.RIGHT) {
		//					targetX -= adjustment;
		//				} else {
		//					targetX += adjustment;
		//				}
		//			}
		//		}

		// EXPERIMENT: make bigger front steps if hanging (capture steps)
		//		double hangingFront = matrix[2][1];
		//		if (Math.abs(hangingFront) > 0.08) {
		//			// make capture step right
		//			if (supportFoot == SupportFoot.LEFT && targetFoot == SupportFoot.RIGHT && hangingFront > 0) {
		//				targetY += adjust * Geometry.getLinearFuzzyValue(0.08, 0.2, true, hangingFront);
		//			} else if (supportFoot == SupportFoot.RIGHT && targetFoot == SupportFoot.LEFT && hangingFront < 0) {
		//				targetY -= adjust * Geometry.getLinearFuzzyValue(0.08, 0.2, true, -hangingFront);
		//			}
		//		}

		// Limit z-target angle, since we can't reach negative/positive angles
		// with the left/right foot
		double targetZAngle = -1 * side * support * step.turnAngle;
		if (side * targetZAngle > 0) {
			targetZAngle = 0;
		}

		return new Pose6D(targetX, targetY, params.getWalkHeight(), //
				params.getFootSluntAngle(), 0, targetZAngle);
	}

	/**
	 * Calculates the 2D free foot pose relative to the support foot, which
	 * results from the application of the given step parameter.<br>
	 * <b>Note:</b> The resulting positions are in a system facing the x-axis
	 * just like the global field system.
	 *
	 * @param step the planning step parameter
	 * @param supportFoot the support foot of the step
	 * @return the relative pose of the free foot
	 */
	public Pose2D calculateRelativeFreeFootPose(StepParameters step, SupportFoot supportFoot)
	{
		SupportFoot freeFoot = supportFoot == SupportFoot.LEFT ? SupportFoot.RIGHT : SupportFoot.LEFT;
		Pose6D supportFootTargetPose = calculateFootTargetPose(step, supportFoot, supportFoot, 0.0);
		Pose6D freeFootTargetPose = calculateFootTargetPose(step, freeFoot, supportFoot, 0.0);

		Pose2D supportFootTargetPose2D =
				new Pose2D(supportFootTargetPose.y, -supportFootTargetPose.x, Angle.deg(supportFootTargetPose.zAngle));
		Pose2D freeFootTargetPose2D =
				new Pose2D(freeFootTargetPose.y, -freeFootTargetPose.x, Angle.deg(freeFootTargetPose.zAngle));

		return supportFootTargetPose2D.applyInverseTo(freeFootTargetPose2D);
	}

	@Override
	public IKWalkMovementParametersBase getWalkParameters()
	{
		return params;
	}

	@Override
	public double getSpeed()
	{
		double maxMovement = Math.max(Math.abs(currentStep.xTargetDistance / params.getMaxStepWidth()),
				Math.abs(currentStep.yTargetDistance / params.getMaxStepLength()));

		return Math.max(maxMovement, Math.abs(currentStep.turnAngle / params.getMaxTurnAngle()));
	}

	@Override
	public Vector3D getIntendedLeaningVector()
	{
		return getLeaningVector(currentStep);
	}

	/**
	 * @param currentStep: the current step parameter
	 * @return the leaning vector with respect to the given step parameter
	 */
	protected Vector3D getLeaningVectorAcceleration(StepParameters currentStep)
	{
		double yAngle = 0;
		// double xAngle = 0;
		double acceleration = currentStep.yTargetDistance - previousStep.yTargetDistance;
		double xAngle = Math.toRadians(acceleration * -800);

		return new Rotation(RotationOrder.XYZ, xAngle, yAngle, 0).applyTo(Vector3D.PLUS_K);
	}

	/**
	 * @param currentStep: the current step parameter
	 * @return the leaning vector with respect to the given step parameter
	 */
	protected Vector3D getLeaningVector(StepParameters currentStep)
	{
		// double xAngle = Math.toRadians(-1 * params.getMaxForwardLeaning()
		// * currentStep.y_targetDistance / params.getMaxStepLength());
		// double yAngle = Math.toRadians(params.getMaxSidewardsLeaning()
		// * currentStep.x_targetDistance / params.getMaxStepWidth());
		double xAngle = Math.toRadians(-1 * params.getMaxForwardLeaning());
		double yAngle = Math.toRadians(params.getMaxSidewardsLeaning());

		return new Rotation(RotationOrder.XYZ, xAngle, yAngle, 0).applyTo(Vector3D.PLUS_K);
	}

	@Override
	public void setNextStep(StepParameters nextStep)
	{
		this.previousStep.from(currentStep);
		this.currentStep.from(nextStep);
	}

	@Override
	public StepParameters getCurrentStep()
	{
		return currentStep;
	}
}
