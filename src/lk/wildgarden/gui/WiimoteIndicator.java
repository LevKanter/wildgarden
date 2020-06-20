package lk.wildgarden.gui;

import java.util.HashMap;
import java.util.Iterator;

import lk.generic.gui.ImagedArea;
import lk.generic.input.Wiimote;
import lk.generic.input.WiimoteResponder;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class WiimoteIndicator 
	extends ImagedArea
	implements WiimoteResponder
{
	private class WiiButton
	{
		PImage img;
		boolean isActive;
		
		WiiButton(String path) {
			img = app.loadImage(path);
			isActive = false;
		}
	}
	
	private Wiimote wiimote;
	private HashMap<String, WiiButton> buttons;
	private PImage signalImg;
	private PImage noSignalImg;
	private boolean blinkSwitch;
		
	public WiimoteIndicator(PApplet app, Wiimote wiimote, float cornerX, float cornerY) {
		super(app, "images/wiimote/wiimote_base.png", cornerX, cornerY);
		this.wiimote = wiimote;
		this.wiimote.plugResponder(this);
		
		buttons = new HashMap<String, WiiButton>();
		
		buttons.put("home", new WiiButton("images/wiimote/wiimote_home.png"));
		buttons.put("A", new WiiButton("images/wiimote/wiimote_A.png"));
		buttons.put("B", new WiiButton("images/wiimote/wiimote_B.png"));
		buttons.put("1", new WiiButton("images/wiimote/wiimote_1.png"));
		buttons.put("2", new WiiButton("images/wiimote/wiimote_2.png"));
		buttons.put("minus", new WiiButton("images/wiimote/wiimote_minus.png"));
		buttons.put("plus", new WiiButton("images/wiimote/wiimote_plus.png"));
		buttons.put("up", new WiiButton("images/wiimote/wiimote_up.png"));
		buttons.put("down", new WiiButton("images/wiimote/wiimote_down.png"));
		buttons.put("left", new WiiButton("images/wiimote/wiimote_left.png"));
		buttons.put("right", new WiiButton("images/wiimote/wiimote_right.png"));
	
		signalImg = app.loadImage("images/wiimote/wiimote_signal.png");
		noSignalImg = app.loadImage("images/wiimote/wiimote_no_signal.png");
	}
		
	public void draw(PGraphics surface) {
		surface.pushMatrix();
			surface.translate(x1, y1);
			surface.image(storage, 0, 0, w, h);
			Iterator<WiiButton> iterator = buttons.values().iterator();
			while (iterator.hasNext()) {
				WiiButton btn = iterator.next();
				if (btn.isActive) {
					surface.image(btn.img, 0, 0);
				}
			}
			if (wiimote.isSignal()) {
				surface.image(signalImg, 0, 0, w, h);
			} else {
				if (app.frameCount % 60 == 0) {
					blinkSwitch = !blinkSwitch;
				}
				if (blinkSwitch) {
					surface.image(noSignalImg, 0, 0, w, h);
				}
			}
		surface.popMatrix();
	}
	
	public void buttonHome(int bang) {button("home", bang);}
	public void buttonA(int bang) {button("A", bang);}
	public void buttonB(int bang) {button("B", bang);}
	public void button1(int bang) {button("1", bang);}
	public void button2(int bang) {button("2", bang);}
	public void buttonMinus(int bang) {button("minus", bang);}
	public void buttonPlus(int bang) {button("plus", bang);}
	public void buttonUp(int bang) {button("up", bang);}
	public void buttonDown(int bang) {button("down", bang);}
	public void buttonLeft(int bang) {button("left", bang);}
	public void buttonRight(int bang) {button("right", bang);}
	
	protected void button(String key, int bang) {
		WiiButton btn = buttons.get(key);
		if (bang > 0) {
			btn.isActive = true;
		}
		else {
			btn.isActive = false;
		}
	}
	
}
