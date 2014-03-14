package projects.OldBetHop.nodes.timers;

import java.util.ArrayList;

import projects.OldBetHop.nodes.messages.PackReplyOldBetHop;
import projects.OldBetHop.nodes.nodeImplementations.NodeOldBetHop;
import sinalgo.nodes.timers.Timer;

public class TimerFwdReplyOldBetHop extends Timer {
	
	private PackReplyOldBetHop pkt;
	
	public TimerFwdReplyOldBetHop() {}
	
	public TimerFwdReplyOldBetHop(PackReplyOldBetHop msg) {
		//super();
		this.pkt = msg;
		pkt.setHops(msg.getHops());
		pkt.setPath(msg.getPath());
		pkt.setSenderID(msg.getSenderID());
		pkt.setSinkID(msg.getSinkID());
		pkt.setSendTo(msg.getSendTo());
		pkt.setSendToNodes(msg.getSendToNodes());
		pkt.setsBet(msg.getsBet());
	}
	
	public TimerFwdReplyOldBetHop(int hops, int path, int senderID, int sinkID, int sendTo, ArrayList<Integer> sendToNodes , double sBet) {
		//super();
		//this.pkt = pkt;
		pkt = new PackReplyOldBetHop(hops, path, senderID, sinkID, sendTo, sendToNodes, sBet);
	}
	
	@Override
	public void fire() {
		// TODO Auto-generated method stub
		((NodeOldBetHop) this.node).fwdReply(this.pkt);
	}
	
	public PackReplyOldBetHop getPkt() {
		return pkt;
	}

	public void setPkt(PackReplyOldBetHop pkt) {
		this.pkt = pkt;
	}

}
