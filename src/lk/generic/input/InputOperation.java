package lk.generic.input;

import processing.core.PApplet;

/*--------------------------------------------------------------------------
	General-purpose static methods related to processing user input
	 
------------------------------------------------------------------------- */
public class InputOperation 
{
	private InputOperation() {}
	
	/**
	 * Smooth incoming, relative to current, by smoothing.
	 * smoothing should be between 0.0 and 1.0,
	 * where 0.0 means no smoothing and 1.0 means maximum smoothing.
	 * 
	 */
	public static float smoothData(float current, float incoming, float smoothing) {
		float safeSmoothing = PApplet.constrain(smoothing, 0f, 1f);
		return safeSmoothing*current + (1 - safeSmoothing)*incoming;  
	}
	
}
