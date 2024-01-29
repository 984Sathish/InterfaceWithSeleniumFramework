package com.OrangeHRM.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ShadowDOMUtils {


	/**
	 * Returns Web element from shadow-root elements
	 * 
	 * @param driver
	 * @param element
	 * @return
	 */
	public static WebElement expandRootElement(WebDriver driver, WebElement element) {
		WebElement shadowRootElement = (WebElement) ((JavascriptExecutor) driver)
				.executeScript("return arguments[0].shadowRoot", element);
		return shadowRootElement;
	}

	/**
	 * To get actual web element located by given CSS inside the shadow root
	 * attached to the shadow host located by given CSS
	 * 
	 * @param driver        - WebDriver
	 * @param shadowHostCSS - CSS selector of the host from which the shadow Root is
	 *                      attached
	 * @param shadowDOMQuery    - Fully qualified JS query of the shadowDOM
	 * @return WebElement
	 */
	public static WebElement getWebElement(WebDriver driver, String shadowHostCSS, String shadowDOMQuery) {
		JavascriptExecutor  js = (JavascriptExecutor)driver;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Utils.maxElementWait));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(shadowHostCSS)));
		return (WebElement)js.executeScript("return "+shadowDOMQuery);	
	}

	/**
	 * To get actual web element located by given CSS inside the shadow root
	 * attached to the given shadow host WebElement
	 * 
	 * @param driver     - WebDriver
	 * @param shadowHost - shadow host WebElement from which the shadow Root is
	 *                   attached
	 * @param elementCSS - CSS selector of the element to find inside the given
	 *                   shadow Root
	 * @return WebElement
	 */
	public static WebElement getWebElement(WebDriver driver, WebElement shadowHost, String elementCSS) {
		WebElement shadowRootElement = expandRootElement(driver, shadowHost);
		WebElement actualElement = shadowRootElement.findElement(By.cssSelector(elementCSS));
		return actualElement;
	}

	/**
	 * To get all web elements located by given CSS inside the shadow root attached
	 * to the shadow host located by given CSS
	 * 
	 * @param driver        - WebDriver
	 * @param shadowHostCSS - CSS selector of the host from which the shadow Root is
	 *                      attached
	 * @param elementCSS    - CSS selector of the element to find inside the given
	 *                      shadow Root
	 * @return List of WebElements
	 */
	public static List<WebElement> getAllWebElements(WebDriver driver, String shadowHostCSS, String elementCSS) {
		WebElement shadowHost = driver.findElement(By.cssSelector(shadowHostCSS));
		WebElement shadowRootElement = expandRootElement(driver, shadowHost);
		List<WebElement> actualElement = shadowRootElement.findElements(By.cssSelector(elementCSS));
		return actualElement;
	}

	/**
	 * To get all web elements located by given CSS inside the shadow root attached
	 * to the given shadow host WebElement
	 * 
	 * @param driver     - WebDriver
	 * @param shadowHost - shadow host WebElement from which the shadow Root is
	 *                   attached
	 * @param elementCSS - CSS selector of the element to find inside the given
	 *                   shadow Root
	 * @return List of WebElements
	 */
	public static List<WebElement> getAllWebElements(WebDriver driver, WebElement shadowHost, String elementCSS) {
		WebElement shadowRootElement = expandRootElement(driver, shadowHost);
		List<WebElement> actualElement = shadowRootElement.findElements(By.cssSelector(elementCSS));
		return actualElement;
	}

	/**
	 * To execute GetObject function in JavaScript file to get the shadow element as a object through Javascript execution in the driver
	 * 
	 * @param driver - WebDriver
	 * @param script - javascript that contains the cssSelector to find inside the shadow Root
	 * @return Object
	 */
	private static Object executerGetObject(WebDriver driver, String script) {
		String javascript = convertJStoText().toString();
		javascript += script;
		return ((JavascriptExecutor) driver).executeScript(javascript);
	}

	/**
	 * To get the JavaScript file as a String from querySelector.js file
	 * 
	 * @return StringBuilder - javascript functions as a StringBuilder
	 */
	private static StringBuilder convertJStoText() {
		File jsFile = new File("./src/main/resources/import_files/querySelector.js");
		BufferedReader reader = null;
		StringBuilder text = new StringBuilder();
		try {
			reader = new BufferedReader(new FileReader(jsFile));
		} catch (FileNotFoundException e) {
			try {
				Log.exception(e);
			} catch (Exception logException) {
				// TODO Auto-generated catch block
				System.err.println("Exception: " + e.getMessage());	
		}		}
		if (reader != null) {
			try {
				while (reader.ready()) {
					text.append(reader.readLine());
				}
			} catch (IOException e) {
				Log.failsoft(e.getMessage());
			}
		}
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				Log.failsoft(e.getMessage());
			}
		}
		return text;
	}

	/**
	 * To fix the found shadow element
	 * 
	 * @param context - driver
	 * @param cssLocator - cssSelector
	 * @param element - WebElement
	 */
	private static void fixLocator(SearchContext context, String cssLocator, WebElement element) {
		if (element instanceof RemoteWebElement) {
			try {
				@SuppressWarnings("rawtypes")
				Class[] parameterTypes = new Class[] { SearchContext.class, String.class, String.class };
				Method m = element.getClass().getDeclaredMethod("setFoundBy", parameterTypes);
				m.setAccessible(true);
				Object[] parameters = new Object[] { context, "cssSelector", cssLocator };
				m.invoke(element, parameters);
			} catch (Exception fail) {
				Log.failsoft(fail.getMessage());
			}
		}
	}

	/**
	 * To get actual web element located by given CSS inside the shadow root
	 * attached to the given shadow host WebElement
	 * 
	 * @param driver
	 *            - WebDriver	
	 * @param cssSelector
	 *            - CSS selector of the element to find inside the given shadow Root
	 * @return WebElement
	 */
	public static WebElement findElement(String cssSelector, WebDriver driver) {
		WebElement element = null;
		element = (WebElement) executerGetObject(driver, "return getObject(\"" + cssSelector + "\");");
		fixLocator(driver, cssSelector, element);
		return element;
	}

	/**
	 * To get all web elements located by given CSS inside the shadow root attached
	 * to the shadow host located by given CSS
	 * 
	 * @param driver        - WebDriver
	 * @param cssSelector    - CSS selector of the element to find inside the given
	 *                      shadow Root
	 * @return List of WebElements
	 */
	@SuppressWarnings("unchecked")
	public static List<WebElement> findElements(String cssSelector, WebDriver driver) {		
		List<WebElement> element = null;
		Object object = executerGetObject(driver, "return getAllObject(\"" + cssSelector + "\");");
		if (object != null && object instanceof List<?>) {
			element = (List<WebElement>) object;
		}
		for (WebElement webElement : element) {
			fixLocator(driver, cssSelector, webElement);
		}
		return element;
	}

	/**
	 * To get the WebElement located by given CSS inside the shadow root traversing from Parent Element
	 * 
	 * @param cssSelector - CSS selector of the element to find inside the given shadow Root
	 * @param parentWebElement - WebElement
	 * @param driver
	 * 
	 * @return WebElement
	 */
	public static WebElement findElementFromParent(String cssSelector, WebElement parentWebElement, WebDriver driver) {
		String locator = "return arguments[0].shadowRoot.querySelector('" + cssSelector + "')";                 
		return (WebElement) ((JavascriptExecutor) driver).executeScript(locator, parentWebElement);
	}
}