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
package magma.agent.model.agentmodel.impl;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import hso.autonomy.agent.model.agentmeta.IAgentMetaModel;
import hso.autonomy.agent.model.agentmodel.impl.AgentModel;
import hso.autonomy.agent.model.agentmodel.impl.BodyModel;
import hso.autonomy.agent.model.agentmodel.impl.ik.IAgentIKSolver;
import hso.autonomy.util.geometry.Pose2D;
import magma.agent.model.agentmeta.IRoboCupAgentMetaModel;
import magma.agent.model.agentmodel.IRoboCupAgentModel;

/**
 * Implementation of the RoboCup specific part of AgentModel. Used to represent all the information the
 * agent has about itself.
 *
 * @author Klaus Dorer
 */
public class RoboCupAgentModel extends AgentModel implements IRoboCupAgentModel
{
	/** the static pivot point used in static movement definitions */
	protected final Vector3D staticPivotPoint;

	/**
	 * initializes all known Sensors like: HingeJoints, ForceResistances and
	 * GyroRates
	 */
	public RoboCupAgentModel(IRoboCupAgentMetaModel metaModel, IAgentIKSolver ikSolver)
	{
		super(metaModel, ikSolver);
		staticPivotPoint = metaModel.getStaticPivotPoint();
	}

	@Override
	public void beamToPosition(Pose2D pose)
	{
		((RoboCupBodyModel) bodyModelFuture).beamToPosition(pose);
	}

	@Override
	public void sayMessage(String message)
	{
		((RoboCupBodyModel) bodyModelFuture).sayMessage(message);
	}

	@Override
	public Vector3D getStaticPivotPoint()
	{
		if (staticPivotPoint == null) {
			return Vector3D.ZERO;
		}

		return staticPivotPoint;
	}

	/**
	 * Factory method
	 * @param sourceModel the source from which to create the new body model
	 * @return the specific body model created
	 */
	@Override
	protected BodyModel createBodyModel(BodyModel sourceModel)
	{
		return new RoboCupBodyModel((RoboCupBodyModel) sourceModel);
	}

	/**
	 * Factory method
	 * @param metaModel the agent configuration meta model
	 * @param ikSolver an inverse kinematic solver if required
	 * @return the specific body model created
	 */
	@Override
	protected BodyModel createBodyModel(IAgentMetaModel metaModel, IAgentIKSolver ikSolver)
	{
		return new RoboCupBodyModel(metaModel, ikSolver);
	}

	@Override
	protected IRoboCupAgentMetaModel getMetaModel()
	{
		return (IRoboCupAgentMetaModel) super.getMetaModel();
	}

	@Override
	public boolean hasFootForceSensors()
	{
		return getMetaModel().hasFootForceSensors();
	}

	@Override
	public float getSoccerPositionKneeAngle()
	{
		return getMetaModel().getSoccerPositionKneeAngle();
	}

	@Override
	public float getSoccerPositionHipAngle()
	{
		return getMetaModel().getSoccerPositionHipAngle();
	}

	@Override
	public float getCycleTime()
	{
		return getMetaModel().getCycleTime();
	}

	@Override
	public int getGoalPredictionTime()
	{
		return getMetaModel().getGoalPredictionTime();
	}

	@Override
	public float getHeight()
	{
		return getMetaModel().getHeight();
	}

	@Override
	public float getTorsoZUpright()
	{
		return getMetaModel().getTorsoZUpright();
	}

	@Override
	public String getModelName()
	{
		return getMetaModel().getName();
	}
}
