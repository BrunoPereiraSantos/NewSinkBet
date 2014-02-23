package projects.BaseTest.nodes.nodeImplementations;

import projects.BaseTest.nodes.messages.MBT;
import projects.BaseTest.nodes.timers.Temporizador;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

public class NodeBT extends Node {

	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub
		while (inbox.hasNext()) {
			Message msg = inbox.next();
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
			Temporizador t = new Temporizador(new MBT("Ola sou o "+this.ID));
			t.startRelative(2, this);
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
