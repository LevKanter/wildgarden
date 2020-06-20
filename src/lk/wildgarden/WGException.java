package lk.wildgarden;

import lk.generic.util.MyRuntimeException;

public class WGException 
	extends MyRuntimeException
{
	private static final long serialVersionUID = -9026375312776125970L;
	
	public WGException() { 
		this("WildGarden exception"); 
	}
	
	public WGException(String message) {
		this(message, true);
	}
	
	public WGException(String message, Exception cause) {
		this(message, true, cause);
	}
	
	public WGException(String message, boolean shouldPrint) {
		this(message, shouldPrint, null);
	}
	
	public WGException(String message, boolean shouldPrint, Exception cause) {
		super(message, shouldPrint, cause);
	}
	
}