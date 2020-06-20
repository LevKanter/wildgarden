package lk.generic.recording;

import processing.core.PApplet;

public abstract class Saver 
{
	protected PApplet app;
	
	protected String directory;
	protected String title;
	protected String format;
	  
	protected boolean isRunning; 
	protected int count;
	
	public Saver(PApplet app, String title) {
		this(app, "", title);
	}
	
	public Saver(PApplet app, String directory, String title) { 
		this.app = app;
	    
		this.directory = directory;
		this.title = title; 
	    
		isRunning = false; 
		count = 0;
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
	public int getCount() {
		return count;
	}
	
	public void start() {
		System.out.println("STARTED RECORDING --> frame " + app.frameCount);
		isRunning = true;
	}
	  
	public void stop() {
		System.out.println("STOPPED RECORDING --> " + count + " frames recorded");
		isRunning = false;
	}
	
	abstract public void update();
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setDirectory(String directory) {
		this.directory = directory;
	}

}
