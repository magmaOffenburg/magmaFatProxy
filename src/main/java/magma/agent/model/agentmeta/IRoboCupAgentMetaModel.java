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
package magma.agent.model.agentmeta;

import hso.autonomy.agent.model.agentmeta.IAgentMetaModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public interface IRoboCupAgentMetaModel extends IAgentMetaModel {
	/**
	 * Returns the corresponding action scene string, to this meta model.
	 *
	 * @return the scene-string
	 */
	String getSceneString();

	/**
	 * Returns the static pivot-point used as replacement of the CoM in the
	 * balancing engine related movements. This pivot-point is usually close to
	 * the initial CoM location or somewhere in the pelvis of the robot.
	 *
	 * @return the static pivot-point
	 */
	Vector3D getStaticPivotPoint();

	float getSoccerPositionKneeAngle();

	float getSoccerPositionHipAngle();

	int getGoalPredictionTime();

	float getCycleTime();

	float getVisionCycleTime();

	/**
	 * @return the z coordinate of the torso center when the robot is upright
	 */
	float getTorsoZUpright();

	boolean hasFootForceSensors();
}
