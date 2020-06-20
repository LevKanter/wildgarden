package lk.generic.colors;

import processing.core.PApplet;

public abstract class ColorPalette 
{
	protected PApplet app;
	protected int nColors;
	protected int[] colors;
	
	public String title = null;
	
	///////////////////////////////////////////////////////////////////////////
	public int get(int index) {
		if (index >= 0 && index < nColors) {
			return colors[index];
		}
		return -1;
	}
	
	public int[] getAll() {
		return colors;
	}
	
	public int getRandom() {
		return get(PApplet.floor(app.random(0, nColors)));
	}
	
	public int size() {
		return nColors;
	}
	
	///////////////////////////////////////////////////////////////////////////
	public void load(int[] intColors) {
		colors = intColors;
		nColors = colors.length;
	}
	
	public void load(String[] strColors) {
		int len = strColors.length;
		int[] intColors = new int[len];
		
		for (int i = 0; i < len; i =+ 1) {
			try {
				intColors[i] = ColorOperation.unhexAndFormat(strColors[i]);
			}
			catch (HexStringException hse) {
				return;
			}
		}
		load(intColors);
	}
	
}
