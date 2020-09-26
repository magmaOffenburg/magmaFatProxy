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
package magma.agent.model.agentmodel.impl;

import static org.junit.Assert.assertEquals;

import hso.autonomy.agent.model.agentmeta.impl.HingeJointConfiguration;
import hso.autonomy.agent.model.agentmodel.IAgentModel;
import hso.autonomy.agent.model.agentmodel.impl.AgentModel;
import hso.autonomy.agent.model.agentmodel.impl.HingeJoint;
import hso.autonomy.util.file.SerializationUtil;
import magma.robots.nao.model.agentmeta.NaoAgentMetaModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for Serialization of agent model
 * @author Klaus Dorer
 */
public class SerializationTest
{
	private HingeJoint testee1;

	@Before
	public void setUp() throws Exception
	{
		testee1 = new HingeJoint(
				new HingeJointConfiguration("test", "test", "test", new Vector3D(1, 0, 0), -120, 120, 7, false));
	}

	@Test
	public void testHingeJointSerialization() throws Exception
	{
		HingeJoint result = (HingeJoint) SerializationUtil.doubleSerialize(testee1);
		assertEquals(testee1, result);
	}

	@Test
	public void testAgentModelSerialization() throws Exception
	{
		IAgentModel model = new AgentModel(NaoAgentMetaModel.INSTANCE, null);
		IAgentModel result = (IAgentModel) SerializationUtil.doubleSerialize(model);
		assertEquals(result, model);
	}
}
