package lk.generic.input;

import java.util.HashMap;

import netP5.NetAddress;
import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;
import processing.core.PVector;
import processing.xml.XMLElement;

public class Wiimote
	implements InputDevice, WiimoteResponder
{
	public static final float SHAKE_THRESHOLD = 0.8f;
	
	protected PApplet app;
	protected int incomingPort;
	protected int outgoingPort;
		
	protected OscP5 oscManager;
	protected NetAddress outgoingAddr;
	protected HashMap<String, String> addrSpaceMap;
	
	protected boolean isSignal;
	protected int timeSinceLastSignal;
	protected float batteryLevel;
	
	protected boolean isPressedHome;
	protected boolean isPressedA;
	protected boolean isPressedB;
	protected boolean isPressed1;
	protected boolean isPressed2;
	protected boolean isPressedMinus;
	protected boolean isPressedPlus;
	protected boolean isPressedUp;
	protected boolean isPressedDown;
	protected boolean isPressedLeft;
	protected boolean isPressedRight;
	
	protected boolean isX;
	protected boolean isY;
	
	protected PVector pos;
	protected PVector ppos;
	protected PVector vel;
	
	protected float pitch;
	protected float roll;
	protected float yaw;
	protected float accel;
	
	protected float smoothing;
	
	///////////////////////////////////////////////////////////////////////////
	public Wiimote(PApplet app, XMLElement configXML) {
		this.app = app;
		
		addrSpaceMap = new HashMap<String, String>();
		try {
			setupOSC(configXML);
		} 
		catch (InputConfigException cce) {
			app.exit();
 		}
				
		pos = new PVector();
		ppos = new PVector();
		vel = new PVector();	

		smoothing = 0f;
		
		timeSinceLastSignal = 0;
	}
	
	protected void setupOSC(XMLElement configXML) 
		throws InputConfigException
	{
		try {
			incomingPort = configXML.getChild("port").getIntAttribute("incoming");
			outgoingPort = configXML.getChild("port").getIntAttribute("outgoing");
			XMLElement addressSpaceXML = configXML.getChild("addressSpace");
				
			for(int i = 0; i < addressSpaceXML.getChildCount(); i += 1) {
				XMLElement addr = addressSpaceXML.getChild(i); 
				addrSpaceMap.put(addr.getName(), addr.getContent().trim());
			}
			
			oscManager = new OscP5(this, incomingPort);
			
			outgoingAddr = new NetAddress("localhost", outgoingPort); 
			
			plugResponder(this);
			
			plug(this, "setX_IR", "x"); 
			plug(this, "setY_IR", "y");
			
			plug(this, "setPitch", "pitch");
			plug(this, "setRoll", "roll"); 
			plug(this, "setYaw", "yaw"); 
			plug(this, "setAccel", "accel");				
		} 
		catch (Exception ex) {
			throw new InputConfigException(this);
		}
	}
	
	public void query(String addrPattern, String command) {
		OscMessage q = new OscMessage(addrPattern);
		q.add(command);
		oscManager.send(q, outgoingAddr);
	}
	
	public void oscEvent(OscMessage incoming) {		
		if (incoming.isPlugged()) {
			if (!isSignal) {
				isSignal = true;
			}
		} else {
			if (incoming.typetag().equals("sf") && incoming.addrPattern().equals("/osculator/wii/1")) {
				String info = incoming.get(0).stringValue();
				if (info.equals("battery")) {
					batteryLevel = incoming.get(1).floatValue();
				}
			} else {
				System.out.println("UNPLUGGED MESSAGE WITH ADDR PATTERN: " + incoming.addrPattern());
			}
		}
		timeSinceLastSignal = 0;
	}
	
	public void plug(Object object, String method, String event) { 
		oscManager.plug(object, method, (String)addrSpaceMap.get(event));
	}
	
	/**
	 * Convenience for automatically forwarding button signals to WiimoteResponder objects
	 * 
	 */
	public void plugResponder(WiimoteResponder responder) {
		plug(responder, "buttonHome", "home");
		plug(responder, "buttonA", "A");
		plug(responder, "buttonB", "B");
		plug(responder, "button1", "1");
		plug(responder, "button2", "2");
		plug(responder, "buttonMinus", "minus");
		plug(responder, "buttonPlus", "plus");
		plug(responder, "buttonUp", "up");
		plug(responder, "buttonDown", "down");
		plug(responder, "buttonLeft", "left");
		plug(responder, "buttonRight", "right");
	}
	
	///////////////////////////////////////////////////////////////////////////
	public void buttonHome(int bang) {isPressedHome = button(bang);}
	public void buttonA(int bang) {isPressedA = button(bang);}
	public void buttonB(int bang) {isPressedB = button(bang);}
	public void button1(int bang) {isPressed1 = button(bang);}
	public void button2(int bang) {isPressed2 = button(bang);}
	public void buttonMinus(int bang) {isPressedMinus = button(bang);}
	public void buttonPlus(int bang) {isPressedPlus = button(bang);}
	public void buttonUp(int bang) {isPressedUp = button(bang);}
	public void buttonDown(int bang) {isPressedDown = button(bang);}
	public void buttonLeft(int bang) {isPressedLeft = button(bang);}
	public void buttonRight(int bang) {isPressedRight = button(bang);}
	
	public void setX_IR(float value) {setX(value);}
	public void setY_IR(float value) {setY(value);}
	
	public void setPitch(float value) {pitch = value;}
	public void setRoll(float value) {roll = value;}
	public void setYaw(float value) {yaw = value;}
	public void setAccel(float value) {accel = value;}
	
	public void setSmoothing(float smoothing) {
		this.smoothing = smoothing;
	}
	
	///////////////////////////////////////////////////////////////////////////
	protected boolean button(int bang) {
		return (bang > 0) ? true : false;
	}
	
	protected void setX(float value) {
		ppos.x = pos.x;	
		pos.x = InputOperation.smoothData(ppos.x, value*app.width, smoothing);

		if (pos.x < 0 || pos.x > app.width) {
			isX = false;
			return;
		}
		isX = true;
	}
	
	protected void setY(float value) {
		ppos.y = pos.y;		
		pos.y = InputOperation.smoothData(ppos.y, PApplet.abs(app.height - (value*app.height)), smoothing);
		
		if (pos.y < 0 || pos.y > app.height) {
			isY = false;
			return;
		}
		isY = true;
	}
	
	///////////////////////////////////////////////////////////////////////////
	public boolean isSignal() { 
		return isSignal;
	}
	
	protected boolean isIdle() {
		if (timeSinceLastSignal < IDLE_TIME) {
			return false;
		}
		return true;
	}
	
	public boolean isShaking() {
		if (accel > SHAKE_THRESHOLD) {
			return true;
		}
		return false;
	}
		
	public boolean isHome() {return isPressedHome;}
	public boolean isA() {return isPressedA;}
	public boolean isB() {return isPressedB;}
	public boolean is1() {return isPressed1;}
	public boolean is2() {return isPressed2;}
	public boolean isMinus() {return isPressedMinus;}
	public boolean isPlus() {return isPressedPlus;}
	public boolean isUp() {return isPressedUp;}
	public boolean isDown() {return isPressedDown;}
	public boolean isLeft() {return isPressedLeft;}
	public boolean isRight() {return isPressedRight;}
	
	public PVector getPos() {return pos;}
	public PVector getVel() {return vel;}
	
	public float getPitch() {return pitch;}
	public float getRoll() {return roll;}
	public float getYaw() {return yaw;}
	public float getAccel() {return accel;}
	
	public float[] getAccelPacket() {
		float[] accelPacket = {pitch, roll, yaw, accel};
		return accelPacket;
	}
	
	///////////////////////////////////////////////////////////////////////////		
	public void update() {
		if (isSignal) {
			timeSinceLastSignal += 1;
			if (isIdle()) {
				isSignal = false;
				return;
			}
			vel = PVector.sub(pos, ppos);
			if (!(isX && isY)) {
				PVector followThrough = vel;
				followThrough.normalize();
				followThrough.mult(4f*smoothing);
				pos.add(followThrough);
			}
			if (app.frameCount % 600 == 0) {
				query("/osculator/wii/1", "battery?");
			}
		}
		isX = isY = false;
	}
	
}
