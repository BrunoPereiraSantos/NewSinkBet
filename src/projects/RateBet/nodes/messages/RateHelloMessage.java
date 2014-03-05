package projects.RateBet.nodes.messages;

import sinalgo.nodes.messages.Message;

public class RateHelloMessage extends Message {

	private int hops; //numero de hops do nodo que o enviou o pacote
	private int paths; //numeros de caminho do nodo que o enviou o pacote
	private int sinkID; // ID do sink
	private double pathRate; // ID do sink
	
	
	/**
	 * @param hops
	 * @param path
	 * @param sinkID
	 */
	public RateHelloMessage(int hops, int paths, int sinkID, double pathRate) {
		super();
		this.hops = hops;
		this.paths = paths;
		this.sinkID = sinkID;
		this.pathRate = pathRate;
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
		return new RateHelloMessage(this.hops, this.paths, this.sinkID, this.pathRate);
	}


	

	
	@Override
	public String toString() {
		return "RateHelloMessage [hops=" + hops + ", path=" + paths
				+ ", sinkID=" + sinkID + ", pathRate=" + pathRate + "]";
	}

	public double getPathRate() {
		return pathRate;
	}

	public void setPathRate(double pathRate) {
		this.pathRate = pathRate;
	}

	public int getHops() {
		return hops;
	}




	public void setHops(int hops) {
		this.hops = hops;
	}




	public int getPaths() {
		return paths;
	}




	public void setPaths(int paths) {
		this.paths = paths;
	}




	public int getSinkID() {
		return sinkID;
	}




	public void setSinkID(int sinkID) {
		this.sinkID = sinkID;
	}

	
}
