/*******************************************************************************
 * Copyright 2008 - 2020 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under MIT License (see LICENSE).
 *******************************************************************************/
package hso.autonomy.agent.model.agentmodel;

import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.util.geometry.IPose3D;
import java.util.Map;

/**
 *
 * @author dorer
 */
public interface ISensor
{
	/**
	 * Retrieve the unique sensor name.
	 * @return Sensor name
	 */
	String getName();

	/**
	 * Retrieve the pose of this sensor, relative to its parent body.
	 * @return Sensor pose
	 */
	IPose3D getSensorPose();

	/**
	 * Updates the sensor information in the body model from perception
	 * @param perception the new perception we made
	 */
	void updateFromPerception(IPerception perception);

	/**
	 * Updates the sensor information in the body model with no perception
	 */
	void updateNoPerception();

	/**
	 * @return a deep copy of this sensor
	 */
	ISensor copy();

	/**
	 * Adds this sensor the appropriate list depending if it is structured or not
	 */
	void updateSensors(Map<String, ISensor> flatSensors, Map<String, ISensor> structuredSensors);
}