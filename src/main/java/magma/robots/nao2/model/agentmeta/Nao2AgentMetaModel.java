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
package magma.robots.nao2.model.agentmeta;

import hso.autonomy.agent.model.agentmeta.IBodyPartConfiguration;
import hso.autonomy.agent.model.agentmeta.IHingeJointConfiguration;
import java.util.List;
import magma.robots.nao.model.agentmeta.NaoAgentMetaModel;

public class Nao2AgentMetaModel extends NaoAgentMetaModel
{
	public static final Nao2AgentMetaModel INSTANCE = new Nao2AgentMetaModel();

	public static final String NAME = "Nao2";

	private Nao2AgentMetaModel()
	{
		super(NAME, ACTION_SCENE_HETERO + " 2");
	}

	@Override
	protected List<IBodyPartConfiguration> createBodyPartConfigs()
	{
		List<IBodyPartConfiguration> configs = super.createBodyPartConfigs();

		((IHingeJointConfiguration) getBodyPart(configs, RAnkle).getJointConfiguration()).setMaxSpeed(8.80667f);
		((IHingeJointConfiguration) getBodyPart(configs, RFoot).getJointConfiguration()).setMaxSpeed(3.47234f);

		((IHingeJointConfiguration) getBodyPart(configs, LAnkle).getJointConfiguration()).setMaxSpeed(8.80667f);
		((IHingeJointConfiguration) getBodyPart(configs, LFoot).getJointConfiguration()).setMaxSpeed(3.47234f);

		return configs;
	}
}
