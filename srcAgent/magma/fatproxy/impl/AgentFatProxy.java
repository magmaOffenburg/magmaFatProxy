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
package magma.fatproxy.impl;

import java.net.Socket;
import java.util.StringTokenizer;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;

import hso.autonomy.agent.agentruntime.AgentRuntime;
import hso.autonomy.agent.model.agentmodel.IBodyPart;
import hso.autonomy.util.connection.ConnectionException;
import hso.autonomy.util.connection.IServerConnection;
import hso.autonomy.util.observer.IObserver;
import hso.autonomy.util.observer.IPublishSubscribe;
import hso.autonomy.util.observer.Subject;
import magma.agent.IMagmaConstants;
import magma.agent.agentruntime.ComponentFactory;
import magma.agent.agentruntime.PlayerParameters;
import magma.agent.agentruntime.RoboCupAgentRuntime;
import magma.agent.communication.perception.ISimsparkPerceptorNames;
import magma.agent.model.worldmodel.IThisPlayer;
import magma.agent.model.worldmodel.impl.RoboCupWorldModel;
import magma.common.spark.IConnectionConstants;
import magma.monitor.general.impl.MonitorRuntime;
import magma.robots.RobotConfigurationHelper;
import magma.robots.nao.INaoConstants;
import magma.robots.nao.model.agentmeta.NaoAgentMetaModel;
import magma.robots.nao1.model.agentmeta.Nao1AgentMetaModel;
import magma.robots.nao2.model.agentmeta.Nao2AgentMetaModel;
import magma.robots.nao3.model.agentmeta.Nao3AgentMetaModel;
import magma.robots.naotoe.model.agentmeta.NaoToeAgentMetaModel;
import magma.tools.proxy.impl.AgentProxy;
import magma.util.roboviz.RoboVizDraw;
import magma.util.roboviz.RoboVizParameters;

/**
 * @author kdorer
 *
 */
public class AgentFatProxy extends AgentProxy implements IServerConnection, ISimsparkPerceptorNames
{
	private static final String[] COMMANDS_TO_KEEP_FROM_CLIENT =
			new String[] {"beam", "say", "syn", "init", "scene", "pass"};
	private static final String[] HIGH_LEVEL_COMMANDS_FROM_CLIENT = new String[] {"proxy"};
	private static final String[] PERCEPTIONS_TO_REMOVE_FROM_SERVER = new String[] {
			//
			LToesP, LFootRollP, LFootPitchP, LKneePitchP, LHipPitchP, LHipRollP, LHipYawP, //
			RToesP, RFootRollP, RFootPitchP, RKneePitchP, RHipPitchP, RHipRollP, RHipYawP, //
			LShoulderPitchP, LShoulderYawP, LShoulderRollP, LArmYawP,					   //
			RShoulderPitchP, RShoulderYawP, RShoulderRollP, RArmYawP,					   //
	};

	// TODO: should we exclude head, foot force, gyro also?
	//	NeckYawP = "hj1";
	//	NeckPitchP = "hj2";
	// "RFootForce", "rf", "RToeForce", "rtf"
	// "TorsoGyro", "torsoGyro"
	//	"TorsoAccel", "torsoAccel"

	/** the observing channel(s) */
	protected final IPublishSubscribe<byte[]> observer;

	/** connection state */
	private boolean connected;

	/** the last message received from the server */
	private byte[] lastMessageFromServer;

	/** the message from the fat proxy agent runtime for the server */
	private byte[] lastAgentRuntimeToServerMessage;

	private String robotType;

	private String teamName;

	private int playerID;

	private AgentRuntime agentRuntime;

	private MonitorRuntime monitor;

	/**
	 * @param clientSocket
	 * @param ssHost
	 * @param ssPort
	 * @param showMessages
	 * @param monitor
	 */
	public AgentFatProxy(Socket clientSocket, String ssHost, int ssPort, boolean showMessages, MonitorRuntime monitor)
	{
		super(clientSocket, ssHost, ssPort, showMessages);
		this.monitor = monitor;
		observer = new Subject<>();
		connected = false;
	}

	@Override
	protected byte[] onNewServerMessage(byte[] message)
	{
		lastMessageFromServer = message;

		// TODO remove sensors and add orientation by uncommenting this
		//		byte[] noSensors = removeNodes(message, PERCEPTIONS_TO_REMOVE_FROM_SERVER);
		//		noSensors = addPoseToMessage(noSensors);
		//		return super.onNewServerMessage(noSensors);

		return super.onNewServerMessage(message);
	}

	/**
	 * Adds torso and head pose to message
	 * @param message the message from the server
	 * @return the message with the orientation added as s node
	 */
	private byte[] addPoseToMessage(byte[] message)
	{
		if (agentRuntime == null) {
			return message;
		}

		IThisPlayer thisPlayer = ((RoboCupWorldModel) agentRuntime.getThoughtModel().getWorldModel()).getThisPlayer();
		Rotation tor = thisPlayer.getOrientation();
		IBodyPart head = agentRuntime.getAgentModel().getBodyPart(INaoConstants.Head);
		Rotation hor = head.getOrientation();
		String msg = "(tor " + tor.getQ0() + " " + tor.getQ1() + " " + tor.getQ2() + " " + tor.getQ3() + ")";
		msg += "(hor " + hor.getQ0() + " " + hor.getQ1() + " " + hor.getQ2() + " " + hor.getQ3() + ")";
		byte[] result = appendMessages(message, msg.getBytes());
		return result;
	}

	@Override
	public byte[] onNewClientMessage(byte[] message)
	{
		// called if the client sent a new message (to the server)
		boolean haveScene = false;
		if (agentRuntime == null) {
			haveScene = checkSceneAndInit(message);
		}

		byte[] result = null;

		// inform our agent runtime about the new perceptions. This will trigger a sendMessage() below
		if (agentRuntime != null && lastMessageFromServer != null) {
			synchronized (this)
			{
				// we do not want to send old server messages
				lastAgentRuntimeToServerMessage = null;
			}

			byte[] highLevel = keepNodes(message, HIGH_LEVEL_COMMANDS_FROM_CLIENT);
			byte[] all = appendMessages(highLevel, lastMessageFromServer);

			lastMessageFromServer = null;
			observer.onStateChange(all);

			// we have to wait until agent runtime wants to send a message to server
			synchronized (this)
			{
				try {
					if (lastAgentRuntimeToServerMessage == null) {
						wait();
					}
					result = lastAgentRuntimeToServerMessage;
					lastAgentRuntimeToServerMessage = null;
				} catch (InterruptedException e) {
				}
			}
		}

		if (result == null) {
			if (haveScene) {
				return super.onNewClientMessage(message);
			} else {
				return "(syn)".getBytes();
			}
		}

		byte[] fromClient = keepNodes(message, COMMANDS_TO_KEEP_FROM_CLIENT);
		byte[] fromAgentRuntime = removeNodes(result, COMMANDS_TO_KEEP_FROM_CLIENT);
		result = appendMessages(fromClient, fromAgentRuntime);

		return result;
	}

	protected boolean checkSceneAndInit(byte[] message)
	{
		String msg = new String(message);
		boolean result = false;
		int scene = msg.indexOf("scene");
		if (scene >= 0) {
			robotType = getRobotType(msg);
			createAgentRuntime();
			result = true;
		}

		int init = msg.indexOf("init");
		if (init >= 0) {
			teamName = getPlayerInfo(msg, init);
			createAgentRuntime();
			result = true;
		}
		return result;
	}

	private void createAgentRuntime()
	{
		if (robotType == null) {
			// no scene message so far
			return;
		}

		if (teamName == null) {
			// no init message so far
			return;
		}

		Thread ourThread = new Thread(() -> {
			ComponentFactory componentFactory = RobotConfigurationHelper.getComponentFactory(robotType);
			componentFactory = new FatproxyComponentFactoryDecorator(componentFactory, this);
			RoboVizParameters roboVizParams =
					new RoboVizParameters(false, RoboVizDraw.DEFAULT_HOST, RoboVizDraw.DEFAULT_PORT, playerID);
			byte teamID = 0;

			PlayerParameters params = new PlayerParameters(teamName, teamID, playerID, IConnectionConstants.SERVER_IP,
					IConnectionConstants.AGENT_PORT, null, IMagmaConstants.DEFAULT_SERVER_VERSION, componentFactory,
					IMagmaConstants.DEFAULT_DECISION_MAKER, roboVizParams, false);
			agentRuntime = new RoboCupAgentRuntime(params, monitor);
			agentRuntime.startClient();
		});
		ourThread.start();
	}

	protected String getRobotType(String msg)
	{
		// get robot type
		String robotType = NaoAgentMetaModel.NAME;
		int heteroIndex = msg.indexOf("hetero");
		if (heteroIndex > 0) {
			int bracketIndex = msg.indexOf(')', heteroIndex);
			String type = (msg.substring(bracketIndex - 1, bracketIndex));
			switch (type) {
			case "1":
				robotType = Nao1AgentMetaModel.NAME;
				break;
			case "2":
				robotType = Nao2AgentMetaModel.NAME;
				break;
			case "3":
				robotType = Nao3AgentMetaModel.NAME;
				break;
			case "4":
				robotType = NaoToeAgentMetaModel.NAME;
				break;
			}
		}
		return robotType;
	}

	private String getPlayerInfo(String msg, int init)
	{
		StringTokenizer tokenizer = new StringTokenizer(msg.substring(init + 5), " ()");
		while (tokenizer.hasMoreTokens() && (teamName == null || playerID == 0)) {
			String token = tokenizer.nextToken();
			if (token.equalsIgnoreCase("unum")) {
				playerID = Integer.parseInt(tokenizer.nextToken());
			} else if (token.equalsIgnoreCase("teamname")) {
				teamName = tokenizer.nextToken();
			}
		}
		return teamName;
	}

	@Override
	public void attach(IObserver<byte[]> newObserver)
	{
		observer.attach(newObserver);
	}

	@Override
	public void establishConnection() throws ConnectionException
	{
		connected = true;
	}

	@Override
	public void sendMessage(byte[] msg) throws ConnectionException
	{
		synchronized (this)
		{
			if (lastAgentRuntimeToServerMessage == null) {
				lastAgentRuntimeToServerMessage = msg;
			} else {
				// more than one message to send: concatenate
				lastAgentRuntimeToServerMessage = appendMessages(lastAgentRuntimeToServerMessage, msg);
			}
			// System.out.println("have message from agent runtime: " + new String(lastAgentRuntimeToServerMessage));
			notify();
		}
	}

	@Override
	public void startReceiveLoop() throws ConnectionException
	{
		// nothing to do here.
	}

	@Override
	public void stopReceiveLoop()
	{
		connected = false;
	}

	@Override
	public boolean isConnected()
	{
		return connected;
	}

	/**
	 * Appends the two passed messages into a new byte array
	 * @param msg1 first message
	 * @param msg2 second message
	 * @return msg1 + msg2
	 */
	static byte[] appendMessages(byte[] msg1, byte[] msg2)
	{
		byte[] result = new byte[msg1.length + msg2.length];
		System.arraycopy(msg1, 0, result, 0, msg1.length);
		System.arraycopy(msg2, 0, result, msg1.length, msg2.length);
		return result;
	}

	/**
	 * Removes all s-expression nodes (including their sub nodes if existing) from message starting with a head passed
	 * in heads
	 * @param msg the message to remove from
	 * @param heads array of head keywords to remove
	 * @return a new message not containing the specified nodes
	 */
	static byte[] removeNodes(byte[] msg, String[] heads)
	{
		StringBuffer result = new StringBuffer(msg.length);
		String message = new String(msg);

		for (int i = 0; i < message.length(); i++) {
			char letter = message.charAt(i);
			if (letter == '(') {
				// check if one of the keywords appeared
				boolean found = false;
				for (String keyword : heads) {
					if (message.startsWith(keyword, i + 1)) {
						found = true;
						// remove whole node including sub nodes
						i += keyword.length() + 1;
						int level = 0;
						do {
							char skipLetter = message.charAt(i);
							if (skipLetter == ')') {
								level--;
								if (level < 0) {
									break;
								}
							}
							if (skipLetter == '(') {
								level++;
							}
							i++;

						} while (i < msg.length);
						break;
					}
				}
				if (!found) {
					result.append(letter);
				}

			} else {
				result.append(letter);
			}
		}

		return result.toString().getBytes();
	}

	/**
	 * Creates a new message with all s-expression nodes (including their sub nodes if existing) that start with one of
	 * the passed keywords in heads
	 * @param msg the message to copy from
	 * @param heads array of head keywords to keep
	 * @return a new message only containing the specified nodes
	 */
	static byte[] keepNodes(byte[] msg, String[] heads)
	{
		StringBuffer result = new StringBuffer(msg.length);
		String message = new String(msg);

		for (int i = 0; i < message.length(); i++) {
			char letter = message.charAt(i);
			if (letter == '(') {
				// check if one of the keywords appeared
				boolean found = false;
				for (String keyword : heads) {
					if (message.startsWith(keyword, i + 1)) {
						found = true;
						result.append(letter);
						break;
					}
				}
				// remove or keep whole node including sub nodes
				int level = 0;
				do {
					i++;
					char skipLetter = message.charAt(i);
					if (found) {
						result.append(skipLetter);
					}
					if (skipLetter == ')') {
						level--;
						if (level < 0) {
							break;
						}
					}
					if (skipLetter == '(') {
						level++;
					}

				} while (i < msg.length - 1);

			} else {
				result.append(letter);
			}
		}

		return result.toString().getBytes();
	}
}
