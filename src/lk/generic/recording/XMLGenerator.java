package lk.generic.recording;

import java.io.PrintWriter;
import java.util.ArrayList;
import processing.core.PApplet;

public class XMLGenerator 
{
	
	String tabsString(int n) {
		StringBuilder s = new StringBuilder("");
		for (int i = 0; i < n; i += 1) {
			s.append("\t");
		}
		return s.toString();
	}
	
	public static class Attribute {
		String key, value;
		
		Attribute(String key, String value) {
			this.key = key;
			this.value = value;
		}
		
		public String toString() {
			return toString(true);
		}
		
		public String toString(boolean addSpace) {
			String str = key + "='" + value + "'";
			if (addSpace) {
				str += " ";
			}
			return str;
		}
	}
	
	public XMLGenerator.Attribute createAttribute(String key, String value) {
		return new XMLGenerator.Attribute(key, value);
	}
	
	abstract class XMLComponent {
		String name;
		ArrayList<Attribute> attributes;
		XMLBlock parent;
		int tabLevel;
		
		XMLComponent(String name) {
			this.name = name;
			attributes = new ArrayList<Attribute>(3);
			parent = null;
			tabLevel = 0;
		}
		
		void addAttribute(String k, String v) {
			addAttribute(new Attribute(k, v));
		}
		
		void addAttribute(Attribute attr) {
			attributes.add(attr);
		}
		
		String attributesString() {
			StringBuilder s = new StringBuilder("");
			for (int i = 0; i < attributes.size(); i += 1) {
				if (i == attributes.size() - 1) {
					s.append(attributes.get(i).toString(false));
				} else {
					s.append(attributes.get(i).toString(true));
				}
			}
			return s.toString();
		}
		
		String openString() {
			StringBuilder s = new StringBuilder(tabsString(tabLevel) + "<" + name);
			if (attributes.size() > 0) {
				s.append(" " + attributesString());
			}
			s.append(">");
			return s.toString();
		}
		
		String closeString() {
			return "</" + name + ">\r\n";
		}
	}
	
	class XMLUnit 
		extends XMLComponent 
	{ 
		String content;
		
		XMLUnit(String name, String content) {
			super(name);
			this.content = content;
		}
		
		public String toString() {
			return openString() + content + closeString();
		}
	}
	
	class XMLBlock
		extends XMLComponent
	{
		ArrayList<XMLComponent> children;
		
		XMLBlock(String name) {
			super(name);
			children = new ArrayList<XMLComponent>();
		}
		
		void addChild(XMLComponent child) {
			child.parent = this;
			child.tabLevel = tabLevel + 1; 
			children.add(child);
		}
		
		String openString() {
			return super.openString() + "\r\n";
		}
		
		String closeString() {
			return tabsString(tabLevel) + super.closeString();
		}
		
		public String toString() {
			StringBuilder s = new StringBuilder(openString());
			for (int i = 0; i < children.size(); i += 1) {
				s.append(children.get(i).toString());
			}
			s.append(closeString());
			return s.toString();
		}
	}
	
	PrintWriter scribe;
	
	XMLBlock masterBlock, currentBlock; 
	
	public XMLGenerator(PApplet app, String title) {
		this(app, title, null);
	}
	
	public XMLGenerator(PApplet app, String title, String description) {
		scribe = app.createWriter(title + ".xml");
		scribe.println("<?xml version='1.0'?>\r\n");
		if (description != null) {
			scribe.println("<!-- " + description + " -->\r\n");
		}
		masterBlock = new XMLBlock(title);
		currentBlock = masterBlock;
	}
	
	public void pushTag(String name) {
		XMLBlock tag = new XMLBlock(name);
		currentBlock.addChild(tag);
		currentBlock = tag;
	}
		
	public void popTag() {
		if (currentBlock.parent != null) {
			currentBlock = currentBlock.parent;
		}
	}
	
	public void addUnit(String name, String content) {
		addUnit(name, content, null);
	}
	
	public void addUnit(String name, Attribute[] attributes) {
		addUnit(name, "", attributes);
	}
	
	public void addUnit(String name, String content, Attribute[] attributes) {
		XMLUnit unit = new XMLUnit(name, content);
		if (attributes != null) {
			for (int i = 0; i < attributes.length; i += 1) {
				unit.addAttribute(attributes[i]);
			}
		}
		currentBlock.addChild(unit);
	}
	
	public void printout() {
		printout(null);
	}
	
	public void printout(String completionMessage) {
		scribe.println(masterBlock.toString());
		scribe.flush();
		scribe.close();
		if (completionMessage != null) { 
			System.out.println(completionMessage);
		}
	}
	
}