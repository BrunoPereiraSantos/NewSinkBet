package Analises;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.corba.se.spi.monitoring.StatisticsAccumulator;

public class StatisticsNodes {

	private boolean isFistEv = true; // variavel para indicar se foi o primeiro
										// evento a chegar (nodo)
	private double timeFistEv; // tempo que o primeiro evento chegou (sink |
								// nodo)
	private double timeLastEv; // tempo que o primeiro evento chegou (sink |
								// nodo)
	private int evReceived; // quantidades de eventos recebidos (sink)
	private int relayedMessages; // quantidade de mensagens retransmitidas
									// (nodo)
	private int heardMessages;// quantidade de mensagens escutadas (nodo)
	private int broadcastEv;// quantidade de broadcast events (node)
	static int GlobalrelayedMessages; // quantidade de mensagens retransmitidas
										// (Global)
	static int GlobalDropMessages;// quantidade de mensagens perdidas (Global)
	
	EnergyModel energy; // modelo de energia para cada nodo
	
	private Map<Integer, ArrayList<Double>> incomingEvents; //Key=id do nodo que envou a msg. Value = lista de tempo gasto por cada mensagem

	public StatisticsNodes(int id) {
		this.timeFistEv = 0.0;
		this.evReceived = 0;
		this.relayedMessages = 0;
		this.heardMessages = 0;
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

	public void countBroadcastEv() {
		this.broadcastEv++;
	}

	public int getBroadcastEv() {
		return broadcastEv;
	}

	public void setBroadcastEv(int broadcastEv) {
		this.broadcastEv = broadcastEv;
	}

	public void countHeardMessages() {
		this.heardMessages++;
	}

	public int getHeardMessages() {
		return heardMessages;
	}

	public void setHeardMessages(int heardMessages) {
		this.heardMessages = heardMessages;
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

	@Override
	public String toString() {
		return "StatisticsNodes [timeFistEv=" + timeFistEv + ", timeLastEv="
				+ timeLastEv + ", evReceived=" + evReceived
				+ ", relayedMessages=" + relayedMessages + ", heardMessages="
				+ heardMessages + ", broadcastEv=" + broadcastEv 
				+ "]";
	}

}
