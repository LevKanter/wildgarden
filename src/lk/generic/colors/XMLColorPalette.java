package lk.generic.colors;

import processing.core.PApplet;
import processing.xml.XMLElement;
import processing.xml.XMLException;

public class XMLColorPalette
	extends ColorPalette
{
	protected XMLElement data;
		
	public XMLColorPalette(PApplet app, String path, int libraryIndex) {
		this.app = app;
		try {
			setup(path, libraryIndex);
		} 
		catch (HexStringException hse) {
			app.exit();
		} 
		catch (XMLException xe) {
			System.err.println(xe.getMessage());
			app.exit();
		}
	}
	
	private void setup(String path, int index)
		throws HexStringException, XMLException 
	{	
		data = new XMLElement(app, path).getChildren()[index];
		title = data.getStringAttribute("title", "XML color palette");
		
		nColors = data.getChildCount();
		colors = new int[nColors];
		
		for (int i = 0; i < nColors; i += 1) {
			XMLElement current = data.getChild(i);
			colors[i] = ColorOperation.unhexAndFormat(current.getContent());
		}		
	}
	
}
