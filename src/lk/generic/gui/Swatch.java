package lk.generic.gui;

import processing.core.PApplet;
import processing.core.PGraphics;

public class Swatch 
	extends Area 
{
	protected int fillColor;
	protected boolean selectionEnabled;

	public Swatch(PApplet app, float cornerX, float cornerY, float w, float h, int fillColor) {
		super(app, cornerX, cornerY, w, h);
		setColor(fillColor);
		setSelectionOption(true);
	}

	public void setColor(int fillColor) {
		this.fillColor = fillColor;
	}

	public int getColor() {
		return fillColor;
	}
	
	public void setSelectionOption(boolean selectionEnabled) {
		this.selectionEnabled = selectionEnabled;
	}

	public void draw(PGraphics surface) {
		if(!isVisible) return;
		
		drawPlane(surface, fillColor);
		
		if(selectionEnabled) {
			if(state == AreaState.HOVER || state == AreaState.ACTIVE) {
				int hoverStroke = app.color(0, 255, 0); //TODO pull this out
				int hoverStrokeWeight = 2; //TODO
				surface.pushMatrix();
					surface.translate(x1, y1);
					surface.pushStyle();
					surface.noFill();
					surface.stroke(hoverStroke);
					surface.strokeWeight(hoverStrokeWeight);
					surface.rect(0, 0, w - hoverStrokeWeight, h - hoverStrokeWeight);
					surface.popStyle();
				surface.popMatrix();
			}
		}
	}

}
