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
package magma.agent.model.flags.impl;

import hso.autonomy.agent.communication.perception.IFlagPerceptor;
import hso.autonomy.agent.communication.perception.IPerception;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import magma.agent.IFlagConstants;
import magma.agent.model.flags.Flag;
import magma.agent.model.flags.IFlagModel;
import magma.agent.model.flags.RoleAssignmentFlag;

/**
 * Model for accessing different flags of the agent.
 *
 * @author Stefan Glaser
 */
public class FlagModel implements IFlagModel
{
	/** A map holding simple boolean flags */
	private Map<Flag, Boolean> simpleFlags;

	/** The role assignment flag */
	private RoleAssignmentFlag roleAssignmentFlag;

	public FlagModel()
	{
		// Create simple boolean flags
		simpleFlags = new HashMap<>();
		for (Flag f : Flag.values()) {
			simpleFlags.put(f, f.getDefault());
		}

		// Create enum flags
		roleAssignmentFlag = RoleAssignmentFlag.getDefault();
	}

	@Override
	public boolean update(IPerception perception)
	{
		if (perception != null) {
			List<IFlagPerceptor> flagPerceptors = perception.getFlags();

			for (IFlagPerceptor flag : flagPerceptors) {
				setFlag(flag.getName(), flag.getValue());
			}
		}

		return false;
	}

	private void setFlag(String flagName, int value)
	{
		// Check all simple (boolean) flags
		Flag flag = Flag.byName(flagName);
		if (flag != null) {
			set(flag, value != 0);
		} else {
			// Check for complex (multiple state) flags
			if (flagName.equals(IFlagConstants.ROLE_ASSIGNMENT)) {
				final RoleAssignmentFlag[] roleFlags = RoleAssignmentFlag.values();
				if (value >= 0 && value < roleFlags.length) {
					roleAssignmentFlag = roleFlags[value];
				} else {
					System.err.println("Invalid flag value! Flag: " + flagName + " -> " + value);
				}
			} else {
				System.err.println("Unknown flag: " + flagName);
			}
		}
	}

	@Override
	public void set(Flag flag)
	{
		set(flag, true);
	}

	@Override
	public void clear(Flag flag)
	{
		set(flag, false);
	}

	@Override
	public void set(Flag flag, boolean value)
	{
		simpleFlags.put(flag, value);
	}

	@Override
	public boolean isSet(Flag flag)
	{
		Boolean result = simpleFlags.get(flag);

		if (result != null) {
			return result;
		}

		return false;
	}

	@Override
	public RoleAssignmentFlag getRoleAssignmentFlag()
	{
		return roleAssignmentFlag;
	}

	@Override
	public void setRoleAssignmentFlag(RoleAssignmentFlag value)
	{
		if (value == null) {
			return;
		}

		roleAssignmentFlag = value;
	}
}
