package lk.generic.colors;

import processing.core.PApplet;

/*--------------------------------------------------------------------------
	General-purpose static methods related to color
	
------------------------------------------------------------------------- */
public class ColorOperation 
{	
	private ColorOperation() {}
	
	public static int getRandomColorRGB(PApplet app) {
		app.pushStyle();
			app.colorMode(PApplet.RGB, 255);
			int c = app.color(app.random(0,255),app.random(0,255),app.random(0,255));
		app.popStyle();
		return c;
	}
	
	public static int getRandomColorHSB(PApplet app) {
		app.pushStyle();
			app.colorMode(PApplet.HSB, 360, 100, 100);
			int c = app.color(app.random(0,360), 100, 100);
		app.popStyle();
		return c;
	}
	
	public static int fastRed(int c) { 
		return (c >> 16) & 0xFF;
	}
	public static int fastGreen(int c) { 
		return (c >> 8) & 0xFF;
	}
	public static int fastBlue(int c) { 
		return c & 0xFF;
	}
	public static int fastAlpha(int c) {
		return (c >> 24) & 0xFF;
	}
		
	public static int unhexAndFormat(String hex)
		throws HexStringException 
	{	
		String hexStr = hex;
		
		if (hexStr.startsWith("0x") || hexStr.startsWith("0X")) {
			hexStr = "FF" + hexStr.substring(2);
		} else if (hexStr.startsWith("#")) {
			hexStr = "FF" + hexStr.substring(1);
		} else {
			throw new HexStringException();
		}
		return PApplet.unhex(hexStr);
	}

}
