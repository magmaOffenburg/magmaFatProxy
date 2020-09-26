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
package magma.agent.model.thoughtmodel.impl;

import hso.autonomy.util.geometry.Area2D;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.ISoccerPitchDescription;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class PenaltyKickPositionProfiler extends KickPositionProfilerGoal
{
	public PenaltyKickPositionProfiler(ISoccerPitchDescription soccerPitch)
	{
		super(soccerPitch);
	}

	@Override
	protected float estimateUtility(IRoboCupThoughtModel thoughtModel, Area2D.Float playableArea, Vector3D position,
			double opponentBlockDistance)
	{
		return (float) -thoughtModel.getWorldModel().getOtherGoalPosition().distance(position);
	}
}
