package lk.wildgarden.gui;

import lk.wildgarden.WG;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/*--------------------------------------------------------------------------------
	Cycles a slide-show of images using
	lk.generic.recording.ScreenshotManager

------------------------------------------------------------------------------- */
public class DisplayImageManager 
{
	public static final boolean ALLOW_USER_SCREENSHOTS = false;
	
	protected WG garden;
	protected PImage currentDisplayImage;
	protected PImage nextDisplayImage;
	
	private float imageScalar;
	private int cyclePeriod;
	private boolean isAutoCycle;
	
	public DisplayImageManager(WG garden) {
		this.garden = garden;
		currentDisplayImage = getRandomImage();
		imageScalar = 1f;
		cyclePeriod = 800;
		isAutoCycle = true;
	}
	
	public void update() {
		if (isAutoCycle) {
			if (garden.frameCount % cyclePeriod == 0) {
				nextDisplayImage = getRandomImage();
			}
		}
		if (nextDisplayImage != null && nextDisplayImage.width > 0) {
			currentDisplayImage = nextDisplayImage;
			nextDisplayImage = null;
		}
	}
	
	public void draw(PGraphics surface) {
		if (currentDisplayImage != null && currentDisplayImage.width > 0) {
			int displayW = (int)(currentDisplayImage.width*imageScalar);
			int displayH = (int)(currentDisplayImage.height*imageScalar);
			
			surface.pushMatrix();
				surface.translate(surface.width/2f - displayW/2f, surface.height/2f - displayH/2f);
				surface.image(currentDisplayImage, 0, 0, displayW, displayH);
			surface.popMatrix();
		}
	}
	
	private PImage getRandomImage() {
		if (!ALLOW_USER_SCREENSHOTS) {
			try {
				return garden.screenshotManager.getRandomImage("config/myScreenGrabs.xml");
			}
			catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
		return garden.screenshotManager.getRandomImage();
	}
	
	public void setCyclePeriod(int cyclePeriod) {
		this.cyclePeriod = PApplet.constrain(cyclePeriod, 0, 30000);
	}
	
	public void setAutoCycle(boolean isAutoCycle) {
		this.isAutoCycle = isAutoCycle;
	}
		
	public void requestImage() {
		nextDisplayImage = getRandomImage();
	}
	
}