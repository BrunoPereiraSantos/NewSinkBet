package projects.BetEtx.nodes.timers;

import java.util.ArrayList;

import projects.BetEtx.nodes.messages.PackReplyEtx;
import projects.BetEtx.nodes.nodeImplementations.NodeBetEtx;
import sinalgo.nodes.timers.Timer;

public class TimerFwdReplyBetEtx extends Timer {

	private PackReplyEtx pkt;
	
	public TimerFwdReplyBetEtx() {}
	
	
	
	/**
	 * @param pkt
	 */
	public TimerFwdReplyBetEtx(PackReplyEtx msg) {
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

	public TimerFwdReplyBetEtx(int hops, int path, int senderID, int sinkID, int sendTo, ArrayList<Integer> sendToNodes, double etx, double sBet, int fwdID) {
		//super();
		//this.pkt = pkt;
		pkt = new PackReplyEtx(hops, path, senderID, sinkID, sendTo, sendToNodes, etx, sBet, fwdID);
	}

	@Override
	public void fire() {
		// TODO Auto-generated method stub
		((NodeBetEtx)this.node).fwdReply(this.pkt);
	}



	public PackReplyEtx getPkt() {
		return pkt;
	}



	public void setPkt(PackReplyEtx pkt) {
		this.pkt = pkt;
	}
	
	

}
