package ASPIREAI.customfactories;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Coordinates;

import ASPIREAI.customfactories.HasInputDevices;
import ASPIREAI.customfactories.Mouse;
import ASPIREAI.customfactories.WebDriverEventListener;

/**
 * A mouse that fires events.
 */
public class EventFiringMouse implements Mouse {
	private final WebDriver driver;
	private final WebDriverEventListener dispatcher;
	private final Mouse mouse;

	public EventFiringMouse(WebDriver driver, WebDriverEventListener dispatcher) {
		this.driver = driver;
		this.dispatcher = dispatcher;
		this.mouse = ((HasInputDevices) this.driver).getMouse();
	}

	@Override
	public void click(Coordinates where) {
		mouse.click(where);
	}

	@Override
	public void doubleClick(Coordinates where) {
		mouse.doubleClick(where);
	}

	@Override
	public void mouseDown(Coordinates where) {
		mouse.mouseDown(where);
	}

	@Override
	public void mouseUp(Coordinates where) {
		mouse.mouseUp(where);
	}

	@Override
	public void mouseMove(Coordinates where) {
		mouse.mouseMove(where);
	}

	@Override
	public void mouseMove(Coordinates where, long xOffset, long yOffset) {
		mouse.mouseMove(where, xOffset, yOffset);
	}

	@Override
	public void contextClick(Coordinates where) {
		mouse.contextClick(where);
	}
}
