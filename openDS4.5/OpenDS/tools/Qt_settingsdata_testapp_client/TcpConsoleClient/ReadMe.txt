MIT License

Copyright (c) [2017] [Johan Strand]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

This application was created using Genivi development platform (gdp-sdk-raspberrypi2-201610271344).
Development environment: Qt creator version 4.0.1 based on Qt 5.6.1 (GCC 4.9.1)

Note that this is an test application only and are missing vital functionality to be user friendly and easely configurable. 
To be able to use the application it has to be built with correct settings for server IP and port. 

Getting started
*Download Genivi development platform (https://at.projects.genivi.org/wiki/pages/viewpage.action?pageId=11573902)
*Download and install Git Large File System (version 1.5.5 confirmed working): https://git-lfs.github.com/
*Start Git Bash
	-Init Git LFS (large file systen): $git lfs install
	-Download the Car-simulation repo to a workspace of your choice: git clone https://github.com/PeterWinzell/car-simulation.git
	-Go to you new Git repo and go to folder: ~\car-simulation\openDS4.5\OpenDS\. You should have a "assets.zip" file with a 
	 size of around 4-500MB in \OpenDS\, unzip this file in the same folder as it is located.
	 if the size of assets.zip is very small (like 1kb), then its most likely an issue with Git LFS system, perhaps you forgot to initiate?
*Start Qt creator and open the project "~/car-simulation/openDS4.5/OpenDS/tools/Qt_settingsdata_testapp_client/TcpConsoleClient"
*Change in main which function you want to call in clientest.cpp
*Start your OpenDS application (the setting server will automatically start when you start a driving session)
*Change the IP-address in the function you choose to run to the IP on which the OpenDS server is running 
 (if you changed the port from default 5678 then you also have to update this)
*Build the Qt application for either PC or raspberry PI and run it
*If successfull you should see some debug information about connection status and values from the simulation server in a console window or on
your raspberry pi.

Note: the default console configured in Qt creator does not seem to work, if so then you can resolve it by changing in 
Tools->options, Environment/System-tab change "Terminal" to the "xterm -e".