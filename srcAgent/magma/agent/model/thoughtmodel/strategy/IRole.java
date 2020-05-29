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
package magma.agent.model.thoughtmodel.strategy;

import hso.autonomy.util.geometry.IPose2D;

/**
 * Interface for all roles our team can take. The position specifies where the
 * player is situated on the field (horizontal viewpoint).
 *
 * @author Stephan Kammerer
 */
public interface IRole {
	/**
	 * @return the name of the role
	 */
	String getName();

	/**
	 * Update the internal state of the role.
	 */
	void update();

	/**
	 * @return the target pose
	 */
	IPose2D getTargetPose();

	IPose2D getIndependentTargetPose();

	/**
	 * Retrieve the priority of the role.
	 *
	 * @return the priority of the role
	 */
	float getPriority();
}
