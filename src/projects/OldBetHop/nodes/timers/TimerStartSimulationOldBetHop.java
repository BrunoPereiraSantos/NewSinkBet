package projects.OldBetHop.nodes.timers;

import projects.OldBetHop.nodes.nodeImplementations.NodeOldBetHop;
import sinalgo.nodes.timers.Timer;

public class TimerStartSimulationOldBetHop extends Timer {

	@Override
	public void fire() {
		// TODO Auto-generated method stub
		( (NodeOldBetHop) this.node).sendHelloFlooding();
	}

}
