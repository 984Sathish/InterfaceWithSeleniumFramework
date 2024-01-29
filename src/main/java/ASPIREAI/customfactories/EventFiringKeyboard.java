package ASPIREAI.customfactories;

import org.openqa.selenium.WebDriver;

/**
 * A keyboard firing events.
 */
public class EventFiringKeyboard implements Keyboard {
	private final WebDriver driver;
	private final WebDriverEventListener dispatcher;
	private final Keyboard keyboard;

	/**
	 * @param driver
	 * @param dispatcher
	 */
	public EventFiringKeyboard(WebDriver driver, WebDriverEventListener dispatcher) {
		this.driver = driver;
		this.dispatcher = dispatcher;
		this.keyboard = ((HasInputDevices) this.driver).getKeyboard();

	}

	/**
	 *
	 */
	@Override
	public void sendKeys(CharSequence... keysToSend) {
		keyboard.sendKeys(keysToSend);
	}

	/**
	 *
	 */
	@Override
	public void pressKey(CharSequence keyToPress) {
		keyboard.pressKey(keyToPress);
	}

	/**
	 *
	 */
	@Override
	public void releaseKey(CharSequence keyToRelease) {
		keyboard.releaseKey(keyToRelease);
	}
}
