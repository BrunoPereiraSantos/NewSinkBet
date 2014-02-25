package projects.EtxBet.nodes.messages;

import java.util.ArrayList;

import sinalgo.nodes.messages.Message;

public class PackReplyEtx extends Message {

	private int hops; //numero de hops do nodo que o enviou o pacote
	private int path; //numeros de caminho do nodo que o enviou o pacote
	private int senderID; // ID do nodo que enviou o pacote
	private int sinkID; // ID do sink
	private int sendTo; // next hop no nodo que enviou o pacote
	private ArrayList<Integer> sendToNodes; // nodos que devem receber
	private double ETX; //ETX do nodo que enviou o pacote
	private double sBet; //metrica sBet do nodo que enviou o pacote
	private int fwdID; //ID do no que encaminhou a mensagem por ultimo
	
	public PackReplyEtx(){}
	
	
	
	/**
	 * @param hops
	 * @param path
	 * @param senderID
	 * @param sinkID
	 * @param sendTo
	 * @param sendToNodes
	 * @param eTX
	 * @param sBet
	 * @param fwdID
	 */
	public PackReplyEtx(int hops, int path, int senderID, int sinkID,
			int sendTo, ArrayList<Integer> sendToNodes, double eTX,
			double sBet, int fwdID) {
		super();
		this.hops = hops;
		this.path = path;
		this.senderID = senderID;
		this.sinkID = sinkID;
		this.sendTo = sendTo;
		this.sendToNodes = sendToNodes;
		ETX = eTX;
		this.sBet = sBet;
		this.fwdID = fwdID;
	}

	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new PackReplyEtx(this.hops, this.path, this.senderID, this.sinkID, this.sendTo, this.sendToNodes, this.ETX, this.sBet, this.fwdID);
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



	public double getETX() {
		return ETX;
	}



	public void setETX(double eTX) {
		ETX = eTX;
	}



	public double getsBet() {
		return sBet;
	}



	public void setsBet(double sBet) {
		this.sBet = sBet;
	}



	public int getFwdID() {
		return fwdID;
	}



	public void setFwdID(int fwdID) {
		this.fwdID = fwdID;
	}



	@Override
	public String toString() {
		return "PackReplyEtx [hops=" + hops + ", path=" + path + ", senderID="
				+ senderID + ", sinkID=" + sinkID + ", sendTo=" + sendTo
				+ ", sendToNodes=" + sendToNodes + ", ETX=" + ETX + ", sBet="
				+ sBet + ", fwdID=" + fwdID + "]";
	}
	
	

}
