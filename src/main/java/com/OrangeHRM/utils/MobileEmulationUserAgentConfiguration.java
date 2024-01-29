package com.OrangeHRM.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to set the Mobile emulation configuration on chrome browser
 * 
 * @see <a href=
 *      "http://www.webapps-online.com/online-tools/user-agent-strings/dv">User
 *      Agent String</a> for more info on the available user agent string.
 * @see <a href=
 *      "https://sites.google.com/a/chromium.org/chromedriver/mobile-emulation">Mobile
 *      emulation on chrome</a> for more info on chrome mobile emulation.
 * 
 */
public class MobileEmulationUserAgentConfiguration {

	public static final String SM_GALAXY_TAB4_AND5_1_LANDSCAPE = "galaxy_tab4_android5.1_chrome_landscape(1280*800)";
	public static final String SM_GALAXY_TAB4_AND5_1_PORTRAIT = "galaxy_tab4_android5.1_chrome_portrait(800*1280)";
	public static final String SM_GALAXY_TAB4_7_AND4_4_2_LANDSCAPE = "galaxy_tab4_7_android4.4.2_chrome_landscape(961*600)";
	public static final String SM_GALAXY_TAB4_7_AND4_4_2_PORTRAIT = "galaxy_tab4_7_android4.4.2_chrome_portrait(600*961)";
	public static final String APPLE_IPAD4_IOS9_LANDSCAPE = "ipad4_ios9_safari_landscape(1024*768)";
	public static final String APPLE_IPAD4_IOS9_PORTRAIT = "ipad4_ios9_safari_portrait(768*1024)";
	public static final String APPLE_IPAD4_IOS10_LANDSCAPE = "ipad4_ios10_safari_landscape(1024*768)";
	public static final String APPLE_IPAD4_IOS10_PORTRAIT = "ipad4_ios10_safari_portrait(768*1024)";

	public static final String SM_GALAXY_TAB4_AND4_4_2_LANDSCAPE = "galaxy_tab4_android4.4.2_chrome_landscape(1280*800)";
	public static final String SM_GALAXY_TAB4_AND4_4_2_PORTRAIT = "galaxy_tab4_android4.4.2_chrome_portrait(800*1280)";
	public static final String SM_GALAXY_TAB3_AND4_2_2_LANDSCAPE = "galaxy_tab3_android4.2.2_chrome_landscape(1280*800)";
	public static final String SM_GALAXY_TAB3_AND4_2_2_PORTRAIT = "galaxy_tab3_android4.2.2_chrome_portrait(800*1280)";
	public static final String APPLE_IPAD4_IOS8_LANDSCAPE = "ipad4_ios8_safari_landscape(1024*768)";
	public static final String APPLE_IPAD4_IOS8_PORTRAIT = "ipad4_ios8_safari_portrait(768*1024)";
	public static final String APPLE_IPAD4_IOS7_LANDSCAPE = "ipad4_ios7_safari_landscape(1024*768)";
	public static final String APPLE_IPAD4_IOS7_PORTRAIT = "ipad4_ios7_safari_portrait(768*1024)";

	public static final String CHROME_BOOK = "chromebook(1366*768)";


	private final Map<String, String> samsung_galaxy_tab_5_1_landscape = Map.of(
			"userAgent",
					"Mozilla/5.0 (Linux; Android 5.1.1; SM-T335 Build/LMY47X) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.83 Safari/537.36",
			"width", "1280",
			"height", "800",
			"pixelRatio", "2"
			);

	private final Map<String, String> samsung_galaxy_tab_5_1_portrait = Map.of(
			"userAgent",
					"Mozilla/5.0 (Linux; Android 5.1.1; SM-T335 Build/LMY47X) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.83 Safari/537.36",
			"width", "800",
			"height", "1280",
			"pixelRatio", "2"
			);

	private final Map<String, String> samsung_galaxy_tab_4_4_2_landscape = Map.of(
			"userAgent",
					"Mozilla/5.0 (Linux; Android 4.4.2; SM-T230NU Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.93 Safari/537.36",
			"width", "961",
			"height", "600",
			"pixelRatio", "2"
			);

	private final Map<String, String> samsung_galaxy_tab_4_4_2_portrait = Map.of(
			"userAgent",
					"Mozilla/5.0 (Linux; Android 4.4.2; SM-T230NU Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.93 Safari/537.36",
			"width", "600",
			"height", "961",
			"pixelRatio", "2"
			);

	private final Map<String, String> samsung_galaxy_tab4_landscape = Map.of(
			"userAgent",
					"Mozilla/5.0 (Linux; Android 4.4.2; SM-T531 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.84 Safari/537.36",
			"width", "1280",
			"height", "800",
			"pixelRatio", "2"
			);

	private final Map<String, String> samsung_galaxy_tab4_portrait = Map.of(
			"userAgent",
					"Mozilla/5.0 (Linux; Android 4.4.2; SM-T531 Build/KOT49H) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.84 Safari/537.36",
			"width", "800",
			"height", "1280",
	        "pixelRatio", "2"
			);

	private final Map<String, String> samsung_galaxy_tab3_landscape = Map.of(
			"userAgent",
					"Mozilla/5.0 (Linux; Android 4.2.2; SM-T110 Build/JDQ39) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.94 Safari/537.36",
			"width", "1280",
			"height", "800",
			"pixelRatio", "2"
			);

	private final Map<String, String> samsung_galaxy_tab3_portrait = Map.of(
			"userAgent",
					"Mozilla/5.0 (Linux; Android 4.2.2; SM-T110 Build/JDQ39) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.94 Safari/537.36",
			"width", "800",
			"height", "1280",
			"pixelRatio", "2"
			);

	private final Map<String, String> apple_ipad4_ios9_landscape = Map.of(
			"userAgent",
					"Mozilla/5.0 (iPad; CPU OS 9_3_5 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13G34 Safari/601.1",
			"width", "1024",
			"height", "768",
			"pixelRatio", "2"
			);
	
	private final Map<String, String> apple_ipad4_ios9_portrait = Map.of(
			"userAgent",
					"Mozilla/5.0 (iPad; CPU OS 9_3_5 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13G34 Safari/601.1",
			"width", "768",
			"height", "1024",
			"pixelRatio", "2"
			);

	private final Map<String, String> apple_ipad4_ios10_landscape = Map.of(
			"userAgent",
					"Mozilla/5.0 (iPad; CPU OS 10_0_2 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0 Mobile/14A456 Safari/602.1",
			"width", "1024",
			"height", "768",
		    "pixelRatio", "2"
			);
	
	private final Map<String, String> apple_ipad4_ios10_portrait = Map.of(
			"userAgent",
					"Mozilla/5.0 (iPad; CPU OS 10_0_2 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) Version/10.0 Mobile/14A456 Safari/602.1",
			"width", "768",
			"height", "1024",
			"pixelRatio", "2"
			);

	private final Map<String, String> apple_ipad4_ios8_landscape = Map.of(
			"userAgent",
					"Mozilla/5.0 (iPad; CPU OS 8_0 like Mac OS X) AppleWebKit/600.1.3 (KHTML, like Gecko) Version/8.0 Mobile/12A4345d Safari/600.1.4",
			"width", "1024",
			"height", "768",
			"pixelRatio", "2"
			);
	
	private final Map<String, String> apple_ipad4_ios8_portrait = Map.of(
			"userAgent",
					"Mozilla/5.0 (iPad; CPU OS 8_0 like Mac OS X) AppleWebKit/600.1.3 (KHTML, like Gecko) Version/8.0 Mobile/12A4345d Safari/600.1.4",
			"width", "768",
			"height", "1024",
			"pixelRatio", "2"
			);

	private final Map<String, String> apple_ipad4_ios7_landscape = Map.of(
			"userAgent",
			"Mozilla/5.0 (iPad; CPU OS 7_0_2 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A501 Safari/9537.53",
			"width", "1024",
			"height", "768",
			"pixelRatio", "2"
			);

	private final Map<String, String> apple_ipad4_ios7_portrait = Map.of(
			"userAgent",
			"Mozilla/5.0 (iPad; CPU OS 7_0_2 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) Version/7.0 Mobile/11A501 Safari/9537.53",
			"width", "768",
			"height", "1024",
			"pixelRatio", "2"
			);

	private final Map<String, String> chrome_book = Map.of(
			"userAgent", "Mozilla/5.0 (X11; CrOS x86_64 8350.68.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36",
			"width", "1366",
			"height", "768",
			"pixelRatio", "2"
			);

	/**
	 * To storing the all the devices configurations
	 *
	 * @return userAgentData - have mobile emulation configuration data(user agent,
	 *         width, height and pixelRatio)
	 */
	public Map<String, Map<String, String>> setUserAgentConfigurationValue() {
		Map<String, Map<String, String>> userAgentData = new HashMap<>();
		
		userAgentData.put(SM_GALAXY_TAB4_AND5_1_LANDSCAPE, samsung_galaxy_tab_5_1_landscape);
		userAgentData.put(SM_GALAXY_TAB4_AND5_1_PORTRAIT, samsung_galaxy_tab_5_1_portrait);
		userAgentData.put(SM_GALAXY_TAB4_7_AND4_4_2_LANDSCAPE, samsung_galaxy_tab_4_4_2_landscape);
		userAgentData.put(SM_GALAXY_TAB4_7_AND4_4_2_PORTRAIT, samsung_galaxy_tab_4_4_2_portrait);

		userAgentData.put(SM_GALAXY_TAB4_AND4_4_2_LANDSCAPE, samsung_galaxy_tab4_landscape);
		userAgentData.put(SM_GALAXY_TAB4_AND4_4_2_PORTRAIT, samsung_galaxy_tab4_portrait);
		userAgentData.put(SM_GALAXY_TAB3_AND4_2_2_LANDSCAPE, samsung_galaxy_tab3_landscape);
		userAgentData.put(SM_GALAXY_TAB3_AND4_2_2_PORTRAIT, samsung_galaxy_tab3_portrait);
		
		userAgentData.put(APPLE_IPAD4_IOS9_LANDSCAPE, apple_ipad4_ios9_landscape);
		userAgentData.put(APPLE_IPAD4_IOS9_PORTRAIT, apple_ipad4_ios9_portrait);
		userAgentData.put(APPLE_IPAD4_IOS10_LANDSCAPE, apple_ipad4_ios10_landscape);
		userAgentData.put(APPLE_IPAD4_IOS10_PORTRAIT, apple_ipad4_ios10_portrait);

		
		userAgentData.put(APPLE_IPAD4_IOS8_LANDSCAPE, apple_ipad4_ios8_landscape);
		userAgentData.put(APPLE_IPAD4_IOS8_PORTRAIT, apple_ipad4_ios8_portrait);
		userAgentData.put(APPLE_IPAD4_IOS7_LANDSCAPE, apple_ipad4_ios7_landscape);

		userAgentData.put(APPLE_IPAD4_IOS7_PORTRAIT, apple_ipad4_ios7_portrait);
		userAgentData.put(CHROME_BOOK, chrome_book);
		
		return userAgentData;
	}
	
	/**
	 * To get the user agent string from device name
	 * 
	 * @param deviceName - device name which going to perform a mobile emulation on
	 *                   chrome
	 * @return dataToBeReturned- device width
	 */
	public String getUserAgent(String deviceName) {
		String dataToBeReturned = null;
		Map<String, Map<String, String>> getUserAgent = setUserAgentConfigurationValue();
		dataToBeReturned = hasDeviceName(deviceName) ? (String) getUserAgent.get(deviceName).get("userAgent") : null;
		return dataToBeReturned;
	}

	/**
	 * To get the device width string from device name
	 * 
	 * @param deviceName - device name which going to perform a mobile emulation on
	 *                   chrome
	 * @return
	 */
	public String getDeviceWidth(String deviceName) {
		String dataToBeReturned = null;
		Map<String, Map<String, String>> getDeviceWidth = setUserAgentConfigurationValue();
		dataToBeReturned = hasDeviceName(deviceName) ? (String) getDeviceWidth.get(deviceName).get("width") : null;
		return dataToBeReturned;
	}

	/**
	 * To get the device height string from device name
	 * 
	 * @param deviceName - device name which going to perform a mobile emulation on
	 *                   chrome
	 * @return dataToBeReturned- device height
	 */
	public String getDeviceHeight(String deviceName) {
		String dataToBeReturned = null;
		Map<String, Map<String, String>> getDeviceHeight = setUserAgentConfigurationValue();
		dataToBeReturned = hasDeviceName(deviceName) ? (String) getDeviceHeight.get(deviceName).get("height") : null;
		return dataToBeReturned;
	}

	/**
	 * To get the device pixel ratio string from device name
	 * 
	 * @param deviceName - device name which going to perform a mobile emulation on
	 *                   chrome
	 * @return dataToBeReturned - device pixel ratio
	 */
	public String getDevicePixelRatio(String deviceName) {
		String dataToBeReturned = null;
		Map<String, Map<String, String>> getDevicePixelRatio = setUserAgentConfigurationValue();
		dataToBeReturned = hasDeviceName(deviceName) ? (String) getDevicePixelRatio.get(deviceName).get("pixelRatio")
				: null;
		return dataToBeReturned;
	}

	/**
	 * To check the device name present in the set up key hash map
	 * 
	 * @param deviceName - device name which going to perform a mobile emulation on
	 *                   chrome
	 * @return boolean value - if device name in the set up key will return true,
	 *         otherwise false
	 */
	private boolean hasDeviceName(String deviceName) {
		Map<String, Map<String, String>> hasDeviceName = setUserAgentConfigurationValue();
		return hasDeviceName.containsKey(deviceName);
	}

	/**
	 * To get the device name from mobile emulation attributes
	 * 
	 * @param userAgent  - mapped user agent string with device name
	 * @param pixelRatio - mapped pixel ratio with device name
	 * @param width      - mapped width with device name
	 * @param height     - mapped height with device name
	 * @return dataToBeReturned - device name mapped with user agent, pixel ratio,
	 *         width and height
	 */
	public String getDeviceNameFromMobileEmulation(String userAgent, String pixelRatio, String width, String height) {
		String dataToBeReturned = null;
		boolean found = false;
		Map<String, Map<String, String>> getDeviceData = setUserAgentConfigurationValue();
		for (Object usKey : getDeviceData.keySet()) {
			if (getDeviceData.get(usKey).get("userAgent").equals(userAgent)
					&& getDeviceData.get(usKey).get("pixelRatio").equals(pixelRatio)
					&& getDeviceData.get(usKey).get("width").equals(width)
					&& getDeviceData.get(usKey).get("height").equals(height)) {
				dataToBeReturned = (String) usKey;
				found = true;
			}
		}
		if (!found) {
			dataToBeReturned = null;
		}
		return dataToBeReturned;
	}

}
