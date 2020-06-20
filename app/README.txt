WildGarden -- version 0.1 -- 6/12/2010
by Lev Kanter ( http://levkanter.com )

--------------------------->
Run the app

	
In its current form, the WildGarden app runs on MacOSX 10+
	
WildGarden uses another application, OSCulator, to receive data from a Wiimote
	http://www.osculator.net/

The applescript "run_WildGarden.applescript" can be used to run WildGarden with the prerequisite OSCulator file automatically running in the background.
	
WildGarden is used with a standard Nintendo Wiimote controller and sensor bar. The sensor bar is needed to track the Wiimote and can be any infrared light source. Both the controller and the sensor bar can be purchased on Amazon:

	Wiimote: http://www.amazon.com/Wii-Remote-Controller-nintendo/dp/B000IMWK2G/ref=pd_sim_vg_1
	Sensor bar: http://www.amazon.com/Nyko-Wireless-Sensor-Bar-Nintendo-Wii/dp/B000LFJNG6
	
	
--------------------------->
Check out the source code


WildGarden is implemented in Java using the open source Processing library ( http://processing,org )
WildGarden also uses other libraries that were written for Processing:
	oscP5 by Andreas Schlegel ( http://www.sojamo.de/libraries/oscP5/ )
	GLGraphics by Andres Colubri ( http://glgraphics.sourceforge.net/ )

WildGarden is being hosted on Google Code: http://code.google.com/p/lk-wildgarden/
A read-only version of the project source code can be checked out over HTTP using SVN:
	svn checkout http://lk-wildgarden.googlecode.com/svn/trunk/ lk-wildgarden-read-only