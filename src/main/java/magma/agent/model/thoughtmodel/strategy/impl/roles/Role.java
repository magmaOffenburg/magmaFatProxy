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
package magma.agent.model.thoughtmodel.strategy.impl.roles;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.misc.ValueUtil;
import magma.agent.model.thoughtmodel.strategy.IRole;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * This is the base class for roles. Each role knows its relative home-position
 * (x-, and y-coordinate) on the field. The determine Position-method returns
 * the target point each role has.
 *
 * @author Stephan Kammerer
 */
public abstract class Role implements IRole
{
	private static final float SAFETY_DISTANCE = 0.3f;

	protected final IRoboCupWorldModel worldModel;

	/**
	 * to avoid clustering in front of the penalty area in defensive situations
	 */
	protected final float safetyDistanceOffset;

	/** The name of the Role */
	protected final String name;

	/** Specific basic priority a role has in the team */
	protected final float basePriority;

	/** The dynamically calculated priority. */
	protected float priority;

	/** The target pose. */
	protected IPose2D targetPose;

	/** Minimal x-position where this role moves */
	protected final double minX;

	/** Maximal x-position where this role moves */
	protected final double maxX;

	/**
	 * @param name name of role
	 * @param priority Our priority of role
	 * @param minX Minimal x-position where this agent moves
	 * @param maxX Maximal x-position where this agent moves
	 */
	public Role(IRoboCupWorldModel worldModel, String name, float priority, double minX, double maxX)
	{
		this(worldModel, name, priority, minX, maxX, 0);
	}

	public Role(IRoboCupWorldModel worldModel, String name, float priority, double minX, double maxX,
			float safetyDistanceOffset)
	{
		this.worldModel = worldModel;
		this.name = name;
		this.basePriority = priority;
		this.targetPose = new Pose2D();
		this.minX = minX;
		this.maxX = maxX;
		this.safetyDistanceOffset = safetyDistanceOffset;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public float getPriority()
	{
		return priority;
	}

	protected float getExtraPriority(IRoboCupWorldModel worldModel)
	{
		float fieldHalfLength = worldModel.fieldHalfLength();
		Vector3D goalVector = new Vector3D(fieldHalfLength, 0, 0);

		Vector3D goalBallPos = worldModel.getBall().getPosition().add(goalVector);
		Pose2D trans = new Pose2D(-fieldHalfLength, 0, Angle.rad(goalBallPos.getAlpha()));
		Vector3D tempPos = trans.applyInverseTo(targetPose.getPosition3D().add(goalVector));

		return (float) (1 - (Math.abs(tempPos.getY()) / worldModel.fieldHalfWidth()));
	}

	@Override
	public void update()
	{
		// update target pose
		targetPose = avoidGoal(determinePosition());

		// update priority
		priority = basePriority + getExtraPriority(worldModel);
	}

	@Override
	public IPose2D getTargetPose()
	{
		return targetPose;
	}

	@Override
	public IPose2D getIndependentTargetPose()
	{
		return getTargetPose();
	}

	protected abstract IPose2D determinePosition();

	/**
	 * @param targetX the target x-position
	 * @return the limited target x-position
	 */
	protected double keepXLimits(double targetX)
	{
		return ValueUtil.limitValue(targetX, minX, maxX);
	}

	protected IPose2D avoidGoal(IPose2D target)
	{
		return avoidPenaltyArea(target);
	}

	private IPose2D avoidPenaltyArea(IPose2D target)
	{
		float penaltyLineY = worldModel.penaltyHalfLength() + worldModel.goalHalfWidth() + SAFETY_DISTANCE;
		float penaltyLineX =
				-worldModel.fieldHalfLength() + worldModel.penaltyWidth() + SAFETY_DISTANCE + safetyDistanceOffset;

		if (Math.abs(target.getY()) < penaltyLineY && target.getX() < penaltyLineX) {
			return new Pose2D(penaltyLineX, target.getY(), target.getAngle());
		}

		return target;
	}

	protected IPose2D keepMinDistanceToGoal(IPose2D target, double minGoalDistance)
	{
		Vector3D goalToTarget = target.getPosition3D().subtract(worldModel.getOwnGoalPosition());
		double distanceToGoal = goalToTarget.getNorm();
		if (distanceToGoal >= minGoalDistance) {
			return target;
		}

		if (distanceToGoal < 0.001) {
			return new Pose2D(minX, 0, target.getAngle());
		}
		return new Pose2D(worldModel.getOwnGoalPosition().add(goalToTarget.normalize().scalarMultiply(minGoalDistance)),
				target.getAngle());
	}

	protected Angle calculateBallDirection(Vector3D playerPosition)
	{
		return Angle.rad(worldModel.getBall().getPosition().subtract(playerPosition).getAlpha());
	}

	@Override
	public String toString()
	{
		return name + " " + priority;
	}
}
