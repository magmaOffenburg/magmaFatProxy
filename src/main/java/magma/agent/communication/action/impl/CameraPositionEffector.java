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
package magma.agent.communication.action.impl;

import hso.autonomy.agent.communication.action.impl.Effector;
import hso.autonomy.util.geometry.Pose3D;
import magma.agent.communication.action.ICameraPositionEffector;

/**
 * @author rschilli
 *
 */
public class CameraPositionEffector extends Effector implements ICameraPositionEffector
{
	private Pose3D ballWorld;

	private Pose3D cameraTorso;

	private Pose3D cameraWorld;

	private String headBehavior;

	public CameraPositionEffector()
	{
		super("CameraPositionEffector");
	}

	@Override
	public Pose3D getBallWorld()
	{
		return ballWorld;
	}

	public void setBallWorld(Pose3D ballWorld)
	{
		this.ballWorld = ballWorld;
	}

	@Override
	public Pose3D getCameraTorso()
	{
		return cameraTorso;
	}

	public void setCameraTorso(Pose3D cameraTorso)
	{
		this.cameraTorso = cameraTorso;
	}

	@Override
	public Pose3D getCameraWorld()
	{
		return cameraWorld;
	}

	public void setCameraWorld(Pose3D cameraWorld)
	{
		this.cameraWorld = cameraWorld;
	}

	@Override
	public String getHeadBehavior()
	{
		return headBehavior;
	}

	public void setHeadBehavior(String headBehavior)
	{
		this.headBehavior = headBehavior;
	}

	@Override
	public void setEffectorValues(float... values)
	{
		// not used for this effector
	}
}
