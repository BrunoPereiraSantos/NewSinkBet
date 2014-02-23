package projects.BaseTest.nodes.edges;

import sinalgo.nodes.Position;
import sinalgo.nodes.edges.BidirectionalEdge;

public class MyEdge extends BidirectionalEdge {
	double comprimento;

	
	
	/**
	 * @param comprimento
	 */
	public MyEdge() {
		super();
//		Position a = this.endNode.getPosition();
//		Position b = this.startNode.getPosition();
//		comprimento = Math.sqrt(Math.pow(a.xCoord -b.xCoord, 2) + Math.pow(a.yCoord -b.yCoord, 2));
		
	}

	public double getComprimento() {
		return comprimento;
	}

	public void setComprimento(double comprimento) {
		this.comprimento = comprimento;
	}

	@Override
	public String toString() {
		return "MyEdge [comprimento=" + comprimento + "]";
	}
	
	
	
}
