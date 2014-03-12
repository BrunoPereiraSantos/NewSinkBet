package projects.OldBetEtx.nodes.timers;

import projects.OldBetEtx.nodes.nodeImplementations.NodeOldBetEtx;
import sinalgo.nodes.timers.Timer;

public class TimerSendHelloOldBetEtx extends Timer {

	public TimerSendHelloOldBetEtx() {}
	
	
	@Override
	public void fire() {
		// TODO Auto-generated method stub
		((NodeOldBetEtx)this.node).fwdHelloPack();
	}

}
