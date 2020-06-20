package lk.wildgarden;

import lk.generic.colors.ColorSetter;
import lk.generic.particles.ForceField;
import lk.generic.particles.VectorOperation;
import processing.core.PGraphics;
import processing.core.PVector;

public class WGForceField 
	extends ForceField
	implements WGPresets
{
	protected WG garden;
	
	public WGForceField(WG garden) {
		this(garden, garden.width / DEFAULT_DENSITY_X, garden.height / DEFAULT_DENSITY_Y, garden.width, garden.height);
	}
	
	public WGForceField(WG garden, float resW, float resH, float WIDTH, float HEIGHT) {
		super(resW, resH, WIDTH, HEIGHT);
		this.garden = garden;
	}
	
	public void manipulateViaGarden(float velDamping, float scalar) {
		manipulateViaMotion(garden.getControlPos(), garden.getControlVel(), velDamping, scalar);
	}
	
	public void draw(PGraphics surface) {
		draw(surface, DEFAULT_DRAW_MAG_SCALAR, ColorSetter.WHITE, DEFAULT_DRAW_ANGLE_SCALAR, ColorSetter.OFFWHITE);
	}
	
	public void draw(PGraphics surface, float magnitudeScalar, int magnitudeColor, float angleScalar, int angleColor) {
		surface.pushStyle();
		surface.strokeWeight(1);
		
		PVector scale = new PVector(width / resW, height / resH, 0);
		
		for (int i = 0; i < resW; i += 1) {
			for (int j = 0; j < resH; j += 1) {
				int index = j*(int)resW + i;
				
				float startX = i*scale.x;
				float startY = j*scale.y;
				
				float endX = startX + field[index].x*magnitudeScalar;
				float endY = startY + field[index].y*magnitudeScalar;
				
				surface.stroke(magnitudeColor);
				surface.line(startX, startY, endX, endY);
				
				PVector tiltLine = new PVector(endX - startX, endY - startY, 0);
				float angleMag = tiltLine.mag();
				tiltLine.normalize();
				
				VectorOperation.rotateVector2D(tiltLine, 90);
				
				angleMag *= angleScalar;
				
				surface.stroke(angleColor);
				surface.line(startX-tiltLine.x*angleMag, startY-tiltLine.y*angleMag, startX+tiltLine.x*angleMag, startY + tiltLine.y*angleMag);
			}
		}
		surface.popStyle();
	}
	
}

