package projects.defaultProject.nodes.edges;

import sinalgo.nodes.edges.BidirectionalEdge;
import sinalgo.tools.statistics.UniformDistribution;

public class GenericWeightedEdge extends BidirectionalEdge {

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
			setParam(0.001f, 0.24f, 11.f);
			return;
		}
		if(dist >= 399 && dist < 531){
			setParam(0.25f, 0.49f, 5.5f);
			return;
		}
		if(dist >= 531 && dist < 669){
			setParam(0.50f, 0.74f, 2.f);
			return;
		}
		if(dist >= 669 && dist <= 796){
			setParam(0.75f, 0.99f, 1.f);
			return;
		}
	}
	
	
	public void setParam(float min, float max, float rate){
		UniformDistribution v = new UniformDistribution(min, max);
		this.etx = (float) v.nextSample();
		this.rate = rate;
		this.mtm = mtmWeights(rate);
		this.ett = etx * mtm;
	}
	
	public void setParam(float min, float max, float rate, float mtm){
		UniformDistribution v = new UniformDistribution(min, max);
		this.etx = (float) v.nextSample();
		this.rate = rate;
		this.mtm = mtmWeights(rate);
		this.ett = etx * mtm;
	}
	
	public void setParam(float etx, float rate){
		this.etx = etx;
		this.rate = rate;
		this.mtm = mtmWeights(rate);
		this.ett = etx * mtm;
	}
	
	/**
	 * Define o mtm pesos, segundo o artigo:
	 * High Throughput Route Selection in Multi-Rate Ad Hoc Wireless Networks
 	 * 
	 */
	private float mtmWeights(float rate){
		/* Link rate 	|	MTM Weights		|
		 * ----------------------------------
		 * 11			|	1				|
		 * 5.5			|	1.44			|
		 * 2			|	3				|
		 * 1			|	5.45			|
		 * */
		
		if(rate == 11f)
			return 1f;
		if(rate == 5.5f)
			return 1.44f;
		if(rate == 2f)
			return 3f;
		if(rate == 1f)
			return 5.54f;
		
		return 5.45f;
	}
	
	/**
	 * retorna o rate em bits/s
	 */
	public float getRateBits(){
		if(rate == 11f)
			return 11000000f;
		if(rate == 5.5f)
			return 5500000f;
		if(rate == 2f)
			return 2000000f;
		if(rate == 1f)
			return 1000000f;
		
		return 1000000f;
	}
	
	
	@Override
	public String toString() {
		
		return "GenericWeightedEdge" +
				"\n[etx=" + String.format("%.2f", etx) +
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
