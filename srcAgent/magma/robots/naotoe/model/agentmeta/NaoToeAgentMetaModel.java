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
package magma.robots.naotoe.model.agentmeta;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import hso.autonomy.agent.model.agentmeta.IBodyPartConfiguration;
import hso.autonomy.agent.model.agentmeta.impl.BodyPartConfiguration;
import hso.autonomy.agent.model.agentmeta.impl.HingeJointConfiguration;
import hso.autonomy.agent.model.agentmeta.impl.SensorConfiguration;
import magma.robots.nao.model.agentmeta.NaoAgentMetaModel;

public class NaoToeAgentMetaModel extends NaoAgentMetaModel
{
	public static final NaoToeAgentMetaModel INSTANCE = new NaoToeAgentMetaModel();

	public static final String NAME = "NaoToe";

	private NaoToeAgentMetaModel()
	{
		super(NAME, ACTION_SCENE_HETERO_TOE + " 4");
	}

	@Override
	protected List<IBodyPartConfiguration> createBodyPartConfigs()
	{
		List<IBodyPartConfiguration> configs = super.createBodyPartConfigs();

		getBodyPart(configs, RFoot)
				.setTranslation(new Vector3D(0, 0.01, -0.035))
				.setMass(0.15f)
				.setGeometry(new Vector3D(0.08, 0.12, 0.02))
				.setJointAnchor(new Vector3D(0, -0.01, 0.035));

		configs.add(new BodyPartConfiguration(RToe, RFoot, new Vector3D(0, 0.08, -0.005), 0.05f,
				new Vector3D(0.08, 0.04, 0.01),
				new HingeJointConfiguration(RToePitch, RToesP, RToesE, Vector3D.PLUS_I, -1, 70, MAX_JOINT_SPEED, true),
				new Vector3D(0, -0.02, -0.005), null, null, new SensorConfiguration("RToeForce", "rtf"), null, null));

		getBodyPart(configs, LFoot)
				.setTranslation(new Vector3D(0, 0.01, -0.035))
				.setMass(0.15f)
				.setGeometry(new Vector3D(0.08, 0.12, 0.02))
				.setJointAnchor(new Vector3D(0, -0.01, 0.035));

		configs.add(new BodyPartConfiguration(LToe, LFoot, new Vector3D(0, 0.08, -0.005), 0.05f,
				new Vector3D(0.08, 0.04, 0.01),
				new HingeJointConfiguration(LToePitch, LToesP, LToesE, Vector3D.PLUS_I, -1, 70, MAX_JOINT_SPEED, true),
				new Vector3D(0, -0.02, -0.005), null, null, new SensorConfiguration("LToeForce", "ltf"), null, null));

		return configs;
	}
}
