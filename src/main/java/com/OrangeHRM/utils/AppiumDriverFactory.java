package com.OrangeHRM.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Reporter;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;

/**
 * AppiumDriverFactory class used to get a app driver instance, depends on the
 * user requirement as app we adding the desiredCapabilities initialized here
 */

public class AppiumDriverFactory {

	private static Logger logger = LoggerFactory.getLogger(AppiumDriverFactory.class);
	private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();
	private static String platform;
	private static List<String> udidList;
	private static Map<String, Boolean> deviceBuffer;
	private static Map<String, AppiumDriver> driverBuffer;
	private static final int SECONDS = Integer.parseInt(configProperty.getProperty("appiumMaxElementWait"));
	private static final int TOTAL_TIMES;
	private static CommandPrompt commandPrompt = new CommandPrompt();

	static {
		platform = configProperty.hasProperty("platform") ? configProperty.getProperty("platform")
				: "platform-not-specified";
		TOTAL_TIMES = configProperty.hasProperty("DevicePollingCount")
				? Integer.valueOf(configProperty.getProperty("DevicePollingCount"))
				: 3;
		driverBuffer = new HashMap<String, AppiumDriver>();
		deviceBuffer = new HashMap<String, Boolean>();

		udidList = new ArrayList<String>();
		if (configProperty.hasProperty("udidList")) {
			udidList.addAll(Arrays.asList(configProperty.getProperty("udidList").split(",")));
		}
		if (configProperty.hasProperty("udid") && !udidList.contains(configProperty.getProperty("udid"))) {
			udidList.add(configProperty.getProperty("udid"));
		}
		if (configProperty.hasProperty("udid2") && !udidList.contains(configProperty.getProperty("udid2"))) {
			udidList.add(configProperty.getProperty("udid2"));
		}

		udidList.forEach(udid -> {
			if (StringUtils.isNotBlank(udid)) {
				deviceBuffer.put(udid, true);
				driverBuffer.put(udid, null);
			}
		});
	}

	/**
	 * To get a new instance of app driver using default parameters
	 * 
	 * @return driver
	 */
	public static AppiumDriver get() {
		return get(null);
	}

	/**
	 * To get a new instance of app driver using a particular udid/devicename from
	 * config.properties file (say udid2/devicename2)
	 * 
	 * @return driver
	 */
	public static AppiumDriver getAnotherDevice() {
		if (configProperty.hasProperty("udid2")) {
			return get(configProperty.getProperty("udid2"));
		} else {
			throw new RuntimeException("udid2 is not mentioned in config.properties file");
		}
	}

	/**
	 * To get a new instance of app driver using a particular udid
	 * 
	 * @return driver
	 */
	public static AppiumDriver get(String udid) {
		AppiumDriver driver = null;
		try {
			driver = getAppiumDriver(udid);
		} catch (Exception e) {
			logger.error("Could not create a driver session. Trying again...");
			driver = getAppiumDriver(udid);
		} finally {
			// Update start time of the tests once free slot is assigned - Just
			// for reporting purpose
			try {
				Field f = Reporter.getCurrentTestResult().getClass().getDeclaredField("m_startMillis");
				f.setAccessible(true);
				f.setLong(Reporter.getCurrentTestResult(), Calendar.getInstance().getTime().getTime());
			} catch (Exception e) {
				try {
					Log.exception(e);
				} catch (Exception logException) {
					// TODO Auto-generated catch block
					System.err.println("Exception: " + e.getMessage());	
			}			}
		}
		return driver;
	}

	/**
	 * To set desired capabilities using configured parameters
	 * 
	 * @param udid - udid to get a particular device/ blank or null to get any
	 *             available device
	 * @return capabilities
	 */
	private static DesiredCapabilities getDesiredCapabilities(String udid) {
		DesiredCapabilities capabilities = new DesiredCapabilities();

		capabilities.setCapability("platformName", configProperty.getProperty("platform"));
		capabilities.setCapability("platformVersion", configProperty.getProperty("version"));

		if (StringUtils.isNotBlank(udid)) {
			if (isDeviceAvailable(udid)) {
				capabilities.setCapability("udid", udid);
				capabilities.setCapability("deviceName", configProperty.getProperty("deviceName"));
			} else {
				throw new RuntimeException(udid + " not available after waiting for " + TOTAL_TIMES + " minutes");
			}
		} else {
			udid = getAvailableDeviceUDID();
			if (StringUtils.isBlank(udid)) {
				throw new RuntimeException("No device available after waiting for " + TOTAL_TIMES + " minutes");
			}
			capabilities.setCapability("udid", udid);
			capabilities.setCapability("deviceName", configProperty.getProperty("deviceName"));
		}
		logger.info("Device udid : " + udid);

		if (configProperty.getProperty("platform").equalsIgnoreCase("win")) {
			capabilities.setCapability("platformVersion", udid);
		}

		if ("mobileweb".equalsIgnoreCase(configProperty.getProperty("appType"))) {
			capabilities.setCapability("browserName", configProperty.getProperty("browser"));
			capabilities.setCapability("autoAcceptAlerts", true);
			capabilities.setCapability("webkitResponseTimeout", 100000);
			if (configProperty.getProperty("useNewWDA") != null && !configProperty.getProperty("useNewWDA").isEmpty()) {
				capabilities.setCapability("useNewWDA", Boolean.valueOf(configProperty.getProperty("useNewWDA")));
			}
			capabilities.setCapability("clearSystemFiles", true);

			if (configProperty.getProperty("startIWDP") != null && !configProperty.getProperty("startIWDP").isEmpty()) {
				capabilities.setCapability("startIWDP", Boolean.valueOf(configProperty.getProperty("startIWDP")));
			}
			if (configProperty.getProperty("safariInitialUrl") != null
					&& !configProperty.getProperty("safariInitialUrl").isEmpty()) {
				capabilities.setCapability("safariInitialUrl", configProperty.getProperty("safariInitialUrl"));
			}
		} else if ("Win".equalsIgnoreCase(platform)) {
			capabilities.setCapability("app", configProperty.getProperty("WinAppPackage"));
		} else if (configProperty.hasProperty("appPathiOS")) {
			capabilities.setCapability("app", configProperty.getProperty("appPathiOS"));
		} else {
			String appPath = getAppAbsoultePath();
			capabilities.setCapability("app", appPath);
		}

		if (configProperty.getProperty("version").contains("11") || configProperty.getProperty("version").contains("10")
				|| configProperty.getProperty("version").contains("12")) {
			capabilities.setCapability("automationName", "XCUITest");
			if (configProperty.getProperty("xcodeOrgId") != null
					&& !configProperty.getProperty("xcodeOrgId").isEmpty()) {
				capabilities.setCapability("xcodeOrgId", configProperty.getProperty("xcodeOrgId"));
			}
			if (configProperty.getProperty("xcodeSigningId") != null
					&& !configProperty.getProperty("xcodeSigningId").isEmpty()) {
				capabilities.setCapability("xcodeSigningId", configProperty.getProperty("xcodeSigningId"));
			}
			if (configProperty.getProperty("RealDeviceLogger") != null
					&& !configProperty.getProperty("RealDeviceLogger").isEmpty()) {
				capabilities.setCapability("realDeviceLogger", configProperty.getProperty("RealDeviceLogger"));
			}
			if (configProperty.getProperty("XcodeConfigFile") != null
					&& !configProperty.getProperty("XcodeConfigFile").isEmpty()) {
				capabilities.setCapability("XcodeConfigFile", configProperty.getProperty("XcodeConfigFile"));
			}

		}
		if (configProperty.hasProperty("newCommandTimeout"))
			capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT,
					configProperty.getProperty("newCommandTimeout"));
		return capabilities;
	}

	/**
	 * Method to get absolute path of app (ipa or app / apk)
	 * 
	 * @return absolute path of app
	 */
	private static String getAppAbsoultePath() {
		File classpathRoot = new File(System.getProperty("user.dir"));
		logger.info("App path should be:" + classpathRoot + "/app");
		File appDir = new File(classpathRoot, "/app");
		String fileName = "";
		File folder = new File("app");
		File[] listOfFiles = folder.listFiles();
		for (File listFile : listOfFiles) {
			String fileExtension = FilenameUtils.getExtension(listFile.getAbsolutePath());
			if (null != platform && platform.equalsIgnoreCase("iOS")
					&& (fileExtension.equals("ipa") || fileExtension.equals("app"))) {
				fileName = listFile.getName();
				break;
			} else if (null != platform && platform.equalsIgnoreCase("Andriod") && fileExtension.equals("apk")) {
				fileName = listFile.getName();
				break;
			}
		}
		if (null != fileName && !fileName.isEmpty()) {
			File app = new File(appDir, fileName);
			String appName = app.getAbsolutePath();
			return appName;
		}
		return null;
	}

	/**
	 * To wait for given time until a random device becomes available
	 * 
	 * @return udid of the device when it is available
	 */
	private static String getAvailableDeviceUDID() {
		String udid = null;
		int maxTry = 0;
		while (udid == null && maxTry++ < TOTAL_TIMES) {
			clearStaleDriverSessions();
			synchronized (deviceBuffer) {
				for (String deviceudid : deviceBuffer.keySet()) {
					if (deviceBuffer.get(deviceudid)) {
						udid = deviceudid;
						deviceBuffer.put(deviceudid, false);
						break;
					}
				}
			}
			if (udid == null) {
				try {
					logger.info(Reporter.getCurrentTestResult().getName()
							+ " says -> No devices available now. Waiting for 1 minute...");
					TimeUnit.SECONDS.sleep(SECONDS);
				} catch (InterruptedException e) {
			        // Re-interrupt the thread
			        Thread.currentThread().interrupt();
			        logger.error("Unable to get udid: " + e.getMessage());
			    }
			}
		}
		return udid;
	}

	/**
	 * To wait for given time until the device with given udid become available
	 * 
	 * @param udid - udid of the device
	 * @return true when the device is available
	 */
	private static boolean isDeviceAvailable(String udid) {
		boolean isFree = false;
		int maxTry = 0;
		while (!isFree && maxTry++ < TOTAL_TIMES) {
			clearStaleDriverSessions();
			synchronized (deviceBuffer) {
				if (deviceBuffer.get(udid)) {
					deviceBuffer.put(udid, false);
					isFree = true;
				}
			}
			if (!isFree) {
				try {
					logger.info(Reporter.getCurrentTestResult().getName() + " says -> " + udid
							+ " is not available now. Waiting for 1 minute...");
					TimeUnit.SECONDS.sleep(SECONDS);
				} catch (InterruptedException e) {
			        // Re-interrupt the thread
			        Thread.currentThread().interrupt();
			        logger.error("Unable to get udid: " + e.getMessage());
			    }
			}
		}
		return isFree;
	}

	/**
	 * To quit the appium driver and update the device buffer and driver buffer
	 * 
	 * @param driver
	 */
	public static void quitDriver(AppiumDriver driver) {
		if (driver != null) {
			String udid = "";
			if (null != driver.getCapabilities().getCapability("udid")) {
				udid = driver.getCapabilities().getCapability("udid").toString();
				synchronized (deviceBuffer) {
					driver.quit();
					deviceBuffer.put(udid, true);
					driverBuffer.put(udid, null);
				}
			} else {
				udid = driver.getCapabilities().getCapability("platformVersion").toString();
				synchronized (deviceBuffer) {
					driver.quit();
					deviceBuffer.put(udid, true);
					driverBuffer.put(udid, null);
				}
			}
			driver.quit();
		} else {
			logger.error("Driver is null");
		}
	}

	/**
	 * To create an Appium Driver session with given capabilities.
	 * 
	 * @param udid - udid to get a particular device/ blank or null to get any
	 *             available device
	 * @return AppiumDriver instance
	 */
	private static AppiumDriver getAppiumDriver(String udid) {
		AppiumDriver driver = null;
		DesiredCapabilities capabilities = new DesiredCapabilities();
		if (System.getProperty("appium.screenshots.dir") != null) {
			try {
				logger.info("Initialize the Appium driver for AWS");
				driver = new IOSDriver(new URL(configProperty.getProperty("host")+":"+configProperty.getProperty("port")+"/wd/hub"), capabilities);
			} catch (MalformedURLException e) {
				try {
					Log.exception(e);
				} catch (Exception logException) {
					// TODO Auto-generated catch block
					System.err.println("Exception: " + e.getMessage());	
			}			}
		} else {
			logger.info("Initialize the Appium driver for local");
			try {
				String appiumURL = "http://" + configProperty.getProperty("host") + ":"
						+ configProperty.getProperty("port") + "/wd/hub";
				capabilities = getDesiredCapabilities(udid);
				udid = capabilities.getCapability("udid").toString();

				switch (platform.toLowerCase()) {
				case "android":
					driver = new AndroidDriver(new URL(appiumURL), capabilities);
					break;
				case "ios":
					driver = new IOSDriver(new URL(appiumURL), capabilities);
					break;
				case "win":
					appiumURL = "http://" + udid + ":" + configProperty.getProperty("port");
					driver = new IOSDriver(new URL(appiumURL), capabilities);
					break;
				default:
					logger.error("Unable to load platform property. Platform is set to " + platform);
					break;
				}

				synchronized (deviceBuffer) {
					deviceBuffer.put(udid, false);
					driverBuffer.put(udid, driver);
				}
			} catch (Exception e) {
				synchronized (deviceBuffer) {
					if (capabilities.getCapability("udid") != null
							&& deviceBuffer.containsKey(capabilities.getCapability("udid").toString())) {
						deviceBuffer.put(capabilities.getCapability("udid").toString(), true);
						driverBuffer.put(capabilities.getCapability("udid").toString(), null);
					}
				}
				logger.error(
						"Unable to create driver session with given URL and DesiredCapabilities : " + e.getMessage());
				throw new RuntimeException(
						"Unable to create driver session with given URL and DesiredCapabilities : " + e.getMessage());
			}
		}
		return driver;
	}

	/**
	 * To clear the stale driver sessions and to free the udid of the devices in the
	 * driverBuffer and the deviceBuffer respectively
	 */
	private static void clearStaleDriverSessions() {
		synchronized (deviceBuffer) {
			for (String udid : deviceBuffer.keySet()) {
				try {
					AppiumDriver driver = driverBuffer.get(udid);
					if (driver != null) {
						driver.getSessionId().toString();

					}
				} catch (Exception e) {
					deviceBuffer.put(udid, true);
					driverBuffer.put(udid, null);
				}
			}
		}
	}

	/**
	 * To get the device name and version (Works only when the iOS device is
	 * connected to the same MAC machine where the code gets executed)
	 * 
	 * @param udid - udid of the device
	 */
	public static String getIOSDeviceInfo(String udid) throws InterruptedException {
		String deviceNameAndVersion = "";
		try {
			String deviceName = commandPrompt.runCommand("/usr/local/bin/idevicename --udid " + udid)
					.replace("\\W", "_").trim();
			String deviceVersion = commandPrompt
					.runNestedCommand("/usr/local/bin/ideviceinfo --udid " + udid + " | grep ProductVersion")
					.split(":")[1].replace("\n", "").trim();
			deviceNameAndVersion = deviceName + "_" + deviceVersion;
		} catch (Exception e) {
			 // Either re-interrupt the thread or rethrow the InterruptedException
			Thread.currentThread().interrupt();
	        throw new InterruptedException("Interrupted while getting iOS device info: " + e.getMessage());
		}
		return deviceNameAndVersion;
	}
}
