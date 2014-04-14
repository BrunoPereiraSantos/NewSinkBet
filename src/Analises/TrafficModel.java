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
import sinalgo.nodes.messages.Inbox;
import sinalgo.runtime.Runtime;
import sinalgo.tools.Tools;
import sinalgo.tools.statistics.Distribution;
import sinalgo.tools.statistics.PoissonDistribution;
import sinalgo.tools.statistics.UniformDistribution;

public class TrafficModel {
	private static final String separator = "#####----- start Traffic Nodes -----#####";

	
	
	public static boolean readEvents(String name) {
		InputStream is = null;
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
				if(parts.length < 2) {
					throw new PositionFileException("Illegal line: expected two ints, separated by space. Found \n" + line);	
				}
				int n = Integer.parseInt(parts[0]);
				int shots = Integer.parseInt(parts[1]);
				setTrafficToNode((InterfaceEventTest) Tools.getNodeByID(n), 60, shots);
			} catch(NumberFormatException e) {
				throw new PositionFileException("Illegal line: expected two ints, separated by comma. Found \n" + line);
			}
			
		}
			
		
		/*try {
			String line = reader.readLine();
			if(line == null) {
				throw new PositionFileException("The specified file contains not enough informations");	
			}
			try {
				//while(line != null){
					String[] parts = line.split(" ");
					if(parts.length < 2) {
						throw new PositionFileException("Illegal line: expected two ints, separated by space. Found \n" + line);	
					}
					int n = Integer.getInteger(parts[0]);
					int shots = Integer.getInteger(parts[1]);
					setTrafficToNode((InterfaceEventTest) Tools.getNodeByID(n), 60, shots);
					//return new Position(x,y,z);
				//}
			} catch(NumberFormatException e) {
				throw new PositionFileException("Illegal line: expected three doubles, separated by comma. Found \n" + line);
			}
		} catch (IOException e) {
			throw new PositionFileException(e.getMessage());
		}*/
		
		return false;
	}

	public static boolean writeEvents(int numNodes, int idTraffic) {
		double percent = 0;	
		int qntNodeEv = 0;
		double lambda = 0;
		Distribution distTraffic = null;
		
		System.out.println("------------------------------------------------------");
		System.out.println("ConfigTest");
		System.out.println("------------------------------------------------------");
		try {
			percent = Configuration.getDoubleParameter("ConfigTest/NodeEvents/percent");
			qntNodeEv = (int) (numNodes * percent) / 100;
			
			System.out.println("ConfigTest/NodeEvents/percent = "+percent);
			System.out.println("qntNodeEv = "+qntNodeEv);
		} catch (CorruptConfigurationEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			lambda = Configuration.getDoubleParameter("ConfigTest/Traffic/lambda");
			distTraffic = new PoissonDistribution(lambda);
		} catch (CorruptConfigurationEntryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			PrintStream ps = new PrintStream("./Traffic/"+idTraffic+"_traffic_"+numNodes+".txt");
			ps.println("Number of nodes: " + numNodes);
			Configuration.printConfiguration(ps);
			ps.println(separator);
			
			UniformDistribution ud = new UniformDistribution(2, numNodes);
			Set<Integer> setNodes = new HashSet<Integer>();
			
			while(setNodes.size() <= qntNodeEv){
				setNodes.add((int) ud.nextSample());
			}
			
			Iterator<Integer> it = setNodes.iterator();
			while(it.hasNext()){
				ps.println(it.next()+" "+(int)distTraffic.nextSample());
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
	
	public static void setTrafficToRangeHops(int minHop, int maxHop) {
		if (minHop < 1 || maxHop < 1 || maxHop < minHop)
			return;
		
		Iterator<Node> it = Tools.getNodeList().iterator();
		InterfaceEventTest n;
		Node node;
		while (it.hasNext()) {
			n = (InterfaceEventTest) it.next();
			node = (Node) n;
			
			if (minHop <= n.getHops() && maxHop >= n.getHops()) {
				setTrafficToNode(n, 60, 10);
			}
		}
	}
	
	public static void setTrafficToRangeHops(int hop){
		setTrafficToRangeHops(hop, hop);
	}
	
	public static void setTrafficToNode(InterfaceEventTest n, double duration,
			int shots) {

		double interval = duration / shots;

		// uniforme distribuição para escolher um tempo de 0 a 10 minutos para
		// iniciar a sequencia de eventos daquele nodo
		// UniformDistribution ud = new UniformDistribution(0, 5);
		// double time = interval + (((int)ud.nextSample()) * 60);
		double time = 1;

		//System.out.println("########## shots=" + shots);
		//System.out.println("########## time=" + time);

		while (shots > 0) {
			n.sentEventRelative(time);
			time += interval;
			shots -= 1;
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
