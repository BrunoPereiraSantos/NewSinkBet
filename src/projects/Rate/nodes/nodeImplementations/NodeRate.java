package projects.Rate.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;

import Analises.InterfaceEventTest;
import Analises.StatisticsNode;
import projects.BetEtt.nodes.messages.BetEttHelloMessage;
import projects.Hop.nodes.messages.HopHelloMessage;
import projects.Rate.nodes.edges.EdgeRate;
import projects.Rate.nodes.messages.RateHelloMessage;
import projects.defaultProject.models.reliabilityModels.GenericReliabilityModel;
import projects.defaultProject.models.reliabilityModels.ReliableDelivery;
import projects.defaultProject.nodes.edges.GenericWeightedEdge;
import projects.defaultProject.nodes.messages.EventMessage;
import projects.defaultProject.nodes.messages.StringMessage;
import projects.defaultProject.nodes.timers.GenericMessageTimer;
import projects.defaultProject.nodes.timers.MessageTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.nodes.messages.NackBox;
import sinalgo.tools.Tools;

public class NodeRate extends Node implements InterfaceEventTest{
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
	GenericMessageTimer fhp;
	
	// Coleta as estatisticas do nodo
	StatisticsNode statistics;

	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub
		while (inbox.hasNext()) {
			Message m = inbox.next();
			 
			
			if(m instanceof RateHelloMessage){
				
				statistics.countHeardMessagesTree();

				RateHelloMessage msg = (RateHelloMessage) m;
				System.out.println("-------------MSG arrive------------------");
				System.out.println("Conteúdo: "+msg.toString());
				System.out.println("De: "+inbox.getSender());
				System.out.println("Para: "+inbox.getReceiver());
				System.out.println("Chegou em: "+inbox.getArrivingTime());
				System.out.println("-------------MSG END------------------");
				 
				
				handleHello(inbox.getSender(), inbox.getReceiver(), (GenericWeightedEdge) inbox.getIncomingEdge(), msg);
			} 
			
			if (m instanceof EventMessage) {

				statistics.countHeardMessagesEv((GenericWeightedEdge) inbox
						.getIncomingEdge());

				EventMessage msg = (EventMessage) m;
				System.out
						.println("-------------MSG EventMessage------------------");
				System.out.println("Conteúdo: " + msg.toString());
				System.out.println("De: " + inbox.getSender().ID);
				System.out.println("Para: " + inbox.getReceiver().ID);
				System.out.println("Chegou em: " + inbox.getArrivingTime());
				System.out.println("-------------MSG END------------------");

				handleEvent(inbox, msg);
			}
			
		}

	}

	public void handleNAckMessages(NackBox nackBox) {
		while (nackBox.hasNext()) {
			Message msg = nackBox.next();

			System.out.println("-------------NAckMessages------------------");
			System.out.println("De: " + nackBox.getSender().ID);
			System.out.println("Para: " + nackBox.getReceiver().ID);
			System.out.println("Chegou em: " + nackBox.getArrivingTime());
			System.out
					.println("-------------NAckMessages END------------------");

			if (msg instanceof EventMessage) {
				EventMessage m = (EventMessage) msg;
				if (nackBox.getReceiver().ID == nextHop) {
					statistics.countRelayedMessages();

					this.setColor(Color.RED);
					m.setNextHop(nextHop);
					GenericMessageTimer t = new GenericMessageTimer(m);
					t.startRelative(0.5, this);
				}
			}
		}
	}

	
	private void handleHello(Node sender, Node receiver, GenericWeightedEdge incomingEdge, RateHelloMessage msg) {
		
		// no sink nao manipula pacotes hello
		if(this.ID == msg.getSinkID()){
			msg = null;
			return;
		}
		
		GenericWeightedEdge edgeToSender = (GenericWeightedEdge) incomingEdge.oppositeEdge;
		
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
			
			//verifica se tem alguma msg na fila, caso tenha atualiza o timer em mais 1s,
			//caso contrário envia uma nova mensagem hellow
			if(sentMyHello){
				if(fhp.updateTimer(1, this, fhp.getFireTime())){
					sentMyHello = false;
				}
			}
		}
		
		if(!isSentMyHello()){
			fhp = new GenericMessageTimer(msg);
			fhp.startRelative(hops, this);
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
		
		statistics = new StatisticsNode(this.ID);
		
		if (this.ID == 1) {// sink
			this.sinkID = this.ID;
			this.nextHop = this.ID;
			this.hops = 0;
			this.sentMyHello = true;
			this.mtmPath = 0.0f;
			
			this.setColor(Color.BLUE);

			RateHelloMessage hellomsg = new RateHelloMessage();
			GenericMessageTimer mt = new GenericMessageTimer(hellomsg);
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

	

	@NodePopupMethod(menuText = "Start")
	public void start() {
		
		RateHelloMessage hellomsg = new RateHelloMessage(0, this.ID, 0.0f);
		GenericMessageTimer mt = new GenericMessageTimer(hellomsg);
		mt.startRelative(1, this);
		
		this.setColor(Color.BLUE);
		
		Tools.appendToOutput("Node "+ this.ID +" start hello flood from.");
	}

	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {

		String str = Integer.toString(this.ID);

		super.drawNodeAsSquareWithText(g, pt, highlight, str, 30, Color.YELLOW);
		// super.drawAsRoute(g, pt, highlight, 30);
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

	@Override
	public void checkRequirements() throws WrongConfigurationException {
		// TODO Auto-generated method stub
		if(!(reliabilityModel instanceof ReliableDelivery)){
			this.reliabilityModel = new ReliableDelivery();
		}
	}
	
	@Override
	public void changeRequirements() throws WrongConfigurationException {
		// TODO Auto-generated method stub
		this.reliabilityModel = new GenericReliabilityModel();
		System.out.println(this.getReliabilityModel());
	}

	@Override
	public void sentEventRelative(double timeStartEvents) {
		// TODO Auto-generated method stub
		EventMessage em = new EventMessage(this.ID, nextHop, Tools.getGlobalTime()+timeStartEvents, 0);
		GenericMessageTimer t = new GenericMessageTimer(em);
		t.startRelative(timeStartEvents, this);
	}

	@Override
	public void broadcastWithNack(Message m) {
		// TODO Auto-generated method stub
		GenericWeightedEdge edgeToTarget = null;
		EventMessage em = (EventMessage) m;
		
		this.setColor(Color.GRAY);
		Iterator<Edge> it = this.outgoingConnections.iterator();
		GenericWeightedEdge e;
		while(it.hasNext()){
			e = (GenericWeightedEdge) it.next();
			this.send(m, e.endNode);
			
			if(em.nextHop == e.endNode.ID){
				edgeToTarget = e;
			}
		}
		
		statistics.countBroadcastEv(edgeToTarget);
	}

	@Override
	public void sendUnicastMsg(Message msg, Node n) {
		// TODO Auto-generated method stub
		if(msg instanceof RateHelloMessage){
			RateHelloMessage m = (RateHelloMessage) msg;
			m.setHops(hops);
			m.setSinkID(sinkID);
			m.setMtmPath(mtmPath);
			
			broadcast(m);
			
			//statistics.countBroadcastTree();
		}
	}

	@Override
	public void broadcastMsg(Message msg) {
		// TODO Auto-generated method stub
		if(msg instanceof RateHelloMessage){
			RateHelloMessage m = (RateHelloMessage) msg;
			m.setHops(hops);
			m.setSinkID(sinkID);
			m.setMtmPath(mtmPath);
			
			broadcast(m);
			
			statistics.countBroadcastTree();
		}
	}

	@Override
	public void handleEvent(Inbox inbox, EventMessage msg) {
		// TODO Auto-generated method stub
		if((msg.getNextHop() == 1) && (this.ID == 1)){
			statistics.countEvReceived(inbox.getArrivingTime());
			statistics.IncomingEvents(msg.idSender, Tools.getGlobalTime() - msg.firedTime);
			return;
		}
		
		if(msg.getNextHop() == this.ID){
			this.setColor(Color.ORANGE);
			msg.setNextHop(nextHop);
			GenericMessageTimer mt = new GenericMessageTimer(msg);
			mt.startRelative(0.01, this);
		}
	}

	@Override
	public StatisticsNode getStatisticNode() {
		return this.statistics;
	}

	@Override
	public int getHops() {
		return hops;
	}
	
}
