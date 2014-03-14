package projects.BetRate.nodes.messages;

import java.awt.Color;
import java.util.ArrayList;

import sinalgo.nodes.messages.Message;

public class BetRateReplyMessage extends Message {

	
	private int hops; //numero de hops do nodo que o enviou o pacote
	private int path; //numeros de caminho do nodo que o enviou o pacote
	private int senderID; // ID do nodo que enviou o pacote
	private int sinkID; // ID do sink
	private int sendTo; // next hop no nodo que enviou o pacote
	private ArrayList<Integer> sendToNodes; //nodos que devem receber
	private float mtmPath; //EtxPath do nodo que enviou o pacote
	private float sBet; //metrica sBet do nodo que enviou o pacote
	private int fwdID; //ID do no que encaminhou a mensagem por ultimo
	

	/**
	 * @param hops
	 * @param path
	 * @param senderID
	 * @param sinkID
	 * @param sendTo
	 * @param sendToNodes
	 * @param mtmPath
	 * @param sBet
	 * @param fwdID
	 */
	public BetRateReplyMessage(int hops, int path, int senderID, int sinkID,
			int sendTo, ArrayList<Integer> sendToNodes, float mtmPath,
			float sBet, int fwdID) {
		super();
		this.hops = hops;
		this.path = path;
		this.senderID = senderID;
		this.sinkID = sinkID;
		this.sendTo = sendTo;
		this.sendToNodes = sendToNodes;
		this.mtmPath = mtmPath;
		this.sBet = sBet;
		this.fwdID = fwdID;
	}
	
	
	public BetRateReplyMessage() {
		super();
	}

	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new BetRateReplyMessage(hops, path, senderID, sinkID, sendTo, sendToNodes, mtmPath, sBet, fwdID);
	}

	@Override
	public String toString() {
		return "BetRateReplyMessage [hops=" + hops 
				+ "\n path=" + path
				+ "\n senderID=" + senderID 
				+ "\n sinkID=" + sinkID 
				+ "\n sendTo="+ sendTo 
				+ "\n sendToNodes=" + sendToNodes 
				+ "\n mtmPath=" + mtmPath
				+ "\n sBet=" + sBet 
				+ "\n fwdID=" + fwdID + "]";
	}

	public int getHops() {
		return hops;
	}

	public void setHops(int hops) {
		this.hops = hops;
	}

	public int getPath() {
		return path;
	}

	public void setPath(int path) {
		this.path = path;
	}

	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}

	public int getSinkID() {
		return sinkID;
	}

	public void setSinkID(int sinkID) {
		this.sinkID = sinkID;
	}

	public int getSendTo() {
		return sendTo;
	}

	public void setSendTo(int sendTo) {
		this.sendTo = sendTo;
	}

	public ArrayList<Integer> getSendToNodes() {
		return sendToNodes;
	}

	public void setSendToNodes(ArrayList<Integer> sendToNodes) {
		this.sendToNodes = sendToNodes;
	}

	public float getMtmPath() {
		return mtmPath;
	}


	public void setMtmPath(float mtmPath) {
		this.mtmPath = mtmPath;
	}


	public float getsBet() {
		return sBet;
	}

	public void setsBet(float sBet) {
		this.sBet = sBet;
	}

	public int getFwdID() {
		return fwdID;
	}

	public void setFwdID(int fwdID) {
		this.fwdID = fwdID;
	}
	
}
