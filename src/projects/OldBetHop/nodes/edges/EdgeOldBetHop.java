package projects.OldBetHop.nodes.edges;

import sinalgo.nodes.edges.Edge;
import sinalgo.tools.statistics.UniformDistribution;

public class EdgeOldBetHop extends Edge {
	public double etx;

	
	public EdgeOldBetHop() {
		super();
		UniformDistribution cte = new UniformDistribution(1, 100);
		this.etx = (int) cte.nextSample(); 
	}


	public double getEtx() {
		return etx;
	}


	public void setEtx(double etx) {
		this.etx = etx;
	}


	@Override
	public String toString() {
		return "EdgeHopBet [etx=" + etx + "]";
	}

	
	
}