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
package magma.agent.model.worldmeta.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class RCServerMetaModelV62 extends RCServerMetaModelBase
{
	public static final RCServerMetaModelV62 INSTANCE = new RCServerMetaModelV62();

	@Override
	public int getVersion()
	{
		return 62;
	}

	@Override
	public float getBallDecay()
	{
		return 0.94f;
	}

	@Override
	protected Vector2D initFieldDimensions()
	{
		return new Vector2D(12, 8);
	}

	@Override
	protected Vector3D initGoalDimensions()
	{
		return new Vector3D(0.4, 1.4, 0.8);
	}

	@Override
	protected Vector2D initPenaltyAreaDimensions()
	{
		return new Vector2D(1.2, 2.6);
	}

	@Override
	public float getMiddleCircleRadius()
	{
		return 1;
	}

	@Override
	public float getBallRadius()
	{
		return 0.042f;
	}

	@Override
	protected Map<String, Vector3D> initLandMarks()
	{
		landmarks = new HashMap<>();

		double fieldHalfLength = getFieldDimensions().getX() / 2;
		double fieldHalfWidth = getFieldDimensions().getY() / 2;
		double goalHalfWidth = getGoalDimensions().getY() / 2;
		double goalHeight = getGoalDimensions().getZ();

		// left goal posts
		landmarks.put("G1L", new Vector3D(-fieldHalfLength, goalHalfWidth, goalHeight));
		landmarks.put("G2L", new Vector3D(-fieldHalfLength, -goalHalfWidth, goalHeight));
		// right goal posts
		landmarks.put("G1R", new Vector3D(fieldHalfLength, goalHalfWidth, goalHeight));
		landmarks.put("G2R", new Vector3D(fieldHalfLength, -goalHalfWidth, goalHeight));
		// flags
		landmarks.put("F1L", new Vector3D(-fieldHalfLength, fieldHalfWidth, 0.0f));
		landmarks.put("F2L", new Vector3D(-fieldHalfLength, -fieldHalfWidth, 0.0f));
		landmarks.put("F1R", new Vector3D(fieldHalfLength, fieldHalfWidth, 0.0f));
		landmarks.put("F2R", new Vector3D(fieldHalfLength, -fieldHalfWidth, 0.0f));

		return landmarks;
	}

	@Override
	protected Map<String, Vector3D[]> initFieldLines()
	{
		return null;
	}
}
