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
package magma.agent.model.thoughtmodel.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import hso.autonomy.agent.communication.action.IAction;
import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.agent.model.agentmodel.IAgentModel;
import hso.autonomy.agent.model.thoughtmodel.ITruthValue;
import hso.autonomy.agent.model.thoughtmodel.impl.ThoughtModel;
import hso.autonomy.agent.model.worldmodel.IMoveableObject;
import hso.autonomy.agent.model.worldmodel.IVisibleObject;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose3D;
import hso.autonomy.util.logging.PropertyMap;
import hso.autonomy.util.misc.FuzzyCompare;
import hso.autonomy.util.misc.ValueUtil;
import magma.agent.IHumanoidJoints;
import magma.agent.communication.action.IRoboCupAction;
import magma.agent.communication.perception.ICameraTiltPerceptor;
import magma.agent.communication.perception.IProxyPerceptor;
import magma.agent.communication.perception.IRoboCupPerception;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.flags.IFlagModel;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.strategy.IRole;
import magma.agent.model.thoughtmodel.strategy.IRoleManager;
import magma.agent.model.thoughtmodel.strategy.impl.roles.DummyRole;
import magma.agent.model.worldmodel.IBall;
import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.agent.model.worldmodel.IRoboCupWorldModel.BallPassing;
import magma.agent.model.worldmodel.IThisPlayer;
import magma.monitor.general.impl.MonitorRuntime;
import magma.util.roboviz.RoboVizDraw;

/**
 * @author Stefan Glaser
 */
public class RoboCupThoughtModel extends ThoughtModel implements IRoboCupThoughtModel
{
	/** Maximum time we believe we know where the ball is. In seconds */
	private static final float LAST_SEEN_THRESHOLD = 3.0f;

	/** The flags model. */
	protected transient final IFlagModel flags;

	/** the indexical functional object calculator. */
	private transient IFOCalculator ifoCalculator;

	/** list of all obstacles to avoid sorted by the distance to me. */
	protected List<IVisibleObject> obstacles;

	/** list of all team-mates sorted by the distance to the ball. */
	private List<IPlayer> teamMateAtBallList;

	/** list of all opponents sorted by the distance to the ball. */
	private List<IPlayer> opponentsAtBallList;

	/** list of all players sorted by the distance to the ball. */
	private List<IPlayer> playersAtBallList;

	/** list of all players sorted by the distance to me. */
	private List<IPlayer> playersAtMeList;

	/** list of all opponents sorted by the distance to me. */
	private List<IPlayer> opponentsAtMeList;

	/** the command received from the thin client */
	protected ProxyCommand proxyCommand;

	protected transient KickPositionProfiler kickPositionProfiler;

	protected transient IRoleManager roleManager;

	/** The current role of the agent. */
	protected transient IRole role;

	protected transient final RoboVizDraw roboVizDraw;

	protected transient PropertyMap properties;

	/** Indicator for camera tilt */
	protected String cameraTilt;

	private boolean inSoccerPosition = false;

	protected transient String headCommand;

	protected transient Angle horizontalHeadAngle;

	protected transient Angle verticalHeadAngle;

	protected transient String movementCommand;

	protected transient Vector3D intendedWalk;

	protected transient Vector2D relBallKickPos;

	protected transient Vector2D relBallKickTargetPos;

	protected transient String sayCommand;

	protected transient Map<String, String> sayParams;

	private MonitorRuntime monitor;

	public RoboCupThoughtModel(IAgentModel agentModel, IRoboCupWorldModel worldModel, IFlagModel flags,
			final RoboVizDraw roboVizDraw, MonitorRuntime monitor)
	{
		super(agentModel, worldModel);
		this.flags = flags;
		this.roboVizDraw = roboVizDraw;
		this.monitor = monitor;
		this.properties = new PropertyMap();

		ifoCalculator = new IFOCalculator(worldModel);
		kickPositionProfiler = new KickPositionProfilerGoal(worldModel);

		roleManager = null;
		role = null;

		// functional Object list
		obstacles = new ArrayList<>(0);
		teamMateAtBallList = new ArrayList<>(0);
		opponentsAtBallList = new ArrayList<>(0);
		playersAtBallList = new ArrayList<>(0);
		playersAtMeList = new ArrayList<>(0);
		opponentsAtMeList = new ArrayList<>(0);

		// Initialize proxy command with empty command
		proxyCommand = new ProxyCommand("", null);
	}

	public MonitorRuntime getMonitorRuntime()
	{
		return monitor;
	}

	@Override
	public IRoboCupAgentModel getAgentModel()
	{
		return (IRoboCupAgentModel) super.getAgentModel();
	}

	@Override
	public IRoboCupWorldModel getWorldModel()
	{
		return (IRoboCupWorldModel) super.getWorldModel();
	}

	@Override
	public IFlagModel getFlags()
	{
		return flags;
	}

	@Override
	public RoboVizDraw getRoboVizDraw()
	{
		return roboVizDraw;
	}

	@Override
	public void log(String name, Object value)
	{
		if (properties != null) {
			properties.log(name, value);
		}
	}

	@Override
	public void disableLogging()
	{
		properties = null;
	}

	@Override
	public PropertyMap getProperties()
	{
		return properties;
	}

	@Override
	public boolean update(IPerception perception)
	{
		if (properties != null) {
			properties.update();
		}

		boolean result = false;
		if (perception != null) {
			// Trigger flags model to process flag information
			flags.update(perception);

			// Trigger agent model to update all internal sensor values
			result = getAgentModel().update(perception);

			// Trigger world model to process vision information
			result = getWorldModel().update(perception) || result;

			ICameraTiltPerceptor cameraTiltPerceptor = ((IRoboCupPerception) perception).getCameraTiltPerceptor();
			if (cameraTiltPerceptor != null) {
				cameraTilt = cameraTiltPerceptor.getCameraTilt();
			}

			// update all truth values with state
			for (ITruthValue value : truthValues.values()) {
				value.update(this);
			}

			// update proxy command
			IProxyPerceptor proxyPerceptor = ((IRoboCupPerception) perception).getProxyPerceptor();
			if (proxyPerceptor != null) {
				proxyCommand = new ProxyCommand(proxyPerceptor.getName(), proxyPerceptor.getValues());
			}
		}

		// Recalculate indexical functional objects
		calculateIndexicalFunctionalObjects();

		// recalculate kick directions
		kickPositionProfiler.resetProfile();

		// determine new roles
		role = roleManager.determineRole(getClosestOwnPlayerAtBall(), getWorldModel().getThisPlayer().getID());

		roboVizDraw.setPlaySide(getWorldModel().getPlaySide());

		return result;
	}

	@Override
	public void mapStateToAction(IAction theAction, boolean remoteControlled)
	{
		IRoboCupAction action = (IRoboCupAction) theAction;
		IRoboCupAgentModel agentModel = getAgentModel();
		agentModel.reflectTargetStateToAction(action);

		// Set DecisionEffector
		int headCommandID = HeadCommands.getCommandFor(headCommand).getId();
		float headTargetYaw = 0;
		float headTargetPitch = 0;
		if (horizontalHeadAngle != null) {
			headTargetYaw = (float) horizontalHeadAngle.radians();
		}
		if (verticalHeadAngle != null) {
			headTargetPitch = (float) verticalHeadAngle.radians();
		}

		int bodyCommandID = MovementCommands.getCommandFor(movementCommand).getId();
		float intendedX = 0;
		float intendedY = 0;
		float intendedTurn = 0;
		if (intendedWalk != null) {
			intendedX = (float) intendedWalk.getX();
			intendedY = (float) intendedWalk.getY();
			intendedTurn = (float) intendedWalk.getZ();
		}

		float relBallPosX = 0;
		float relBallPosY = 0;
		float relBallTargetPosX = 0;
		float relBallTargetPosY = 0;
		if (relBallKickPos != null && relBallKickTargetPos != null) {
			relBallPosX = (float) relBallKickPos.getX();
			relBallPosY = (float) relBallKickPos.getY();
			relBallTargetPosX = (float) relBallKickTargetPos.getX();
			relBallTargetPosY = (float) relBallKickTargetPos.getY();
		}

		int sayCommandID = SayCommands.getCommandFor(sayCommand).getId();

		action.setDecision(headCommandID, headTargetYaw, headTargetPitch, bodyCommandID, intendedX, intendedY,
				intendedTurn, relBallPosX, relBallPosY, relBallTargetPosX, relBallTargetPosY, remoteControlled,
				sayCommandID, sayParams);

		// rotation at ball not required
		Pose3D ballPosition = new Pose3D(getWorldModel().getBall().getPosition(), Rotation.IDENTITY);

		// get local camera pose and calculate the global position
		Pose3D camera = agentModel.getCameraPose();
		Pose3D cameraLocalPose = Geometry.bodyToWorld(camera);
		Pose3D cameraGlobalPose = getWorldModel().getThisPlayer().calculateGlobalBodyPose(camera);

		action.setCameraPosition(ballPosition, cameraLocalPose, cameraGlobalPose, headCommand);
	}

	/**
	 * Calculates the lists of players sorted by different criteria.
	 */
	protected void calculateIndexicalFunctionalObjects()
	{
		IRoboCupWorldModel worldModel = getWorldModel();
		List<IPlayer> visiblePlayers = worldModel.getVisiblePlayers();
		if (worldModel.isBallInCriticalArea()) {
			teamMateAtBallList = ifoCalculator.getTeammatesAtBallWithGoalie(visiblePlayers);
		} else {
			teamMateAtBallList = ifoCalculator.getTeammatesAtBall(visiblePlayers);
		}
		opponentsAtBallList = ifoCalculator.getOpponentsAtBall(visiblePlayers);
		playersAtBallList = ifoCalculator.getPlayersAtBall(visiblePlayers);
		playersAtMeList = ifoCalculator.getPlayersAtMe(visiblePlayers);
		opponentsAtMeList = ifoCalculator.getOpponentsAtMe(playersAtMeList);
		obstacles = ifoCalculator.getObstacles(visiblePlayers, worldModel.getBall());
	}

	// --------------------------------------------------
	// IFOs
	@Override
	public List<IVisibleObject> getObstacles()
	{
		return obstacles;
	}

	@Override
	public List<IPlayer> getOpponentsAtMeList()
	{
		return opponentsAtMeList;
	}

	@Override
	public List<IPlayer> getPlayersAtMeList()
	{
		return playersAtMeList;
	}

	@Override
	public List<IPlayer> getTeammatesAtBall()
	{
		return teamMateAtBallList;
	}

	@Override
	public IPlayer getTeammateAtBall()
	{
		if (teamMateAtBallList.isEmpty()) {
			return null;
		}
		return teamMateAtBallList.get(0);
	}

	@Override
	public List<IPlayer> getOpponentsAtBallList()
	{
		return opponentsAtBallList;
	}

	@Override
	public IPlayer getOpponentAtBall()
	{
		if (opponentsAtBallList.isEmpty()) {
			return null;
		}
		return opponentsAtBallList.get(0);
	}

	@Override
	public List<IPlayer> getPlayersAtBallList()
	{
		return playersAtBallList;
	}

	@Override
	public IPlayer getClosestOwnPlayerAtBall()
	{
		List<IPlayer> playersAtBall = getTeammatesAtBall();
		List<IPlayer> newSort = new ArrayList<>(playersAtBall);
		// we add ourselves to have the same calculation for all
		newSort.add(getWorldModel().getThisPlayer());

		return Collections.min(newSort, new DistanceToBallComparator(getWorldModel()));
	}

	@Override
	public boolean isClosestToBall()
	{
		return getWorldModel().getThisPlayer() == getClosestOwnPlayerAtBall();
	}

	@Override
	public boolean isOpponentNearBall()
	{
		if (getOpponentAtBall() == null) {
			return false;
		}

		Vector3D ballPosition = getWorldModel().getBall().getPosition();
		return getOpponentAtBall().getDistanceToXY(ballPosition) < 1;
	}

	@Override
	public boolean isBallVisible()
	{
		return getWorldModel().getBall().getAge(getWorldModel().getGlobalTime()) < LAST_SEEN_THRESHOLD;
	}

	@Override
	public boolean isInSoccerPosition()
	{
		if (getAxis(IHumanoidJoints.LHipPitch) > 65 || getAxis(IHumanoidJoints.RHipPitch) > 65) {
			inSoccerPosition = false; // sitting position
		}

		if (axesEqual(IHumanoidJoints.LKneePitch, IHumanoidJoints.RKneePitch, 0, 0.5f)) {
			inSoccerPosition = false;
		}

		IRoboCupAgentModel agentModel = getAgentModel();
		if (!axesEqual(IHumanoidJoints.LKneePitch, IHumanoidJoints.RKneePitch, agentModel.getSoccerPositionKneeAngle(),
					5f)) {
			return inSoccerPosition;
		}

		float desiredHipAngle = agentModel.getSoccerPositionHipAngle();
		if (desiredHipAngle < -180) {
			inSoccerPosition = true;
		} else if (axesEqual(IHumanoidJoints.LHipRoll, IHumanoidJoints.RHipRoll, desiredHipAngle, 3f)) {
			inSoccerPosition = true;
		}

		return inSoccerPosition;
	}

	private boolean axesEqual(String leftJoint, String rightJoint, float desired, float range)
	{
		return FuzzyCompare.eq(getAxis(leftJoint), desired, range) &&
				FuzzyCompare.eq(getAxis(rightJoint), desired, range);
	}

	private float getAxis(String jointName)
	{
		return getAgentModel().getHJ(jointName).getAngle();
	}

	@Override
	public void setHeadCommand(String command)
	{
		headCommand = command;
	}

	@Override
	public String getHeadCommand()
	{
		return headCommand;
	}

	@Override
	public void setIntendedViewDirection(Angle horizontalAngle, Angle verticalAngle)
	{
		horizontalHeadAngle = horizontalAngle;
		verticalHeadAngle = verticalAngle;
	}

	@Override
	public void setMovementCommand(String command)
	{
		movementCommand = command;
	}

	@Override
	public void setSayCommand(String command, Map<String, String> sayParams)
	{
		this.sayCommand = command;
		this.sayParams = sayParams;
	}

	@Override
	public void setIntendedWalk(double intendedX, double intendedY, double intendedTurn)
	{
		intendedWalk = new Vector3D(intendedX, intendedY, ValueUtil.limitAbs(intendedTurn, 11) * 100 / 11);
	}

	@Override
	public Vector3D getIntendedWalk()
	{
		return intendedWalk;
	}

	@Override
	public void setIntendedKick(Vector2D relBallKickPos, Vector2D relBallKickTargetPos)
	{
		this.relBallKickPos = relBallKickPos;
		this.relBallKickTargetPos = relBallKickTargetPos;
	}

	// --------------------------------------------------
	// Kick profile and estimation
	@Override
	public Angle getIntendedKickDirection()
	{
		return kickPositionProfiler.getIntendedKickDirection(this);
	}

	@Override
	public float getIntendedKickDistance()
	{
		return kickPositionProfiler.getIntendedKickDistance(this);
	}

	@Override
	public List<KickPositionEstimation> getKickOptions()
	{
		return kickPositionProfiler.getEstimations(this);
	}

	@Override
	public void setKickPositionProfiler(KickPositionProfiler kickPositionProfiler)
	{
		this.kickPositionProfiler = kickPositionProfiler;
	}

	// --------------------------------------------------
	// Strategy and Role management
	@Override
	public IRoleManager getRoleManager()
	{
		return roleManager;
	}

	@Override
	public IRole getRole()
	{
		return role;
	}

	public void setRoleManager(IRoleManager manager)
	{
		roleManager = manager;
		this.role = DummyRole.INSTANCE;
	}

	@Override
	public boolean shouldBeam()
	{
		if (getWorldModel().getGameState().isBeamingAllowed()) {
			double distance = getWorldModel().getThisPlayer().getDistanceToXY(getHomePose().getPosition());
			if (distance > 0.3) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Pose2D getHomePose()
	{
		return roleManager.getStrategy().getFormation().getPlayerPose(getWorldModel().getThisPlayer().getID());
	}

	// --------------------------------------------------
	// Other stuff
	@Override
	public String getCameraTilt()
	{
		return cameraTilt;
	}

	/**
	 * @return true if the ball is in a position in which we should dribble into goal
	 */
	@Override
	public boolean ballIsInGoalDribblingPosition(double maxXDistanceFromGoal)
	{
		Vector3D ballPos = getWorldModel().getBall().getPosition();
		float goalHalfWidth = getWorldModel().goalHalfWidth() - 0.05f;

		if (ballPos.getX() > (getWorldModel().fieldHalfLength() - maxXDistanceFromGoal) &&
				ballPos.getY() < goalHalfWidth && ballPos.getY() > -goalHalfWidth) {
			return true;
		}

		return false;
	}

	/**
	 * @param limitAngle the maximum abs horizontal angle (in deg) we can face
	 * @return true if we are in a position in which we should dribble into goal
	 */
	@Override
	public boolean canDribbleIntoGoal(double limitAngle)
	{
		IThisPlayer thisPlayer = getWorldModel().getThisPlayer();
		Vector3D ballPos = getWorldModel().getBall().getPosition();
		float goalHalfWidth = getWorldModel().goalHalfWidth() - 0.05f;

		double viewDirection = thisPlayer.getHorizontalAngle().degrees();

		// Ball is very close or inside the opponent's goal
		// Therefore just allow dribblings towards the goal
		if (ballPos.getX() > (getWorldModel().fieldHalfLength() - 0.3)) {
			double limit = limitAngle - (Math.abs(ballPos.getY()) / goalHalfWidth) * limitAngle;

			if ((ballPos.getY() < 0 && limitAngle > viewDirection && viewDirection > -limit) ||
					(ballPos.getY() >= 0 && limit > viewDirection && viewDirection > -limitAngle)) {
				// 3. Dribble 10cm forwards with full speed
				return true;
			}
		} else {
			IMoveableObject ball = getWorldModel().getBall();
			Vector3D otherGoalPos = getWorldModel().getOtherGoalPosition();

			double ballToOtherUpperPost =
					ball.getDirectionTo(otherGoalPos.add(new Vector3D(0, goalHalfWidth, 0))).degrees();
			double ballToOtherLowerPost =
					ball.getDirectionTo(otherGoalPos.subtract(new Vector3D(0, goalHalfWidth, 0))).degrees();

			if (ballToOtherUpperPost > viewDirection && viewDirection > ballToOtherLowerPost) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Calculates target position for dribbling, based on our current position
	 * and orientation.
	 *
	 * @return the target pose for dribbling (usually 10 cm in front of us)
	 */
	@Override
	public Pose2D getDribblePose()
	{
		IThisPlayer thisPlayer = getWorldModel().getThisPlayer();
		Angle viewDirection = thisPlayer.getHorizontalAngle();

		Vector3D targetPos = thisPlayer.getPosition().add(new Vector3D(0.1, new Vector3D(viewDirection.radians(), 0)));
		return new Pose2D(targetPos, viewDirection);
	}

	@Override
	public Vector3D getDesiredKickPosition()
	{
		return kickPositionProfiler.getFilteredPosition();
	}

	@Override
	public boolean isBallPassingShort()
	{
		IBall ball = getWorldModel().getBall();
		if (ball.isMoving()) {
			Vector3D futureBallPosition = ball.getFuturePosition(200);
			Vector3D otherGoalPosition = getWorldModel().getOtherGoalPosition();
			if (Vector3D.distance(futureBallPosition, otherGoalPosition) <
					Vector3D.distance(ball.getPosition(), otherGoalPosition)) {
				// ball is moving towards other goal
				return false;
			}

			BallPassing ballIsPassing = getWorldModel().ballIsPassing(futureBallPosition);
			switch (ballIsPassing) {
			case SHORT_LEFT:
			case SHORT_RIGHT:
				return true;
			case CENTER:
			case FAR_LEFT:
			case FAR_RIGHT:
			case UNREACHABLE:
			default:
				return false;
			}
		}
		return false;
	}

	@Override
	public ProxyCommand getProxyCommand()
	{
		return proxyCommand;
	}
}
