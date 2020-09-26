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
package magma.robots.nao1.model.agentmeta;

import hso.autonomy.agent.model.agentmeta.IBodyPartConfiguration;
import java.util.List;
import magma.robots.nao.model.agentmeta.NaoAgentMetaModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Nao1AgentMetaModel extends NaoAgentMetaModel
{
	public static final Nao1AgentMetaModel INSTANCE = new Nao1AgentMetaModel();

	public static final String NAME = "Nao1";

	private Nao1AgentMetaModel()
	{
		super(NAME, ACTION_SCENE_HETERO + " 1");
	}

	@Override
	protected List<IBodyPartConfiguration> createBodyPartConfigs()
	{
		List<IBodyPartConfiguration> configs = super.createBodyPartConfigs();

		getBodyPart(configs, RElbow).setTranslation(new Vector3D(-0.01, 0.10664, 0.009));
		getBodyPart(configs, RThight)
				.setTranslation(new Vector3D(0, 0.01, -0.05832f))
				.setJointAnchor(new Vector3D(0, -0.01, 0.05832f));
		getBodyPart(configs, RAnkle).setTranslation(new Vector3D(0, -0.01, -0.07332));

		getBodyPart(configs, LElbow).setTranslation(new Vector3D(0.01, 0.10664, 0.009));
		getBodyPart(configs, LThight)
				.setTranslation(new Vector3D(0, 0.01, -0.05832f))
				.setJointAnchor(new Vector3D(0, -0.01, 0.05832f));
		getBodyPart(configs, LAnkle).setTranslation(new Vector3D(0, -0.01, -0.07332));

		return configs;
	}
}
