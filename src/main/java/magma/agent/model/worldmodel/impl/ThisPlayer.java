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

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Area2D;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose3D;
import magma.agent.model.worldmodel.IThisPlayer;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Container for this agent's information that is unique to the player this
 * agent is representing
 *
 * @author Klaus Dorer, Stefan Glaser
 */
public class ThisPlayer extends Player implements IThisPlayer
{
	/** manager for desired positions */
	private final PositionManager positionManager;

	/** the orientation of the player relative to the local coordinate system */
	private transient Rotation localOrientation;

	/** intended global speed in m/cycle */
	private Vector3D intendedGlobalSpeed;

	public ThisPlayer(String teamname, int id, float cycleTime, float torsoZUpright)
	{
		super(id, teamname, true, cycleTime);
		setIntendedGlobalSpeed(Vector3D.ZERO);
		position = new Vector3D(0, 0, torsoZUpright);
		positionManager = new PositionManager();
	}

	@Override
	public void setGlobalOrientation(Rotation orientation)
	{
		// Rotation noisyOrientation = addNoiseToGyro(orientation);
		// super.setGlobalOrientation(noisyOrientation);
		super.setGlobalOrientation(orientation);
		localOrientation = Geometry.zTransformRotation(globalOrientation, Math.PI / 2);

		// this method is called each cycle so we reset intended speed here
		// it will be set again by the walk behavior each cycle
		setIntendedGlobalSpeed(Vector3D.ZERO);
	}

	@Override
	public Rotation getOrientation()
	{
		// Handle not serialized localOrientation
		if (localOrientation == null) {
			return Geometry.zTransformRotation(globalOrientation, Math.PI / 2);
		}
		return localOrientation;
	}

	@Override
	public boolean isLying()
	{
		return getOrientation().getMatrix()[2][2] < 0.3;
	}

	@Override
	public boolean isLyingOnBack()
	{
		return getOrientation().getMatrix()[2][1] > 0.75;
	}

	@Override
	public boolean isLyingOnFront()
	{
		double[][] orientation = getOrientation().getMatrix();
		return orientation[2][1] < -0.75 || (orientation[2][1] < 0 && orientation[2][2] < -0.3);
	}

	@Override
	public boolean isLeaningToSide()
	{
		double[][] orientation = getOrientation().getMatrix();
		return orientation[2][0] > 0.7 || orientation[2][0] < -0.7;
	}

	@Override
	public boolean isInHandStand()
	{
		return getOrientation().getMatrix()[2][2] < -0.5;
	}

	@Override
	public Vector3D calculateGlobalPosition(Vector3D localPos)
	{
		return position.add(globalOrientation.applyTo(localPos));
	}

	@Override
	public Vector3D calculateGlobal2DPosition(Vector3D localPos)
	{
		return position.add(Geometry.createZRotation(getHorizontalAngle().radians()).applyTo(localPos));
	}

	@Override
	public Pose2D calculateGlobalBodyPose2D(Pose3D poseToTranslate)
	{
		return new Pose2D( //
				calculateGlobalPosition(new Vector3D(poseToTranslate.getY(), -poseToTranslate.getX(),
						poseToTranslate.getZ())), //
				Angle.rad(-Geometry.getTopViewZAngle(getOrientation().applyTo(poseToTranslate.getOrientation()))));
	}

	@Override
	public Pose3D calculateGlobalBodyPose(Pose3D poseToTranslate)
	{
		return getPose().applyTo(Geometry.bodyToWorld(poseToTranslate));
	}

	@Override
	public Vector3D calculateLocalPosition(Vector3D globalPosition)
	{
		Vector3D localPos = globalPosition.subtract(position);
		return globalOrientation.applyInverseTo(localPos);
	}

	/**
	 * Returns the relative angle (rad) this player's torso has to the specified
	 * position
	 * @param position the position to which to calculate the body angle
	 * @return the relative angle (rad) this player's torso has to the specified
	 *         position
	 */
	@Override
	public Angle getBodyDirectionTo(Vector3D position)
	{
		return getDirectionTo(position).subtract(getHorizontalAngle());
	}

	@Override
	public boolean isInsideArea(Vector3D absolutePosition, Area2D.Float area)
	{
		Vector3D localPosition = calculateLocalPosition(absolutePosition);
		return area.contains(localPosition);
	}

	/**
	 * @param absolutePosition the position to check
	 * @return true if the specified position is left of this player with respect
	 *         to the root body orientation
	 */
	@Override
	public boolean positionIsLeft(Vector3D absolutePosition)
	{
		return calculateLocalPosition(absolutePosition).getY() >= 0;
	}

	/**
	 * @param absolutePosition the position to check
	 * @return true if the specified position is right of this player with
	 *         respect to the root body orientation
	 */
	@Override
	public boolean positionIsRight(Vector3D absolutePosition)
	{
		return calculateLocalPosition(absolutePosition).getY() <= 0;
	}

	/**
	 * @param absolutePosition the position to check
	 * @return true if the specified position is behind this player with respect
	 *         to the root body orientation
	 */
	@Override
	public boolean positionIsBehind(Vector3D absolutePosition)
	{
		return calculateLocalPosition(absolutePosition).getX() <= 0;
	}

	@Override
	public PositionManager getPositionManager()
	{
		return positionManager;
	}

	@Override
	public double getMaxSpeed()
	{
		return 0.85;
	}

	public Pose3D getPose()
	{
		return new Pose3D(position, globalOrientation);
	}

	public IPose2D getPose2D()
	{
		return new Pose2D(position, getHorizontalAngle());
	}

	@Override
	public void setGlobalPosition(Vector3D position, float time)
	{
		updateFromAudio(Vector3D.ZERO, position, time);
	}

	@Override
	public Vector3D getIntendedGlobalSpeed()
	{
		return intendedGlobalSpeed;
	}

	@Override
	public void setIntendedGlobalSpeed(Vector3D intendedGlobalSpeed)
	{
		// convert from local to global coordinate frame
		Vector3D localSpeed = Geometry.createZRotation(getHorizontalAngle().radians()).applyTo(intendedGlobalSpeed);
		this.intendedGlobalSpeed = localSpeed;
	}
}
