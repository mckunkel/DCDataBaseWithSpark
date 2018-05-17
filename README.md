# DCDataBaseWithSpark
Java based project that utilzes SparkSQL, GROOT, Apache Commons, JBDC, Swing and AWT to provide a GUI for the user to visualize, categorize and document Drift Chamber (DC) wire faults.



perform the querying and updating on CLAS12 MySQL database for broken wires found in the Drift Chambers (DC). 


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


