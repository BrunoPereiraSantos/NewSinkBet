package projects.Hop.nodes.messages;

import sinalgo.nodes.messages.Message;

public class HopHelloMessage extends Message {

	private int hops; // numero de hops do nodo que o enviou o pacote
	private int sinkID; // ID do sink

	/**
	 * @param hops
	 * @param path
	 * @param sinkID
	 * @param mtmPath
	 */
	public HopHelloMessage(int hops, int sinkID) {
		super();
		this.hops = hops;
		this.sinkID = sinkID;
	}

	/**
	 * 
	 */
	public HopHelloMessage() {
		super();
	}

	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new HopHelloMessage(this.hops, this.sinkID);
	}

	@Override
	public String toString() {
		return "RateHelloMessage [hops=" + hops + ", sinkID=" + sinkID + "]";
	}

	public int getHops() {
		return hops;
	}

	public void setHops(int hops) {
		this.hops = hops;
	}

	public int getSinkID() {
		return sinkID;
	}

	public void setSinkID(int sinkID) {
		this.sinkID = sinkID;
	}

}
