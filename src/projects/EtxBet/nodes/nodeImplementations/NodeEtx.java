package projects.EtxBet.nodes.nodeImplementations;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import projects.EtxBet.nodes.edges.EdgeEtx;
import projects.EtxBet.nodes.messages.PackHelloEtx;
import projects.EtxBet.nodes.messages.PackReplyEtx;
import projects.EtxBet.nodes.timers.TimerFwdReplyEtx;
import projects.EtxBet.nodes.timers.TimerSendHelloEtx;
import projects.EtxBet.nodes.timers.TimerStartReplyFlood;
import projects.EtxBet.nodes.timers.TimerStartSimulationEtx;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;

public class NodeEtx extends Node {
	// Qual o papel do nodo
	private NodeRoleEtx role;
		
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
	
	//Flag para indicar se o nodo ja enviou seu pkt border
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
	
	//ID da mensagem que deve ser atrasada devido a varios caminhos
	private int fwdMsgID;
	
	//variavel para gerar numeros aleatorios
	private Random gerador = new Random();
	
	// etx acumulado do caminho ate o nodo
	// Obs: note que o valor do etx acumulado
		// sao sempre calculados com as arestas que
		// apontam para o caminho ate o sink
		// ex: 2 ~[3.0]> 1 e 3 ~[2.0]> 2
		// entao etxPath (etx acumulado) do nodo 3 = 5
	private double etxPath;
	
	//Disparadores de flood
	TimerSendHelloEtx fhp = new TimerSendHelloEtx();
	TimerStartReplyFlood srf = new TimerStartReplyFlood();
	
	@Override
	public void handleMessages(Inbox inbox) {
		while (inbox.hasNext()) {
			Message msg = inbox.next();
			if (msg instanceof PackHelloEtx) {
				
				PackHelloEtx a = (PackHelloEtx) msg;
				handlePackHello(inbox.getSender(), inbox.getReceiver(), a);
				
			}else if (msg instanceof PackReplyEtx) {
				
				PackReplyEtx b = (PackReplyEtx) msg;
				handlePackReply(inbox.getSender(), inbox.getReceiver(), b);
				
			}
		}

	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*=============================================================
	 *                 Manipulando o pacote Hello
	 * ============================================================
	 */
	public void handlePackHello(Node sender, Node receiver, PackHelloEtx message) {
		// no sink nao manipula pacotes hello
		if (message.getSinkID() == this.ID){
			message = null;	//drop message
			return;
		}
		double etxToNode = getEtxToNode(sender.ID);
		
		// o nodo e vizinho direto do sink (armazena o nextHop como id do sink
		if (sender.ID == message.getSinkID()) {	
			setNextHop(message.getSinkID());
			setNeighborMaxSBet(Double.MAX_VALUE);
		}

		// nodo acaba de ser descoberto ou acabou de encontrar um caminho mais curto
		if ((message.getETX() + etxToNode) < getEtxPath() || (getEtxPath() == Double.MAX_VALUE)) {	
			this.setColor(Color.GREEN);
			setHops(message.getHops() + 1);
			message.setHops(getHops());
			setPathsToSink(message.getPath());
			setSinkID(message.getSinkID());
			setNextHop(sender.ID);
			setEtxPath(message.getETX() + etxToNode);
			message.setETX(getEtxPath()); // e mesmo necessario fazer isso aqui?
			//setSentMyHello(false);
			
			if(!neighbors.isEmpty()){
				neighbors.removeAll(neighbors);
			}
			
			//adiciona os vizinhos mais proximos do sink que sao rotas
			if(!neighbors.contains(sender.ID)){
				neighbors.add(sender.ID);
			}
		}

		// existe mais de um caminho deste nodo ate o sink com a mesmo ETX acumulado
		if ((message.getETX() + etxToNode) == getEtxPath()) { 
			this.setColor(Color.MAGENTA);
			setPathsToSink(getPathsToSink() + message.getPath());
			//setPathsToSink(getPathsToSink() + 1);
			//fhp.updateTimer(2.0);
			
			fhp.updateTimer(2.0, this);
			//setSentMyHello(false);
			
			//adiciona os vizinhos mais proximos do sink que sao rotas
			if(!neighbors.contains(sender.ID)){
				neighbors.add(sender.ID);
			}
		}
		
		
		
		// eh a primeira vez que o nodo recebe um hello
		// ele deve encaminhar um pacote com seus dados
		// Essas flags ajudam para nao sobrecarregar a memoria com eventos 
		// isto e, mandar mensagens com informa�oes desatualizadas
		if(!isSentMyHello()){
			//FwdPack fhp = new FwdPack(message, this.ID);
			//fhp.setPkt(new PackHelloEtxBet(hops, pathsToSink, this.ID, 1));
			/*message.setHops(getHops());
			message.setETX(getEtxPath());
			message.setSenderID(this.ID);
			message.setSinkID(sinkID);
			message.setPath(getPathsToSink());
			fhp.setPkt(message);*/
			fhp.startRelative(getHops()+1, this);	//Continuara o encaminhamento do pacote hello
			setSentMyHello(true);
			
			// Dispara um timer para enviar um pacote de borda
			// para calculo do sbet
			// nodos do tipo border e relay devem enviar tal pacote
			if(!isSentMyReply()){
				
				srf.startAbsolute((double) waitingTime(), this);
				//srf.startRelative(getHops()*2, this);
				//srf.startRelative(getHops()*3+200, this);
				setSentMyReply(true);
			}
		}
		
		/*System.out.println("======inicio===== node "+this.ID);
		TimerCollection tc = getTimers();
		System.out.println("size  "+tc.size());
		Iterator<Timer> it = tc.iterator();
		
		while(it.hasNext()){
			Timer tm = it.next();
			if ( tm instanceof SendPackHelloEtxBet) {
				SendPackHelloEtxBet a = (SendPackHelloEtxBet) tm;
				System.out.println(tm.getFireTime());
			}
		}
		System.out.println("======fim=====\n\n\n");*/
		
		message = null;	//drop message
	}
	
	public void fwdHelloPack() {
		// Encaminha um pacote com as informacoes atualizadas
		broadcast(new PackHelloEtx(hops, pathsToSink, this.ID, sinkID, etxPath)); 
		setSentMyHello(true);
	}
	
	public void sendHelloFlooding() {
		
		//Somente o no que inicia o flood (neste caso o no 1) executa essa chamada
		//System.out.println(pkt);
		broadcast(new PackHelloEtx(hops, 1, this.ID, this.ID, 0.0));
		setSentMyHello(true);
	}

	public void sendReplyFlooding(){ //Dispara o flooding das respostas dos nodos com papel BORDER e RELAY
		if ((getRole() == NodeRoleEtx.BORDER) || 
			(getRole() == NodeRoleEtx.RELAY)) {
			this.setColor(Color.GRAY);
			//Pack pkt = new Pack(this.hops, this.pathsToSink, this.ID, 1, this.sBet, TypeMessage.BORDER);
			
			PackReplyEtx pkt = new PackReplyEtx(hops, pathsToSink, this.ID, sinkID, nextHop, neighbors, etxPath, sBet, this.ID);
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
	public void handlePackReply(Node sender, Node receiver, PackReplyEtx message) {
		
		// o sink nao deve manipular pacotes do tipo Relay
		if(this.ID == message.getSinkID()){
			return;
		}
		
		//ETX para o ultimo no que enviou a msg 
		double etxToNode = getEtxToNode(sender.ID);
		
		// necessaria verificacao de que o no ja recebeu menssagem de um
		// determinado descendente, isso e feito para evitar mensagens
		// duplicadas.
		// Se o nodo ainda nao recebeu menssagem de um descendente ele
		// deve 'processar' (recalcular o sbet) e propagar o pacote
		// desse descendente para que os outros nodos facam o mesmo
		if (!sons.contains(message.getSenderID()) && 
			(message.getETX() > getEtxPath())	  && 
			(message.getSendToNodes().contains(this.ID))
			/*(message.getSendTo() == this.ID)*/) { 
			
			sons.add(message.getSenderID());

			// se o nodo faz essa operacao ele eh relay
			this.setColor(Color.CYAN);
			setRole(NodeRoleEtx.RELAY);
			
			processBet(message);
			
			message.setSendTo(getNextHop());
			message.setFwdID(this.ID);
			message.setSendToNodes(neighbors);
			
			TimerFwdReplyEtx fwdReply = new TimerFwdReplyEtx(message);
			fwdReply.startRelative(1, this);
					
		}
		
		// Uma mensagem foi recebida pelos ancestrais logo devo analisar se e o meu nextHop
		if (message.getETX() + etxToNode <= getEtxPath()){
			if(this.ID == 6){
				System.out.println(message);
			}
			if (message.getsBet() > getNeighborMaxSBet()) {
				//System.out.println("Antes\n"+this);
				setNeighborMaxSBet(message.getsBet());
				setNextHop(message.getSenderID());
				//System.out.println("Depois\n"+this+"\n\n");
			}
			message = null;
		}
		
		
	}
	
	public void processBet(PackReplyEtx message) {	// faz o cal. do Sbet
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
	
	public void fwdReply(PackReplyEtx pkt){//Dispara um broadcast com o pacote PackReplyEtxBet | metodo utilizado pelos timers
		broadcast(pkt);
	}
		
	@Override
	public void preStep() {
		
	}

	
	
	@Override
	public void init() {
		setRole(NodeRoleEtx.BORDER);
		setPathsToSink(0);
		setHops(0);
		setsBet(0.0);
		setNextHop(Integer.MAX_VALUE);
		setNeighborMaxSBet(Double.MIN_VALUE);
		setEtxPath(Double.MAX_VALUE);
		setRcvAck(false);
		setSentMyReply(false);
		setSentMyHello(false);
		setInAggregation(false);
		setSendEvent(false);
		
		setSonsPathMap(new HashMap<Integer, Integer>());
		setSons(new ArrayList<Integer>());
		setNeighbors(new ArrayList<Integer>());

		if (this.ID == 1) {
			this.setColor(Color.BLUE);
			setRole(NodeRoleEtx.SINK);
			
			(new TimerStartSimulationEtx()).startRelative(3, this);
			
			/*SendPackHelloEtxBet pkt = new SendPackHelloEtxBet(hops, 1, this.ID, this.ID);
			pkt.startRelative(2, this);*/
			/*Pack p = new Pack(this.hops, this.pathsToSink, this.ID, this.ID, this.sBet, TypeMessage.HELLO);
			
			PackTimer pTimer = new PackTimer(p);
			pTimer.startRelative(2, this);*/
			
			
			//readConfigurationParameters();
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
		str = str.concat("etxPath "+etxPath+"\n");
		str = str.concat("\n");
		
		return str;
	}
	/********************************************************************************
	 *							Gets e Sets
	 *********************************************************************************/
	public NodeRoleEtx getRole() {return role;}
	public void setRole(NodeRoleEtx role) {this.role = role;}
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



	public static Set<Integer> getSetNodesEv() {
		return setNodesEv;
	}

	public Random getGerador() {
		return gerador;
	}

	public void setGerador(Random gerador) {
		this.gerador = gerador;
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
	
	public double getEtxPath() {
		return etxPath;
	}

	public void setEtxPath(double etxPath) {
		this.etxPath = etxPath;
	}

	public double getEtxToNode(int nodeID) {
		Iterator<Edge> it2 = this.outgoingConnections.iterator();
		EdgeEtx e;
		while (it2.hasNext()) {
			e = (EdgeEtx) it2.next();
			if (e.endNode.ID == nodeID)
				return e.getEtx();
		}
		return 0.0;
	}

	public double getEtxToMeFromNode(int nodeID) {
		Iterator<Edge> it2 = this.outgoingConnections.iterator();
		EdgeEtx e;
		while (it2.hasNext()) {
			e = (EdgeEtx) it2.next();
			if (e.endNode.ID == nodeID){
				e = (EdgeEtx) e.getOppositeEdge();
				return e.getEtx();
			}
		}
		return 0.0;
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
