package lk.generic.util;

import processing.core.PImage;

public class GraphicsOperation
{
	private GraphicsOperation() {}
	
	public static void copyPixels(PImage source, PImage destination) {
		int sW = source.width, sH = source.height; 
		int dW = destination.width, dH = destination.height;
		destination.copy(source, 0, 0, sW, sH, 0, 0, dW, dH);
	}
	
	public static void setAllPixelsTransparent(PImage img) {
		setAllPixels(img, 0);
	}
	
	public static void setAllPixels(PImage img, int c) {
		img.loadPixels();
		for (int i = 0; i < img.pixels.length; i += 1) { 
			img.pixels[i] = c;
		}
		img.updatePixels();
	}
				
}
