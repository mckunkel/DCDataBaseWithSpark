#!/bin/csh
source /group/clas12/gemc/environment.csh 4a.2.3
setenv CCDB_CONNECTION mysql://clas12writer:geom3try@clasdb.jlab.org/clas12
ccdb add calibration/dc/tracking/wire_status -r 3923-2147483647 Run_3923.txt #Adding run 3923
