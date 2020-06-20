package lk.wildgarden;

import lk.generic.particles.ParticleSystem;
import processing.core.PGraphics;

public class WGCellSystem 
	extends ParticleSystem 
{
	protected WG garden;
	
	///////////////////////////////////////////////////////////////////////////
	public WGCellSystem(WG garden) {
		this(garden, -1);
	}
	
	public WGCellSystem(WG garden, int capacity) {
		super(capacity);
		this.garden = garden;
	}
	
	///////////////////////////////////////////////////////////////////////////
	public void update() {
		super.update();
		for (int i = 0; i < contents.size(); i += 1) {
			try {
				WGCell cell = (WGCell)contents.get(i);
				cell.update();
				if (!cell.isAlive) {
					contents.remove(cell);
				}
			} 
			catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}		
	}
	
	protected void handleBorders() {
		bounceOffWalls(
			0, 0, 
			garden.getCurrentSurface().width, garden.getCurrentSurface().height
		);
	}
	
	public void draw(PGraphics surface) {
		surface.pushMatrix();
			surface.translate(pos.x, pos.y);
			for (int i = 0; i < contents.size(); i += 1) {
				WGCell cell = (WGCell)contents.get(i);
				cell.draw(surface);
			}
		surface.popMatrix();
	}
	
}

