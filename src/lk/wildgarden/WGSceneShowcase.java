package lk.wildgarden;

import java.util.HashMap;
import java.util.Iterator;

import lk.generic.colors.XMLColorLibrary;
import lk.generic.gui.Area;
import lk.generic.gui.AreaState;
import lk.generic.gui.ColorPicker;
import lk.generic.gui.ImagedAreaButton;
import lk.generic.util.MyRuntimeException;
import lk.wildgarden.gui.DisplayImageManager;
import lk.wildgarden.gui.WGButton;
import lk.wildgarden.gui.WGConsole;
import lk.wildgarden.gui.WiimoteIndicator;
import processing.core.PImage;

public class WGSceneShowcase 
	extends WGScene
{
	private DisplayImageManager dim;
	private HashMap<String, Area> mainGuiComponents;
	private HashMap<String, Area> colorGuiComponents;
	private WGConsole console;
	private boolean toConfigColors;
					
	public WGSceneShowcase(WG garden) {
		super(garden);
	}
	
	public void setup() {
		dim = new DisplayImageManager(garden);
		mainGuiComponents = new HashMap<String, Area>();
		colorGuiComponents = new HashMap<String, Area>();
		
		int counter = 0;
		float bcolX = width/2 + 10;
		float bcolY = 140;
		float bgutY = 20;
		
		WGButton newButton = new WGButton(garden, bcolX, bcolY + (WG.BUTTON_H + bgutY)*counter, "New composition");
		newButton.setAction(this, "newComposition");
		counter += 1;
		
		WGButton colorsButton = new WGButton(garden, bcolX, bcolY + (WG.BUTTON_H + bgutY)*counter, "Configure colors");
		colorsButton.setAction(this, "configColors");
		counter += 1;
		
		WGButton continueButton = new WGButton(garden, bcolX, bcolY+(WG.BUTTON_H + bgutY)*counter, "Resume composition");
		continueButton.setAction(this, "continueComposition");
		continueButton.hide();
		counter += 1;
		
		WiimoteIndicator wiiIndicator = new WiimoteIndicator(garden, garden.scepter, 200, 100);
		
		console = garden.wgConsole;
				
		mainGuiComponents.put("new", newButton);
		mainGuiComponents.put("colors", colorsButton);
		mainGuiComponents.put("continue", continueButton);
		mainGuiComponents.put("wii", wiiIndicator);	
		
		counter = 0;
		bcolY = 20;
		bcolX = 20;
				
		WGButton finish = new WGButton(garden, (counter + 1)*bcolX + (counter*WG.BUTTON_W), bcolY, "Use these colors");
		finish.setAction(this, "finishConfigColors");
		finish.toggleCallToAction();
		counter += 1;
		
		WGButton cancel = new WGButton(garden, (counter + 1)*bcolX + (counter*WG.BUTTON_W), bcolY, "Cancel");
		cancel.setAction(this, "cancelConfigColors");
		counter += 1;
		
		ColorPicker bgPicker = new ColorPicker(
			garden,
			20, 310,
			0, 0,
			54, 54, 4,
			new XMLColorLibrary(garden, "color/backgrounds.xml").getCurrent()
		);
		
		ColorPicker pPicker = new ColorPicker(
			garden,
			580, 180,
			0, 0,
			20, 20, 12,
			garden.colorLibrary.getCurrent()
		);
		pPicker.setSelectionOption(false);
		
		PImage next_def = garden.loadImage("images/arrows/arrow_next_def.png");
		PImage next_hov = garden.loadImage("images/arrows/arrow_next_hov.png");
		PImage next_foc = garden.loadImage("images/arrows/arrow_next_foc.png");
		PImage next_act = garden.loadImage("images/arrows/arrow_next_act.png");
		ImagedAreaButton cpNext = new ImagedAreaButton(garden, next_def, next_hov, next_foc, next_act);
		cpNext.setCoordinatesFromCorner(840, 180, next_def.width, next_def.height);
		cpNext.setFireOnRelease();
		cpNext.setAction(this, "nextColorPalette");
		
		PImage prev_def = garden.loadImage("images/arrows/arrow_prev_def.png");
		PImage prev_hov = garden.loadImage("images/arrows/arrow_prev_hov.png");
		PImage prev_foc = garden.loadImage("images/arrows/arrow_prev_foc.png");
		PImage prev_act = garden.loadImage("images/arrows/arrow_prev_act.png");
		ImagedAreaButton cpPrev = new ImagedAreaButton(garden, prev_def, prev_hov, prev_foc, prev_act);
		cpPrev.setCoordinatesFromCorner(460, 180, prev_def.width, prev_def.height);
		cpPrev.setFireOnRelease();
		cpPrev.setAction(this, "prevColorPalette");
	
		colorGuiComponents.put("finish", finish);
		colorGuiComponents.put("cancel", cancel);
		colorGuiComponents.put("bgPicker", bgPicker);
		colorGuiComponents.put("pPicker", pPicker);		
		colorGuiComponents.put("nextPalette", cpNext);
		colorGuiComponents.put("prevPalette", cpPrev);
	}
	
	public void update() {
		boolean connected = (garden.scepter.isSignal()) ? true: false; 
		float wgX = garden.getControlX();
		float wgY = garden.getControlY();
		boolean controlTrigger = garden.isControlPrimaryActionFired();
		boolean inProgress = garden.painting.inProgess();

		if (!connected) {
			//Disable button control
			wgX = wgY = 0;
			controlTrigger = false;
			noSignalMessage();
		}
						
		if (toConfigColors) {
			Iterator<Area> iterator = colorGuiComponents.values().iterator();
			while (iterator.hasNext()) {
				iterator.next().update(wgX, wgY, controlTrigger);
			}
		}
		else {
			dim.update();
			
			Iterator<Area> iterator = mainGuiComponents.values().iterator();
			while (iterator.hasNext()) {
				iterator.next().update(wgX, wgY, controlTrigger);
			}
					
			WGButton newButton = (WGButton)mainGuiComponents.get("new");
			if (!connected || inProgress) newButton.setCallToAction(false);
			else newButton.setCallToAction(true);
			
			if (newButton.getState() == AreaState.HOVER && !inProgress) {
				console.clear().addMessage("Press the trigger and release to begin painting");
			}
			
			WGButton colorsButton = (WGButton)mainGuiComponents.get("colors");
			WGButton continueButton = (WGButton)mainGuiComponents.get("continue");
					
			if (inProgress) {
				if (connected) {
					continueButton.setCallToAction(true);
				} else {
					continueButton.setCallToAction(false);
				}
				if (!continueButton.isVisible()) {
					continueButton.swapCoordinates(colorsButton);
					continueButton.show();
				}
			} 
			if (!inProgress && continueButton.isVisible()) {
				continueButton.swapCoordinates(colorsButton);
				continueButton.hide();
			}			
		}
		
		console.update(wgX, wgY, controlTrigger);
	}
	
	public void draw() {
		garden.background(DEFAULT_BG);
		
		if (toConfigColors) {
			garden.pushStyle();
				garden.fill(160, 60);
				garden.rect(0, 0, width, height);
			garden.popStyle();
			garden.drawBar();
			Iterator<Area> iterator = colorGuiComponents.values().iterator();
			while (iterator.hasNext()) {
				Area guiComponent = iterator.next();
				guiComponent.draw();
			}
			
			garden.pushStyle();
				garden.noStroke();
				garden.fill(255);
				garden.textFont(WG.bigFont, WG.bigFontSize);
				garden.text("Current background color", 20, 160);
				garden.text("Current brush palette", 580, 160);
				ColorPicker bgPicker = (ColorPicker)colorGuiComponents.get("bgPicker");
//				if (bgPicker.getState()==AreaState.HOVER) {
//					console.flush().addMessage("Choose a background color");
//				}
				int currentBGTemp;
				try {
					currentBGTemp = bgPicker.getCurrent().getColor();
				}
				catch (MyRuntimeException mge) { 
					currentBGTemp = garden.colorSetter.bg;
				}
				garden.stroke(0);
				garden.strokeWeight(1);
				garden.fill(currentBGTemp);
				garden.rect(20, 180, 60, 60);
				garden.noStroke();
				garden.fill(255);
				garden.textFont(WG.smallFont, WG.smallFontSize);
				garden.text("Choose a background color", 20, 290);
			garden.popStyle();
			
		}
		else {
			dim.draw(garden.g);
			garden.drawModalBox();
			Iterator<Area> iterator = mainGuiComponents.values().iterator();
			while (iterator.hasNext()) {
				iterator.next().draw();
			}
		}
		
		console.draw();
												
		garden.toDrawCursor = true;
	}
		
	public void keyPressed(int key, int keyCode) {
		
		if (key == WG.CODED) {
			switch (keyCode) {
				case WG.UP:
					break;
				case WG.DOWN:
					break;
				case WG.RIGHT:
					break;
				case WG.LEFT:
					break;
				default:
					break;
			}
		} else {
			switch (key) {
				case WG.ENTER:
				case WG.RETURN:
					dim.requestImage();
					break;
					
				default:
					break;
			}
		}
	}
	
	public void keyReleased(int key, int keyCode) {}
	public void mousePressed() {}
	public void mouseReleased() {}
	public void mouseDragged() {}
	
	public void buttonHome(int bang) {
		if (bang > 0) {
			if (!toConfigColors) console.addMessage("Home button: Toggle pause screen and menu");
		}
	}
	public void buttonA(int bang) {
		if (bang > 0) {
			if (!toConfigColors) console.addMessage("A button: Change current paintbrush behavior");
		}
	}
	public void buttonB(int bang) {
		if (bang > 0) {
			if (!toConfigColors) console.addMessage("B button (trigger): Create a new paint stroke (Activate a button when not in paint mode)");
		}
	}
	public void button1(int bang) {
		
	}
	public void button2(int bang) {}
	public void buttonMinus(int bang) {}
	public void buttonPlus(int bang) {}
	public void buttonUp(int bang) {
		if (bang > 0) {
			if (!toConfigColors) console.addMessage("Up button: Make paint surface more transparent");
		}
	}
	public void buttonDown(int bang) {
		if (bang > 0) {
			if (!toConfigColors) console.addMessage("Down button: Make paint surface more opaque");
		}
	}
	public void buttonLeft(int bang) {
		if (bang > 0) {
			if (!toConfigColors) console.addMessage("Left button: Decrease current paintbrush size");
		}
	}
	public void buttonRight(int bang) {
		if (bang > 0) {
			if (!toConfigColors) console.addMessage("Right button: Increase current paintbrush size");
		}
	}
	
	protected void noSignalMessage() {
		console.clear().addMessage("Hold down buttons 1 & 2 on your Wiimote to connect", "warning");
	}
	
	/*--------------------------------------------------------------------------
		Button actions
 
	------------------------------------------------------------------------- */
	@SuppressWarnings("unused")
	private void newComposition() {
		garden.setScene(WGSceneMode.PAINTING);
		garden.painting.clear();
	}
	
	@SuppressWarnings("unused")
	private void continueComposition() {
		garden.setScene(WGSceneMode.PAINTING);
	}
	
	@SuppressWarnings("unused")
	private void configColors() {
		toConfigColors = true;
		console.clear();
	}
	
	@SuppressWarnings("unused")
	private void finishConfigColors() {
		try {
			ColorPicker bgPicker = (ColorPicker)colorGuiComponents.get("bgPicker");
			garden.colorSetter.bg = bgPicker.getCurrent().getColor();
		}
		catch (MyRuntimeException mre) { 
			
		}
		garden.colorLibrary.setCurrentIndex(garden.colorLibrary.getCloneIndex());
		toConfigColors = false;
	}
	
	@SuppressWarnings("unused")
	private void cancelConfigColors() {
		toConfigColors = false;
	}
	
	@SuppressWarnings("unused")
	private void nextColorPalette() {
		ColorPicker nextPicker = (ColorPicker)colorGuiComponents.get("pPicker");
		nextPicker.load(garden.colorLibrary.getNext());
	}
	
	@SuppressWarnings("unused")
	private void prevColorPalette() {
		ColorPicker prevPicker = (ColorPicker)colorGuiComponents.get("pPicker");
		prevPicker.load(garden.colorLibrary.getPrev());
	}
	
}
