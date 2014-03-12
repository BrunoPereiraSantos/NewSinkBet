package projects.OldBetEtx.nodes.timers;

import java.util.ArrayList;

import projects.OldBetEtx.nodes.messages.PackReplyOldBetEtx;
import projects.OldBetEtx.nodes.nodeImplementations.NodeOldBetEtx;
import sinalgo.nodes.timers.Timer;

public class TimerFwdReplyOldBetEtx extends Timer {

	private PackReplyOldBetEtx pkt;
	
	public TimerFwdReplyOldBetEtx() {}
	
	
	
	/**
	 * @param pkt
	 */
	public TimerFwdReplyOldBetEtx(PackReplyOldBetEtx msg) {
		super();
		this.pkt = msg;
		pkt.setHops(msg.getHops());
		pkt.setPath(msg.getPath());
		pkt.setSenderID(msg.getSenderID());
		pkt.setSinkID(msg.getSinkID());
		pkt.setSendTo(msg.getSendTo());
		pkt.setSendToNodes(msg.getSendToNodes());
		pkt.setsBet(msg.getsBet());
		pkt.setFwdID(msg.getFwdID());
	}

	public TimerFwdReplyOldBetEtx(int hops, int path, int senderID, int sinkID, int sendTo, ArrayList<Integer> sendToNodes, double etx, double sBet, int fwdID) {
		//super();
		//this.pkt = pkt;
		pkt = new PackReplyOldBetEtx(hops, path, senderID, sinkID, sendTo, sendToNodes, etx, sBet, fwdID);
	}

	@Override
	public void fire() {
		// TODO Auto-generated method stub
		((NodeOldBetEtx)this.node).fwdReply(this.pkt);
	}



	public PackReplyOldBetEtx getPkt() {
		return pkt;
	}



	public void setPkt(PackReplyOldBetEtx pkt) {
		this.pkt = pkt;
	}
	
	

}
