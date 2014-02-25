package projects.EtxBet.nodes.timers;

import projects.EtxBet.nodes.nodeImplementations.NodeEtx;
import sinalgo.nodes.timers.Timer;

public class TimerSendHelloEtx extends Timer {

	public TimerSendHelloEtx() {}
	
	
	@Override
	public void fire() {
		// TODO Auto-generated method stub
		((NodeEtx)this.node).fwdHelloPack();
	}

}
