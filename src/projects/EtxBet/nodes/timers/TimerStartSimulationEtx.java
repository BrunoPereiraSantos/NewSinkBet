package projects.EtxBet.nodes.timers;

import projects.EtxBet.nodes.nodeImplementations.NodeEtx;
import sinalgo.nodes.timers.Timer;

public class TimerStartSimulationEtx extends Timer {

	@Override
	public void fire() {
		// TODO Auto-generated method stub
		( (NodeEtx) this.node).sendHelloFlooding();
	}

}
