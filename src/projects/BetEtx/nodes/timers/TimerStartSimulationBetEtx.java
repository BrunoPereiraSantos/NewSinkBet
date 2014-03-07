package projects.BetEtx.nodes.timers;

import projects.BetEtx.nodes.nodeImplementations.NodeBetEtx;
import sinalgo.nodes.timers.Timer;

public class TimerStartSimulationBetEtx extends Timer {

	@Override
	public void fire() {
		// TODO Auto-generated method stub
		( (NodeBetEtx) this.node).sendHelloFlooding();
	}

}
