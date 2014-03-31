#!/usr/bin/perl

$numRounds = 100; # number of rounds to perform per simulation
$numNodes = 9;
for($i=0; $i<1; $i+=1) {
system("java -cp binaries/bin sinalgo.Run " .
"-project ExportTopology " .             # choose the project
"-batch ".
"-rounds 1 ".
"-gen $numNodes ".
"DummyNode ".
"Random ".
"-overwrite " .                   # Overwrite configuration file parameters
"ConfigTest/idTopology=$i ".
#"PositionFile '('./position_9.pos')' " . # generate nodes
#"-overwrite " .                   # Overwrite configuration file parameters
#"mobility=false ".
#"interference=true ".
#"dimX=1000 dimY=1000 dimZ=0 ".
#"GeometricNodeCollection/rMax=100 ".
#"UDG/rMax=100 ".
#"Node/defaultSize=10 ".
#"exitAfter=true exitAfter/Rounds=$numRounds " . # number of rounds to perform & stop
#"exitOnTerminationInGUI=true " .  # Close GUI when hasTerminated() returns true
#"AutoStart=true " .               # Automatically start communication protocol
#"outputToConsole=false " .        # Create a framework log-file for each run
#"extendedControl=false " .        # Don't show the extended control in the GUI
#"-rounds $numRounds " .           # Number of rounds to start simulation
#"-refreshRate 1".               # Don't draw GUI often
"");
}
