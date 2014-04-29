package projects.BetHop.nodes.messages;

import sinalgo.nodes.messages.Message;

public class BetHopHelloMessage extends Message {

	private int hops; //numero de hops do nodo que o enviou o pacote
	private int paths; //numeros de caminho do nodo que o enviou o pacote
	private int sinkID; // ID do sink
	
	
	

	/**
	 * @param hops
	 * @param paths
	 * @param sinkID
	 * @param pathEtx
	 */
	public BetHopHelloMessage(int hops, int paths, int sinkID) {
		super();
		this.hops = hops;
		this.paths = paths;
		this.sinkID = sinkID;
	}


	/**
	 * 
	 */
	public BetHopHelloMessage() {
		super();
	}


	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new BetHopHelloMessage(hops, paths, sinkID);
	}

	@Override
	public String toString() {
		return "BetEtxHelloMessage [hops=" + hops + 
				"\n path=" + paths+ 
				"\n sinkID=" + sinkID + 
				"]";
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
