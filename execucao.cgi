#!/usr/bin/perl

$numRounds = 1000000000; # number of rounds to perform per simulation
$numNodes = 100;
$Version = 'Hop';


#for($numNodes=200; $numNodes<=200; $numNodes+=100) {
system("java -cp binaries/bin sinalgo.Run " .
"-project $Version " .             # choose the project
"-batch ".
"-gen $numNodes ".
"$Version:Node$Version ".
"PositionFile '('./Topology/0_topology_$numNodes.pos')' " . # generate nodes
"-overwrite " .                   # Overwrite configuration file parameters
"ConfigTest/ID=0 ".
#"mobility=false ".
#"interference=true ".
"dimX=2400 dimY=2400 dimZ=0 ".
"GeometricNodeCollection/rMax=796 ".
"UDG/rMax=796 ".
#"Node/defaultSize=15 ".
#"exitAfter=true exitAfter/Rounds=$numRounds " . # number of rounds to perform & stop
"exitOnTerminationInGUI=true " .  # Close GUI when hasTerminated() returns true
"AutoStart=true " .               # Automatically start communication protocol
#"outputToConsole=false " .        # Create a framework log-file for each run
#"extendedControl=false " .        # Don't show the extended control in the GUI
"-rounds $numRounds " .           # Number of rounds to start simulation
"-refreshRate 10000".               # Don't draw GUI often
"");
#}
