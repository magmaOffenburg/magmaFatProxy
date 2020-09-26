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
package magma.agent.model.agentmodel;

import hso.autonomy.agent.model.agentmodel.IBodyModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author dorer
 */
public interface IRoboCupBodyModel extends IBodyModel {
	/**
	 * Center of Mass relative to the ground
	 *
	 * TODO: Should be renamed to "Center of Stability" or something, since
	 * "center of Gravity" would result in the same as "Center of Mass" as all
	 * Body Parts have the same density
	 */
	Vector3D getCenterOfGravity();

	/**
	 * 4 Vectors that define the stability hull<br>
	 * <br>
	 * 0: Left foot, left tiptoe <br>
	 * 1: Left foot, left heel <br>
	 * 2: Right foot, right tiptoe <br>
	 * 3: Right foot, right heel
	 *
	 * @return Stability Hull
	 */
	Vector3D[] getStabilityHull();
}