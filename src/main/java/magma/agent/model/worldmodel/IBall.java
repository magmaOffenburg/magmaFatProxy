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
package magma.agent.model.worldmodel;

import hso.autonomy.agent.model.worldmodel.IMoveableObject;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public interface IBall extends IMoveableObject
{
	float COLLISION_DISTANCE = 0.28f;

	float getRadius();

	void resetPosition(Vector3D position);

	boolean isBouncing(float globalTime);
}
