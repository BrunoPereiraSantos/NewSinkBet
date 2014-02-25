package projects.BaseTest.nodes.nodeImplementations;

import java.awt.Color;

import projects.BaseTest.nodes.messages.MBT;
import projects.BaseTest.nodes.timers.Temporizador;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

public class NodeBT extends Node {
	
	Temporizador t = new Temporizador(new MBT("Ola sou o "+this.ID));
	
	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub
		while (inbox.hasNext()) {
			Message msg = inbox.next();
			
			System.out.println("Dados da msg: ");
			System.out.println("Time arrive: "+inbox.getArrivingTime());
			System.out.println("Time send: "+inbox.getSendingTime());
			System.out.println("Receiver: "+inbox.getReceiver().ID);
			System.out.println("Sender: "+inbox.getSender().ID);
			System.out.println("edge: "+inbox.getIncomingEdge().getID());
			if (msg instanceof MBT) {
				MBT m = (MBT)msg;
				System.out.println(this.ID+" mensagem recebida: "+m.getA());
			}
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
			t.startRelative(.000000000000001, this);
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

}
