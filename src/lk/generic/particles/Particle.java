package lk.generic.particles;

import java.util.ArrayList;

import lk.generic.particles.VectorOperation.FlockingRule;
import processing.core.PVector;

public abstract class Particle 
{
	public PVector pos;
	public PVector vel;
	public PVector acc;
	public PVector drag;
	public float velLimit;
	private int age;
	
	public Particle() {
		pos = new PVector();
		vel = new PVector();
		acc = new PVector();
		drag = new PVector();
		velLimit = -1;
		age = 0;
	}

	///////////////////////////////////////////////////////////////////////////
	protected void age() {
		age += 1;
	}
	
	public int getAge() {
		return age;
	}
	
	///////////////////////////////////////////////////////////////////////////
	public void update() {
		applyPhysics();
		handleBorders();
		age();
	}
	
	private void applyPhysics() {
		vel.add(acc);
		
		if (velLimit > 0) { 
			vel.limit(velLimit);
		}
		if (drag.x > 0) {
			vel.x *= drag.x;
		}
		if (drag.y > 0) {
			vel.y *= drag.y;
		}
		
		pos.add(vel);
		
		acc.mult(0);
	}
				
	///////////////////////////////////////////////////////////////////////////
	/*--------------------------------------------------------------------------
		Borders
	------------------------------------------------------------------------- */
	
	protected abstract void handleBorders();
	
	protected void bounceOffWalls(float minX, float minY, float maxX, float maxY) {
		if (pos.x < minX || pos.x > maxX) {
			pos.x = (pos.x < minX) ? minX : maxX;
			if (drag.x != 0) {
				vel.x *= drag.x;
			}
			vel.x *= -1;
		}
		if (pos.y < minY || pos.y > maxY) {
			pos.y = (pos.y < minY) ? minY : maxY;
			if (drag.y != 0) {
				vel.y *= drag.y;
			}
			vel.y *= -1;
		}
	}
	
	protected void wrapAroundWalls(float minX, float minY, float maxX, float maxY) {
		if (pos.x < minX) {
			pos.x = maxX;
		}
		if (pos.x > maxX) {
			pos.x = minX;
		}
		if (pos.y < minY) {
			pos.y = maxY;
		}
		if (pos.y > maxY) {
			pos.y = minY;
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	/*--------------------------------------------------------------------------
		Flocking 
	------------------------------------------------------------------------- */
	
	public void flock(ArrayList<Particle> neighborhood) {		
		flock(neighborhood, new FlockHandle());
	}
	
	public void flock(ArrayList<Particle> neighborhood, FlockHandle flockHandle) {
		velLimit = flockHandle.velLimit;
			
		PVector sep = VectorOperation.steerInFlock(this, neighborhood, flockHandle, FlockingRule.SEPARATION);
		PVector ali = VectorOperation.steerInFlock(this, neighborhood, flockHandle, FlockingRule.ALIGNMENT);
		PVector coh = VectorOperation.steerInFlock(this, neighborhood, flockHandle, FlockingRule.COHESION);
		
		acc.add(sep);
		acc.add(ali);
		acc.add(coh);	    
	}
	
}
