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
package magma.monitor.messageparser.impl;

import java.util.ArrayList;
import java.util.List;
import magma.common.spark.Foul;
import magma.monitor.messageparser.ISimulationState;

public class SimulationState implements ISimulationState
{
	private Float fieldLength;

	private Float fieldWidth;

	private Float fieldHeight;

	private Float goalWidth;

	private Float goalDepth;

	private Float goalHeight;

	private Float borderSize;

	private Float freeKickDistance;

	private Float waitBeforeKickOff;

	private Float agentRadius;

	private Float ballRadius;

	private Float ballMass;

	private Float ruleGoalPauseTime;

	private Float ruleKickInPauseTime;

	private Float ruleHalfTime;

	private String[] playModes;

	private Float time;

	private String leftTeam;

	private String rightTeam;

	private Integer half;

	private Integer leftScore;

	private Integer rightScore;

	private Integer playMode;

	private List<Foul> fouls = new ArrayList<>();

	@Override
	public Float getFieldLength()
	{
		return fieldLength;
	}

	@Override
	public Float getFieldWidth()
	{
		return fieldWidth;
	}

	@Override
	public Float getFieldHeight()
	{
		return fieldHeight;
	}

	@Override
	public Float getGoalWidth()
	{
		return goalWidth;
	}

	@Override
	public Float getGoalDepth()
	{
		return goalDepth;
	}

	@Override
	public Float getGoalHeight()
	{
		return goalHeight;
	}

	@Override
	public Float getBorderSize()
	{
		return borderSize;
	}

	@Override
	public Float getFreeKickDistance()
	{
		return freeKickDistance;
	}

	@Override
	public Float getWaitBeforeKickOff()
	{
		return waitBeforeKickOff;
	}

	@Override
	public Float getAgentRadius()
	{
		return agentRadius;
	}

	@Override
	public Float getBallRadius()
	{
		return ballRadius;
	}

	@Override
	public Float getBallMass()
	{
		return ballMass;
	}

	@Override
	public Float getRuleGoalPauseTime()
	{
		return ruleGoalPauseTime;
	}

	@Override
	public Float getRuleKickInPauseTime()
	{
		return ruleKickInPauseTime;
	}

	@Override
	public Float getRuleHalfTime()
	{
		return ruleHalfTime;
	}

	@Override
	public String[] getPlayModes()
	{
		return playModes;
	}

	@Override
	public Float getTime()
	{
		return time;
	}

	@Override
	public String getLeftTeam()
	{
		return leftTeam;
	}

	@Override
	public String getRightTeam()
	{
		return rightTeam;
	}

	@Override
	public Integer getHalf()
	{
		return half;
	}

	@Override
	public Integer getLeftScore()
	{
		return leftScore;
	}

	@Override
	public Integer getRightScore()
	{
		return rightScore;
	}

	@Override
	public Integer getPlayMode()
	{
		return playMode;
	}

	@Override
	public List<Foul> getFouls()
	{
		return fouls;
	}

	public void setFieldLength(Float fieldLength)
	{
		this.fieldLength = fieldLength;
	}

	public void setFieldWidth(Float fieldWidth)
	{
		this.fieldWidth = fieldWidth;
	}

	public void setFieldHeight(Float fieldHeight)
	{
		this.fieldHeight = fieldHeight;
	}

	public void setGoalWidth(Float goalWidth)
	{
		this.goalWidth = goalWidth;
	}

	public void setGoalDepth(Float goalDepth)
	{
		this.goalDepth = goalDepth;
	}

	public void setGoalHeight(Float goalHeight)
	{
		this.goalHeight = goalHeight;
	}

	public void setBorderSize(Float borderSize)
	{
		this.borderSize = borderSize;
	}

	public void setFreeKickDistance(Float freeKickDistance)
	{
		this.freeKickDistance = freeKickDistance;
	}

	public void setWaitBeforeKickOff(Float waitBeforeKickOff)
	{
		this.waitBeforeKickOff = waitBeforeKickOff;
	}

	public void setAgentRadius(Float agentRadius)
	{
		this.agentRadius = agentRadius;
	}

	public void setBallRadius(Float ballRadius)
	{
		this.ballRadius = ballRadius;
	}

	public void setBallMass(Float ballMass)
	{
		this.ballMass = ballMass;
	}

	public void setRuleGoalPauseTime(Float ruleGoalPauseTime)
	{
		this.ruleGoalPauseTime = ruleGoalPauseTime;
	}

	public void setRuleKickInPauseTime(Float ruleKickInPauseTime)
	{
		this.ruleKickInPauseTime = ruleKickInPauseTime;
	}

	public void setRuleHalfTime(Float ruleHalfTime)
	{
		this.ruleHalfTime = ruleHalfTime;
	}

	public void setPlayModes(String[] playModes)
	{
		this.playModes = playModes;
	}

	public void setTime(Float time)
	{
		this.time = time;
	}

	public void setLeftTeam(String leftTeamName)
	{
		this.leftTeam = leftTeamName;
	}

	public void setRightTeam(String rightTeamName)
	{
		this.rightTeam = rightTeamName;
	}

	public void setHalf(Integer half)
	{
		this.half = half;
	}

	public void setLeftScore(Integer leftScore)
	{
		this.leftScore = leftScore;
	}

	public void setRightScore(Integer rightScore)
	{
		this.rightScore = rightScore;
	}

	public void setPlayMode(Integer playMode)
	{
		this.playMode = playMode;
	}

	public void addFoul(Foul foul)
	{
		fouls.add(foul);
	}
}
