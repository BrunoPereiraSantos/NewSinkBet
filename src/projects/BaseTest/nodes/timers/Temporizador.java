package projects.BaseTest.nodes.timers;

import projects.BaseTest.nodes.messages.MBT;
import projects.BaseTest.nodes.nodeImplementations.NodeBT;
import sinalgo.nodes.timers.Timer;

public class Temporizador extends Timer {
	MBT m;
	
	
	
	/**
	 * @param m
	 */
	public Temporizador(MBT a) {
		super();
		this.m = a;
		this.m.setA(a.getA());
	}



	@Override
	public void fire() {
		// TODO Auto-generated method stub
		((NodeBT) this.node).broadcast(m);
	}

}
