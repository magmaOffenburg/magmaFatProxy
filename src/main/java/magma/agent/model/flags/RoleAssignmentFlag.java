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

/**
 * Flag for indicating the role assignment.
 *
 * @author Stefan Glaser
 */
public enum RoleAssignmentFlag
{
	/** Dynamic role assignment based on internal logic */
	DYNAMIC,

	/** Static striker role assignment */
	STRIKER,

	/** Static goalie role assignment */
	GOALIE;

	/**
	 * Retrieve the default value to this flag.
	 *
	 * @return the default value
	 */
	public static RoleAssignmentFlag getDefault()
	{
		return DYNAMIC;
	}
}
