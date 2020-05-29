/*******************************************************************************
 * Copyright 2008 - 2020 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under MIT License (see LICENSE).
 *******************************************************************************/
package hso.autonomy.agent.communication.perception.impl;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import hso.autonomy.agent.communication.perception.IIMUPerceptor;

/**
 * IMU perceptor
 *
 * @author Stefan Glaser
 */
public class IMUPerceptor extends Perceptor implements IIMUPerceptor
{
	private Rotation orientation;

	/**
	 * Default constructor, initializes the IMU to zero.
	 *
	 * @param name Perceptor name
	 */
	public IMUPerceptor(String name)
	{
		this(name, Rotation.IDENTITY);
	}

	public IMUPerceptor(String name, Rotation orientation)
	{
		super(name);
		this.orientation = orientation;
	}

	@Override
	public Rotation getOrientation()
	{
		return orientation;
	}
}
