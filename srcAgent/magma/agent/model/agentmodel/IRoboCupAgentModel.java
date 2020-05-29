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
package magma.agent.model.agentmodel;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import hso.autonomy.agent.model.agentmodel.IAgentModel;
import hso.autonomy.util.geometry.Pose2D;

/**
 * Provides read only access to the agent model including universal joints,
 * hinge joints, force resistance and gyro rate sensors
 *
 * @author Stefan Glaser, Klaus Dorer
 */
public interface IRoboCupAgentModel extends IAgentModel {
	/**
	 * Set the message to say in that cycle.
	 */
	void sayMessage(String message);

	/**
	 * Set the beam position of the agent.
	 */
	void beamToPosition(Pose2D pose);

	/**
	 * Returns the static pivot-point used as replacement of the CoM in the
	 * balancing engine related movements. This pivot-point is usually close to
	 * the initial CoM location or somewhere in the pelvis of the robot.
	 *
	 * @return the static pivot-point
	 */
	Vector3D getStaticPivotPoint();

	/**
	 * @return true if this agent has foot force sensors attached
	 */
	boolean hasFootForceSensors();

	/**
	 * @return the angle (in degrees) the knees should have when starting to walk
	 */
	float getSoccerPositionKneeAngle();

	/**
	 * @return the angle (in degrees) the hip pitch should have when starting to walk
	 */
	float getSoccerPositionHipAngle();

	/**
	 * @return the time (in s) for one robot cycle
	 */
	float getCycleTime();

	/**
	 * @return the time (in cycles) we look into future to predict goals
	 */
	int getGoalPredictionTime();

	/**
	 * @return the height of the robot (in m)
	 */
	float getHeight();

	/**
	 * @return the z coordinate of the torso center when the robot is upright
	 */
	float getTorsoZUpright();

	/**
	 * @return the name of this robot model as defined in the meta model
	 */
	String getModelName();
}