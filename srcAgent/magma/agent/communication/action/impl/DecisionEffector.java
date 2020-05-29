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
package magma.agent.communication.action.impl;

import java.util.Map;

import hso.autonomy.agent.communication.action.impl.Effector;
import magma.agent.communication.action.IDecisionEffector;

/**
 * The DecisionEffector holds some high level decision information.
 *
 * @author Stefan Glaser
 */
public class DecisionEffector extends Effector implements IDecisionEffector
{
	private int headCommand;

	private float headTargetYaw;

	private float headTargetPitch;

	private int movementCommand;

	private float intendedX;

	private float intendedY;

	private float intendedTurn;

	private float relBallPosX;

	private float relBallPosY;

	private float relBallTargetPosX;

	private float relBallTargetPosY;

	private int state;

	private int sayCommand;

	private Map<String, String> sayParams;

	public DecisionEffector()
	{
		super("decision");
	}

	@Override
	public void setEffectorValues(float... values)
	{
		headCommand = (int) values[0];
		headTargetYaw = values[1];
		headTargetPitch = values[2];

		movementCommand = (int) values[3];
		intendedX = values[4];
		intendedY = values[5];
		intendedTurn = values[6];
		relBallPosX = values[7];
		relBallPosY = values[8];
		relBallTargetPosX = values[9];
		relBallTargetPosY = values[10];
		state = (int) values[11];

		// System.out.println(
		// "DecisionEffector:\tHead " + headCommand + " " + headTargetYaw + " "
		// + headTargetPitch + "\t\tMovement " + movementCommand + " "
		// + intendedX + " " + intendedY + " " + intendedTurn);
	}

	public void setSayCommand(int sayCommand, Map<String, String> sayParams)
	{
		this.sayCommand = sayCommand;
		this.sayParams = sayParams;
	}

	@Override
	public int getHeadCommand()
	{
		return headCommand;
	}

	@Override
	public float getHeadTargetYaw()
	{
		return headTargetYaw;
	}

	@Override
	public float getHeadTargetPitch()
	{
		return headTargetPitch;
	}

	@Override
	public int getMovementCommand()
	{
		return movementCommand;
	}

	@Override
	public float getIntendedX()
	{
		return intendedX;
	}

	@Override
	public float getIntendedY()
	{
		return intendedY;
	}

	@Override
	public float getIntendedTurn()
	{
		return intendedTurn;
	}

	@Override
	public float getRelBallPosX()
	{
		return relBallPosX;
	}

	@Override
	public float getRelBallPosY()
	{
		return relBallPosY;
	}

	@Override
	public float getRelBallTargetPosX()
	{
		return relBallTargetPosX;
	}

	@Override
	public float getRelBallTargetPosY()
	{
		return relBallTargetPosY;
	}

	@Override
	public int getState()
	{
		return state;
	}

	@Override
	public int getSayCommand()
	{
		return sayCommand;
	}

	@Override
	public Map<String, String> getSayParams()
	{
		return sayParams;
	}
}
