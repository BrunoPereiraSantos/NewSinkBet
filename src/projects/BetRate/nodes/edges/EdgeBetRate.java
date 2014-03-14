package projects.BetRate.nodes.edges;

import sinalgo.nodes.edges.BidirectionalEdge;
import sinalgo.tools.statistics.UniformDistribution;

public class EdgeBetRate extends BidirectionalEdge {

	private float etx;
	private float rate;
	private float mtm;
	private float ett;
	
	@Override
	public void initializeEdge() {
		// TODO Auto-generated method stub
		super.initializeEdge();
		
		double dist = startNode.getPosition().distanceTo(endNode.getPosition());
		//System.out.println("Distancia na edge: "+dist);
		if(dist < 399){
			setParam(0.001f, 0.24f, 11.0f, 1.0f);
			//setParam(0, 0, 0);
			return;
		}
		if(dist >= 399 && dist < 531){
			setParam(0.25f, 0.49f, 5.5f, 1.44f);
			//setParam(0, 0, 0);
			return;
		}
		if(dist >= 531 && dist < 669){
			setParam(0.50f, 0.75f, 2.0f, 3.0f);
			//setParam(0, 0, 0);
			return;
		}
		if(dist >= 669 && dist <= 796){
			setParam(0.75f, 0.99f, 1.0f, 5.45f);
			//setParam(0, 0, 0);
			return;
		}
	}
	
	
	public void setParam(float min, float max, float rate, float mtm){
		UniformDistribution v = new UniformDistribution(min, max);
		this.etx = (float) v.nextSample();
		this.rate = rate;
		this.mtm = mtm;
		this.ett = etx * mtm;
	}
	
	public void setParam(float etx, float rate, float mtm){
		this.etx = etx;
		this.rate = rate;
		this.mtm = mtm;
		this.ett = etx * mtm;
	}
	
	@Override
	public String toString() {
		
		return "EdgeEtt [etx=" + etx +
				"\nrate=" + String.format("%.2f", rate) +
				"\nmtm=" + String.format("%.2f", mtm) +
				"\nett=" + String.format("%.2f", ett) +
				"\nstartNode=" + startNode.ID +
				"\nendNode=" + endNode.ID +
				"\nnumberOfMessagesOnThisEdge="+ numberOfMessagesOnThisEdge+
				"\ngetID()=" + getID()+
				"\ntoString()=" + super.toString() + "]";
	}

	

	public float getEtx() {
		return etx;
	}

	public void setEtx(float etx) {
		this.etx = etx;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public float getMtm() {
		return mtm;
	}

	public void setMtm(float mtm) {
		this.mtm = mtm;
	}

	public float getEtt() {
		return ett;
	}

	public void setEtt(float ett) {
		this.ett = ett;
	}
}
