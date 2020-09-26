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
package magma.agent.decision.behavior.basic;

import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.util.geometry.Pose2D;
import magma.agent.decision.behavior.IBeam;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * This Behavior is to beam the agent somewhere to the field
 *
 * @author Ingo Schindler
 */
public class BeamToPosition extends RoboCupBehavior implements IBeam
{
	private Pose2D target;

	protected BeamToPosition(String name, IThoughtModel thoughtModel)
	{
		super(name, thoughtModel);
	}

	public BeamToPosition(IThoughtModel thoughtModel)
	{
		super(IBehaviorConstants.BEAM_TO_POSITION, thoughtModel);
	}

	@Override
	public void perform()
	{
		super.perform();

		IRoboCupWorldModel worldModel = getWorldModel();
		double previousZ = worldModel.getThisPlayer().getPosition().getZ();
		getAgentModel().beamToPosition(target);
		worldModel.resetPositionFilter();
		worldModel.getThisPlayer().setGlobalPosition(
				new Vector3D(target.x, target.y, previousZ), worldModel.getGlobalTime());
	}

	@Override
	public void setPose(Pose2D target)
	{
		this.target = new Pose2D(target);
	}

	@Override
	public Pose2D getPose()
	{
		return target;
	}
}
