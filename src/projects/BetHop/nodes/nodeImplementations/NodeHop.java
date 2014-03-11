package projects.BetHop.nodes.nodeImplementations;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import projects.BetHop.nodes.edges.EdgeHop;
import projects.BetHop.nodes.messages.PackHelloHop;
import projects.BetHop.nodes.messages.PackReplyHop;
import projects.BetHop.nodes.timers.TimerFwdReplyHop;
import projects.BetHop.nodes.timers.TimerSendHelloHopSbet;
import projects.BetHop.nodes.timers.TimerStartReplyFloodHop;
import projects.BetHop.nodes.timers.TimerStartSimulation;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;

public class NodeHop extends Node {

	
	// Qual o papel do nodo.
	private NodeRoleHopSbet role;
	
	//private int sonsPath[];
	//cada nodo mantem um map com chave(num de caminhos) e valor(quantos nodos descendentes tem 'num de caminhos'
	private Map<Integer, Integer> sonsPathMap;	
	
	//ID do no sink
	private int sinkID;
	
	//numero de caminhos para o sink
	private int pathsToSink;	
	
	//numero de hops ate o sink
	private int hops;			
	
	//id do prox no usando a metrica numero de hops
	private int nextHop;		
	
	//valor do Sink Betweenness
	private double sBet;		
	
	//Valor do maior sBet entre os vizinhos diretos
	private double neighborMaxSBet;	
	//private double timeRcv;
	
	//Flag para indicar se o nodo ja enviou seu pkt hello
	private boolean sentMyHello;
	
	//Flag para indicar se o nodo ja enviou seu pkt reply
	private boolean sentMyReply;
	
	//Flag para indicar que o nodo recebeu um Ack
	private boolean rcvAck;
	
	//Flag para indicar se o nodo esta em periodo de agregacao
	private boolean inAggregation;

	//Flag para informar se o nodo emitira evento
	private boolean sendEvent;
	
	//Intervalo para aceitar pacotes para agregacao
	private static double intervalAggr;
	
	//array com os filhos (nodo.hops < hops)
	private ArrayList<Integer> sons;	
	
	//array com os vizinhos diretos do nodo.
	private ArrayList<Integer> neighbors;
	
	//variavel para indicar quais nodos emitirao eventos
	private static Set<Integer>  setNodesEv = new HashSet<Integer>();
	
	//variavel para indicar qual o tempo de inicio do primerio evento deste no
	private int timeEvent;
	
	//variavel para gerar numeros aleatorios
	private Random gerador = new Random();

	//Disparadores de flood
	TimerSendHelloHopSbet fhp = new TimerSendHelloHopSbet();
	TimerStartReplyFloodHop srf = new TimerStartReplyFloodHop();
	
	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub
		while (inbox.hasNext()) {
			Message msg = inbox.next();
			
			if (msg instanceof PackHelloHop) {
				PackHelloHop a = (PackHelloHop) msg;
				handlePackHello(a);
			}else if (msg instanceof PackReplyHop) {
				PackReplyHop b = (PackReplyHop) msg;
				handlePackReply(b);
			}
		}
	}

	
	/*=============================================================
	 *                 Manipulando o pacote Hello
	 * ============================================================
	 */
	public void handlePackHello(PackHelloHop message) {
		// no sink nao manipula pacotes hello
		if (message.getSinkID() == this.ID){
			message = null;	//drop message
			return;
		}
	
		// o nodo e vizinho direto do sink (armazena o nextHop como id do sink
		if (message.getSenderID() == message.getSinkID()) {	
			setNextHop(message.getSinkID());
			setNeighborMaxSBet(Double.MAX_VALUE);
		}

		// nodo acaba de ser descoberto ou acabou de encontrar um caminho mais curto
		if ((message.getHops() + 1 < getHops()) || (getHops() == 0)) {	
			this.setColor(Color.GREEN);
			setHops(message.getHops() + 1);
			message.setHops(getHops());
			setPathsToSink(message.getPath());
			setSinkID(message.getSinkID());
			setNextHop(message.getSenderID());
			
			
			if(!neighbors.isEmpty()){
				neighbors.removeAll(neighbors);
			}
			
			//adiciona os vizinhos mais proximos do sink que sao rotas
			if(!neighbors.contains(message.getSenderID())){
				neighbors.add(message.getSenderID());
			}
		}

		//existe mais de um caminho deste nodo ate o sink com a mesma quantidade de hops
		if ((message.getHops() + 1 == getHops())) {	
			this.setColor(Color.MAGENTA);
			setPathsToSink(getPathsToSink() + message.getPath());
			message.setHops(getHops());
			
			//System.out.println(message);
			fhp.updateTimer(1.0, this, fhp.getFireTime());
			
			//adiciona os vizinhos mais proximos do sink que sao rotas
			if(!neighbors.contains(message.getSenderID())){
				neighbors.add(message.getSenderID());
			}
		}
				
		// eh a primeira vez que o nodo recebe um hello
		// ele deve encaminhar um pacote com seus dados
		// Essas flags ajudam para nao sobrecarregar a memoria com eventos 
		// isto e, mandar mensagens com informa�oes desatualizadas
		if(!isSentMyHello()){
			//FwdPack fhp = new FwdPack(message, this.ID);
			//fhp.setPkt(new PackHelloHopSbet(hops, pathsToSink, this.ID, 1));
			fhp.startRelative(getHops()+1, this);	//Continuara o encaminhamento do pacote hello
			setSentMyHello(true);
			
			// Dispara um timer para enviar um pacote de borda
			// para calculo do sbet
			// nodos do tipo border e relay devem enviar tal pacote
			if(!isSentMyReply()){
				
				srf.startRelative((double) waitingTime(), this);
				//srf.startRelative(getHops()*2, this);
				//srf.startRelative(getHops()*3+200, this);
				setSentMyReply(true);
			}
		}
		
		message = null;	//drop message
	}
	
	public void fwdHelloPack() {
		// Encaminha um pacote com as informacoes atualizadas
		broadcast(new PackHelloHop(hops, pathsToSink, this.ID, sinkID)); 
		setSentMyHello(true);
		
	}
	
	public void sendHelloFlooding() {
		//Somente o no que inicia o flood (neste caso o no 1) executa essa chamada
		broadcast(new PackHelloHop(hops, 1, this.ID, this.ID));
		setSentMyHello(true);
		
	}

	public void sendReplyFlooding(){ //Dispara o flooding das respostas dos nodos com papel BORDER e RELAY
		if ((getRole() == NodeRoleHopSbet.BORDER) || 
			(getRole() == NodeRoleHopSbet.RELAY)) {
			this.setColor(Color.GRAY);
			//Pack pkt = new Pack(this.hops, this.pathsToSink, this.ID, 1, this.sBet, TypeMessage.BORDER);
			
			PackReplyHop pkt = new PackReplyHop(hops, pathsToSink, this.ID, sinkID, nextHop, neighbors, sBet);
			broadcast(pkt);
			
			setSentMyReply(true);
			
		}
	}
	
	public double waitingTime () {//atraso para enviar o pacote [referencia artigo do Eduardo]
		double waitTime = 0.0;
		//waitTime = 1 / (Math.exp(this.hops) * Math.pow(10, -20));
		//waitTime = 1 / (this.hops * (Math.pow(5, -3.3)));
		waitTime = Math.pow(5, 3.3) / this.hops;
		//System.out.println(waitTime);
		return waitTime+100; //o flood somente inicia apos o tempo 100
	}
	
	
	/*=============================================================
	 *                 Manipulando o pacote Reply
	 * ============================================================
	 */
	public void handlePackReply(PackReplyHop message) {
		
		// o sink nao deve manipular pacotes do tipo Relay
		if(this.ID == message.getSinkID()){
			return;
		}
		
		
		// o border e fonte nao devem manipular pacotes do tipo Relay
		// necessaria verificacao de que o no ja recebeu menssagem 
		// de um determinado descendente, isso e feito para evitar mensagens duplicadas.
		// Se o nodo ainda nao recebeu menssagem de um descendente 
		// ele deve 'processar' (recalcular o sbet) e propagar o pacote desse descendente
		// para que os outros nodos tambem calculem seu sBet
		if (!sons.contains(message.getSenderID()) && 
			(message.getHops() > getHops())		  && 
			(message.getSendToNodes().contains(this.ID))
			/*(message.getSendTo() == this.ID)*/) { 
			
			sons.add(message.getSenderID());

			this.setColor(Color.CYAN);
			setRole(NodeRoleHopSbet.RELAY);
			
			processBet(message);
			
			message.setSendTo(getNextHop());
			message.setSendToNodes(neighbors);
			
			TimerFwdReplyHop fwdReply = new TimerFwdReplyHop(message);
			
			fwdReply.startRelative(0.0000000000001, this);
					
		}
		
		// Uma mensagem foi recebida pelos ancestrais logo devo analisar se e o meu nextHop
		if (message.getHops() < getHops()){	
			if (message.getsBet() > getNeighborMaxSBet()) {
				//System.out.println(message);
				//System.out.println(this.ID+" Entrei e mudei meu nhop");
				setNeighborMaxSBet(message.getsBet());
				setNextHop(message.getSenderID());
				
			}
			message = null;
		}
		
		
	}
	
	public void processBet(PackReplyHop message) {	// faz o cal. do Sbet
		// faz a adicao do par <chave, valor>
		// chave = num de caminhos, valor = numero de nodos
		// descendentes com 'aquela' quantidade de caminho
		if(sonsPathMap.containsKey(message.getPath())){	
			sonsPathMap.put(message.getPath(), sonsPathMap.get(message.getPath()) + 1);
		}else{
			sonsPathMap.put(message.getPath(), 1);
		}
		
		double tmp = 0.0;
		
		for(Entry<Integer, Integer> e : sonsPathMap.entrySet())	// faz o calculo do Sbet
			tmp = tmp + (e.getValue() * ((double) this.pathsToSink / e.getKey()));
		
		setsBet(tmp);
	}
	
	public void fwdReply(PackReplyHop pkt){//Dispara um broadcast com o pacote PackReplyHopSbet | metodo utilizado pelos timers
		broadcast(pkt);
	}
	
	
	@Override
	public void init() {		
		setRole(NodeRoleHopSbet.BORDER);
		setPathsToSink(0);
		setHops(0);
		setsBet(0.0);
		setNextHop(Integer.MAX_VALUE);
		setNeighborMaxSBet(Double.MIN_VALUE);
		setSentMyReply(false);
		setSentMyHello(false);
		setInAggregation(false);
		setSendEvent(false);
		
		setSonsPathMap(new HashMap<Integer, Integer>());
		setSons(new ArrayList<Integer>());
		setNeighbors(new ArrayList<Integer>());

		if (this.ID == 1) {
			this.setColor(Color.BLUE);
			setRole(NodeRoleHopSbet.SINK);
			
			(new TimerStartSimulation()).startRelative(1, this);
			
			/*SendPackHelloHopSbet pkt = new SendPackHelloHopSbet(hops, 1, this.ID, this.ID);
			pkt.startRelative(2, this);*/
			/*Pack p = new Pack(this.hops, this.pathsToSink, this.ID, this.ID, this.sBet, TypeMessage.HELLO);
			
			PackTimer pTimer = new PackTimer(p);
			pTimer.startRelative(2, this);*/
		}
	}
	
	
	@Override
	public void preStep() {
		// TODO Auto-generated method stub

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
	
	public String toString() {

		String str = "Dados do no ";
		str = str.concat("" + this.ID + "\n");
		str = str.concat("role = " + role + "\n");
		str = str.concat("Path = " + pathsToSink + "\n");
		str = str.concat("Hops = " + hops + "\n");
		str = str.concat("Sbet = " + sBet + "\n");
		str = str.concat("nextHop = " + nextHop + "\n");
		str = str.concat("maxSbet = " + neighborMaxSBet + "\n");
		str = str.concat("SonspathMap -> "+sonsPathMap+"\n");
		str = str.concat("sons -> "+sons+"\n");
		str = str.concat("neighbors -> "+neighbors+"\n");
		str = str.concat("\n");
		
		return str;
	}
	/********************************************************************************
	 *							Gets e Sets
	 *********************************************************************************/
	public NodeRoleHopSbet getRole() {return role;}
	public void setRole(NodeRoleHopSbet role) {this.role = role;}
	public Map<Integer, Integer> getSonsPathMap() {return sonsPathMap;}
	public void setSonsPathMap(Map<Integer, Integer> sonsPathMap) {this.sonsPathMap = sonsPathMap;}
	public int getPathsToSink() {return pathsToSink;}
	public void setPathsToSink(int pathsToSink) {this.pathsToSink = pathsToSink;}
	public int getHops() {return hops;}
	public void setHops(int hops) {this.hops = hops;}
	public int getNextHop() {return nextHop;}
	public void setNextHop(int nextHop) {this.nextHop = nextHop;}
	public double getsBet() {return sBet;}
	public void setsBet(double sBet) {this.sBet = sBet;}
	public double getNeighborMaxSBet() {return neighborMaxSBet;}
	public void setNeighborMaxSBet(double neighborMaxSBet) {this.neighborMaxSBet = neighborMaxSBet;}

	public double getEtxToMeFromNode(int nodeID) {
		Iterator<Edge> it2 = this.outgoingConnections.iterator();
		EdgeHop e;
		while (it2.hasNext()) {
			e = (EdgeHop) it2.next();
			if (e.endNode.ID == nodeID){
				e = (EdgeHop) e.getOppositeEdge();
				return e.getEtx();
			}
		}
		return 0.0;
	}

	public double getEtxToNode(int nodeID) {
		Iterator<Edge> it2 = this.outgoingConnections.iterator();
		EdgeHop e;
		while (it2.hasNext()) {
			e = (EdgeHop) it2.next();
			if (e.endNode.ID == nodeID)
				return e.getEtx();
		}
		return 0.0;
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

	public ArrayList<Integer> getSons() {return sons;}
	public void setSons(ArrayList<Integer> sons) {this.sons = sons;}
	public ArrayList<Integer> getNeighbors() {return neighbors;}
	public void setNeighbors(ArrayList<Integer> neighbors) {this.neighbors = neighbors;}
	public boolean isInAggregation() {
		return inAggregation;
	}

	public void setInAggregation(boolean inAggregation) {
		this.inAggregation = inAggregation;
	}

	public Random getGerador() {
		return gerador;
	}

	public void setGerador(Random gerador) {
		this.gerador = gerador;
	}

	public TimerSendHelloHopSbet getFhp() {
		return fhp;
	}

	public void setFhp(TimerSendHelloHopSbet fhp) {
		this.fhp = fhp;
	}

	public TimerStartReplyFloodHop getSrf() {
		return srf;
	}

	public void setSrf(TimerStartReplyFloodHop srf) {
		this.srf = srf;
	}

	public int getSinkID() {
		return sinkID;
	}

	public void setSinkID(int sinkID) {
		this.sinkID = sinkID;
	}

	public static double getIntervalAggr() {
		return intervalAggr;
	}

	public boolean isRcvAck() {
		return rcvAck;
	}


	public void setRcvAck(boolean rcvAck) {
		this.rcvAck = rcvAck;
	}

	public boolean isSendEvent() {
		return sendEvent;
	}


	public void setSendEvent(boolean sendEvent) {
		this.sendEvent = sendEvent;
	}


	public int getTimeEvent() {
		return timeEvent;
	}


	public void setTimeEvent(int timeEvent) {
		this.timeEvent = timeEvent;
	}
}
