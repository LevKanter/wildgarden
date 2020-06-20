package lk.generic.recording;

import processing.core.PApplet;
import processing.core.PImage;

public class FrameSaver 
	extends Saver 
{
	
	public FrameSaver(PApplet app, String title) {
		this(app, "", title);
	}
 		
	public FrameSaver(PApplet app, String directory, String title) { 
		super(app, directory, title);
		format = "png";
	}
	
	/**
	 * update() without arguments can be used to save a frame sequence
	 * This currently on works by saving the PApplet's entire canvas
	 * (as opposed to any individual graphics buffer)
	 * 
	 */
	public void update() {
		if (isRunning) {
			app.saveFrame(directory + title + "-#####." + format);
			count += 1; 
		}
	}
	
	public void update(PImage surfaceImage) {
		if (isRunning) {
			String path = directory + title + "-" + app.frameCount + "." + format;
			surfaceImage.save(path);
			System.out.println("Saved " + path);
			count += 1;
		}
	}
	
	public void takeScreenshot(PImage surfaceImage) {
		start();
		update(surfaceImage);
		stop();
	}
	
	public void takeScreenshot() {
		takeScreenshot(app.g);
	}
		
}
