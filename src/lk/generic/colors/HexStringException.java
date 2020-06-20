package lk.generic.colors;

import lk.generic.util.MyRuntimeException;

public class HexStringException 
	extends MyRuntimeException 
{
	private static final long serialVersionUID = -4530549462353975851L;
	
	public static final String DEFAULT_MESSAGE = 
		"String must be of the format 0x000000 or #000000"; 
	
	public HexStringException() { 
		super(DEFAULT_MESSAGE); 
	}
	
	public HexStringException(boolean shouldPrint) {
		super(DEFAULT_MESSAGE, shouldPrint);
	}

}
