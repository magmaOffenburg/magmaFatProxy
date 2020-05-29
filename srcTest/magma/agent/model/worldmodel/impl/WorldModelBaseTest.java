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
package magma.agent.model.worldmodel.impl;

import static org.mockito.Matchers.anyFloat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import hso.autonomy.agent.model.worldmodel.ILandmark;
import hso.autonomy.agent.model.worldmodel.IVisibleObject;
import magma.agent.IMagmaConstants;
import magma.agent.model.worldmodel.IBall;
import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.agent.model.worldmodel.IThisPlayer;

/**
 *
 * @author Klaus Dorer
 */
public class WorldModelBaseTest
{
	protected IRoboCupWorldModel worldModelMock;

	protected IThisPlayer thisPlayerMock;

	protected IBall ballMock;

	protected IPlayer playerMock1;

	protected IPlayer playerMock2;

	protected IPlayer playerMock3;

	protected IPlayer playerMock4;

	@Before
	public void setUp()
	{
		playerMock1 = createPlayerMock(playerMock1, 1);
		playerMock2 = createPlayerMock(playerMock2, 2);
		playerMock3 = createPlayerMock(playerMock3, 3);
		playerMock4 = createPlayerMock(playerMock4, 4);
		ballMock = mock(IBall.class);
		thisPlayerMock = mock(IThisPlayer.class);
		when(thisPlayerMock.getID()).thenReturn(3);
		worldModelMock = mock(IRoboCupWorldModel.class);
		when(worldModelMock.getBall()).thenReturn(ballMock);
		when(worldModelMock.getThisPlayer()).thenReturn(thisPlayerMock);
		when(worldModelMock.getGlobalTime()).thenReturn(0.0f);
		when(worldModelMock.getServerVersion()).thenReturn(IMagmaConstants.DEFAULT_SERVER_VERSION);
		when(worldModelMock.getLandmarks()).thenReturn(new ArrayList<ILandmark>());
		when(worldModelMock.getGoalPostObstacles()).thenReturn(new ArrayList<IVisibleObject>());
	}

	/**
	 * Train a player mock (for unit testing only!)
	 *
	 * @param playerMock Player mock
	 * @param id Player id
	 * @return Trained mock
	 */
	IPlayer createPlayerMock(IPlayer playerMock, int id)
	{
		playerMock = mock(IPlayer.class);
		when(playerMock.isOwnTeam()).thenReturn(true);
		when(playerMock.getID()).thenReturn(id);
		when(playerMock.getAge(anyFloat())).thenReturn(0.0f);

		if (id == 1) {
			when(playerMock.isGoalie()).thenReturn(true);
		} else {
			when(playerMock.isGoalie()).thenReturn(false);
		}

		return playerMock;
	}

	@Test
	public void emptyDummyTestToKeepJenkinsHappy() throws Exception
	{
	}
}
