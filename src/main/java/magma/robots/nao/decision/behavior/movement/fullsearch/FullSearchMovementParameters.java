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
package magma.robots.nao.decision.behavior.movement.fullsearch;

import kdo.util.parameter.ParameterList;

/**
 * @author Klaus Dorer
 */
public abstract class FullSearchMovementParameters extends ParameterList
{
	public enum Param
	{
		PHASES,
		MODE,
		POS_X,
		POS_Y,
		KICK_ANGLE,
		MIN_X_KICK,
		MAX_X_KICK,
		MAX_Y_KICK, //
		TIME,
		LHYP,
		LHR,
		LHP,
		LKP,
		LFP,
		LFR,
		LTP,
		RHYP,
		RHR,
		RHP,
		RKP,
		RFP,
		RFR,
		RTP;

		public String time(int phase)
		{
			return TIME.name() + phase;
		}

		public String angle(int phase)
		{
			return name() + phase;
		}

		public String speed(int phase)
		{
			return name() + "S" + phase;
		}
	}

	private int phases;

	private int mode;

	/** the original array from which the parameters are filled */
	private double[] paramArray;

	protected FullSearchMovementParameters(double[] p)
	{
		// default, will be overwritten in setParams
		this.phases = 4;
		this.mode = 0;
		this.paramArray = p;
		setParams(p);
	}

	/**
	 * The names are assuemd to be like NON-PHASE parameters Phase parameters
	 * angles left, right if existing phase parameters speed left, right
	 *
	 * @param index the index of the parameter to get
	 * @return the name of the ith parameter
	 */
	protected String getParamKey(int index)
	{
		int numberOfNonPhaseParams = 8;
		int numberOfPhaseParams = 15;
		int numberOfParamNames = numberOfPhaseParams;
		if (mode == 0) {
			// have also speeds, time is not duplicate
			numberOfPhaseParams = 2 * numberOfPhaseParams - 1;
		}
		if (index < numberOfNonPhaseParams) {
			return Param.values()[index].name();
		}
		int indexInPhase = index - numberOfNonPhaseParams;
		int phase = indexInPhase / numberOfPhaseParams;
		int mod = indexInPhase % numberOfPhaseParams;
		String suffix = "";
		if (mode == 1 && mod > 0) {
			// we are in speed mode and it is not the time parameter
			suffix = "S";
		} else if (mode == 0 && mod >= numberOfParamNames) {
			// we are in angle and speed mode and have reached speed parameters
			suffix = "S";
			mod = mod - numberOfParamNames + 1;
		}
		return Param.values()[mod + numberOfNonPhaseParams].name() + suffix + phase;
	}

	@Override
	protected String getParamCode(String key)
	{
		if (key.equals(Param.PHASES.name()) || key.equals(Param.MODE.name()) || key.equals(Param.POS_X.name()) ||
				key.equals(Param.POS_Y.name()) || key.equals(Param.KICK_ANGLE.name()) ||
				key.equals(Param.MIN_X_KICK.name()) || key.equals(Param.MAX_X_KICK.name()) ||
				key.equals(Param.MAX_Y_KICK.name())) {
			return super.getParamCode(key);
		}

		int phase = Integer.parseInt(Character.toString(key.charAt(key.length() - 1)));
		String name = key.substring(0, key.length() - 1);
		String function = "angle";
		if (name.equals(Param.TIME.name())) {
			function = "time";
		} else if (name.endsWith("S")) {
			function = "speed";
			name = name.substring(0, name.length() - 1);
		}

		return String.format("Param.%s.%s(%d)", name, function, phase);
	}

	protected void setValues()
	{
		setParams(paramArray);
	}

	public float get(Param param)
	{
		return get(param.name());
	}

	public float time(int phase)
	{
		return get(Param.TIME.name() + phase);
	}

	public float angle(int phase, Param param)
	{
		return get(param.name() + phase);
	}

	public float speed(int phase, Param param)
	{
		return get(param.name() + "S" + phase);
	}

	public void put(Param param, float value)
	{
		put(param.name(), value);
	}

	public int getPhases()
	{
		return phases;
	}

	private void setParams(double[] p)
	{
		for (int i = 0; i < p.length; i++) {
			String name = getParamKey(i);
			put(name, (float) p[i]);
			if (name.equals(Param.PHASES.name())) {
				phases = (int) p[i];
			} else if (name.equals(Param.MODE.name())) {
				mode = (int) p[i];
			}
		}
	}
}