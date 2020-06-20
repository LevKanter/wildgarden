package lk.generic.gui;

import java.lang.reflect.Method;
import java.util.Arrays;

import processing.core.PApplet;
import processing.core.PGraphics;

public class AreaButton 
	extends Area
{
	protected int triggerEnergy;
	protected int maxEnergy;
	protected boolean toBang;
	
	private Method actionMethod;
	private Object actionObject;
	protected Object[] actionParameters;
	private boolean isActionSet;
	
	public AreaButton(PApplet app, float cornerX, float cornerY, float w, float h) {
		super(app, cornerX, cornerY, w, h);
		triggerEnergy = 0;
		setFireOn(1);
	}
	
	public boolean ready() {return toBang;}
	public int getCurrentEnergy() {return triggerEnergy;}
	
	public float getEnergyPercentage() {
		if (maxEnergy > 0) {
			return 100f*((float)triggerEnergy/(float)maxEnergy);
		}
		System.out.println("There is no max energy -- this button will only fire a bang when released");
		return 0f;
	}
	
	public void setFireOn(int maxEnergy) {
		this.maxEnergy = maxEnergy;
	}
	
	public void setFireOnRelease() {
		setFireOn(-1);
	}
	
	public void setAction(Object obj, String methodName) {
		setAction(obj, methodName, (Class<?>[])null);
	}
	
	public void setAction(Object obj, String methodName, Class<?>[] methodParTypes) {
		try {
			actionObject = obj;
			
			/*--------------------------------------------------------------------------
				Try to insure that the method is accessible 
				(so private methods can be attached to buttons)
				Have to loop through all declared methods because simply using
				getMethod() won't retrieve the method if it is not accessible
			
			------------------------------------------------------------------------- */
			Method[] methods = actionObject.getClass().getDeclaredMethods();
			for (int i = 0; i < methods.length; i += 1) {
				if (methodName.equals(methods[i].getName())) {
					boolean found = false;
					
					if (methodParTypes == null && methods[i].getParameterTypes().length == 0) {
						found = true;
					} 
					else if (Arrays.equals(methods[i].getParameterTypes(), methodParTypes)) {
						found = true;
					}
					
					if (found) {
						methods[i].setAccessible(true);
						actionMethod = methods[i];
						actionParameters = (Object[])null;
						isActionSet = true;
						break;
					}
				}
			}			
		}
		catch (Exception ex) {
			System.err.println(ex.toString());
		}
	}
	
	public void update(float x, float y, boolean shouldActivate) {
		super.update(x, y, shouldActivate);
		if (state == AreaState.ACTIVE) {
			if (!shouldActivate) {
				toBang = true;
			} else {
				triggerEnergy += 1;
				if (maxEnergy > 0 && triggerEnergy > maxEnergy) {
					toBang = true;
				}
			}
		} else {
			triggerEnergy = 0;
			toBang = false;
		}
		
		if (ready()) {
			if (isActionSet) {
				try {
					updateActionParameters();
					actionMethod.invoke(actionObject, actionParameters);
				} 
				catch (Exception ex) {
					System.err.println(ex.toString());
				}
			}
			resetState();
		}
	}
	
	protected void updateActionParameters() { }
		
	public void draw(PGraphics surface) {
		drawPlane(surface);
	}
	
}
