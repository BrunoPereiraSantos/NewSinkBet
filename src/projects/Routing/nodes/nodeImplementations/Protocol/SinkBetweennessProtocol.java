package projects.Routing.nodes.nodeImplementations.Protocol;

import java.awt.Color;
import java.util.Map.Entry;

import projects.Routing.nodes.edges.WeightEdge;
import projects.Routing.nodes.messages.PackEvent;
import projects.Routing.nodes.messages.PackHello;
import projects.Routing.nodes.messages.PackReply;
import projects.Routing.nodes.nodeImplementations.RoutingNode;
import projects.Routing.nodes.timers.RoutingMessageTimer;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;
import sinalgo.tools.statistics.UniformDistribution;

public class SinkBetweennessProtocol extends Protocol {

	public SinkBetweennessProtocol(boolean activeAgregation,
			double timeInAgregation) {
		super(activeAgregation, timeInAgregation);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void interceptPackHello(Inbox inbox, PackHello msg) {
		RoutingNode receiver = (RoutingNode) inbox.getReceiver();
		RoutingNode sender = (RoutingNode) inbox.getSender();

		if (receiver.ID == msg.getSinkID()) {
			// no sink nao manipula pacotes hello
			msg = null;
			return;
		}

		WeightEdge edgeToSender = (WeightEdge) inbox.getIncomingEdge().oppositeEdge;

		if (msg.getSinkID() == sender.ID) {
			// o nodo e vizinho direto do sink (armazena o nextHop como id do
			// sink
			receiver.nextHop = sender.ID;

		}

		if ((receiver.strategy.calcGradient(msg, edgeToSender) < receiver.metricPath)
				|| receiver.metricPath == Float.MAX_VALUE) {
			// nodo acaba de ser descoberto
			// ou acabou de encontrar um caminho mais curto
			// atualizo o pacote somente no momento do envio

			System.out.println("Entrei no foi descoberto " + receiver.ID);

			receiver.sinkID = msg.getSinkID();

			receiver.hops = msg.getHops() + 1;

			receiver.pathsToSink = msg.getPaths();

			receiver.nextHop = sender.ID;

			receiver.metricPath = receiver.strategy.calcGradient(msg,
					edgeToSender);

			if (!receiver.neighborsFathes.isEmpty()) {
				receiver.neighborsFathes.removeAll(receiver.neighborsFathes);
			}

			// adiciona os vizinhos mais proximos do sink que sao rotas
			if (!receiver.neighborsFathes.contains(sender.ID)) {
				receiver.neighborsFathes.add(sender.ID);
			}

		} else if ((receiver.strategy.calcGradient(msg, edgeToSender) == receiver.metricPath)
				&& (msg.getHops() + 1 == receiver.hops)) {
			// existe mais de um caminho deste no ate o sink
			// com a mesma métrica acumulada

			System.out.println("Entrei no + caminhos " + receiver.ID);
			receiver.setColor(Color.MAGENTA);
			receiver.pathsToSink += msg.getPaths();

			// fhp.updateTimer(1, this, fhp.getFireTime());
			// System.out.println("Tempo de disparo: " + fhp.getFireTime());

			if (!receiver.neighborsFathes.contains(sender.ID)) {
				// adiciona os vizinhos mais proximos do sink que sao rotas

				receiver.neighborsFathes.add(sender.ID);
			}

		}

		if (!receiver.isSentMyHello()) {
			// ele deve encaminhar um pacote com seus dados atualizados
			// Essas flags ajudam para nao sobrecarregar a memoria com eventos
			// isto e, mandar mensagens com informacoes desatualiza

			System.out.println("Entrei no foi send hello" + receiver.ID);
			receiver.fhp = new RoutingMessageTimer(msg, true);
			receiver.fhp.startRelative(waitingTimeHello(receiver.hops),
					receiver);
			receiver.sentMyHello = true;
		}

		if (!receiver.isSentMyReply()) {
			// Dispara um timer para enviar um pacote de borda
			// para calculo do sbet
			// nodos do tipo border e relay devem enviar tal pacote

			System.out.println("Entrei no send reply" + receiver.ID);
			receiver.frp = new RoutingMessageTimer(new PackReply(), true);
			receiver.frp.startRelative((float) waitingTimeReply(receiver.hops),
					receiver);
			receiver.sentMyReply = true;
		}

	}

	private float waitingTimeHello(int x) {
		// atraso para enviar o pacote [referencia artigo do Eduardo]
		UniformDistribution r = new UniformDistribution(0, 1);
		return (float) (x + r.nextSample());
	}

	private float waitingTimeReply(int x) {
		// atraso para enviar o pacote [referencia artigo do Eduardo]
		UniformDistribution r = new UniformDistribution(0, 1);
		return (float) (1 / (Math.sqrt(x) * Math.pow(10, -3)) + r.nextSample());

		/*
		 * float waitTime = 0.0f; // waitTime = 1 / (Math.exp(this.hops) *
		 * Math.pow(10, -20)); // waitTime = 1 / (this.hops * (Math.pow(5,
		 * -3.3))); waitTime = (float) Math.pow(5, 3.3) / x; //
		 * System.out.println(waitTime); return (float) waitTime + 100; // o
		 * flood somente inicia apos o tempo // 100
		 */
	}

	@Override
	public void interceptPackReply(Inbox inbox, PackReply msg) {
		RoutingNode receiver = (RoutingNode) inbox.getReceiver();
		WeightEdge edgeToSender = (WeightEdge) inbox.getIncomingEdge().oppositeEdge;

		if (receiver.ID == msg.getSinkID()) {
			// o sink nao deve manipular pacotes do tipo Relay
			return;
		}

		if (!receiver.sons.contains(msg.getSenderID())
				&& (msg.getMetric() > receiver.metricPath)
				&& (msg.getSendToNodes().contains(receiver.ID))
		/* (message.getSendTo() == this.ID) */) {
			// necessaria verificacao de que o no ja recebeu menssagem de um
			// determinado descendente, isso e feito para evitar mensagens
			// duplicadas.
			// Se o nodo ainda nao recebeu menssagem de um descendente ele
			// deve 'processar' (recalcular o sbet) e propagar o pacote
			// desse descendente para que os outros nodos facam o mesmo

			System.out.println("Processando sink betweenness para o node ID="
					+ receiver.ID);
			receiver.sons.add(msg.getSenderID());

			// se o nodo faz essa operacao ele eh relay
			receiver.setColor(Color.CYAN);
			// setRole(NodeRoleEtxBet.RELAY);

			processBet(receiver, msg);

			msg.setSendTo(receiver.nextHop);
			msg.setFwdID(receiver.ID);
			msg.setSendToNodes(receiver.neighborsFathes);

			// FwdPackReplyEtxBet fwdReply = new FwdPackReplyEtxBet(message);
			// eu realmente quero que ele somente encaminhe o pacote
			// com as informacoes acima preenchidas
			RoutingMessageTimer fwdReply = new RoutingMessageTimer(msg, false);
			fwdReply.startRelative(0.01, receiver);

		}

		Node n = Tools.getNodeByID(msg.getSenderID());
		if ((receiver.strategy.calcGradient(msg, edgeToSender) <= receiver.metricPath)
				&& (receiver.outgoingConnections.contains(receiver, n))) {
			// Uma mensagem foi recebida pelos ancestrais, logo
			// devo analisar se e o meu nextHop
			// virifica se a msg veio de um dos seus vizinhos diretos

			if (msg.getsBet() >= receiver.neighborMaxSBet) {
				// se o nodo que enviou a mensagem eh mais central
				// entao devo substituir meu next hop

				// System.out.println("Antes\n"+this);
				System.out.println("Atualizando neighborMaxSBet do no ID="
						+ receiver.ID);
				receiver.neighborMaxSBet = msg.getsBet();
				receiver.nextHop = msg.getSenderID();
				// System.out.println("Depois\n"+this+"\n\n");
			}
		}
	}

	private void processBet(RoutingNode n, PackReply msg) {
		// cacula o Sink Betweenness para um no

		if (n.sonsPathMap.containsKey(msg.getPath())) {
			// faz a adicao do par <chave, valor>
			// chave = num de caminhos, valor = numero de nodos
			// descendentes com 'aquela' quantidade de caminho

			n.sonsPathMap.put(msg.getPath(),
					n.sonsPathMap.get(msg.getPath()) + 1);
		} else {
			n.sonsPathMap.put(msg.getPath(), 1);
		}

		float tmp = 0.0f;

		for (Entry<Integer, Integer> e : n.sonsPathMap.entrySet()) {
			// faz o calculo do Sbet
			tmp = tmp + (e.getValue() * ((float) n.pathsToSink / e.getKey()));
		}

		n.sBet = tmp;
	}

	@Override
	public void interceptPackEvent(Inbox inbox, PackEvent msg) {
		RoutingNode receiver = (RoutingNode) inbox.getReceiver();

		if ((msg.getNextHop() == 1) && (receiver.ID == 1)) {
			receiver.statistic.countEvReceived(inbox.getArrivingTime());
			receiver.statistic.arriveMessage(msg.idSender, Global.currentTime
					- msg.timeFired);

			return;
		}

		if (msg.getNextHop() == receiver.ID) {
			receiver.setColor(Color.ORANGE);

			if (activeAgregation) {
				funcAgreggation(inbox, msg);
			} else {
				RoutingMessageTimer mt = new RoutingMessageTimer(msg, true);
				mt.startRelative(timeInAgregation, receiver);
			}

		}
	}

}
