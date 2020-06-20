package lk.generic.util;

/*--------------------------------------------------------------------------
	Base class for application-specific runtime exceptions
	
------------------------------------------------------------------------- */
public class MyRuntimeException
	extends RuntimeException
{
	private static final long serialVersionUID = -7891619890148738656L;
	
	public static final String DEFAULT_MESSAGE = 
		"Runtime exception in my app"; 
	
	public MyRuntimeException() {
		this(DEFAULT_MESSAGE);
	}
	
	public MyRuntimeException(String message) {
		this(message, true);
	}
	
	public MyRuntimeException(String message, Exception cause) {
		this(message, true, cause);
	}
	
	public MyRuntimeException(String message, boolean shouldPrint) {
		this(message, shouldPrint, null);
	}
	
	public MyRuntimeException(String message, boolean shouldPrint, Exception cause) {
		super(message, cause);
		if (shouldPrint) {
			System.err.println(message);
		}
	}
	
}
