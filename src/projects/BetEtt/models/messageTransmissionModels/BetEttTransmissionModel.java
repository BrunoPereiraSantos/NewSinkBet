package projects.BetEtt.models.messageTransmissionModels;

import sinalgo.models.MessageTransmissionModel;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

public class BetEttTransmissionModel extends MessageTransmissionModel {

	@Override
	public double timeToReach(Node startNode, Node endNode, Message msg) {
		// TODO Auto-generated method stub
		double dist = startNode.getPosition().distanceTo(endNode.getPosition());
		/* Link rate 	|	time arrive mSec	|	dist
		 * ----------------------------------------------------
		 * 11			|	2542				|	399
		 * 5.5			|	3673				|	531
		 * 2			|	7634				|	669
		 * 1			|	13858				|	796
		 * */
		//System.out.println("MTM distancia: "+Math.sqrt(dist));
		System.out.println("MTM  Sem sqrt distancia: "+dist);
		
		if(dist < 399)						return 0.002542;
		if(dist >= 399 && dist < 531)		return 0.003673;
		if(dist >= 531 && dist < 669)		return 0.007634;
		if(dist >= 669 && dist <= 796)		return 0.013858;
		
		return 1;
	}

}
