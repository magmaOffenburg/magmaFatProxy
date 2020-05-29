/*******************************************************************************
 * Copyright 2008 - 2020 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under MIT License (see LICENSE).
 *******************************************************************************/
package hso.autonomy.agent.model.agentmeta.impl;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import hso.autonomy.agent.model.agentmeta.IHingeJointConfiguration;
import hso.autonomy.util.geometry.Pose3D;

public class HingeJointConfiguration extends SensorConfiguration implements IHingeJointConfiguration
{
	private final String effectorName;

	private final int minAngle;

	private final int maxAngle;

	private float maxSpeed;

	private float maxAcceleration;

	private final boolean defaultToInitialPos;

	public HingeJointConfiguration(String name, String perceptorName, String effectorName, Vector3D jointAxis,
			int minAngle, int maxAngle, float maxSpeed, float maxAcceleration, boolean defaultToInitialPos)
	{
		super(name, perceptorName, new Pose3D(Vector3D.ZERO, new Rotation(Vector3D.PLUS_K, jointAxis)));

		this.effectorName = effectorName;
		this.minAngle = minAngle;
		this.maxAngle = maxAngle;
		this.maxSpeed = maxSpeed;
		this.maxAcceleration = maxAcceleration;
		this.defaultToInitialPos = defaultToInitialPos;
	}

	public HingeJointConfiguration(String name, String perceptorName, String effectorName, Vector3D jointAxis,
			int jointMinAngle, int jointMaxAngle, float jointMaxSpeed, boolean defaultToInitialPos)
	{
		this(name, perceptorName, effectorName, jointAxis, jointMinAngle, jointMaxAngle, jointMaxSpeed, Float.MAX_VALUE,
				defaultToInitialPos);
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getPerceptorName()
	{
		return perceptorName;
	}

	@Override
	public String getEffectorName()
	{
		return effectorName;
	}

	@Override
	public Vector3D getJointAxis()
	{
		return pose.getOrientation().applyTo(Vector3D.PLUS_K);
	}

	@Override
	public int getMinAngle()
	{
		return minAngle;
	}

	@Override
	public int getMaxAngle()
	{
		return maxAngle;
	}

	@Override
	public float getMaxSpeed()
	{
		return maxSpeed;
	}

	@Override
	public float getMaxAcceleration()
	{
		return maxAcceleration;
	}

	@Override
	public void setMaxSpeed(float maxSpeed)
	{
		this.maxSpeed = maxSpeed;
	}

	@Override
	public boolean getDefaultToInitialPos()
	{
		return defaultToInitialPos;
	}
}
