# magmaFatProxy

Fat Proxy for the [RoboCup 3D Soccer Simulation League](http://wiki.robocup.org/wiki/Soccer_Simulation_League) created by the [magmaOffenburg team](http://robocup.hs-offenburg.de/).

The Fat Proxy is a proxy for client agents, connecting to the
Simspark server. It extends the magmaProxy by supporting high level dash and kick commands for agents.
The proxy is developed using Java 1.8.
It should be compatible with older JREs, too, but this is not verified.

![](screenshots/magmaProxy.png)

## Contents

- [Installation](#installation)
- [Preparation](#preparation)
- [Usage](#usage)
- [Command Line Interface](#command-line-interface)
- [Concepts](#concepts)
- [High Level Commands](#high-level-commands)
- [Implementation](#implementation)
- [Command Line Parameter](#command-line-parameter)
- [Authors](#authors)
- [Contact](#contact)

## Installation

- Clone the repository: `git clone https://github.com/magmaOffenburg/magmaFatProxy`.
- Make sure you have Java installed and available on your command line.
- Make sure you have [simspark and rcsserver3d](https://gitlab.com/robocup-sim/SimSpark/-/wikis/home) installed and the command `rcssserver3d` is available on your command line.
- The Fat Proxy itself does not require an installation.

For contributions, please set up `clang-format` as described [here](https://github.com/hsoautonomy/formatting). 

## Preparation
Before you can use the Fat Proxy, you have to configure the soccer server to run in sync mode.
Change attribute `agentSyncMode` in the config file of the server `~/.simspark/spark.rb` to true.

## Usage
The process of running a soccer game with the Fat Proxy is as follows:

1. Start the server on the server computer with `rcssserver3d`.
2. Start a Fat Proxy instance on each client computer (see below).
3. Start the agents making sure they get the Fat Proxy port passed to the `start.sh` script

To simplify starting of the Fat Proxy, a start script is provided:

`bash start.sh <simspark-server-IP> <simspark-server-Port> <fatproxy-server-port>`

- `<simspark-server-IP>`: IP address of the soccer server.
- `<simspark-server-Port>`: Port of the soccer server.
- `<fatproxy-server-port>`: Port on the Fat Proxy's machine to which the players have to connect.


## Command Line Interface

The following commands are provided when the Fat Proxy is running:

- "q", or "quit" : Shutdown the Fat Proxy server and exit the program
- "l", or "list" : List active agent Fat Proxy instances
- "s", or "status" : Print Fat Proxy live status and list of all agent proxy instances 
- "v", or "verbose" : Verbosly list active agent proxies
- "m" : Switch on/off printing start of all client messages
- "n" : Switch on/off printing start of all server messages

## Concepts
To have simplified access to the simspark simulation, the Fat Proxy takes over all motor control of the simulated Nao robots.
Agents control the robot with high level dash and kick commands. Anything else like getting up or focusing the head is done by the Fat Proxy.

This is achieved by forwarding simspark observations to both the agent and our magma fatproxy agent. Currently, all observations are forwarded to the agent. This will probably change in future versions, where we plan to remove all motor perceptions. Actions sent by the agent are dispatched as follows: high level dash and kick commands are forwarded to the magma fatproxy agent, that translates them into motor commands. Low level commands including "beam", "say", "syn", "init", "scene" and "pass" are directly sent to the server together with the motor commands of the magma fatproxy agent. Motor commands possibly contained in the agent actions will be removed.

Any features introduced with the (non fat) proxy are also available. Actually, Fat Proxy is completely built onto the magmaProxy.jar.

## High Level Commands
The following high level commands are supported by the magmaFatProxy.

### dash
`(proxy dash <forward/backward> <left/right> <turnAngle>)`
- `<forward/backward>`: How much speed we want in axial direction [-100 (backward) .. 100 (forward)]
- `<left/right>`: How much speed we want in side-wise direction [-100 (right) .. 100 (left)]
- `<turnAngle>`: How much we want to turn with each step  [-60 (right) .. 60 (left)] (in degrees)

Example: `(proxy dash 100.0 0.0 0.58)`: Full speed forward with slight turn to left.

Currently, the FatProxy performs tests to cut off parameter combinations that will not work and make the agent fall like (proxy dash 100 100 60). This might be removed in future releases.

### kick
`(proxy kick <distance> <horizontalAngle> <verticalAngle>)`
- `<distance>`: The desired distance in m (however, see kick model)
- `<horizontalAngle>`: The horizontal kick direction relative to the player (in degrees, 0 is straight)
- `<verticalAngle>`: The (positive) vertical angle for high kicking ([0..90] in degrees)

Example: `(proxy kick 8.5 -50.028 10.0)`: Kick 8.5m 50 degrees to the right with an initial 10 degrees vertical trajectory.

The magmaFatProxy uses a 3D adaption of the kick model used in the 2D soccer simulation league. 
effectiveKickPower = kickPower * (1 - 0.25 * deltaAngle - 0.25 * distanceBall / KICKABLE_MARGIN)
- deltaAngle: The relative horizontal angle of the ball with respect to the player divided by 180. 
(`Math.abs(torso.getHorizontalAngleTo(ball).degrees()) / 180`). So a ball behind the player can only be kicked with 25% less power.
- distanceBall: The 3D distance of the ball to the center of the torso. So a far ball can only be kicked with 25% less power. If the ball is outside the KICKABLE_MARGIN, it will not be kicked at all. The failed kick command will make the agent slowly walk forward (10,0,0).
- KICKABLE_MARGIN: 0.55m which means that a ball on the ground can roughly be kicked at a distance of 0.4m. The agent may kick a ball also in the air.

It is important, that a kick command takes at least 3 simulation cycles to be performed. So the agent has to send a kick command for at least 3 consecutive cycles. If an agent sends both dash and kick commands within one cycles, the kick is prioritized.

## Implementation
The magmaFatProxy extension of magmaProxy is implemented in Java and separated across two main classes: 
SimsparkAgentFatProxyServer and AgentFatProxy. 

- The SimsparkAgentFatProxyServer extends the magmaProxy SimsparkAgentProxyServer with a monitor connection used for monitor kicks and creates the AgentFatProxy instances.

- The AgentFatProxy extends extends the magmaProxy AgentProxy class and does all the routing and filtering of messages to agent, fat proxy agent and server. It contains the fundamental logic of the Fat Proxy.

## Command Line Parameter

The magmaFatProxy has the same command line parameters as magmaProxy.

## Authors
Ester Amelia, Maximilian Baritz, Martin Baur, Hannes Braun, Kim Christmann, Alexander Derr, 
Klaus Dorer, Mathias Ehret, Sebastian Eppinger, Jens Fischer, 
Camilo Gelvez, Stefan Glaser, Stefan Grossmann, Marcel Gruessinger, Julian Hohenoecker, Thomas Huber, 
Stephan Kammerer, Fabian Korak, Maximilian Kroeg, Pascal Liegibel, Duy Nguyen, 
Simon Raffeiner, Srinivasa Ragavan, Thomas Rinklin, Bjoern Ritter, 
Mahdi Sadeghi, Joachim Schilling, Rico Schillings, Ingo Schindler, Carmen Schmider, Frederik Sdun, Rajit Shahi, 
Bjoern Weiler, David Weiler, David Zimmermann, Denis Zimmermann

## Contact

klaus dot dorer at hs-offenburg dot de