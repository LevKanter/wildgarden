package lk.generic.input;

public interface InputDevice 
{
	public void update();
	
	public static final float DEFAULT_SMOOTHING = 0.96f;
	
	///////////////////////////////////////////////////////////////////////////
	/**
	 * Is the device connected?
	 *  
	 */
	public boolean isSignal();
	 
	/**
	 * How many frames to wait before determining idleness?
	 *  
	 */
	public static final int IDLE_TIME = 300;
	
}