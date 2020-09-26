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
package magma.agent.decision.behavior;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public interface IKickParameters {
	Angle getIntendedKickDirection();

	void setIntendedKickDirection(Angle intendedKickDirection);

	float getIntendedKickDistance();

	void setIntendedKickDistance(float intendedKickDistance);

	Vector3D getExpectedBallPosition();

	void setExpectedBallPosition(Vector3D pos);

	SupportFoot getKickingFoot();

	Pose2D getRelativeRunToPose();

	double getMaxKickDistance();

	double getMinKickDistance();

	double getOpponentMinDistance();

	float getBallMaxSpeed();

	double getOpponentMaxDistance();

	float getPriority();

	Angle getKickDirection();

	int getBallHitCycles();

	Vector2D getSpeedAtRunToPose();

	Angle getRelativeKickDirection();
}
