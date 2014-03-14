package projects.BetHop.models.reliabilityModels;

import java.util.Iterator;
import projects.BetHop.nodes.edges.EdgeBetHop;
import sinalgo.models.ReliabilityModel;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Packet;
import sinalgo.tools.statistics.UniformDistribution;

public class BetHopReliabilityModel extends ReliabilityModel {

	UniformDistribution ud = new UniformDistribution(.1, 100.0);
	private double etxLink;
	
	
	@Override
	public boolean reachesDestination(Packet p) {
		// TODO Auto-generated method stub
		Iterator<Edge> edgeIt = p.origin.outgoingConnections.iterator();
		EdgeBetHop e;
		while(edgeIt.hasNext()){
			e = (EdgeBetHop) edgeIt.next();
			if(e.endNode.equals(p.destination)){
				etxLink = e.getEtx();
			}
		}
		
		double r = ud.nextSample();
		System.out.println("r = "+String.format("%.2f", r)+", extLink = "+String.format("%.2f", etxLink));
		return r >= etxLink;
	}

}
