# DCDataBaseWithSpark
Java based project that utilzes [SparkSQL](https://spark.apache.org/sql/), [groot](https://github.com/gavalian/groot/), [clas12-offline software](https://github.com/JeffersonLab/clas12-offline-software), [Apache Commons](https://commons.apache.org/), [JDBC](https://en.wikipedia.org/wiki/JDBC_driver), Swing and AWT to provide a GUI for the user to visualize, categorize and document Drift Chamber (DC) wire faults for the CLAS12 detector

# Documentation
Check out the [Wiki](https://github.com/mkunkel/DCFaultFinder/wiki "DCFaultFinder Wiki")!

## The GUI consists of 6 panels
<img src="https://github.com/mckunkel/DCFaultFinder/blob/master/images/InitialStartUpLabeled.png" width="800">


- The top-left panel is a table that will contain possible wire faults
- The bottom-left panel is a control panel to cycle through the Sectors and Superlayers

- The top-middle panel is a table that will contain the wires to be sent to the MySQL database
- The bottom-middle panel is a control panel with 2 buttons
--

- The top-middle panel is a table that will containt possible wire faults
- The bottom-middle panel is a control panel to cycle through the Sectors and Superlayers

## Data Processing and Visualization
- Processing uses the clas12-offline-software HipoDataSource found in the COATJava package
- Visualization uses the groot package to create histograms of "Layer vs. Wire" for each segment of Sector and Superlayer. It should be noted that "Layer" in the graphical display is "Local Layer", i.e. for each Sector and Superlayer there are 6 "Local Layers" This can be seen on the right side panel of ###NEED IMAGE REFERENCE###

## Fault Categorization 
At the lowest level there is a A.I. that searches for dead wires according to a set value of percent efficiency given by the user. A generated table of possible dead wires will be initially displayed based upon this criterion. This table is the left side panel of the GUI. 


This is a maven project.
CLAS12 dependancies should be changed in the pom.xml file

This project creates a GUI that a user can use to interject broken wires into a MySQL and CCDB database.

The entrance point for this GUI is DCFaultFinderApp found under the package database.ui.
Maven will package a fat-jar wil all dependencies with mvn package is invoked. 
Be sure the CLAS12 COATJava dependency is set in the pom.xml file

Once packaged with maven
run this command in a terminal
java -jar DCFaultFinderApp-jar-with-dependencies.jar

If the user is not on the JLab cluster, the user has the option of just creating the scripts for the CCDB, dependent if the
user has a verison of MySQL built with user as root and null password

please email mkunkel@jlab.org for problems or feature requests


