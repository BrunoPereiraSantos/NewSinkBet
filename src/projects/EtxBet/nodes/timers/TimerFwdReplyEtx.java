package projects.EtxBet.nodes.timers;

import java.util.ArrayList;

import projects.EtxBet.nodes.messages.PackReplyEtx;
import projects.EtxBet.nodes.nodeImplementations.NodeEtx;
import sinalgo.nodes.timers.Timer;

public class TimerFwdReplyEtx extends Timer {

	private PackReplyEtx pkt;
	
	public TimerFwdReplyEtx() {}
	
	
	
	/**
	 * @param pkt
	 */
	public TimerFwdReplyEtx(PackReplyEtx msg) {
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

	public TimerFwdReplyEtx(int hops, int path, int senderID, int sinkID, int sendTo, ArrayList<Integer> sendToNodes, double etx, double sBet, int fwdID) {
		//super();
		//this.pkt = pkt;
		pkt = new PackReplyEtx(hops, path, senderID, sinkID, sendTo, sendToNodes, etx, sBet, fwdID);
	}

	@Override
	public void fire() {
		// TODO Auto-generated method stub
		((NodeEtx)this.node).fwdReply(this.pkt);
	}



	public PackReplyEtx getPkt() {
		return pkt;
	}



	public void setPkt(PackReplyEtx pkt) {
		this.pkt = pkt;
	}
	
	

}
