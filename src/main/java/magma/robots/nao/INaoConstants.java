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
package magma.robots.nao;

import magma.agent.IHumanoidConstants;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public interface INaoConstants extends IHumanoidConstants
{
	// General constants
	String ACTION_SCENE = "rsg/agent/nao/nao.rsg";

	String ACTION_SCENE_HETERO = "rsg/agent/nao/nao_hetero.rsg";

	String ACTION_SCENE_HETERO_TOE = "rsg/agent/nao/nao_hetero.rsg";

	float MAX_JOINT_SPEED = 7.03f;

	Vector3D STATIC_PIVOT_POINT = new Vector3D(0, 0, -0.075);

	float TORSO_Z_UPRIGHT = 0.4f;

	// Body part Constants
	String Torso = "torso";

	String Head = "head";

	String Neck = "neck";

	String RShoulder = "rshoulder";

	String RUpperArm = "rupperarm";

	String RElbow = "relbow";

	String RLowerArm = "rlowerarm";

	String LShoulder = "lshoulder";

	String LUpperArm = "lupperarm";

	String LElbow = "lelbow";

	String LLowerArm = "llowerarm";

	String RHip1 = "rhip1";

	String RHip2 = "rhip2";

	String RThight = "rthight";

	String RShank = "rshank";

	String RAnkle = "rankle";

	String RToe = "rtoe";

	String LHip1 = "lhip1";

	String LHip2 = "lhip2";

	String LThight = "lthight";

	String LShank = "lshank";

	String LAnkle = "lankle";

	String LToe = "ltoe";

	// GyroRate-Constants
	String TorsoGyro = "TorsoGyro";

	// ForceResistance-Constants
	String RFootForce = "RFootForce";

	String LFootForce = "LFootForce";

	String RToeForce = "RToeForce";

	String LToeForce = "LToeForce";
}
