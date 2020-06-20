package lk.wildgarden;

import java.util.ArrayList;
import java.util.Collections;

import lk.generic.colors.ColorOperation;
import lk.generic.particles.Particle;
import processing.core.PApplet;
import processing.core.PGraphics;

/*--------------------------------------------------------------------------
	The core particle of paint 
 
------------------------------------------------------------------------- */
public class WGCell 
	extends Particle 
	implements WGPresets 
{
	protected WG garden;
	
	public float size;
	public int fillColor;
	
	protected boolean isAlive;
	protected int lifeSpan;
	
	///////////////////////////////////////////////////////////////////////////
	public WGCell(WG garden) {
		this(garden, false);
	}
	
	public WGCell(WG garden, boolean leaveTrail) {
		super();
		this.garden = garden;
		
		lifeSpan = DEFAULT_LIFESPAN;
		isAlive = true;
		size = DEFAULT_CELL_SIZE;
		
		fillColor = DEFAULT_FILL_COLOR;
	}
	
	///////////////////////////////////////////////////////////////////////////
	public void kill() {
		isAlive = false;
	}
			
	protected void handleBorders() {
		//dieInTheVoid(0, 0, garden.getCurrentSurface().width, garden.getCurrentSurface().height);
		bounceOffWalls(0, 0, garden.getCurrentSurface().width, garden.getCurrentSurface().height);
		//wrapAroundWalls(0, 0, garden.getCurrentSurface().width, garden.getCurrentSurface().height);
	}
	
	protected void dieInTheVoid(float minX, float minY, float maxX, float maxY) {
		if (pos.x < minX || pos.x > maxX || pos.y < minY || pos.y > maxY) {
			kill();
		}
	}
	
	public void randomizeDrag() {
		drag.x = garden.random(0.9f, 1f);
		drag.y = garden.random(0.9f, 1f);
	}
	
	public void update() {
		if (isAlive) {
			if (getAge() > lifeSpan) {
				kill();
				return;
			}
			super.update();
		}
		if (size < .001f) { 
			size = .001f;
		}
		else if (size > 20) {
			size = 20;
		}
	}
		
	public void draw(PGraphics surface) {
		if (isAlive) {
			surface.pushStyle();
				style(surface);
				surface.pushMatrix();
					surface.translate(pos.x, pos.y);
					renderTo(surface);
				surface.popMatrix();
			surface.popStyle();
		}
	}
	
	protected void style(PGraphics surface) {
		surface.fill(fillColor);
		surface.noStroke();
	}
	
	protected void renderTo(PGraphics surface) {
		surface.ellipse(0, 0, size, size);
		//randomlyModulateColor();
		//surface.ellipse(garden.random(-6, 6), garden.random(-6, 6), size, size);
		//surface.fill(200, 0, 0);
		//surface.ellipse(1, 1, size-1, size-1);
		//darkenColor();
		//renderAsTriangles(surface);
	}
	
		protected void renderAsTriangles(PGraphics surface) {
			surface.rotate(vel.heading2D()+(PApplet.PI/2));
			surface.beginShape(PApplet.TRIANGLES);
		    	surface.vertex(0, -size);
		    	surface.vertex(-size, size);
		    	surface.vertex(size, size);
		    surface.endShape();
		}
		
	/*public void randomlyModulateColor() {
		Integer fcr = Integer.valueOf(ColorOperation.fastRed(fillColor));
		Integer fcg = Integer.valueOf(ColorOperation.fastGreen(fillColor));
		Integer fcb = Integer.valueOf(ColorOperation.fastBlue(fillColor));
		
		ArrayList<Integer> packet = new ArrayList<Integer>(3);
		packet.add(fcr);
		packet.add(fcg);
		packet.add(fcb);
		
		Integer max = Collections.max(packet);
		
		
		
		//fillColor = (int) PApplet.lerp(fillColor, garden.color(fcg, fcr, fcb), .9f);
	
		fillColor = garden.color(fcg, fcr, fcb);
	}*/

}
