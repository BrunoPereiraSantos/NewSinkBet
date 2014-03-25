package projects.Rate.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;

import projects.BetEtt.nodes.messages.BetEttHelloMessage;
import projects.Rate.nodes.edges.EdgeRate;
import projects.Rate.nodes.messages.RateHelloMessage;
import projects.Rate.nodes.timers.RateMessageTimer;
import projects.defaultProject.nodes.messages.StringMessage;
import projects.defaultProject.nodes.timers.MessageTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.nodes.messages.NackBox;
import sinalgo.tools.Tools;

public class NodeRate extends Node {
	// Qual o papel do nodo
	private NodeRoleRate role;

	// ID do no sink
	private int sinkID;

	// numero de hops ate o sink
	private int hops;

	// id do prox no usando a metrica numero de hops
	private int nextHop;
	
	//rate acumulado do caminho
	private float mtmPath = Float.MAX_VALUE;

	// Flag para indicar se o nodo ja enviou seu pkt hello
	private boolean sentMyHello = false;
	
	//Disparadores de flood
	RateMessageTimer fhp;

	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub
		while (inbox.hasNext()) {
			Message m = inbox.next();
			 
			
			if(m instanceof RateHelloMessage){
				RateHelloMessage msg = (RateHelloMessage) m;
				System.out.println("-------------MSG arrive------------------");
				System.out.println("Conteúdo: "+msg.toString());
				System.out.println("De: "+inbox.getSender());
				System.out.println("Para: "+inbox.getReceiver());
				System.out.println("Chegou em: "+inbox.getArrivingTime());
				System.out.println("-------------MSG END------------------");
				 
				
				handleHello(inbox.getSender(), inbox.getReceiver(), (EdgeRate) inbox.getIncomingEdge(), msg);
			} 
			
		}

	}

	public void handleNAckMessages(NackBox nackBox) {
		while (nackBox.hasNext()) {
			Message msg = nackBox.next();
			StringMessage m = (StringMessage) msg;
			/*
			 * System.out.println("-------------NACK arrive------------------");
			 * System.out.println("Conteúdo: "+m.text);
			 * System.out.println("De: "+nackBox.getSender());
			 * System.out.println("Para: "+nackBox.getReceiver());
			 * System.out.println("Chegou em: "+nackBox.getArrivingTime());
			 * System.out.println("-------------NACK END------------------");
			 * System.out.println("\n\nResending"); (new MessageTimer(m,
			 * nackBox.getReceiver())).startRelative(1, this);
			 */

		}
	}

	
	private void handleHello(Node sender, Node receiver, EdgeRate incomingEdge, RateHelloMessage msg) {
		
		// no sink nao manipula pacotes hello
		if(this.ID == msg.getSinkID()){
			msg = null;
			return;
		}
		
		EdgeRate edgeToSender = (EdgeRate) incomingEdge.oppositeEdge;
		
		// o nodo e vizinho direto do sink (armazena o nextHop como id do sink
		if(msg.getSinkID() == sender.ID){
			nextHop = sender.ID;
		}
		
		// nodo acaba de ser descoberto ou acabou de encontrar um caminho mais curto
		if((msg.getMtmPath() + edgeToSender.getMtm() < mtmPath) 
				|| mtmPath == Double.MAX_VALUE){
			
			sinkID = msg.getSinkID();
			
			hops = msg.getHops() + 1;
			msg.setHops(hops);
			
			nextHop = sender.ID;
			
			mtmPath = msg.getMtmPath() + edgeToSender.getMtm();
			msg.setMtmPath(mtmPath);
			
			sentMyHello = false;
		}
		
		if(!isSentMyHello()){
			fhp = new RateMessageTimer(msg);
			fhp.startRelative(1, this);
			sentMyHello = true;
		}
		
		
	}
	
	@Override
	public void preStep() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		if (this.ID == 1) {
			this.setColor(Color.BLUE);

			RateHelloMessage hellomsg = new RateHelloMessage(0, this.ID, 0.0f);
			MessageTimer mt = new MessageTimer(hellomsg);
			mt.startRelative(1, this);
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

	@NodePopupMethod(menuText = "Start")
	public void start() {
		
		RateHelloMessage hellomsg = new RateHelloMessage(0, this.ID, 0.0f);
		MessageTimer mt = new MessageTimer(hellomsg);
		mt.startRelative(1, this);
		
		this.setColor(Color.BLUE);
		
		Tools.appendToOutput("Node "+ this.ID +" start hello flood from.");
	}

	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {

		String str = Integer.toString(this.ID);

		super.drawNodeAsSquareWithText(g, pt, highlight, str, 30, Color.YELLOW);
		// super.drawAsRoute(g, pt, highlight, 30);
	}

	public void sendUnicastRateMsg(Message msg, Node receiver) {
		// TODO Auto-generated method stub
		
	}

	public void broadcastRateMsg(Message msg) {
		if(msg instanceof RateHelloMessage){
			RateHelloMessage m = (RateHelloMessage) msg;
			m.setHops(hops);
			m.setMtmPath(mtmPath);
			m.setSinkID(sinkID);
			
			broadcast(m);
		}
		
	}
	
	
	@Override
	public String toString() {
		return "NodeRate [role=" + role + 
				"\nsinkID=" + sinkID + 
				"\nhops="+ hops + 
				"\nnextHop=" + nextHop +
				"\nmtmPath="+ String.format("%.2f", mtmPath) + 
				"\nsentMyHello=" + sentMyHello+ "]";
	}

	public boolean isSentMyHello() {
		return sentMyHello;
	}
	
}
