package projects.BetRate.nodes.messages;

import sinalgo.nodes.messages.Message;

public class BetRateHelloMessage extends Message {

	private int hops; //numero de hops do nodo que o enviou o pacote
	private int paths; //numeros de caminho do nodo que o enviou o pacote
	private int sinkID; // ID do sink
	private float mtmPath; // mtmPath do caminho percorrido pela msg
	
	
	

	/**
	 * @param hops
	 * @param paths
	 * @param sinkID
	 * @param mtmPath
	 */
	public BetRateHelloMessage(int hops, int paths, int sinkID, float mtmPath) {
		super();
		this.hops = hops;
		this.paths = paths;
		this.sinkID = sinkID;
		this.mtmPath = mtmPath;
	}


	/**
	 * 
	 */
	public BetRateHelloMessage() {
		super();
	}


	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new BetRateHelloMessage(hops, paths, sinkID, mtmPath);
	}

	@Override
	public String toString() {
		return "BetEtxHelloMessage [hops=" + hops + 
				"\n path=" + paths+ 
				"\n sinkID=" + sinkID + 
				"\n mtmPath=" + mtmPath + "]";
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
