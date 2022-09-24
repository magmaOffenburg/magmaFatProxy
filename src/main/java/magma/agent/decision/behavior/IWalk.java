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
package magma.agent.decision.behavior;

import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.util.geometry.Pose2D;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public interface IWalk extends IBehavior
{
	/**
	 * Set parameters for Walk. It is possible to combine all different
	 * parameters, e.g. forwards and sidesteps.
	 *
	 * @param forwardsBackwards positive = forwards; negative = backwards (-100
	 *        .. 100)
	 * @param stepLeftRight positive = right; negative = left (-100 .. 100)
	 * @param turnLeftRight positive = right; negative = left (-30 .. 30)
	 */
	void walk(double forwardsBackwards, double stepLeftRight, double turnLeftRight);

	/**
	 * Set parameters for Walk. It is possible to combine all different
	 * parameters, e.g. forwards and sidesteps.
	 *
	 * @param forwardsBackwards positive = forwards; negative = backwards (-100
	 *        .. 100)
	 * @param stepLeftRight positive = right; negative = left (-100 .. 100)
	 * @param turnLeftRight positive = right; negative = left (-30 .. 30)
	 * @param paramSetName the name of the walk parameter set to use
	 */
	void walk(double forwardsBackwards, double stepLeftRight, double turnLeftRight, String paramSetName);

	/**
	 *
	 * @param walkTo global position to walk to
	 * @param speedThere the speed vector we want to have at the destination
	 * @param slowDownDistance distance to this position at which we start to get
	 *        slower
	 * @param maxSpeedLimit the maximal speed we want to keep
	 * @param paramSetName name of the walk parameter set to use
	 */
	void globalWalk(
			Pose2D walkTo, Vector3D speedThere, double slowDownDistance, double maxSpeedLimit, String paramSetName);
}
