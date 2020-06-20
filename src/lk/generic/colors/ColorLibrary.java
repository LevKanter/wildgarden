package lk.generic.colors;

import java.util.ArrayList;

import processing.core.PApplet;

public class ColorLibrary 
{
	protected ArrayList<ColorPalette> storage;
	protected int nLibs;
	protected int currentIndex;
	protected int cloneIndex;
	
	public ColorLibrary() {
		storage = new ArrayList<ColorPalette>();
		currentIndex = 0;
		cloneIndex = currentIndex;
	}
	
	public void add(ColorPalette colorPalette) {
		storage.add(colorPalette);
		nLibs = storage.size();
	}
	
	public ColorPalette getCurrent() {
		return storage.get(currentIndex);
	}
	
	public int getCloneIndex() {
		return cloneIndex;
	}
	
	public void setCurrentIndex(int newCurrent) {
		currentIndex = PApplet.constrain(newCurrent, 0, nLibs - 1); 
	}
	
	public ColorPalette loadNext() {
		currentIndex += 1;
		if (currentIndex > (nLibs - 1)) { 
			currentIndex = 0;
		}
		cloneIndex = currentIndex;
		
		return getCurrent();
	}
	
	public ColorPalette loadPrev() {
		currentIndex -= 1;
		if (currentIndex < 0) { 
			currentIndex = nLibs - 1;
		}
		cloneIndex = currentIndex;
			
		return getCurrent();
	}
	
	public ColorPalette getNext() {
		cloneIndex += 1;
		if (cloneIndex > (nLibs - 1)) { 
			cloneIndex = 0;
		}
		return storage.get(cloneIndex);
	}
	
	public ColorPalette getPrev() {
		cloneIndex -= 1;
		if (cloneIndex < 0) { 
			cloneIndex = nLibs - 1;
		}
		return storage.get(cloneIndex);
	}
	
	
	
}
