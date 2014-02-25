package projects.BaseTest.models.messageTransmissionModels;

import sinalgo.models.MessageTransmissionModel;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Message;

public class MyMsgTransModel extends MessageTransmissionModel {

	@Override
	public double timeToReach(Node startNode, Node endNode, Message msg) {
		// TODO Auto-generated method stub
		double dist = startNode.getPosition().distanceTo(endNode.getPosition());
		
		System.out.println("MTM distancia: "+Math.sqrt(dist));
		System.out.println("MTM  Sem sqrt distancia: "+dist);
		if(dist < 300){
			return 0.4;
		}else{
			return 5.2;
		}
	}

}
