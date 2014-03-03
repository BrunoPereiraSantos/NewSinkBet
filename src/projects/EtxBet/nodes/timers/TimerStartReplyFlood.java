package projects.EtxBet.nodes.timers;

import projects.EtxBet.nodes.nodeImplementations.NodeEtx;
import projects.HopBet.nodes.nodeImplementations.NodeHop;
import sinalgo.nodes.timers.Timer;

public class TimerStartReplyFlood extends Timer {

	public TimerStartReplyFlood() {
	}

	@Override
	public void fire() {
		// TODO Auto-generated method stub
		((NodeHop)this.node).sendReplyFlooding();
	}

}
