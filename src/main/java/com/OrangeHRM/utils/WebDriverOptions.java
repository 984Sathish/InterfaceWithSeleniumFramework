package com.OrangeHRM.utils;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariOptions;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to setup general browser options
 */
public class WebDriverOptions {
	public static ChromeOptions setChromeOptions() {

		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("--ignore-certificate-errors");
		// opt.addArguments("--disable-bundled-ppapi-flash");
		chromeOptions.addArguments("--disable-extensions");
		// Commented out the below line due to the RRS pagination issue
		// chromeOptions.addArguments("--disable-web-security");
		chromeOptions.addArguments("--always-authorize-plugins");
		chromeOptions.addArguments("--allow-running-insecure-content");
		chromeOptions.addArguments("--test-type");
		chromeOptions.addArguments("--enable-npapi");
		Map<String, Object> prefs = new HashMap<>();
		prefs.put("profile.default_content_setting_values.plugins", 1);
		prefs.put("profile.content_settings.plugin_whitelist.adobe-flash-player", 1);
		prefs.put("profile.content_settings.exceptions.plugins.*,*.per_resource.adobe-flash-player", 1);

//		chromeOptions.setExperimentalOption("prefs", prefs);
		chromeOptions.setExperimentalOption("w3c", true);
		return chromeOptions;
	}

	public static EdgeOptions setEdgeOptions() {

		EdgeOptions edgeOptions = new EdgeOptions();
		return edgeOptions;
	}

	public static FirefoxOptions setFirefoxOptions() {

		FirefoxOptions firefoxOptions = new FirefoxOptions();
		return firefoxOptions;
	}

	public static SafariOptions setSafariOptions() {
		SafariOptions safariOptions = new SafariOptions();
		return safariOptions;
	}
}
