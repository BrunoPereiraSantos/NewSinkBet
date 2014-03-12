package projects.OldBetEtx.nodes.messages;

import sinalgo.nodes.messages.Message;

public class PackHelloOldBetEtx extends Message {

	private int hops;	//numero de hops do nodo que o enviou o pacote
	private int path;	//numeros de caminho do nodo que o enviou o pacote
	private int senderID; // ID do nodo que enviou o pacote
	private int sinkID;	// ID do sink
	private double ETX; //ETX do nodo que enviou o pacote
	
	
	
	/**
	 * 
	 */
	public PackHelloOldBetEtx() {
		super();
	}

	/**
	 * @param hops
	 * @param path
	 * @param senderID
	 * @param sinkID
	 * @param eTX
	 */
	public PackHelloOldBetEtx(int hops, int path, int senderID, int sinkID, double eTX) {
		super();
		this.hops = hops;
		this.path = path;
		this.senderID = senderID;
		this.sinkID = sinkID;
		ETX = eTX;
	}
	
	
	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new PackHelloOldBetEtx(this.hops, this.path, this.senderID, this.sinkID, this.ETX);
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

	public double getETX() {
		return ETX;
	}

	public void setETX(double eTX) {
		ETX = eTX;
	}

	@Override
	public String toString() {
		return "PackHelloEtx [hops=" + hops 
				+ ", path=" + path 
				+ ", senderID="	+ senderID 
				+ ", sinkID=" + sinkID 
				+ ", ETX=" + ETX 
				+ "]";
	}

}
