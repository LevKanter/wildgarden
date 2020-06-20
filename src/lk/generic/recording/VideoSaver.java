package lk.generic.recording;

import processing.core.PApplet;

public class VideoSaver 
	extends Saver
{
	
	public VideoSaver(PApplet app, String title) {
		this(app, "", title);
	}
 		
	public VideoSaver(PApplet app, String directory, String title) { 
		super(app, directory, title);
		format = "mov";
	}
	
	public void update() {
		
	}
}
