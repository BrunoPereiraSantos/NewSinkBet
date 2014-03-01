package projects.HopBet.nodes.timers;

import java.util.ArrayList;

import projects.HopBet.nodes.messages.PackReplyHop;
import projects.HopBet.nodes.nodeImplementations.NodeHop;
import sinalgo.nodes.timers.Timer;

public class TimerFwdReplyHop extends Timer {
	
	private PackReplyHop pkt;
	
	public TimerFwdReplyHop() {}
	
	public TimerFwdReplyHop(PackReplyHop msg) {
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
	
	public TimerFwdReplyHop(int hops, int path, int senderID, int sinkID, int sendTo, ArrayList<Integer> sendToNodes , double sBet) {
		//super();
		//this.pkt = pkt;
		pkt = new PackReplyHop(hops, path, senderID, sinkID, sendTo, sendToNodes, sBet);
	}
	
	@Override
	public void fire() {
		// TODO Auto-generated method stub
		((NodeHop) this.node).fwdReply(this.pkt);
	}
	
	public PackReplyHop getPkt() {
		return pkt;
	}

	public void setPkt(PackReplyHop pkt) {
		this.pkt = pkt;
	}

}
