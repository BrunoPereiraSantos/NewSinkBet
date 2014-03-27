package Analises;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import sinalgo.configuration.Configuration;
import sinalgo.configuration.CorruptConfigurationEntryException;
import sinalgo.nodes.Node;
import sinalgo.runtime.Runtime;
import sinalgo.tools.Tools;


public class TestClass {
	
	
	public void runTree(){
		if(Tools.isSimulationInBatchMode())
			Tools.getBatchRuntime().run(Integer.MAX_VALUE, true);
		else
			Tools.getGuiRuntime().run(Integer.MAX_VALUE, true);
	}
	
	
	
	
	public void installEvents(){
		double percent = 0;
		int qntNodeEv = 0;
		int numNodes = Runtime.nodes.size();
		Set<Integer> nodesEvent = new HashSet<Integer>();
		Iterator<Integer> it;
		Random r = new Random();
		int range = 0;
		
		
		try {
			percent = Configuration.getDoubleParameter("NodeEvents/percent");
			qntNodeEv = (int ) (numNodes * percent) / 100;
			range = numNodes - 2;
			if(range <= 0)	range = 1;
		} catch (CorruptConfigurationEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Numero de nodos "+ Runtime.nodes.size());
		System.out.println("Porcentagem de nodos que vao emitir eventos "+ percent + "%");
		
		/*while(qntNodeEv > nodesEvent.size()){
			nodesEvent.add(2+r.nextInt((range)));
			System.out.println(nodesEvent);
		}
		
		it = nodesEvent.iterator();
		while(it.hasNext()){
			selectNodeEvent( (InterfaceEventTest) Tools.getNodeByID(it.next()));
		}*/
		selectNodeEvent( (InterfaceEventTest) Tools.getNodeByID(6));
		selectNodeEvent( (InterfaceEventTest) Tools.getNodeByID(7));
		changeReabilityModel();
	}
	
	private void selectNodeEvent(InterfaceEventTest n){
		n.sentEvent_IEV(1.);
	}
	
	private void changeReabilityModel(){
		Iterator<Node> it = Tools.getNodeList().iterator();
		InterfaceEventTest n;
		while(it.hasNext()){
			n = (InterfaceEventTest) it.next();
			n.changeRequirements();
		}
	}
}
