#!/bin/csh
source /group/clas12/gemc/environment.csh 4a.2.3
setenv CCDB_CONNECTION mysql://clas12writer:geom3try@clasdb.jlab.org/clas12
ccdb add calibration/dc/tracking/wire_status -r 3432-2147483647 Run_3432.txt #Adding run 3432
