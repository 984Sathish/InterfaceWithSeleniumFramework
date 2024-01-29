package com.OrangeHRM.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.testng.log4testng.Logger;

/**
 * ScreenshotManager to take screenshots using logger class
 *
 */
public class ScreenshotManager {
	private static final Logger logger = Logger.getLogger(ScreenshotManager.class);
	private static AtomicInteger screenShotIndex = new AtomicInteger(0);

	public static void takeScreenshot(WebDriver driver, String filepath) {
		File screenshot = null;
		// Checking AWS system property to check whether test is running from AWS or
		// local
		if (System.getProperty("appium.screenshots.dir") != null) {
			String screenshotDirectory = System.getProperty("appium.screenshots.dir",
					System.getProperty("java.io.tmpdir", ""));
			screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			boolean renameTo = screenshot.renameTo(new File(screenshotDirectory,
					String.format("%s.png", "Screenshot_" + screenShotIndex.incrementAndGet())));
			if(renameTo)
				logger.info("Screenshot renamed successfully");
		} else {
			if (driver instanceof TakesScreenshot) {
				screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			} else {
				WebDriver augmentedDriver = new Augmenter().augment(driver);
				screenshot = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
			}
			try {
				File destFile = new File(filepath);
				destFile.getParentFile().mkdirs();
				FileUtils.copyFile(screenshot, destFile);
				boolean deletePreviousScreenshot = screenshot.delete(); // it will delete the previous screenshots
				if(deletePreviousScreenshot) {
					logger.info("Previous screenshot deleted");
				}		
				logger.debug("screenshot taken and stored at " + destFile.getAbsolutePath());
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * takeScreenshot to take screenshots by passing driver as parameter with date
	 * and time
	 *
	 */
	public static void takeScreenshot(WebDriver driver) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-hhmmss-SSS");
		String path = "screenshots/Test-" + sdf.format(cal.getTime()) + ".jpg";
		takeScreenshot(driver, path);
	}
}