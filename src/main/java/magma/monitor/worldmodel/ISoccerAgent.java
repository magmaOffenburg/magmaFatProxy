package magma.monitor.worldmodel;

import hso.autonomy.util.geometry.Pose3D;

public interface ISoccerAgent extends ISimulationObject {
	String getTeamName();

	int getPlayerID();

	Pose3D getBodyPartPose(SoccerAgentBodyPart bodyPart);

	String getRobotModel();
}
