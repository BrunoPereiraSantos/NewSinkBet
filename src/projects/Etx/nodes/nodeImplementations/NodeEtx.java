package projects.Etx.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;

import projects.Etx.nodes.edges.EdgeEtx;
import projects.Etx.nodes.messages.EtxHelloMessage;
import projects.Etx.nodes.nodeImplementations.NodeRoleBetEtx;
import projects.Etx.nodes.timers.EtxMessageTimer;
import projects.defaultProject.nodes.timers.MessageTimer;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.nodes.messages.NackBox;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

public class NodeEtx extends Node {
	// Qual o papel do nodo
	private NodeRoleBetEtx role;

	// ID do no sink
	private int sinkID;

	// numero de hops ate o sink
	private int hops;

	// id do prox no usando a metrica numero de hops
	private int nextHop;

	// rate acumulado do caminho
	private float EtxPath = Float.MAX_VALUE;

	// Flag para indicar se o nodo ja enviou seu pkt hello
	private boolean sentMyHello = false;

	// Disparadores de flood
	EtxMessageTimer fhp;

	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub
		while (inbox.hasNext()) {
			Message m = inbox.next();

			if (m instanceof EtxHelloMessage) {
				EtxHelloMessage msg = (EtxHelloMessage) m;
				System.out.println("-------------MSG arrive------------------");
				System.out.println("Conteúdo: " + msg.toString());
				System.out.println("De: " + inbox.getSender().ID);
				System.out.println("Para: " + inbox.getReceiver().ID);
				System.out.println("Saiu em: " + inbox.getSendingTime());
				System.out.println("Chegou em: " + inbox.getArrivingTime());
				System.out.println("-------------MSG END------------------");

				handleHello(inbox.getSender(), inbox.getReceiver(),
						(EdgeEtx) inbox.getIncomingEdge(), msg);

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
			EdgeEtx incomingEdge, EtxHelloMessage msg) {
		
		// no sink nao manipula pacotes hello
		if (this.ID == msg.getSinkID()) {
			msg = null;
			return;
		}

		EdgeEtx edgeToSender = (EdgeEtx) incomingEdge.oppositeEdge;
		
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
		if ((msg.getPathEtx() + edgeToSender.getEtx() < EtxPath)
				|| EtxPath == Float.MAX_VALUE) {
			System.out.println("Entrei no foi descoberto"+this.ID);
			sinkID = msg.getSinkID();

			hops = msg.getHops() + 1;
			msg.setHops(hops);

			nextHop = sender.ID;
			EtxPath = msg.getPathEtx() + edgeToSender.getEtx();
			msg.setPathEtx(EtxPath);
		}

		
		// ele deve encaminhar um pacote com seus dados atualizados
		// Essas flags ajudam para nao sobrecarregar a memoria com eventos
		// isto e, mandar mensagens com informacoes desatualiza
		if (!isSentMyHello()) {
			System.out.println("Entrei no foi send hello"+this.ID);
			fhp = new EtxMessageTimer(new EtxHelloMessage());
			fhp.startRelative(hops, this);
			sentMyHello = true;
		}

		
		msg = null;
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
			
			EtxHelloMessage hellomsg = new EtxHelloMessage(0, this.ID, this.EtxPath);
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

		EtxHelloMessage hellomsg = new EtxHelloMessage(0, this.ID, 0.0f);
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
		if (msg instanceof EtxHelloMessage) {
			EtxHelloMessage m = (EtxHelloMessage) msg;
			m.setHops(hops);
			m.setPathEtx(EtxPath);
			m.setSinkID(sinkID);

			broadcast(m);
		}

	}

	@Override
	public String toString() {
		return "NodeBetEtx [role=" + role + "\n sinkID=" + sinkID + "\n hops="
				+ hops + "\n nextHop=" + nextHop + "\n EtxPath=" + EtxPath
				+ "\n sentMyHello=" + sentMyHello + "]";
	}

	public boolean isSentMyHello() {
		return sentMyHello;
	}

}
