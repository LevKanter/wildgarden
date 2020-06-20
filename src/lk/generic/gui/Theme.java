package lk.generic.gui;

import java.util.HashMap;

import lk.generic.colors.ColorPair;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;

public class Theme
	extends ColorPair
{
	protected PApplet app;
	
	protected PFont font;
	protected int fontSize;
		
	protected HashMap<AreaState, ColorPair> stateColorMap;
	
	///////////////////////////////////////////////////////////////////////////	
	public Theme(PApplet app) {
		this.app = app;
		
		stateColorMap = new HashMap<AreaState, ColorPair>(4, 1);
		generatePalette();
	}
	
	///////////////////////////////////////////////////////////////////////////
	public void setFont(String fontFile, int fontSize) { 
		setFont(app.loadFont(fontFile), fontSize);
	}
	
	public void setFont(PFont font, int fontSize) { 
		this.font = font;
		this.fontSize = fontSize; 
	}
		
	public void update(AreaState state) {		
		switch(state) {			
			case HOVER:
				bg = stateColorMap.get(AreaState.HOVER).bg;
				fg = stateColorMap.get(AreaState.HOVER).fg;
				break;
			case FOCUS:
				bg = stateColorMap.get(AreaState.FOCUS).bg;
				fg = stateColorMap.get(AreaState.FOCUS).fg;
				break;
			case ACTIVE:
				bg = stateColorMap.get(AreaState.ACTIVE).bg;
				fg = stateColorMap.get(AreaState.ACTIVE).fg;
				break;
			case DEFAULT:
			default:
				bg = stateColorMap.get(AreaState.DEFAULT).bg;
				fg = stateColorMap.get(AreaState.DEFAULT).fg;	
				break;
		}
	}
	
	public void backStyle(PGraphics surface) {
		surface.stroke(30);
		surface.fill(bg);
	}
	
	public void frontStyle(PGraphics surface) {
		surface.noStroke();
		surface.fill(fg);
		fontStyle(surface);
	}
	
	protected void fontStyle(PGraphics surface) {
		surface.textFont(font, fontSize);
	}
	
	public static ColorPair[] synthesizeDefaultPalette() {
		ColorPair def = new ColorPair(70, 200);
		ColorPair[] colorDuos = { def, def, def, def };
		return colorDuos;
	}
	
	public static ColorPair[] synthesizePalette(
			int bgDefault, int fgDefault, 
			int bgHover, int fgHover,
			int bgFocus, int fgFocus,
			int bgActive, int fgActive) 
	{
		ColorPair[] colorDuos = {
			new ColorPair(bgDefault, fgDefault),
			new ColorPair(bgHover, fgHover),
			new ColorPair(bgFocus, fgFocus),
			new ColorPair(bgActive, fgActive)
		};
		return colorDuos;
	}
	
	///////////////////////////////////////////////////////////////////////////
	protected void generatePalette() {		
		generatePalette(synthesizeDefaultPalette());
	}
	
	protected void generatePalette(ColorPair[] colors) {
		if(colors.length != 4) return;
		stateColorMap.clear();
		
		stateColorMap.put(AreaState.DEFAULT, colors[0]);
		stateColorMap.put(AreaState.HOVER, colors[1]);
		stateColorMap.put(AreaState.FOCUS, colors[2]);
		stateColorMap.put(AreaState.ACTIVE, colors[3]);
	}
	
}
