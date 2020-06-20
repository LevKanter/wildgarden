package lk.wildgarden.gui;

import lk.generic.colors.ColorPair;
import lk.generic.gui.Theme;
import lk.wildgarden.WG;
import processing.core.PGraphics;

public class WGTheme 
	extends Theme
{	
	public static final int WARNING_COLOR = -53248;
	
	public WGTheme(WG garden) {
		super(garden);
		setFont(WG.smallFont, WG.smallFontSize);
	}
	
	protected void generatePalette() {		
		ColorPair def = new ColorPair(app.color(60, 205), app.color(213, 192, 51));
		ColorPair[] colors = { def, def, def, def };
		generatePalette(colors);
	}
	
	public void backStyle(PGraphics surface) {
		surface.noStroke();
		surface.fill(bg);
	}
	
}
