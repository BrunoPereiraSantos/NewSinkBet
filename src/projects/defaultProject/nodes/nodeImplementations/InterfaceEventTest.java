package projects.defaultProject.nodes.nodeImplementations;

import projects.defaultProject.nodes.messages.EventMessage;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

public interface InterfaceEventTest {
	void sentEvent_IEV(double timeStartEvents);
	void broadcastEvent_IEV(Message m);
	void handleEvent(Node sender, Node Receiver, EventMessage msg);
}
