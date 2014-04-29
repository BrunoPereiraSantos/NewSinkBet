package projects.Routing.nodes.nodeImplementations.MetricStrategy;

import projects.Routing.nodes.edges.WeightEdge;
import projects.Routing.nodes.messages.Pack;

public class EtxStrategy implements MetricStrategyInterface{

	@Override
	public float calcGradient(Pack msg, WeightEdge e) {
		
		float result = 0.0f;
		result = msg.getMetric() + e.getEtx();
		
		return result;
	}



}
