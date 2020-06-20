package lk.generic.particles;

import processing.core.PApplet;

/*--------------------------------------------------------------------------
	Manages flocking parameters
 
------------------------------------------------------------------------- */
public class FlockHandle 
{	
	public float velLimit;
	public float accLimit;
	
	public float sepRange;
	public float aliRange;
	public float cohRange;
	
	public float sepScalar;
	public float aliScalar;
	public float cohScalar;
	
	public int neighborRange;
	
	public FlockHandle() {
		setDefaultParams();
	}
	
	public FlockHandle(
			float velLimit, float accLimit, 
			float sepRange, float aliRange, float cohRange, 
			float sepScalar, float aliScalar, float cohScalar,
			int neighborRange) 
	{	
		setParams(
			velLimit, accLimit, 
			sepRange, aliRange, cohRange, 
			sepScalar, aliScalar, cohScalar, 
			neighborRange);		
	}
	
	public void setDefaultParams() {
		setParams(2.8f, 2.05f, 25, 25, 25, 8, 1, 1, 8);
	}
	
	public void setParams(
			float velLimit, float accLimit, 
			float sepRange, float aliRange, float cohRange, 
			float sepScalar, float aliScalar, float cohScalar,
			int neighborRange) 
	{
		this.velLimit = velLimit;
		this.accLimit = accLimit;
		
		this.sepRange = sepRange;
		this.aliRange = aliRange;
		this.cohRange = cohRange;
		
		this.sepScalar = sepScalar;
		this.aliScalar = aliScalar;
		this.cohScalar = cohScalar;
		
		this.neighborRange = neighborRange;
	}
	
	public void manipulate(PApplet app, float [] packet) {
		float[] scalars = { 1, 1, 1, 1 };
		manipulate(app, packet, scalars);
	}
		
	public void manipulate(PApplet app, float[] packet, float[] scalars) {
		try {
			float pitch = (packet[0] - 0.5f)*scalars[0];
			float roll = (packet[1] - 0.5f)*scalars[1];
			float yaw = (packet[2] - 0.5f)*scalars[2];
			float accel = (packet[3])*scalars[3];
			
			aliScalar += pitch;
			cohScalar += roll;
			aliScalar += yaw;
			accLimit += accel;
		} 
		catch (NullPointerException npe) {
			System.out.println(npe.getMessage());
		} 
		catch (ArrayIndexOutOfBoundsException aiob) {
			System.out.println(aiob.getMessage());
		}
	}
	
	public void moreSeperation() {
		sepScalar += 1;
	}
	public void moreAlignment() {
		aliScalar += 1;
	}
	public void moreCohesion() {
		cohScalar += 1;
	}
	
	public void lessSeperation() {
		sepScalar -= 1;
	}
	public void lessAlignment() {
		aliScalar -= 1;
	}
	public void lessCohesion() {
		cohScalar -= 1;
	}
	
	
	
}
