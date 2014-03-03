package projects.RateBet.nodes.nodeImplementations;

import projects.RateBet.nodes.messages.Teste;
import projects.RateBet.nodes.timers.TimerTeste;
import projects.defaultProject.nodes.timers.MessageTimer;
import projects.sample1.nodes.messages.S1Message;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.Node;
import sinalgo.nodes.Node.NodePopupMethod;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

public class NodeRate extends Node {

	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub
		while (inbox.hasNext()) {
			Message msg = inbox.next();
			Teste m = (Teste) msg;
			System.out.println("Conteúdo: "+m.getData());
			System.out.println("De: "+inbox.getSender());
			System.out.println("Para: "+inbox.getReceiver());
			System.out.println("Chegou em: "+inbox.getArrivingTime());
		}

	}

	@Override
	public void preStep() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void neighborhoodChange() {
		// TODO Auto-generated method stub

	}

	@Override
	public void postStep() {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkRequirements() throws WrongConfigurationException {
		// TODO Auto-generated method stub

	}

	@NodePopupMethod(menuText="Start")
	public void start() {
		TimerTeste msgTimer = new TimerTeste(new Teste("Eu sou o "+this.ID)); 
		msgTimer.startRelative(1, this);
		Tools.appendToOutput("Start Routing from node " + this.ID + "\n");
	}
	
}
