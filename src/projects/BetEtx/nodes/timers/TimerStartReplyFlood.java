package projects.BetEtx.nodes.timers;

import projects.BetEtx.nodes.nodeImplementations.NodeBetEtx;
import sinalgo.nodes.timers.Timer;

public class TimerStartReplyFlood extends Timer {

	public TimerStartReplyFlood() {
	}

	@Override
	public void fire() {
		// TODO Auto-generated method stub
		((NodeBetEtx)this.node).sendReplyFlooding();
	}

}
