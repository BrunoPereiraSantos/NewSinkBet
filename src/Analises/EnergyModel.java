package Analises;

import projects.defaultProject.nodes.edges.GenericWeightedEdge;

public class EnergyModel {
	
	/**
	 * Custo = Etxelec + (Eamp * dist^alpha) + sum(Etrelec) 
	 */
	
	public static float globalEnergySpend = 0; //energia gasta por todos os nodos
	private float energySpendEv = 0; //energia gasta na fase de eventos
	private float energySpendTree = 0; //energia gasta na fase de eventos
	
	private float Ptxelec = 50f; // Pot para transmissao em mW
	private float Prxelec = 50f; // Pot para recepcao em mW
	private float b = 1000000f;// rate em bit/s
	private float Eamp = 0.016f; // artigo do Ramom
	private float dist = 100f; // distancia em metros
	private int alpha = 2; // distancia em metros
	
	/**
	 * ele vai mandar com a menor taxa possível para que alcance a maior quantidade de nos
	 * por isso sempre uso 1Mbps
	 */
	public void spendTx_tree(){
		energySpendTree += Tx(1000000f); 
		globalEnergySpend += Tx(1000000f);
	}
	
	/**
	 * ele vai mandar com a menor taxa possível para que alcance a maior quantidade de nos
	 * por isso sempre uso 1Mbps
	 */
	public void spendRx_tree(){
		energySpendTree += Rx(1000000f);
		globalEnergySpend += Rx(1000000f);
	}
	
	public void spendTx_Ev(GenericWeightedEdge e){
		float d = (float) e.startNode.getPosition().distanceTo(e.endNode.getPosition());
		float spend = Tx(e.getRateBits(), d);
		energySpendEv += spend;
		globalEnergySpend += spend;
	}
	
	public void spendRx_Ev(GenericWeightedEdge e){
		energySpendEv += Rx(e.getRateBits());
		globalEnergySpend += Rx(e.getRateBits());
	}
	
	private float Rx(float b){
		return Prxelec/b;
	}
	
	private float Tx(float b){
		return (float) (Ptxelec/(b * Eamp * Math.pow(dist, alpha)));
	}
	
	private float Tx(float b, float d){
		return (float) (Ptxelec/(b * Eamp * Math.pow(d, alpha)));
	}
	
	public static float getGlobalEnergySpend() {
		return globalEnergySpend;
	}

	public static void setGlobalEnergySpend(float globalEnergySpend) {
		EnergyModel.globalEnergySpend = globalEnergySpend;
	}
	
	public float getEnergySpendEv() {
		return energySpendEv;
	}

	public void setEnergySpendEv(float energySpendEv) {
		this.energySpendEv = energySpendEv;
	}

	public float getPtxelec() {
		return Ptxelec;
	}

	public void setPtxelec(float ptxelec) {
		Ptxelec = ptxelec;
	}

	public float getPrxelec() {
		return Prxelec;
	}

	public void setPrxelec(float prxelec) {
		Prxelec = prxelec;
	}

	public float getB() {
		return b;
	}

	public void setB(float b) {
		this.b = b;
	}

	public float getEamp() {
		return Eamp;
	}

	public void setEamp(float eamp) {
		Eamp = eamp;
	}

	public float getDist() {
		return dist;
	}

	public void setDist(float dist) {
		this.dist = dist;
	}

	public int getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public float getEnergySpendTree() {
		return energySpendTree;
	}

	public void setEnergySpendTree(float energySpendTree) {
		this.energySpendTree = energySpendTree;
	}

	@Override
	public String toString() {
		return "	" +  String.format("%.9f", energySpendTree) + "	" + String.format("%.9f", energySpendEv);
	}
	
	
	
}
