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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class RCServerMetaModelV64 extends RCServerMetaModelV63
{
	public static final RCServerMetaModelV64 INSTANCE = new RCServerMetaModelV64();

	@Override
	public int getVersion()
	{
		return 64;
	}

	@Override
	protected Vector2D initFieldDimensions()
	{
		return new Vector2D(21, 14);
	}

	@Override
	protected Map<String, Vector3D[]> initFieldLines()
	{
		Map<String, Vector3D[]> fieldLines = new HashMap<>();

		double fieldHalfLength = getFieldDimensions().getX() / 2;
		double fieldHalfWidth = getFieldDimensions().getY() / 2;

		// Ground lines
		fieldLines.put("UGL",
				new Vector3D[] {// Upper-ground-line
						new Vector3D(-fieldHalfLength, fieldHalfWidth, 0),
						new Vector3D(fieldHalfLength, fieldHalfWidth, 0)});

		fieldLines.put("LGL",
				new Vector3D[] {// Lower-ground-line
						new Vector3D(-fieldHalfLength, -fieldHalfWidth, 0),
						new Vector3D(fieldHalfLength, -fieldHalfWidth, 0)});

		// Side lines
		fieldLines.put("LSL",
				new Vector3D[] {// Left-Side-Line
						new Vector3D(-fieldHalfLength, fieldHalfWidth, 0),
						new Vector3D(-fieldHalfLength, -fieldHalfWidth, 0)});

		fieldLines.put("RSL",
				new Vector3D[] {// Right-side-line
						new Vector3D(fieldHalfLength, fieldHalfWidth, 0),
						new Vector3D(fieldHalfLength, -fieldHalfWidth, 0)});

		// Middle line
		fieldLines.put("ML", new Vector3D[] {// Middle-Line
									 new Vector3D(0, fieldHalfWidth, 0), new Vector3D(0, -fieldHalfWidth, 0)});

		double penaltyHalfLength = getPenaltyHalfLength();
		double penaltyWidth = getPenaltyAreaDimensions().getX();

		// Left penalty area lines
		fieldLines.put("LPAUL",
				new Vector3D[] {// Left-penalty-area-upper-line
						new Vector3D(-fieldHalfLength, penaltyHalfLength, 0),
						new Vector3D(-fieldHalfLength + penaltyWidth, penaltyHalfLength, 0)});

		fieldLines.put("LPALL",
				new Vector3D[] {// Left-penalty-area-lower-line
						new Vector3D(-fieldHalfLength, -penaltyHalfLength, 0),
						new Vector3D(-fieldHalfLength + penaltyWidth, -penaltyHalfLength, 0)});

		fieldLines.put("LPAFL",
				new Vector3D[] {// Left-penalty-area-front-line
						new Vector3D(-fieldHalfLength + penaltyWidth, penaltyHalfLength, 0),
						new Vector3D(-fieldHalfLength + penaltyWidth, -penaltyHalfLength, 0)});

		// Right penalty area lines
		fieldLines.put("RPAUL",
				new Vector3D[] {// Right-penalty-area-upper-line
						new Vector3D(fieldHalfLength, penaltyHalfLength, 0),
						new Vector3D(fieldHalfLength - penaltyWidth, penaltyHalfLength, 0)});

		fieldLines.put("RPALL",
				new Vector3D[] {// Right-penalty-area-lower-line
						new Vector3D(fieldHalfLength, -penaltyHalfLength, 0),
						new Vector3D(fieldHalfLength - penaltyWidth, -penaltyHalfLength, 0)});

		fieldLines.put("RPAFL",
				new Vector3D[] {// Right-penalty-area-front-line
						new Vector3D(fieldHalfLength - penaltyWidth, penaltyHalfLength, 0),
						new Vector3D(fieldHalfLength - penaltyWidth, -penaltyHalfLength, 0)});

		// Middle circle lines
		double px1 = getMiddleCircleRadius();
		double py1 = 0;
		double px2, py2;

		for (int deg = 36; deg <= 360; deg += 36) {
			px2 = Math.cos(Math.toRadians(deg)) * getMiddleCircleRadius();
			py2 = Math.sin(Math.toRadians(deg)) * getMiddleCircleRadius();

			fieldLines.put("MC_" + (deg - 36) + "-" + deg,
					new Vector3D[] {new Vector3D(px1, py1, 0), new Vector3D(px2, py2, 0)});

			px1 = px2;
			py1 = py2;
		}

		return fieldLines;
	}

	protected double getPenaltyHalfLength()
	{
		return getPenaltyAreaDimensions().getY() / 2 + getGoalDimensions().getY() / 2;
	}
}
