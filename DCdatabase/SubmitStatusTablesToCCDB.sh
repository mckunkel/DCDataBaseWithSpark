#!/bin/csh
source /group/clas12/gemc/environment.csh
setenv CCDB_CONNECTION mysql://clas12writer:geom3try@clasdb.jlab.org/clas12
ccdb add calibration/dc/tracking/wire_status -r 3105-2147483647 Run_3105.txt #Adding run 3105
