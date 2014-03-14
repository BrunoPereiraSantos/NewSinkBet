package projects.Hop.nodes.messages;

import sinalgo.nodes.messages.Message;

public class HopHelloMessage extends Message {

	private int hops; //numero de hops do nodo que o enviou o pacote
	private int paths; //numeros de caminho do nodo que o enviou o pacote
	private int sinkID; // ID do sink
	private double mtmPath; // ID do sink
	
	
	/**
	 * @param hops
	 * @param path
	 * @param sinkID
	 * @param mtmPath
	 */
	public HopHelloMessage(int hops, int paths, int sinkID, double mtmPath) {
		super();
		this.hops = hops;
		this.paths = paths;
		this.sinkID = sinkID;
		this.mtmPath = mtmPath;
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
		return new HopHelloMessage(this.hops, this.paths, this.sinkID, this.mtmPath);
	}

	@Override
	public String toString() {
		return "RateHelloMessage [hops=" + hops + ", path=" + paths
				+ ", sinkID=" + sinkID + ", mtmPath=" + mtmPath + "]";
	}
	
	
	public double getMtmPath() {
		return mtmPath;
	}

	public void setMtmPath(double mtmPath) {
		this.mtmPath = mtmPath;
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
