package lk.generic.gui;

import processing.core.PApplet;
import processing.core.PGraphics;

/*--------------------------------------------------------------------------
	Base class for GUI components
 
------------------------------------------------------------------------- */
public abstract class Area 
{
	protected PApplet app;
	protected AreaState state;
	protected boolean isVisible;

	protected float x1;
	protected float y1;
	protected float x2;
	protected float y2;

	protected float w;
	protected float h;

	///////////////////////////////////////////////////////////////////////////
	public Area(PApplet app) {
		this(app, 0, 0, 0, 0);
	}

	public Area(PApplet app, float cornerX, float cornerY, float w, float h) {
		this.app = app;
		resetState();
		setCoordinatesFromCorner(cornerX, cornerY, w, h);
		show();
	}
	
	///////////////////////////////////////////////////////////////////////////
	public AreaState getState() {
		return state;
	}

	public float[] getCoordinates() {
		float[] coordinates = { x1, y1, x2, y2 };
		return coordinates;
	}
	
	public float getWidth() {
		return w;
	}

	public float getHeight() {
		return h;
	}
	
	public boolean isVisible() {
		return isVisible;
	}
	
	///////////////////////////////////////////////////////////////////////////
	public void setCoordinates(float x1, float y1, float x2, float y2)
		throws ScrambledCoordinatesException
	{
		if (x2 < x1 || y2 < y1) {
			throw new ScrambledCoordinatesException(x1, y1, x2, y2);
		}

		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;

		w = x2 - x1;
		h = y2 - y1;
	}
	
	public void setCoordinatesFromCorner(float cornerX, float cornerY, float w, float h) {
		try {
			setCoordinates(cornerX, cornerY, cornerX + w, cornerY + h);
		} 
		catch (ScrambledCoordinatesException sce) {
			x1 = 0; 
			x2 = 0;
			y1 = 0;
			y2 = 0;
			w = 0;
			h = 0;
		}
	}
	
	public void translate(float tX, float tY) {
		setCoordinatesFromCorner(x1 + tX, y1 + tY, w, h);
	}
	
	public void resetState() {
		this.state = AreaState.DEFAULT;
	}
	
	public void show() {
		setVisible(true);
	}
	public void hide() {
		setVisible(false);
	}
	public void toggle() {
		setVisible(!isVisible());
	}
	protected void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	/**
	 * Default = mouse input
	 * 
	 */
	public void update() { 
		update(app.mouseX, app.mouseY, app.mousePressed);
	}
	
	public void update(float x, float y, boolean shouldActivate) {
		if (!isVisible()) return;
		
		if (contains(x, y)) {
			switch (state) {
				case DEFAULT:
					state = AreaState.HOVER;
					break;
				case FOCUS:
					if (shouldActivate) {
						state = AreaState.ACTIVE;
					}
					break;
				case HOVER:
					if (shouldActivate) {
						state = AreaState.FOCUS;
					}
					break;
				default:
					break;
			}
		} 
		else {
			switch (state) {
				case HOVER:
					state = AreaState.DEFAULT;
					break;
				case ACTIVE:
					state = AreaState.FOCUS;
					break;
				case FOCUS:
					if (shouldActivate) {
						state = AreaState.DEFAULT;
					}
					break;
				default:
					break;
			}
		}		
	}
	
	protected boolean contains(float x, float y) {
		return (x >= x1 && x <= x2 && y >= y1 && y <= y2);
	}

	///////////////////////////////////////////////////////////////////////////
	abstract public void draw(PGraphics surface);
	
	/*--------------------------------------------------------------------------
		Convenience methods for rendering the area as a simple rectangle
 
	------------------------------------------------------------------------- */
	protected void drawPlane(PGraphics surface) {
		if (!isVisible()) return;
		
		surface.pushMatrix();
			surface.translate(x1, y1);
			surface.pushStyle();
				style(surface);
				surface.rect(0, 0, w, h);
			surface.popStyle();
		surface.popMatrix();
	}

	protected void drawPlane(PGraphics surface, int fillColor) {
		if (!isVisible()) return;
		
		surface.pushMatrix();
			surface.translate(x1, y1);
			surface.pushStyle();
				surface.noStroke();
				surface.fill(fillColor);
				surface.rect(0, 0, w, h);
			surface.popStyle();
		surface.popMatrix();
	}
	
	protected void style(PGraphics surface) {}
	
	/*--------------------------------------------------------------------------
		Forwarding to default graphics
	 
	------------------------------------------------------------------------- */
	public void draw() {
		draw(app.g);
	}
	public void drawPlane() {
		drawPlane(app.g);
	}
	public void drawPlane(int fillColor) {
		drawPlane(app.g, fillColor);
	}
	protected void style() {
		style(app.g);
	}

}
