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
import hso.autonomy.agent.model.worldmodel.IVisibleObject;
import hso.autonomy.agent.model.worldmodel.InformationSource;
import hso.autonomy.util.misc.SayCoder;
import magma.agent.IMagmaConstants;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.model.worldmodel.IRoboCupWorldModel;

/**
 * Used for communication with the server
 *
 * @author Klaus Dorer
 */
public class SayPositions extends RoboCupBehavior
{
	public SayPositions(IThoughtModel thoughtModel)
	{
		super(IBehaviorConstants.SAY_POSITIONS, thoughtModel);
	}

	@Override
	public void perform()
	{
		super.perform();

		IRoboCupWorldModel worldModel = getWorldModel();
		int count = (int) (worldModel.getGlobalTime() * 100.f);
		// we may only say when it is our turn and only every 3 cycles and one
		// cycle is 20 ms
		if (count % 2 > 0) {
			// avoids a problem that server's global time sometimes is 30ms more
			count--;
		}
		int turn = ((count / 6) % IMagmaConstants.NUMBER_OF_PLAYERS_PER_TEAM) + 1;
		int ourID = worldModel.getThisPlayer().getID();
		if (turn != ourID || count % 6 != 0) {
			return;
		}

		String message = SayCoder.encodeID(ourID);
		message += SayCoder.encodePosition(worldModel.getThisPlayer().getPosition());

		// communicate ball position
		if (worldModel.getBall().getAge(worldModel.getGlobalTime()) < 0.1 &&
				worldModel.getBall().getInformationSource() == InformationSource.VISION) {
			// only say if ball has been seen recently
			message += SayCoder.encodePosition(worldModel.getBall().getPosition());
		} else {
			message += SayCoder.encodeInvalidPosition();
		}

		// communicate opponent position (same id)
		IVisibleObject player = worldModel.getVisiblePlayer(ourID, false);
		if (player != null && player.getAge(worldModel.getGlobalTime()) < 0.1 &&
				player.getInformationSource() == InformationSource.VISION) {
			message += SayCoder.encodePosition(player.getPosition());
		} else {
			message += SayCoder.encodeInvalidPosition();
		}

		// communicate opponent position (next id)
		IVisibleObject player2 =
				worldModel.getVisiblePlayer(((ourID + 5) % IMagmaConstants.NUMBER_OF_PLAYERS_PER_TEAM) + 1, false);
		if (player2 != null && player2.getAge(worldModel.getGlobalTime()) < 0.1 &&
				player2.getInformationSource() == InformationSource.VISION) {
			message += SayCoder.encodePosition(player2.getPosition());
		} else {
			message += SayCoder.encodeInvalidPosition();
		}

		message += SayCoder.encodeName(worldModel.getThisPlayer().getTeamname());

		getAgentModel().sayMessage(message);
	}
}
