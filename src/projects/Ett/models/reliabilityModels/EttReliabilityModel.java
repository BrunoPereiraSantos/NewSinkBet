package projects.Ett.models.reliabilityModels;

import java.util.Iterator;

import projects.Ett.nodes.edges.EdgeEtt;
import sinalgo.models.ReliabilityModel;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Packet;
import sinalgo.tools.statistics.UniformDistribution;

public class EttReliabilityModel extends ReliabilityModel {

	UniformDistribution ud = new UniformDistribution(.1, 100.0);
	private double etxLink;
	
	
	@Override
	public boolean reachesDestination(Packet p) {
		// TODO Auto-generated method stub
		Iterator<Edge> edgeIt = p.origin.outgoingConnections.iterator();
		EdgeEtt e;
		while(edgeIt.hasNext()){
			e = (EdgeEtt) edgeIt.next();
			if(e.endNode.equals(p.destination)){
				etxLink = e.getEtx();
			}
		}
		
		double r = ud.nextSample();
		System.out.println("r = "+String.format("%.2f", r)+", extLink = "+String.format("%.2f", etxLink));
		return r >= etxLink;
	}

}
