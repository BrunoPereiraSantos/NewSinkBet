package projects.Routing.nodes.nodeImplementations.Protocol;

import projects.Routing.nodes.messages.PackHello;
import projects.Routing.nodes.messages.PackReply;
import sinalgo.nodes.messages.Inbox;

public interface ProtocolInterface {
	
	/**
	 * Ao interceptar uma mensagem do tipo hello o protocolo deve seguir 
	 * algum procedimento de descoberta
	 * @param inbox informações sobre receptor, emissor etc...
	 * @param msg mensagem hello a ser tratada
	 */
	public void interceptPackHello(Inbox inbox, PackHello msg);
	
	/**
	 * Ao interceptar uma mensagem do tipo Reply o protocolo deve seguir 
	 * algum procedimento de descoberta
	 * @param inbox informações sobre receptor, emissor etc...
	 * @param msg mensagem Reply a ser tratada
	 */
	public void interceptPackReply(Inbox inbox, PackReply msg);
}	
