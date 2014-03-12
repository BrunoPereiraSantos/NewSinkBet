package projects.OldBetEtx.nodes.timers;

import projects.OldBetEtx.nodes.nodeImplementations.NodeOldBetEtx;
import sinalgo.nodes.timers.Timer;

public class TimerStartSimulationOldBetEtx extends Timer {

	@Override
	public void fire() {
		// TODO Auto-generated method stub
		( (NodeOldBetEtx) this.node).sendHelloFlooding();
	}

}
