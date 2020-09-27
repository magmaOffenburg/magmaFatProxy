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
package magma.monitor.worldmodel.impl;

import hso.autonomy.util.geometry.Pose3D;
import java.util.HashMap;
import java.util.Map;
import magma.monitor.worldmodel.ISoccerAgent;
import magma.monitor.worldmodel.SoccerAgentBodyPart;
import magma.util.scenegraph.IBaseNode;
import magma.util.scenegraph.IMeshNode;
import magma.util.scenegraph.IModelFiles;
import magma.util.scenegraph.ITransformNode;
import magma.util.scenegraph.NodeType;

public class SoccerAgent extends SimulationObject implements ISoccerAgent
{
	private String teamName;

	private int playerID;

	private String robotModel;

	private Map<SoccerAgentBodyPart, Pose3D> bodyPartPoses = new HashMap<>();

	public SoccerAgent(IBaseNode graphRoot, String teamName)
	{
		super(graphRoot);

		this.teamName = teamName;

		if (graphRoot != null) {
			IMeshNode meshNode = graphRoot.getNode(IMeshNode.class, "objName", IModelFiles.NAO_BODY);
			if (meshNode != null && meshNode.getMaterials() != null) {
				String[] materials = meshNode.getMaterials();
				for (String material : materials) {
					if (material.startsWith("matNum") && material.length() > 6) {
						String numberStr = material.substring(6, material.length());

						try {
							playerID = Integer.parseInt(numberStr);
						} catch (NumberFormatException e) {
							System.out.println("Error parsing playerID in: " + numberStr);
							e.printStackTrace();
						}
					}
				}
				robotModel = meshNode.getObjName();
			}

			refresh(0);
		}
	}

	@Override
	public String getTeamName()
	{
		return teamName;
	}

	public void setTeamName(String teamName)
	{
		this.teamName = teamName;
	}

	@Override
	public int getPlayerID()
	{
		return playerID;
	}

	@Override
	public void refresh(float deltaT)
	{
		if (graphRoot == null || graphRoot.getChildren() == null) {
			return;
		}

		graphRoot.getChildren()
				.stream()
				.filter(childNode -> childNode.getNodeType() == NodeType.TRANSFORM)
				.forEach(childNode -> {
					ITransformNode node = (ITransformNode) childNode;

					checkBodyPart(node, IModelFiles.NAO_HEAD, SoccerAgentBodyPart.HEAD);
					checkBodyPart(node, IModelFiles.NAO_BODY, SoccerAgentBodyPart.BODY);
					checkBodyPart(node, IModelFiles.LEFT_UPPER_ARM, SoccerAgentBodyPart.LEFT_UPPER_ARM);
					checkBodyPart(node, IModelFiles.RIGHT_UPPER_ARM, SoccerAgentBodyPart.RIGHT_UPPER_ARM);
					checkBodyPart(node, IModelFiles.LEFT_LOWER_ARM, SoccerAgentBodyPart.LEFT_LOWER_ARM);
					checkBodyPart(node, IModelFiles.RIGHT_LOWER_ARM, SoccerAgentBodyPart.RIGHT_LOWER_ARM);
					checkBodyPart(node, IModelFiles.LEFT_THIGH, SoccerAgentBodyPart.LEFT_THIGH);
					checkBodyPart(node, IModelFiles.RIGHT_THIGH, SoccerAgentBodyPart.RIGHT_THIGH);
					checkBodyPart(node, IModelFiles.LEFT_FOOT, SoccerAgentBodyPart.LEFT_FOOT);
					checkBodyPart(node, IModelFiles.RIGHT_FOOT, SoccerAgentBodyPart.RIGHT_FOOT);
				});

		IBaseNode child = graphRoot.getChildren().get(0);
		if (child.getNodeType() == NodeType.TRANSFORM) {
			position = ((ITransformNode) child).getPosition();
		}
	}

	private void checkBodyPart(ITransformNode node, String model, SoccerAgentBodyPart bodyPart)
	{
		if (hasModel(node, model)) {
			bodyPartPoses.put(bodyPart, new Pose3D(node.getPosition(), node.getOrientation()));
		}
	}

	private boolean hasModel(ITransformNode node, String model)
	{
		return node.getNode(IMeshNode.class, "objName", model) != null;
	}

	@Override
	public Pose3D getBodyPartPose(SoccerAgentBodyPart bodyPart)
	{
		return bodyPartPoses.get(bodyPart);
	}

	@Override
	public String getRobotModel()
	{
		return robotModel;
	}
}
