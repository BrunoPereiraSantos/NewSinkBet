package projects.BaseTest.models.connectivityModels;

import java.util.Iterator;

import projects.BaseTest.nodes.edges.MyEdge;
import sinalgo.models.ConnectivityModelHelper;
import sinalgo.nodes.Node;
import sinalgo.nodes.Position;
import sinalgo.nodes.edges.Edge;
import sinalgo.runtime.Global;

public class MyConnectivityHelper extends ConnectivityModelHelper {

	@Override
	protected boolean isConnected(Node from, Node to) {
		// TODO Auto-generated method stub
		Position a = from.getPosition();
		Position b = to.getPosition();
		
		double comprimento;
		comprimento = a.distanceTo(b);
		Iterator<Edge> iEdge = from.outgoingConnections.iterator();
		MyEdge e;
		while(iEdge.hasNext()){
			e = (MyEdge) iEdge.next();
			if(e.endNode.equals(to))
				e.setComprimento(comprimento);
		}
		
		return true;
	}

}
