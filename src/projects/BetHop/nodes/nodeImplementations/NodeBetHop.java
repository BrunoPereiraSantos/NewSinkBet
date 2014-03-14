package projects.BetHop.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import projects.BetHop.nodes.edges.EdgeBetHop;
import projects.BetHop.nodes.messages.BetHopHelloMessage;
import projects.BetHop.nodes.messages.BetHopReplyMessage;
import projects.BetHop.nodes.nodeImplementations.NodeRoleBetHop;
import projects.BetHop.nodes.timers.BetHopMessageTimer;
import projects.defaultProject.nodes.timers.MessageTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.nodes.messages.NackBox;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

public class NodeBetHop extends Node {
	// Qual o papel do nodo
	private NodeRoleBetHop role;

	// ID do no sink
	private int sinkID;

	// numero de hops ate o sink
	private int hops = Integer.MAX_VALUE;

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
	BetHopMessageTimer fhp;
	BetHopMessageTimer frp;

	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub
		while (inbox.hasNext()) {
			Message m = inbox.next();

			if (m instanceof BetHopHelloMessage) {
				BetHopHelloMessage msg = (BetHopHelloMessage) m;
				System.out.println("-------------MSG arrive------------------");
				System.out.println("Conteúdo: " + msg.toString());
				System.out.println("De: " + inbox.getSender().ID);
				System.out.println("Para: " + inbox.getReceiver().ID);
				System.out.println("Saiu em: " + inbox.getSendingTime());
				System.out.println("Chegou em: " + inbox.getArrivingTime());
				System.out.println("-------------MSG END------------------");

				handleHello(inbox.getSender(), inbox.getReceiver(),
						(EdgeBetHop) inbox.getIncomingEdge(), msg);

			} else if (m instanceof BetHopReplyMessage) {
				BetHopReplyMessage msg = (BetHopReplyMessage) m;
				System.out.println("-------------MSG arrive------------------");
				System.out.println("Conteúdo: " + msg.toString());
				System.out.println("De: " + inbox.getSender().ID);
				System.out.println("Para: " + inbox.getReceiver().ID);
				System.out.println("Saiu em: " + inbox.getSendingTime());
				System.out.println("Chegou em: " + inbox.getArrivingTime());
				System.out.println("-------------MSG END------------------");

				handleReply(inbox.getSender(), inbox.getReceiver(),
						(EdgeBetHop) inbox.getIncomingEdge(), msg);

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
			EdgeBetHop incomingEdge, BetHopHelloMessage msg) {
		
		// no sink nao manipula pacotes hello
		if (this.ID == msg.getSinkID()) {
			msg = null;
			return;
		}

		EdgeBetHop edgeToSender = (EdgeBetHop) incomingEdge.oppositeEdge;
		
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

		// nodo acaba de ser descoberto ou acabou de encontrar um caminho mais
		// curto
		if (msg.getHops() + 1 < hops) {
			System.out.println("Entrei no foi descoberto"+this.ID);
			sinkID = msg.getSinkID();

			hops = msg.getHops() + 1;
			msg.setHops(hops);

			pathsToSink = msg.getPaths();

			nextHop = sender.ID;

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
		if (msg.getHops() + 1 == hops) {
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
			fhp = new BetHopMessageTimer(new BetHopHelloMessage());
			fhp.startRelative(hops, this);
			sentMyHello = true;
		}

		// Dispara um timer para enviar um pacote de borda
		// para calculo do sbet
		// nodos do tipo border e relay devem enviar tal pacote
		if (!isSentMyReply()) {
			System.out.println("Entrei no send reply"+this.ID);
			frp = new BetHopMessageTimer(new BetHopReplyMessage());
			frp.startAbsolute((float) waitingTime(), this);
			sentMyReply = true;
		}
		
		msg = null;
	}

	// atraso para enviar o pacote [referencia artigo do Eduardo]
	private float waitingTime() {
		float waitTime = 0.0f;
		// waitTime = 1 / (Math.exp(this.hops) * Math.pow(10, -20));
		// waitTime = 1 / (this.hops * (Math.pow(5, -3.3)));
		waitTime = (float) (Math.pow(5, 3.3) / this.hops);
		// System.out.println(waitTime);
		return waitTime + 100; // o flood somente inicia apos o tempo 100
	}

	/*
	 * ============================================================= Manipulando
	 * o pacote Reply
	 * ============================================================
	 */
	private void handleReply(Node sender, Node receiver,
			EdgeBetHop incomingEdge, BetHopReplyMessage msg) {
		
		// o sink nao deve manipular pacotes do tipo Relay
		if (this.ID == msg.getSinkID()) {
			return;
		}

		EdgeBetHop edgeToSender = (EdgeBetHop) incomingEdge.oppositeEdge;

		// necessaria verificacao de que o no ja recebeu menssagem de um
		// determinado descendente, isso e feito para evitar mensagens
		// duplicadas.
		// Se o nodo ainda nao recebeu menssagem de um descendente ele
		// deve 'processar' (recalcular o sbet) e propagar o pacote
		// desse descendente para que os outros nodos facam o mesmo
		if (!sons.contains(msg.getSenderID()) && (msg.getHops() > hops)
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
		System.out.println("IncommingEdge="+incomingEdge.getEtx());
		System.out.println("edgeToSender="+edgeToSender.getEtx());
		
		// Uma mensagem foi recebida pelos ancestrais logo devo analisar se e o
		// meu nextHop
		// virifica se a msg veio de um dos seus vizinhos diretos
		if ((msg.getHops()  < hops) &&
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
	private void processBet(BetHopReplyMessage msg) {
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
			tmp = tmp + (e.getValue() * ((float) this.pathsToSink / e.getKey()));

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
			this.EtxPath = 0.0f;
			
			BetHopHelloMessage hellomsg = new BetHopHelloMessage(0, 1, this.ID, this.EtxPath);
			MessageTimer mt = new MessageTimer(hellomsg);
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

	@Override
	public void checkRequirements() throws WrongConfigurationException {
		// TODO Auto-generated method stub

	}

	@NodePopupMethod(menuText = "Start")
	public void start() {

		BetHopHelloMessage hellomsg = new BetHopHelloMessage(0, 1, this.ID, 0.0f);
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

	public void sendUnicastBetEtxMsg(Message msg, Node receiver) {
		// TODO Auto-generated method stub

	}

	public void broadcastBetEtxMsg(Message msg) {
		if (msg instanceof BetHopHelloMessage) {
			BetHopHelloMessage m = (BetHopHelloMessage) msg;
			m.setHops(hops);
			m.setPathEtx(EtxPath);
			m.setPaths(pathsToSink);
			m.setSinkID(sinkID);

			broadcast(m);
		}

		if (msg instanceof BetHopReplyMessage) {
			this.setColor(Color.GREEN);
			BetHopReplyMessage m = (BetHopReplyMessage) msg;
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
		}

	}

	@Override
	public String toString() {
		return "NodeBetHop [role=" + role + "\n sinkID=" + sinkID + "\n hops="
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

}
