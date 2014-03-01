package projects.HopBet.nodes.timers;

import projects.HopBet.nodes.nodeImplementations.NodeHop;
import sinalgo.nodes.timers.Timer;

public class TimerStartSimulation extends Timer {

	@Override
	public void fire() {
		// TODO Auto-generated method stub
		( (NodeHop) this.node).sendHelloFlooding();
	}

}
