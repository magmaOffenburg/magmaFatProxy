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
package magma.monitor.general.impl;

import magma.common.spark.PlayMode;

public class MonitorRuntimeTest extends MonitorRuntime
{
	private long counter;

	public MonitorRuntimeTest(MonitorParameters parameter)
	{
		super(parameter);
		counter = 0;
	}

	@Override
	public void update(byte[] content)
	{
		super.update(content);
		counter++;
		if (counter < 50) {
			return;
		}

		if (counter == 50) {
			commander.setPlaymode(PlayMode.PLAY_ON);
		}

		if ((counter + 250) % 350 == 0) {
			commander.beamBall(-2, 0.4f, 0, -18, 0, 6.35f);
			// commander.beamBall(-2, 0.5f,0,-22,0,0);
		} else if ((counter + 150) % 400 == 0) {
			commander.beamBall(0, 0);
			commander.setPlaymode(PlayMode.PLAY_ON);
		}
	}
}
