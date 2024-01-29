package ASPIREAI.customfactories;

import ASPIREAI.customfactories.Keyboard;
import ASPIREAI.customfactories.Mouse;

public interface HasInputDevices {
	Keyboard getKeyboard();

	Mouse getMouse();
}
