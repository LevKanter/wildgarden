package lk.generic.particles;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class VectorOperation
{	
	private VectorOperation() {}
	
	///////////////////////////////////////////////////////////////////////////
	public static void rotateVector2D(PVector vec, float degrees) {
		rotateVector2D_Radians(vec, PApplet.radians(degrees));
	}
	
	public static void rotateVector2D_Radians(PVector vec, float theta) {
		float rX = vec.x*PApplet.cos(theta) - vec.y*PApplet.sin(theta);
		float rY = vec.x*PApplet.sin(theta) - vec.y*PApplet.cos(theta);
		
		vec.x = rX;
		vec.y = rY;
	}
	
	///////////////////////////////////////////////////////////////////////////
	public static PVector steer(Particle p, Particle p2) {
		return steer(p, p2.pos);
	}
	
	public static PVector steer(Particle p, PVector to) {
		PVector target = PVector.sub(to, p.pos);
		float distance = target.mag();
		 
		if (distance <= 0) {
			return new PVector();
		}
		
		target.normalize();
		return PVector.sub(target, p.vel);		
	}
	
	///////////////////////////////////////////////////////////////////////////
	public enum FlockingRule 
	{
		SEPARATION,
		ALIGNMENT,
		COHESION
	};
		
	public static PVector steerInFlock(Particle p, ArrayList<Particle> neighbors, FlockingRule rule) {
		return steerInFlock(p, neighbors, new FlockHandle(), rule);
	}

	/**
	 * The core flocking algorithm
	 * 
	 * based on an example by Daniel Shiffman
	 * --> http://www.shiffman.net/itp/classes/nature/week06_s09/flocking/
	 * 
	 */
	public static PVector steerInFlock(Particle p, ArrayList<Particle> neighbors, FlockHandle flockHandle, FlockingRule rule) {
		PVector vec = new PVector();
		
		float range;
		switch (rule) {
		case SEPARATION:
			range = flockHandle.sepRange;
			break;
		case ALIGNMENT:
			range = flockHandle.aliRange;
			break;
		case COHESION:
			range = flockHandle.cohRange;
			break;
		default:
			return vec;
		}
		
		int nValidNeighbors = 0;
		for (int i = 0; i < neighbors.size(); i += 1) {
			Particle other = neighbors.get(i);
			float distance = p.pos.dist(other.pos);
			if (distance > 0 && distance < range) {
				switch (rule) {
				case SEPARATION:
					PVector diff = PVector.sub(p.pos, other.pos);
					diff.normalize();
					diff.div(distance);
					vec.add(diff);
					break;
				case ALIGNMENT:
					vec.add(other.vel);
					break;
				case COHESION:
					vec.add(other.pos);
					break;
				default:
					return new PVector();
				}
				nValidNeighbors += 1;
			}
		}
		
		if (nValidNeighbors > 0) {
			vec.div((float)nValidNeighbors);
			
			switch (rule) {
				case SEPARATION:
					//...
					break;
				case ALIGNMENT:
					//...
					break;
				case COHESION:
					vec = steer(p, vec);
					break;
				default:
					return new PVector();
			}
			vec.limit(flockHandle.accLimit);
		}
			
		float scalar;
		switch (rule) {
			case SEPARATION:
				scalar = flockHandle.sepScalar;
				break;
			case ALIGNMENT:
				scalar = flockHandle.aliScalar;
				break;
			case COHESION:
				scalar = flockHandle.cohScalar;
				break;
			default:
				scalar = 1;
				break;
		}
		vec.mult(scalar);
		
		return vec;
	}
	
}
