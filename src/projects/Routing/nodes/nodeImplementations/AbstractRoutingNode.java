package projects.Routing.nodes.nodeImplementations;

import projects.defaultProject.models.reliabilityModels.GenericReliabilityModel;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;
/**
 * Define todo o que deve existir de comum nas 
 * diferentes implementacoes 
 * @author bruno
 *
 */
public abstract class AbstractRoutingNode extends Node {
	
	
	/**
	 * SOMENTE MECHA NESSE MÉTODO SE VC SABE O QUE ESTA FAZENDO
	 * Este método pode ser utilizado para alterar 
	 * em tempo de execução alguma das configurações iniciais.
	 * @throws WrongConfigurationException
	 */
	public void changeRequirements() throws WrongConfigurationException{
		this.reliabilityModel = new GenericReliabilityModel();
		System.out.println(this.getReliabilityModel());
	}
	
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
