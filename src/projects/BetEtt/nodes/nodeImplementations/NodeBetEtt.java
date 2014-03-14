package projects.BetEtt.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import projects.BetEtt.nodes.edges.EdgeBetEtt;
import projects.BetEtt.nodes.messages.BetEttHelloMessage;
import projects.BetEtt.nodes.messages.BetEttReplyMessage;
import projects.BetEtt.nodes.nodeImplementations.NodeRoleBetEtt;
import projects.BetEtt.nodes.timers.BetEttMessageTimer;
import projects.defaultProject.nodes.timers.MessageTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.nodes.messages.NackBox;
import sinalgo.tools.Tools;

public class NodeBetEtt extends Node {
	// Qual o papel do nodo
	private NodeRoleBetEtt role;

	// ID do no sink
	private int sinkID;

	// numero de hops ate o sink
	private int hops;

	// id do prox no usando a metrica numero de hops
	private int nextHop;

	// numero de caminhos para o sink
	private int pathsToSink;

	// rate acumulado do caminho
	private double EttPath = Double.MAX_VALUE;

	// Flag para indicar se o nodo ja enviou seu pkt hello
	private boolean sentMyHello = false;

	// Flag para indicar se o nodo ja enviou seu pkt border
	private boolean sentMyReply = false;

	// valor do Sink Betweenness
	private double sBet;

	// Valor do maior sBet entre os vizinhos diretos
	private double neighborMaxSBet;

	// array com os filhos (this.ett < sons.ett)
	private ArrayList<Integer> sons = new ArrayList<Integer>();

	// array com os vizinhos diretos do nodo.
	private ArrayList<Integer> neighbors = new ArrayList<Integer>();

	// cada nodo mantem um map com chave(num de caminhos) e valor(quantos nodos
	// descendentes tem 'num de caminhos'
	private Map<Integer, Integer> sonsPathMap = new HashMap<Integer, Integer>();

	// Disparadores de flood
	BetEttMessageTimer fhp;
	BetEttMessageTimer frp;

	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub
		while (inbox.hasNext()) {
			Message m = inbox.next();

			if (m instanceof BetEttHelloMessage) {
				BetEttHelloMessage msg = (BetEttHelloMessage) m;
				System.out.println("-------------MSG arrive------------------");
				System.out.println("Conteúdo: " + msg.toString());
				System.out.println("De: " + inbox.getSender().ID);
				System.out.println("Para: " + inbox.getReceiver().ID);
				System.out.println("Saiu em: " + inbox.getSendingTime());
				System.out.println("Chegou em: " + inbox.getArrivingTime());
				System.out.println("-------------MSG END------------------");

				handleHello(inbox.getSender(), inbox.getReceiver(),
						(EdgeBetEtt) inbox.getIncomingEdge(), msg);

			} else if (m instanceof BetEttReplyMessage) {
				BetEttReplyMessage msg = (BetEttReplyMessage) m;
				System.out.println("-------------MSG arrive------------------");
				System.out.println("Conteúdo: " + msg.toString());
				System.out.println("De: " + inbox.getSender().ID);
				System.out.println("Para: " + inbox.getReceiver().ID);
				System.out.println("Saiu em: " + inbox.getSendingTime());
				System.out.println("Chegou em: " + inbox.getArrivingTime());
				System.out.println("-------------MSG END------------------");

				handleReply(inbox.getSender(), inbox.getReceiver(),
						(EdgeBetEtt) inbox.getIncomingEdge(), msg);

			}

		}

	}

	public void handleNAckMessages(NackBox nackBox) {
		while (nackBox.hasNext()) {
			/*
			 * Message msg = nackBox.next(); StringMessage m = (StringMessage)
			 * msg;
			 * 
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

	/*
	 * ============================================================= Manipulando
	 * o pacote Hello
	 * ============================================================
	 */
	private void handleHello(Node sender, Node receiver,
			EdgeBetEtt incomingEdge, BetEttHelloMessage msg) {

		// no sink nao manipula pacotes hello
		if (this.ID == msg.getSinkID()) {
			msg = null;
			return;
		}

		EdgeBetEtt edgeToSender = (EdgeBetEtt) incomingEdge.oppositeEdge;

		// o nodo e vizinho direto do sink (armazena o nextHop como id do sink
		if (msg.getSinkID() == sender.ID) {
			nextHop = sender.ID;
		}

		// nodo acaba de ser descoberto ou acabou de encontrar um caminho mais
		// curto
		if ((msg.getPathEtt() + edgeToSender.getEtt() < EttPath)
				/*|| EttPath == Integer.MAX_VALUE*/) {
			System.out.println("Entrei no foi descoberto"+this.ID);
			sinkID = msg.getSinkID();

			hops = msg.getHops() + 1;
			msg.setHops(hops);

			pathsToSink = msg.getPaths();

			nextHop = sender.ID;

			EttPath = msg.getPathEtt() + edgeToSender.getEtt();
			msg.setPathEtt(EttPath);

			if (!neighbors.isEmpty()) {
				neighbors.removeAll(neighbors);
			}

			// adiciona os vizinhos mais proximos do sink que sao rotas
			if (!neighbors.contains(sender.ID)) {
				neighbors.add(sender.ID);
			}

			//sentMyHello = false;
		}

		// existe mais de um caminho deste no ate o sink com a mesmo ett
		// acumulado
		if (msg.getPathEtt() + edgeToSender.getEtt() == EttPath) {
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
			fhp = new BetEttMessageTimer(msg);
			fhp.startRelative(hops, this);
			sentMyHello = true;
		}

		// Dispara um timer para enviar um pacote de borda
		// para calculo do sbet
		// nodos do tipo border e relay devem enviar tal pacote
		if (!isSentMyReply()) {
			System.out.println("Entrei no send reply"+this.ID);
			frp = new BetEttMessageTimer(new BetEttReplyMessage());
			frp.startAbsolute((double) waitingTime(), this);
			sentMyReply = true;
		}

	}

	// atraso para enviar o pacote [referencia artigo do Eduardo]
	private double waitingTime() {
		double waitTime = 0.0;
		// waitTime = 1 / (Math.exp(this.hops) * Math.pow(10, -20));
		// waitTime = 1 / (this.hops * (Math.pow(5, -3.3)));
		waitTime = Math.pow(5, 3.3) / this.hops;
		// System.out.println(waitTime);
		return waitTime + 100; // o flood somente inicia apos o tempo 100
	}

	/*
	 * ============================================================= Manipulando
	 * o pacote Reply
	 * ============================================================
	 */
	private void handleReply(Node sender, Node receiver,
			EdgeBetEtt incomingEdge, BetEttReplyMessage msg) {

		// o sink nao deve manipular pacotes do tipo Relay
		if (this.ID == msg.getSinkID()) {
			return;
		}

		EdgeBetEtt edgeToSender = (EdgeBetEtt) incomingEdge.oppositeEdge;

		// necessaria verificacao de que o no ja recebeu menssagem de um
		// determinado descendente, isso e feito para evitar mensagens
		// duplicadas.
		// Se o nodo ainda nao recebeu menssagem de um descendente ele
		// deve 'processar' (recalcular o sbet) e propagar o pacote
		// desse descendente para que os outros nodos facam o mesmo
		if (!sons.contains(msg.getSenderID()) && (msg.getEttPath() > EttPath)
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
			MessageTimer fwdReply = new MessageTimer(msg);
			fwdReply.startRelative(1, this);

		}

		
		Node n = Tools.getNodeByID(msg.getSenderID());
		System.out.println("ID="+n.ID);
		System.out.println("ID="+this.outgoingConnections.contains(this, n));
		System.out.println("IncommingEdge="+incomingEdge.getEtt());
		System.out.println("edgeToSender="+edgeToSender.getEtt());
		
		// Uma mensagem foi recebida pelos ancestrais logo devo analisar se e o
		// meu nextHop
		// virifica se a msg veio de um dos seus vizinhos diretos
		if ((msg.getEttPath() + edgeToSender.getEtt() <= EttPath) &&
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
	private void processBet(BetEttReplyMessage msg) {
		// faz a adicao do par <chave, valor>
		// chave = num de caminhos, valor = numero de nodos
		// descendentes com 'aquela' quantidade de caminho
		if (sonsPathMap.containsKey(msg.getPath())) {
			sonsPathMap.put(msg.getPath(), sonsPathMap.get(msg.getPath()) + 1);
		} else {
			sonsPathMap.put(msg.getPath(), 1);
		}

		double tmp = 0.0;

		for (Entry<Integer, Integer> e : sonsPathMap.entrySet())
			// faz o calculo do Sbet
			tmp = tmp
					+ (e.getValue() * ((double) this.pathsToSink / e.getKey()));

		sBet = tmp;
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

			BetEttHelloMessage hellomsg = new BetEttHelloMessage(0, 1, this.ID,
					0);
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

		BetEttHelloMessage hellomsg = new BetEttHelloMessage(0, 1, this.ID, 0);
		MessageTimer mt = new MessageTimer(hellomsg);
		mt.startRelative(1, this);

		this.setColor(Color.BLUE);

		Tools.appendToOutput("Node " + this.ID + " start hello flood from.");
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
		if (msg instanceof BetEttHelloMessage) {
			BetEttHelloMessage m = (BetEttHelloMessage) msg;
			m.setHops(hops);
			m.setPathEtt(EttPath);
			m.setPaths(pathsToSink);
			m.setSinkID(sinkID);

			broadcast(m);
		}

		if (msg instanceof BetEttReplyMessage) {
			this.setColor(Color.GREEN);
			BetEttReplyMessage m = (BetEttReplyMessage) msg;
			m.setSinkID(sinkID);
			m.setEttPath(EttPath);
			m.setFwdID(this.ID);
			m.setHops(hops);
			m.setPath(pathsToSink);
			m.setsBet(sBet);
			m.setSenderID(this.ID);
			m.setSendTo(nextHop);
			m.setSendToNodes(neighbors);

			broadcast(m);
		}

	}

	@Override
	public String toString() {
		return "NodeBetEtt [role=" + role + "\n sinkID=" + sinkID + "\n hops="
				+ hops + "\n nextHop=" + nextHop + "\n pathsToSink="
				+ pathsToSink + "\n EttPath=" + String.format("%.2f", EttPath)
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

}
