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

import static org.junit.Assert.assertEquals;

import hso.autonomy.agent.model.worldmodel.IVisibleObject;
import hso.autonomy.agent.model.worldmodel.impl.VisibleObject;
import magma.agent.model.worldmodel.IBall;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Klaus Dorer
 *
 */
public class BallTest
{
	private VisibleObject testee;

	private ThisPlayer thisPlayer;

	@Before
	public void setUp()
	{
		testee = new Ball(0.042f, .94f, Vector3D.ZERO, IBall.COLLISION_DISTANCE, 0.02f);
		thisPlayer = new ThisPlayer("testTeam", -1, 0.02f, 0.4f);
	}

	/**
	 * Test method for
	 * {@link
	 * hso.autonomy.agent.model.worldmodel.impl.VisibleObject#getDirectionTo(IVisibleObject)}
	 * .
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testGetDirectionTo() throws Exception
	{
		thisPlayer.setPosition(new Vector3D(1.0, 1.0, 0.0));
		testee.setPosition(new Vector3D(2.0, 2.0, 0.0));

		assertEquals(-135.0, testee.getDirectionTo(thisPlayer).degrees(), 0.0001);
		assertEquals(45.0, thisPlayer.getDirectionTo(testee).degrees(), 0.0001);
	}
}
