package projects.Routing.nodes.nodeImplementations;

import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;
/**
 * Define todo o que deve existir de comum nas 
 * diferentes implementacoes 
 * @author bruno
 *
 */
public abstract class AbstractRoutingNode extends Node {
	
	
	/*
	 * Inicio dos metodos de Radio
	 */
	public abstract void forwardMsg(Message m);
	public abstract void processBroadcastMsg(Message m);
	public abstract void processBroadcastMsgWithNack(Message m);
	/*
	 * Inicio dos metodos de Radio
	 */
}
