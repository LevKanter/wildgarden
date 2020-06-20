package lk.generic.colors;

public class ColorPair 
{
	public int bg;
	public int fg;
	
	public ColorPair() {
		this(0, 255);
	}
	
	public ColorPair(int bg, int fg) {
		this.bg = bg;
		this.fg = fg;
	}
	
}
