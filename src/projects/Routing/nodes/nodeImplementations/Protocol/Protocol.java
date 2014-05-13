package projects.Routing.nodes.nodeImplementations.Protocol;

import java.awt.Color;

import projects.Routing.nodes.messages.PackEvent;
import projects.Routing.nodes.messages.PackHello;
import projects.Routing.nodes.messages.PackReply;
import projects.Routing.nodes.nodeImplementations.RoutingNode;
import projects.Routing.nodes.timers.RoutingMessageTimer;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.NackBox;

public abstract class Protocol {

	protected double timeInAgregation = 0.001;
	protected boolean activeAgregation = false;
	private boolean inAgregation = false;

	/**
	 * @param timeInAgregation
	 */
	public Protocol(boolean activeAgregation, double timeInAgregation) {
		super();
		this.timeInAgregation = timeInAgregation;
		this.activeAgregation = activeAgregation;
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

	/**
	 * Funcao de agregação que recebe uma ou mais mensagens e envia somente uma
	 * mensagem. O tempo de espera de cada mensagem é definido por
	 * timeInAgregation passado pelo arquivo conf.xml. Por default o nó espera
	 * 0.001 para sair do modo de agregacao
	 * 
	 * @param inbox
	 *            informações sobre receptor, emissor etc.
	 * @param msg
	 *            mensagem que será agregada.
	 */
	public void funcAgreggation(Inbox inbox, PackEvent m) {
		RoutingNode receiver = (RoutingNode) inbox.getReceiver();

		if (!isInAgregation()) {
			System.out.printf("No[%d] em agregação...\n", receiver.ID);
			m.setNextHop(receiver.nextHop);
			RoutingMessageTimer mt = new RoutingMessageTimer(m, true);
			mt.startRelative(timeInAgregation, receiver);
			setInAgregation(true);
		} else {
			receiver.statistic.countAggregateMsg();
		}
	}

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
