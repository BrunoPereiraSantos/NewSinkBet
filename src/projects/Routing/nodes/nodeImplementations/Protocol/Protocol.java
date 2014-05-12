package projects.Routing.nodes.nodeImplementations.Protocol;

import java.awt.Color;

import projects.Routing.nodes.messages.PackEvent;
import projects.Routing.nodes.messages.PackHello;
import projects.Routing.nodes.messages.PackReply;
import projects.Routing.nodes.nodeImplementations.RoutingNode;
import projects.Routing.nodes.timers.RoutingMessageTimer;
import projects.defaultProject.nodes.timers.GenericMessageTimer;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.NackBox;
import sinalgo.runtime.Global;
import sinalgo.runtime.Runtime;
import sinalgo.tools.Tools;

public abstract class Protocol {

	
	private boolean inAgregation = false;
	protected double timeInAgregation = 0.001;
	
	/**
	 * @param timeInAgregation
	 */
	public Protocol(double timeInAgregation) {
		super();
		this.timeInAgregation = timeInAgregation;
	}

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
	public abstract void interceptPackHello(Inbox inbox, PackHello msg);

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
	public abstract void interceptPackReply(Inbox inbox, PackReply msg);

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
	public abstract void interceptPackEvent(Inbox inbox, PackEvent msg);
	public abstract void funcAgreggation();

	/**
	 * Manipula nack messagens. Caso o receptor da mensagem não receba um ack
	 * devido a perda do pacote ele deve reencaminha-la.
	 * 
	 * @param inbox
	 *            informações sobre receptor, emissor etc...
	 * @param msg
	 *            mensagem Event que não recebeu ack a ser tratada
	 */
	public void interceptNack(NackBox nackBox, PackEvent msg) {
		RoutingNode receiver = (RoutingNode) nackBox.getReceiver();
		RoutingNode sender = (RoutingNode) nackBox.getSender();

		if (receiver.ID == sender.nextHop) {

			sender.setColor(Color.RED);
			msg.setNextHop(sender.nextHop);
			RoutingMessageTimer mt = new RoutingMessageTimer(msg, true);
			mt.startRelative(0.5, sender);
			
			sender.statistic.countRelayedMessages();
		}
	}

	public boolean isInAgregation() {
		return inAgregation;
	}

	public void setInAgregation(boolean inAgregation) {
		this.inAgregation = inAgregation;
	}
	
	
}
