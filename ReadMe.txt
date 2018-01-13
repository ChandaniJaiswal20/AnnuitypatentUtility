			++ READ ME ++
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

Issue: Patent Annuity user documents that have not been Approved by the Examiners. Hence the affected Patent's status have moved from 'Granted' to 'To Be lapsed', although annuities were received for them.

++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

Execution steps:

This utility identifies all such patent's in status 'To be lapsed' for which User Document of type Annuities have been received but are in Pending status. 
It executes a special action to move the Patent's status back to Granted and also performs Approval action on the related annuity user documents.

++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

Prerequisites:

Jdk(Java version 1.6.*) should be installed in the running system.

The following things needs to be configured in the run.bat files

Set JAVA_HOME in environment variables to point to the jdk directory.

++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
PatentAnnuityUtility_MK folder should contains following folders/files:

1)patentAnnuitityConfig.properties		(file)
2)lib      					(folder)
3)annuityPatentUtility.jar	(file)
4)ReadMe.txt				(file)
5)run.bat					(file)
6)log4j.properties 			(file)
7) log						(folder that gets generated on running the utility)



'patentAnnuitityConfig.properties' files contains properties of database and configuration details of Actions and Statuses required for running this program.
The following things needs to be configured in the patentAnnuitityConfig.properties file

Set iiopHost with the IP address of the Glassfish domain server.
Set iiopPort with the IIOP port of the Glassfish domain server.
Set databaseHost with the IP Address of the Database of IPAS.
Set databasePort with the port of the Database of IPAS.
Set the Database connection details against databaseName, userName and password.

Set the configuration details of actions and statuses and application types as described in the 'patentAnnuitityConfig.properties' file.
'lib' folder contains all jar files that is required for this program.

'annuityPatentUtility.jar' is main executable jar.

'ReadMe' file contains the instructions to run this program.

'run.bat' file contains script to execute this program.


++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
HOW TO RUN PROGRAM?
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

1) Open 'patentAnnuitityConfig.properties' file and change properties of the attributes and save it.
2) Double click on 'run.bat' to start program.













