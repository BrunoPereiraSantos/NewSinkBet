package projects.Etx.nodes.messages;

import sinalgo.nodes.messages.Message;

public class EtxHelloMessage extends Message {

	private int hops; //numero de hops do nodo que o enviou o pacote
	private int sinkID; // ID do sink
	private float pathEtx; // ETX do caminho percorrido pela msg
	
	
	

	/**
	 * @param hops
	 * @param sinkID
	 * @param pathEtx
	 */
	public EtxHelloMessage(int hops, int sinkID, float pathEtx) {
		super();
		this.hops = hops;
		this.sinkID = sinkID;
		this.pathEtx = pathEtx;
	}


	/**
	 * 
	 */
	public EtxHelloMessage() {
		super();
	}


	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new EtxHelloMessage(hops, sinkID, pathEtx);
	}

	@Override
	public String toString() {
		return "BetEtxHelloMessage [hops=" + hops + 
				"\n sinkID=" + sinkID + 
				"\n pathEtx=" + pathEtx + "]";
	}

	

	public float getPathEtx() {
		return pathEtx;
	}


	public void setPathEtx(float pathEtx) {
		this.pathEtx = pathEtx;
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
