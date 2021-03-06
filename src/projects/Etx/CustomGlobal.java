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
package projects.Etx;


import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;

import Analises.EdgeExportImport;
import Analises.InterfaceRequiredMethods;
import Analises.TrafficExportImport;
import projects.Etx.nodes.edges.EdgeEtx;
import projects.Etx.nodes.nodeImplementations.NodeEtx;
import projects.defaultProject.nodes.edges.GenericWeightedEdge;
import sinalgo.configuration.Configuration;
import sinalgo.configuration.CorruptConfigurationEntryException;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.runtime.AbstractCustomGlobal;
import sinalgo.runtime.Runtime;
import sinalgo.tools.Tools;
import sinalgo.tools.logging.Logging;

/**
 * This class holds customized global state and methods for the framework. 
 * The only mandatory method to overwrite is 
 * <code>hasTerminated</code>
 * <br>
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
 * @see sinalgo.runtime.AbstractCustomGlobal for more details.
 * <br>
 * In addition, this class also provides the possibility to extend the framework with
 * custom methods that can be called either through the menu or via a button that is
 * added to the GUI. 
 */
public class CustomGlobal extends AbstractCustomGlobal{
	
	private int id_execution = 0;
	private Logging logExecution;
	private Logging logEnergy;
	
	boolean exec1xTraffic = true;
	boolean exec1xLog = true;

	@Override
	public void handleEmptyEventQueue() {

		// TODO Auto-generated method stub
		super.handleEmptyEventQueue();
		if (exec1xTraffic) {
			
			TrafficExportImport.changeReabilityModel();
			
			TrafficExportImport.readEvents("./Traffic/" + id_execution + "_traffic_"+ Tools.getNodeList().size() + ".txt");
			//TrafficModel.setTrafficToRangeHops(2, 3);
			exec1xTraffic = false;
		}else if (exec1xLog){
			printStatistics();
			exec1xLog = false;
		}
	}

	private void printStatistics() {
		
		logExecution = Logging.getLogger(Tools.getProjectName()+"_logExecution_"
				+ Tools.getNodeList().size() + ".txt", true);
		logEnergy = Logging.getLogger(Tools.getProjectName()+"_logEnergy_"
				+ Tools.getNodeList().size() + ".txt", true);
		String separator = "##############----- start new simulation -----##############";
		
		
		InterfaceRequiredMethods in = (InterfaceRequiredMethods) Tools.getNodeByID(1);
		
		System.out.println(in.getStatisticNode().toString());
		logExecution.logln(in.getStatisticNode().toString());
		logExecution.logln(separator);
		
		System.out.println(in.getStatisticNode().printStatisticsPerNode());
		logEnergy.logln(in.getStatisticNode().printStatisticsPerNode());
		logEnergy.logln(separator);
		/*Iterator<Node> it = Tools.getNodeList().iterator();
		Node n;
		InterfaceEventTest in;
		while (it.hasNext()) {
			n = it.next();
			in = (InterfaceEventTest) n;
			System.out.println("Id=" + n.ID + " "
					+ in.getStatisticNode().toString());
			

			executionLog.logln("Id=" + n.ID + " "
					+ in.getStatisticNode().toString());
		}*/

	}
	
	@Override
	public void preRun() {
		// TODO Auto-generated method stub
		super.preRun();
		// printGraphicsINGuI();
		// Runtime.reevaluateConnections();
		EdgeExportImport.readEdges("./Topology/Edges/" + id_execution + "_edge_"+ Tools.getNodeList().size() + ".txt");
		//insertEtx();

		// tc.installEvents();
		// tc.runTree();
	}
	
	@Override
	public void checkProjectRequirements() {
		// TODO Auto-generated method stub
		super.checkProjectRequirements();

		try {
			id_execution = Configuration.getIntegerParameter("ConfigTest/ID");
		} catch (CorruptConfigurationEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/* (non-Javadoc)
	 * @see runtime.AbstractCustomGlobal#hasTerminated()
	 */
	public boolean hasTerminated() {
		return false;
	}

	/**
	 * An example of a method that will be available through the menu of the GUI.
	 */
	@AbstractCustomGlobal.GlobalMethod(menuText="Echo")
	public void echo() {
		// Query the user for an input
		String answer = JOptionPane.showInputDialog(null, "This is an example.\nType in any text to echo.");
		// Show an information message 
		JOptionPane.showMessageDialog(null, "You typed '" + answer + "'", "Example Echo", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Dummy button to create a tree.  
	 */
	@AbstractCustomGlobal.CustomButton(buttonText="Etx", toolTipText="Insert Etx")
	public void Button() {
		//int numNodes = Integer.parseInt(Tools.showQueryDialog("Number of nodes:"));
		//int fanOut = Integer.parseInt(Tools.showQueryDialog("Max fanout:"));
		//buildTree(fanOut, numLeaves);
		insertEtx(); 
	}
	
	public void insertEtx(){
		Iterator<Node> it = Runtime.nodes.iterator();
		NodeEtx n;
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
			n = (NodeEtx) it.next();
			// System.out.println(n);
			Iterator<Edge> it2 = n.outgoingConnections.iterator();
			GenericWeightedEdge e;
			while (it2.hasNext()) {
				e = (GenericWeightedEdge) it2.next();
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
	@AbstractCustomGlobal.CustomButton(buttonText="Graphics", toolTipText="Show Graphics")
	public void Button3() {
		//int numNodes = Integer.parseInt(Tools.showQueryDialog("Number of nodes:"));
		//int fanOut = Integer.parseInt(Tools.showQueryDialog("Max fanout:"));
		//buildTree(fanOut, numLeaves);
		printGraphicsINGuI();
		Runtime.reevaluateConnections();
		insertEtx();
	}
	
	public void printGraphicsINGuI(){
		Vector<NodeEtx> myNodes = new Vector<NodeEtx>();
		
		NodeEtx n = new NodeEtx();
		n.setPosition(300, 500, 0);
		n.finishInitializationWithDefaultModels(true);
		myNodes.add(n);
		
		n = new NodeEtx();
		n.setPosition(350, 500, 0);
		n.finishInitializationWithDefaultModels(true);
		myNodes.add(n);
		
		
		n = new NodeEtx();
		n.setPosition(400, 450, 0);
		n.finishInitializationWithDefaultModels(true);
		myNodes.add(n);
		
		n = new NodeEtx();
		n.setPosition(400, 550, 0);
		n.finishInitializationWithDefaultModels(true);
		myNodes.add(n);
		
		n = new NodeEtx();
		n.setPosition(450, 500, 0);
		n.finishInitializationWithDefaultModels(true);
		myNodes.add(n);
		
		n = new NodeEtx();
		n.setPosition(450, 400, 0);
		n.finishInitializationWithDefaultModels(true);
		myNodes.add(n);
		
		n = new NodeEtx();
		n.setPosition(500, 450, 0);
		n.finishInitializationWithDefaultModels(true);
		myNodes.add(n);
		
		
		n = new NodeEtx();
		n.setPosition(500, 500, 0);
		n.finishInitializationWithDefaultModels(true);
		myNodes.add(n);
		
		n = new NodeEtx();
		n.setPosition(550, 550, 0);
		n.finishInitializationWithDefaultModels(true);
		myNodes.add(n);
		// Repaint the GUI as we have added some nodes
		Tools.repaintGUI();
		
	}
	
	
}
