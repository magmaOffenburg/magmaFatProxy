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
package magma.agent.model.agentmeta.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import hso.autonomy.agent.communication.action.IEffector;
import hso.autonomy.agent.model.agentmeta.IBodyPartConfiguration;
import hso.autonomy.agent.model.agentmeta.IHingeJointConfiguration;
import hso.autonomy.util.geometry.IPose3D;
import magma.agent.model.agentmeta.IRoboCupAgentMetaModel;

public class AgentMetaModelDecorator implements IRoboCupAgentMetaModel
{
	private final IRoboCupAgentMetaModel decoratee;

	public AgentMetaModelDecorator(IRoboCupAgentMetaModel decoratee)
	{
		this.decoratee = decoratee;
	}

	@Override
	public String getName()
	{
		return decoratee.getName();
	}

	public Map<String, IEffector> createEffectors()
	{
		return decoratee.createEffectors();
	}

	@Override
	public String getSceneString()
	{
		return decoratee.getSceneString();
	}

	@Override
	public Vector3D getStaticPivotPoint()
	{
		return decoratee.getStaticPivotPoint();
	}

	@Override
	public List<IBodyPartConfiguration> getBodyPartConfigurations()
	{
		return decoratee.getBodyPartConfigurations();
	}

	@Override
	public String getNameOfCameraBodyPart()
	{
		return decoratee.getNameOfCameraBodyPart();
	}

	@Override
	public IPose3D getCameraOffset()
	{
		return decoratee.getCameraOffset();
	}

	@Override
	public List<IHingeJointConfiguration> getAvailableJoints()
	{
		return decoratee.getAvailableJoints();
	}

	@Override
	public List<IBodyPartConfiguration> getChildBodyConfigurations(IBodyPartConfiguration bodyPart)
	{
		return decoratee.getChildBodyConfigurations(bodyPart);
	}

	@Override
	public IBodyPartConfiguration getRootBodyConfiguration()
	{
		return decoratee.getRootBodyConfiguration();
	}

	@Override
	public List<String> getAvailableJointNames()
	{
		return decoratee.getAvailableJointNames();
	}

	@Override
	public List<String> getJointPerceptorNames()
	{
		return decoratee.getJointPerceptorNames();
	}

	@Override
	public List<String> getAvailableEffectorNames()
	{
		return decoratee.getAvailableEffectorNames();
	}

	@Override
	public float getSoccerPositionKneeAngle()
	{
		return decoratee.getSoccerPositionKneeAngle();
	}

	@Override
	public float getSoccerPositionHipAngle()
	{
		return decoratee.getSoccerPositionHipAngle();
	}

	@Override
	public float getHeight()
	{
		return decoratee.getHeight();
	}

	@Override
	public int getGoalPredictionTime()
	{
		return decoratee.getGoalPredictionTime();
	}

	@Override
	public float getCycleTime()
	{
		return decoratee.getCycleTime();
	}

	@Override
	public float getVisionCycleTime()
	{
		return decoratee.getVisionCycleTime();
	}

	@Override
	public float getTorsoZUpright()
	{
		return decoratee.getTorsoZUpright();
	}

	@Override
	public boolean hasFootForceSensors()
	{
		return decoratee.hasFootForceSensors();
	}
}
