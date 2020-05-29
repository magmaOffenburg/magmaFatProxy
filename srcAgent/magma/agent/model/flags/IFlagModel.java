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
package magma.agent.model.flags;

import hso.autonomy.agent.communication.perception.IPerception;

/**
 * @author Stefan Glaser
 */
public interface IFlagModel {
	/**
	 * Called to trigger an update of the Flags based on the given perception
	 * object.
	 *
	 * @param perception - the Perception
	 */
	boolean update(IPerception perception);

	/**
	 * Set the given flag (assign true).
	 *
	 * @param flag - the flag to set
	 */
	void set(Flag flag);

	/**
	 * Clear the given flag (assign false).
	 *
	 * @param flag - the flag to clear
	 */
	void clear(Flag flag);

	/**
	 * Set the given flag to the specified value.
	 *
	 * @param flag - the flag to set/clear
	 * @param value - the new value of the flag
	 */
	void set(Flag flag, boolean value);

	/**
	 * Check if the given flag is set or not.
	 *
	 * @param flag - the flag to check
	 * @return true if the flag was set, false otherwise
	 */
	boolean isSet(Flag flag);

	/**
	 * Set the role assignment flag the the given value.
	 *
	 * @param value - the new value of the role-assignment-flag
	 */
	void setRoleAssignmentFlag(RoleAssignmentFlag value);

	/**
	 * Role flag.
	 *
	 * @return role assignment indicator
	 */
	RoleAssignmentFlag getRoleAssignmentFlag();
}
