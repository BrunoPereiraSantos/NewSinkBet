package projects.Routing.nodes.nodeImplementations.Protocol;

import projects.Routing.nodes.messages.PackEvent;
import projects.Routing.nodes.messages.PackHello;
import projects.Routing.nodes.messages.PackReply;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.NackBox;

public interface ProtocolInterface {

	/**
	 * Ao interceptar uma mensagem do tipo Hello o protocolo deve seguir os
	 * procedimentos para manipular tal pacote, i.e, são verificados: Se o nó
	 * foi descoberto ou acabou de encontrar uma rota com melhor qualidade de
	 * métrica
	 * 
	 * Se o nó possui mais de um caminho com a mesma qualidade de métrica
	 * 
	 * Depois de efetuar os procedimentos acima. O nó encaminha o pacote hello
	 * com suas informações atualizadas
	 * 
	 * Também é disparado um temporizador para a segunda inundação i.e, os
	 * pacotes reply.
	 * 
	 * @param inbox
	 *            informações sobre receptor, emissor etc...
	 * @param msg
	 *            mensagem hello a ser tratada
	 */
	public void interceptPackHello(Inbox inbox, PackHello msg);

	/**
	 * Ao receber um pacote Reply o nó deve: Verificar se o pacote vem de um
	 * descendete ou anscestral Caso descendente o nó deve atualizar sua
	 * centralidade caso ancestral o nó deve verificar se o ancestral é possui
	 * mais centralidade do que o nodo mais central visto até o momento.
	 * 
	 * 
	 * @param inbox
	 *            informações sobre receptor, emissor etc...
	 * @param msg
	 *            mensagem Reply a ser tratada
	 */
	public void interceptPackReply(Inbox inbox, PackReply msg);

	/**
	 * Ao interceptar uma mensagem do tipo Event o protocolo deve seguir os
	 * procedimentos para manipular tal pacote, i.e, se o pacote for para o nó
	 * que recebeu, ele não faz nada (root não faz nada), caso contrário o nó
	 * encaminha o evento
	 * 
	 * @param inbox
	 *            informações sobre receptor, emissor etc...
	 * @param msg
	 *            mensagem Event a ser tratada
	 */
	public void interceptPackEvent(Inbox inbox, PackEvent msg);

	/**
	 * Manipula nack messagens. Caso o receptor da mensagem não receba um ack
	 * devido a perda do pacote ele deve reencaminha-la.
	 * 
	 * @param inbox
	 *            informações sobre receptor, emissor etc...
	 * @param msg
	 *            mensagem Event que não recebeu ack a ser tratada
	 */
	public void interceptNack(NackBox inbox, PackEvent msg);

}
