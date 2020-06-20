package lk.generic.colors;

import processing.core.PApplet;
import processing.core.PGraphics;

public class ColorSetter
	extends ColorPair
	implements ColorPresets
{
	protected PApplet app;
	
	public int a;
		
	public ColorSetter(PApplet app) {
		this(app, 255, 0, 255);
	}
	
	public ColorSetter(PApplet app, int fg, int bg, int a) {
		super(fg, bg);
		this.app = app;
		this.a = a;
	}
	
	public void erase() {erase(false);}
	
	public void erase(PGraphics surface) {erase(surface, false);}
	
	public void erase(boolean opaque) {erase(app.g, opaque);}
	
	public void erase(PGraphics surface, boolean opaque) {
		if (opaque) {
			surface.background(bg);
		} else {
			surface.pushStyle();
				surface.noStroke();
				surface.fill(bg, a);
				surface.rectMode(PApplet.CORNER);
				surface.rect(0, 0, surface.width, surface.height);
			surface.popStyle();
		}
	}
	
	public void raiseAlpha() {
		raiseAlpha(1);
	}
	public void raiseAlpha(int amount) {
		a = PApplet.constrain(a + amount, 0, 255);
	}
	public void lowerAlpha() {
		lowerAlpha(1);
	}
	public void lowerAlpha(int amount) {
		raiseAlpha(-1*amount);
	}
	
}
