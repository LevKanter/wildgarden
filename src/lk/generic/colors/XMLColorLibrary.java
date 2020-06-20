package lk.generic.colors;

import processing.core.PApplet;
import processing.xml.XMLElement;

public class XMLColorLibrary 
	extends ColorLibrary
{
	protected PApplet app;
	protected XMLElement data;
	
	public XMLColorLibrary(PApplet app, String path) {
		super();
		this.app = app;
		
		data = new XMLElement(app, path);
		
		for (int i = 0; i < data.getChildCount(); i += 1) {
			add(new XMLColorPalette(app, path, i));
		}
	}
	
}
