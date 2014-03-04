package projects.RateBet.nodes.nodeImplementations;

import java.awt.Color;

import projects.RateBet.nodes.messages.Teste;
import projects.RateBet.nodes.timers.TimerTeste;
import projects.defaultProject.nodes.messages.StringMessage;
import projects.defaultProject.nodes.timers.MessageTimer;
import projects.sample1.nodes.messages.S1Message;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.Node;
import sinalgo.nodes.Node.NodePopupMethod;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.nodes.messages.NackBox;
import sinalgo.tools.Tools;

public class NodeRate extends Node {

	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub
		while (inbox.hasNext()) {
			Message msg = inbox.next();
			StringMessage m = (StringMessage) msg;
			System.out.println("-------------MSG arrive------------------");
			System.out.println("Conteúdo: "+m.text);
			System.out.println("De: "+inbox.getSender());
			System.out.println("Para: "+inbox.getReceiver());
			System.out.println("Chegou em: "+inbox.getArrivingTime());
			System.out.println("-------------MSG END------------------");
		}

	}

	public void handleNAckMessages(NackBox nackBox) {
		while (nackBox.hasNext()) {
			Message msg = nackBox.next();
			StringMessage m = (StringMessage) msg;
			System.out.println("-------------NACK arrive------------------");
			System.out.println("Conteúdo: "+m.text);
			System.out.println("De: "+nackBox.getSender());
			System.out.println("Para: "+nackBox.getReceiver());
			System.out.println("Chegou em: "+nackBox.getArrivingTime());
			System.out.println("-------------NACK END------------------");
			System.out.println("\n\nResending");
			(new MessageTimer(m, nackBox.getReceiver())).startRelative(1, this);;
			
		}
	}
	
	@Override
	public void preStep() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		if(this.ID == 1){
			this.setColor(Color.BLUE);
		}
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
		StringMessage m = new StringMessage("Olá sou o ID = "+this.ID);
		Node dest = Tools.getNodeByID(2);
		MessageTimer mt = new MessageTimer(m, dest);
		mt.startRelative(1, this);
		Tools.appendToOutput("Start Routing from node " + this.ID + "\n");
	}
	
}
