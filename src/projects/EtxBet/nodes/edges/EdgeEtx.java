package projects.EtxBet.nodes.edges;

import sinalgo.nodes.edges.Edge;
import sinalgo.tools.statistics.UniformDistribution;

public class EdgeEtx extends Edge {
	private double etx;

	public EdgeEtx() {
		super();
		UniformDistribution cte = new UniformDistribution(1, 100);
		//setETX((int)cte.nextSample());
		this.etx =( int)cte.nextSample();
		
	}

	public double getEtx() {
		return etx;
	}

	public void setEtx(double ETX_) {
		etx = ETX_;
	}
	
	public String toString(){
		String str = "ETX link = ";
		str += etx;
		return str;
	}
	
}
