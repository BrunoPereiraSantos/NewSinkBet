package projects.Routing.nodes.nodeImplementations;

import java.awt.Color;
import java.util.Iterator;

import projects.Routing.models.reliabilityModels.RoutingReliabilityModel;
import projects.Routing.nodes.edges.WeightEdge;
import projects.Routing.nodes.messages.PackEvent;
import projects.Routing.nodes.timers.RoutingMessageTimer;
import projects.defaultProject.models.reliabilityModels.GenericReliabilityModel;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Message;
import sinalgo.runtime.Global;

/**
 * Define todo o que deve existir de comum nas diferentes implementacoes
 * 
 * @author bruno
 * 
 */
public abstract class AbstractRoutingNode extends Node {

	/**
	 * SOMENTE MECHA NESSE MÉTODO SE VC SABE O QUE ESTA FAZENDO Este método pode
	 * ser utilizado para alterar em tempo de execução alguma das configurações
	 * iniciais.
	 * 
	 * @throws WrongConfigurationException
	 */
	public void changeRequirements() throws WrongConfigurationException {
		this.reliabilityModel = new RoutingReliabilityModel();
		System.out.println(this.getReliabilityModel());
	}

	/**
	 * Insere PackEvents para utilizarem a arvore de roteamento
	 * 
	 * @param duration
	 *            tempo de duração dos shots
	 * @param shots
	 *            quantidades de packevent a ser inserido
	 * @param n 
	 */
	public void setTraffic(double duration, int shots) {
		double interval = duration / shots;

		// uniforme distribuição para escolher um tempo de 0 a 10 minutos para
		// iniciar a sequencia de eventos daquele nodo
		// UniformDistribution ud = new UniformDistribution(0, 5);
		// double time = interval + (((int)ud.nextSample()) * 60);
		double time = 1;

		//System.out.println("########## shots=" + shots);
		//System.out.println("########## time=" + time);

		while (shots > 0) {
			PackEvent pkt = new PackEvent(this.ID, 0, Global.currentTime + time, 0);
			RoutingMessageTimer mt = new RoutingMessageTimer(pkt, true);
			mt.startRelative(time, this);
			time += interval;
			shots -= 1;
		}
	}

	/*
	 * Inicio dos metodos de Radio
	 */
	public abstract void forwardMsg(Message m);

	public abstract void processBroadcastMsg(Message m);

	/**
	 * Envia uma mensagem (Unicast) para cada um dos vizinhos do dono.
	 * 
	 * @param m
	 *            mensagem a ser enviada
	 */
	public void processBroadcastMsgWithNack(Message m) {
		WeightEdge edge = null;
		Iterator<Edge> it = this.outgoingConnections.iterator();

		while (it.hasNext()) {
			edge = (WeightEdge) it.next();

			this.send(m, edge.endNode);
		}

		this.setColor(Color.GRAY);
	}
	/*
	 * Fim dos metodos de Radio
	 */
}
