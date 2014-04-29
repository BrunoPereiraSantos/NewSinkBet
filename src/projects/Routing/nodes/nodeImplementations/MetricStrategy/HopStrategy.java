package projects.Routing.nodes.nodeImplementations.MetricStrategy;

import projects.Routing.nodes.edges.WeightEdge;
import projects.Routing.nodes.messages.Pack;

public class HopStrategy implements MetricStrategyInterface {

	@Override
	public float calcGradient(Pack msg, WeightEdge e) {

		float result = 0.0f;
		result = msg.getMetric() + 1;
		
		return result;
	}

}
