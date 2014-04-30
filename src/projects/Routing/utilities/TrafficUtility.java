package projects.Routing.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import projects.Routing.nodes.nodeImplementations.AbstractRoutingNode;
import sinalgo.io.positionFile.PositionFileIO.PositionFileException;
import sinalgo.tools.Tools;

public class TrafficUtility implements UtilityInterface {
	private static final String separator = "#####----- start Traffic Nodes -----#####";
	
	
	@Override
	public void importRead(String path) {
		InputStream is = null;
		try {
			is = new FileInputStream(path);
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
				((AbstractRoutingNode) Tools.getNodeByID(n)).setTraffic(60.0, shots);
				
				//setTrafficToNode((InterfaceRequiredMethods) Tools.getNodeByID(n), 60, shots);
			} catch(NumberFormatException e) {
				throw new PositionFileException("Illegal line: expected two ints, separated by comma. Found \n" + line);
			}
			
		}

	}

	@Override
	public void exportWrite(String path) {
		// TODO Auto-generated method stub

	}

}
