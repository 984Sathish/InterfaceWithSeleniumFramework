package com.OrangeHRM.utils;

import java.time.Duration;

import org.apache.commons.lang3.BooleanUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class WaitUtils {

	public static ExpectedCondition<Boolean> pageLoad;
	public static String queryString = "return (document.readyState != 'complete' || document.styleSheets.length  == 0)";

	static {
		pageLoad = currentDriver -> BooleanUtils.isNotTrue((Boolean) ((JavascriptExecutor) currentDriver).executeScript(queryString));
	}

	/**
	 * waitForPageLoad waits for the page load with custom page load wait time
	 *
	 */
	public static void waitForPageLoad(final WebDriver driver, int maxWait) {
		long startTime = StopWatch.startTime();
		FluentWait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(maxWait))
				.ignoring(StaleElementReferenceException.class, WebDriverException.class)
				.pollingEvery(Duration.ofMillis(500))
				.withMessage("Page Load Timed Out");
		try {
			wait.until(pageLoad);
			String title = driver.getTitle().toLowerCase();
			String url = driver.getCurrentUrl().toLowerCase();
			Log.event("Page URL:: " + url);
			if ("the page cannot be found".equalsIgnoreCase(title) || title.contains("is not available")
					|| url.contains("/error/") || url.toLowerCase().contains("/errorpage/")) {
				Assert.fail("Site is down. [Title: " + title + ", URL:" + url + "]");
			}
		} catch (TimeoutException e) {
			System.out.println("Timeout exception occured");
			Log.message("Timeout exception occured");
			driver.navigate().refresh();
			wait.until(pageLoad);
		}
		Log.event("Page Load Wait: (Sync)", StopWatch.elapsedTime(startTime));

	} // waitForPageLoad

	/**
	 * waitForPageLoad waits for the page load with default page load wait time
	 *
	 */
	public static void waitForPageLoad(final WebDriver driver) {
		waitForPageLoad(driver, WebDriverFactory.maxPageLoadWait);
	}
}
