package projects.BetHop.nodes.timers;

import projects.BetHop.nodes.nodeImplementations.NodeHop;
import sinalgo.nodes.timers.Timer;

public class TimerStartReplyFloodHop extends Timer {

	public TimerStartReplyFloodHop() {}
	
	@Override
	public void fire() {
		// TODO Auto-generated method stub
		((NodeHop)this.node).sendReplyFlooding();
	}

}
