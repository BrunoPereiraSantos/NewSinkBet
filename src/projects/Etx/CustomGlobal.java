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

import projects.Etx.nodes.edges.EdgeEtx;
import projects.Etx.nodes.nodeImplementations.NodeEtx;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.runtime.AbstractCustomGlobal;
import sinalgo.runtime.Runtime;
import sinalgo.tools.Tools;

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
		/*Random generator = new Random(1);
		while(it.hasNext()){
			n = it.next();
			//System.out.println(n);
			Iterator<Edge> it2 = n.outgoingConnections.iterator();
			EdgeWeightEtxBet e;
			while(it2.hasNext()){
				e = (EdgeWeightEtxBet) it2.next();
				//e.setEtx(UniformDistribution.nextUniform(0, 1));
				//e.setEtx(generator.nextDouble());
				e.setEtx(1+generator.nextInt(9));
				System.out.println("ID "+n.ID+" ~["+e.getEtx()+"]> "+e.endNode.ID);

			}
		}*/
		
		while(it.hasNext()){
			n = (NodeEtx) it.next();
			//System.out.println(n);
			Iterator<Edge> it2 = n.outgoingConnections.iterator();
			EdgeEtx e;
			while(it2.hasNext()){
				e = (EdgeEtx) it2.next();
				if(n.ID == 1){
					if(e.endNode.ID == 2)
						e.setEtx(.2f);
				}
				
				if(n.ID == 2){
					if(e.endNode.ID == 1)
						e.setEtx(.1f);
					if(e.endNode.ID == 3)
						e.setEtx(.1f);
					if(e.endNode.ID == 4)
						e.setEtx(.1f);
				}

				if(n.ID == 3){
					if(e.endNode.ID == 2)
						e.setEtx(.2f);
					if(e.endNode.ID == 5)
						e.setEtx(.1f);
					if(e.endNode.ID == 6)
						e.setEtx(.1f);
				}

				if(n.ID == 4){
					if(e.endNode.ID == 2)
						e.setEtx(.2f);
					if(e.endNode.ID == 5)
						e.setEtx(.1f);
				}

				if(n.ID == 5){
					if(e.endNode.ID == 3)
						e.setEtx(.2f);
					if(e.endNode.ID == 4)
						e.setEtx(.2f);
					if(e.endNode.ID == 7)
						e.setEtx(.1f);
					if(e.endNode.ID == 8)
						e.setEtx(.1f);
				}

				if(n.ID == 6){
					if(e.endNode.ID == 3)
						e.setEtx(.9f);
					if(e.endNode.ID == 7)
						e.setEtx(.1f);
				}

				if(n.ID == 7){
					if(e.endNode.ID == 5)
						e.setEtx(.2f);
					if(e.endNode.ID == 6)
						e.setEtx(.1f);
					if(e.endNode.ID == 8)
						e.setEtx(.2f);
				}

				if(n.ID == 8){
					if(e.endNode.ID == 5)
						e.setEtx(.4f);
					if(e.endNode.ID == 7)
						e.setEtx(.2f);
					if(e.endNode.ID == 9)
						e.setEtx(.2f);
				}

				if(n.ID == 9){
					if(e.endNode.ID == 8)
						e.setEtx(.1f);
				}
				
				//e.setEtx(1+generator.nextInt(9));
				System.out.println("ID "+ e.getID()+"      " +n.ID+" ~["+e.getEtx()+"]> "+e.endNode.ID);

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
