package lk.wildgarden.gui;

import lk.generic.gui.TextDisplayer;
import lk.generic.util.MessageLevel;
import lk.generic.util.MyConsole;
import lk.wildgarden.WG;
import processing.core.PApplet;
import processing.core.PGraphics;

/*--------------------------------------------------------------------------
	Present messages to the user

------------------------------------------------------------------------- */
public class WGConsole 
	extends TextDisplayer
{	
	private MyConsole myConsole;
	private int counter;
	private int cyclePeriod;
	
	public WGConsole(WG garden) {
		super(garden, 0, garden.height-30, garden.width, 30, new WGTheme(garden));
		setPadding(6, 6);
		myConsole = new MyConsole();
		counter = 0;
		cyclePeriod = 180;
	}
	
	public void update(float x, float y, boolean shouldActivate) {
		super.update(x, y, shouldActivate);
		
		if (!myConsole.isEmpty()) {
			show();
			updateDisplayText(myConsole.peekString());
			if (counter >= cyclePeriod) {
				myConsole.poll();
				counter = 0;
			} else {
				counter += 1;
			}
		} else {
			hide();
		}
	}
	
	public void addMessage(String message) {
		addMessage(message, "info");
	}
	
	public void addMessage(String message, String levelStr) {
		myConsole.addMessage(message, levelStr);
	}
	
	public WGConsole clear() {
		myConsole.clear();
		counter = 0;
		return this;
	}
	
	public void setCyclePeriod(int cyclePeriod) {
		this.cyclePeriod = PApplet.constrain(cyclePeriod, 0, 3000);
	}
	
	protected void drawFG(PGraphics surface) {
		surface.pushMatrix();
			surface.translate(x1, y1);
			surface.pushStyle();
				theme.frontStyle(surface);
				if (!myConsole.isEmpty() && myConsole.peekLevel() == MessageLevel.WARNING) {
					surface.fill(WGTheme.WARNING_COLOR);
				}
				surface.text(displayText.toString(), padding[0], padding[1], w - padding[0], h - padding[1]);
			surface.popStyle();
		surface.popMatrix();
	}

}
