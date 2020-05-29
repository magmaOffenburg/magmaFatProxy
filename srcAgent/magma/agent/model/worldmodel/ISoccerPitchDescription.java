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
package magma.agent.model.worldmodel;

import java.io.Serializable;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public interface ISoccerPitchDescription extends Serializable {
	float fieldHalfLength();

	float fieldHalfWidth();

	float goalHalfWidth();

	float goalHeight();

	float goalDepth();

	float penaltyHalfLength();

	float penaltyWidth();

	float centerCircleRadius();

	/**
	 * Retrieve the position of the own goal (position between the goal posts).
	 *
	 * @return the position in the middle of the own goal posts
	 */
	Vector3D getOwnGoalPosition();

	/**
	 * Retrieve the position of the other goal (position between the goal posts).
	 *
	 * @return the position in the middle of the other goal posts
	 */
	Vector3D getOtherGoalPosition();
}
