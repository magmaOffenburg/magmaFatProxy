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
package magma.monitor.worldmodel;

import hso.autonomy.util.observer.IObserver;
import java.util.List;
import magma.common.spark.Foul;
import magma.common.spark.PlayMode;
import magma.monitor.messageparser.IMonitorMessageParser;
import magma.util.scenegraph.impl.SceneGraph;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public interface IMonitorWorldModel extends IObserver<IMonitorMessageParser> {
	SceneGraph getSceneGraph();

	boolean hasSceneGraphStructureChanged();

	Vector3D getFieldDimensions();

	Vector3D getGoalDimensions();

	float getTime();

	String getLeftTeamName();

	String getRightTeamName();

	int getScoreLeft();

	int getScoreRight();

	PlayMode getPlayMode();

	int getHalf();

	ISoccerBall getBall();

	List<? extends ISoccerAgent> getSoccerAgents();

	List<Foul> getFouls();
}
