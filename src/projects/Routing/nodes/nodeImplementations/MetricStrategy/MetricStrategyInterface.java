package projects.Routing.nodes.nodeImplementations.MetricStrategy;

import projects.Routing.nodes.edges.WeightEdge;
import projects.Routing.nodes.messages.Pack;

public interface MetricStrategyInterface {
	
	public float calcGradient(Pack msg, WeightEdge e);
	
	
}
