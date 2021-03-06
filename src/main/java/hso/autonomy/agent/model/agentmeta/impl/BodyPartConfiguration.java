/*******************************************************************************
 * Copyright 2008 - 2020 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under MIT License (see LICENSE).
 *******************************************************************************/
package hso.autonomy.agent.model.agentmeta.impl;

import hso.autonomy.agent.model.agentmeta.IBodyPartConfiguration;
import hso.autonomy.agent.model.agentmeta.IHingeJointConfiguration;
import hso.autonomy.agent.model.agentmeta.ISensorConfiguration;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class BodyPartConfiguration implements IBodyPartConfiguration
{
	private final String name;

	private final String parent;

	private Vector3D translation;

	private float mass;

	private Vector3D geometry;

	private final IHingeJointConfiguration jointConfig;

	private Vector3D jointAnchor;

	private final ISensorConfiguration gyroRateConfig;

	private final ISensorConfiguration accelerometerConfig;

	private final ISensorConfiguration forceResistanceConfig;

	private final ISensorConfiguration imuConfig;

	private final ISensorConfiguration compassConfig;

	public BodyPartConfiguration(String name, String parent, Vector3D translation, float mass, Vector3D geometry,
			IHingeJointConfiguration jointConfig, Vector3D jointAnchor, ISensorConfiguration gyroRateConfig,
			ISensorConfiguration accelerometerConfig, ISensorConfiguration forceResistanceConfig,
			ISensorConfiguration imuConfig, ISensorConfiguration compassConfig)
	{
		this.name = name;
		this.parent = parent;
		this.translation = translation;
		this.mass = mass;
		this.geometry = geometry;
		this.jointConfig = jointConfig;
		this.jointAnchor = jointAnchor;
		this.gyroRateConfig = gyroRateConfig;
		this.accelerometerConfig = accelerometerConfig;
		this.forceResistanceConfig = forceResistanceConfig;
		this.imuConfig = imuConfig;
		this.compassConfig = compassConfig;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getParent()
	{
		return parent;
	}

	@Override
	public boolean isRootBody()
	{
		return parent == null || jointConfig == null;
	}

	@Override
	public Vector3D getTranslation()
	{
		return translation;
	}

	@Override
	public float getMass()
	{
		return mass;
	}

	@Override
	public Vector3D getGeometry()
	{
		return geometry;
	}

	@Override
	public IHingeJointConfiguration getJointConfiguration()
	{
		return jointConfig;
	}

	@Override
	public Vector3D getJointAnchor()
	{
		return jointAnchor;
	}

	@Override
	public ISensorConfiguration getGyroRateConfiguration()
	{
		return gyroRateConfig;
	}

	@Override
	public ISensorConfiguration getAccelerometerConfiguration()
	{
		return accelerometerConfig;
	}

	@Override
	public ISensorConfiguration getForceResistanceConfiguration()
	{
		return forceResistanceConfig;
	}

	@Override
	public ISensorConfiguration getIMUConfiguration()
	{
		return imuConfig;
	}

	@Override
	public ISensorConfiguration getCompassConfig()
	{
		return compassConfig;
	}

	public BodyPartConfiguration setTranslation(Vector3D translation)
	{
		this.translation = translation;
		return this;
	}

	public BodyPartConfiguration setMass(float mass)
	{
		this.mass = mass;
		return this;
	}

	public BodyPartConfiguration setGeometry(Vector3D geometry)
	{
		this.geometry = geometry;
		return this;
	}

	public BodyPartConfiguration setJointAnchor(Vector3D jointAnchor)
	{
		this.jointAnchor = jointAnchor;
		return this;
	}
}
