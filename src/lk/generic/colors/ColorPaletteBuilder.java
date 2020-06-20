package lk.generic.colors;

import java.io.PrintWriter;

import processing.core.PApplet;
import processing.core.PImage;

/*--------------------------------------------------------------------------
	Generate an XML file that stores color palette data

------------------------------------------------------------------------- */
public class ColorPaletteBuilder 
{
	protected PApplet app;
	protected PrintWriter output;
	
	private final String title;
	private final String baseNodeName;
	
	public ColorPaletteBuilder(PApplet app, String title) {
		this.app = app;
		this.title = title;
		
		baseNodeName = "palette";
		output = app.createWriter(title+".xml");
		
		output.println("<?xml version='1.0'?>");
		output.println("<" + baseNodeName + " title='" + title + "'>");
	}
	
	public void writeColors(int[] colors) {
		for (int i = 0; i < colors.length; i += 1) {
			output.println("\t<color>0x" + PApplet.hex(colors[i], 6) + "</color>");
		}
	}
	
	public void writeColors(String path, int freqFactor) {
		writeColors(app.loadImage(path), freqFactor);
	}
	
	public void writeColors(PImage img, int freqFactor) {
		img.loadPixels();
		int step = img.pixels.length/freqFactor;
		for (int i = 0; i < img.pixels.length; i += step) {
			output.println("\t<color>0x" + PApplet.hex(img.pixels[i], 6) + "</color>");
		}
		img.updatePixels();
	}
	
	public void end() {
		output.println("</" + baseNodeName + ">");
		output.flush();
		output.close();
		System.out.println("Finished writing " + title + " color palette");
	}
	
}
