package lk.generic.util;

public class MyConsoleMessage 
{
	private String message;
	private MessageLevel level;
	
	public MyConsoleMessage(String message) {
		this(message, "info");
	}
	
	public MyConsoleMessage(String message, String levelStr) {
		this.message = message;
		setLevelFromString(levelStr);
	}
	
	public String getContent() {
		return message;
	}
	
	public MessageLevel getLevel() {
		return level;
	}
	
	public void println() {
		switch (level) {
		case ERROR:
			System.err.println(message);
			break;
		case INFO:
		default:
			System.out.println(message);
		}
	}
	
	protected void setLevelFromString(String str) {
		if (str.equalsIgnoreCase("error")) {
			level = MessageLevel.ERROR;
		} else if (str.equalsIgnoreCase("warning")) {
			level = MessageLevel.WARNING;
		} else {
			level = MessageLevel.INFO;
		}
	}
	
}
