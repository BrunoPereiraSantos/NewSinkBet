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
import sinalgo.tools.statistics.Distribution;
import sinalgo.tools.statistics.PoissonDistribution;
import sinalgo.tools.statistics.UniformDistribution;


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
		Distribution distNodes = null;
		Distribution distTraffic = null;
		double trafficDuration = 0.;
		int range = 0;
		
		
		
		System.out.println("------------------------------------------------------");
		System.out.println("ConfigTest");
		System.out.println("------------------------------------------------------");
		try {
			percent = Configuration.getDoubleParameter("ConfigTest/NodeEvents/percent");
			qntNodeEv = (int ) (numNodes * percent) / 100;
			range = numNodes - 2;
			if(range <= 0)	range = 1;
			
			System.out.println("ConfigTest/NodeEvents/percent = "+percent);
			System.out.println("qntNodeEv = "+qntNodeEv);
		} catch (CorruptConfigurationEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//----------- Configurando nos selecionados para eventos ----------------------------------
		try {
			String model = Configuration.getStringParameter("ConfigTest/NodeEvents/distribution");
			System.out.println("ConfigTest/NodeEvents/distribution = "+model);
			
			if(model.equalsIgnoreCase("Uniform")){
				
				distNodes = new UniformDistribution(1, numNodes);
				
			}else if(model.equalsIgnoreCase("Poisson")){
				
				double lambda = Configuration.getDoubleParameter("ConfigTest/NodeEvents/lambda");
				distNodes = new PoissonDistribution(lambda);
				System.out.println("ConfigTest/NodeEvents/lambda = "+lambda);
			}
			
		} catch (CorruptConfigurationEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//----------- Configurando nos trafego      ----------------------------------
		try {
			String model = Configuration.getStringParameter("ConfigTest/Traffic/distribution");
			System.out.println("ConfigTest/Traffic/distribution = "+model);
			
			if(model.equalsIgnoreCase("Uniform")){
				
				distTraffic = new UniformDistribution(1, 100); //os nodos podem enviar de 1 a 100 eventos por minuto
				
			}else if(model.equalsIgnoreCase("Poisson")){
				
				double lambda = Configuration.getDoubleParameter("ConfigTest/Traffic/lambda");
				distTraffic = new PoissonDistribution(lambda);
				System.out.println("ConfigTest/Traffic/lambda = "+lambda);
			}
			
			trafficDuration = Configuration.getDoubleParameter("ConfigTest/Traffic/duration");
			System.out.println("ConfigTest/Traffic/duration = "+trafficDuration);
		} catch (CorruptConfigurationEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		int nodeChoose;
		while(qntNodeEv > nodesEvent.size()){
			if((nodeChoose = (int) distNodes.nextSample()) == 1)	continue; // nao posso escolher o nodo 1, pois e o sink
			nodesEvent.add(nodeChoose);
		}
		
		it = nodesEvent.iterator();
		while(it.hasNext()){
			setTrafficNodes( (InterfaceEventTest) Tools.getNodeByID(it.next()), trafficDuration, distTraffic);
		}
		
		

		System.out.println("Chosen nodes = "+nodesEvent);
		
		System.out.println("------------------------------------------------------");
		System.out.println("End ConfigTest");
		System.out.println("------------------------------------------------------");
		
		//selectNodeEvent( (InterfaceEventTest) Tools.getNodeByID(6));
		//selectNodeEvent( (InterfaceEventTest) Tools.getNodeByID(7));
		//setTrafficNodes( (InterfaceEventTest) Tools.getNodeByID(6), trafficDuration, distTraffic);
		changeReabilityModel();
	}
	
	private void selectNodeEvent(InterfaceEventTest n){
		n.sentEvent_IEV(1.);
	}
	
	/**
	 * @param n, o nodo que vai enviar o tráfego
	 * @param duration, duracao que vai levar aquele tráfego
	 * @param dist, distribuição que indica qual a taxa aquele nodo vai enviar, esta taxa é dada como números de eventos a cada 60s
	 */
	private void setTrafficNodes(InterfaceEventTest n, double duration,
			Distribution dist) {
		if (dist instanceof UniformDistribution) {
			int rate = (int) dist.nextSample();
			double interval = duration / rate;
			
			// uniforme distribuição para escolher um tempo de 0 a 10 minutos para iniciar a sequencia de eventos daquele nodo
			//UniformDistribution ud = new UniformDistribution(0, 5); 
			//double time = interval + (((int)ud.nextSample()) * 60);
			double time = 1;
			
			System.out.println("########## rate="+rate);
			System.out.println("########## time="+time);
			
			int qntEventos = (int) ((duration / 60) * rate);
			while (qntEventos > 0) {
				n.sentEvent_IEV(time);
				time += interval;
				qntEventos -= 1;
			}
		}
	}
	
	public static void changeReabilityModel(){
		Iterator<Node> it = Tools.getNodeList().iterator();
		InterfaceEventTest n;
		while(it.hasNext()){
			n = (InterfaceEventTest) it.next();
			n.changeRequirements();
		}
	}
}
