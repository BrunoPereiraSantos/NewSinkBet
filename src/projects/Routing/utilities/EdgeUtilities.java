package projects.Routing.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Scanner;

import projects.Routing.nodes.edges.WeightEdge;
import sinalgo.io.positionFile.PositionFileIO.PositionFileException;
import sinalgo.nodes.Node;
import sinalgo.nodes.edges.Edge;
import sinalgo.tools.Tools;

public class EdgeUtilities implements UtilitiesInterface {

	private static final String separator = "#####----- start Edges -----#####";

	@Override
	public void importRead(String path) {
		InputStream is = null;
		int start;
		int end;
		float etx;
		float rate;
		float mtm;
		float ett;
		
		Tools.reevaluateConnections();
		
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
	}

	
	private static boolean insertValuesInEdge(int idStartNode, int idEndNode, float etx,
			float rate, float mtm, float ett) {
		
		Node startNode = Tools.getNodeByID(idStartNode);
		Node endNode = Tools.getNodeByID(idEndNode);
		
		if(!startNode.outgoingConnections.contains(startNode, endNode))
			return false;
		
		Iterator<Edge> it = startNode.outgoingConnections.iterator();
		WeightEdge e;
		
		while(it.hasNext()){
			e = (WeightEdge) it.next();
			
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
	
	@Override
	public void exportWrite(String path) {
		// TODO Auto-generated method stub

	}

}
