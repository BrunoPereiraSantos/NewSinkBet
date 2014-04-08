package projects.defaultProject.models.reliabilityModels;

import java.util.Iterator;

import Analises.StatisticsNode;
import projects.defaultProject.nodes.edges.GenericWeightedEdge;
import sinalgo.models.ReliabilityModel;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Packet;
import sinalgo.tools.statistics.UniformDistribution;

public class GenericReliabilityModel extends ReliabilityModel {
	UniformDistribution ud = new UniformDistribution(.001, 0.99);
	private float etxLink;
	
	
	@Override
	public boolean reachesDestination(Packet p) {
		// TODO Auto-generated method stub
		Iterator<Edge> edgeIt = p.origin.outgoingConnections.iterator();
		GenericWeightedEdge e;
		while(edgeIt.hasNext()){
			e = (GenericWeightedEdge) edgeIt.next();
			if(e.endNode.equals(p.destination)){
				etxLink = e.getEtx();
			}
		}
		
		double r = ud.nextSample();
		//System.out.println("De node="+p.origin.ID+" Para Node="+p.destination.ID);
		//System.out.println("r = "+String.format("%.2f", r)+", extLink = "+String.format("%.2f", etxLink) + " aceita? "+( r >= etxLink));
		if(r >= etxLink)
			return true;
		
		StatisticsNode.countGlobalDropMessages();
		return false;
	}

}
