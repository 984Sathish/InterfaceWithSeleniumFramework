package ASPIREAI.customfactories;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Coordinates;

import ASPIREAI.customfactories.HasTouchScreen;
import ASPIREAI.customfactories.TouchScreen;
import ASPIREAI.customfactories.WebDriverEventListener;

/**
 * A touch screen that fires events.
 */
public class EventFiringTouch implements TouchScreen {

	private final WebDriver driver;
	private final WebDriverEventListener dispatcher;
	private final TouchScreen touchScreen;

	public EventFiringTouch(WebDriver driver, WebDriverEventListener dispatcher) {
		this.driver = driver;
		this.dispatcher = dispatcher;
		this.touchScreen = ((HasTouchScreen) this.driver).getTouch();
	}

	@Override
	public void singleTap(Coordinates where) {
		touchScreen.singleTap(where);
	}

	@Override
	public void down(int x, int y) {
		touchScreen.down(x, y);
	}

	@Override
	public void up(int x, int y) {
		touchScreen.up(x, y);
	}

	@Override
	public void move(int x, int y) {
		touchScreen.move(x, y);
	}

	@Override
	public void scroll(Coordinates where, int xOffset, int yOffset) {
		touchScreen.scroll(where, xOffset, yOffset);
	}

	@Override
	public void doubleTap(Coordinates where) {
		touchScreen.doubleTap(where);
	}

	@Override
	public void longPress(Coordinates where) {
		touchScreen.longPress(where);
	}

	@Override
	public void scroll(int xOffset, int yOffset) {
		touchScreen.scroll(xOffset, yOffset);
	}

	@Override
	public void flick(int xSpeed, int ySpeed) {
		touchScreen.flick(xSpeed, ySpeed);
	}

	@Override
	public void flick(Coordinates where, int xOffset, int yOffset, int speed) {
		touchScreen.flick(where, xOffset, yOffset, speed);
	}
}
