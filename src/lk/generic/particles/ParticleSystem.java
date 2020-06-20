package lk.generic.particles;

import java.util.ArrayList;

public abstract class ParticleSystem 
	extends Particle 
{
	protected ArrayList<Particle> contents;
	
	/**
	 * A limit on the number of particles (set to negative if there is no limit)
	 * 
	 */
	protected int capacity;
	
	public ParticleSystem() {
		this(-1);
	}
	
	public ParticleSystem(int capacity) {
		contents = new ArrayList<Particle>();
		this.capacity = capacity;
		if (capacity > 0) {
			contents.ensureCapacity(capacity);
		}
	}
				
	public void add(Particle p) {
		if (capacity >= 0 && contents.size() > capacity) {
			handleOverflow(p);
			return;
		}
		contents.add(p);
	}
	
	public ArrayList<Particle> getContents() {
		return contents;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public int size() {
		return contents.size();
	}
	
	public void clear() {
		contents.clear();
	}
	
	protected void handleOverflow(Particle p) {
		clear();
	}
	
}
