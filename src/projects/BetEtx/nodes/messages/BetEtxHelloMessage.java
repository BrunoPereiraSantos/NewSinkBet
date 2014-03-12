package projects.BetEtx.nodes.messages;

import sinalgo.nodes.messages.Message;

public class BetEtxHelloMessage extends Message {

	private int hops; //numero de hops do nodo que o enviou o pacote
	private int paths; //numeros de caminho do nodo que o enviou o pacote
	private int sinkID; // ID do sink
	private double pathEtx; // ETX do caminho percorrido pela msg
	
	
	/**
	 * @param hops
	 * @param path
	 * @param sinkID
	 * @param pathEtx
	 */
	public BetEtxHelloMessage(int hops, int paths, int sinkID, double pathEtx) {
		super();
		this.hops = hops;
		this.paths = paths;
		this.sinkID = sinkID;
		this.pathEtx = pathEtx;
	}

	/**
	 * 
	 */
	public BetEtxHelloMessage() {
		super();
	}


	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new BetEtxHelloMessage(this.hops, this.paths, this.sinkID, this.pathEtx);
	}


	

	
	@Override
	public String toString() {
		return "BetEtxHelloMessage [hops=" + hops + 
				"\n path=" + paths+ 
				"\n sinkID=" + sinkID + 
				"\n pathEtx=" + pathEtx + "]";
	}

	public double getPathEtx() {
		return pathEtx;
	}

	public void setPathEtx(double pathEtx) {
		this.pathEtx = pathEtx;
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
