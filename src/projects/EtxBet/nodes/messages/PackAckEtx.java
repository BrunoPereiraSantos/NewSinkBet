package projects.EtxBet.nodes.messages;

import sinalgo.nodes.messages.Message;

public class PackAckEtx extends Message {
	int destination;
	
	/**
	 * @param destination
	 */
	public PackAckEtx(int destination) {
		super();
		this.destination = destination;
	}

	@Override
	public Message clone() {
		// TODO Auto-generated method stub
		return new PackAckEtx(this.destination);
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	
}
