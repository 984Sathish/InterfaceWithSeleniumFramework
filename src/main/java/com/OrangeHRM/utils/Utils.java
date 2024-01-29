package com.OrangeHRM.utils;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

/**
 * Util class consists wait for page load, page load with user defined max time
 * and is used globally in all classes and methods
 * 
 */
public class Utils {
	private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();
	static MobileEmulationUserAgentConfiguration mobEUA = new MobileEmulationUserAgentConfiguration();
	public static int maxElementWait = Integer.parseInt(configProperty.getProperty("maxElementWait"));
	public static int minElementWait = Integer.parseInt(configProperty.getProperty("minElementWait"));
	public static int maxPageLoadWait = Integer.parseInt(configProperty.getProperty("maxPageLoadWait"));
	private static final SecureRandom random = new SecureRandom();
	/**
	 * To get the test orientation
	 * 
	 * <p>
	 * if test run on sauce lab device return landscape or portrait or valid
	 * message, otherwise check local device execution and return landscape or
	 * portrait or valid message
	 * 
	 * @return dataToBeReturned - portrait or landscape or valid message
	 */
	public static String getTestOrientation() {
		String dataToBeReturned = null;
		boolean checkExecutionOnSauce = false;
		boolean checkDeviceExecution = false;
		checkExecutionOnSauce = (System.getProperty("SELENIUM_DRIVER") != null
				|| System.getenv("SELENIUM_DRIVER") != null) ? true : false;

		if (checkExecutionOnSauce) {
			checkDeviceExecution = ((System.getProperty("runUserAgentDeviceTest") != null)
					&& (System.getProperty("runUserAgentDeviceTest").equalsIgnoreCase("true"))) ? true : false;
			if (checkDeviceExecution) {
				dataToBeReturned = (System.getProperty("deviceOrientation") != null)
						? System.getProperty("deviceOrientation")
								: "no sauce run system variable: deviceOrientation ";
			} else {
				dataToBeReturned = "sauce browser test: no orientation";
			}
		} else {
			checkDeviceExecution = (configProperty.hasProperty("runUserAgentDeviceTest")
					&& (configProperty.getProperty("runUserAgentDeviceTest").equalsIgnoreCase("true"))) ? true : false;
			if (checkDeviceExecution) {
				dataToBeReturned = configProperty.hasProperty("deviceOrientation")
						? configProperty.getProperty("deviceOrientation")
								: "no local run config variable: deviceOrientation ";
			} else {
				dataToBeReturned = "local browser test: no orientation";
			}
		}
		return dataToBeReturned;
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

			wait.until(WebDriverFactory.documentLoad);
			wait.until(WebDriverFactory.imagesLoad);
			wait.until(WebDriverFactory.framesLoad);
			String title = driver.getTitle().toLowerCase();
			String url = driver.getCurrentUrl().toLowerCase();
			Log.event("Page URL:: " + url);

			if ("the page cannot be found".equalsIgnoreCase(title) || title.contains("is not available")
					|| url.contains("/error/") || url.toLowerCase().contains("/errorpage/")) {
				Assert.fail("Site is down. [Title: " + title + ", URL:" + url + "]");
			}
		} catch (TimeoutException e) {
			driver.navigate().refresh();
			wait.until(WebDriverFactory.documentLoad);
			wait.until(WebDriverFactory.imagesLoad);
			wait.until(WebDriverFactory.framesLoad);
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

	/**
	 * To wait for the specific element on the page
	 * 
	 * @param driver  : WebDriver
	 * @param element : WebElement to wait for
	 * @return boolean - return true if element is present else return false
	 */
	public static boolean waitForElement(WebDriver driver, WebElement element) {
		return waitForElement(driver, element, maxElementWait);
	}

	/**
	 * To wait for the specific element on the page
	 * 
	 * @param driver  - WebDriver
	 * @param element - WebElement to wait for
	 * @param maxWait - Max wait duration
	 * @return statusOfElementToBeReturned - return true if element is present else
	 *         return false
	 */
	public static boolean waitForElement(WebDriver driver, WebElement element, int maxWait) {
		boolean statusOfElementToBeReturned = false;
		long startTime = StopWatch.startTime();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(maxWait));
		try {
			WebElement waitElement = wait.until(ExpectedConditions.visibilityOf(element));
			if (waitElement.isDisplayed() && waitElement.isEnabled()) {
				statusOfElementToBeReturned = true;
				Log.event("Element is displayed:: " + element.toString());
			}
		} catch (Exception e) {
			//        	try {
			//                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
			//                WebElement waitElement = wait.until(ExpectedConditions.visibilityOf(element));
			//                if (waitElement.isDisplayed() && waitElement.isEnabled()) {
			//                    statusOfElementToBeReturned = true;
			//                    Log.event("Element is displayed:: " + element.toString());
			//                }    	
			//			} catch (Exception e2) {
			statusOfElementToBeReturned = false;
			Log.event("Unable to find a element after " + StopWatch.elapsedTime(startTime) + " sec ==> "
					+ element.toString());
		}
		// }
		return statusOfElementToBeReturned;
	}

	/**
	 * To Switch the windows
	 * 
	 * @param driver             - WebDriver
	 * @param windowToSwitch     - Windows to Switch
	 * @param opt                - options
	 * @param closeCurrentDriver - Close Current active window
	 * @return assingedWebDriver - return true if assigned driver is present else
	 *         return false
	 */
	public static WebDriver switchWindows(WebDriver driver, String windowToSwitch, String opt,
			String closeCurrentDriver) throws Exception {
		WebDriver currentWebDriver = driver;
		WebDriver assingedWebDriver = driver;
		boolean windowFound = false;
		ArrayList<String> multipleWindows = new ArrayList<String>(assingedWebDriver.getWindowHandles());

		for (int i = 0; i < multipleWindows.size(); i++) {
			assingedWebDriver.switchTo().window(multipleWindows.get(i));
			if (opt.equals("title")) {
				if (assingedWebDriver.getTitle().trim().equals(windowToSwitch)) {
					windowFound = true;
					break;
				}
			} else if (opt.equals("url")) {
				if (assingedWebDriver.getCurrentUrl().contains(windowToSwitch)) {
					windowFound = true;
					break;
				}
			} // if
		} // for
		if (!windowFound)
			throw new Exception("Window: " + windowToSwitch + ", not found!!");
		else {
			if (closeCurrentDriver.equals("true"))
				currentWebDriver.close();
		}
		return assingedWebDriver;
	}// switchWindows

	/**
	 * Switching between tabs or windows in a browser
	 * 
	 * @param driver - WebDriver
	 */

	public static void switchToNewWindow(WebDriver driver) {
		String winHandle = driver.getWindowHandle();
		for (String index : driver.getWindowHandles()) {
			if (!index.equals(winHandle)) {
				driver.switchTo().window(index);
				break;
			}
		}
		if (!((RemoteWebDriver) driver).getCapabilities().getBrowserName().toLowerCase().contains(".*safari.*")) {
			((JavascriptExecutor) driver).executeScript(
					"if(window.screen){window.moveTo(0, 0); window.resizeTo(window.screen.availWidth, window.screen.availHeight);};");
		}
	}

	/**
	 * Method to switch to another Tab
	 * 
	 * @param windowHandle
	 * @param driver       - WebDriver
	 */
	public static void switchToWindow(String windowHandle, WebDriver driver) {
		for (String index : driver.getWindowHandles()) {
			if (!index.equals(windowHandle)) {
				driver.switchTo().window(index);
				break;
			}
		}
		if (!((RemoteWebDriver) driver).getCapabilities().getBrowserName().toLowerCase().contains(".*safari.*")) {
			((JavascriptExecutor) driver).executeScript(
					"if(window.screen){window.moveTo(0, 0); window.resizeTo(window.screen.availWidth, window.screen.availHeight);};");
		}
	}

	/**
	 * To compare two HashMap values,then print unique list value and print missed
	 * list value
	 * 
	 * @param expectedList - expected element list
	 * @param actualList   - actual element list
	 * @return boolean - returns true if both the lists are equal, else returns
	 *         false
	 */
	public static boolean compareTwoHashMap(Map<String, String> expectedList, Map<String, String> actualList) {
		List<String> missedkey = new ArrayList<String>();
		HashMap<String, String> missedvalue = new HashMap<String, String>();
		try {
			for (String k : expectedList.keySet()) {
				if (!(actualList.get(k).equalsIgnoreCase(expectedList.get(k))
						|| (actualList.get(k).contains(expectedList.get(k))))) {
					missedvalue.put(k, actualList.get(k));
					Log.failsoft("Missed Values:: Expected List:: " + missedvalue);
					return false;
				}
			}
			for (String y : actualList.keySet()) {
				if (!expectedList.containsKey(y)) {
					missedkey.add(y);
					Log.failsoft("Missed keys:: Actual List:: " + missedkey);
					return false;
				}
			}
		} catch (NullPointerException np) {
			return false;
		}
		return true;
	}

	/**
	 * To compare two Linked list HashMap values,then print unique list value and
	 * print missed list value
	 * 
	 * @param expectedList - expected element list
	 * @param actualList   - actual element list
	 * @return flag - returns true if both the lists are equal, else returns false
	 */

	public static boolean compareTwoLinkedListHashMap(LinkedList<LinkedHashMap<String, String>> expectedList,
			LinkedList<LinkedHashMap<String, String>> actualList, int[] noNeed) {
		int size = expectedList.size();
		if (noNeed.length > 0) {
			for (int i = 0; i < noNeed.length; i++) {
				  int index = noNeed[i];
		            if (index >= 0 && index < size) {
		                expectedList.remove(index);
		                actualList.remove(index);
		            }
		        }
		    }
		boolean flag = true;
		try {
			for (int i = 0; i < size; i++) {
				if (!Utils.compareTwoHashMap(expectedList.get(i), actualList.get(i)))
					flag = false;
			}
		} catch (NullPointerException np) {
			return false;
		}
		return flag;
	}

	/**
	 * To compare two array list values, then print unique list value and print
	 * missed list value
	 * 
	 * @param expectedElements - expected element list
	 * @param actualElements   - actual element list
	 * @return statusToBeReturned - returns true if both the lists are equal, else
	 *         returns false
	 */
	public static boolean compareTwoList(List<String> expectedElements, List<String> actualElements) {
		boolean statusToBeReturned = false;
		List<String> uniqueList = new ArrayList<String>();
		List<String> missedList = new ArrayList<String>();
		for (String item : expectedElements) {
			if (actualElements.contains(item)) {
				uniqueList.add(item);
			} else {
				missedList.add(item);
			}
		}
		Collections.sort(expectedElements);
		Collections.sort(actualElements);

		if (expectedElements.equals(actualElements)) {
			Log.event("All elements checked on this page:: " + uniqueList);
			statusToBeReturned = true;
		} else {
			Log.failsoft("Missing element on this page:: " + missedList);
			statusToBeReturned = false;
		}
		return statusToBeReturned;
	}



	/**
	 * Verify the css property for an element
	 * 
	 * @param element     - WebElement for which to verify the css property
	 * @param cssProperty - the css property name to verify
	 * @param actualValue - the actual css value of the element
	 * @return result
	 */
	public static boolean verifyCssPropertyForElement(WebElement element, String cssProperty, String actualValue) {
		boolean result = false;
		String actualClassProperty = element.getCssValue(cssProperty);
		if (actualClassProperty.trim().contains(actualValue)) {
			result = true;
		}
		return result;
	}

	/**
	 * To get the value of an input field.
	 * 
	 * @param element - WebElement
	 * @param driver  - WebDriver
	 * @return sDataToBeReturned - text of the input's value
	 */
	public static String getValueOfInputField(WebElement element, WebDriver driver) {
		String sDataToBeReturned = null;
		if (Utils.waitForElement(driver, element)) {
			sDataToBeReturned = element.getAttribute("value");
		}
		return sDataToBeReturned;
	}

	/**
	 * To wait for the specific element which is in disabled state on the page
	 * 
	 * @param driver  - current WebDriver object
	 * @param element - disabled WebElement
	 * @param maxWait - duration of wait in seconds
	 * @return statusOfElementToBeReturned - return true if disabled element is
	 *         present else return false
	 */
	public static boolean waitForDisabledElement(WebDriver driver, WebElement element, int maxWait) {
		boolean statusOfElementToBeReturned = false;
		long startTime = StopWatch.startTime();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(maxWait));
		try {
			WebElement waitElement = wait.until(ExpectedConditions.visibilityOf(element));
			if (!waitElement.isEnabled()) {
				statusOfElementToBeReturned = true;
				Log.event("Element is displayed and disabled:: " + element.toString());
			}
		} catch (Exception ex) {
			statusOfElementToBeReturned = false;
			Log.event("Unable to find disabled element after " + StopWatch.elapsedTime(startTime) + " sec ==> "
					+ element.toString());
		}
		return statusOfElementToBeReturned;
	}

	/**
	 * Wait until element disappears in the page
	 * 
	 * @param driver  - WebDriver instance
	 * @param element - WebElement to wait to have disaapear
	 * @return isNotDisplayed - true if element is not appearing in the page
	 */
	public static boolean waitUntilElementDisappear(WebDriver driver, final WebElement element) {
		final boolean isNotDisplayed;
		WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, Duration.ofSeconds(60));
		isNotDisplayed = wait.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver webDriver) {
				boolean isPresent = false;
				try {
					if (element.isDisplayed()) {
						isPresent = false;
						Log.event("Element " + element.toString() + ", is still visible in page");
					}
				} catch (Exception ex) {
					isPresent = true;
					Log.event("Element " + element.toString() + ", is not displayed in page ");
					return isPresent;
				}
				return isPresent;
			}
		});
		return isNotDisplayed;
	}

	/**
	 * Wait until element disappears in the page
	 * 
	 * @param driver  - WebDriver instance
	 * @param element - WebElement to wait to have disaapear
	 * @return isNotDisplayed - true if element is not appearing in the page
	 */
	public static boolean waitUntilElementDisappear(WebDriver driver, final WebElement element, int maxWait) {
		final boolean isNotDisplayed;

		WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, Duration.ofSeconds(maxWait));
		isNotDisplayed = wait.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver webDriver) {
				boolean isPresent = false;
				try {
					if (element.isDisplayed()) {
						isPresent = false;
						Log.event("Element " + element.toString() + ", is still visible in page");
					}
				} catch (Exception ex) {
					isPresent = true;
					Log.event("Element " + element.toString() + ", is not displayed in page ");
					return isPresent;
				}
				return isPresent;
			}
		});
		return isNotDisplayed;
	}

	/**
	 * To generate random number from '0 to Maximum Value' or 'Minimum Value ----
	 * Maximum Value'
	 * 
	 * @param max - maximum bound
	 * @param min - origin bound
	 * @return rand - random number between 'min to max' or '0 to max'
	 * @throws Exception
	 */
	public static int getRandom(int min, int max) throws Exception {
		int rand;
		if (min == 0)
			rand = random.nextInt(max);
		else
			rand = random.nextInt(min);
		return rand;
	}

	/**
	 * Round to certain number of decimals
	 * @param d
	 * @param decimalPlace - Numbers of decimals
	 * @return - True/False
	 */
	public static float round(double d, int decimalPlace) {
		return BigDecimal.valueOf(d).setScale(decimalPlace, BigDecimal.ROUND_HALF_UP).floatValue();
	}


	/**
	 * To copy the values with Hash Map
	 * 
	 * @param actua- Actual
	 * @param ignore - Ignore
	 * @return expected
	 * @throws Exception
	 */
	public static LinkedHashMap<String, String> copyHashMap(LinkedHashMap<String, String> actual, String ignore)
			throws Exception {
		List<String> indexes = new ArrayList<String>(actual.keySet());
		LinkedHashMap<String, String> expected = new LinkedHashMap<String, String>();

		for (int i = 0; i < indexes.size(); i++) {
			if (!indexes.get(i).equals(ignore))
				expected.put(indexes.get(i), actual.get(indexes.get(i)));
		}
		return expected;
	}

	/**
	 * To Copy the Values with LinkedListHashMap
	 * 
	 * @param actual - Actual
	 * @param ignore - Ignore
	 * @return expected
	 * @throws Exception
	 */
	public static LinkedList<LinkedHashMap<String, String>> copyLinkedListHashMap(
			LinkedList<LinkedHashMap<String, String>> actual, String ignore) throws Exception {
		int size = actual.size();
		LinkedList<LinkedHashMap<String, String>> expected = new LinkedList<LinkedHashMap<String, String>>();
		for (int j = 0; j < size; j++) {
			List<String> indexes = new ArrayList<String>(actual.get(j).keySet());
			LinkedHashMap<String, String> hashMap = new LinkedHashMap<String, String>();
			for (int i = 0; i < indexes.size(); i++) {
				if (!indexes.get(i).equals(ignore))
					hashMap.put(indexes.get(i), actual.get(j).get(indexes.get(i)));
			}
			expected.add(hashMap);
		}
		return expected;
	}



	/**
	 * To get browser name
	 * 
	 * @param driver : WebDriver
	 * @return browserName - Browser name
	 * @throws Exception
	 */
	public static String getBrowserName(final WebDriver driver) throws Exception {
		String browserName = ((RemoteWebDriver) driver).getCapabilities().getBrowserName();
		return browserName;
	}

	/**
	 * To get the Name of the Test that is running currently
	 * 
	 * @return String - Test Name
	 */
	public static String getCurrentTestName() {
		return Reporter.getCurrentTestResult().getName();
	}

	/**
	 * @author venkata.vadlapudi
	 * @param element
	 * @param maxWait
	 * @return true if the driver finds the element within specified max time else
	 *         false
	 */
	public static boolean waitForElementToBeDisplayed(WebDriver driver, WebElement element, int maxWait) throws InterruptedException {
	    long i = 0;
	    try {
	        for (i = 0; i < maxWait; i++) {
	            try {
	                if (element.isDisplayed() && element.isEnabled()) {
	                    Log.event("Element is displayed:: " + element.toString());
	                    return true;
	                }
	                Thread.sleep(1000);
	            } catch (StaleElementReferenceException | NoSuchElementException | IndexOutOfBoundsException ex) {
	                Thread.sleep(1000);
	            }
	        }
	    } catch (InterruptedException ex) {
	        // Re-interrupt the thread or handle the InterruptedException appropriately
	        Thread.currentThread().interrupt();
	        Log.event("Thread interrupted while waiting for element");
	    } catch (Exception ex) {
	        // Handle other exceptions if needed
	        Log.event("An error occurred: " + ex.getMessage());
	    }

	    Log.event("Unable to find an element after " + i + " sec ==> " + element.toString());
	    return false;
	}

	/**
	 * switchFrame : Switches frame to metadatacard dialog
	 * 
	 * @param None
	 * @return None
	 * @throws Exception
	 */
	public static void switchFrame(WebDriver driver, WebElement element) throws Exception {
		try {
			driver.switchTo().frame(element);
		} // End try
		catch (Exception e) {
			Log.exception(new Exception("Failed to switch the frame"), driver);
		} // End catch
	} // End switchFrame

	/**
	 * switchToDefaultContent : Switches frame to default content
	 * 
	 * @param None
	 * @return None
	 * @throws Exception
	 */
	public static void switchToDefaultContent(WebDriver driver) throws Exception {
		try {
			driver.switchTo().defaultContent();
		} catch (Exception e) {
			Log.exception(new Exception("Failed to switch to default content "), driver);
		} // End catch
	} // End switchToDefaultContent

	/**
	 * create new tab
	 * 
	 * @throws Exception
	 */
	public static void switchTab(WebDriver driver) throws Exception {
		try {
			((JavascriptExecutor) driver).executeScript("window.open()");
			ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
			driver.switchTo().window(tabs.get(1));
		} catch (Exception e) {
			Log.exception(new Exception("Failed to switch to tab"), driver);
		} // End catch
	}// End

	/**
	 * To get the device name
	 * 
	 * <p>
	 * if test run on sauce lab device then return device name or valid message,
	 * otherwise check local device execution then return device name or valid
	 * message
	 * 
	 * @param driver
	 *            - corresponding web driver instance
	 * @return dataToBeReturned - device name or valid message
	 */
	public static String getDeviceName(final WebDriver driver) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String dataToBeReturned = null;
		boolean checkExecutionOnSauce = false;
		boolean checkDeviceExecution = false;
		checkExecutionOnSauce = (System.getProperty("SELENIUM_DRIVER") != null || System.getenv("SELENIUM_DRIVER") != null) ? true : false;

		if (checkExecutionOnSauce) {
			checkDeviceExecution = ((System.getProperty("runUserAgentDeviceTest") != null) && (System.getProperty("runUserAgentDeviceTest").equalsIgnoreCase("true"))) ? true : false;
			if (checkDeviceExecution) {
				String userAgentString = (String) (js.executeScript("return navigator.userAgent"));
				long pixelRatio = (Long) js.executeScript("return window.devicePixelRatio");
				long width = (Long) js.executeScript("return screen.width");
				long height = (Long) js.executeScript("return screen.height");

				dataToBeReturned = mobEUA.getDeviceNameFromMobileEmulation(userAgentString, Long.toString(pixelRatio), Long.toString(width), Long.toString(height));
				// dataToBeReturned = (System.getProperty("deviceName") != null)
				// ? System.getProperty("deviceName") : null;
			} else {
				dataToBeReturned = "sauce browser test: no device";
			}
		} else {
			checkDeviceExecution = (configProperty.hasProperty("runUserAgentDeviceTest") && (configProperty.getProperty("runUserAgentDeviceTest").equalsIgnoreCase("true"))) ? true : false;
			if (checkDeviceExecution) {
				dataToBeReturned = configProperty.hasProperty("deviceName") ? configProperty.getProperty("deviceName") : null;
			} else {
				dataToBeReturned = "local browser test: no device";
			}
		}
		return dataToBeReturned;
	}

	public static boolean waitForElementSelected(WebDriver driver, WebElement element, int maxWait) {
		boolean statusOfElementToBeReturned = false;
		long startTime = StopWatch.startTime();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(maxWait));
		try {
			Boolean waitElement = wait.until(ExpectedConditions.elementToBeSelected(element));
			if (waitElement==true) {
				Log.event("Element is Selectable ");
			}
		} catch (Exception e) {
			statusOfElementToBeReturned = false;
			Log.event("Unable to select element after " + StopWatch.elapsedTime(startTime) + " sec ==> "
					+ element.toString());
		}
		return statusOfElementToBeReturned;
	}
} // Utils