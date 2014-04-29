package projects.BetEtx.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import Analises.InterfaceRequiredMethods;
import Analises.StatisticsNode;
import projects.BetEtx.nodes.messages.BetEtxHelloMessage;
import projects.BetEtx.nodes.messages.BetEtxReplyMessage;
import projects.BetEtx.nodes.nodeImplementations.NodeRoleBetEtx;
import projects.defaultProject.models.reliabilityModels.GenericReliabilityModel;
import projects.defaultProject.models.reliabilityModels.ReliableDelivery;
import projects.defaultProject.nodes.edges.GenericWeightedEdge;
import projects.defaultProject.nodes.messages.EventMessage;
import projects.defaultProject.nodes.timers.GenericMessageTimer;
import projects.defaultProject.nodes.timers.MessageTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.nodes.messages.NackBox;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

public class NodeBetEtx extends Node implements InterfaceRequiredMethods{
	// Qual o papel do nodo
	private NodeRoleBetEtx role;

	// ID do no sink
	private int sinkID;

	// numero de hops ate o sink
	private int hops;

	// id do prox no usando a metrica numero de hops
	private int nextHop;

	// numero de caminhos para o sink
	private int pathsToSink;

	// rate acumulado do caminho
	private float EtxPath = Float.MAX_VALUE;

	// Flag para indicar se o nodo ja enviou seu pkt hello
	private boolean sentMyHello = false;

	// Flag para indicar se o nodo ja enviou seu pkt border
	private boolean sentMyReply = false;

	// valor do Sink Betweenness
	private float sBet;

	// Valor do maior sBet entre os vizinhos diretos
	private float neighborMaxSBet;

	// array com os filhos (this.etx < sons.etx)
	private ArrayList<Integer> sons = new ArrayList<Integer>();

	// array com os vizinhos diretos do nodo.
	private ArrayList<Integer> neighbors = new ArrayList<Integer>();

	// cada nodo mantem um map com chave(num de caminhos) e valor(quantos nodos
	// descendentes tem 'num de caminhos'
	private Map<Integer, Integer> sonsPathMap = new HashMap<Integer, Integer>();

	// Disparadores de flood
	GenericMessageTimer fhp;
	GenericMessageTimer frp;
	
	// Coleta as estatisticas do nodo
	StatisticsNode statistics;

	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub
		while (inbox.hasNext()) {
			Message m = inbox.next();

			if (m instanceof BetEtxHelloMessage) {
				
				statistics.countHeardMessagesTree();
				
				BetEtxHelloMessage msg = (BetEtxHelloMessage) m;
				System.out.println("-------------MSG arrive------------------");
				System.out.println("Conteúdo: " + msg.toString());
				System.out.println("De: " + inbox.getSender().ID);
				System.out.println("Para: " + inbox.getReceiver().ID);
				System.out.println("Saiu em: " + inbox.getSendingTime());
				System.out.println("Chegou em: " + inbox.getArrivingTime());
				System.out.println("-------------MSG END------------------");

				handleHello(inbox.getSender(), inbox.getReceiver(),
						(GenericWeightedEdge) inbox.getIncomingEdge(), msg);

			} else if (m instanceof BetEtxReplyMessage) {
				
				statistics.countHeardMessagesTree();
				
				BetEtxReplyMessage msg = (BetEtxReplyMessage) m;
				System.out.println("-------------MSG arrive------------------");
				System.out.println("Conteúdo: " + msg.toString());
				System.out.println("De: " + inbox.getSender().ID);
				System.out.println("Para: " + inbox.getReceiver().ID);
				System.out.println("Saiu em: " + inbox.getSendingTime());
				System.out.println("Chegou em: " + inbox.getArrivingTime());
				System.out.println("-------------MSG END------------------");

				handleReply(inbox.getSender(), inbox.getReceiver(),
						(GenericWeightedEdge) inbox.getIncomingEdge(), msg);

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

	/*
	 * ============================================================= Manipulando
	 * o pacote Hello
	 * ============================================================
	 */
	private void handleHello(Node sender, Node receiver,
			GenericWeightedEdge incomingEdge, BetEtxHelloMessage msg) {
		
		// no sink nao manipula pacotes hello
		if (this.ID == msg.getSinkID()) {
			msg = null;
			return;
		}

		GenericWeightedEdge edgeToSender = (GenericWeightedEdge) incomingEdge.oppositeEdge;
		
		System.out.println("***************Informações***************");
		Float a = new Float(msg.getPathEtx() + edgeToSender.getEtx());
		System.out.println("a = "+a.toString());
		System.out.println("msg.getPathEtx() = "+String.format("%f", msg.getPathEtx()));
		System.out.println("edgeToSender.getEtx() = "+ String.format("%f", edgeToSender.getEtx()));
		System.out.println("msg.getPathEtx() + edgeToSender.getEtx() = "+ (float)(msg.getPathEtx() + edgeToSender.getEtx()));
		System.out.println("EtxPath = "+EtxPath);
		System.out.println("Compare = "+Float.compare((float) (msg.getPathEtx() + edgeToSender.getEtx()), EtxPath));
		System.out.println("***************FIM DAS Informações***************");
		

		// o nodo e vizinho direto do sink (armazena o nextHop como id do sink
		if (msg.getSinkID() == sender.ID) {
			nextHop = sender.ID;
		}

		/*
		 *  nodo acaba de ser descoberto ou acabou de encontrar um caminho mais
		 *  curto
		 */
		if ((msg.getPathEtx() + edgeToSender.getEtx() < EtxPath)
				|| EtxPath == Float.MAX_VALUE) {
			System.out.println("Entrei no foi descoberto"+this.ID);
			sinkID = msg.getSinkID();

			hops = msg.getHops() + 1;
			msg.setHops(hops);

			pathsToSink = msg.getPaths();

			nextHop = sender.ID;
			EtxPath = msg.getPathEtx() + edgeToSender.getEtx();
			msg.setPathEtx(EtxPath);

			if (!neighbors.isEmpty()) {
				neighbors.removeAll(neighbors);
			}

			// adiciona os vizinhos mais proximos do sink que sao rotas
			if (!neighbors.contains(sender.ID)) {
				neighbors.add(sender.ID);
			}
			
			//sentMyHello = false;
		}

		// existe mais de um caminho deste no ate o sink com a mesmo etx
		// acumulado
		if (msg.getPathEtx() + edgeToSender.getEtx() == EtxPath) {
			System.out.println("Entrei no + caminhos"+this.ID);
			this.setColor(Color.MAGENTA);
			pathsToSink += msg.getPaths();
			fhp.updateTimer(1, this, fhp.getFireTime());
			System.out.println("Tempo de disparo: "+fhp.getFireTime());

			// adiciona os vizinhos mais proximos do sink que sao rotas
			if (!neighbors.contains(sender.ID)) {
				neighbors.add(sender.ID);
			}
		}

		
		// ele deve encaminhar um pacote com seus dados atualizados
		// Essas flags ajudam para nao sobrecarregar a memoria com eventos
		// isto e, mandar mensagens com informacoes desatualiza
		if (!isSentMyHello()) {
			System.out.println("Entrei no foi send hello"+this.ID);
			fhp = new GenericMessageTimer(msg);
			fhp.startRelative(hops, this);
			sentMyHello = true;
		}

		// Dispara um timer para enviar um pacote de borda
		// para calculo do sbet
		// nodos do tipo border e relay devem enviar tal pacote
		if (!isSentMyReply()) {
			System.out.println("Entrei no send reply"+this.ID);
			frp = new GenericMessageTimer(new BetEtxReplyMessage());
			frp.startAbsolute((float) waitingTime(), this);
			sentMyReply = true;
		}
		
	}

	
	private float waitingTime() {
		// atraso para enviar o pacote [referencia artigo do Eduardo]
		
		float waitTime = 0.0f;
		// waitTime = 1 / (Math.exp(this.hops) * Math.pow(10, -20));
		// waitTime = 1 / (this.hops * (Math.pow(5, -3.3)));
		waitTime = (float) (Math.pow(5, 3.3) / this.hops);
		// System.out.println(waitTime);
		return waitTime + 100; // o flood somente inicia apos o tempo 100
	}

	/*
	 * ============================================================= 
	 * Manipulando o pacote Reply
	 * ============================================================
	 */
	private void handleReply(Node sender, Node receiver,
			GenericWeightedEdge incomingEdge, BetEtxReplyMessage msg) {
		
		// o sink nao deve manipular pacotes do tipo Relay
		if (this.ID == msg.getSinkID()) {
			return;
		}

		GenericWeightedEdge edgeToSender = (GenericWeightedEdge) incomingEdge.oppositeEdge;

		// necessaria verificacao de que o no ja recebeu menssagem de um
		// determinado descendente, isso e feito para evitar mensagens
		// duplicadas.
		// Se o nodo ainda nao recebeu menssagem de um descendente ele
		// deve 'processar' (recalcular o sbet) e propagar o pacote
		// desse descendente para que os outros nodos facam o mesmo
		if (!sons.contains(msg.getSenderID()) && (msg.getEtxPath() > EtxPath)
				&& (msg.getSendToNodes().contains(this.ID))
		/* (message.getSendTo() == this.ID) */) {

			System.out.println("Processando sink betweenness para o node ID="
					+ this.ID);
			sons.add(msg.getSenderID());

			// se o nodo faz essa operacao ele eh relay
			this.setColor(Color.CYAN);
			// setRole(NodeRoleEtxBet.RELAY);

			processBet(msg);

			msg.setSendTo(nextHop);
			msg.setFwdID(this.ID);
			msg.setSendToNodes(neighbors);

			// FwdPackReplyEtxBet fwdReply = new FwdPackReplyEtxBet(message);
			//eu realmente quero que ele somente encaminhe o pacote
			//com as informacoes acima preenchidas
			GenericMessageTimer fwdReply = new GenericMessageTimer(msg, true);
			fwdReply.startRelative(1, this);

		}

		
		Node n = Tools.getNodeByID(msg.getSenderID());
		System.out.println("ID="+n.ID);
		System.out.println("ID="+this.outgoingConnections.contains(this, n));
		System.out.println("IncommingEdge="+incomingEdge.getEtx());
		System.out.println("edgeToSender="+edgeToSender.getEtx());
		
		// Uma mensagem foi recebida pelos ancestrais logo devo analisar se e o
		// meu nextHop
		// virifica se a msg veio de um dos seus vizinhos diretos
		if ((msg.getEtxPath() + edgeToSender.getEtx() <= EtxPath) &&
			(this.outgoingConnections.contains(this, n))
			){
			if (msg.getsBet() >= neighborMaxSBet) {
				// System.out.println("Antes\n"+this);
				System.out.println("Atualizando neighborMaxSBet do no ID="
						+ this.ID);
				neighborMaxSBet = msg.getsBet();
				nextHop = msg.getSenderID();
				// System.out.println("Depois\n"+this+"\n\n");
			}
			msg = null;
		}

	}

	//cacula o Sink Betweenness para um no
	private void processBet(BetEtxReplyMessage msg) {
		// faz a adicao do par <chave, valor>
		// chave = num de caminhos, valor = numero de nodos
		// descendentes com 'aquela' quantidade de caminho
		if (sonsPathMap.containsKey(msg.getPath())) {
			sonsPathMap.put(msg.getPath(), sonsPathMap.get(msg.getPath()) + 1);
		} else {
			sonsPathMap.put(msg.getPath(), 1);
		}

		float tmp = 0.0f;

		for (Entry<Integer, Integer> e : sonsPathMap.entrySet())
			// faz o calculo do Sbet
			tmp = tmp
					+ (e.getValue() * ((float) this.pathsToSink / e.getKey()));

		sBet = tmp;
	}

	@Override
	public void preStep() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		statistics = new StatisticsNode(this.ID);
		
		if (this.ID == 1) {
			this.sinkID = this.ID;
			this.nextHop = this.ID;
			this.hops = 0;
			this.sentMyHello = true;
			this.pathsToSink = 1;
			this.EtxPath = 0.0f;
			this.setColor(Color.BLUE);
		
			GenericMessageTimer mt = new GenericMessageTimer(new BetEtxHelloMessage());
			mt.startRelative(Global.currentTime + 1, this);
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

		BetEtxHelloMessage hellomsg = new BetEtxHelloMessage(0, 1, this.ID, 0.0f);
		MessageTimer mt = new MessageTimer(hellomsg);
		mt.startRelative(1, this);

		this.setColor(Color.BLUE);

		Tools.appendToOutput("Node " + this.ID + " start hello flood from.");
	}

	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {

		String str = Integer.toString(this.ID);

		super.drawNodeAsSquareWithText(g, pt, highlight, str, 10, Color.YELLOW);
		// super.drawAsRoute(g, pt, highlight, 30);
	}

	@Override
	public String toString() {
		return "NodeBetEtx [role=" + role + "\n sinkID=" + sinkID + "\n hops="
				+ hops + "\n nextHop=" + nextHop + "\n pathsToSink="
				+ pathsToSink + "\n EtxPath=" + EtxPath
				+ "\n sentMyHello=" + sentMyHello + "\n sentMyReply="
				+ sentMyReply + "\n sBet=" + sBet + "\n neighborMaxSBet="
				+ neighborMaxSBet + "\n sons=" + sons + "\n neighbors="
				+ neighbors + "\n sonsPathMap=" + sonsPathMap + "]";
	}

	public boolean isSentMyHello() {
		return sentMyHello;
	}

	public boolean isSentMyReply() {
		return sentMyReply;
	}

	@Override
	public void checkRequirements() throws WrongConfigurationException {
		if (!(reliabilityModel instanceof ReliableDelivery)) {
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
		EventMessage em = new EventMessage(this.ID, nextHop,
				Tools.getGlobalTime() + timeStartEvents, 0);
		GenericMessageTimer t = new GenericMessageTimer(em);
		t.startRelative(timeStartEvents, this);
		
		statistics.countAmountSentMsg();
	}

	@Override
	public void broadcastWithNack(Message m) {
		GenericWeightedEdge edgeToTarget = null;
		EventMessage em = (EventMessage) m;

		this.setColor(Color.GRAY);
		Iterator<Edge> it = this.outgoingConnections.iterator();
		GenericWeightedEdge e;
		while (it.hasNext()) {
			e = (GenericWeightedEdge) it.next();
			this.send(m, e.endNode);

			if (em.nextHop == e.endNode.ID) {
				edgeToTarget = e;
			}
		}

		statistics.countBroadcastEv(edgeToTarget);
	}

	@Override
	public void sendUnicastMsg(Message msg, Node n, boolean fwd) {
		
		if(fwd){
			this.send(msg, n);
			return;
		}
		
		if (msg instanceof BetEtxHelloMessage) {
			BetEtxHelloMessage m = (BetEtxHelloMessage) msg;
			m.setHops(hops);
			m.setPathEtx(EtxPath);
			m.setPaths(pathsToSink);
			m.setSinkID(sinkID);

			this.send(m, n);
			return;
		}

		if (msg instanceof BetEtxReplyMessage) {
			this.setColor(Color.GREEN);
			BetEtxReplyMessage m = (BetEtxReplyMessage) msg;
			m.setSinkID(sinkID);
			m.setEtxPath(EtxPath);
			m.setFwdID(this.ID);
			m.setHops(hops);
			m.setPath(pathsToSink);
			m.setsBet(sBet);
			m.setSenderID(this.ID);
			m.setSendTo(nextHop);
			m.setSendToNodes(neighbors);

			this.send(m, n);
			return;
		}
		
	}

	@Override
	public void broadcastMsg(Message msg, boolean fwd) {

		if(fwd){
			broadcast(msg);
			statistics.countBroadcastTree();
			return;
		}
		
		if (msg instanceof BetEtxHelloMessage) {
			BetEtxHelloMessage m = (BetEtxHelloMessage) msg;
			m.setHops(hops);
			m.setPathEtx(EtxPath);
			m.setPaths(pathsToSink);
			m.setSinkID(sinkID);

			broadcast(m);
			
			statistics.countBroadcastTree();
			return;
		}

		if (msg instanceof BetEtxReplyMessage) {
			this.setColor(Color.GREEN);
			BetEtxReplyMessage m = (BetEtxReplyMessage) msg;
			m.setSinkID(sinkID);
			m.setEtxPath(EtxPath);
			m.setFwdID(this.ID);
			m.setHops(hops);
			m.setPath(pathsToSink);
			m.setsBet(sBet);
			m.setSenderID(this.ID);
			m.setSendTo(nextHop);
			m.setSendToNodes(neighbors);

			broadcast(m);
			
			statistics.countBroadcastTree();
			
			return;
		}

		if(msg instanceof EventMessage){
			broadcastWithNack(msg);
		}

	}

	@Override
	public void handleEvent(Inbox inbox, EventMessage msg) {
		// TODO Auto-generated method stub
		if ((msg.getNextHop() == 1) && (this.ID == 1)) {
			statistics.countEvReceived(inbox.getArrivingTime());
			statistics.IncomingEvents(msg.idSender, 
					Tools.getGlobalTime() - msg.firedTime);
			return;
		}

		if (msg.getNextHop() == this.ID) {
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
