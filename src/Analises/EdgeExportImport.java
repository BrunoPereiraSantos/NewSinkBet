package Analises;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFileChooser;

import projects.defaultProject.nodes.edges.GenericWeightedEdge;
import projects.defaultProject.nodes.nodeImplementations.DummyNode;
import sinalgo.Run;
import sinalgo.configuration.AppConfig;
import sinalgo.configuration.Configuration;
import sinalgo.configuration.CorruptConfigurationEntryException;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.io.eps.Exporter.PositionFileFilter;
import sinalgo.io.eps.Exporter.SingleFileFilter;
import sinalgo.io.positionFile.PositionFileIO;
import sinalgo.io.positionFile.PositionFileIO.PositionFileException;
import sinalgo.nodes.Node;
import sinalgo.nodes.Position;
import sinalgo.nodes.edges.Edge;
import sinalgo.nodes.messages.Inbox;
import sinalgo.runtime.Runtime;
import sinalgo.tools.Tools;
import sinalgo.tools.statistics.Distribution;
import sinalgo.tools.statistics.PoissonDistribution;
import sinalgo.tools.statistics.UniformDistribution;

public class EdgeExportImport {
	private static final String separator = "#####----- start Edges -----#####";

	
	
	public static boolean readEdges(String name) {
		InputStream is = null;
		int start;
		int end;
		float etx;
		float rate;
		float mtm;
		float ett;
		
		Tools.reevaluateConnections();
		
		try {
			is = new FileInputStream(name);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    InputStreamReader isr = new InputStreamReader(is);
		Scanner scan = new Scanner(is);
		
		// skip the first lines
		String numNodes = scan.nextLine();
		while(numNodes != null && !numNodes.equals(separator)) {
			numNodes = scan.nextLine();
		}
		
		while(scan.hasNext()){
			String line = scan.nextLine();
			if(line == null) {
				throw new PositionFileException("The specified file contains not enough informations");	
			}
			
			try {
				String[] parts = line.split(" ");
				if(parts.length < 6) {
					throw new PositionFileException("Illegal line: expected six floats, separated by tab. Found \n" + line);	
				}
				start = Integer.parseInt(parts[0]);
				end = Integer.parseInt(parts[1]);
				etx = Float.parseFloat(parts[2]);
				rate = Float.parseFloat(parts[3]);
				mtm = Float.parseFloat(parts[4]);
				ett = Float.parseFloat(parts[5]);
				
				if( !insertValuesInEdge(start, end, etx, rate, mtm, ett) ) {
					throw new PositionFileException("Illegal line: not fond start or end nodes for insert edge values. \n" + line);
				}
				
			} catch(NumberFormatException e) {
				throw new PositionFileException("Illegal line: expected two ints, separated by comma. Found \n" + line);
			}
			
		}
			
		return false;
	}

	private static boolean insertValuesInEdge(int idStartNode, int idEndNode, float etx,
			float rate, float mtm, float ett) {
		
		Node startNode = Tools.getNodeByID(idStartNode);
		Node endNode = Tools.getNodeByID(idEndNode);
		
		if(!startNode.outgoingConnections.contains(startNode, endNode))
			return false;
		
		Iterator<Edge> it = startNode.outgoingConnections.iterator();
		GenericWeightedEdge e;
		
		while(it.hasNext()){
			e = (GenericWeightedEdge) it.next();
			
			if(e.endNode.equals(endNode)){
				e.setEtt(ett);
				e.setEtx(etx);
				e.setMtm(mtm);
				e.setRate(rate);
				return true;
			}
		}
		
		return false;
	}

	public static boolean writeEdges(int numNodes, int idTopology) {
		
		try {
			PrintStream ps = new PrintStream("./Topology/Edges/"+idTopology+"_edge_"+numNodes+".txt");
			ps.println("Number of nodes: " + numNodes);
			Configuration.printConfiguration(ps);
			ps.println(separator);
			
			
			Iterator<Node> it = Tools.getNodeList().iterator();
			Iterator<Edge> eIt;
			Edge e;
 			Node n;
			while(it.hasNext()){
				n = it.next();
				eIt = n.outgoingConnections.iterator();
				while(eIt.hasNext()){
					e = eIt.next();
					
					ps.println(((GenericWeightedEdge) e).printEdgeInformation());
				}
			}
			/*for(Node n : Tools.getNodeList()) {
				Position p = n.getPosition();
				ps.println(p.xCoord + ", " + p.yCoord + ", " + p.zCoord);
			}*/
			
			
			ps.close();
			
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Tools.minorError(e.getMessage());
		}
		
		

		System.out.println("------------------------------------------------------");
		System.out.println("End ConfigTest");
		System.out.println("------------------------------------------------------");
		return false;
		
	}
	
}
