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

import static org.junit.jupiter.api.Assertions.assertEquals;

import hso.autonomy.agent.model.worldmodel.IWorldModel;
import hso.autonomy.util.file.SerializationUtil;
import magma.agent.model.agentmodel.impl.RoboCupAgentModel;
import magma.agent.model.worldmeta.impl.RCServerMetaModelV63;
import magma.agent.model.worldmodel.IBall;
import magma.robots.nao.model.agentmeta.NaoAgentMetaModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.Test;

/**
 * Tests for Serialization of world model
 * @author Klaus Dorer
 */
public class SerializationTest
{
	/**
	 * Test for {@link SerializationUtil#doubleSerialize(Object)}
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testBallSerialization() throws Exception
	{
		Vector3D localPos = new Vector3D(1.0, 2.0, 3.0);
		Vector3D globalPos = new Vector3D(4.0, -2.0, 1.0);
		Ball testee = new Ball(0.042f, .94f, Vector3D.ZERO, IBall.COLLISION_DISTANCE, 0.02f);
		testee.updateFromVision(Vector3D.ZERO, localPos, globalPos, 1.0f);

		Ball result = (Ball) SerializationUtil.doubleSerialize(testee);
		assertEquals(testee, result);
	}

	/**
	 * Test for {@link SerializationUtil#doubleSerialize(Object)}
	 *
	 * @throws Exception In case of fatal error
	 */
	@Test
	public void testWorldModelSerialization() throws Exception
	{
		IWorldModel testee = new RoboCupWorldModel(new RoboCupAgentModel(NaoAgentMetaModel.INSTANCE, null), null,
				RCServerMetaModelV63.INSTANCE, "team", 0);
		IWorldModel result = (IWorldModel) SerializationUtil.doubleSerialize(testee);
		assertEquals(testee, result);
	}
}
