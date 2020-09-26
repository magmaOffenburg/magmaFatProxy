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

import hso.autonomy.util.geometry.Pose6D;
import hso.autonomy.util.geometry.interpolation.IPoseInterpolator;
import hso.autonomy.util.geometry.interpolation.IValueInterpolator;
import hso.autonomy.util.geometry.interpolation.pose.PoseInterpolator;
import hso.autonomy.util.geometry.interpolation.value.LinearValueInterpolator;
import magma.agent.decision.behavior.SupportFoot;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * @author Stefan Glaser
 */
public abstract class IKMovementBase extends BalancingEngineParameters implements IIKMovement
{
	/** the name of the movement */
	protected String name;

	protected IRoboCupWorldModel worldModel;

	protected IRoboCupAgentModel agentModel;

	/** number of cycles for one movement unit */
	private int movementCycles;

	/** number of cycles to hold the last pose */
	protected int holdCycles;

	/** cycle counter starting at 0 when the movement is initialized */
	protected int cycleProgress;

	/** indicator for static movement */
	protected boolean isStatic;

	/** indicator for finished movement */
	protected boolean isFinished;

	/** the trajectories of all body parts over time */
	protected MovementTrajectories trajectories;

	/**
	 * The initial pose of the left foot. By default, this pose is set to the
	 * current left foot pose of the previously performed movement or initiated
	 * by the {@link #setDefaultInitialPoses()}.
	 */
	protected Pose6D leftFootInitialPose;

	/**
	 * The initial pose of the right foot. By default, this pose is set to the
	 * current right foot pose of the previously performed movement or initiated
	 * by the {@link #setDefaultInitialPoses()}.
	 */
	protected Pose6D rightFootInitialPose;

	/**
	 * The initial pose of the left arm. By default, this pose is set to the
	 * current left arm pose of the previously performed movement or initiated by
	 * the {@link #setDefaultInitialPoses()}.
	 */
	protected Pose6D leftArmInitialPose;

	/**
	 * The initial adjustment factors. By default, this adjustment factors are
	 * set to the current factors of the previously performed movement or
	 * initiated by the {@link #setDefaultInitialPoses()}.
	 */
	protected Vector2D initialAdjustmentFactors;

	/**
	 * The initial pose of the right arm. By default, this pose is set to the
	 * current right arm pose of the previously performed movement or initiated
	 * by the {@link #setDefaultInitialPoses()}.
	 */
	protected Pose6D rightArmInitialPose;

	/** pose-trajectory interpolator for left foot */
	protected IPoseInterpolator leftFootInterpolator;

	/** pose-trajectory interpolator for right foot */
	protected IPoseInterpolator rightFootInterpolator;

	/** pose-trajectory interpolator for left arm */
	protected IPoseInterpolator leftArmInterpolator;

	/** pose-trajectory interpolator for right arm */
	protected IPoseInterpolator rightArmInterpolator;

	/** value interpolator for saggital adjustment factor */
	protected IValueInterpolator saggitalAdjustmentInterpolator;

	/** value interpolator for coronal adjustment factor */
	protected IValueInterpolator coronalAdjustmentInterpolator;

	/** the supporting foot */
	protected SupportFoot supportFoot;

	/** the pose of the left foot in the current cycle */
	private Pose6D leftFootPose;

	/** the pose of the right foot in the current cycle */
	private Pose6D rightFootPose;

	/** the pose of the left arm in the current cycle */
	private Pose6D leftArmPose;

	/** the pose of the right arm in the current cycle */
	private Pose6D rightArmPose;

	/** the currently used index in the interpolation array */
	private int currentIndex;

	public IKMovementBase(String name, IRoboCupThoughtModel thoughtModel, int movementCycles, int holdCycles)
	{
		this.name = name;
		worldModel = thoughtModel.getWorldModel();
		agentModel = thoughtModel.getAgentModel();
		this.movementCycles = movementCycles;
		this.holdCycles = holdCycles;
		this.isStatic = false;
		this.isFinished = true;
		this.supportFoot = SupportFoot.BOTH;

		trajectories = new MovementTrajectories(movementCycles);

		leftFootInitialPose = new Pose6D();
		leftArmInitialPose = new Pose6D();
		rightFootInitialPose = new Pose6D();
		rightArmInitialPose = new Pose6D();
		initialAdjustmentFactors = Vector2D.ZERO;

		leftFootPose = new Pose6D();
		leftArmPose = new Pose6D();
		rightFootPose = new Pose6D();
		rightArmPose = new Pose6D();

		createTrajectoryInterpolators();
	}

	/**
	 * Create interpolator instances for all body parts. The default
	 * implementation creates pure linear pose trajectory interpolators.
	 */
	protected void createTrajectoryInterpolators()
	{
		leftFootInterpolator = new PoseInterpolator();
		rightFootInterpolator = new PoseInterpolator();
		leftArmInterpolator = new PoseInterpolator();
		rightArmInterpolator = new PoseInterpolator();

		saggitalAdjustmentInterpolator = new LinearValueInterpolator();
		coronalAdjustmentInterpolator = new LinearValueInterpolator();
	}

	/**
	 * Set the xxx-InitialPose attributes to their initial poses.<br>
	 * The default implementation resets all poses to zero.
	 */
	protected void setDefaultInitialPoses()
	{
		leftFootInitialPose.reset();
		leftArmInitialPose.reset();
		rightFootInitialPose.reset();
		rightArmInitialPose.reset();

		initialAdjustmentFactors = getAdjustmentTargetsByStaticIndicator();
	}

	/**
	 * HACK METHOD!!! Only for temporary use!! This method stupidly provides the
	 * adjustment factors used for simulated Nao robot.<br>
	 * Statement from 3. feb 2014 :)
	 *
	 * @return {1, 1} if isStatic is true, {0.2, 0.2*0.9} otherwise
	 */
	@Deprecated
	protected Vector2D getAdjustmentTargetsByStaticIndicator()
	{
		if (isStatic) {
			return new Vector2D(1, 1);
		} else {
			return new Vector2D(0.2f, 0.2f * 0.9f);
		}
	}

	/**
	 * HACK METHOD!!! Only for temporary use!! This method uses the adjustment
	 * factor targets dependent on the isStatic attribute (which probable will be
	 * deleted soon).<br>
	 * Statement from 3. feb 2014 :)
	 */
	@Deprecated
	protected void interpolateMovement(Pose6D leftFootTargetPose, Pose6D rightFootTargetPose)
	{
		interpolateMovement(leftFootTargetPose, rightFootTargetPose, getAdjustmentTargetsByStaticIndicator());
	}

	@Override
	public void init(IIKMovement other)
	{
		cycleProgress = 0;
		isFinished = false;

		if (other == null) {
			// set the default initial poses
			setDefaultInitialPoses();
		} else {
			// Fetch current pose of the other movement as initial pose of this
			// movement
			leftFootInitialPose.set(other.getLeftFootPose());
			leftArmInitialPose.set(other.getLeftArmPose());
			rightFootInitialPose.set(other.getRightFootPose());
			rightArmInitialPose.set(other.getRightArmPose());

			initialAdjustmentFactors =
					new Vector2D(other.getSaggitalAdjustmentFactor(), other.getCoronalAdjustmentFactor());
		}

		// calculate arm and leg pose trajectories based on the current situation
		calculateMovementTrajectory();
	}

	/**
	 * Called once at initialization of the movement to dynamically calculate the
	 * body part trajectories for the current situation.
	 */
	protected abstract void calculateMovementTrajectory();

	@Override
	public boolean update()
	{
		if (isFinished) {
			return false;
		}

		currentIndex = getIndexToCycle();

		// buffer poses to current cycle
		leftFootPose.set(trajectories.leftFootTrajectory[currentIndex]);
		leftArmPose.set(trajectories.leftArmTrajectory[currentIndex]);

		rightFootPose.set(trajectories.rightFootTrajectory[currentIndex]);
		rightArmPose.set(trajectories.rightArmTrajectory[currentIndex]);

		// buffer balancing parameters to current cycle and calculate pivot point
		saggitalAdjustmentFactor = (float) trajectories.adjustmentFactorTrajectory[currentIndex].getX();
		coronalAdjustmentFactor = (float) trajectories.adjustmentFactorTrajectory[currentIndex].getY();
		Vector3D com = agentModel.getCenterOfMass();
		Vector3D diff = agentModel.getStaticPivotPoint().subtract(com);
		float zFactor = (saggitalAdjustmentFactor + coronalAdjustmentFactor) / 2;
		pivotPoint = new Vector3D(									 //
				com.getX() + diff.getX() * saggitalAdjustmentFactor, //
				com.getY() + diff.getY() * coronalAdjustmentFactor,	 //
				com.getZ() + diff.getZ() * zFactor);

		// progress cycle counter
		cycleProgress++;

		// check if movement is finished
		if (cycleProgress >= (movementCycles + holdCycles)) {
			isFinished = true;
		}

		return true;
	}

	protected int getIndexToCycle()
	{
		return cycleProgress < movementCycles ? cycleProgress : movementCycles - 1;
	}

	/**
	 * Interpolates the (remaining) pose trajectory from this xxx-InitialPose
	 * attributes to the xxx-targetPose parameters. The poses are interpolated
	 * using the pose interpolators for the corresponding limbs.<br>
	 * This method calculates only the remaining poses from the current
	 * cycleProgress to movementCycles. This way, movements recalculating the
	 * remaining trajectory during their execution are less cost intensive and
	 * monitoring such movement keeps consistent. Less dynamic movements can
	 * still calculate a whole trajectory once during initialization, which will
	 * be performed iteratively.<br>
	 * <br>
	 * The adjustmentTargets for the balancing engine are handled similarly.
	 */
	protected void interpolateMovement(
			Pose6D leftFootTargetPose, Pose6D rightFootTargetPose, Vector2D adjustmentTargets)
	{
		// TODO: Hack: Add arm poses to method arguments =======================
		Pose6D leftArmTargetPose = new Pose6D();
		Pose6D rightArmTargetPose = new Pose6D();
		// ======================================================================

		// EXPERIMENT: kick while walk
		// calls++;
		// if (calls % 11 == 0) {
		//
		// if (supportFoot == SupportFoot.LEFT) {
		// setMovementCycles(15);
		// } else {
		// setMovementCycles(9);
		// }
		// } else {
		// setMovementCycles(9);
		// }

		for (int i = getIndexToCycle(); i < movementCycles; i++) {
			float t = ((float) i + 1) / movementCycles;

			MovementTrajectories trajectory = trajectories;

			// interpolate legs
			trajectory.leftFootTrajectory[i] =
					leftFootInterpolator.interpolate(leftFootInitialPose, leftFootTargetPose, t);
			trajectory.rightFootTrajectory[i] =
					rightFootInterpolator.interpolate(rightFootInitialPose, rightFootTargetPose, t);

			// EXPERIMENT: kick while walk
			// if (calls % 11 == 0) {
			// if (supportFoot == SupportFoot.LEFT && i > 3 && i < 10) {
			// rightFootTrajectory[i].y += 0.15;
			// rightFootTrajectory[i].z += 0.04;
			// System.out.println(rightFootTrajectory[i].y);
			// }
			// }

			// EXPERIMENT: variable walk height
			// double walkHeightDelta = Math.sin(t * Math.PI) * 0.03 - 0.0;
			// leftFootTrajectory[i].z += walkHeightDelta;
			// rightFootTrajectory[i].z += walkHeightDelta;
			// System.out.println("walkHeight: " + walkHeightDelta +" progress: "
			// + i);

			// interpolate arms
			trajectory.leftArmTrajectory[i] = leftArmInterpolator.interpolate(leftArmInitialPose, leftArmTargetPose, t);
			trajectory.rightArmTrajectory[i] =
					rightArmInterpolator.interpolate(rightArmInitialPose, rightArmTargetPose, t);

			// interpolate adjustment factors
			trajectory.adjustmentFactorTrajectory[i] =
					new Vector2D(saggitalAdjustmentInterpolator.interpolate(
										 initialAdjustmentFactors.getX(), adjustmentTargets.getX(), t),
							coronalAdjustmentInterpolator.interpolate(
									initialAdjustmentFactors.getY(), adjustmentTargets.getY(), t));
		}
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public SupportFoot getSupportFoot()
	{
		return supportFoot;
	}

	@Override
	public SupportFoot getNextSupportFoot()
	{
		return supportFoot;
	}

	@Override
	public boolean isStatic()
	{
		return isStatic;
	}

	@Override
	public boolean isFinished()
	{
		return isFinished;
	}

	/**
	 * Sets the number on movement cycles to the given parameter and
	 * reinitializes the target position/angle arrays if necessary.
	 */
	protected void setMovementCycles(int movementCycles)
	{
		this.movementCycles = movementCycles;

		if (trajectories.leftFootTrajectory.length < movementCycles) {
			trajectories = new MovementTrajectories(movementCycles);
		}
	}

	@Override
	public int getMovementCycles()
	{
		return movementCycles;
	}

	@Override
	public int getHoldCycles()
	{
		return holdCycles;
	}

	@Override
	public int getMovementProgress()
	{
		return cycleProgress;
	}

	@Override
	public Pose6D getLeftFootPose()
	{
		return leftFootPose;
	}

	@Override
	public Pose6D getRightFootPose()
	{
		return rightFootPose;
	}

	@Override
	public Pose6D getLeftArmPose()
	{
		return leftArmPose;
	}

	@Override
	public Pose6D getRightArmPose()
	{
		return rightArmPose;
	}

	public int getCurrentIndex()
	{
		return currentIndex;
	}

	@Override
	public MovementTrajectories getMovementTrajectories()
	{
		return trajectories;
	}
}