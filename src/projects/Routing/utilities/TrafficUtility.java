package projects.Routing.utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import projects.Routing.nodes.nodeImplementations.AbstractRoutingNode;
import sinalgo.configuration.Configuration;
import sinalgo.configuration.CorruptConfigurationEntryException;
import sinalgo.io.positionFile.PositionFileIO.PositionFileException;
import sinalgo.tools.Tools;
import sinalgo.tools.statistics.Distribution;
import sinalgo.tools.statistics.PoissonDistribution;
import sinalgo.tools.statistics.UniformDistribution;

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
		while (numNodes != null && !numNodes.equals(separator)) {
			numNodes = scan.nextLine();
		}

		while (scan.hasNext()) {
			String line = scan.nextLine();
			if (line == null) {
				throw new PositionFileException(
						"The specified file contains not enough informations");
			}

			try {
				String[] parts = line.split(" ");
				if (parts.length < 2) {
					throw new PositionFileException(
							"Illegal line: expected two ints, separated by space. Found \n"
									+ line);
				}
				int nId = Integer.parseInt(parts[0]);
				int shots = Integer.parseInt(parts[1]);

				((AbstractRoutingNode) Tools.getNodeByID(nId)).setTraffic(60.0,
						shots);

				// setTrafficToNode((InterfaceRequiredMethods)
				// Tools.getNodeByID(n), 60, shots);
			} catch (NumberFormatException e) {
				throw new PositionFileException(
						"Illegal line: expected two ints, separated by comma. Found \n"
								+ line);
			}

		}

	}

	@Override
	public void exportWrite(String path) {
		double percent = 0;
		int qntNodeEv = 0;
		double lambda = 0;
		Distribution distTraffic = null;

		System.out.println("----------------------------------");
		System.out.println("ConfSimulation");
		System.out.println("----------------------------------");

		try {
			// Ler os parametros do xml de configuração e inicializa a
			// quantidade de nodos que irão enviar eventos

			percent = Configuration
					.getDoubleParameter("ConfSimulation/traffic/percentNodes");
			qntNodeEv = (int) (Tools.getNodeList().size() * percent) / 100;

			lambda = Configuration
					.getDoubleParameter("ConfSimulation/traffic/lambda");

			distTraffic = new PoissonDistribution(lambda);

			System.out.println("ConfSimulation/traffic/percentNodes = "
					+ percent);
			System.out.println("qntNodeEv = " + qntNodeEv);
			System.out.println("ConfSimulation/traffic/lambda = " + lambda);

		} catch (CorruptConfigurationEntryException e) {
			e.printStackTrace();
		}

		try {
			// imprime em um arquivo quais nodos seu
			// respectivo número de tiros seguindo
			// uma distribuição de poisson

			PrintStream ps = new PrintStream(path);
			ps.println("Number of nodes: " + Tools.getNodeList().size());
			Configuration.printConfiguration(ps);
			ps.println(separator);

			// id = 1 é o root e não deve ser selecionado
			UniformDistribution ud = new UniformDistribution(2, Tools
					.getNodeList().size());
			Set<Integer> setNodes = new HashSet<Integer>();

			while (setNodes.size() <= qntNodeEv) {
				setNodes.add((int) ud.nextSample());
			}

			Iterator<Integer> it = setNodes.iterator();
			while (it.hasNext()) {
				ps.println(it.next() + " " + (int) distTraffic.nextSample());
			}
			/*
			 * for(Node n : Tools.getNodeList()) { Position p = n.getPosition();
			 * ps.println(p.xCoord + ", " + p.yCoord + ", " + p.zCoord); }
			 */

			ps.close();

		} catch (FileNotFoundException e) {
			Tools.minorError(e.getMessage());
		}

		System.out.println("----------------------------------");
		System.out.println("End ConfSimulation");
		System.out.println("----------------------------------");

	}

}
