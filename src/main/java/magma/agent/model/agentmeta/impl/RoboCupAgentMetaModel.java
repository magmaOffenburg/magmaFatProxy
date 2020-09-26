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

import hso.autonomy.agent.communication.action.IEffector;
import hso.autonomy.agent.communication.action.impl.HingeEffector;
import hso.autonomy.agent.model.agentmeta.IBodyPartConfiguration;
import hso.autonomy.agent.model.agentmeta.IHingeJointConfiguration;
import hso.autonomy.agent.model.agentmeta.impl.AgentMetaModel;
import hso.autonomy.util.geometry.IPose3D;
import java.util.HashMap;
import java.util.Map;
import magma.agent.model.agentmeta.IRoboCupAgentMetaModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Agent meta model class
 *
 * @author Stefan Glaser
 */
public abstract class RoboCupAgentMetaModel extends AgentMetaModel implements IRoboCupAgentMetaModel
{
	protected final String sceneString;

	private final float soccerPositionKneeAngle;

	private final float soccerPositionHipAngle;

	private final int goalPredictionTime;

	private final float cycleTime;

	private final float visionCycleTime;

	protected final Vector3D staticPivotPoint;

	private final float torsoZUpright;

	/**
	 * Constructor.
	 *
	 * @param modelName - the name of this model
	 * @param sceneString - the scene string
	 * @param staticPivotPoint - the static pivot-point related to this robot
	 * @param bodyPartContainingCamera - the name of the body part containing the
	 *        camera
	 */
	public RoboCupAgentMetaModel(String modelName, String sceneString, Vector3D staticPivotPoint,
			String bodyPartContainingCamera, IPose3D cameraOffset, float soccerPositionKneeAngle,
			float soccerPositionHipAngle, float height, int goalPredictionTime, float cycleTime, float visionCycleTime,
			float torsoZUpright)
	{
		super(modelName, bodyPartContainingCamera, cameraOffset, height);
		this.sceneString = sceneString;
		this.staticPivotPoint = staticPivotPoint;
		this.soccerPositionKneeAngle = soccerPositionKneeAngle;
		this.soccerPositionHipAngle = soccerPositionHipAngle;
		this.goalPredictionTime = goalPredictionTime;
		this.cycleTime = cycleTime;
		this.visionCycleTime = visionCycleTime;
		this.torsoZUpright = torsoZUpright;
	}

	/**
	 * Creates all HingeJoint- and CompositeJoint-Effectors based on this meta-model
	 * @return a map with all effectors
	 */
	public Map<String, IEffector> createEffectors()
	{
		Map<String, IEffector> effectors = new HashMap<String, IEffector>();

		for (IBodyPartConfiguration config : bodyPartConfigs) {
			IHingeJointConfiguration jointConfig = config.getJointConfiguration();

			if (jointConfig != null) {
				String effectorName = jointConfig.getEffectorName();
				effectors.put(effectorName, new HingeEffector(effectorName));
			}
		}

		return effectors;
	}

	@Override
	public String getSceneString()
	{
		return sceneString;
	}

	@Override
	public Vector3D getStaticPivotPoint()
	{
		return staticPivotPoint;
	}

	@Override
	public float getSoccerPositionKneeAngle()
	{
		return soccerPositionKneeAngle;
	}

	@Override
	public float getSoccerPositionHipAngle()
	{
		return soccerPositionHipAngle;
	}

	@Override
	public int getGoalPredictionTime()
	{
		return goalPredictionTime;
	}

	@Override
	public float getCycleTime()
	{
		return cycleTime;
	}

	@Override
	public float getVisionCycleTime()
	{
		return visionCycleTime;
	}

	@Override
	public float getTorsoZUpright()
	{
		return torsoZUpright;
	}

	@Override
	public boolean hasFootForceSensors()
	{
		return false;
	}
}
