package projects.OldBetEtx.nodes.timers;

import projects.OldBetEtx.nodes.nodeImplementations.NodeOldBetEtx;
import sinalgo.nodes.timers.Timer;

public class TimerStartReplyFloodOldBetEtx extends Timer {

	public TimerStartReplyFloodOldBetEtx() {
	}

	@Override
	public void fire() {
		// TODO Auto-generated method stub
		((NodeOldBetEtx)this.node).sendReplyFlooding();
	}

}
