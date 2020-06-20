package lk.generic.util;

import java.util.LinkedList;

public class MyConsole
{
	private LinkedList<MyConsoleMessage> messageQueue;
	private int maxMessages;
	
	public MyConsole() {
		this(20);
	}
	
	public MyConsole(int maxMessages) {
		messageQueue = new LinkedList<MyConsoleMessage>();
		this.maxMessages = maxMessages;
	}
	
	public void addMessage(String message) {
		addMessage(message, "info");
	}
	
	public void addMessage(String message, String levelStr) {
		if (messageQueue.size() > maxMessages) return;
		messageQueue.add(new MyConsoleMessage(message, levelStr));
	}
	
	public boolean isEmpty() {
		return messageQueue.isEmpty();
	}
	
	public MyConsoleMessage peek() {
		return messageQueue.peek();
	}
	
	public String peekString() {
		return peek().getContent();
	}
	
	public MessageLevel peekLevel() {
		return peek().getLevel();
	}
	
	public MyConsole poll() {
		messageQueue.poll();
		return this;
	}
	
	public MyConsole clear() {
		messageQueue.clear();
		return this;
	}
	
	public void println() {
		messageQueue.peek().println();
	}
		
}
