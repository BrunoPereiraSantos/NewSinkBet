package projects.Rate.nodes.edges;

import sinalgo.nodes.edges.BidirectionalEdge;
import sinalgo.tools.statistics.UniformDistribution;

public class EdgeRate extends BidirectionalEdge {

	private double etx;
	private double rate;
	private double mtm;
	private double ett;
	
	@Override
	public void initializeEdge() {
		// TODO Auto-generated method stub
		super.initializeEdge();
		
		double dist = startNode.getPosition().distanceTo(endNode.getPosition());
		//System.out.println("Distancia na edge: "+dist);
		if(dist < 399){
			setParam(0.001, 0.24, 11., 1.);
			return;
		}
		if(dist >= 399 && dist < 531){
			setParam(0.25, 0.49, 5.5, 1.44);
			return;
		}
		if(dist >= 531 && dist < 669){
			setParam(0.50, 0.74, 2., 3.);
			return;
		}
		if(dist >= 669 && dist <= 796){
			setParam(0.75, 0.98, 1., 1.);
			return;
		}
	}

	public void setParam(double min, double max, double rate, double mtm){
		UniformDistribution v = new UniformDistribution(min, max);
		this.etx = v.nextSample();
		this.rate = rate;
		this.mtm = mtm;
		this.ett = etx * mtm;
	}
	
	public void setParam(double etx, double rate, double mtm){
		this.etx = etx;
		this.rate = rate;
		this.mtm = mtm;
		this.ett = etx * mtm;
	}
	
	@Override
	public String toString() {
		
		return "EdgeRate [etx=" + String.format("%.2f", etx) +
				"\nrate=" + String.format("%.2f", rate) +
				"\nmtm=" + String.format("%.2f", mtm) +
				"\nett=" + String.format("%.2f", ett) +
				"\nstartNode=" + startNode.ID +
				"\nendNode=" + endNode.ID +
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

	public double getMtm() {
		return mtm;
	}

	public void setMtm(double mtm) {
		this.mtm = mtm;
	}

	public double getEtt() {
		return ett;
	}

	public void setEtt(double ett) {
		this.ett = ett;
	}
	
}
