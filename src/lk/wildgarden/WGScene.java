package lk.wildgarden;

import lk.generic.input.WiimoteResponder;

public abstract class WGScene
	implements WGPresets, WiimoteResponder
{
	protected WG garden;
	protected float width;
	protected float height;
	protected boolean isLocked;
	
	public WGScene(WG garden) {
		this.garden = garden;
		width = garden.width;
		height = garden.height;
		unlock();
		setup();
	}
	
	abstract public void setup();
	abstract public void update();
	abstract public void draw();
	
	abstract public void keyPressed(int key, int keyCode);
	abstract public void keyReleased(int key, int keyCode);
	abstract public void mousePressed();
	abstract public void mouseReleased();
	abstract public void mouseDragged();
	
	public boolean isLocked() { 
		return isLocked;
	}
	
	public void lock() {
		isLocked = true;
	}
	public void unlock() {
		isLocked = false;
	}
	
	/*----------------------------------------------
		Wiimote events (not abstract)

	--------------------------------------------- */
	public void buttonHome(int bang) {}
	public void buttonA(int bang) {}
	public void buttonB(int bang) {}
	public void button1(int bang) {}
	public void button2(int bang) {}
	public void buttonMinus(int bang) {}
	public void buttonPlus(int bang) {}
	public void buttonUp(int bang) {}
	public void buttonDown(int bang) {}
	public void buttonLeft(int bang) {}
	public void buttonRight(int bang) {}
		
}
