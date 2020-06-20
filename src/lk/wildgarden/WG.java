package lk.wildgarden;

import java.util.HashMap;
import java.util.Iterator;

import lk.generic.colors.ColorLibrary;
import lk.generic.colors.ColorSetter;
import lk.generic.colors.XMLColorLibrary;
import lk.generic.gui.TextDisplayer;
import lk.generic.input.InputOperation;
import lk.generic.input.InputDevice;
import lk.generic.input.WiimoteResponder;
import lk.generic.recording.ScreenshotManager;
import lk.wildgarden.gui.WGButton;
import lk.wildgarden.gui.WGConsole;
import lk.wildgarden.gui.WGTheme;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;
import processing.xml.XMLElement;
import codeanticode.glgraphics.GLConstants;

/*-----------------------------------------------------------------------------------------
	WildGarden -- main program
	Lev Kanter 2010
		
---------------------------------------------------------------------------------------- */
public class WG 
	extends PApplet 
	implements WGPresets, WiimoteResponder
{
	private static final long serialVersionUID = 2433978351592424055L;

	///////////////////////////////////////////////////////////////////////////	
	public static void main(String _args[]) {
		PApplet.main(new String[] {
			"--present", "--hide-stop", "--exclusive", 
			lk.wildgarden.WG.class.getName() 
		});
	}
	
	///////////////////////////////////////////////////////////////////////////	
	/*----------------------------------------------
		Core properties and assets
	
	--------------------------------------------- */
	private String compTitle;
	
	public static PFont smallFont;
	public static int smallFontSize;
	public static PFont bigFont;
	public static int bigFontSize;
	
	public ScreenshotManager screenshotManager;
	private String screenshotDir; //<-- where screenshots are saved 
	private String screenshotInfoPath; //<-- path to XML file that stores screenshot paths
	private final boolean allowScreenshotOverwrite = false; 
	
	private HashMap<String, WGButton> pausedButtons;
	
	private TextDisplayer fps;
	public WGConsole wgConsole;
		
	/*----------------------------------------------
		Scenes
	
	--------------------------------------------- */
	protected WGScenePainting painting;
	protected WGSceneShowcase showCase;
	
	/*----------------------------------------------
		Input
	
	--------------------------------------------- */
	protected WGScepter scepter;
	private float smoothing;
	
	//current controller position coordinates
	private float wgX;
	private float wgY;
	
	//previous controller position coordinates
	private float pwgX;
	private float pwgY;
	
	/*----------------------------------------------
		State
	
	--------------------------------------------- */
	protected boolean isPaused;
	private WGSceneMode sceneMode;
	private WGInputMode controlMode;
	private int controlTriggerEnergy;
	
	/*----------------------------------------------
		Graphics
		
	--------------------------------------------- */
	public ColorSetter colorSetter;
	protected ColorLibrary colorLibrary;
	protected boolean toDrawCursor;
	
	///////////////////////////////////////////////////////////////////////////
	/*----------------------------------------------
		Setup
	
	--------------------------------------------- */
	public void setup() {
		size(1024, 768, GLConstants.GLGRAPHICS);

		try {
			setupCore();
			setupControlProperties();
			setupGraphics();
			setupScenes();
			setupPausedScreen();
		} 
		catch(WGException wge) {
			System.err.println("Forced exit during app setup()");
			exit();
		}
	}
	
	private void setupCore() 
		throws WGException
	{
		XMLElement rootConfigXML = new XMLElement(this, "config/config.xml");
		
		try {
			compTitle = rootConfigXML.getChild("TITLE").getContent().trim();
		} 
		catch(NullPointerException npe) {
			compTitle = "My-WildGarden-Comp";
		}
		System.out.println(compTitle);
		
		XMLElement initXML = rootConfigXML.getChild("INIT");
		try {
			int initScene = -1 + initXML.getChild("scene").getIntAttribute("which");
			setScene(initScene);
			setPaused(Boolean.parseBoolean(initXML.getChild("scene").getStringAttribute("paused")));
			
			System.out.println("Starting in " + sceneMode + " mode");
			System.out.println();
		} 
		catch(NullPointerException npe) {
			throw new WGException("Could not initialize scenes");
		}
		
		XMLElement pathsXML = rootConfigXML.getChild("PATHS");
		
		//TODO make this better
		try {
			screenshotDir = pathsXML.getChild("screenshot").getContent().trim();
			screenshotInfoPath = pathsXML.getChild("screenshotInfo").getContent().trim();
		} 
		catch(NullPointerException npe) {
			screenshotDir = "";
		}
		screenshotManager = new ScreenshotManager(this, screenshotDir, compTitle);
		screenshotManager.setOutputPath(screenshotInfoPath);
		screenshotManager.setTitle(compTitle);
		
		scepter = new WGScepter(this, rootConfigXML.getChild("WIIMOTE"));
		scepter.plugResponder(this);
				
		try { 
			colorLibrary = new XMLColorLibrary(this, "color/palettes.xml");
		} 
		catch(NullPointerException npe) {
			throw new WGException("Cannot load color library");
		}
	}
	
	private void setupControlProperties() {
		controlMode = WGInputMode.WIIMOTE;
		controlTriggerEnergy = 0;
		smoothing = InputDevice.DEFAULT_SMOOTHING;		
		scepter.setSmoothing(smoothing);
			
		wgX = 0;
		wgY = 0;
		pwgX = 0;
		pwgY = 0;
	}
	
	private void setupGraphics() 
		throws WGException
	{
		try {
			smallFont = loadFont("fonts/EurostileRegular-20.vlw");
			smallFontSize = 20;
			bigFont = loadFont("fonts/EurostileLTStd-24.vlw");
			bigFontSize = 24;
			textFont(smallFont, smallFontSize);
		} 
		catch(Exception ex) {
			throw new WGException("Cannot load fonts", ex);
		}
		
		smooth();
		noCursor();
		noStroke();
		
		colorSetter = new ColorSetter((PApplet)this);
		colorSetter.fg = colorLibrary.getCurrent().getRandom();
		colorSetter.bg = 160;
		colorSetter.a = 0;
		background(DEFAULT_BG);
	
		fps = new TextDisplayer(this, 10, height-40, 145, 30, new WGTheme(this));
		fps.setPadding(6, 6);
		fps.hide();
		
		wgConsole = new WGConsole(this);
	}
	
	private void setupScenes() {
		painting = new WGScenePainting(this);
		showCase = new WGSceneShowcase(this); 	
	}
	
	private void setupPausedScreen() {
		pausedButtons = new HashMap<String, WGButton>();
		
		int counter = 0;
		int pbW = BUTTON_W-40;
		int pbH = BUTTON_H;
		float x = 20;
		float y = 20;
		
		WGButton resume = new WGButton(this, (counter + 1)*x + (counter*pbW), y, pbW, pbH, "Resume");
		resume.toggleCallToAction();
		resume.setAction(this, "resume");
		pausedButtons.put("resume", resume);
		counter += 1;
		
		WGButton screenshot = new WGButton(this, (counter + 1)*x + (counter*pbW), y, pbW, pbH, "Screenshot");
		screenshot.setAction(this, "takeScreenshot");
		pausedButtons.put("screenshot", screenshot);
		counter += 1;
		
		WGButton clear = new WGButton(this, (counter + 1)*x + (counter*pbW), y, pbW, pbH, "New composition");
		clear.setAction(this, "clearComposition");
		pausedButtons.put("clear", clear);
		counter += 1;
		
		WGButton homeScreen = new WGButton(this, (counter + 1)*x + (counter*pbW), y, pbW, pbH, "Home screen");
		homeScreen.setAction(this, "homeScreen");
		pausedButtons.put("homeScreen", homeScreen);
		counter += 1;
	}
	
	///////////////////////////////////////////////////////////////////////////
	/*----------------------------------------------
		Drawing (and updating)
	
	--------------------------------------------- */
	public void draw() {
		updateControl();
		//if(controlMode != WGControlMode.WIIMOTE) { setScene(WGSceneMode.SHOWCASE); }
		
		fps.update(wgX, wgY, isControlPrimaryActionFired(), "fps: " + frameRate);
						
		if (isPaused) {
			drawPaused();
		} 
		else {
			drawUnPaused();
		}
		
		fps.draw();
		
		if (toDrawCursor) { 
			drawCursor(255, true);
		}
	}
	
	private void drawUnPaused() {
		colorSetter.erase(true);
		
		switch (sceneMode) {
			case PAINTING:
				painting.update();
				painting.draw();
				break;
			case SHOWCASE:
				showCase.update();
				showCase.draw();
				break;
			default:
				break;
		}
	}
	
	private void drawPaused() {
		boolean controlTrigger = isControlPrimaryActionFired();
		Iterator<WGButton> iterator = pausedButtons.values().iterator();
		while (iterator.hasNext()) {
			iterator.next().update(wgX, wgY, controlTrigger);
		}
		
		background(DEFAULT_BG);
		image(painting.surface.getTexture(), 0, 0);
		drawModalBar();
		
		pausedButtons.get("resume").draw();
		if (sceneMode == WGSceneMode.PAINTING) {
			pausedButtons.get("screenshot").draw();
			pausedButtons.get("clear").draw();
			pausedButtons.get("homeScreen").draw();
		}
		
		drawCursor(255, true);
	}
	
	protected void drawCursor(int strokeColor) {
		drawCursor(strokeColor, false);
	}
	
	protected void drawCursor(int strokeColor, boolean toCrosshair) {
		pushStyle();
			if (toCrosshair) {
				stroke(strokeColor, 30);
				line(wgX, 0, wgX, height);
				line(0, wgY, width, wgY);
			}
			pushMatrix();
				translate(wgX, wgY);
				noFill();
				stroke(strokeColor);
				strokeWeight(2);
				ellipse(0, 0, 10, 10);
			popMatrix();
		popStyle();
	}
	
	protected void drawModalBox() {
		drawOverlay();
		drawBox();
	}
	
	protected void drawModalBar() {
		drawOverlay();
		drawBar();
	}
	
	protected void drawOverlay() {
		pushStyle();
			noStroke();
			fill(0, MODAL_OVERLAY_ALPHA);
			rect(0, 0, width, height);
		popStyle();
	}
	
	protected void drawBox() {
		pushStyle();
			stroke(20, 90);
			fill(60, 245);
			rect(180, 80, width-360, height-160);
		popStyle();
	}
	
	protected void drawBar() {
		pushStyle();
			stroke(20, 90);
			fill(60, 245);
			rect(0, 0, width, BUTTON_H+40);
		popStyle();
	}
		
	///////////////////////////////////////////////////////////////////////////
	/*----------------------------------------------
		Key/mouse events

	--------------------------------------------- */
	public void keyPressed() {
		if (key == CODED) {
			switch (keyCode) {
				case UP:
					break;
				case RIGHT:
					break;
				case DOWN:
					break;
				case LEFT:
					break;
				default:
					break;
			}
		} else {
			switch (key) {
				case '1':
					//setScene(WGSceneMode.PAINTING);
					break;
				case '2':
					//setScene(WGSceneMode.SHOWCASE);
					break;
					
				case 'f':
				case 'F':
					fps.toggle();
					break;
					
				case 'p':
				case 'P':
					setPaused(!isPaused);
					break;
					
				case 's':
				case 'S':
					takeScreenshot();
					break;
					
				case 'd':
				case 'D':
					//save an image of everything on the screen (not just the painting)
					screenshotManager.takeScreenshot();
					break;
					
				default:
					break;
			}
		}
		
		if (!isPaused) {
			switch (sceneMode) {
				case PAINTING:
					painting.keyPressed(key, keyCode);
					break;
				case SHOWCASE:
					showCase.keyPressed(key, keyCode);
					break;
				default:
					break;
			}
		}
	}
	
	public void keyReleased() {
		if (key == CODED) {
			switch (keyCode) {
				case UP:
					break;
				case RIGHT:
					break;
				case DOWN:
					break;
				case LEFT:
					break;
				default:
					break;
			}
		} else {
			switch (key) {
				default:
					break;
			}
		}
		
		if (!isPaused) {
			switch (sceneMode) {
				case PAINTING:
					painting.keyReleased(key, keyCode);
					break;
				case SHOWCASE:
					showCase.keyReleased(key, keyCode);
					break;
				default:
					break;
			}
		}
	}
	
	public void mousePressed() {
		if (!isPaused) {
			switch (sceneMode) {
				case PAINTING:
					painting.mousePressed();
					break;
				case SHOWCASE:
					showCase.mousePressed();
					break;
				default:
					break;
			}
		}
	}
	
	public void mouseReleased() {		
		if (!isPaused) {
			switch (sceneMode) {
				case PAINTING:
					painting.mouseReleased();
					break;
				case SHOWCASE:
					showCase.mouseReleased();
					break;
				default:
					break;
			}
		}
	}

	public void mouseDragged() {		
		if (!isPaused) {
			switch (sceneMode) {
				case PAINTING:
					painting.mouseDragged();
					break;
				case SHOWCASE:
					showCase.mouseDragged();
					break;
				default:
					break;
			}
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	/*----------------------------------------------
		Wiimote events

	--------------------------------------------- */
	public void buttonHome(int bang) { 
		if (sceneMode == WGSceneMode.PAINTING) {
			togglePaused(bang);
		} 
		else if (sceneMode == WGSceneMode.SHOWCASE)  {
			showCase.buttonHome(bang);
		}
	}
	
	public void buttonA(int bang) { 
		if (!isPaused) {
			switch (sceneMode) {
				case PAINTING: if (!painting.isLocked()) painting.buttonA(bang); break;
				case SHOWCASE: showCase.buttonA(bang); break;
				default: break;
			}
		}
	}
	
	public void buttonB(int bang) { 
		if (!isPaused) {
			switch (sceneMode) {
				case PAINTING: if (!painting.isLocked()) painting.buttonB(bang); break;
				case SHOWCASE: showCase.buttonB(bang); break;
				default: break;
			}
		}
	}
	
	public void button1(int bang) { 
		if (!isPaused) {
			switch (sceneMode) {
				case PAINTING: if (!painting.isLocked()) painting.button1(bang); break;
				case SHOWCASE: showCase.button1(bang); break;
				default: break;
			}
		}
	}
	
	public void button2(int bang) { 
		if (!isPaused) {
			switch (sceneMode) {
				case PAINTING: if (!painting.isLocked()) painting.button2(bang); break;
				case SHOWCASE: showCase.button2(bang); break;
				default: break;
			}
		}
	}
	
	public void buttonMinus(int bang) { 
		if (!isPaused) {
			switch (sceneMode) {
				case PAINTING: if (!painting.isLocked()) painting.buttonMinus(bang); break;
				case SHOWCASE: showCase.buttonMinus(bang); break;
				default: break;
			}
		}
	}
	
	public void buttonPlus(int bang) {
		if (!isPaused) {
			switch (sceneMode) {
				case PAINTING: if (!painting.isLocked()) painting.buttonPlus(bang); break;
				case SHOWCASE: showCase.buttonPlus(bang); break;
				default: break;
			}
		}
	}
	
	public void buttonUp(int bang) { 
		if (!isPaused) {
			switch (sceneMode) {
				case PAINTING: if (!painting.isLocked()) painting.buttonUp(bang); break;
				case SHOWCASE: showCase.buttonUp(bang); break;
				default: break;
			}
		}
	}
	
	public void buttonDown(int bang) { 
		if (!isPaused) {
			switch (sceneMode) {
				case PAINTING: if (!painting.isLocked()) painting.buttonDown(bang); break;
				case SHOWCASE: showCase.buttonDown(bang); break;
				default: break;
			}
		}
	}
	
	public void buttonLeft(int bang) {
		if (!isPaused) {
			switch (sceneMode) {
				case PAINTING: if (!painting.isLocked()) painting.buttonLeft(bang); break;
				case SHOWCASE: showCase.buttonLeft(bang); break;
				default: break;
			}
		}
	}
	
	public void buttonRight(int bang) { 
		if(!isPaused) {
			switch(sceneMode) {
				case PAINTING: if (!painting.isLocked()) painting.buttonRight(bang); break;
				case SHOWCASE: showCase.buttonRight(bang); break;
				default: break;
			}
		}
	}
	
	///////////////////////////////////////////////////////////////////////////
	/*----------------------------------------------
		Utilities

	--------------------------------------------- */
	private void updateControl() {
		pwgX = wgX;
		pwgY = wgY;
		
		switch (controlMode) {
			case WIIMOTE:
				scepter.update(sceneMode);
				if (scepter.isSignal()) {
					wgX = scepter.getPos().x;
					wgY = scepter.getPos().y;
				} else {
					controlMode = WGInputMode.MOUSE;
				}
				break;
			case MOUSE:
			default:
				wgX = InputOperation.smoothData(pwgX, mouseX, smoothing);
				wgY = InputOperation.smoothData(pwgY, mouseY, smoothing);
				
				if (scepter.isSignal()) {
					controlMode = WGInputMode.WIIMOTE;
				}
				break;
		}
		
		if (isControlPrimaryActionFired()) controlTriggerEnergy++;
		else controlTriggerEnergy = 0;
	}
	
	protected int getControlTriggerEnergy() {
		return controlTriggerEnergy;
	}
	
	protected boolean isControlPrimaryActionFired() {
		switch (controlMode) {
			case WIIMOTE:
				return (scepter.isB() || scepter.isA());
			case MOUSE:
			default:
				return mousePressed;
		}
	}
	
	public float getControlX() {
		return wgX;
	}
	
	public float getControlY() {
		return wgY;
	}
	
	public PVector getControlPos() {
		return new PVector(wgX, wgY);
	}
	
	public PVector getControlVel() {
		switch (controlMode) {
			case WIIMOTE:
				return scepter.getVel();
			case MOUSE:
			default:
				return PVector.sub(getControlPos(), new PVector(pwgX, pwgY));
		}
	}
	
	public PVector getCenter() {
		return new PVector(width/2, height/2, 0);
	}
	
	public PVector getRandomPos() {
		return new PVector(random(0, width), random(0, height));
	}
	
	public PGraphics getCurrentSurface() {
		return painting.surface;
	}

	public WGInputMode getControlMode() {
		return controlMode;
	}
	
	protected void nextScene() {
		setScene(sceneMode.ordinal()+1);
	}
	protected void nextScene(int bang) {
		if (bang > 0) nextScene();
	}
	protected void prevScene() {
		setScene(sceneMode.ordinal()-1);
	}
	protected void prevScene(int bang) {
		if (bang > 0) prevScene();
	}
	protected void setScene(int index) {
		WGSceneMode[] scenes = WGSceneMode.values();
		setScene(scenes[PApplet.constrain(index, 0, scenes.length-1)]);
	}
	protected void setScene(WGSceneMode sceneMode) {
		//setting a scene automatically switches out of paused mode
		//and clears the WGConsole
		
		setPaused(false);
		if (painting != null && painting.inProgess() && wgConsole != null) {
			wgConsole.clear();
		}
		if (sceneMode == WGSceneMode.PAINTING) {
			painting.lock();
		}
		if (sceneMode == WGSceneMode.SHOWCASE && controlMode != WGInputMode.WIIMOTE) {
			if (wgConsole != null) {
				showCase.noSignalMessage();
			}
		}
			
		this.sceneMode = sceneMode;
	}
	
	protected void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}
	protected void togglePaused(int bang) {
		if (bang > 0) setPaused(!isPaused);
	}
		
	protected void takeScreenshot() {
		takeScreenshot(painting.getSurfaceImage());
	}
	protected void takeScreenshot(PImage surfaceImage) {
		screenshotManager.takeScreenshot(surfaceImage);
	}
	protected void takeScreenshot(int bang) {
		if (bang > 0) takeScreenshot();
	}
	protected void takeScreenshot(int bang, PImage surfaceImage) {
		if (bang > 0) takeScreenshot(surfaceImage);
	}
	
	@SuppressWarnings("unused")
	private void resume() {
		setPaused(false);
	}
	
	@SuppressWarnings("unused")
	private void clearComposition() {
		painting.clear();
		setPaused(false);
	}
	
	@SuppressWarnings("unused")
	private void homeScreen() {
		setScene(WGSceneMode.SHOWCASE);
	}
	
	///////////////////////////////////////////////////////////////////////////
	/*----------------------------------------------
		Cleanup

	--------------------------------------------- */
	public void stop() {
		if (allowScreenshotOverwrite) {
			screenshotManager.write();
		}
		super.stop();
	}
	
}