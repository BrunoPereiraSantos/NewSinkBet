package projects.Routing.utilities;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import Analises.EnergyModel;
import Analises.InterfaceRequiredMethods;
import projects.Routing.models.energyModel.RoutingEnergyModel;
import projects.Routing.nodes.edges.WeightEdge;
import projects.Routing.nodes.nodeImplementations.RoutingNode;
import sinalgo.nodes.Node;
import sinalgo.tools.Tools;
import sinalgo.tools.logging.Logging;

public class Statistics implements UtilityInterface {
	private boolean isFistEv = true; // variavel para indicar se foi o primeiro
	// evento a chegar (nodo)
	private double timeFistEv; // tempo que o primeiro evento chegou (sink |
	// nodo)
	private double timeLastEv; // tempo que o primeiro evento chegou (sink |
	// nodo)
	private int evReceived; // quantidades de eventos recebidos (sink)

	private int relayedMessages; // quantidade de mensagens retransmitidas
	// (nodo)
	private int sentEvent; // Quantidade de eventos envidados

	private int heardMessagesEv;// quantidade de mensagens escutadas na fase de
	// eventos (nodo)
	private int broadcastEv;// quantidade de broadcast na fase de eventos (node)

	private int heardMessagesTree;// quantidade de mensagens escutadas na fase
	// de contrucao da arvore (nodo)
	private int broadcastTree;// quantidade de broadcast na fase de contrucao da
	// arvore (node)

	static int GlobalrelayedMessages; // quantidade de mensagens retransmitidas
	// (Global)
	static int GlobalDropMessages;// quantidade de mensagens perdidas (Global)
	
	private int aggregateMsg;// quantidade de mensagens agregradas
	
	RoutingEnergyModel energy; // modelo de energia para cada nodo

	Map<Integer, ArrayList<Double>> incomingEvents; // Key=id do nodo
	// que envou a msg.
	// Value = lista de
	// tempo gasto por
	// cada mensagem

	public Statistics(int id) {
		this.timeFistEv = 0.0;
		this.evReceived = 0;
		this.relayedMessages = 0;
		this.heardMessagesEv = 0;
		this.broadcastTree = 0;
		this.broadcastEv = 0;
		GlobalrelayedMessages = 0;
		GlobalDropMessages = 0;
		energy = new RoutingEnergyModel();
		sentEvent = 0;
		aggregateMsg = 0;

		if (id == 1) {
			incomingEvents = new HashMap<Integer, ArrayList<Double>>();
		} else {
			incomingEvents = null;
		}
	}

	public void arriveMessage(int id, double time) {
		if (!incomingEvents.containsKey(id)) {
			ArrayList<Double> l = new ArrayList<Double>();
			l.add(time);
			incomingEvents.put(id, l);
		} else {
			incomingEvents.get(id).add(time);
		}

	}

	public Map<Integer, ArrayList<Double>> getIncomingEvents() {
		return incomingEvents;
	}

	public void setIncomingEvents(Map<Integer, ArrayList<Double>> incomingEvents) {
		this.incomingEvents = incomingEvents;
	}

	public void countBroadcastTree() {
		this.broadcastTree++;
		energy.spendTx_tree();
	}

	public void countBroadcastEv(WeightEdge e) {
		this.broadcastEv++;
		energy.spendTx_Ev(e);
	}

	public int getBroadcastEv() {
		return broadcastEv;
	}

	public void setBroadcastEv(int broadcastEv) {
		this.broadcastEv = broadcastEv;
	}

	public void countHeardMessagesTree() {
		this.heardMessagesTree++;
		energy.spendRx_tree();
	}

	public void countHeardMessagesEv(WeightEdge e) {
		this.heardMessagesEv++;
		energy.spendRx_Ev(e);
	}

	public int getHeardMessagesEv() {
		return heardMessagesEv;
	}

	public void setHeardMessagesEv(int heardMessagesEv) {
		this.heardMessagesEv = heardMessagesEv;
	}

	public static void countGlobalDropMessages() {
		GlobalDropMessages++;
	}

	public static int getGlobalDropMessages() {
		return GlobalDropMessages;
	}

	public static void setGlobalDropMessages(int globalDropMessages) {
		GlobalDropMessages = globalDropMessages;
	}

	public static int getGlobalrelayedMessages() {
		return GlobalrelayedMessages;
	}

	public static void countGlobalrelayedMessages() {
		GlobalrelayedMessages++;
	}

	public static void setGlobalrelayedMessages(int globalrelayedMessages) {
		GlobalrelayedMessages = globalrelayedMessages;
	}

	public void countRelayedMessages() {
		countGlobalrelayedMessages();
		this.relayedMessages++;
	}

	public int getRelayedMessages() {
		return relayedMessages;
	}

	public void setRelayedMessages(int relayedMessages) {
		this.relayedMessages = relayedMessages;
	}

	public void countEvReceived(double time) {
		if (isFistEv) {
			timeFistEv = time;
			isFistEv = false;
		}
		timeLastEv = time;
		this.evReceived++;
	}

	public int getEvReceived() {
		return evReceived;
	}

	public void setEvReceived(int evReceived) {
		this.evReceived = evReceived;
	}

	public double getTimeFistEv() {
		return timeFistEv;
	}

	public void setTimeFistEv(double timeFistEv) {
		this.timeFistEv = timeFistEv;
	}

	public RoutingEnergyModel getEnergy() {
		return energy;
	}

	public void setEnergy(RoutingEnergyModel energy) {
		this.energy = energy;
	}

	public int getHeardMessagesTree() {
		return heardMessagesTree;
	}

	public void setHeardMessagesTree(int heardMessagesTree) {
		this.heardMessagesTree = heardMessagesTree;
	}

	public int getBroadcastTree() {
		return broadcastTree;
	}

	public void setBroadcastTree(int broadcastTree) {
		this.broadcastTree = broadcastTree;
	}

	public int getSentEvent() {
		return sentEvent;
	}

	public void setSentEvent(int sentEvent) {
		this.sentEvent = sentEvent;
	}
	
	public void countSentEvent() {
		this.sentEvent++;
	}

	
	public void countAggregateMsg() {
		this.aggregateMsg++;
	}
	
	public int getAggregateMsg() {
		return aggregateMsg;
	}

	public void setAggregateMsg(int aggregateMsg) {
		this.aggregateMsg = aggregateMsg;
	}

	/**
	 * Faz a media para uma lista de valores
	 * 
	 * @param list
	 * @return
	 */
	public double averageSimple(ArrayList<Double> list) {
		if (list == null || list.isEmpty())
			return 0.0;

		Double sum = 0.0;
		int n = list.size();

		for (int i = 0; i < n; i++)
			sum += list.get(i);

		return sum / n;
	}

	public double sum(ArrayList<Double> list) {
		double sum = 0.0;

		for (Double it : list)
			sum += it.doubleValue();

		return sum;
	}

	/**
	 * Faz a media para nos que estao em um range de saltos do sink
	 * 
	 */
	public double averageNodeHop(int minHop, int maxHop) {
		InterfaceRequiredMethods n;
		ArrayList<Double> list = new ArrayList<Double>();

		for (Entry<Integer, ArrayList<Double>> entry : incomingEvents
				.entrySet()) {
			n = (InterfaceRequiredMethods) Tools.getNodeByID(entry.getKey());

			if (minHop <= n.getHops() && maxHop >= n.getHops())
				list.add(averageSimple(entry.getValue()));
		}

		return averageSimple(list);
	}

	public double averageNodeHop(int Hop) {
		return averageNodeHop(Hop, Hop);
	}

	@Override
	public String toString() {
		ArrayList<Double> list = new ArrayList<Double>();
		String str = "";

		/*
		 * return "StatisticsNodes [timeFistEv=" + String.format("%.3f",
		 * timeFistEv) + ", timeLastEv="+String.format("%.3f",+ timeLastEv) +
		 * ", evRcv=" + evReceived + ", GdropM=" + GlobalDropMessages +
		 * ", GrelayedM=" + GlobalrelayedMessages + ", relayedM=" +
		 * relayedMessages + ", heardEv="+ heardMessagesEv + ", heardTree=" +
		 * heardMessagesTree + ", bEv=" + broadcastEv + ", bTree=" +
		 * broadcastTree + ", eEv=" + String.format("%.7f",
		 * energy.getEnergySpendEv()) + ", eTree=" + String.format("%.7f",
		 * energy.getEnergySpendTree()) + ", eGlobal=" + String.format("%.7f",
		 * EnergyModel.globalEnergySpend) + "]";
		 */
		// str += timeFistEv
		str += timeFistEv + "	" + timeLastEv + "	" + evReceived + "	"
				+ Tools.getGlobalTime()
				// + "	" + GlobalDropMessages
				+ "	" + GlobalrelayedMessages
				// + "	" + relayedMessages
				// + ", heardEv="+ heardMessagesEv
				// + ", heardTree=" + heardMessagesTree
				// + ", bEv=" + broadcastEv
				// + ", bTree=" + broadcastTree
				// + ", eEv=" + String.format("%.7f", energy.getEnergySpendEv())
				// + ", eTree=" + String.format("%.7f",
				// energy.getEnergySpendTree())
				+ "	" + EnergyModel.globalEnergySpend;

		for (Entry<Integer, ArrayList<Double>> entry : incomingEvents
				.entrySet()) {
			// str += " node: " + entry.getKey() + " Average : "
			// + averageSimple(entry.getValue());
			list.add(averageSimple(entry.getValue()));
		}

		str += "	" + averageSimple(list);
		// str += "	" + averageNodeHop(2);
		// str += "	" + averageNodeHop(3);

		return str;
	}

	public String printStatisticsPerNode() {
		String str = "";
		Iterator<Node> it = Tools.getNodeList().iterator();
		RoutingNode n, nodeOne = (RoutingNode) Tools.getNodeByID(1);
		
		
		double totalEnergySpend;
		while (it.hasNext()) {
			n = (RoutingNode) it.next();
			totalEnergySpend = n.statistic.energy.getEnergySpendTree()
					+ n.statistic.energy.getEnergySpendEv();

			str += n.ID
					+ "	"
					+ n.hops
					+ "	"
					+ n.statistic.energy.getEnergySpendTree()
					+ "	"
					+ n.statistic.energy.getEnergySpendEv()
					+ "	"
					+ totalEnergySpend
					+ "	"
					+ n.statistic.broadcastTree
					+ "	"
					+ n.statistic.broadcastEv
					+ "	"
					+ (n.statistic.broadcastTree + n.statistic.broadcastEv)
					+ "	"
					+ n.statistic.heardMessagesTree
					+ "	"
					+ n.statistic.heardMessagesEv
					+ "	"
					+ (n.statistic.heardMessagesTree + n.statistic.heardMessagesEv)
					+ "	" + n.statistic.sentEvent 
					+ "	" + n.statistic.relayedMessages
					+ "	" + n.statistic.aggregateMsg;
			
					if(nodeOne.statistic.incomingEvents.containsKey(n.ID)){
						str += "	" + averageSimple(nodeOne.statistic.incomingEvents.get(n.ID));
					}else{
						str += "	0.000";
					}
					
					
					str += "\n";
		}

		return str;
	}

	@Override
	public void importRead(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void exportWrite(String path) {
		Logging log = Logging.getLogger(path);
		log.logln(printStatisticsPerNode());
	}

	public class LogL extends sinalgo.tools.logging.LogL {
		public static final boolean testLog = false;
		public static final boolean nodeSpeed = true;
	}
}


