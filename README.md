# car-simulation

This repo contains sources(cloned) for the OpenDS car simulation software. The eclipse project and Eclipse specific files are not tracked. Eclipse project files can be downloaded: https://java.net/projects/opends/pages/2_4_BuildingAndRunningOpenDSWithinEclipse. 

**Assets**

The directory assets is not tracked in it self, instead assets.zip is used. To use this file a GIT extension is needed: https://git-lfs.github.com. Install and follow instructions on usage.

 **Jar file**
 
 A OpenDS.jar file has been added which will let you execute the simulation software standalone. Download the jar file and the assets.zip file. Place the jar file one level up from the unzipped assets directory and click on OpenDS.jar.
 
 **Set**
 
  Modifications to openDS has been made to support Set, i.e setting certain selected car signals. We have added "SetValue" as part of the xml command parsing and are listening for this in the 
  ```
  eu.opends.settingsController.ConnectionHandler.parseXML 
  ``` 
 method. A setValue method was added in the 
 ``` 
 eu.opends.settingsContoller.APIData
 ```
 class.  In our example we have been using handbrake toggle and cruise control toggle signals.
