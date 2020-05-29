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

import java.util.Map;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import magma.agent.model.worldmeta.IRoboCupWorldMetaModel;

public abstract class RCServerMetaModelBase implements IRoboCupWorldMetaModel
{
	protected Vector2D fieldDimensions;

	protected Vector3D goalDimensions;

	protected Vector2D penaltyAreaDimensions;

	protected Map<String, Vector3D> landmarks;

	protected Map<String, Vector3D[]> fieldLines;

	@Override
	public final Vector2D getFieldDimensions()
	{
		if (fieldDimensions == null) {
			fieldDimensions = initFieldDimensions();
		}
		return fieldDimensions;
	}

	@Override
	public final Vector3D getGoalDimensions()
	{
		if (goalDimensions == null) {
			goalDimensions = initGoalDimensions();
		}
		return goalDimensions;
	}

	@Override
	public final Vector2D getPenaltyAreaDimensions()
	{
		if (penaltyAreaDimensions == null) {
			penaltyAreaDimensions = initPenaltyAreaDimensions();
		}
		return penaltyAreaDimensions;
	}

	@Override
	public final Map<String, Vector3D> getLandmarks()
	{
		if (landmarks == null) {
			landmarks = initLandMarks();
		}
		return landmarks;
	}

	@Override
	public final Map<String, Vector3D[]> getFieldLines()
	{
		if (fieldLines == null) {
			fieldLines = initFieldLines();
		}
		return fieldLines;
	}

	protected abstract Vector2D initFieldDimensions();

	protected abstract Vector3D initGoalDimensions();

	protected abstract Vector2D initPenaltyAreaDimensions();

	protected abstract Map<String, Vector3D> initLandMarks();

	protected abstract Map<String, Vector3D[]> initFieldLines();
}
