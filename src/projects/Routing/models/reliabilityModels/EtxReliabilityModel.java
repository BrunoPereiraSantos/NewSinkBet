package projects.Routing.models.reliabilityModels;

import projects.Routing.nodes.edges.WeightEdge;
import sinalgo.models.ReliabilityModel;
import sinalgo.nodes.messages.Packet;
import sinalgo.tools.statistics.UniformDistribution;

public class EtxReliabilityModel extends ReliabilityModel {
	UniformDistribution ud = new UniformDistribution(.001, 0.99);
	float etxLink = 0.0f;
	
	@Override
	public boolean reachesDestination(Packet p) {
		
		if(p.edge != null)
			etxLink = ((WeightEdge) p.edge).getEtx();
		
		float r = (float) ud.nextSample();
			
		//System.out.println("De node="+p.origin.ID+" Para Node="+p.destination.ID);
		//System.out.println("r = "+String.format("%.2f", r)+", extLink = "+String.format("%.2f", etxLink) + " aceita? "+( r >= etxLink));
		if(r >= etxLink)
			return true;
			
			
		return false;
	}

}
