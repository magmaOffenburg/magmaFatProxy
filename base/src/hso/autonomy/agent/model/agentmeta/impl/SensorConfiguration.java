/*******************************************************************************
 * Copyright 2008 - 2020 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under MIT License (see LICENSE).
 *******************************************************************************/
package hso.autonomy.agent.model.agentmeta.impl;

import hso.autonomy.agent.model.agentmeta.ISensorConfiguration;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.geometry.Pose3D;

/**
 * @author Stefan Glaser
 */
public class SensorConfiguration implements ISensorConfiguration
{
	protected final String name;

	protected final String perceptorName;

	protected final IPose3D pose;

	public SensorConfiguration(String name, String perceptorName)
	{
		this(name, perceptorName, new Pose3D());
	}

	public SensorConfiguration(String name, String perceptorName, IPose3D pose)
	{
		this.name = name;
		this.perceptorName = perceptorName;
		this.pose = pose;
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
	public IPose3D getPose()
	{
		return pose;
	}
}
