package lk.generic.gui;

import java.util.HashMap;

public class AreaGroup 
{
	protected HashMap<String, Area> components;
	
	public AreaGroup() {
		components = new HashMap<String, Area>();
	}
	
	public HashMap<String, Area> getComponents() {
		return components;
	}
	
	public Area getComponent(String key) {
		Area component = components.get(key);
		if(component == null) {
			System.out.println("Component [" + key + "] not found");
		}
		return component;
	}
	
	public void addComponent(String key, Area component) {
		components.put(key, component);
	}
	
}
