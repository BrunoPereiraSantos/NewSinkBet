package projects.RateBet.nodes.edges;

import sinalgo.nodes.edges.BidirectionalEdge;
import sinalgo.tools.statistics.UniformDistribution;

public class EdgeRate extends BidirectionalEdge {

	private double etx;
	private double rate;
	
	@Override
	public void initializeEdge() {
		// TODO Auto-generated method stub
		super.initializeEdge();
		UniformDistribution v;
		double dist = startNode.getPosition().distanceTo(endNode.getPosition());
		//System.out.println("Distancia na edge: "+dist);
		if(dist < 399){
			v = new UniformDistribution(0.01d, 24.9d);
			etx = v.nextSample();
			rate = 1.0f;
			return;
		}
		if(dist >= 399 && dist < 531){
			v = new UniformDistribution(25.0d, 49.9d);
			etx = v.nextSample();
			rate = 1.44f;
			return;
		}
		if(dist >= 531 && dist < 669){
			v = new UniformDistribution(50.0d, 74.9d);
			etx = v.nextSample();
			rate = 3.0f;
			return;
		}
		if(dist >= 669 && dist <= 796){
			v = new UniformDistribution(75.0d, 99.d);
			etx = v.nextSample();
			rate = 5.45f;
			return;
		}
	}

	@Override
	public String toString() {
		
		return "EdgeRate [etx=" + String.format("%.2f", etx) +
				"\nrate=" + rate +
				"\nstartNode=" + startNode +
				"\nendNode=" + endNode +
				"\nnumberOfMessagesOnThisEdge="+ numberOfMessagesOnThisEdge+
				"\ngetID()=" + getID()+
				"\ntoString()=" + super.toString() + "]";
	}

	public double getEtx() {
		return etx;
	}

	public void setEtx(double etx) {
		this.etx = etx;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
	
	
	
	
}
