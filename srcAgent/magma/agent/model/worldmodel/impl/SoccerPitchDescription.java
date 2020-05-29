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
package magma.agent.model.worldmodel.impl;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import magma.agent.model.worldmeta.IRoboCupWorldMetaModel;
import magma.agent.model.worldmodel.ISoccerPitchDescription;

/**
 * This class describes a soccer pitch and some geometric calculations to it.
 *
 * @author Stefan Glaser
 */
public class SoccerPitchDescription implements ISoccerPitchDescription
{
	/** The half length of the soccer pitch (global x) */
	public final float fieldHalfLength;

	/** The half width of the soccer pitch (global y) */
	public final float fieldHalfWidth;

	/** The half width of the goals (global y) */
	public final float goalHalfWidth;

	/** The height of the goals (global z) */
	public final float goalHeight;

	/** The depth of the goals (global x) */
	public final float goalDepth;

	/** The width of the penalty areas (global x) */
	public final float penaltyWidth;

	/**
	 * The half length of the penalty areas (global y) - does NOT include the
	 * goal width!
	 */
	public final float penaltyHalfLength;

	/** The radius of the center circle */
	public final float centerCircleRadius;

	/** The position of the own goal (between the goal posts) */
	public final Vector3D ownGoalPosition;

	/** The position of the other goal (between the goal posts) */
	public final Vector3D otherGoalPosition;

	public SoccerPitchDescription(float fieldHalfLength, float fieldHalfWidth, float goalDepth, float goalHalfWidth,
			float goalHeight, float penaltyWidth, float penaltyHalfLength, float centerCircleRadius)
	{
		this.fieldHalfLength = fieldHalfLength;
		this.fieldHalfWidth = fieldHalfWidth;
		this.goalDepth = goalDepth;
		this.goalHalfWidth = goalHalfWidth;
		this.goalHeight = goalHeight;
		this.penaltyWidth = penaltyWidth;
		this.penaltyHalfLength = penaltyHalfLength;
		this.centerCircleRadius = centerCircleRadius;

		ownGoalPosition = new Vector3D(-fieldHalfLength, 0, 0);
		otherGoalPosition = new Vector3D(fieldHalfLength, 0, 0);
	}

	public SoccerPitchDescription(IRoboCupWorldMetaModel metaModel)
	{
		this((float) metaModel.getFieldDimensions().getX() / 2, (float) metaModel.getFieldDimensions().getY() / 2,
				(float) metaModel.getGoalDimensions().getX(), (float) metaModel.getGoalDimensions().getY() / 2,
				(float) metaModel.getGoalDimensions().getZ(), (float) metaModel.getPenaltyAreaDimensions().getX(),
				(float) metaModel.getPenaltyAreaDimensions().getY() / 2, metaModel.getMiddleCircleRadius());
	}

	@Override
	public float fieldHalfLength()
	{
		return fieldHalfLength;
	}

	@Override
	public float fieldHalfWidth()
	{
		return fieldHalfWidth;
	}

	@Override
	public float goalHalfWidth()
	{
		return goalHalfWidth;
	}

	@Override
	public float goalHeight()
	{
		return goalHeight;
	}

	@Override
	public float goalDepth()
	{
		return goalDepth;
	}

	@Override
	public float penaltyWidth()
	{
		return penaltyWidth;
	}

	@Override
	public float penaltyHalfLength()
	{
		return penaltyHalfLength;
	}

	@Override
	public float centerCircleRadius()
	{
		return centerCircleRadius;
	}

	@Override
	public Vector3D getOwnGoalPosition()
	{
		return ownGoalPosition;
	}

	@Override
	public Vector3D getOtherGoalPosition()
	{
		return otherGoalPosition;
	}
}
