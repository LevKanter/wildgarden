-- runs WildGarden app with the prerequisite OSCulator file open in the background  --


set oscFileName to "wiimote.oscd" -- this file must be in the same folder as this script


tell application "Finder"
	set basePath to parent of (path to me) as alias
end tell

set oscFilePath to POSIX path of basePath & oscFileName

set openOSCulatorFile to "open " & quoted form of oscFilePath
do shell script openOSCulatorFile


activate application "WildGarden"