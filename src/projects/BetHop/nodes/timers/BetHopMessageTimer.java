package projects.BetHop.nodes.timers;

import projects.BetHop.nodes.nodeImplementations.NodeBetHop;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;
import sinalgo.nodes.timers.Timer;

public class BetHopMessageTimer extends Timer {
	private Node receiver; // the receiver of the message, null if the message should be broadcast
	private Message msg; // the message to be sent
	
	/**
	 * Creates a new MessageTimer object that unicasts a message to a given receiver when the timer fires.
	 * 
	 * Nothing happens when the message cannot be sent at the time when the timer fires.
	 *
	 * @param msg The message to be sent when this timer fires.
	 * @param receiver The receiver of the message.
	 */
	public BetHopMessageTimer(Message msg, Node receiver) {
		this.msg = msg;
		this.receiver = receiver;
	}
	
	/**
	 * Creates a MessageTimer object that broadcasts a message when the timer fires.
	 *
	 * @param msg The message to be sent when this timer fires.
	 */
	public BetHopMessageTimer(Message msg) {
		this.msg = msg;
		this.receiver = null; // indicates broadcasting
	}
	
	@Override
	public void fire() {
		if(receiver != null) { // there's a receiver => unicast the message
			((NodeBetHop) this.node ).sendUnicastBetEtxMsg(this.msg, this.receiver);
		} else { // there's no reciever => broadcast the message
			((NodeBetHop) this.node).broadcastBetEtxMsg(this.msg);
		}
	}


}
