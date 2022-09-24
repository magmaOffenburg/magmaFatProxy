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
package magma.agent.model.worldmeta;

import java.io.Serializable;
import java.util.Map;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public interface IRoboCupWorldMetaModel extends Serializable
{
	/**
	 * Returns the version of the meta model (server version)
	 *
	 * @return server version
	 */
	int getVersion();

	/**
	 * Returns the decay of the ball
	 *
	 * @return ball decay
	 */
	float getBallDecay();

	/**
	 * Returns the dimensions of the soccer field in the form: (length, width)
	 *
	 * @return the soccer field dimensions (meters)
	 */
	Vector2D getFieldDimensions();

	/**
	 * Returns the dimensions of the goal in the form: (depth, width, height)
	 *
	 * @return the goal dimensions (meters)
	 */
	Vector3D getGoalDimensions();

	/**
	 * Returns the dimensions of the penalty area in the form: (length, width)
	 * Note that the length here is width in naosoccersim.rb and vice versa. The
	 * width does NOT include the goal width.
	 *
	 * @return the penalty area dimensions (meters)
	 */
	Vector2D getPenaltyAreaDimensions();

	/**
	 * Returns the radius of the middle circle
	 *
	 * @return the middle circle radius (meters)
	 */
	float getMiddleCircleRadius();

	/**
	 * Returns a map of landmarks and their known position
	 *
	 * @return the map of landmarks
	 */
	Map<String, Vector3D> getLandmarks();

	/**
	 * Returns a map of field lines and their known position
	 *
	 * @return the map of field lines
	 */
	Map<String, Vector3D[]> getFieldLines();

	/**
	 * @return the ball radius in m
	 */
	float getBallRadius();
}
