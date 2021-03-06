package projects.BetEtt.nodes.messages;

import sinalgo.nodes.messages.Message;

public class BetEttHelloMessage extends Message {

	private int hops; //numero de hops do nodo que o enviou o pacote
	private int paths; //numeros de caminho do nodo que o enviou o pacote
	private int sinkID; // ID do sink
	private float pathEtt; // ID do sink
	
	
	/**
	 * @param hops
	 * @param path
	 * @param sinkID
	 */
	public BetEttHelloMessage(int hops, int paths, int sinkID, float pathEtt) {
		super();
		this.hops = hops;
		this.paths = paths;
		this.sinkID = sinkID;
		this.pathEtt = pathEtt;
	}

	/**
	 * 
	 */
	public BetEttHelloMessage() {
		super();
	}


	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new BetEttHelloMessage(this.hops, this.paths, this.sinkID, this.pathEtt);
	}


	

	
	@Override
	public String toString() {
		return "BetEttHelloMessage [hops=" + hops + 
				"\n path=" + paths+ 
				"\n sinkID=" + sinkID + 
				"\n pathEtt=" + pathEtt + "]";
	}
	

	public float getPathEtt() {
		return pathEtt;
	}

	public void setPathEtt(float pathEtt) {
		this.pathEtt = pathEtt;
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
