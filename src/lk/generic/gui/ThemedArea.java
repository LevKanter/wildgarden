package lk.generic.gui;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;

public abstract class ThemedArea 
	extends Area 
{
	protected Theme theme;
	protected int[] padding;
	
	public ThemedArea(PApplet app, float cornerX, float cornerY, float w, float h) {
		this(app, cornerX, cornerY, w, h, new Theme(app));
	}
	
	public ThemedArea(PApplet app, float cornerX, float cornerY, float w, float h, Theme theme) {
		super(app, cornerX, cornerY, w, h);
		setTheme(theme);
		padding = new int[2];//padding[0] is left/right, padding[1] is top/bottom
	}
	
	public void setTheme(Theme theme) {
		this.theme = theme;
	}
	
	public void setPadding(int[] padding) {
		if(padding.length > 4 || padding.length == 0) {
			return;
		}
		this.padding = padding;
	}
	
	public void setPadding(int lr, int tb) {
		padding[0] = lr;
		padding[1] = tb;
	}
	
	public void setFont(PFont font, int fontSize) { 
		theme.setFont(font, fontSize);
	}
	
	public void update(float x, float y, boolean shouldActivate) {
		super.update(x, y, shouldActivate);
		theme.update(state);
	}
	
	public void draw(PGraphics surface) {
		if(!isVisible) return;
		
		drawBG(surface);
		drawFG(surface);
	}
	
	protected void drawBG(PGraphics surface) {
		surface.pushMatrix();
			surface.translate(x1, y1);
			surface.pushStyle();
				theme.backStyle(surface);
				surface.rect(0, 0, w, h);
			surface.popStyle();
		surface.popMatrix();
	}
	
	protected void drawFG(PGraphics surface) {}

}