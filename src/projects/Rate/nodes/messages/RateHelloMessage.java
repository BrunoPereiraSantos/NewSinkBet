package projects.Rate.nodes.messages;

import sinalgo.nodes.messages.Message;

public class RateHelloMessage extends Message {

	private int hops; // numero de hops do nodo que o enviou o pacote
	private int sinkID; // ID do sink
	private float mtmPath; // metrica mtm (multi rate)

	/**
	 * @param hops
	 * @param sinkID
	 * @param mtmPath
	 */
	public RateHelloMessage(int hops, int sinkID, float mtmPath) {
		super();
		this.hops = hops;
		this.sinkID = sinkID;
		this.mtmPath = mtmPath;
	}

	/**
	 * 
	 */
	public RateHelloMessage() {
		super();
	}

	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new RateHelloMessage(this.hops, this.sinkID, this.mtmPath);
	}

	@Override
	public String toString() {
		return "RateHelloMessage [hops=" + hops + ", sinkID=" + sinkID
				+ ", mtmPath=" + mtmPath + "]";
	}

	public float getMtmPath() {
		return mtmPath;
	}

	public void setMtmPath(float mtmPath) {
		this.mtmPath = mtmPath;
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
