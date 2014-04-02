package Analises;

import projects.defaultProject.nodes.edges.GenericWeightedEdge;

public class EnergyModel {
	
	/**
	 * Custo = Etxelec + (Eamp * dist^alpha) + sum(Etrelec) 
	 */
	
	public static float globalEnergySpend = 0; //energia gasta por todos os nodos
	private float energySpend = 0; //energia gasta
	
	private float Ptxelec = 50f; // Pot para transmissao em mW
	private float Prxelec = 50f; // Pot para recepcao em mW
	private float b = 1000000f;// rate em bit/s
	private float Eamp = 0.016f; // artigo do Ramom
	private float dist = 30f; // distancia em metros
	private int alpha = 2; // distancia em metros
	
	
	public void spendTx(GenericWeightedEdge e){
		energySpend += (Ptxelec/e.getRateBits()) * Eamp * dist;
		globalEnergySpend += (Ptxelec/e.getRateBits()) * Eamp * dist;
	}
	
	public void spendRx(GenericWeightedEdge e){
		energySpend += (Prxelec/e.getRateBits());
		globalEnergySpend += (Prxelec/e.getRateBits());
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
}
