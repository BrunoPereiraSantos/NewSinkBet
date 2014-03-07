package projects.BetHop.nodes.timers;

import projects.BetHop.nodes.nodeImplementations.NodeHop;
import sinalgo.nodes.timers.Timer;

public class TimerSendHelloHopSbet extends Timer {

	public TimerSendHelloHopSbet() {}
	
	@Override
	public void fire() {
		// TODO Auto-generated method stub
		((NodeHop)this.node).fwdHelloPack();
	}

}
