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
package magma.agent.model.worldmodel.impl;

import hso.autonomy.agent.communication.perception.IGlobalPosePerceptor;
import hso.autonomy.agent.communication.perception.ILinePerceptor;
import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.agent.communication.perception.ITimerPerceptor;
import hso.autonomy.agent.communication.perception.IVisibleObjectPerceptor;
import hso.autonomy.agent.model.agentmodel.IBodyPart;
import hso.autonomy.agent.model.agentmodel.ICompass;
import hso.autonomy.agent.model.agentmodel.IGyroRate;
import hso.autonomy.agent.model.worldmodel.ILandmark;
import hso.autonomy.agent.model.worldmodel.IVisibleObject;
import hso.autonomy.agent.model.worldmodel.InformationSource;
import hso.autonomy.agent.model.worldmodel.impl.FieldLine;
import hso.autonomy.agent.model.worldmodel.impl.Landmark;
import hso.autonomy.agent.model.worldmodel.impl.LocalizationLine;
import hso.autonomy.agent.model.worldmodel.impl.WorldModel;
import hso.autonomy.agent.model.worldmodel.localizer.ILocalizationLine;
import hso.autonomy.agent.model.worldmodel.localizer.ILocalizer;
import hso.autonomy.agent.model.worldmodel.localizer.IReferenceLine;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose3D;
import hso.autonomy.util.geometry.PoseSpeed2D;
import hso.autonomy.util.geometry.orientationFilter.IOrientationFilter;
import hso.autonomy.util.geometry.orientationFilter.NoOrientationFilter;
import hso.autonomy.util.misc.SayCoder;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import magma.agent.IMagmaConstants;
import magma.agent.communication.perception.IGameStatePerceptor;
import magma.agent.communication.perception.IHearPerceptor;
import magma.agent.communication.perception.IPlayerPos;
import magma.agent.communication.perception.IRoboCupPerception;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.worldmeta.IRoboCupWorldMetaModel;
import magma.agent.model.worldmodel.GameState;
import magma.agent.model.worldmodel.IBall;
import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.agent.model.worldmodel.IThisPlayer;
import magma.common.spark.PlayMode;
import magma.common.spark.PlaySide;
import magma.common.spark.TeamColor;
import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Container class for all visible objects on the field, for the time and game
 * state
 */
public class RoboCupWorldModel extends WorldModel implements IRoboCupWorldModel
{
	private static final float TIME_TO_TRUST_HEAR = 0.2f;

	/** the time we keep other players in mind if not seeing them (in seconds) */
	protected static int REMEMBRANCE_TIME = 30;

	/** The soccer pitch description and helper class */
	protected SoccerPitchDescription soccerPitch;

	/** the ball of the game */
	protected IBall ball;

	/**
	 * A map of all known players over time. As unique key, the team name
	 * appended by the player number is used.
	 */
	protected Map<String, IPlayer> knownPlayers;

	/**
	 * A String that represents which is the game situation.
	 *
	 */
	protected GameMode gameMode;

	/** the list of all visible players */
	protected List<IPlayer> visiblePlayers;

	/** the time the game is running now (cycles since start) */
	private float gameTime;

	/**
	 * The side which our team is playing. Note: It's important that this value
	 * is initialized with the left side!
	 */
	private PlaySide playSide = PlaySide.LEFT;

	/** color of team. Currently unknown in simspark! */
	private TeamColor teamColor = TeamColor.BLUE;

	/** the playmode the game is running now */
	private PlayMode playmode = PlayMode.BEFORE_KICK_OFF;

	/** the game state, the game is running now */
	protected GameState gameState = GameState.BEFORE_KICK_OFF;

	/** the game state, the game was running previously */
	protected GameState previousGameState;

	/** the global coordinates of the agent */
	protected ThisPlayer thisPlayer;

	/** goals scored by the opponent */
	protected int goalsTheyScored = 0;

	/** goals scored by us */
	protected int goalsWeScored = 0;

	protected IRoboCupWorldMetaModel worldMetaModel;

	/** The version of the soccer server used */
	protected int serverVersion;

	private transient IOrientationFilter orientationFilter;

	/**
	 * The name of the other team.<br>
	 * <tt>null</tt> as long we haven't seen any other player.
	 */
	protected String otherTeamName;

	/**
	 * The transformation from the camera body system to the root body system of
	 * the current cycle.
	 */
	private transient Pose3D cameraToRootVision;

	// KlausTest
	// private transient AttitudeEstimator attitudeEstimator;
	//
	// public transient Rotation attitudeEstimation;

	/**
	 * Constructor
	 *
	 * @param agentModel Reference to the agent model object
	 * @param localizer the module that calculates the agent's global position
	 * @param worldMetaModel the meta model of the rc server
	 * @param teamname Team name
	 * @param playerNumber Player number
	 */
	public RoboCupWorldModel(IRoboCupAgentModel agentModel, ILocalizer localizer, IRoboCupWorldMetaModel worldMetaModel,
			String teamname, int playerNumber)
	{
		super(agentModel, localizer);
		this.worldMetaModel = worldMetaModel;
		this.serverVersion = worldMetaModel.getVersion();
		this.soccerPitch = new SoccerPitchDescription(worldMetaModel);

		// create objects
		createObjects(worldMetaModel);

		thisPlayer = new ThisPlayer(teamname, playerNumber, agentModel.getCycleTime(), agentModel.getTorsoZUpright());
		thisPlayer.getPositionManager().setDesiredPosition(new PoseSpeed2D(new Pose2D(), Vector2D.ZERO), false);

		orientationFilter = new NoOrientationFilter();
		// orientationFilter = new OrientationFilter();

		// KlausTest
		// attitudeEstimator = new AttitudeEstimator(true);
		// attitudeEstimator.setMagCalib(-1.0, 0.0, 0.0);
	}

	/**
	 * Creates all objects. We do not create new objects each time on update, but
	 * update the existing objects
	 */
	private void createObjects(IRoboCupWorldMetaModel metaModel)
	{
		ball = new Ball(metaModel.getBallRadius(), metaModel.getBallDecay(), Vector3D.ZERO, IBall.COLLISION_DISTANCE,
				getAgentModel().getCycleTime());

		// Landmarks
		Map<String, Vector3D> landmarkConfig = metaModel.getLandmarks();
		if (landmarkConfig != null) {
			for (String key : landmarkConfig.keySet()) {
				Landmark landmark = new Landmark(key, landmarkConfig.get(key));
				landmarks.put(key, landmark);
				referencePoints.add(landmark);
			}
		}

		// FieldLines
		Map<String, Vector3D[]> fieldLineConfig = metaModel.getFieldLines();
		if (fieldLineConfig != null) {
			for (String key : fieldLineConfig.keySet()) {
				Vector3D[] posArray = fieldLineConfig.get(key);
				FieldLine fieldLine = new FieldLine(key, posArray[0], posArray[1]);
				fieldLines.put(key, fieldLine);
				referenceLines.add(fieldLine);
			}
		}

		// Players
		int numberOfPlayersPerTeam = IMagmaConstants.NUMBER_OF_PLAYERS_PER_TEAM;
		knownPlayers = new HashMap<>(numberOfPlayersPerTeam * 2);
		visiblePlayers = new ArrayList<>(numberOfPlayersPerTeam * 2);
	}

	/**
	 * Called once perception is finished parsing a new incoming message
	 * @param perception the object containing the result from parsing
	 */
	@Override
	public boolean update(IPerception perception)
	{
		// update global time
		ITimerPerceptor timerPerceptor = perception.getTime();
		if (timerPerceptor != null) {
			globalTime = timerPerceptor.getTime();
		}

		// update game time, play side and play mode, as well as scored goals
		processGameState((IRoboCupPerception) perception);

		//
		// if (gyro.getGyro().getY() > 0.1) {
		// if (previousGameState == GameState.BEFORE_KICK_OFF) {
		// ICompass compass = agentModel.getTorso().getSensor(
		// ICompass.class);
		// if (compass != null) {
		// compass.reset();
		// }
		// }
		// previousGameState = gameState;
		// playmode = PlayMode.PLAY_ON;
		// gameState = GameState.PLAY_ON;
		// } else {
		// playmode = PlayMode.BEFORE_KICK_OFF;
		// gameState = GameState.BEFORE_KICK_OFF;
		// }
		// }
		// }

		// camera coordinate system is updated to current pose
		updateCameraCoordinateSystem();

		// calculate the new position of the agent
		calculatePosition(perception);

		// KlausTest
		// useAttitudeEstimator(perception);

		updateBall(perception);
		updateLandmarks(perception);
		updateFieldLines();
		updatePlayers((IRoboCupPerception) perception);

		processHear((IRoboCupPerception) perception);

		// now inform observers about changes
		observer.onStateChange(this);
		return false;
	}

	// KlausTest
	// private void useAttitudeEstimator(IPerception perception)
	// {
	// IGyroRate gyroSensor = agentModel.getTorso().getSensor(IGyroRate.class);
	// if (gyroSensor == null) {
	// return;
	// }
	// Vector3D gyro = gyroSensor.getGyro();
	// // System.out.print("gyro: " + gyro);
	//
	// IAccelerometer accelerometer = agentModel.getTorso().getSensor(
	// IAccelerometer.class);
	// if (accelerometer == null) {
	// return;
	// }
	// Vector3D acceleration = accelerometer.getAcceleration();
	// // System.out.print(" acc: " + acceleration);
	//
	// attitudeEstimator.update(0.020, gyro.getX(), gyro.getY(), gyro.getZ(),
	// acceleration.getX(), acceleration.getY(), acceleration.getZ(), 0.0,
	// 0.0, 0.0 /* magnetometer */);
	// double[] attitude = attitudeEstimator.getAttitude();
	//
	// attitudeEstimation = new Rotation(attitude[0], attitude[1], attitude[2],
	// attitude[3], true);
	//
	// // Rotation oldEstimation = thisPlayer.getOrientation();
	// // System.out.print(" old: "
	// // + Arrays.toString(oldEstimation.getAngles(RotationOrder.ZYX)));
	//
	// // double distance = Rotation.distance(attitudeEstimation, oldEstimation);
	// // System.out.println(" distance: " + distance);
	// }

	protected void processGameState(IRoboCupPerception perception)
	{
		// update game time
		IGameStatePerceptor perceptor = perception.getGameState();
		if (perceptor == null) {
			return;
		}
		gameTime = perceptor.getTime();

		// update playSide
		String playSideString = perceptor.getTeamSide();
		PlaySide newPlaySide = PlaySide.parsePlaySide(playSideString);

		switch (newPlaySide) {
		case LEFT:
			// System.out.println("PlaySide left");
			if (playSide != PlaySide.LEFT) {
				// We switched from right to left side, so remember the new side and
				// mirror the flags
				setPlaySide(PlaySide.LEFT);
			}
			break;
		case RIGHT:
			// System.out.println("PlaySide right");
			if (playSide != PlaySide.RIGHT) {
				// We switched from left to right side, so remember the new side and
				// mirror the flags
				setPlaySide(PlaySide.RIGHT);
			}
			break;
		case UNKNOWN:
		default:
			// No playSide?!? Maybe I'm the referee?
			break;
		}

		// update team color
		teamColor = perceptor.getTeamColor();

		// update playMode
		String playModeString = perceptor.getPlaymode();
		previousGameState = gameState;

		playmode = PlayMode.parsePlayMode(playModeString);
		gameState = GameState.determineGameState(playmode, playSide);

		// update scored goals
		updateGoalsScored(perception);
	}

	private void mirrorLandmarks()
	{
		Map<String, ILandmark> newLandmarks = new HashMap<>();

		// Clear list of reference points
		referencePoints.clear();

		for (String key : landmarks.keySet()) {
			Vector3D pos = landmarks.get(key).getKnownPosition();
			Landmark landmark = new Landmark(key, new Vector3D(-pos.getX(), -pos.getY(), pos.getZ()));
			newLandmarks.put(key, landmark);
			referencePoints.add(landmark);
		}

		landmarks.clear();
		landmarks.putAll(newLandmarks);
	}

	/**
	 * Update the number of scored goals
	 */
	private void updateGoalsScored(IRoboCupPerception perception)
	{
		IGameStatePerceptor gameState = perception.getGameState();
		switch (getPlaySide()) {
		case LEFT:
			goalsWeScored = gameState.getScoreLeft();
			goalsTheyScored = gameState.getScoreRight();

			break;
		case RIGHT:
			goalsWeScored = gameState.getScoreRight();
			goalsTheyScored = gameState.getScoreLeft();
			break;
		case UNKNOWN:
			// Don't update the number of scored goals
			break;
		}
	}

	private void processHear(IRoboCupPerception perception)
	{
		try {
			List<IHearPerceptor> hearList = perception.getHearPerceptors();
			for (IHearPerceptor hear : hearList) {
				String target = hear.getTarget();

				if ("self".equalsIgnoreCase(target)) {
					// message from ourselves
					break;
				}

				String message = hear.getMessage();
				if (message.length() != 4 * SayCoder.getPositionLength() + 2) {
					// our messages are fixed length
					break;
				}

				int posStart = 1;
				int posLength = SayCoder.getPositionLength();
				int playerID = SayCoder.decodeID(message.substring(0, posStart));
				Vector3D playerPos = SayCoder.decodePosition(message.substring(posStart, posStart + posLength));
				posStart += posLength;
				Vector3D ballPos = SayCoder.decodePosition(message.substring(posStart, posStart + posLength));

				posStart += posLength;
				Vector3D opponentPos = SayCoder.decodePosition(message.substring(posStart, posStart + posLength));

				posStart += posLength;
				Vector3D opponentPos2 = SayCoder.decodePosition(message.substring(posStart, posStart + posLength));

				posStart += posLength;
				String teamCode = message.substring(posStart, posStart + 1);

				if (!SayCoder.encodeName(getTeamname(true)).equals(teamCode)) {
					// message is not from our team
					break;
				}

				if (playerPos != null) {
					updatePlayerFromHear(playerID, true, playerPos);
				}

				if (ballPos != null) {
					updateBallFromHear(ballPos);
				}

				if (opponentPos != null) {
					updatePlayerFromHear(playerID, false, opponentPos);
				}

				if (opponentPos2 != null) {
					updatePlayerFromHear(
							((playerID + 5) % IMagmaConstants.NUMBER_OF_PLAYERS_PER_TEAM) + 1, false, opponentPos2);
				}
			}

		} catch (Exception e) {
			// might happen with opponent messages
		}
	}

	public void updateBallFromHear(Vector3D ballPos)
	{
		if (ball.getAge(globalTime) < TIME_TO_TRUST_HEAR && ball.getInformationSource() == InformationSource.VISION) {
			// We have recently seen the ball so no update
			return;
		}

		Vector3D localPos = thisPlayer.calculateLocalPosition(ballPos);
		ball.updateFromAudio(localPos, ballPos, globalTime);
	}

	/**
	 * Updates the local and global position of the corresponding player instance
	 * according to the given global position.
	 *
	 * @param playerID the id of the player
	 * @param ownTeam true if the player to update is of the own team
	 * @param playerPos the new global position
	 */
	private void updatePlayerFromHear(int playerID, boolean ownTeam, Vector3D playerPos)
	{
		String teamName = getTeamname(ownTeam);
		if (teamName == null) {
			// As long we don't know the other team's name, we aren't able to
			// create proper player instances
			return;
		}

		Player player = (Player) knownPlayers.get(teamName + playerID);
		if (player == null) {
			// We hear from a player that we have never seen before
			player = new Player(playerID, teamName, ownTeam, getAgentModel().getCycleTime());
			knownPlayers.put(teamName + playerID, player);
		}

		if (player.getAge(globalTime) < TIME_TO_TRUST_HEAR &&
				player.getInformationSource() == InformationSource.VISION) {
			// We have recently seen the player so no update
			return;
		}

		// Update player information
		Vector3D localPos = thisPlayer.calculateLocalPosition(playerPos);
		player.updateFromAudio(localPos, playerPos, globalTime);
	}

	/**
	 * If ownTeam is true we return the team name of thisPlayer. If false we
	 * return the name of the first seen other player ever. If we haven't seen
	 * any other player yet, this method returns <tt>null</tt>.
	 * @param ownTeam true if from own team, false if from other team
	 * @return the name of the team, if known, null if not
	 */
	public String getTeamname(boolean ownTeam)
	{
		if (ownTeam) {
			return thisPlayer.getTeamname();
		} else {
			return otherTeamName;
		}
	}

	/**
	 * @param perception the object containing the result from parsing
	 */
	protected void calculatePosition(IPerception perception)
	{
		IGlobalPosePerceptor globalPose = perception.getGlobalPose();
		if (globalPose != null) {
			calculatePositionFromGlobalPose(globalPose);
			return;
		}

		// Extract visible lines of the current cycle
		List<ILinePerceptor> visibleLines = perception.getVisibleLines();
		List<ILocalizationLine> visibleFieldLines = new ArrayList<>(visibleLines.size());
		for (ILinePerceptor pl : visibleLines) {
			visibleFieldLines.add(
					new LocalizationLine(toRootVisionSystem(pl.getPosition()), toRootVisionSystem(pl.getPosition2())));
		}

		// Reset reference lines (and implicitly the field lines as well)
		for (IReferenceLine line : referenceLines) {
			line.setVisible(false);
		}

		// Extract visible markers of the current cycle
		for (String name : landmarks.keySet()) {
			Landmark landmark = (Landmark) landmarks.get(name);
			IVisibleObjectPerceptor landmarkSeen = perception.getVisibleObject(name);
			if (landmarkSeen != null) {
				// this landmark is currently visible, so update it's local position
				// and store it to the visible flags for localization
				landmark.updateLocalPosition(toRootVisionSystem(landmarkSeen.getPosition()));
			} else {
				landmark.setVisible(false);
			}
		}

		// Calculate an estimation of the current orientation, based on the gyro
		// sensor in the root body (if existing)
		Rotation orientationEstimation = null;
		IRoboCupAgentModel agentModel = getAgentModel();
		IGyroRate gyro = agentModel.getRootBodyPart().getSensor(IGyroRate.class);

		// If we have a gyro sensor in the root body, use it to estimate the
		// current orientation
		if (gyro != null) {
			Rotation globalGyro =
					Geometry.zTransformRotation(gyro.getOrientationChange(agentModel.getCycleTime()), -Math.PI / 2);
			orientationEstimation = thisPlayer.getGlobalOrientation().applyTo(globalGyro);
		} else {
			ICompass compass = agentModel.getRootBodyPart().getSensor(ICompass.class);

			// If we have a compass sensor in the root body, use it to estimate the
			// current orientation
			if (compass != null) {
				orientationEstimation =
						new Rotation(Vector3D.PLUS_K, compass.getAngle().radians(), RotationConvention.VECTOR_OPERATOR);
			}
		}

		// perform localization
		Pose3D localizeInfo = localize(visibleFieldLines, orientationEstimation);
		if (localizeInfo != null) {
			updateFromVision(localizeInfo);
			thisPlayer.setGlobalOrientation(orientationFilter.filterOrientation(localizeInfo.getOrientation()));
		} else {
			if (orientationEstimation == null) {
				orientationEstimation = thisPlayer.getGlobalOrientation();
			}
			thisPlayer.setGlobalOrientation(orientationEstimation);
			orientationFilter.reset();
		}
	}

	protected void updateFromVision(Pose3D localizeInfo)
	{
		thisPlayer.updateFromVision(Vector3D.ZERO, Vector3D.ZERO,
				posFilter.filterPosition(
						localizeInfo.getPosition(), thisPlayer.getPosition(), thisPlayer.getIntendedGlobalSpeed()),
				globalTime);
	}

	protected void calculatePositionFromGlobalPose(IGlobalPosePerceptor globalPose)
	{
		Pose3D localizeInfo = globalPose.getGlobalPose();
		Vector3D position = localizeInfo.getPosition();
		thisPlayer.updateFromVision(Vector3D.ZERO, Vector3D.ZERO, position, globalTime);
		thisPlayer.setGlobalOrientation(localizeInfo.getOrientation());
	}

	/**
	 * @param perception the object containing the result from parsing
	 */
	protected void updateBall(IPerception perception)
	{
		// HACK === For Dresden
		// IVisibleObjectPerceptor ballVision = perception
		// .getVisibleObject(IPerception.BALL);
		// if (ballVision != null) {
		// ball.updateFromVision(ballVision.getPosition(),
		// ballVision.getPosition(), globalTime);
		// }
		// HACK === For Dresden

		IVisibleObjectPerceptor ballVision = perception.getVisibleObject(IRoboCupPerception.BALL);
		if (ballVision == null) {
			// the ball is currently not visible. This will be true every 2 out of
			// 3 cycles without camera image
			ball.setVisible(false);
			// ball.updateNoVision(globalTime);
			return;
		}

		Vector3D ballPosition = ballVision.getPosition();
		if (!ballVision.hasDepth()) {
			IRoboCupAgentModel agentModel = getAgentModel();
			IBodyPart cameraBodyPart = agentModel.getBodyPartContainingCamera();
			if (cameraBodyPart == null) {
				ball.setVisible(false);
				return;
			}
			// Pose3D cameraPose = cameraBodyPart.getPose();
			Pose3D cameraPose = agentModel.getCameraPose();
			ballPosition = estimateDepth(thisPlayer.getPose(), cameraPose, ballPosition, ball.getRadius());
			if (ballPosition == null) {
				ball.setVisible(false);
				return;
			}
		}
		Vector3D localPos = toRootVisionSystem(ballPosition);
		Vector3D globalPos = thisPlayer.calculateGlobalPosition(localPos);

		ball.updateFromVision(ballPosition, localPos, globalPos, globalTime);
	}

	protected Vector3D estimateDepth(
			Pose3D thisPlayerPose, Pose3D cameraPose, Vector3D position, float heightAboveGround)
	{
		// transform camera to global coordinate system
		Pose3D rotationVision = new Pose3D(
				Vector3D.ZERO, new Rotation(Vector3D.PLUS_K, Math.PI / 2, RotationConvention.VECTOR_OPERATOR));
		Pose3D rotationTorso = new Pose3D(
				Vector3D.ZERO, new Rotation(Vector3D.PLUS_K, -Math.PI / 2, RotationConvention.VECTOR_OPERATOR));
		Pose3D globalPose = thisPlayerPose.applyTo(rotationTorso).applyTo(cameraPose).applyTo(rotationVision);

		// intersect ray to ball with field plane
		Plane objectPlane = new Plane(new Vector3D(0, 0, heightAboveGround), Vector3D.PLUS_K, 0.001);
		Vector3D p1 = globalPose.getPosition();
		Vector3D p2 = globalPose.applyTo(position);
		Line line = new Line(p1, p2, 0.001);
		Vector3D intersection = objectPlane.intersection(line);
		if (intersection == null) {
			return null;
		}

		// if the intersection is behind us, we do not accept that it is the ball
		// if (intersection.getX() < 0) {
		// // wrong ball or not on ground
		// // System.out.println("wrong");
		// return null;
		// }

		// convert back into camera coordinate system
		double distance = Vector3D.distance(p1, intersection);
		if (position.getNorm() < 0.00001) {
			position = Vector3D.PLUS_I;
		}
		return position.normalize().scalarMultiply(distance);
	}

	/**
	 * Updates the local and global position of the visible landmarks.
	 *
	 * @param perception the object containing the result from parsing
	 */
	protected void updateLandmarks(IPerception perception)
	{
		for (String name : landmarks.keySet()) {
			Landmark landmark = (Landmark) landmarks.get(name);
			IVisibleObjectPerceptor landmarkSeen = perception.getVisibleObject(name);
			if (landmarkSeen == null) {
				// this landmark is currently not visible
				landmark.setVisible(false);
			} else {
				Vector3D localPos = toRootVisionSystem(landmarkSeen.getPosition());
				Vector3D globalPos = thisPlayer.calculateGlobalPosition(localPos);

				landmark.updateFromVision(landmarkSeen.getPosition(), localPos, globalPos, globalTime);
			}
		}
	}

	/**
	 * Updates the global position of the as visible marked field lines.
	 */
	private void updateFieldLines()
	{
		fieldLines.values().stream().filter(IVisibleObject::isVisible).forEach(fieldLine -> {
			((FieldLine) fieldLine)
					.updatePositions(thisPlayer.calculateGlobalPosition(fieldLine.getLocalPosition1()),
							thisPlayer.calculateGlobalPosition(fieldLine.getLocalPosition2()), globalTime);
		});
	}

	/**
	 * Updates the player information according to the new visual perception.
	 *
	 * @param perception the object containing the result from vision parsing
	 */
	protected void updatePlayers(IRoboCupPerception perception)
	{
		// We only act when vision information should be present
		if (!perception.containsVision()) {
			return;
		}

		// Clear visible players
		visiblePlayers.clear();

		// Process player visions
		List<IPlayerPos> playersVision = perception.getVisiblePlayers();
		Player currentPlayer;
		boolean isOwnTeam;

		for (IPlayerPos playerVision : playersVision) {
			isOwnTeam = thisPlayer.getTeamname().equals(playerVision.getTeamname());

			// Don't process myself as a teammate
			if (isOwnTeam && playerVision.getId() == thisPlayer.getID()) {
				continue;
			}

			// Fetch a player instance
			IRoboCupAgentModel agentModel = getAgentModel();
			if (playerVision.getTeamname() == null ||
					IMagmaConstants.UNKNOWN_PLAYER_TEAMNAME.equals(playerVision.getTeamname()) ||
					playerVision.getId() == IMagmaConstants.UNKNOWN_PLAYER_NUMBER) {
				// If the player vision information is not complete, create just a
				// temporary player instance to hold the perceived information and
				// add it to the visiblePlayers array
				// TODO: Instead of simply adding an additional player by default,
				// one can perform some kind of lookup in the known players map and
				// try to match the player to an previously known player near by
				currentPlayer = new Player(
						playerVision.getId(), playerVision.getTeamname(), isOwnTeam, agentModel.getCycleTime());
				visiblePlayers.add(currentPlayer);
			} else {
				// If the player vision information is complete, fetch or create a
				// permanent instance for him
				currentPlayer = (Player) knownPlayers.get(playerVision.getTeamname() + playerVision.getId());
				if (currentPlayer == null) {
					currentPlayer = new Player(
							playerVision.getId(), playerVision.getTeamname(), isOwnTeam, agentModel.getCycleTime());
					knownPlayers.put(currentPlayer.getTeamname() + currentPlayer.getID(), currentPlayer);

					// Remember the name of the other team once we see the first
					// other player
					if (otherTeamName == null && !isOwnTeam) {
						otherTeamName = playerVision.getTeamname();
					}
				}
			}

			// Update the player information
			updatePlayer(currentPlayer, playerVision);
		}

		// Extract all recently seen players from known players map and mark the
		// others as not visible
		for (IPlayer player : knownPlayers.values()) {
			if (player.getAge(globalTime) < REMEMBRANCE_TIME) {
				// TODO: check if the player is in the visible cone and should have
				// been seen therefore

				visiblePlayers.add(player);
			} else {
				player.setVisible(false);
			}
		}
	}

	/**
	 * Updates a player's body parts as well as its local and global position
	 *
	 * @param player the player to update
	 * @param playerVision the update information
	 */
	protected void updatePlayer(Player player, IPlayerPos playerVision)
	{
		Vector3D localPos;
		Vector3D globalPos;

		// Transform body parts of player
		Map<String, Vector3D> bodyParts = new HashMap<>();
		Map<String, Vector3D> allBodyParts = playerVision.getAllBodyParts();
		for (String name : allBodyParts.keySet()) {
			localPos = toRootVisionSystem(allBodyParts.get(name));
			globalPos = thisPlayer.calculateGlobalPosition(localPos);

			bodyParts.put(name, globalPos);
		}

		// Transform player positions
		localPos = toRootVisionSystem(playerVision.getPosition());
		globalPos = thisPlayer.calculateGlobalPosition(localPos);

		// Update positions and body parts of player
		player.update(playerVision.getPosition(), localPos, globalPos, bodyParts, globalTime);
	}

	/**
	 * @param id the player's number (1-11)
	 * @param ownTeam true if we want the player of our own team
	 * @return the referenced player, null if not in list
	 */
	@Override
	public IPlayer getVisiblePlayer(int id, boolean ownTeam)
	{
		if (id < 1) {
			return null;
		}
		for (IPlayer player : visiblePlayers) {
			if (id == player.getID() && ownTeam == player.isOwnTeam()) {
				return player;
			}
		}
		return null;
	}

	// --------------------------------------------------
	// General environment and game information
	/**
	 * @return the time the game has been running
	 */
	@Override
	public float getGameTime()
	{
		return gameTime;
	}

	@Override
	public int getGoalsTheyScored()
	{
		return goalsTheyScored;
	}

	@Override
	public int getGoalsWeScored()
	{
		return goalsWeScored;
	}

	/**
	 * Currently only known for Humanoid League!
	 * @return the color of our team
	 */
	@Override
	public TeamColor getTeamColor()
	{
		return teamColor;
	}

	/**
	 * @return the current mode of the game as String
	 */
	@Override
	public PlayMode getPlaymode()
	{
		return playmode;
	}

	@Override
	public PlaySide getPlaySide()
	{
		return playSide;
	}

	private void setPlaySide(PlaySide playSide)
	{
		this.playSide = playSide;
		mirrorLandmarks();
	}

	@Override
	public GameState getGameState()
	{
		return gameState;
	}

	// --------------------------------------------------
	// Visual object getters
	@Override
	public IBall getBall()
	{
		return ball;
	}

	public void setBall(Ball ball)
	{
		this.ball = ball;
	}

	@Override
	public List<IVisibleObject> getGoalPostObstacles()
	{
		// the positions of the goal posts are slightly outside the field to allow
		// a bigger radius for avoidance
		List<IVisibleObject> result = new ArrayList<>(4);
		for (ILandmark lm : getLandmarks()) {
			double shiftOutside = 0.5;
			if (lm.getName().contains("G")) {
				Vector3D knownPos = lm.getKnownPosition();

				if (knownPos.getX() < 0) {
					shiftOutside = -shiftOutside;
				}
				result.add(new Landmark(
						lm.getName() + "Obstacle", new Vector3D(knownPos.getX() + shiftOutside, knownPos.getY(), 0)));
			}
		}
		return result;
	}

	@Override
	public Vector2D goalLineIntersection(Vector2D point1, Vector2D point2, double offset)
	{
		Vector2D otherLeftGoalPost = new Vector2D(fieldHalfLength(), goalHalfWidth());
		Vector2D otherRightGoalPost = new Vector2D(fieldHalfLength(), -goalHalfWidth());
		org.apache.commons.math3.geometry.euclidean.twod.Line goalLine =
				new org.apache.commons.math3.geometry.euclidean.twod.Line(
						otherLeftGoalPost, otherRightGoalPost, 0.00001);
		org.apache.commons.math3.geometry.euclidean.twod.Line line =
				new org.apache.commons.math3.geometry.euclidean.twod.Line(point1, point2, 0.00001);
		Vector2D intersection = line.intersection(goalLine);
		if (intersection != null) {
			if (Math.abs(intersection.getY()) < goalHalfWidth() - offset) {
				return intersection;
			}
		}
		return null;
	}

	@Override
	public void setGameMode(GameMode mode)
	{
		this.gameMode = mode;
	}

	@Override
	public List<IPlayer> getVisiblePlayers()
	{
		return Collections.unmodifiableList(visiblePlayers);
	}

	public void setVisiblePlayers(List<IPlayer> players)
	{
		this.visiblePlayers = players;
	}

	@Override
	public IThisPlayer getThisPlayer()
	{
		return thisPlayer;
	}

	// --------------------------------------------------
	// EnvironmentModel Methods

	// --------------------------------------------------
	// Server version and soccer pitch description delegates
	@Override
	public int getServerVersion()
	{
		return serverVersion;
	}

	@Override
	public float fieldHalfLength()
	{
		return soccerPitch.fieldHalfLength;
	}

	@Override
	public float fieldHalfWidth()
	{
		return soccerPitch.fieldHalfWidth;
	}

	@Override
	public float penaltyHalfLength()
	{
		return soccerPitch.penaltyHalfLength;
	}

	@Override
	public float penaltyWidth()
	{
		return soccerPitch.penaltyWidth;
	}

	@Override
	public float goalHalfWidth()
	{
		return soccerPitch.goalHalfWidth;
	}

	@Override
	public float goalHeight()
	{
		return soccerPitch.goalHeight;
	}

	@Override
	public float goalDepth()
	{
		return soccerPitch.goalDepth;
	}

	@Override
	public float centerCircleRadius()
	{
		return soccerPitch.centerCircleRadius;
	}

	@Override
	public Vector3D getOwnGoalPosition()
	{
		return soccerPitch.ownGoalPosition;
	}

	@Override
	public Vector3D getOtherGoalPosition()
	{
		return soccerPitch.otherGoalPosition;
	}

	@Override
	public boolean isBallInCriticalArea()
	{
		Vector3D ballPos = getBall().getPosition();

		if (gameMode == GameMode.PENALTY) {
			return ballPos.getX() < -fieldHalfLength() + penaltyWidth() - 0.3 &&
					Math.abs(ballPos.getY()) < penaltyHalfLength() + goalHalfWidth() - 0.3;
		} else {
			return ballPos.getX() < -fieldHalfLength() + penaltyWidth() * 2 - 0.3 &&
					Math.abs(ballPos.getY()) < penaltyHalfLength() + goalHalfWidth() * 2 - 0.3;
		}
	}

	@Override
	public BallPassing ballIsPassing(Vector3D futureBallPosition)
	{
		Vector3D ballPosition = getBall().getPosition();
		Line2D ballLine = new Line2D.Double(
				futureBallPosition.getX(), futureBallPosition.getY(), ballPosition.getX(), ballPosition.getY());

		Angle playerAngle = getThisPlayer().getHorizontalAngle();

		Vector3D direction = new Vector3D(playerAngle.add(Math.PI / 2).radians(), 0).normalize();
		double length = 0.15;

		Vector3D playerPosition = getThisPlayer().getPosition();
		Vector3D centerLeft = playerPosition.add(direction.scalarMultiply(length));
		Vector3D centerRight = playerPosition.subtract(direction.scalarMultiply(length));
		Line2D playerDiameterCenter =
				new Line2D.Double(centerLeft.getX(), centerLeft.getY(), centerRight.getX(), centerRight.getY());
		if (playerDiameterCenter.intersectsLine(ballLine)) {
			return BallPassing.CENTER;
		}

		length = getAgentModel().getHeight() * 0.4;
		centerLeft = playerPosition.add(direction.scalarMultiply(length));
		Line2D leftLine =
				new Line2D.Double(centerLeft.getX(), centerLeft.getY(), playerPosition.getX(), playerPosition.getY());
		if (leftLine.intersectsLine(ballLine)) {
			return BallPassing.SHORT_LEFT;
		}

		centerRight = playerPosition.subtract(direction.scalarMultiply(length));
		Line2D rightLine =
				new Line2D.Double(centerRight.getX(), centerRight.getY(), playerPosition.getX(), playerPosition.getY());
		if (rightLine.intersectsLine(ballLine)) {
			return BallPassing.SHORT_RIGHT;
		}

		Vector3D left = playerPosition.add(direction.scalarMultiply(length * 3));
		Line2D playerDiameterLeft =
				new Line2D.Double(playerPosition.getX(), playerPosition.getY(), left.getX(), left.getY());
		if (playerDiameterLeft.intersectsLine(ballLine)) {
			return BallPassing.FAR_LEFT;
		}

		Vector3D right = playerPosition.subtract(direction.scalarMultiply(length * 3));
		Line2D playerDiameterRight =
				new Line2D.Double(playerPosition.getX(), playerPosition.getY(), right.getX(), right.getY());
		if (playerDiameterRight.intersectsLine(ballLine)) {
			return BallPassing.FAR_RIGHT;
		}

		// the ball is outside of the player's reach
		return BallPassing.UNREACHABLE;
	}

	// --------------------------------------------------
	// Basic object methods
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof RoboCupWorldModel)) {
			return false;
		}
		RoboCupWorldModel other = (RoboCupWorldModel) o;

		if (!ball.equals(other.ball)) {
			return false;
		}

		if (!ball.equals(other.ball)) {
			return false;
		}

		if (!landmarks.equals(other.landmarks)) {
			return false;
		}

		return visiblePlayers.equals(other.visiblePlayers);
	}

	@Override
	protected IRoboCupAgentModel getAgentModel()
	{
		return (IRoboCupAgentModel) super.getAgentModel();
	}
}
