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
package magma.agent.communication.action.impl;

import java.util.Map;

import hso.autonomy.agent.communication.action.IEffector;
import hso.autonomy.agent.communication.action.IMessageEncoder;
import hso.autonomy.agent.communication.action.impl.HingeEffector;

/**
 * Encapsulation of the protocol to simspark server
 * @author kdorer
 */
public class ServerMessageEncoder implements IMessageEncoder
{
	@Override
	public byte[] encodeMessage(Map<String, IEffector> effectors)
	{
		StringBuilder builder = new StringBuilder(400);
		for (IEffector effector : effectors.values()) {
			if (effector instanceof HingeEffector) {
				HingeEffector hinge = (HingeEffector) effector;
				builder.append("(").append(hinge.getName());
				builder.append(" ").append(truncate(hinge.getSpeed())).append(")");

			} else if (effector instanceof BeamEffector) {
				float[] beam = ((BeamEffector) effector).getBeam();
				builder.append("(beam ").append(beam[0]).append(" ").append(beam[1]).append(" ").append(beam[2]).append(
						")");

			} else if (effector instanceof SayEffector) {
				String message = ((SayEffector) effector).getMessage();
				if (message.length() > 0) {
					builder.append("(say ").append(message).append(")");
				}
			}
		}

		builder.append("(syn)");

		return builder.toString().getBytes();
	}

	/**
	 * Truncates to three digits
	 * @param value the value to truncate
	 * @return truncated float
	 */
	private float truncate(float value)
	{
		int i = (int) (value * 1000.0);
		return i / 1000.0f;
	}
}
