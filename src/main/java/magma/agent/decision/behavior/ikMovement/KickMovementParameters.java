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
package magma.agent.decision.behavior.ikMovement;

import kdo.util.parameter.EnumParameterList;
import magma.agent.decision.behavior.base.KickDistribution;

/**
 * @author Klaus Dorer
 */
public abstract class KickMovementParameters extends EnumParameterList<KickMovementParameters.Param>
{
	protected KickDistribution distribution;

	public enum Param {
		TIME0,
		TIME1,
		TIME2,
		TIME3,
		LHP1,
		RHP1,
		RKP1,
		RFP1,
		RTP1,
		LHP2,
		LHYP1,
		LHYPS1,
		LHYPS2,
		RHYP2,
		RHP2,
		RKP2,
		RFP2,
		RTP2,
		RKPS1,
		RHPS2,
		RHYPS2,
		RFPS2,
		RROLL,
		LROLL,
		LKP2,
		LFP2,
		POS_X,
		POS_Y,
		KICK_ANGLE,
		MIN_X_OFFSET,
		RUN_TO_X,
		RUN_TO_Y,
		CANCEL_DISTANCE,
		STABILIZE_TIME
	}

	public KickMovementParameters()
	{
		super(Param.class);
	}

	@Override
	protected void setValues()
	{
		put(Param.TIME0, 10.0f);
		put(Param.TIME1, 4.0f);
		put(Param.TIME2, 5.0f);
		put(Param.TIME3, 0.0f);
		put(Param.LHP1, 3.0f);
		put(Param.RHP1, -20.0f);
		put(Param.RKP1, -72.0f);
		put(Param.RFP1, 30.0f);
		put(Param.RTP1, 0);
		put(Param.LHP2, -16.0f);
		put(Param.LHYP1, 0f);
		put(Param.LHYPS1, 3f);
		put(Param.LHYPS2, 3f);
		put(Param.LHP2, -16.0f);
		put(Param.LHP2, -16.0f);
		put(Param.RHYP2, -58.0f);
		put(Param.RHP2, 61.0f);
		put(Param.RKP2, 6.0f);
		put(Param.RFP2, -27.0f);
		put(Param.RTP2, 0);
		put(Param.RKPS1, 5.0f);
		put(Param.RHPS2, 3.0f);
		put(Param.RHYPS2, 0.0f);
		put(Param.RFPS2, 4.2f);
		put(Param.RROLL, -10.0f);
		put(Param.LROLL, 12.0f);
		put(Param.LKP2, -1.5f);
		put(Param.LFP2, -1.6f);
		put(Param.POS_X, 0.13f);
		put(Param.POS_Y, -0.10f);
		put(Param.KICK_ANGLE, -8);
		put(Param.MIN_X_OFFSET, 0.13f);
		put(Param.RUN_TO_X, -0.15f);
		put(Param.RUN_TO_Y, 0.025f);
		put(Param.CANCEL_DISTANCE, 0.225f);
		put(Param.STABILIZE_TIME, 12);
	}

	final public int getTime0()
	{
		return (int) get(Param.TIME0);
	}

	final public int getTime1()
	{
		return (int) get(Param.TIME1);
	}

	final public int getTime2()
	{
		return (int) get(Param.TIME2);
	}

	final public int getTime3()
	{
		return (int) get(Param.TIME3);
	}

	final public float getLHP1()
	{
		return get(Param.LHP1);
	}

	final public float getRHP1()
	{
		return get(Param.RHP1);
	}

	final public float getRKP1()
	{
		return get(Param.RKP1);
	}

	final public float getRFP1()
	{
		return get(Param.RFP1);
	}

	final public float getRTP1()
	{
		return get(Param.RTP1);
	}

	final public float getLHP2()
	{
		return get(Param.LHP2);
	}

	final public float getLHYP1()
	{
		return get(Param.LHYP1);
	}

	final public float getLHYPS1()
	{
		return get(Param.LHYPS1);
	}

	final public float getLHYPS2()
	{
		return get(Param.LHYPS2);
	}

	final public float getRHYP2()
	{
		return get(Param.RHYP2);
	}

	final public float getRHP2()
	{
		return get(Param.RHP2);
	}

	final public float getRKP2()
	{
		return get(Param.RKP2);
	}

	final public float getRFP2()
	{
		return get(Param.RFP2);
	}

	final public float getRTP2()
	{
		return get(Param.RTP2);
	}

	final public float getRKPS1()
	{
		return get(Param.RKPS1);
	}

	final public float getRHPS2()
	{
		return get(Param.RHPS2);
	}

	final public float getRHYPS2()
	{
		return get(Param.RHYPS2);
	}

	final public float getRFPS2()
	{
		return get(Param.RFPS2);
	}

	final public float getRRoll()
	{
		return get(Param.RROLL);
	}

	final public float getLRoll()
	{
		return get(Param.LROLL);
	}

	final public float getPosX()
	{
		return get(Param.POS_X);
	}

	final public float getPosY()
	{
		return get(Param.POS_Y);
	}

	final public float getKickAngle()
	{
		return get(Param.KICK_ANGLE);
	}

	final public float getLKP2()
	{
		return get(Param.LKP2);
	}

	final public float getLFP2()
	{
		return get(Param.LFP2);
	}

	public KickDistribution getDistribution()
	{
		return distribution;
	}
}
