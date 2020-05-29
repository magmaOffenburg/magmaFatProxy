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

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import hso.autonomy.agent.communication.action.IAction;
import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.agent.model.agentmeta.IAgentMetaModel;
import hso.autonomy.agent.model.agentmodel.IBodyPart;
import hso.autonomy.agent.model.agentmodel.impl.Accelerometer;
import hso.autonomy.agent.model.agentmodel.impl.BodyModel;
import hso.autonomy.agent.model.agentmodel.impl.ik.IAgentIKSolver;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose3D;
import hso.autonomy.util.misc.FuzzyCompare;
import magma.agent.IHumanoidConstants;
import magma.agent.communication.action.IRoboCupAction;
import magma.agent.model.agentmodel.IRoboCupBodyModel;

/**
 * Represents the body with its body parts and joints
 * @author dorer
 */
class RoboCupBodyModel extends BodyModel implements IRoboCupBodyModel
{
	/** the next say message to the server */
	protected String sayMessage;

	/** the next beam position */
	protected Pose2D beamPose;

	/** indicator if beam action has been performed and should be reset */
	private transient boolean beamPerformed;

	/**
	 * Constructor to create the body model from a passed meta model
	 * @param metaModel the meta model containing body part information
	 */
	RoboCupBodyModel(IAgentMetaModel metaModel, IAgentIKSolver ikSolver)
	{
		super(metaModel, ikSolver);
		sayMessage = null;
		beamPose = null;
		beamPerformed = false;
	}

	/**
	 * Copy constructor
	 * @param source the source body model to copy
	 */
	RoboCupBodyModel(RoboCupBodyModel source)
	{
		super(source);
		sayMessage = source.sayMessage;
		if (source.beamPose == null) {
			beamPose = null;
		} else {
			beamPose = source.beamPose;
		}
	}

	/**
	 * Creates the action content for sending to the server
	 */
	@Override
	protected void generateTargetStateToAction(IAction action)
	{
		IRoboCupAction theAction = (IRoboCupAction) action;

		// compared to earlier versions the shutoff action is now generated after beam action
		generateBeamAction(theAction);

		super.generateTargetStateToAction(action);

		generateSayAction(theAction);
	}

	/**
	 * Called to forward the beam action.
	 */
	void generateBeamAction(IRoboCupAction action)
	{
		if (beamPose != null) {
			action.sendBeamString(beamPose);
			beamPerformed = true;
		}
	}

	void beamToPosition(Pose2D pose)
	{
		beamPose = pose;
	}

	/**
	 * Called to forward the say message.
	 */
	void generateSayAction(IRoboCupAction action)
	{
		if (sayMessage != null) {
			action.sayMessage(sayMessage);
		}
	}

	void sayMessage(String message)
	{
		sayMessage = message;
	}

	/**
	 * Updates the joint values in the body model from perception
	 * @param perception the new perception we made
	 */
	@Override
	protected void updateFromPerception(IPerception perception)
	{
		// reset actions
		sayMessage = null;
		if (beamPerformed) {
			beamPose = null;
			beamPerformed = false;
		}

		super.updateFromPerception(perception);
	}

	/**
	 * Updates the joint values in the body model if not connected to perception
	 */
	@Override
	protected void updateNoPerception()
	{
		// reset actions
		sayMessage = null;
		if (beamPerformed) {
			beamPose = null;
			beamPerformed = false;
		}

		super.updateNoPerception();
	}

	@Override
	public Vector3D getCenterOfGravity()
	{
		// TODO: Remove dependency to Nao robot model
		Vector3D acc =
				((Accelerometer) rootBodyPart.getSensorDeep(IHumanoidConstants.TorsoAccelerometer)).getAcceleration();

		double distToGroundLeft = getBodyPart(IHumanoidConstants.LFoot).getPosition().getNorm();
		double distToGroundRight = getBodyPart(IHumanoidConstants.RFoot).getPosition().getNorm();

		double distToExpectedGround;
		if (FuzzyCompare.gt(distToGroundLeft, distToGroundRight, 4)) {
			distToExpectedGround = distToGroundLeft;
		} else if (FuzzyCompare.lt(distToGroundLeft, distToGroundRight, 4)) {
			distToExpectedGround = distToGroundRight;
		} else {
			distToExpectedGround = (distToGroundRight + distToGroundLeft) / 2;
		}

		if (FuzzyCompare.eq(0, acc.getNorm(), 0.00001)) {
			return getCenterOfMass();
		}

		acc = acc.negate();

		acc = acc.normalize();

		acc = acc.scalarMultiply(distToExpectedGround);

		acc = acc.add(getCenterOfMass());

		double x = Double.isNaN(acc.getX()) ? 0 : acc.getX();
		double y = Double.isNaN(acc.getY()) ? 0 : acc.getY();
		double z = Double.isNaN(acc.getZ()) ? 0 : acc.getZ();

		return new Vector3D(x, y, z);
	}

	@Override
	public Vector3D[] getStabilityHull()
	{
		// TODO: Remove dependency to Nao robot model
		IBodyPart lfoot = rootBodyPart.getBodyPart(IHumanoidConstants.LFoot);
		Pose3D lPose = lfoot.getPose();
		Vector3D lPos = lPose.getPosition();
		Rotation lOrientation = lPose.getOrientation();
		Vector3D lHalfSize = lfoot.getGeometry().scalarMultiply(0.5);
		IBodyPart rfoot = rootBodyPart.getBodyPart(IHumanoidConstants.RFoot);
		Pose3D rPose = rfoot.getPose();
		Vector3D rPos = rPose.getPosition();
		Rotation rOrientation = rPose.getOrientation();
		int index = 0;

		Vector3D leftFrontCorner = new Vector3D(-lHalfSize.getX(), lHalfSize.getY(), -lHalfSize.getZ());
		Vector3D rightFrontCorner = new Vector3D(lHalfSize.getX(), lHalfSize.getY(), -lHalfSize.getZ());
		Vector3D rightBackCorner = new Vector3D(lHalfSize.getX(), -lHalfSize.getY(), -lHalfSize.getZ());
		Vector3D leftBackCorner = new Vector3D(-lHalfSize.getX(), -lHalfSize.getY(), -lHalfSize.getZ());

		Vector3D[] positions = new Vector3D[6];

		positions[index] = lPos.add(lOrientation.applyTo(leftFrontCorner));
		index++;
		if (lPos.getY() > rPos.getY()) {
			positions[index] = lPos.add(lOrientation.applyTo(rightFrontCorner));
			index++;
		} else {
			// if (rPos.getY() > lPos.getY()) {
			positions[index] = rPos.add(rOrientation.applyTo(leftFrontCorner));
			index++;
		}
		positions[index] = rPos.add(rOrientation.applyTo(rightFrontCorner));
		index++;
		positions[index] = rPos.add(rOrientation.applyTo(rightBackCorner));
		index++;
		if (lPos.getY() > rPos.getY()) {
			positions[index] = rPos.add(rOrientation.applyTo(leftBackCorner));
			index++;
		} else {
			// if (rPos.getY() > lPos.getY()) {
			positions[index] = lPos.add(lOrientation.applyTo(rightBackCorner));
			index++;
		}
		positions[index] = lPos.add(lOrientation.applyTo(leftBackCorner));

		// Vector3D[] lFoot = getCorners(torso.getBodyPart(INaoConstants.LFoot));
		// Vector3D[] rFoot = getCorners(torso.getBodyPart(INaoConstants.RFoot));
		//
		// positions[0] = lFoot[0];
		// positions[1] = rFoot[1];
		//
		// positions[2] = rFoot[2];
		// positions[3] = lFoot[3];

		return positions;
	}
}
