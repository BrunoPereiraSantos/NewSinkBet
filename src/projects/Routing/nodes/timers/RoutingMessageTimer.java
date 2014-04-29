package projects.Routing.nodes.timers;

import projects.Routing.nodes.nodeImplementations.RoutingNode;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;
import sinalgo.nodes.timers.Timer;

public class RoutingMessageTimer extends Timer {
	private Node receiver; //caso unicast ele deve ser referenciado
	private Message m;  // mensagem a ser enviada
	private boolean process; // caso verdadosomente encaminhar sem alteracoes
	
	
	
	/**
	 * @param receiver caso unicast ele deve ser referenciado
	 * @param msg mensagem a ser enviada
	 * @param processcaso verdadeiro ele atualiza os campos, 
	 * caso contrario sem alteracoes somente encaminha a mensagem
	 */
	public RoutingMessageTimer(Node receiver, Message msg, boolean process) {
		super();
		this.receiver = receiver;
		this.m = msg;
		this.process = process;
	}


	/**
	 * @param msg mensagem a ser enviada
	 */
	public RoutingMessageTimer(Message msg, boolean process) {
		super();
		this.receiver = null;
		this.m = msg;
		this.process = process;
	}
	

	@Override
	public void fire() {
		if(receiver != null){
			//NAO IMPLEMENTADO
			return;
		}else if(process){
			((RoutingNode) this.node).processBroadcastMsg(m);
		}else{
			((RoutingNode) this.node).forwardMsg(m);
		}

	}


	public Message getM() {
		return m;
	}


	public void setM(Message m) {
		this.m = m;
	}
	
	

}
