package lk.generic.gui;

import processing.core.PApplet;
import processing.core.PGraphics;

public class TextDisplayer 
	extends ThemedArea 
{
	public StringBuffer displayText;
	
	public TextDisplayer(PApplet app, float cornerX, float cornerY, float w, float h) {
		this(app, cornerX, cornerY, w, h, new Theme(app));
	}
	
	public TextDisplayer(PApplet app, float cornerX, float cornerY, float w, float h, Theme theme) {
		super(app, cornerX, cornerY, w, h, theme);
		displayText = new StringBuffer();
	}
	
	public void update(float x, float y, boolean shouldActive, String displayText) {
		update(x, y, shouldActive);
		updateDisplayText(displayText);
	}
	
	protected void updateDisplayText(String displayText) {
		this.displayText.replace(0, this.displayText.length(), displayText);
	}
	
	protected void drawFG(PGraphics surface) {
		surface.pushMatrix();
			surface.translate(x1, y1);
			surface.pushStyle();
				theme.frontStyle(surface);
				surface.text(displayText.toString(), padding[0], padding[1], w-padding[0], h-padding[1]);
			surface.popStyle();
		surface.popMatrix();
	}
	
}
