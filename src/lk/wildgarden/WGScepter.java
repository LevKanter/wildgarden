package lk.wildgarden;

import lk.generic.input.Wiimote;
import oscP5.OscMessage;
import processing.core.PApplet;
import processing.xml.XMLElement;

public class WGScepter 
	extends Wiimote 
	implements WGPresets 
{
	protected WGSceneMode mode;
			
	///////////////////////////////////////////////////////////////////////////	
	public WGScepter(PApplet garden, XMLElement configXML) {
		super(garden, configXML);
	}
	
	///////////////////////////////////////////////////////////////////////////
	public void oscEvent(OscMessage incoming) {super.oscEvent(incoming);}
	public void buttonHome(int bang) {super.buttonHome(bang);}
	public void buttonA(int bang) {super.buttonA(bang);}
	public void buttonB(int bang) {super.buttonB(bang);}
	public void button1(int bang) {super.button1(bang);}
	public void button2(int bang) {super.button2(bang);}
	public void buttonMinus(int bang) {super.buttonMinus(bang);}
	public void buttonPlus(int bang) {super.buttonPlus(bang);}
	public void buttonUp(int bang) {super.buttonUp(bang);}
	public void buttonDown(int bang) {super.buttonDown(bang);}
	public void buttonLeft(int bang) {super.buttonLeft(bang);}
	public void buttonRight(int bang) {super.buttonRight(bang);}
	public void setX_IR(float value) {super.setX_IR(value);}
	public void setY_IR(float value) {super.setY_IR(value);}
	public void setPitch(float value) {super.setPitch(value);}
	public void setRoll(float value) {super.setRoll(value);}
	public void setYaw(float value) {super.setYaw(value);}
	public void setAccel(float value) {super.setAccel(value);}
	
	///////////////////////////////////////////////////////////////////////////	
	public void update(WGSceneMode mode) {
		super.update();
		this.mode = mode;
	}

}
