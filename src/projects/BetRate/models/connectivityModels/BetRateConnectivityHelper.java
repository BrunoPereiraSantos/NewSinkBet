package projects.BetRate.models.connectivityModels;

import sinalgo.configuration.Configuration;
import sinalgo.configuration.CorruptConfigurationEntryException;
import sinalgo.models.ConnectivityModelHelper;
import sinalgo.nodes.Node;
import sinalgo.nodes.Position;
import sinalgo.runtime.Global;
import sinalgo.runtime.Main;

public class BetRateConnectivityHelper extends ConnectivityModelHelper {
private double squareRadius;
	
	@Override
	protected boolean isConnected(Node from, Node to) {
		Position p1 = from.getPosition();
		Position p2 = to.getPosition();
		
		double distance = p1.squareDistanceTo(p2);
		
		System.out.println("distancia: "+Math.sqrt(distance));
		return (distance < squareRadius);
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// Code to initialize the static variables of this class 
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -   
	
	private static boolean initialized = false; // indicates whether the static fields of this class have already been initialized 
	private static double rMaxSquare; // we reuse the rMax value from the GeometricNodeCollection.  
	
	/**
	 * @return The maximum transmission range of this UDG model.
	 */
	public double getMaxTransmissionRange() {
		return Math.sqrt(squareRadius);
	}
	
	/**
	 * Sets the maximum transmission range of this UDG model.
	 * @param rMax The new max. transmission range.
	 */
	public void setMaxTransmissionRange(double rMax) {
		squareRadius = rMax * rMax;
	}
	
	public BetRateConnectivityHelper(double rMax) {
		squareRadius = rMax * rMax;
	}
	
	/**
	 * The default constructor for this class.  
	 * 
	 * The first time this constructor is called, it initializes the static parameters of this class. 
	 * @throws CorruptConfigurationEntryException If one of the initialization steps fails.
	 */
	public BetRateConnectivityHelper() throws CorruptConfigurationEntryException {
		if(! initialized) {
			double geomNodeRMax = Configuration.getDoubleParameter("GeometricNodeCollection/rMax");
			try {
				rMaxSquare = Configuration.getDoubleParameter("UDG/rMax");
			} catch(CorruptConfigurationEntryException e) {
				Global.log.logln("\nWARNING: There is no entry 'UDG/rMax' in the XML configuration file. This entry specifies the max. transmission range for the UDG connectivity model.\nThe simulation now uses GeometricNodeCollection/rMax instead.\n");
				rMaxSquare = geomNodeRMax;
			}
			if(rMaxSquare > geomNodeRMax) { // dangerous! This is probably not what the user wants!
				Main.minorError("WARNING: The maximum transmission range used for the UDG connectivity model is larger than the maximum transmission range specified for the GeometricNodeCollection.\nAs a result, not all connections will be found! Either fix the problem in the project-specific configuration file or the '-overwrite' command line argument.");
			}
			
			rMaxSquare = rMaxSquare * rMaxSquare;
	
			initialized = true;
		}
		squareRadius = rMaxSquare;
	}

}
