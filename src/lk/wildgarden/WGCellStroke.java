package lk.wildgarden;

import java.util.ArrayList;
import java.util.Collections;

import lk.generic.particles.FlockHandle;
import lk.generic.particles.Particle;
import lk.generic.particles.ParticleComparator;
import lk.generic.particles.VectorOperation;
import processing.core.PVector;

public class WGCellStroke 
	extends WGCellSystem 
{
	private FlockHandle handle;
	protected ParticleComparator sorter;
	protected boolean toFollowControl;
	protected float followScalar;

	////////////////////////////////////////////////////////////////////////////
	public WGCellStroke(WG garden) {
		this(garden, 200);
	}

	public WGCellStroke(WG garden, int capacity) {
		this(garden, capacity, new FlockHandle());
	}
	
	public WGCellStroke(WG garden, int capacity, FlockHandle handle) {
		super(garden, capacity);
		sorter = new ParticleComparator(garden);
		this.handle = handle;
		toFollowControl = false;
		followScalar = 0.1f;
	}

	public void update() {
		super.update();
		
		/*--------------------------------------------------------------------------
			Constrain the amount of particles that are flocked against
			
			Instead of doing something like "binning" -- I'm just sorting
			the particles according to their distances to some arbitrary point.
			I've decided to do it this way because I want to handle situations
			where there are a lot of particles very close together (and therefore
			would be likely to all end up in the same "bin") 
			
		------------------------------------------------------------------------- */
		sorter.setArbitraryLoc();
		Collections.sort(contents, sorter);
		
		for (int i = 0; i < contents.size(); i += 1) {
			ArrayList<Particle> neighborhood = new ArrayList<Particle>(handle.neighborRange);
			int nNeighbors;

			// first half of range forward
			nNeighbors = 0;
			for (int j = i + 1; j < contents.size(); j += 1) {
				nNeighbors += 1;
				if (nNeighbors > handle.neighborRange / 2) {
					break;
				}
				neighborhood.add(contents.get(j));
			}
			// then half of range backward
			nNeighbors = 0;
			for (int j = i - 1; j >= 0; j -= 1) {
				nNeighbors += 1;
				if (nNeighbors > handle.neighborRange / 2) {
					break;
				}
				neighborhood.add(contents.get(j));
			}

			WGCell current = (WGCell)contents.get(i);
			current.flock(neighborhood, handle);
			if(toFollowControl) {
				PVector following = VectorOperation.steer(current, garden.getControlPos());
				following.mult(followScalar);
				current.acc.add(following);
			}
			current.update();
		}
	}
	
	public void setFollowControl(boolean toFollowControl) {
		this.toFollowControl = toFollowControl;
	}

	/*--------------------------------------------------------------------------
		FlockHandle operations

	------------------------------------------------------------------------- */
	public FlockHandle getHandle() {
		return handle;
	}
	
	public void setHandle(FlockHandle handle) {
		this.handle = handle;
	}
	
	public void setDefaultParams() {
		handle.setDefaultParams();
	}
	
	public void manipulateAlgorithm(float[] packet) {
		handle.manipulate(garden, packet);
	}
	
	public void manipulateAlgorithm(float[] packet, float[] scalars) {
		handle.manipulate(garden, packet, scalars);
	}
	
}
