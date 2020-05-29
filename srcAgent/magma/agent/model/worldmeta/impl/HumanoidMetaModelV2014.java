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
package magma.agent.model.worldmeta.impl;

import hso.autonomy.agent.model.worldmodel.impl.Landmark;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import java.util.*;
import java.util.stream.Collectors;

public class HumanoidMetaModelV2014 extends RCServerMetaModelV66
{
	public static final HumanoidMetaModelV2014 INSTANCE = new HumanoidMetaModelV2014();

	@Override
	protected Vector2D initFieldDimensions()
	{
		return new Vector2D(9, 6);
	}

	@Override
	protected Vector3D initGoalDimensions()
	{
		return new Vector3D(0.6, 2.6, 1.8);
	}

	@Override
	protected Vector2D initPenaltyAreaDimensions()
	{
		return new Vector2D(1, 5);
	}

	@Override
	public float getMiddleCircleRadius()
	{
		return 0.75f;
	}

	@Override
	public float getBallRadius()
	{
		return 0.11f;
	}

	@Override
	protected double getPenaltyHalfLength()
	{
		return getPenaltyAreaDimensions().getY() / 2;
	}

	@Override
	protected Map<String, Vector3D> initLandMarks()
	{
		landmarks = new HashMap<>();

		double fieldHalfLength = getFieldDimensions().getX() / 2;
		double fieldHalfWidth = getFieldDimensions().getY() / 2;
		double penaltyAreaHalfLength = getPenaltyHalfLength();
		double penaltyAreaWidth = getPenaltyAreaDimensions().getX();
		double goalHalfWidth = getGoalDimensions().getY() / 2;

		// all possible t-corners at the field
		landmarks.put("Tsla", new Vector3D(-fieldHalfLength, penaltyAreaHalfLength, 0.0));
		landmarks.put("Tsra", new Vector3D(-fieldHalfLength, -penaltyAreaHalfLength, 0.0));
		landmarks.put("Tclf", new Vector3D(0.0f, fieldHalfWidth, 0.0));
		landmarks.put("Tcrf", new Vector3D(0.0f, -fieldHalfWidth, 0.0));
		landmarks.put("Tola", new Vector3D(fieldHalfLength, penaltyAreaHalfLength, 0.0));
		landmarks.put("Tora", new Vector3D(fieldHalfLength, -penaltyAreaHalfLength, 0.0));

		// all possible l-corners at the field
		landmarks.put("Lslf", new Vector3D(-fieldHalfLength, fieldHalfWidth, 0.0));
		landmarks.put("Lsrf", new Vector3D(-fieldHalfLength, -fieldHalfWidth, 0.0));
		landmarks.put("Lsla", new Vector3D(-(fieldHalfLength - penaltyAreaWidth), penaltyAreaHalfLength, 0.0));
		landmarks.put("Lsra", new Vector3D(-(fieldHalfLength - penaltyAreaWidth), -penaltyAreaHalfLength, 0.0));
		landmarks.put("Lola", new Vector3D((fieldHalfLength - penaltyAreaWidth), penaltyAreaHalfLength, 0.0));
		landmarks.put("Lora", new Vector3D((fieldHalfLength - penaltyAreaWidth), -penaltyAreaHalfLength, 0.0));
		landmarks.put("Lolf", new Vector3D(fieldHalfLength, fieldHalfWidth, 0.0));
		landmarks.put("Lorf", new Vector3D(fieldHalfLength, -fieldHalfWidth, 0.0));

		// X-crosses at the field
		landmarks.put("Xclc", new Vector3D(0.0f, (getMiddleCircleRadius()), 0.0));
		landmarks.put("Xcrc", new Vector3D(0.0f, -(getMiddleCircleRadius()), 0.0));

		// penalty marks at the field
		landmarks.put("Psmx", new Vector3D(-2.4f, 0.0, 0.0));
		landmarks.put("Pomx", new Vector3D(2.4f, 0.0, 0.0));

		// goalposts at the field
		landmarks.put("Gsrg", new Vector3D(-fieldHalfLength, -goalHalfWidth, 0.0));
		landmarks.put("Gslg", new Vector3D(-fieldHalfLength, goalHalfWidth, 0.0));
		landmarks.put("Gorg", new Vector3D(fieldHalfLength, -goalHalfWidth, 0.0));
		landmarks.put("Golg", new Vector3D(fieldHalfLength, goalHalfWidth, 0.0));

		return landmarks;
	}

	/**
	 *
	 * @return list of all known landmarks with L-junction
	 */
	public List<Landmark> getTMarks()
	{
		return getLandMarkByType("T");
	}

	/**
	 *
	 * @return list of all known landmarks with L-junction
	 */
	public List<Landmark> getLMarks()
	{
		return getLandMarkByType("L");
	}

	/**
	 *
	 * @return list of all known landmarks with X-junction
	 */
	public List<Landmark> getXMarks()
	{
		return getLandMarkByType("X");
	}

	/**
	 *
	 * @return list of all known landmarks with penalty points
	 */
	public List<Landmark> getPMarks()
	{
		return getLandMarkByType("P");
	}

	/**
	 *
	 * @return list of all known landmarks with goal posts
	 */
	public List<Landmark> getGoalPosts()
	{
		return getLandMarkByType("G");
	}

	private List<Landmark> getLandMarkByType(String type)
	{
		return landmarks.entrySet()
				.stream()
				.filter(entry -> entry.getKey().startsWith(type))
				.map(entry -> new Landmark(entry.getKey(), entry.getValue()))
				.collect(Collectors.toList());
	}
}
