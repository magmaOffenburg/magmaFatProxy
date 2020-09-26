/*******************************************************************************
 * Copyright 2008 - 2020 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under MIT License (see LICENSE).
 *******************************************************************************/
package hso.autonomy.agent.model.agentmodel.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hso.autonomy.agent.communication.action.IEffector;
import hso.autonomy.agent.communication.perception.IAccelerometerPerceptor;
import hso.autonomy.agent.communication.perception.IGyroPerceptor;
import hso.autonomy.agent.communication.perception.IHingeJointPerceptor;
import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.agent.model.agentmeta.IBodyPartConfiguration;
import hso.autonomy.agent.model.agentmeta.impl.AgentMetaModel;
import hso.autonomy.agent.model.agentmeta.impl.BodyPartConfiguration;
import hso.autonomy.agent.model.agentmeta.impl.HingeJointConfiguration;
import hso.autonomy.agent.model.agentmeta.impl.SensorConfiguration;
import hso.autonomy.agent.model.agentmodel.IBodyModel;
import hso.autonomy.util.geometry.Pose3D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author dorer
 */
public class BodyModelTest
{
	private BodyModel testee;

	@Before
	public void setUp() throws Exception
	{
		AgentMetaModel metaModel2 = new AgentMetaModel("name", null, new Pose3D(), 0) {
			@Override
			protected List<IBodyPartConfiguration> createBodyPartConfigs()
			{
				return getTestBodyPartConfig();
			}

			@Override
			public Map<String, IEffector> createEffectors()
			{
				return new HashMap<String, IEffector>();
			}
		};
		testee = new BodyModel(metaModel2, null);
	}

	/**
	 * Test method for
	 * {@link
	 * hso.autonomy.agent.model.agentmodel.impl.BodyModel#BodyModel(magma.agent.meta.agent.IAgentMetaModel)}
	 * .
	 */
	@Test
	public void testBodyModelIAgentMetaModel()
	{
		assertEquals("torso", testee.getRootBodyPart().getName());
		assertEquals(1, testee.getRootBodyPart().getNoOfChildren());
		assertSame(testee.getRootBodyPart().getJoint(), testee.getJoint("torso"));
		assertEquals("TorsoGyro", testee.getSensor("TorsoGyro").getName());
	}

	/**
	 * Test method for
	 * {@link
	 * hso.autonomy.agent.model.agentmodel.impl.BodyModel#BodyModel(hso.autonomy.agent.model.agentmodel.impl.BodyModel)}
	 * .
	 */
	@Test
	public void testBodyModelBodyModel()
	{
		IBodyModel copy = new BodyModel(testee);
		assertNotSame(copy.getRootBodyPart(), testee.getRootBodyPart());
		assertEquals(1, copy.getRootBodyPart().getNoOfChildren());
		assertEquals(testee.getSensor("TorsoGyro"), copy.getSensor("TorsoGyro"));
	}

	/**
	 * Test method for
	 * {@link hso.autonomy.agent.model.agentmodel.impl.BodyModel#getJoint(java.lang.String)} .
	 */
	@Test
	public void testGetJoint()
	{
		assertEquals("joint1", testee.getJoint("joint1").getName());
	}

	/**
	 * Test method for
	 * {@link
	 * hso.autonomy.agent.model.agentmodel.impl.BodyModel#generateJointActions(magma.agent.action.IAction)}
	 * .
	 */
	@Test
	public void testGenerateJointActions()
	{
		Map<String, float[]> actions = new HashMap<>();
		testee.generateJointActions(actions);
		assertEquals(1, actions.size());
		float[] values = actions.get("he1");
		assertEquals(1, actions.size());
		assertEquals(0, values[0], 0.0001);
		assertEquals(0, values[1], 0.0001);
	}

	@Test
	public void testUpdateFromPerception()
	{
		IPerception perception = mock(IPerception.class);
		IHingeJointPerceptor hingePerceptor = mock(IHingeJointPerceptor.class);
		when(hingePerceptor.getAxis()).thenReturn(20.0f);

		IGyroPerceptor gyroPerceptor = mock(IGyroPerceptor.class);
		when(gyroPerceptor.getGyro()).thenReturn(new Vector3D(1, 2, 3));

		IAccelerometerPerceptor accPerceptor = mock(IAccelerometerPerceptor.class);
		when(accPerceptor.getAcceleration()).thenReturn(new Vector3D(1, 2, 3));

		when(perception.getHingeJointPerceptor("hj1")).thenReturn(hingePerceptor);
		when(perception.getGyroRatePerceptor("torsoGyro")).thenReturn(gyroPerceptor);
		when(perception.getAccelerationPerceptor("torsoAccel")).thenReturn(accPerceptor);

		testee.updateFromPerception(perception);

		HingeJoint hingeJoint = (HingeJoint) testee.getJoint("joint1");
		assertEquals(20.0f, hingeJoint.getAngle(), 0.0001);
		assertEquals(0.0f, hingeJoint.getNextAxisSpeed(), 0.0001);
		// TODO switch these three lines on once the miraculous factor 2 in
		// performAxisPosition is removed
		// hingeJoint.performAxisPosition(25);
		// assertEquals(25.0f, hingeJoint.getAxis(), 0.0001);
		// assertEquals(5.0f, hingeJoint.getNextAxisSpeed(), 0.0001);
	}

	private List<IBodyPartConfiguration> getTestBodyPartConfig()
	{
		List<IBodyPartConfiguration> configs = new ArrayList<>();

		IBodyPartConfiguration currentConfig;

		currentConfig =
				new BodyPartConfiguration("torso", null, new Vector3D(0, 0, 0), 1.2171f, new Vector3D(0.1, 0.1, 0.18),
						null, new Vector3D(0, 0, 0), new SensorConfiguration("TorsoGyro", "torsoGyro"),
						new SensorConfiguration("TorsoAccel", "torsoAccel"), null, null, null);
		configs.add(currentConfig);

		currentConfig = new BodyPartConfiguration("neck", "torso", new Vector3D(0, 0, 0.09), 0.05f,
				new Vector3D(0.03, 0.03, 0.08),
				new HingeJointConfiguration("joint1", "hj1", "he1", new Vector3D(0, 0, 1), -120, 120, 7, false),
				new Vector3D(0, 0, 0), null, null, null, null, null);
		configs.add(currentConfig);

		return configs;
	}

	/**
	 * Test method for
	 * {@link
	 * hso.autonomy.agent.model.agentmodel.impl.BodyModel#BodyModel(hso.autonomy.agent.model.agentmodel.impl.BodyModel)}
	 * .
	 */
	@Test
	public void testUpdateJointSpeed()
	{
		BodyModel copy = new BodyModel(testee);

		HingeJoint hj1 = (HingeJoint) copy.getJoint("joint1");
		hj1.performAxisSpeed(3.0f);
		testee.updateJointsSpeed(copy);

		HingeJoint hj1Source = (HingeJoint) testee.getJoint("joint1");
		assertEquals(3.0f, hj1Source.getNextAxisSpeed(), 0.00001);
	}
}
