package lk.generic.gui;

import lk.generic.colors.ColorPalette;
import lk.generic.util.MyRuntimeException;
import processing.core.PApplet;
import processing.core.PGraphics;

public class ColorPicker 
	extends Area 
{
	protected ColorPalette storage;
	protected Swatch[] swatches;
	protected Swatch current;
	
	protected int swatchWidth;
	protected int swatchHeight;
	protected int nPerRow;
	protected int nRows;
	
	protected boolean selectionEnabled;
			
	public ColorPicker(PApplet app, ColorPalette storage) {
		this(app, 0, 0, 0, 0, 54, 54, 12, storage);
	}
	
	public ColorPicker(PApplet app, float cornerX, float cornerY, 
			float w, float h, int swatchWidth, int swatchHeight, int nPerRow, ColorPalette storage) 
	{
		super(app, cornerX, cornerY, w, h);
		
		this.swatchWidth = swatchWidth;
		this.swatchHeight = swatchHeight;
		this.nPerRow = nPerRow;
		
		setSelectionOption(true);
		load(storage);
	}
	
	public void setSelectionOption(boolean selectionEnabled) {
		this.selectionEnabled = selectionEnabled;
	}
	
	public void load(ColorPalette storage) {
		this.storage = storage;
		
		int nColors = storage.size();
		swatches = new Swatch[nColors];
		
		for(int i = 0; i < nColors; i += 1) {		
			swatches[i] = new Swatch(
				app, 
				swatchWidth*(i % nPerRow) + x1, 
				swatchHeight*(i / nPerRow) + y1, 
				swatchWidth, 
				swatchHeight, 
				storage.get(i)
			);
		}
		if(selectionEnabled) { 
			current = swatches[0];
		}
		syncCoordinates();		
	}
		
	public Swatch getCurrent() 
		throws MyRuntimeException
	{
		if(current != null) {
			return current;
		}
		throw new MyRuntimeException("No color is selected");
	}
		
	public void update(float x, float y, boolean shouldActive) {
		super.update(x, y, shouldActive);
		
		for(int i = 0; i < swatches.length; i += 1) {
			swatches[i].update(x, y, shouldActive);
		}
		
		for(int i = 0; i < swatches.length; i += 1) {
			if(!selectionEnabled) {
				swatches[i].setSelectionOption(false);
			} else {
				if(swatches[i].getState() == AreaState.ACTIVE) {
					current = swatches[i];
				}
			}
		}
	}
	
	public void draw(PGraphics surface) {
		if(!isVisible) return;
		
		for(int i = 0; i < swatches.length; i += 1) {
			swatches[i].draw(surface);
		}
	}
	
	public void translate(float tX, float tY) {
		super.translate(tX, tY);
		for(int i = 0; i < swatches.length; i += 1) {
			swatches[i].translate(tX, tY);
		}
	}
	
	/**
	 * Update the corners of this ColorPicker 
	 * so that it wraps all of the colors it holds
	 * 
	 */
	private void syncCoordinates() {
		float containerX1 = swatches[0].getCoordinates()[0];
		float containerY1 = swatches[0].getCoordinates()[1];
		float containerW = swatchWidth*nPerRow;
		float containerH = PApplet.abs(swatches[swatches.length-1].getCoordinates()[3] - containerY1);
		setCoordinatesFromCorner(containerX1, containerY1, containerW, containerH);
	}
		
}
