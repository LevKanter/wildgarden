package lk.wildgarden;

import java.util.ArrayList;

import lk.generic.colors.ColorOperation;
import lk.generic.particles.FlockHandle;
import lk.generic.util.GraphicsOperation;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import codeanticode.glgraphics.GLGraphicsOffScreen;

public class WGScenePainting 
	extends WGScene
{
	private boolean isInProgress;
	private boolean toDrawCursor;
	
	protected GLGraphicsOffScreen surface;
	
	public static final int PAINT_CAPACITY = 2;
	
	private ArrayList<WGCellStroke> paint;
	private int activeStrokeIndex;
	
	private float generalCellSize;
	
	public WGScenePainting(WG garden) {
		super(garden);
	}
	
	public void setup() {
		toDrawCursor = true;
		
		//off-screen OpenGL buffer with 4x anti-aliasing
		surface = new GLGraphicsOffScreen((PApplet)garden, (int)width, (int)height, true, 4);
		paint = new ArrayList<WGCellStroke>(PAINT_CAPACITY);
		clear();
		
		generalCellSize = 4;
	}
		
	public void update() {
		unlock();
		
		for (int i = 0; i<paint.size(); i += 1) {
			WGCellStroke stroke = paint.get(i);
			stroke.update();
			
			if (i == activeStrokeIndex) continue;
			
			//shiftColor(stroke, 100f);
			for (int j = 0; j<stroke.size(); j += 1) {
				WGCell cell = (WGCell)(stroke.getContents().get(j));
				
			}
		}
		
		if (activeStrokeIndex > -1) {
			try { 
				WGCellStroke activeStroke = paint.get(activeStrokeIndex); 
				
				if (garden.scepter.isA()) {
					FlockHandle handle = activeStroke.getHandle();
					
					//handle.manipulate(garden, garden.scepter.getAccelPacket());
					
					float coin = garden.random(0, 1);
					if (coin > 0.5f) {
						float moreCoin = garden.random(0, 1);
						if (moreCoin >.66f) {
							handle.moreSeperation();
						} else if (moreCoin > .33f) {
							handle.moreAlignment();
						} else {
							handle.moreCohesion();
						}
					} 
					else {
						float lessCoin = garden.random(0, 1);
						if (lessCoin >.66f) {
							handle.lessSeperation();
						} else if (lessCoin > .33f) {
							handle.lessAlignment();
						} else {
							handle.lessCohesion();
						}
						
					}
					
					activeStroke.setHandle(handle);
				}
				
				if (garden.scepter.isRight()) {
					increaseSizes(activeStroke);
				} 
				else if (garden.scepter.isLeft()) {
					decreaseSizes(activeStroke);
				}
				
				for (int i = 0; i < activeStroke.size(); i += 1) {
					WGCell cell = (WGCell)(activeStroke.getContents().get(i));
				}
			}
			catch (IndexOutOfBoundsException iobe) { 
				
			}
			
			if (garden.scepter.isUp()) {
				garden.colorSetter.raiseAlpha();
			}
			else if (garden.scepter.isDown()) {
				garden.colorSetter.lowerAlpha();
			}
		}
	}
	
	public void draw() {
		surface.beginDraw();
			surface.smooth();	
			garden.colorSetter.erase(surface);		
			for (int i = 0; i < paint.size(); i += 1) {
				paint.get(i).draw(surface);
			}		
		surface.endDraw();
		garden.image(surface.getTexture(), 0, 0);
		
		if (toDrawCursor) {
			garden.toDrawCursor = true;
		} else {
			garden.toDrawCursor = false;
		}
	}
	
	public void keyPressed(int key, int keyCode) {
		
		if (key == WG.CODED) {
			switch (keyCode) {
				case WG.UP:
					break;
				case WG.DOWN:
					break;
				case WG.RIGHT:
					break;
				case WG.LEFT:
					break;
				default:
					break;
			}
		} else {
			switch (key) {
				case WG.ENTER:
				case WG.RETURN:
					clear();
					break;
					
				case 'c':
				case 'C':
					toDrawCursor = !toDrawCursor;
					break;
					
				default:
					break;
			}
		}
	}
	
	public void keyReleased(int key, int keyCode) {}
	public void mousePressed() {}
	public void mouseReleased() {}
	public void mouseDragged() {}
	
	private void spawnNewStroke() {
		activeStrokeIndex += 1;
		if (activeStrokeIndex >= PAINT_CAPACITY) {
			activeStrokeIndex = 0;
		}
		
		WGCellStroke nextStroke = new WGCellStroke(garden, 600);
		nextStroke.setFollowControl(true);
	
		for (int i = 0; i < nextStroke.getCapacity(); i += 1) {
			WGCell cell = new WGCell(garden);
			cell.size = generalCellSize;
			
			cell.randomizeDrag();
			cell.pos.set(garden.getControlPos());
			cell.acc.set(new PVector(garden.random(-1f, 1f), garden.random(-1f, 1f)));
			nextStroke.add(cell);
		}
		
		if (paint.size() == PAINT_CAPACITY) {
			paint.set(activeStrokeIndex, nextStroke);
		} else {
			paint.add(nextStroke);
		}
		shiftColor(nextStroke, garden.random(8f, 40f));
		
		setInProgress(true);
	}
	
	public void clear() {
		clearSurface(true);
		paint.clear();
		setInProgress(false);
		activeStrokeIndex = -1;
	}
	
	public PImage getSurfaceImage() {
		return getSurfaceImage(true);
	}
	
	/**
	 * Obtain a PImage copy of the off-screen graphics buffer
	 * 
	 */
	public PImage getSurfaceImage(boolean opaque) {
		int format;
		if (opaque) format = PApplet.RGB;
		else format = PApplet.ARGB;
		
		PImage surfaceImage = garden.createImage(surface.width, surface.height, format);
		surface.getTexture().getImage(surfaceImage);
		return surfaceImage;
	}
	
	protected void clearSurface(boolean opaque) {
		if (opaque) {
			surface.beginDraw();
			garden.colorSetter.erase(surface, true); 
			surface.endDraw();
		} else {
			GraphicsOperation.setAllPixelsTransparent(surface);
		}
	}
	
	public boolean inProgess() {
		return isInProgress;
	}
	
	public void setInProgress(boolean isInProgress) {
		this.isInProgress = isInProgress;
	}

	/*----------------------------------------------
		Wiimote events

	--------------------------------------------- */
	public void buttonHome(int bang) {garden.togglePaused(bang);}
	
	public void buttonA(int bang) { }
	
	public void buttonB(int bang) { 
		if (bang > 0) {
			if (!isLocked) {
				spawnNewStroke();
			}
		}
	}
	
	public void button1(int bang) { }
	
	public void button2(int bang) { }
	
	public void buttonMinus(int bang) { }
	
	public void buttonPlus(int bang) { }
	
	public void buttonUp(int bang) { }
	
	public void buttonDown(int bang) { }
	
	public void buttonLeft(int bang) { }
	
	public void buttonRight(int bang) { }
	
	/*----------------------------------------------
		Paint utilities

	--------------------------------------------- */
	private void increaseSizes(WGCellStroke stroke) {
		//generalCellSize += .3f;
			
		for (int i = 0; i < stroke.size(); i += 1) {
			WGCell cell = (WGCell)stroke.getContents().get(i);
			cell.size += garden.random(.15f, .3f);
		}
	}
	
	private void decreaseSizes(WGCellStroke stroke) {
		//generalCellSize -= .3f;
		
		for (int i = 0; i < stroke.size(); i += 1) {
			WGCell cell = (WGCell)stroke.getContents().get(i);
			cell.size -= garden.random(.15f, .3f);
		}
	}
	
	private void shiftColor(WGCellStroke stroke, float distribution) {
		
		int fc1 = garden.colorLibrary.getCurrent().getRandom();
		
		for (int i = 0; i < stroke.size(); i += 1) {
			WGCell cell = (WGCell)stroke.getContents().get(i);
			
			int fcg = ColorOperation.fastGreen(fc1) + (int)garden.random(-distribution,distribution);
			int fcr = ColorOperation.fastRed(fc1) + (int)garden.random(-distribution,distribution);
			int fcb = ColorOperation.fastBlue(fc1) + (int)garden.random(-distribution,distribution);
			
			cell.fillColor = garden.color(fcr, fcg, fcb);
		}
	}
	
}
