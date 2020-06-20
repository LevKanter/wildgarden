package lk.generic.gui;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class ImagedArea 
	extends Area
{
	protected PImage storage;
	
	///////////////////////////////////////////////////////////////////////////
	public ImagedArea(PApplet app) {
		this(app, null, 0, 0, 0, 0);
	}
	
	public ImagedArea(PApplet app, String path) {
		this(app, app.loadImage(path), 0, 0, 0, 0);
	}
	
	public ImagedArea(PApplet app, PImage storage) {
		this(app, storage, 0, 0, 0, 0);
	}
	
	public ImagedArea(PApplet app, String path, float cornerX, float cornerY) {
		this(app, app.loadImage(path), cornerX, cornerY);
	}
	
	public ImagedArea(PApplet app, PImage storage, float cornerX, float cornerY) {
		this(app, storage, cornerX, cornerY, storage.width, storage.height);
	}
	
	public ImagedArea(PApplet app, PImage storage, float cornerX, float cornerY, float w, float h) {
		super(app, cornerX, cornerY, w, h);
		setImage(storage);
	}

	///////////////////////////////////////////////////////////////////////////
	public void setImage(PImage storage) {
		this.storage = storage;
	}
	
	public void draw(PGraphics surface) {
		if(!isVisible) return;
		
		surface.pushMatrix();
			surface.translate(x1, y1);
			surface.image(storage, 0, 0, w, h);
		surface.popMatrix();
	}
	
}
