package projects.Routing.nodes.nodeImplementations.Protocol;

import projects.Routing.nodes.messages.PackEvent;
import projects.Routing.nodes.messages.PackHello;
import projects.Routing.nodes.messages.PackReply;
import sinalgo.nodes.messages.Inbox;

public abstract class Protocol implements ProtocolInterface{

	/**
	 * Ao interceptar uma mensagem do tipo Event o protocolo deve seguir 
	 * algum procedimento de descoberta
	 * @param inbox informações sobre receptor, emissor etc...
	 * @param msg mensagem Event a ser tratada
	 */
	public void interceptPackEvent(Inbox inbox, PackEvent msg){}

	@Override
	public void interceptPackHello(Inbox inbox, PackHello msg) {}

	@Override
	public void interceptPackReply(Inbox inbox, PackReply msg) {}

	
}
