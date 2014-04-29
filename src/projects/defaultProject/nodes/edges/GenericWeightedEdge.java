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
		/*if(dist < 399){
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
			setParam(0.75f, 0.90f, 1.f);
			return;
		}*/
		
		
		if(dist < 399)
			setRate(11.f);
		else if(dist >= 399 && dist < 531)
			setRate(5.5f);
		else if(dist >= 531 && dist < 669)
			setRate(2.f);
		else if(dist >= 669 && dist <= 796)
			setRate(2.f);
		
		UniformDistribution v = new UniformDistribution(0.001, 0.95);
		setEtx((float) v.nextSample());
		
		setMtm(mtmWeights(getRate()));
		
		setEtt(getEtx() * transTime(getRate()));
		
	}
	
	private int transTime(float rate){
		if(rate == 11f)
			return	2542;
		if(rate == 5.5f)
			return 3673;
		if(rate == 2f)
			return 7634;
		if(rate == 1f)
			return 13858;
		
		return 13858;
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
		 * 11			|	5				|
		 * 5.5			|	7				|
		 * 2			|	14				|
		 * 1			|	25				|
		 * */
		
		if(rate == 11f)
			return 5f;
		if(rate == 5.5f)
			return 7f;
		if(rate == 2f)
			return 14f;
		if(rate == 1f)
			return 25f;
		
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
	
	public String printEdgeInformation(){
		return startNode.ID +
				" " + endNode.ID +
				" " + etx +
				" " + rate +
				" " + mtm +
				" " + ett;
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
