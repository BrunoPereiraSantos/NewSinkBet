package projects.BetEtx.nodes.timers;

import projects.BetEtx.nodes.nodeImplementations.NodeBetEtx;
import sinalgo.nodes.timers.Timer;

public class TimerSendHelloBetEtx extends Timer {

	public TimerSendHelloBetEtx() {}
	
	
	@Override
	public void fire() {
		// TODO Auto-generated method stub
		((NodeBetEtx)this.node).fwdHelloPack();
	}

}
