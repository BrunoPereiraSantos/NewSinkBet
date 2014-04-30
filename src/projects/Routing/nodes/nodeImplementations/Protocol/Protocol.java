package projects.Routing.nodes.nodeImplementations.Protocol;

import java.awt.Color;

import projects.Routing.nodes.messages.PackEvent;
import projects.Routing.nodes.messages.PackHello;
import projects.Routing.nodes.messages.PackReply;
import projects.Routing.nodes.nodeImplementations.RoutingNode;
import projects.Routing.nodes.timers.RoutingMessageTimer;
import projects.defaultProject.nodes.timers.GenericMessageTimer;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.NackBox;

public abstract class Protocol implements ProtocolInterface {

	@Override
	public void interceptPackHello(Inbox inbox, PackHello msg) {
	}

	@Override
	public void interceptPackReply(Inbox inbox, PackReply msg) {
	}

	@Override
	public void interceptPackEvent(Inbox inbox, PackEvent msg) {
		RoutingNode receiver = (RoutingNode) inbox.getReceiver();

		if ((msg.getNextHop() == 1) && (receiver.ID == 1)) {
			return;
		}

		if (msg.getNextHop() == receiver.ID) {
			receiver.setColor(Color.ORANGE);
			msg.setNextHop(receiver.nextHop);
			RoutingMessageTimer  mt = new RoutingMessageTimer(msg, true);
			mt.startRelative(0.001, receiver);
		}
	}

	@Override
	public void interceptNack(NackBox nackBox, PackEvent msg) {
		RoutingNode receiver = (RoutingNode) nackBox.getReceiver();
		RoutingNode sender = (RoutingNode) nackBox.getSender();
		
		if (receiver.ID == sender.nextHop) {

			sender.setColor(Color.RED);
			msg.setNextHop(sender.nextHop);
			RoutingMessageTimer  mt = new RoutingMessageTimer(msg, true);
			mt.startRelative(0.5, sender);
		}
		
	}
}
