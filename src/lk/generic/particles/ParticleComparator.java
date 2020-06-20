package lk.generic.particles;

import java.util.Comparator;

import processing.core.PApplet;
import processing.core.PVector;

public class ParticleComparator 
	implements Comparator<Particle> 
{
	protected PApplet app;
	protected PVector arbitraryLoc;
	
	public ParticleComparator(PApplet app) {
		this.app = app;
		arbitraryLoc = new PVector();
	}
	
	public void setArbitraryLoc() {
		arbitraryLoc.set(app.random(0, app.width), app.random(0, app.height), 0);
	}
	
	public void setArbitraryLoc(PVector loc) {
		arbitraryLoc.set(loc);
	}
	
	public int compare(Particle p1, Particle p2) {
		float val1 = PVector.dist(p1.pos, arbitraryLoc);
		float val2 = PVector.dist(p2.pos, arbitraryLoc);
		
		if (val1 < val2) {
			return -1;
		} 
		if (val1 > val2) {
			return 1;
		}
		return 0;
	}
	
}