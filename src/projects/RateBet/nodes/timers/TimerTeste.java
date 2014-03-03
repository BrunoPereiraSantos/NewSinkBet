package projects.RateBet.nodes.timers;

import projects.RateBet.nodes.messages.Teste;
import projects.RateBet.nodes.nodeImplementations.NodeRate;
import sinalgo.nodes.timers.Timer;

public class TimerTeste extends Timer {
	Teste m;
//	
	@Override
	public void fire() {
		// TODO Auto-generated method stub
		((NodeRate) this.node).broadcast(m);
	}
/**
 * @param m
 */
public TimerTeste(Teste m) {
	super();
	this.m = m;
}

}
