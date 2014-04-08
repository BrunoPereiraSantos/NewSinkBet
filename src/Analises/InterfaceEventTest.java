package Analises;

import projects.defaultProject.nodes.messages.EventMessage;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;

public interface InterfaceEventTest {
	void changeRequirements() throws WrongConfigurationException;
	void sentEvent_IEV(double timeStartEvents);
	void broadcastEvent_IEV(Message m);
	void handleEvent(Inbox inbox, EventMessage msg);
	StatisticsNode getStatisticNode();
	int getHops();
}
