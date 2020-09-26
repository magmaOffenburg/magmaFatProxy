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
package magma.agent.decision.behavior.movement;

import hso.autonomy.agent.model.agentmodel.IAgentModel;
import hso.autonomy.agent.model.agentmodel.IHingeJoint;
import java.io.Serializable;

/**
 * A single joint movement specification
 * @author dorer
 */
public class MovementSingle implements Serializable
{
	/** name of the joint to move */
	private String jointName;

	/** the angle to which to move (in degrees) */
	private double jointAngle;

	/** the speed with which to move (in degrees / cycle) */
	private float speed;

	/**
	 * @param jointName name of the joint to move
	 * @param jointAngle the angle to which to move (in degrees)
	 * @param speed the speed with which to move (in degrees / cycle)
	 */
	public MovementSingle(String jointName, double jointAngle, float speed)
	{
		this.jointName = jointName;
		this.jointAngle = jointAngle;
		this.speed = speed;
	}

	/**
	 * @param agentModel the model with the joints
	 * @return true if the joint has reached its desired angle
	 */
	public boolean move(IAgentModel agentModel, float relativeSpeed)
	{
		IHingeJoint writeableHJ = agentModel.getWriteableHJ(jointName);
		if (writeableHJ == null) {
			System.err.println("No such joint: " + jointName);
			return false;
		}
		float speedUsed = writeableHJ.performAxisPosition(jointAngle, speed * 1.0f / relativeSpeed);
		return Math.abs(speedUsed) <= 0.01;
	}

	/**
	 * @return a left handed version of this single movement
	 */
	public MovementSingle getLeftVersion()
	{
		String newName = jointName;
		if (newName.startsWith("R")) {
			newName = "L" + newName.substring(1);
		} else if (newName.startsWith("L")) {
			newName = "R" + newName.substring(1);
		}

		double newAngle = jointAngle;
		if (newName.contains("Roll")) {
			newAngle = -newAngle;
		}
		return new MovementSingle(newName, newAngle, speed);
	}

	public String getJointName()
	{
		return jointName;
	}

	public double getJointAngle()
	{
		return jointAngle;
	}

	public float getSpeed()
	{
		return speed;
	}

	public void setSpeed(float speed)
	{
		this.speed = speed;
	}

	@Override
	public String toString()
	{
		return jointName + " angle: " + jointAngle + " speed: " + speed;
	}
}
