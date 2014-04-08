package Analises;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import projects.defaultProject.nodes.edges.GenericWeightedEdge;

import com.sun.corba.se.spi.monitoring.StatisticsAccumulator;

public class StatisticsNode {

	private boolean isFistEv = true; // variavel para indicar se foi o primeiro
										// evento a chegar (nodo)
	private double timeFistEv; // tempo que o primeiro evento chegou (sink |
								// nodo)
	private double timeLastEv; // tempo que o primeiro evento chegou (sink |
								// nodo)
	private int evReceived; // quantidades de eventos recebidos (sink)
	
	private int relayedMessages; // quantidade de mensagens retransmitidas
									// (nodo)
	
	
	private int heardMessagesEv;// quantidade de mensagens escutadas na fase de eventos (nodo)
	private int broadcastEv;// quantidade de broadcast na fase de eventos (node)
	
	private int heardMessagesTree;// quantidade de mensagens escutadas na fase de contrucao da arvore (nodo)
	private int broadcastTree;// quantidade de broadcast na fase de contrucao da arvore (node)
	
	static int GlobalrelayedMessages; // quantidade de mensagens retransmitidas
										// (Global)
	static int GlobalDropMessages;// quantidade de mensagens perdidas (Global)
	
	EnergyModel energy; // modelo de energia para cada nodo
	
	private Map<Integer, ArrayList<Double>> incomingEvents; //Key=id do nodo que envou a msg. Value = lista de tempo gasto por cada mensagem

	public StatisticsNode(int id) {
		this.timeFistEv = 0.0;
		this.evReceived = 0;
		this.relayedMessages = 0;
		this.heardMessagesEv = 0;
		this.broadcastEv = 0;
		GlobalrelayedMessages = 0;
		GlobalDropMessages = 0;
		energy = new EnergyModel();
		
		if (id == 1) {
			incomingEvents = new HashMap<Integer, ArrayList<Double>>();
		}else{
			incomingEvents = null;
		}
	}

	public void IncomingEvents(int id, double time) {
		if (!incomingEvents.containsKey(id)) {
			ArrayList<Double> l = new ArrayList<Double>();
			l.add(time);
			incomingEvents.put(id, l);
		}else{
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
	
	public void countBroadcastEv(GenericWeightedEdge e) {
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
	
	public void countHeardMessagesEv(GenericWeightedEdge e) {
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

	public EnergyModel getEnergy() {
		return energy;
	}

	public void setEnergy(EnergyModel energy) {
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
	
	public double average(ArrayList<Double> list){
		if(list == null || list.isEmpty()) return 0.0;
		
		Double sum = 0.0;
		int n = list.size();
		
		for(int i = 0; i < n; i++) sum += list.get(i);
		
		return sum / n;
	}

	@Override
	public String toString() {
		String str = "";
		
		/*return "StatisticsNodes [timeFistEv=" +  String.format("%.3f", timeFistEv) 
				+ ", timeLastEv="+String.format("%.3f",+ timeLastEv)
				+ ", evRcv=" + evReceived 
				+ ", GdropM=" + GlobalDropMessages
				+ ", GrelayedM=" + GlobalrelayedMessages 
				+ ", relayedM=" + relayedMessages 
				+ ", heardEv="+ heardMessagesEv
				+ ", heardTree=" + heardMessagesTree
				+ ", bEv=" + broadcastEv 
				+ ", bTree=" + broadcastTree
				+ ", eEv=" + String.format("%.7f", energy.getEnergySpendEv())
				+ ", eTree=" + String.format("%.7f", energy.getEnergySpendTree())
				+ ", eGlobal=" + String.format("%.7f", EnergyModel.globalEnergySpend)
				+ "]";*/
		//str += timeFistEv
		str += timeFistEv
			+ "	" + timeLastEv
			+ "	" + evReceived 
			//+ "	" + GlobalDropMessages
			+ "	" + GlobalrelayedMessages 
			//+ "	" + relayedMessages 
			//+ ", heardEv="+ heardMessagesEv
			//+ ", heardTree=" + heardMessagesTree
			//+ ", bEv=" + broadcastEv 
			//+ ", bTree=" + broadcastTree
			//+ ", eEv=" + String.format("%.7f", energy.getEnergySpendEv())
			//+ ", eTree=" + String.format("%.7f", energy.getEnergySpendTree())
			+ "	" + EnergyModel.globalEnergySpend;
		for (Entry<Integer, ArrayList<Double>> entry : incomingEvents.entrySet()) {
			str += " node: " + entry.getKey() + " Average : "
				+ average(entry.getValue());
		}
		
		return 	str;
	}

}
