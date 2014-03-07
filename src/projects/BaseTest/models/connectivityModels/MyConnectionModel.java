package projects.BaseTest.models.connectivityModels;


import sinalgo.configuration.WrongConfigurationException;
import sinalgo.models.ConnectivityModel;
import sinalgo.nodes.Node;

public class MyConnectionModel extends ConnectivityModel {

	@Override
	public boolean updateConnections(Node n) throws WrongConfigurationException {
//		// TODO Auto-generated method stub
//		Iterator<Edge> iEdge = n.outgoingConnections.iterator();
//		MyEdge e;
//		MyConnectivityHelper test = new MyConnectivityHelper();
//		while(iEdge.hasNext()){
//			e = (MyEdge) iEdge.next();
//			if(test.isConnected(n, e.endNode)){
//				Position a = n.getPosition();
//				Position b = e.endNode.getPosition();
//				double comprimento;
//				comprimento = Math.sqrt(Math.pow(a.xCoord -b.xCoord, 2) + Math.pow(a.yCoord -b.yCoord, 2));
//				System.out.println("Comprimento "+comprimento);
//				e.setComprimento(comprimento);
//				return true;
//			}
//		}
//		
		/*Iterator<Edge> iEdge = n.outgoingConnections.iterator();
		MyEdge e;
		Position a, b;
		double comprimento;
		while(iEdge.hasNext()){
			e = (MyEdge) iEdge.next();
			a = e.startNode.getPosition();
			b = e.endNode.getPosition();
			comprimento = Math.sqrt(Math.pow(a.xCoord -b.xCoord, 2) + Math.pow(a.yCoord -b.yCoord, 2));
			System.out.println("Comprimento "+comprimento);
			e.setComprimento(comprimento);
		}*/
		return false;
	}

}
