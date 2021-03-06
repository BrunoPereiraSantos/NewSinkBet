/*
 Copyright (c) 2007, Distributed Computing Group (DCG)
                    ETH Zurich
                    Switzerland
                    dcg.ethz.ch

 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:

 - Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.

 - Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the
   distribution.

 - Neither the name 'Sinalgo' nor the names of its contributors may be
   used to endorse or promote products derived from this software
   without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package projects.Routing;

import java.awt.Graphics;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;

import projects.Routing.nodes.edges.WeightEdge;
import projects.Routing.nodes.nodeImplementations.AbstractRoutingNode;
import projects.Routing.nodes.nodeImplementations.RoutingNode;
import projects.Routing.utilities.EdgeUtility;
import projects.Routing.utilities.TrafficUtility;
import sinalgo.configuration.Configuration;
import sinalgo.configuration.CorruptConfigurationEntryException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.runtime.AbstractCustomGlobal;
import sinalgo.runtime.Runtime;
import sinalgo.tools.Tools;
import sinalgo.tools.logging.Logging;

/**
 * This class holds customized global state and methods for the framework. The
 * only mandatory method to overwrite is <code>hasTerminated</code> <br>
 * Optional methods to override are
 * <ul>
 * <li><code>customPaint</code></li>
 * <li><code>handleEmptyEventQueue</code></li>
 * <li><code>onExit</code></li>
 * <li><code>preRun</code></li>
 * <li><code>preRound</code></li>
 * <li><code>postRound</code></li>
 * <li><code>checkProjectRequirements</code></li>
 * </ul>
 * 
 * @see sinalgo.runtime.AbstractCustomGlobal for more details. <br>
 *      In addition, this class also provides the possibility to extend the
 *      framework with custom methods that can be called either through the menu
 *      or via a button that is added to the GUI.
 */
public class CustomGlobal extends AbstractCustomGlobal {

	private final String edgeSourcePath = "./Topology/Edges/";
	private final String topologySourcePath = "./Topology/";
	private final String trafficSourcePath = "./Traffic/";

	// importar valores das arestas

	private int idExecution;
	private String metricStrategy = "";

	public boolean hasTerminated() {
		return false;
	}

	/**
	 * An example of a method that will be available through the menu of the
	 * GUI.
	 */
	@AbstractCustomGlobal.GlobalMethod(menuText = "Echo")
	public void echo() {
		// Query the user for an input
		String answer = JOptionPane.showInputDialog(null,
				"This is an example.\nType in any text to echo.");
		// Show an information message
		JOptionPane.showMessageDialog(null, "You typed '" + answer + "'",
				"Example Echo", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * An example to add a button to the user interface. In this sample, the
	 * button is labeled with a text 'GO'. Alternatively, you can specify an
	 * icon that is shown on the button. See AbstractCustomGlobal.CustomButton
	 * for more details.
	 */
	@AbstractCustomGlobal.CustomButton(buttonText = "GO", toolTipText = "A sample button")
	public void sampleButton() {
		JOptionPane.showMessageDialog(null, "You Pressed the 'GO' button.");
	}

	public void insertEtx() {
		Iterator<Node> it = Runtime.nodes.iterator();
		RoutingNode n;
		/*
		 * Random generator = new Random(1); while(it.hasNext()){ n = it.next();
		 * //System.out.println(n); Iterator<Edge> it2 =
		 * n.outgoingConnections.iterator(); EdgeWeightEtxBet e;
		 * while(it2.hasNext()){ e = (EdgeWeightEtxBet) it2.next();
		 * //e.setEtx(UniformDistribution.nextUniform(0, 1));
		 * //e.setEtx(generator.nextDouble()); e.setEtx(1+generator.nextInt(9));
		 * System.out.println("ID "+n.ID+" ~["+e.getEtx()+"]> "+e.endNode.ID);
		 * 
		 * } }
		 */

		while (it.hasNext()) {
			n = (RoutingNode) it.next();
			// System.out.println(n);
			Iterator<Edge> it2 = n.outgoingConnections.iterator();
			WeightEdge e;
			while (it2.hasNext()) {
				e = (WeightEdge) it2.next();
				if (n.ID == 1) {
					if (e.endNode.ID == 2)
						e.setParam(0.2f, 2.f);
				}

				if (n.ID == 2) {
					if (e.endNode.ID == 1)
						e.setParam(0.1f, 11.f);
					if (e.endNode.ID == 3)
						e.setParam(0.1f, 11.f);
					if (e.endNode.ID == 4)
						e.setParam(0.1f, 11.f);
				}

				if (n.ID == 3) {
					if (e.endNode.ID == 2)
						e.setParam(0.2f, 2.f);
					if (e.endNode.ID == 5)
						e.setParam(0.1f, 11.f);
					if (e.endNode.ID == 6)
						e.setParam(0.1f, 11.f);
				}

				if (n.ID == 4) {
					if (e.endNode.ID == 2)
						e.setParam(0.2f, 2.f);
					if (e.endNode.ID == 5)
						e.setParam(0.1f, 11.f);
				}

				if (n.ID == 5) {
					if (e.endNode.ID == 3)
						e.setParam(0.2f, 2.f);
					if (e.endNode.ID == 4)
						e.setParam(0.2f, 2.f);
					if (e.endNode.ID == 7)
						e.setParam(0.1f, 11.f);
					if (e.endNode.ID == 8)
						e.setParam(0.1f, 11.f);
				}

				if (n.ID == 6) {
					if (e.endNode.ID == 3)
						e.setParam(0.9f, 1.f);
					if (e.endNode.ID == 7)
						e.setParam(0.1f, 11.f);
				}

				if (n.ID == 7) {
					if (e.endNode.ID == 5)
						e.setParam(0.2f, 2.f);
					if (e.endNode.ID == 6)
						e.setParam(0.1f, 11.f);
					if (e.endNode.ID == 8)
						e.setParam(0.2f, 2.f);
				}

				if (n.ID == 8) {
					if (e.endNode.ID == 5)
						e.setParam(0.4f, 2.f);
					// e.setEtx(0.4);
					if (e.endNode.ID == 7)
						e.setParam(0.2f, 2.f);
					if (e.endNode.ID == 9)
						e.setParam(0.2f, 2.f);
				}

				if (n.ID == 9) {
					if (e.endNode.ID == 8)
						e.setParam(0.1f, 11.f);
				}

				// e.setEtx(1+generator.nextInt(9));
				System.out.println("ID " + e.getID() + "      " + n.ID + " ~["
						+ e.getEtx() + "]> " + e.endNode.ID);

			}
		}
	}

	/**
	 * Dummy button to create a tree.
	 */
	@AbstractCustomGlobal.CustomButton(buttonText = "Graphics", toolTipText = "Show Graphics")
	public void Button3() {
		// int numNodes =
		// Integer.parseInt(Tools.showQueryDialog("Number of nodes:"));
		// int fanOut = Integer.parseInt(Tools.showQueryDialog("Max fanout:"));
		// buildTree(fanOut, numLeaves);
		printGraphicsINGuI();
		Runtime.reevaluateConnections();
		insertEtx();
	}

	public void printGraphicsINGuI() {
		Vector<RoutingNode> myNodes = new Vector<RoutingNode>();

		RoutingNode n = new RoutingNode();
		n.setPosition(300, 500, 0);
		n.finishInitializationWithDefaultModels(true);
		myNodes.add(n);

		n = new RoutingNode();
		n.setPosition(350, 500, 0);
		n.finishInitializationWithDefaultModels(true);
		myNodes.add(n);

		n = new RoutingNode();
		n.setPosition(400, 450, 0);
		n.finishInitializationWithDefaultModels(true);
		myNodes.add(n);

		n = new RoutingNode();
		n.setPosition(400, 550, 0);
		n.finishInitializationWithDefaultModels(true);
		myNodes.add(n);

		n = new RoutingNode();
		n.setPosition(450, 500, 0);
		n.finishInitializationWithDefaultModels(true);
		myNodes.add(n);

		n = new RoutingNode();
		n.setPosition(450, 400, 0);
		n.finishInitializationWithDefaultModels(true);
		myNodes.add(n);

		n = new RoutingNode();
		n.setPosition(500, 450, 0);
		n.finishInitializationWithDefaultModels(true);
		myNodes.add(n);

		n = new RoutingNode();
		n.setPosition(500, 500, 0);
		n.finishInitializationWithDefaultModels(true);
		myNodes.add(n);

		n = new RoutingNode();
		n.setPosition(550, 550, 0);
		n.finishInitializationWithDefaultModels(true);
		myNodes.add(n);

		// Repaint the GUI as we have added some nodes
		Tools.repaintGUI();

	}

	@Override
	public String includeGlobalMethodInMenu(Method m, String defaultText) {
		// TODO Auto-generated method stub
		return super.includeGlobalMethodInMenu(m, defaultText);
	}

	@Override
	public void customPaint(Graphics g, PositionTransformation pt) {
		// TODO Auto-generated method stub
		super.customPaint(g, pt);
	}

	boolean once = true , exec1xLog = true;
	

	@Override
	public void handleEmptyEventQueue() {
		super.handleEmptyEventQueue();

		if (once) {
			// devo modificar o modelo de confiabilidade
			// devo inserir os eventos para executar os testes

			updateNodeConf();
			
			loadTraffic();
			
			once = false;
		}else if (exec1xLog){
			
			
			printLog();
			
			exec1xLog = false;
		}
	}

	private void printLog() {

		System.out.println(((RoutingNode) Tools.getNodeByID(1)).statistic
				.printStatisticsPerNode());
		Logging myLog = Logging.getLogger(metricStrategy + "_"
				+ Tools.getNodeList().size() + ".txt", true);
		myLog.logln(((RoutingNode) Tools.getNodeByID(1)).statistic
				.printStatisticsPerNode());
		// ((RoutingNode)Tools.getNodeByID(1)).statistic.exportWrite("");
	}

	/**
	 * MODIFIQUE SOMENTE SE SOUBER O QUE ESTA FAZENDO
	 * 
	 * Atualiza as configurações de um nodo chama o changeRequirements() para
	 * cada um dos nodos que estão em execução
	 * 
	 * atualmente ele modifica o modelo de confiabilidade trocando de
	 * ReabilityModel para RoutingReabilityModel
	 */
	private void updateNodeConf() {

		Iterator<Node> it = Tools.getNodeList().iterator();
		AbstractRoutingNode n = null;
		while (it.hasNext()) {
			n = (AbstractRoutingNode) it.next();

			n.changeRequirements();

		}
	}

	@Override
	public void onExit() {
		// TODO Auto-generated method stub
		super.onExit();
		
		if(Tools.getNodeList().size() > 0){
			( (RoutingNode) Tools.getNodeByID(1)).statistic.printStatisticsPerNode();
		}
	}

	@Override
	public void onFatalErrorExit() {
		// TODO Auto-generated method stub
		super.onFatalErrorExit();
	}

	@Override
	public void preRun() {
		super.preRun();
		if (Tools.getNodeList().size() > 0) {
			// somente executo em pre-run se estiver em modo batch
			loadEdgeValues();
		}
	}

	@AbstractCustomGlobal.CustomButton(buttonText = "Read weigth edge", toolTipText = "Read from a file the weight of the edges")
	public void ButtonInportEdgeValues() {
		// JOptionPane.showMessageDialog(null, "You Pressed the 'GO' button.");

		loadEdgeValues();
	}

	@AbstractCustomGlobal.CustomButton(buttonText = "Export weigth edge", toolTipText = "Write in a file the weight of the edges")
	public void ButtonExportEdgeValues() {
		writeEdgeValues();
	}

	private void writeEdgeValues() {
		// chava o utilitário para criar um arquivo com os referentes pesos
		// das arestas

		EdgeUtility edgeUtil = new EdgeUtility(); // utilitario para exportar e
		edgeUtil.exportWrite(edgeSourcePath + idExecution + "_edge_"
				+ Tools.getNodeList().size() + ".txt");
	}

	private void loadEdgeValues() {
		// carrega valores pesos das arestas

		EdgeUtility edgeUtil = new EdgeUtility(); // utilitario para exportar e
		Tools.repaintGUI();
		Runtime.reevaluateConnections();

		edgeUtil.importRead(edgeSourcePath + idExecution + "_edge_"
				+ Tools.getNodeList().size() + ".txt");
	}

	@AbstractCustomGlobal.CustomButton(buttonText = "Import Traffic", toolTipText = "Read in a file with traffic")
	public void ButtonImportTraffic() {
		loadTraffic();
	}

	private void loadTraffic() {
		TrafficUtility tu = new TrafficUtility();

		tu.importRead(trafficSourcePath + idExecution + "_traffic_"
				+ Tools.getNodeList().size() + ".txt");
	}
	
	@AbstractCustomGlobal.CustomButton(buttonText = "Export Traffic", toolTipText = "Write in a file the traffic")
	public void ButtonExportTraffic() {
		writeTraffic();
	}

	private void writeTraffic() {
		TrafficUtility tu = new TrafficUtility();

		tu.exportWrite(trafficSourcePath + idExecution + "_traffic_"
				+ Tools.getNodeList().size() + ".txt");
		
	}

	@Override
	public void preRound() {
		// TODO Auto-generated method stub
		super.preRound();
	}

	@Override
	public void postRound() {
		// TODO Auto-generated method stub
		super.postRound();
	}

	@Override
	public void checkProjectRequirements() {
		super.checkProjectRequirements();
		try {
			idExecution = Configuration
					.getIntegerParameter("ConfSimulation/idExecution");
			
			metricStrategy = Configuration.getStringParameter("ConfSimulation/Metric/strategy");
			
		} catch (CorruptConfigurationEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void nodeAddedEvent(Node n) {
		// TODO Auto-generated method stub
		super.nodeAddedEvent(n);
	}

	@Override
	public void nodeRemovedEvent(Node n) {
		// TODO Auto-generated method stub
		super.nodeRemovedEvent(n);
	}

	@Override
	public void handleGlobalTimers() {
		// TODO Auto-generated method stub
		super.handleGlobalTimers();
	}

}
