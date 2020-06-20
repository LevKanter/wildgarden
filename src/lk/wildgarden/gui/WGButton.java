package lk.wildgarden.gui;

import lk.generic.gui.AreaButton;
import lk.generic.gui.ScrambledCoordinatesException;
import lk.wildgarden.WG;
import processing.core.PApplet;
import processing.core.PGraphics;

public class WGButton 
	extends AreaButton
{
	protected String displayText;
	private boolean isCA;
	
	public WGButton(WG app, float cornerX, float cornerY, String displayText) {
		this(app, cornerX, cornerY, WG.BUTTON_W, WG.BUTTON_H, displayText);
	}
	
	public WGButton(PApplet app, float cornerX, float cornerY, float w, float h, String displayText) {
		super(app, cornerX, cornerY, w, h);
		setLabel(displayText);
		setFireOnRelease();
	}
	
	public void setLabel(String displayText) {
		this.displayText = displayText;
	}
	
	public void toggleCallToAction() {
		setCallToAction(!isCA);
	}
		
	public void setCallToAction(boolean isCA) {
		this.isCA = isCA;
	}
	
	public void swapCoordinates(WGButton other) {
		float[] otherCoords = other.getCoordinates();
		try {
			other.setCoordinates(x1, y1, x2, y2);
			setCoordinates(otherCoords[0], otherCoords[1], otherCoords[2], otherCoords[3]);
		}
		catch (ScrambledCoordinatesException sce) { }
	}
	
	public void draw(PGraphics surface) {
		if (!isVisible) return;
		
		surface.pushMatrix();
			surface.translate(x1, y1);
			surface.pushStyle();
				int bg;
				int fg;
				int st;
				switch (state) {
				case HOVER:
					bg = app.color(89, 133, 39);
					fg = 255;
					st = 50;
					break;
				case FOCUS:
					bg = 51;
					fg = 200;
					st = 40;
					break;	
				case ACTIVE:
					bg = 0;
					fg = app.color(213, 192, 51);
					st = app.color(213, 192, 51);
					break;
				case DEFAULT:	
				default:
					if (isCA) {
						bg = app.color(213, 192, 51);
						fg = app.color(27, 71, 51);
						st = app.color(55, 82, 24);
					} else {
						bg = 70;
						fg = 200;
						st = 50;
					}
					break;
				}
				surface.fill(bg);
				surface.stroke(st);
				surface.rect(0, 0, w, h);
				surface.fill(fg);
				surface.noStroke();
				surface.textFont(WG.bigFont, WG.bigFontSize);
				surface.text(displayText, 8, 28);
			surface.popStyle();
		surface.popMatrix();
	}

}
