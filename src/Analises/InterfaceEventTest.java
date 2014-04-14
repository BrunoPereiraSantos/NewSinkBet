package Analises;

import projects.defaultProject.nodes.messages.EventMessage;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;

public interface InterfaceEventTest {
	/**
	 * É necessario implementar para saber se os nodos iniciam com o modelo de 
	 * confiabilidade do tipo Reliability
	 * @throws WrongConfigurationException
	 */
	void checkRequirements() throws WrongConfigurationException;
	
	/**
	 * Muda a configuracao dos nodos.
	 * Em especifico deve mudar o modelo de Reliability 
	 * @throws WrongConfigurationException
	 */
	void changeRequirements() throws WrongConfigurationException;
	
	/**
	 * Envia um evento em no dado timeStartEvents, usa o tempo relativo
	 * @param timeStartEvents tempo que será enviado o evento
	 */
	void sentEventRelative(double timeStartEvents);
	
	/**
	 * Faz o 'broadcast' enviando uma mensagem unicast para cada vizinho,
	 * isto é feito, pois o framework somente envia NACKs para mensagens unicast
	 * @param m mensagem que será enviada
	 */
	void broadcastWithNack(Message m);
	
	/**
	 * Faz o envio unicast de uma mensagem
	 * @param m mensagem a ser enviada
	 * @param n no de destino para a menssagem 
	 */
	void sendUnicastMsg(Message m, Node n);
	
	/**
	 * Faz um broadcast comum da mensagem
	 * @param m mensagem que deve ser enviada por broadcast
	 */
	void broadcastMsg(Message m);
	
	/**
	 * Manipulador para eventos, recebe um evento e verifica se o nodo é o destinatário da mensagem. Caso seja envaminha para o nextHop,
	 * caso contrário, não faz nada.
	 * 
	 * @param inbox Mensagem que foi recebida pelo inbox do handleMessages
	 * @param msg mensagem convertida para EventMessage
	 */
	void handleEvent(Inbox inbox, EventMessage msg);
	
	/**
	 * Retorna a instancia do nodo que gerencia as estatisticas
	 * @return a instancia de StatisticsNode que controla as estatisticas de energia e mensagens enviadas e transmitidas pelo nodo.
	 */
	StatisticsNode getStatisticNode();
	
	/**
	 * Retorna o nº de saltos do nodo até o sink
	 * @return um inteiro que indica a quantidade de saltos do nodo até o sink
	 */
	int getHops();
}
