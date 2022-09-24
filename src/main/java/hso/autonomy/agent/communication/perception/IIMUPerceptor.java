/*******************************************************************************
 * Copyright 2008 - 2020 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under MIT License (see LICENSE).
 *******************************************************************************/
package hso.autonomy.agent.communication.perception;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;

/**
 * The IMU perceptor measures the orientation (and acceleration) in the three-dimensional space.
 *
 * @author Stefan Glaser
 */
public interface IIMUPerceptor extends IPerceptor
{
	/**
	 * Get gyro orientation
	 */
	Rotation getOrientation();
}
