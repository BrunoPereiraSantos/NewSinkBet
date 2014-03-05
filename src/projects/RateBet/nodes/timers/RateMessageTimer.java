package projects.RateBet.nodes.timers;

import sinalgo.nodes.Node;
import projects.RateBet.nodes.nodeImplementations.NodeRate;
import sinalgo.nodes.messages.Message;
import sinalgo.nodes.timers.Timer;

public class RateMessageTimer extends Timer {
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
	public RateMessageTimer(Message msg, Node receiver) {
		this.msg = msg;
		this.receiver = receiver;
	}
	
	/**
	 * Creates a MessageTimer object that broadcasts a message when the timer fires.
	 *
	 * @param msg The message to be sent when this timer fires.
	 */
	public RateMessageTimer(Message msg) {
		this.msg = msg;
		this.receiver = null; // indicates broadcasting
	}
	
	@Override
	public void fire() {
		if(receiver != null) { // there's a receiver => unicast the message
			((NodeRate) this.node ).sendUnicastRateMsg(this.msg, this.receiver);
		} else { // there's no reciever => broadcast the message
			((NodeRate) this.node).broadcastRateMsg(this.msg);
		}
	}

}
