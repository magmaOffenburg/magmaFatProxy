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
package magma.agent.communication.perception.impl;

import java.util.Arrays;
import java.util.Map;

import hso.autonomy.agent.communication.perception.IPerceptor;
import hso.autonomy.agent.communication.perception.PerceptorConversionException;
import hso.autonomy.agent.communication.perception.IMessageParser;

public class GameControllerMessageParser implements IMessageParser
{
	@Override
	public Map<String, IPerceptor> parseMessage(byte[] message) throws PerceptorConversionException
	{
		// Map<String, IPerceptor> perceptorList = new HashMap<String,
		// IPerceptor>();
		//
		// byte numPlayers;
		//
		// byte gameState = Constants.STATE_INITIAL;
		//
		// byte firstHalf = Constants.TRUE;
		//
		// byte kickOffTeam;
		//
		// byte secGameState = Constants.STATE2_NORMAL;
		//
		// byte dropInTeam;
		//
		// short dropInTime = -1;
		//
		// int estimatedSecs;
		//
		// TeamInfo[] teams = new TeamInfo[Constants.NUM_TEAMS];
		//
		// int offset = 4 + Constants.INT32_SIZE; // wegen header und version
		//
		// numPlayers = message[offset];
		// offset += Constants.INT8_SIZE;
		//
		// gameState = message[offset];
		// offset += Constants.INT8_SIZE;
		//
		// firstHalf = message[offset];
		// offset += Constants.INT8_SIZE;
		//
		// kickOffTeam = message[offset];
		// offset += Constants.INT8_SIZE;
		//
		// secGameState = message[offset];
		// offset += Constants.INT8_SIZE;
		//
		// dropInTeam = message[offset];
		// offset += Constants.INT8_SIZE;
		//
		// dropInTime = message[offset];
		// offset += Constants.INT16_SIZE;
		//
		// estimatedSecs = message[offset];
		// offset += Constants.INT32_SIZE;
		//
		// for (int i = 0; i < teams.length; i++) {
		// teams[i].setTeamNumber(message[offset]);
		// offset += Constants.INT8_SIZE;
		//
		// teams[i].setTeamColour(message[offset]);
		// offset += Constants.INT8_SIZE;
		//
		// teams[i].setGoalColour(message[offset]);
		// offset += Constants.INT8_SIZE;
		//
		// teams[i].setTeamScore(message[i]);
		// offset += Constants.INT8_SIZE;
		//
		// for (byte j = 0; j < teams[i].getPlayers().length; j++) {
		// teams[i].getPlayer(j).setPenalty(message[offset]);
		// offset += Constants.INT16_SIZE;
		//
		// teams[i].getPlayer(j).setSecsTillUnpenalised(message[offset]);
		// }
		// }
		//
		// // String gamestatestr = "" + gameState + secGameState;
		// // gamestateperceptor, timeperceptor
		// GameStatePerceptor gameStatePerceptor = new GameStatePerceptor();
		// perceptorList.put(gameStatePerceptor.getName(), gameStatePerceptor);
		//
		// TimePerceptor timePerceptor = new TimePerceptor();
		// perceptorList.put(timePerceptor.getName(), timePerceptor);
		//
		// return perceptorList;
		return null;
	}

	@Override
	public String getErrorString(byte[] message)
	{
		return Arrays.toString(message);
	}
}
