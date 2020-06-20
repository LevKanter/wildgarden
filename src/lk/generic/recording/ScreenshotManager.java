package lk.generic.recording;

import java.io.IOException;
import java.io.PrintWriter;

import lk.generic.util.MyRuntimeException;
import processing.core.PApplet;
import processing.core.PImage;
import processing.xml.XMLElement;
import processing.xml.XMLWriter;

/*--------------------------------------------------------------------------
	ScreenshotManager handles saving images, 
	writing their respective paths to an XML file,
	using that XML file to retrieve the images later
	
------------------------------------------------------------------------- */
public class ScreenshotManager
	extends FrameSaver
{
	protected XMLElement baseXMLElement;
	private String outputPath;
		
	public ScreenshotManager(PApplet app, String directory, String titlePrefix) {
		super(app, directory, titlePrefix);
		
		baseXMLElement = new XMLElement();
		baseXMLElement.setName("SCREENSHOTS");
	}
	
	/*----------------------------------------------
	 	Retrieval
	 	
	 --------------------------------------------- */	
	public PImage getRandomImage() {
		return getRandomImage(outputPath);
	}
		
	public PImage getRandomImage(String imagesXMLPath) {
		XMLElement xmlElement = new XMLElement(app, imagesXMLPath);
		int randomIndex = PApplet.floor(app.random(0, xmlElement.getChildCount()));
		try {
			return app.requestImage(xmlElement.getChildren()[randomIndex].getContent());
		}
		catch (ArrayIndexOutOfBoundsException aiobe) {
			System.out.println("No screenshots were found");
		}
		return null;
	}
	
	public PImage[] getAllImages() {
		return getAllImages(outputPath);
	}
	
	public PImage[] getAllImages(String imagesXMLPath) {
		XMLElement xmlElement = new XMLElement(app, imagesXMLPath);
		int nImages = xmlElement.getChildCount();
		PImage[] images = new PImage[nImages];
		for (int i = 0; i < nImages; i += 1) {
			try {
				images[i] = app.requestImage(xmlElement.getChildren()[i].getContent());
			}
			catch (ArrayIndexOutOfBoundsException aiobe) {
				System.out.println("No screenshots were found");
				return null;
			}
		}
		return images;
	}
	
	/*----------------------------------------------
 		Recording
 		
 	--------------------------------------------- */
	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}
	
	public void update(PImage surfaceImage) {
		if (isRunning) {
			String path = directory + title + "-" + app.frameCount + "." + format;
			surfaceImage.save(path);
			XMLElement screenshot = new XMLElement();
			screenshot.setName("screenshot");
			screenshot.setContent(path);
			baseXMLElement.addChild(screenshot);			
			System.out.println("Saved " + path);
			count += 1;
		}
	}
	public void update() {
		update(app.g);
	}
		
	public void write() {
		try {
			if (baseXMLElement.getChildCount() > 0) {
				String tryOutputPath;
				if (outputPath != null) {
					tryOutputPath = outputPath;
				} else {
					tryOutputPath = "";
				}
				PrintWriter output = app.createWriter(tryOutputPath);
				output.println("<?xml version='1.0'?>");
				write(new XMLWriter(output), baseXMLElement);
			}
		}
		catch (MyRuntimeException me) {
			
		}
	}
	
	private void write(XMLWriter xmlWriter, XMLElement xmlElement) 
		throws MyRuntimeException
	{
		try { 
			xmlWriter.write(xmlElement, true); //<-- "true" for prettyPrint
		}
		catch (IOException ioe) {
			throw new MyRuntimeException("Could not write XML file", ioe);
		}
	}
	
}
