package lk.generic.particles;

import processing.core.PApplet;
import processing.core.PVector;

public class ForceField 
{
	protected PVector[] field;
	
	protected float resW;
	protected float resH;
	protected float width;
	protected float height;
	
	///////////////////////////////////////////////////////////////////////////	
	public ForceField(float resW, float resH, float width, float height) {
		this.resW = resW;
		this.resH = resH;
		this.width = width;
		this.height = height;
		
		field = new PVector[(int)(resW*resH)];
		
		for (int i = 0; i < field.length; i += 1) {
			field[i] = new PVector();
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	public void reset() {
		scale(0);
	}
		
	public void scale(float multiplier) {
		for (int i = 0; i < field.length; i += 1) {
			field[i].mult(multiplier);
		}
	}
	
	public void randomize(float scalar) {
		for (int i = 0; i < field.length; i += 1) {
			float x = scalar*PApplet.map((float)Math.random(), 0, 1, -1, 1);
			float y = scalar*PApplet.map((float)Math.random(), 0, 1, -1, 1);
			field[i].set(x, y, 0);
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	public PVector getForce(PVector pos, float damping) {
		PVector force = new PVector();
		
		float xPercent = pos.x / (float)width;
		float yPercent = pos.y / (float)height;
		
		if ( (xPercent < 0 || xPercent > 1) || (yPercent < 0 || yPercent > 1) ) {
			return force;
		}
		
		int xIndex = (int)(xPercent*resW);
		int yIndex = (int)(yPercent*resH);
		
		xIndex = PApplet.max(0, PApplet.min(xIndex, (int)resW-1));
		yIndex = PApplet.max(0, PApplet.min(yIndex, (int)resH-1));
		
		int index = yIndex*(int)resW + xIndex;
		
		force.set(field[index].x*damping, field[index].y*damping, 0);
		
		return force;
	}

	///////////////////////////////////////////////////////////////////////////	
	protected void manipulateViaMotion(PVector pos, PVector vel, float velDamping, float scalar) {
		vel.mult(velDamping);
		
		float radius;
		float w = width/resW;
		float h = height/resH;
		if (w > h) {
			radius = w;
		} else {
			radius = h;
		}
		
		manipulate(pos, vel, radius, scalar);
	}
	
	public void manipulate(PVector pos, PVector vel, float radius, float scalar) {
		
		float xPercent = pos.x/width; 
		float yPercent = pos.y/height;
		float rPercent = radius/width;
		
		int xIndex = (int)(xPercent*resW);
		int yIndex = (int)(yPercent*resH);
		radius = (float)(rPercent*resW); 
		
		int startX = (int)PApplet.max(xIndex - radius, 0); 
		int startY = (int)PApplet.max(yIndex - radius, 0);
		int endX =   (int)PApplet.min(xIndex + radius, resW);
		int endY =   (int)PApplet.min(yIndex + radius, resH);	
				
		for (int i = startX; i < endX; i += 1) {
			for (int j = startY; j < endY; j += 1) {
				int index = j*(int)resW + i;
				
				float distance = (float)PApplet.sqrt((xIndex - i)*(xIndex - i) + (yIndex - j)*(yIndex - j));
				
				if (distance <= 0) { 
					distance = 0.0001f;
				}
				
				if (distance < radius) {
					scalar *= (1.0f - distance/radius);
					
					float vX = field[index].x;
					float vY = field[index].y;
					
					vX += (vel.x*scalar);
					vY += (vel.y*scalar);
										
					field[index].set(vX, vY, 0);
				}
			}
		}
	}
	
}
