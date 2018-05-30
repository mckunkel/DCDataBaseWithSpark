
# DCDataBaseWithSpark
Java based project that utilzes [SparkSQL](https://spark.apache.org/sql/), [groot](https://github.com/gavalian/groot/), [clas12-offline software](https://github.com/JeffersonLab/clas12-offline-software), [Apache Commons](https://commons.apache.org/), [JDBC](https://en.wikipedia.org/wiki/JDBC_driver), Swing and AWT to provide a GUI for the user to visualize, categorize and document Drift Chamber (DC) wire faults for the CLAS12 detector
### Available for Linux and MacOSX 
#### Runs on Jlab computing farm cluster
# Quick-Start and Reference 
## To Build
* This is a maven project.
* CLAS12 dependancies should be changed in the pom.xml file
* As of writing this, the CLAS12 maven repository was up to date. However there was no software management tool available to ensure this projects CLAS12 dependencies stayed upto date. Therefore it is recommended that the pom.mxl file be changed to point to the latest CLAS12 COATJava needed to analyze data if CLAS12 does not update their Maven repository. See [class12-offline-software](https://github.com/mckunkel/clas12-offline-software) for instructions on how to build the COATJava libraries.
1. Clone the repository 
2. In the directory FaultFinder 
    1. Update pom.xml to latest COATJava
    2. Execute the buildPackage.pl to build the package on the JLab cluster
        1. ```
           ./buildPackage.pl
           ```
    3. Execute the runDCFaultFinder.pl to run the GUI
        1. ```
            ./runDCFaultFinder.pl
           ```

* If you are building the clas12-offline-software from source, the following is needed
* Once COATJava is built
* Run this command in the clas12-offline-software directory to install COATJava into your .m2 directory
```
mvn install:install-file -Dfile=coatjava/lib/clas/coat-libs-5.1-SNAPSHOT.jar -DgroupId=org.jlab.coat -DartifactId=coat-libs -Dversion=5.1-SNAPSHOT -Dpackaging=jar 
```
* Remove these lines from the pom.xml file located in the FaultFinder directory
```
<repositories>
<repository>
<id>clas12maven</id>
<url>https://clasweb.jlab.org/clas12maven</url>
</repository>
</repositories>
```
* Rerun step 2

<img src="https://github.com/mckunkel/DCFaultFinder/blob/master/images/LabeledProcessedPlot.png" width="800">

* If this program is to ran NOT on the Jlab cluster, please ensure that the MySQL database from JLab 

## Data Processing and Visualization
* Processing uses the clas12-offline-software HipoDataSource found in the COATJava package
* Visualization uses the groot package to create histograms of "Layer vs. Wire" for each segment of Sector and Superlayer. It should be noted that "Layer" in the graphical display is "Local Layer", i.e. for each Sector and Superlayer there are 6 "Local Layers" This can be seen on the Top-Right panel of the figure.


## Fault Categorization 
* At the lowest level there is a A.I. that searches for dead wires according to a set value of percent efficiency given by the user. A generated table of possible dead wires will be initially displayed based upon this criterion. This table is the left side panel of the GUI. 
The user is responsible for ensuring the proper fault is selected for the given section of faults displayed on the "Histogram Panel". 
* To aide the user, there is a "Type of Fault" panel (Bottom-Right). 
* Once a fault is selected, the user can place the mouse over the provided histogram to see what that specific fault would appear to be.
  * Double click the histogram panel, with the proper fault and place the selected wires into the "SQL Query" panel (Top-Middle)

## Fault Documentation
* Once a run's entire sector and superlayer has been investigated for possible faults and those possible faults have been proper categorized and placed into the "SQL Query" panel, the entries should be placed into the MySQL database but clicking the button located in the "Send to Database" panel (Bottom-Middle)
  * To place faults into the CCDB, faults must be placed into the MySQL database first.
* Placing entries into the CCDB can be done only after faults into MySQL has been pushed into the database.
  * If entires are in the MySQL database, and the user does not want to process data, just the CCDB entry list, the "Send to CCDB" can be used

# Documentation
Check out the [Wiki](https://github.com/mckunkel/DCFaultFinder/wiki "DCFaultFinder Wiki")!

please email mkunkel@jlab.org for problems or feature requests


