package lk.generic.input;

import lk.generic.util.MyRuntimeException;

public class InputConfigException 
	extends MyRuntimeException
{
	private static final long serialVersionUID = 5600454709908530120L;
		
	public InputConfigException(InputDevice device) { 
		super("Cannot configure input device: " + device.toString()); 
	}
	
}
