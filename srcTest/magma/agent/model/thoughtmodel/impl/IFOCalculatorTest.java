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
package magma.agent.model.thoughtmodel.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Before;
import org.junit.Test;

import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.impl.WorldModelBaseTest;

/**
 * Test for the {@link IFOCalculator} class
 *
 * @author Klaus Dorer
 */
public class IFOCalculatorTest extends WorldModelBaseTest
{
	private IFOCalculator testee;

	private List<IPlayer> sourceList;

	@Override
	@Before
	public void setUp()
	{
		super.setUp();
		sourceList = new ArrayList<>();
		sourceList.add(playerMock1);
		sourceList.add(playerMock2);
		sourceList.add(playerMock3);
		sourceList.add(playerMock4);

		testee = new IFOCalculator(worldModelMock);
	}

	/**
	 * Test for {@link IFOCalculator#getTeammatesAtBall(List)}
	 */
	@Test
	public void getTeammatesAtBall()
	{
		Vector3D ballPosition = new Vector3D(2, 0, 0);
		when(ballMock.getPosition()).thenReturn(ballPosition);
		when(playerMock2.getDistanceToXY(ballPosition)).thenReturn(1.0);
		when(playerMock2.isLying()).thenReturn(false);
		when(playerMock4.getDistanceToXY(ballPosition)).thenReturn(2.0);
		when(playerMock4.isLying()).thenReturn(false);

		List<IPlayer> result = testee.getTeammatesAtBall(sourceList);
		assertEquals(2, result.size());
		assertEquals(playerMock2, result.get(0));
		assertEquals(playerMock4, result.get(1));
	}

	/**
	 * Test for {@link IFOCalculator#getTeammatesAtBall(List)}
	 */
	@Test
	public void getTeammatesAtBallLying()
	{
		Vector3D ballPosition = new Vector3D(2, 0, 0);
		when(ballMock.getPosition()).thenReturn(ballPosition);
		when(playerMock2.getDistanceToXY(ballPosition)).thenReturn(1.0);
		when(playerMock2.isLying()).thenReturn(true);
		when(playerMock4.getDistanceToXY(ballPosition)).thenReturn(2.0);
		when(playerMock4.isLying()).thenReturn(false);

		List<IPlayer> result = testee.getTeammatesAtBall(sourceList);
		assertEquals(2, result.size());
		assertEquals(playerMock4, result.get(0));
		assertEquals(playerMock2, result.get(1));
	}
}
