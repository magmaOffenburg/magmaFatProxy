package magma.monitor.referee.impl;

import hso.autonomy.util.misc.UnixCommandUtil;
import hso.autonomy.util.timing.AlarmTimer;
import hso.autonomy.util.timing.ITriggerReceiver;
import java.io.IOException;
import magma.monitor.command.IServerCommander;
import magma.monitor.referee.IReferee;
import magma.monitor.worldmodel.IMonitorWorldModel;

public abstract class RefereeBase implements IReferee, ITriggerReceiver
{
	protected IMonitorWorldModel worldModel;

	protected IServerCommander serverCommander;

	/** a watchdog for hanging server connections */
	protected AlarmTimer timer;

	private int timerCounter;

	/** time when the alarm was last setup */
	private long lastSetupTime;

	/** the process id of the server, null if unknown */
	private String serverPid;

	protected RefereeState state;

	public RefereeBase(IMonitorWorldModel mWorldModel, IServerCommander serverCommander, String serverPid)
	{
		this.worldModel = mWorldModel;
		this.serverCommander = serverCommander;
		this.serverPid = serverPid;
		state = RefereeState.CREATED;
	}

	protected void setupTimer(long timeout)
	{
		timerCounter++;
		long time = System.currentTimeMillis();
		if (time - lastSetupTime > 2000) {
			// every some seconds we reset the alarm since the server does
			// obviously not hang
			// System.out.println("Resetup Timer..." + worldModel.getTime() + " id:
			// "
			// + timerCounter);
			stopTimer();
			timer = new AlarmTimer("Alarm-" + timeout + " id: " + timerCounter, this, timeout);
			lastSetupTime = System.currentTimeMillis();
		}
	}

	protected void stopTimer()
	{
		if (timer != null) {
			timer.stopAlarm();
		}
	}

	@Override
	public void trigger(String name)
	{
		// / tce.killServer(); does not work, server does not get it
		try {
			if (serverPid == null || serverPid.isEmpty()) {
				// we did not get a pid, so we assume it is the only server running
				System.err.println("Server hangs, killing all..." + name + " counter: " + timerCounter);
				Runtime.getRuntime().exec("killall -9 rcssserver3d");
			} else {
				// we got the pid from outside
				System.err.println("Server hangs at time " + worldModel.getTime() + ", killing it... " + serverPid +
								   " timer: " + name + " counter: " + timerCounter);
				UnixCommandUtil.killProcessConditional(serverPid, "rcssserver3d");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getNumberOfPlayers()
	{
		return worldModel.getSoccerAgents().size();
	}

	@Override
	public RefereeState getState()
	{
		return state;
	}
}
