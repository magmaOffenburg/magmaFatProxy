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
package magma.agent.agentruntime;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.util.file.SerializationUtil;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import magma.common.spark.IConnectionConstants;
import magma.common.spark.PlaySide;
import magma.util.logging.AgentData;
import magma.util.logging.BehaviorData;

public class GameSeriesReporter
{
	private final BehaviorMap behaviors;

	private final PlaySide playSide;

	private final int totalCycles;

	public GameSeriesReporter(BehaviorMap behaviors, PlaySide playSide, int totalCycles)
	{
		this.behaviors = behaviors;
		this.playSide = playSide;
		this.totalCycles = totalCycles;
	}

	public void report()
	{
		try (DatagramSocket socket = new DatagramSocket()) {
			InetAddress address = InetAddress.getByName("localhost");
			byte[] bytes = SerializationUtil.convertToBytes(collectStats());
			socket.send(new DatagramPacket(bytes, bytes.length, address, IConnectionConstants.GAME_SERIES_PORT));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private AgentData collectStats()
	{
		List<BehaviorData> stats = new ArrayList<>();
		for (IBehavior behavior : behaviors.getMap().values()) {
			if (behavior.getPerforms() == 0) {
				continue;
			}
			int cycles = behavior.getPerformedCycles();
			stats.add(
					new BehaviorData(behavior.getName(), behavior.getPerforms(), ((float) cycles / totalCycles) * 100));
		}
		return new AgentData(playSide, stats);
	}
}
