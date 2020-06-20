package lk.generic.gui;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class ImagedAreaButton 
	extends AreaButton
{
	protected PImage defaultImage;
	protected PImage hoverImage;
	protected PImage focusImage;
	protected PImage activeImage;
	
	/**
	 * Construct a button that uses the same image for all its states
	 * 
	 */
	public ImagedAreaButton(PApplet app, PImage monoImage) {
		this(app, monoImage, monoImage, monoImage, monoImage);
	}
	
	public ImagedAreaButton(PApplet app, 
			PImage defaultImage, PImage hoverImage, PImage focusImage, PImage activeImage) 
	{
		super(app, 0, 0, 0, 0);
		if(!areImageSizesEqual(defaultImage, hoverImage, focusImage, activeImage)) {
			System.err.println("State images should all be the same size");
			return;
		}
		this.defaultImage = defaultImage;
		this.hoverImage = hoverImage;
		this.focusImage = focusImage;
		this.activeImage = activeImage;
	}
	
	protected boolean areImageSizesEqual(PImage imgA, PImage imgB, PImage imgC, PImage imgD) {
		float w = imgA.width;
		if(imgB.width != w || imgC.width != w || imgD.width != w) {
			return false;
		}
		return true;
	}

	public void draw(PGraphics surface) {
		if(!isVisible) return;
		
		surface.pushMatrix();
		surface.translate(x1, y1);
		switch (state) {
		case HOVER:
			surface.image(hoverImage, 0, 0);
			break;
		case FOCUS:
			surface.image(focusImage, 0, 0);
			break;	
		case ACTIVE:
			surface.image(activeImage, 0, 0);
			break;
		case DEFAULT:	
		default:
			surface.image(defaultImage, 0, 0);
			break;
		}
		surface.popMatrix();
	}
		
}
