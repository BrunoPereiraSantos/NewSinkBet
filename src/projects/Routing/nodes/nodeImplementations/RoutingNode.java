package projects.Routing.nodes.nodeImplementations;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import projects.Routing.nodes.messages.PackEvent;
import projects.Routing.nodes.messages.PackHello;
import projects.Routing.nodes.messages.PackReply;
import projects.Routing.nodes.nodeImplementations.MetricStrategy.EttStrategy;
import projects.Routing.nodes.nodeImplementations.MetricStrategy.EtxStrategy;
import projects.Routing.nodes.nodeImplementations.MetricStrategy.HopStrategy;
import projects.Routing.nodes.nodeImplementations.MetricStrategy.MetricEnum;
import projects.Routing.nodes.nodeImplementations.MetricStrategy.MtmStrategy;
import projects.Routing.nodes.nodeImplementations.MetricStrategy.MetricStrategyInterface;
import projects.Routing.nodes.nodeImplementations.Protocol.Protocol;
import projects.Routing.nodes.nodeImplementations.Protocol.ProtocolEnum;
import projects.Routing.nodes.nodeImplementations.Protocol.SinkBetweennessProtocol;
import projects.Routing.nodes.timers.RoutingMessageTimer;
import projects.defaultProject.models.reliabilityModels.ReliableDelivery;
import sinalgo.configuration.Configuration;
import sinalgo.configuration.CorruptConfigurationEntryException;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.nodes.messages.NackBox;
import sinalgo.runtime.Global;

public class RoutingNode extends AbstractRoutingNode {

	// Estrategia (Metrica) que o nodo devera seguir
	public MetricStrategyInterface strategy = null;

	// Qual protocolo o nodo vai seguir (com centralidade ou sem centralidade)
	public Protocol protocol = null;

	// ID do no sink
	public int sinkID = 0;

	// id do prox no usando a metrica numero de hops
	public int nextHop = 0;

	// numero de caminhos para o sink
	public int pathsToSink = 0;

	// numero de hops ate o sink
	public int hops = 0;

	// numero de caminhos para o sink
	public float metricPath = Float.MAX_VALUE;

	// Flag para indicar se o nodo ja enviou seu pkt hello
	public boolean sentMyHello = false;

	// Flag para indicar se o nodo ja enviou seu pkt border
	public boolean sentMyReply = false;

	// array com os vizinhos diretos do nodo.
	public ArrayList<Integer> neighborsFathes = new ArrayList<Integer>();

	// array com os filhos (this.etx < sons.etx)
	public ArrayList<Integer> sons = new ArrayList<Integer>();

	// cada nodo mantem um map com chave(num de caminhos) e valor(quantos nodos
	// descendentes tem 'num de caminhos'
	public Map<Integer, Integer> sonsPathMap = new HashMap<Integer, Integer>();

	// valor do Sink Betweenness
	public float sBet;

	// Valor do maior sBet entre os vizinhos diretos
	public float neighborMaxSBet;

	// Disparadores de flood
	public RoutingMessageTimer fhp;
	public RoutingMessageTimer frp;

	@Override
	public void handleMessages(Inbox inbox) {
		while (inbox.hasNext()) {
			Message m = inbox.next();

			if (m instanceof PackHello) {
				PackHello msg = (PackHello) m;

				System.out.println("-------------MSG Hello arrive------------------");
				System.out.println("Conteúdo: " + msg.toString());
				System.out.println("De: " + inbox.getSender().ID);
				System.out.println("Para: " + inbox.getReceiver().ID);
				System.out.println("Saiu em: " + inbox.getSendingTime());
				System.out.println("Chegou em: " + inbox.getArrivingTime());
				System.out.println("-------------MSG END------------------");

				protocol.interceptPackHello(inbox, msg);

			} else if (m instanceof PackReply) {
				PackReply msg = (PackReply) m;

				System.out.println("-------------MSG Reply arrive------------------");
				System.out.println("Conteúdo: " + msg.toString());
				System.out.println("De: " + inbox.getSender().ID);
				System.out.println("Para: " + inbox.getReceiver().ID);
				System.out.println("Saiu em: " + inbox.getSendingTime());
				System.out.println("Chegou em: " + inbox.getArrivingTime());
				System.out.println("-------------MSG END------------------");

				protocol.interceptPackReply(inbox, msg);

			} else if (m instanceof PackEvent) {
				PackEvent msg = (PackEvent) m;
				
				System.out.println("-------------MSG Event arrive------------------");
				System.out.println("Conteúdo: " + msg.toString());
				System.out.println("De: " + inbox.getSender().ID);
				System.out.println("Para: " + inbox.getReceiver().ID);
				System.out.println("Saiu em: " + inbox.getSendingTime());
				System.out.println("Chegou em: " + inbox.getArrivingTime());
				System.out.println("-------------MSG END------------------");

				protocol.interceptPackEvent(inbox, msg);
			}

		}

	}

	@Override
	public void handleNAckMessages(NackBox nackBox) {
		while (nackBox.hasNext()) {
			Message m = nackBox.next();
			if (m instanceof PackEvent) {
				PackEvent msg = (PackEvent) m;
				System.out.println("-------------NACK arrive------------------");
				System.out.println("Conteúdo: " + msg.toString());
				System.out.println("De: " + nackBox.getSender().ID);
				System.out.println("Para: " + nackBox.getReceiver().ID);
				System.out.println("Saiu em: " + nackBox.getSendingTime());
				System.out.println("Chegou em: " + nackBox.getArrivingTime());
				System.out.println("-------------MSG END------------------");
				
				protocol.interceptNack(nackBox, msg);
			}
		}
	}

	@Override
	public void preStep() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {

		if (this.ID == 1) {
			this.sinkID = this.ID;
			this.nextHop = this.ID;
			this.hops = 0;
			this.sentMyHello = true;
			this.pathsToSink = 1;
			this.metricPath = 0.0f;

			RoutingMessageTimer mt = new RoutingMessageTimer(null,
					new PackHello(), true);
			mt.startRelative(Global.currentTime + 1, this);
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
		// Devo utilizar as estratégias indicadas
		// pelo arquivo xml de conf.

		if (!(reliabilityModel instanceof ReliableDelivery)) {
			// os nodos devem iniciar com entrega confiável

			this.reliabilityModel = new ReliableDelivery();
		}

		try {
			// Set o protocolo utilizado no algoritmo
			// caso o protocolo informado não esteja implementado
			// será utilizado o protocolo sinkBetweenness por default

			String protocolPraram = "";
			protocolPraram = Configuration.getStringParameter("Protocol");

			protocol = chosenProtocol(protocolPraram);

		} catch (CorruptConfigurationEntryException e1) {
			e1.printStackTrace();
		}

		try {
			// Set a metrica utilizada no algoritmo
			// se a métrica não foi implementada será utilizada
			// a métrica hop por default

			String metricParam = "";
			metricParam = Configuration.getStringParameter("Metric/strategy");

			strategy = chosenStrategy(metricParam);
		} catch (CorruptConfigurationEntryException e) {
			e.printStackTrace();
		}

	}

	private Protocol chosenProtocol(String protocolPraram) {
		ProtocolEnum p = ProtocolEnum.valueOf(protocolPraram);

		switch (p) {
		case SinkBetweenness:
			return new SinkBetweennessProtocol();
		default:
			return new SinkBetweennessProtocol();
		}
	}

	private MetricStrategyInterface chosenStrategy(String metricParam) {
		MetricEnum m = MetricEnum.valueOf(metricParam);

		switch (m) {
		case ETX:
			return new EtxStrategy();
		case ETT:
			return new EttStrategy();
		case MTM:
			return new MtmStrategy();
		default:
			return new HopStrategy();
		}
	}

	public boolean isSentMyHello() {
		return sentMyHello;
	}

	public void setSentMyHello(boolean sentMyHello) {
		this.sentMyHello = sentMyHello;
	}

	public boolean isSentMyReply() {
		return sentMyReply;
	}

	public void setSentMyReply(boolean sentMyReply) {
		this.sentMyReply = sentMyReply;
	}

	@Override
	public void forwardMsg(Message m) {
		broadcast(m);
	}

	@Override
	public void processBroadcastMsg(Message m) {

		if (m instanceof PackHello) {
			((PackHello) m).setHops(hops);
			((PackHello) m).setMetric(metricPath);
			((PackHello) m).setPaths(pathsToSink);
			((PackHello) m).setSinkID(sinkID);

			broadcast(m);
		}

		if (m instanceof PackReply) {
			((PackReply) m).setHops(hops);
			((PackReply) m).setPath(pathsToSink);
			((PackReply) m).setSenderID(this.ID);
			((PackReply) m).setSinkID(sinkID);
			((PackReply) m).setSendTo(nextHop);
			((PackReply) m).setSendToNodes(neighborsFathes);
			((PackReply) m).setMetric(metricPath);
			((PackReply) m).setsBet(sBet);
			((PackReply) m).setFwdID(this.ID);

			broadcast(m);
		}
		
		if(m instanceof PackEvent){
			((PackEvent) m).setNextHop(nextHop);
			
			broadcast(m);
		}

	}

	@Override
	public void processBroadcastMsgWithNack(Message m) {
		// TODO Auto-generated method stub
	}

	@Override
	public String toString() {
		return "RoutingNode [sinkID=" + sinkID + "\nnextHop=" + nextHop
				+ "\npathsToSink=" + pathsToSink + "\nhops=" + hops
				+ "\nmetricPath=" + metricPath + "\nsentMyHello=" + sentMyHello
				+ "\nsentMyReply=" + sentMyReply + "\nneighborsFathes="
				+ neighborsFathes + "\nsons=" + sons + "\nsonsPathMap="
				+ sonsPathMap + "\nsBet=" + sBet + "\nneighborMaxSBet="
				+ neighborMaxSBet + "]";
	}

}
