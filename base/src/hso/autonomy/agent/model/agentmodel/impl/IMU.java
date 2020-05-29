/*******************************************************************************
 * Copyright 2008 - 2020 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under MIT License (see LICENSE).
 *******************************************************************************/
package hso.autonomy.agent.model.agentmodel.impl;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;

import hso.autonomy.agent.communication.perception.IIMUPerceptor;
import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.agent.model.agentmeta.ISensorConfiguration;
import hso.autonomy.agent.model.agentmodel.IIMU;
import hso.autonomy.agent.model.agentmodel.ISensor;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.IPose3D;

/**
 * Implementation of an IMU Sensor.
 *
 * @author Stefan Glaser
 *
 */
public class IMU extends Sensor implements IIMU
{
	/** The absolute IMU orientation. */
	private Rotation originalOrientation;

	/** The drift compensated absolute IMU orientation. */
	private Rotation orientation;

	/** The offset rotation around z-axis for compensating Gyroscope drift. */
	private Angle zOffset;

	/**
	 * Instantiates a new IMU sensor and initializes all values to their default
	 *
	 * @param name sensor name
	 * @param perceptorName unique name of the perceptor
	 * @param pose the sensor pose relative to the body part it is mounted to
	 */
	public IMU(String name, String perceptorName, IPose3D pose)
	{
		super(name, perceptorName, pose);

		originalOrientation = Rotation.IDENTITY;
		orientation = Rotation.IDENTITY;
		zOffset = Angle.ZERO;
	}

	public IMU(ISensorConfiguration config)
	{
		this(config.getName(), config.getPerceptorName(), config.getPose());
	}

	/**
	 * Copy constructor
	 * @param source the object to copy from
	 */
	public IMU(IMU source)
	{
		super(source);

		originalOrientation = source.originalOrientation;
		orientation = source.orientation;
		zOffset = source.zOffset;
	}

	/**
	 * Retrieve the original orientation without drift compensation.
	 *
	 * @return the original orientation measured by the sensor
	 */
	public Rotation getOriginalOrientation()
	{
		return originalOrientation;
	}

	@Override
	public Rotation getOrientation()
	{
		return orientation;
	}

	/**
	 * Set new IMU orientation.
	 *
	 * @param orientation the new orientation
	 */
	public void setOrientation(Rotation orientation)
	{
		originalOrientation = pose.getOrientation().applyTo(orientation).applyTo(pose.getOrientation().revert());
		compensateGyroDrift();
	}

	/**
	 * Update drift compensated Gyroscope orientation based on current z-offset angle.
	 */
	protected void compensateGyroDrift()
	{
		double[] angles = originalOrientation.getAngles(RotationOrder.ZYX);
		orientation = new Rotation(RotationOrder.ZYX, (angles[0] + zOffset.radians()), angles[1], angles[2]);
	}

	@Override
	public Angle getZOffset()
	{
		return zOffset;
	}

	@Override
	public void setZOffset(Angle zOffset)
	{
		this.zOffset = zOffset;
		compensateGyroDrift();
	}

	@Override
	public void updateZOffset(Angle delta, double factor)
	{
		setZOffset(zOffset.add(delta.radians() * factor));
	}

	@Override
	public void updateFromPerception(IPerception perception)
	{
		IIMUPerceptor perceptor = perception.getIMUPerceptor(getPerceptorName());
		if (perceptor != null) {
			setOrientation(perceptor.getOrientation());
		}
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof IMU)) {
			return false;
		}
		IMU other = (IMU) o;
		if (!super.equals(other)) {
			return false;
		}

		return zOffset.equals(other.zOffset) &&
				Rotation.distance(originalOrientation, other.originalOrientation) < Math.PI / 1800;
	}

	@Override
	public ISensor copy()
	{
		return new IMU(this);
	}
}
