package lk.generic.gui;

import lk.generic.util.MyRuntimeException;

public class ScrambledCoordinatesException 
	extends MyRuntimeException 
{ 
	private static final long serialVersionUID = 1779339752037772606L;
	
	public static final String DEFAULT_MESSAGE = 
		"Coordinates must define a plane such that (x1, y1) is its upper left corner and (x2, y2) is the lower right"; 
	
	public ScrambledCoordinatesException() { 
		this(DEFAULT_MESSAGE); 
	}
	
	public ScrambledCoordinatesException(String message) {
		super(message);
	}
	
	public ScrambledCoordinatesException(String message, boolean shouldPrint) {
		super(message, shouldPrint);
	}
	
	public ScrambledCoordinatesException(float x1, float y1, float x2, float y2) {
		super("Scrambled coordinates --> " + x1 + ", " + y1 + ", " + x2 + ", " + y2);
	}

}