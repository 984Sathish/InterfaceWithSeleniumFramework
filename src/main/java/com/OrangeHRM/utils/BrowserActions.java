package com.OrangeHRM.utils;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BrowserActions{

	private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();

	/**
	 * Used to select drop down option using Select class and SelectByVisibleText method and verify the selected Text
	 * @param driver
	 * @param webElement
	 * @param optnSelect
	 * @param elementDescription
	 * @param timeOutArray
	 * @throws Exception
	 */
	public static void selectDropDownByVisibleText(WebDriver driver, WebElement webElement, String optnSelect, String elementDescription)  {
		selectDropDownByVisibleText(driver, webElement, optnSelect, false, 0, elementDescription);
	}

	/**
	 * Used to select drop down option using Select class and SelectByVisibleText method and verify the selected Text
	 * @param driver
	 * @param webElement
	 * @param optnSelect
	 * @param elementDescription
	 * @param timeOutArray
	 * @throws Exception
	 */
	public static void selectDropDownByVisibleText(WebDriver driver, WebElement webElement, String optnSelect, double moveToElementInSecs, String elementDescription)  {
		selectDropDownByVisibleText(driver, webElement, optnSelect, false, moveToElementInSecs, elementDescription);
	}

	/**
	 * Used to select drop down option using Select class and SelectByVisibleText method and verify the selected Text
	 * @param driver
	 * @param webElement
	 * @param optnSelect
	 * @param elementDescription
	 * @param timeOutArray
	 * @throws Exception
	 */
	public static void selectDropDownByVisibleText(WebDriver driver, WebElement webElement, String optnSelect, boolean waitForNetworkIdleState, double moveToElementInSecs, String elementDescription)  {
		int timeoutValue = Integer.parseInt(configProperty.getProperty("maxElementWait"));
		int retryCount =0;
		boolean staleElementException = false;
		do {
			try {
				if (!waitForElementToDisplay(driver, webElement, timeoutValue))
					throw new NoSuchElementException(elementDescription + " not found in page!!");
				waitForElementToBeClickable(driver, webElement, elementDescription);
				moveToElement(driver, webElement, moveToElementInSecs, elementDescription);
				Select select=new Select(webElement);
				select.selectByVisibleText(optnSelect);
				if(waitForNetworkIdleState)
					waitForNetworkIdleState(driver);
				Log.event("DropDown value "+optnSelect+" selected");
			}catch(NoSuchElementException e) {
				Log.fail("Element not found "+elementDescription+" "+e, driver);
			}
			catch(StaleElementReferenceException e1) {
				Log.event("click - Stale element exception "+elementDescription);
				staleElementException = true;
			}
			retryCount ++;
		} while(retryCount == 1 && staleElementException);
	}

	/**
	 * Used to select drop down option using Select class and SelectByVisibleText method and verify the selected Text
	 * @param driver
	 * @param webElement
	 * @param optnSelect
	 * @param waitForNetworkIdleState
	 * @param elementDescription
	 * @param timeout
	 * @throws Exception
	 */
	public static void selectDropDownByVisibleText(WebDriver driver, WebElement webElement, String optnSelect, boolean waitForNetworkIdleState, String elementDescription)  {
		selectDropDownByVisibleText(driver, webElement, optnSelect, waitForNetworkIdleState, 0, elementDescription);
	}

	/**
	 * To wait for the specific element to display on the page
	 *
	 * @param driver  - WebDriver
	 * @param webElementBy - By WebElement to wait for
	 * @param timeout - Max wait duration
	 * @param description - Element description
	 * @return statusOfElementToBeReturned - return true if element is present else
	 *         return false
	 */
	public static void waitForElementToDisplay(WebDriver driver, By webElementBy, String description, int... timeout) {
		int timeoutValue = timeout.length > 0 ? timeout[0]: Integer.parseInt(configProperty.getProperty("maxElementWait"));
		boolean elementDisplayed = false;
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutValue));
			wait.until(ExpectedConditions.visibilityOfElementLocated(webElementBy));
			elementDisplayed = true;
			Log.event(description +" Element displayed");
		}catch(TimeoutException e) {
			Log.fail(description +" Element not displayed");
		}
	}


	/**
	 * To wait for the specific element to display on the page
	 *
	 * @param driver  - WebDriver
	 * @param element - WebElement to wait for
	 * @param maxWait - Max wait duration
	 * @return statusOfElementToBeReturned - return true if element is present else
	 *         return false
	 */
	public static boolean waitForElementToDisplay(WebDriver driver, WebElement webElement, int... timeout) {
		int timeoutValue = timeout.length > 0 ? timeout[0]: Integer.parseInt(configProperty.getProperty("maxElementWait"));
		Boolean elementStatus = false;
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)							
					.withTimeout(Duration.ofSeconds(timeoutValue)) 			
					.pollingEvery(Duration.ofMillis(500)) 			
					.ignoring(NoSuchElementException.class, StaleElementReferenceException.class);

			elementStatus = wait.until(new Function<WebDriver, Boolean>() {
				@Override
				public Boolean apply(WebDriver driver) {
					Boolean isElementDisplayed = false;
					Dimension dimension = webElement.getSize();
					isElementDisplayed = dimension.getWidth() > 0 && dimension.getHeight() > 0 ? true : false;
					return isElementDisplayed;
				}
			});
			Log.event(webElement +" displayed");
		}catch(TimeoutException e){
			Log.event(webElement+" not displayed - Timed out exception");
		}
		return elementStatus;
	}

	/**
	 * To wait for the specific element to display on the page
	 *
	 * @param driver  - WebDriver
	 * @param element - WebElement to wait for
	 * @param maxWait - Max wait duration
	 * @return 
	 * @return statusOfElementToBeReturned - return true if element is present else
	 *         return false
	 */
	public static void type(WebDriver driver, WebElement webElement, String textToType, boolean waitForElementState, String elementDescription) {
		type(driver, webElement, textToType, waitForElementState,0, elementDescription);
	}

	/**
	 * To wait for the specific element to display on the page
	 *
	 * @param driver  - WebDriver
	 * @param element - WebElement to wait for
	 * @param maxWait - Max wait duration
	 * @return 
	 * @return statusOfElementToBeReturned - return true if element is present else
	 *         return false
	 */
	public static void type(WebDriver driver, WebElement webElement, String textToType, String elementDescription) {
		type(driver, webElement, textToType, false, 0, elementDescription);
	}


	/**
	 * To wait for the specific element to display on the page
	 *
	 * @param driver  - WebDriver
	 * @param element - WebElement to wait for
	 * @param maxWait - Max wait duration
	 * @return 
	 * @return statusOfElementToBeReturned - return true if element is present else
	 *         return false
	 */
	public static void type(WebDriver driver, WebElement webElement, String textToType, double moveToElementWaitInSecs, String elementDescription) {
		type(driver, webElement, textToType, false, moveToElementWaitInSecs, elementDescription);
	}

	/**
	 * To wait for the specific element to display on the page
	 * @param driver
	 * @param webElement - Input Element
	 * @param textToType
	 * @param elementDescription - Description of the element to print in report
	 * @param waitForNetworkIdleState To wait for page DOM load to complete after type
	 * @param timeout
	 */
	public static void type(WebDriver driver, WebElement webElement, String textToType, boolean waitForNetworkIdleState, double moveToElementWaitInSecs, String elementDescription) {
		int timeoutValue = Integer.parseInt(configProperty.getProperty("maxElementWait"));
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)							
					.withTimeout(Duration.ofSeconds(timeoutValue)) 			
					.pollingEvery(Duration.ofMillis(500)) 			
					.ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
					.ignoring(ElementNotInteractableException.class);

			wait.until(new Function<WebDriver, Boolean>() {
				@Override
				public Boolean apply(WebDriver driver) {
					Boolean isElementDisplayed = false;
					Dimension dimension = webElement.getSize();
					if(dimension.getWidth() > 0 && dimension.getHeight() > 0) {
						moveToElement(driver, webElement, moveToElementWaitInSecs, elementDescription);
						webElement.clear();
						webElement.sendKeys(textToType);
						Log.event("Entered value "+textToType+" in "+elementDescription);
						if(waitForNetworkIdleState) {
							Actions actions = new Actions(driver);
							actions.moveToElement(webElement).sendKeys(Keys.TAB).pause(500).build().perform();
							waitForNetworkIdleState(driver);
						}
						isElementDisplayed = true;
					}
					return isElementDisplayed;
				}
			});
		}
		catch(TimeoutException e){
			Log.fail("Failed to type on "+elementDescription+" "+e, driver);	
		}
	}

	/**
	 * Used to click on the webelement
	 * @param driver
	 * @param webElement - Element to be clicked
	 * @param elementDescription - Description of the element to print in report
	 * @param waitForNetworkIdleState - To wait for page DOM load to complete after click
	 * @param timeout
	 */
	public static void click(WebDriver driver, WebElement webElement, boolean waitForNetworkIdleState, double moveToElementWaitInSecs, String elementDescription)   {
		int timeoutValue = Integer.parseInt(configProperty.getProperty("maxElementWait"));
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)							
					.withTimeout(Duration.ofSeconds(timeoutValue)) 			
					.pollingEvery(Duration.ofMillis(500)) 			
					.ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
					.ignoring(ElementClickInterceptedException.class, WebDriverException.class);

			wait.until(new Function<WebDriver, Boolean>() {
				@Override
				public Boolean apply(WebDriver driver) {
					Boolean isElementDisplayed = false;
					Dimension dimension = webElement.getSize();
					Log.event("Webelement dimension: "+dimension);
					if(dimension.getWidth() > 0 && dimension.getHeight() > 0) {
						//moveToElement(driver, webElement, moveToElementWaitInSecs, elementDescription);
						webElement.click();
						Log.event("Clicked on "+elementDescription);
						if(waitForNetworkIdleState)
							waitForNetworkIdleState(driver);
						isElementDisplayed = true;
					}
					return isElementDisplayed;
				}
			});
		}catch(TimeoutException e){
			Log.fail("Failed to click on "+elementDescription+" "+e, driver);	
		}
	}

	/**
	 * Used to click on the webelement
	 * @param driver
	 * @param webElement - Element to be clicked
	 * @param elementDescription - Description of the element to print in report
	 * @param waitForNetworkIdleState - To wait for page DOM load to complete after click
	 * @param timeout
	 */
	public static void click(WebDriver driver, WebElement webElement, double moveToElementWaitTime, String elementDescription)   {
		click(driver, webElement, false, moveToElementWaitTime, elementDescription);
	}

	/**
	 * Used to click on the webelement
	 * @param driver
	 * @param webElement - Element to be clicked
	 * @param elementDescription - Description of the element to print in report
	 * @param waitForNetworkIdleState - To wait for page DOM load to complete after click
	 * @param timeout
	 */
	public static void click(WebDriver driver, WebElement webElement, boolean waitForNetworkIdleState, String elementDescription)   {
		click(driver, webElement, waitForNetworkIdleState, 0, elementDescription);
	}

	/**
	 * Used to click on the webelement
	 * @param driver
	 * @param btn - Element to be clicked
	 * @param elementDescription - Description of the element
	 * @param timeOutArray - custom timeout in secs
	 */
	public static void click(WebDriver driver, WebElement webElement, String elementDescription)   {
		click(driver, webElement, false, 0, elementDescription);
	}

	/**
	 * To wait for the specific element to display on the page
	 *
	 * @param driver  - WebDriver
	 * @param element - WebElement to wait for
	 * @param maxWait - Max wait duration
	 * @return statusOfElementToBeReturned - return true if element is present else
	 *         return false
	 */
	public static void verifyElementDisplayed(WebDriver driver, WebElement element, String elementDescription, int... timeout) {
		int timeoutValue = timeout.length > 0 ? timeout[0]: Integer.parseInt(configProperty.getProperty("maxElementWait"));
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)							
					.withTimeout(Duration.ofSeconds(timeoutValue)) 			
					.pollingEvery(Duration.ofMillis(500)) 			
					.ignoring(NoSuchElementException.class, StaleElementReferenceException.class);

			wait.until(new Function<WebDriver, Boolean>() {
				@Override
				public Boolean apply(WebDriver driver) {
					List<WebElement> webElements = locateElementsBy(driver, element);
					Boolean isElementDisplayed = false;
					if(webElements!= null) {
						Dimension dimension = webElements.get(0).getSize();
						isElementDisplayed = dimension.getWidth() > 0 && dimension.getHeight() > 0 ? true : false;
					}
					return isElementDisplayed;
				}
			});
			Log.event(elementDescription +" displayed");
		}catch(TimeoutException e){
			Log.fail(elementDescription+" not displayed", driver);

		}
	}


	/**
	 * Wait till the expected element becomes clickable
	 * @param driver
	 * @param webElement - Expected element needs to clickable
	 * @param elementDescription - Description
	 * @param timeOutArray - custom timeout in secs
	 */
	public static void waitForElementToBeClickable(WebDriver driver, WebElement webElement, String elementDescription, int... timeout)   {
		int timeoutValue = timeout.length > 0?timeout[0]:Integer.parseInt(configProperty.getProperty("maxElementWait"));
		try {
			WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(timeoutValue));
			wait.until(ExpectedConditions.elementToBeClickable(webElement));
			Log.event(elementDescription+" is clickable");
		}catch(TimeoutException e) {
			Log.fail(elementDescription+" is not clickable", driver);	
		}
	}

	/**
	 * To wait for the specific element to be invisible
	 *
	 * @param driver  - WebDriver
	 * @param element - WebElement to wait for
	 * @param maxWait - Max wait duration
	 * @return elementStatus - return true if element is not present else
	 *         return false
	 */
	public static void waitForElementToBeInvisible(WebDriver driver, By webElementBy, int... timeout) {
		int timeoutValue = timeout.length > 0 ? timeout[0]: Integer.parseInt(configProperty.getProperty("maxElementWait"));
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutValue));
			wait.until(ExpectedConditions.invisibilityOfElementLocated(webElementBy));
		}catch(TimeoutException e) {
			Log.fail("Page still loading - Interactions disabled", driver);
		}
	}

	/**
	 * This method by default wait for a max of #5secs. Same can be updated by passing timeout value
	 * @param driver
	 * @param timeOut
	 *
	 */
	public static void waitForNetworkIdleState(WebDriver driver, int... timeout) {
		int timeOutValue = timeout.length > 0?timeout[0]:Integer.parseInt(configProperty.getProperty("maxPageRefreshWait"));
		try{
			By pageLoader = By.cssSelector("div[class*='gw-disable-click']");
			waitForElementToBeInvisible(driver, pageLoader, timeOutValue);
		}catch(NoSuchElementException | StaleElementReferenceException e) {}
		Log.event("Network idle state completed");
	}

	/**
	 * This method will return selector name & value when web element is passed as a parameter
	 * @param webElement
	 * @return
	 */
	public static String[] splitLocatorNameValue(WebElement webElement) {
		String webElementInString = webElement.toString();
		Log.event("WebElement: "+webElementInString);
		String locatorNameValue;

		if(webElementInString.contains("Proxy element")) 
			locatorNameValue = webElementInString.split("By.")[1].trim();
		else {
			webElementInString = webElementInString.substring(0, webElementInString.length()-1); //remove last character "]"
			locatorNameValue = webElementInString.split("->")[1].trim();
		}

		String locatorName = locatorNameValue.substring(0, locatorNameValue.indexOf(":")).trim();
		String locatorValue = locatorNameValue.substring(locatorNameValue.indexOf(":")+1).trim();
		return new String[] {locatorName, locatorValue};
	}

	/**
	 * This method will list<WebElement> when web element is passed as a parameter
	 * @param driver
	 * @param webElement
	 * @return
	 */
	public static List<WebElement> locateElementsBy(WebDriver driver, WebElement webElement) {
		String[] locatorNameValue = splitLocatorNameValue(webElement);

		String locatorName = locatorNameValue[0];
		String locatorValue = locatorNameValue[1];

		switch(locatorName) {

		case "css selector":
			return driver.findElements(By.cssSelector(locatorValue));

		case "id":
			return driver.findElements(By.id(locatorValue));

		case "xpath":
			return driver.findElements(By.xpath(locatorValue));

		}
		return null;
	}

	/**
	 * This method will list<WebElement> when web element is passed as a parameter
	 * @param driver
	 * @param webElement
	 * @return
	 */
	public static WebElement locateElementBy(WebDriver driver, String locatorName, String locatorValue) {

		switch(locatorName) {

		case "css selector":
			return driver.findElement(By.cssSelector(locatorValue));

		case "id":
			return driver.findElement(By.id(locatorValue));

		case "xpath":
			return driver.findElement(By.xpath(locatorValue));

		}
		return null;
	}

	/**
	 * @author siva.panjanathan
	 * Used to confirm the alert
	 * @param driver
	 * @param elementDescription - Description of the element
	 * @throws InterruptedException
	 */
	public static void confirmAlert(WebDriver driver, String elementDescription, int... timeOutArray)  {
		int timeout = timeOutArray.length > 0?timeOutArray[0]:Integer.parseInt(configProperty.getProperty("maxElementWait"));
		try {
			WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(timeout));
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			alert.accept();
			Log.event(elementDescription+"is performed");
		} catch (NoAlertPresentException e) {
			Log.fail(elementDescription+"is not performed"+e, driver);        
		}catch (UnhandledAlertException e) {
			Log.fail(elementDescription+"is not performed"+e, driver);        
		}catch(TimeoutException e) {
			Log.message("Alert is not displayed");
		}
	} 

	/**
	 * To wait for the specific element text value is not null - using getText()
	 * @param driver  - WebDriver
	 * @param webElement - WebElement to wait for
	 * @param elementDescription - Element description
	 * @param maxWait - Max wait duration
	 * @return elementTextValue - get text value
	 */
	public static String getText(WebDriver driver, WebElement webElement, String elementDescription, int... timeout) {
		int timeoutValue = timeout.length > 0 ? timeout[0]: Integer.parseInt(configProperty.getProperty("maxElementWait"));
		String elementTextValue = null;
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)							
					.withTimeout(Duration.ofSeconds(timeoutValue)) 			
					.pollingEvery(Duration.ofMillis(500)) 			
					.ignoring(NoSuchElementException.class, StaleElementReferenceException.class);

			elementTextValue = wait.until(new Function<WebDriver, String>() {
				@Override
				public String apply(WebDriver driver) {
					Dimension dimension = webElement.getSize();
					if(dimension.getWidth() > 0 && dimension.getHeight() > 0 && webElement.getText().length() !=0)
						return webElement.getText();
					else
						return null;
				}
			});
			Log.event("Text value displayed for "+elementDescription+" : "+elementTextValue);
		}catch(TimeoutException e){
			Log.fail("Text value not displayed for "+elementDescription+" : "+elementTextValue);
		}
		return elementTextValue;
	}



	/**
	 * Used to verify Element Text Is Displayed
	 * @param driver
	 * @param webElement
	 * @param expectedText
	 * @param waitForNetworkIdleState
	 * @param elementDescription
	 */
	public static void verifyElementTextIsDisplayed(WebDriver driver, WebElement webElement,String expectedText, boolean waitForNetworkIdleState,String elementDescription) {
		int timeoutValue = Integer.parseInt(configProperty.getProperty("maxElementWait"));
		int retryCount =0;
		boolean ElementNotInteractableException = false;
		do {
			try {
				Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)							
						.withTimeout(Duration.ofSeconds(timeoutValue)) 			
						.pollingEvery(Duration.ofMillis(500)) 			
						.ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
						.ignoring(ElementNotInteractableException.class);

				wait.until(new Function<WebDriver, Boolean>() {
					@Override
					public Boolean apply(WebDriver driver) {
						List<WebElement> webElements = locateElementsBy(driver, webElement);
						Boolean isElementDisplayed = false;
						if(webElements!= null) {
							Dimension dimension = webElements.get(0).getSize();
							if(dimension.getWidth() > 0 && dimension.getHeight() > 0) {
								Log.assertThat(webElement.getText().equals(expectedText), elementDescription+" is displayed", elementDescription+" is not displayed", driver);
								if(waitForNetworkIdleState)
									waitForNetworkIdleState(driver);
								isElementDisplayed = true;
							}
						}
						return isElementDisplayed;
					}
				});
			}
			catch(ElementNotInteractableException e1) {
				Log.event("click - Element NotInteractableException "+elementDescription);
				ElementNotInteractableException = true;
			}
			retryCount ++;
		} while(retryCount == 1 && ElementNotInteractableException);
	}

	/**
	 * @author anitha.raphel
	 * Used to verify Element Text message which Contains expected Message after replacing all the Numbers and special characters
	 * @param driver
	 * @param webElement
	 * @param expectedString
	 * @param elementDescription
	 * @param timeOutArray
	 */
	public static void verifyElementTextMessageContainsAlphabeticsUsingReplaceAll(WebDriver driver, WebElement webElement,String expectedString, String elementDescription, int... timeOutArray) {
		int timeout = timeOutArray.length > 0?timeOutArray[0]:Integer.parseInt(configProperty.getProperty("maxElementWait"));
		try {
			if (!BrowserActions.waitForElementToDisplay(driver, webElement, timeout))
				throw new NoSuchElementException(elementDescription + " not found in page!!");
			String text=webElement.getText();
			String textOnly=text.replaceAll("[^a-zA-Z]","");
			Log.assertThat(textOnly.equalsIgnoreCase(expectedString), elementDescription+" Message is Displayed !!", elementDescription+" Message is not Displayed", driver);

		}catch(NoSuchElementException e) {
			Log.fail(elementDescription+" Message is not Displayed" +e, driver);	
		}
	}

	/**
	 * @author anitha.raphel
	 * Used to move on the webelement
	 * @param driver
	 * @param btn - Element to be clicked
	 * @param elementDescription - Description of the element
	 * @param timeOutArray - custom timeout in secs
	 */
	public static void moveToElement(WebDriver driver, WebElement element, double waitTimeInSecs, String elementDescription) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		Actions actions = new Actions(driver);
		actions.moveToElement(element).pause((long)waitTimeInSecs*1000).build().perform();
	}

	/**
	 * @author anitha.raphel
	 * Used to move on the webelement
	 * @param driver
	 * @param btn - Element to be clicked
	 * @param elementDescription - Description of the element
	 * @param timeOutArray - custom timeout in secs
	 */
	public static void moveToElementandClick(WebDriver driver, WebElement element, int waitTimeInSecs, String elementDescription) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		Actions actions = new Actions(driver);
		actions.moveToElement(element).pause((long) waitTimeInSecs * 1000).click().build().perform();
	}

	/**
	 * @author anitha.raphel
	 * Used to move on the webelement
	 * @param driver
	 * @param btn - Element to be clicked
	 * @param elementDescription - Description of the element
	 * @param timeOutArray - custom timeout in secs
	 */
	public static void moveToElement(WebDriver driver, WebElement element, String elementDescription) {
		moveToElement(driver, element, 0, elementDescription);
	}
	
	/**
	 * @author anitha.raphel
	 * Used to move on the webelement
	 * @param driver
	 * @param btn - Element to be clicked
	 * @param elementDescription - Description of the element
	 * @param timeOutArray - custom timeout in secs
	 */
	public static void moveToElementandClick(WebDriver driver, WebElement element, String elementDescription) {
		moveToElementandClick(driver, element, 0, elementDescription);
	}

	/**
	 * Used to select drop down option using Select class and SelectByVisibleText method and verify the selected Text
	 * @param driver
	 * @param webElement
	 * @param optnSelect
	 * @param elementDescription
	 * @param timeOutArray
	 * @throws Exception
	 */
	public static void selectDropDownByIndex(WebDriver driver, WebElement webElement, int index, boolean waitForNetworkIdleState, String elementDescription)  {
		selectDropDownByIndex(driver, webElement, index, waitForNetworkIdleState, 0, elementDescription);
	}

	/**
	 * Used to select drop down option using Select class and SelectByVisibleText method and verify the selected Text
	 * @param driver
	 * @param webElement
	 * @param optnSelect
	 * @param elementDescription
	 * @param timeOutArray
	 * @throws Exception
	 */
	public static void selectDropDownByIndex(WebDriver driver, WebElement webElement, int index, String elementDescription)  {
		selectDropDownByIndex(driver, webElement, index, false, 0, elementDescription);
	}

	/**
	 * Used to select drop down option using Select class and SelectByVisibleText method and verify the selected Text
	 * @param driver
	 * @param webElement
	 * @param optnSelect
	 * @param elementDescription
	 * @param timeOutArray
	 * @throws Exception
	 */
	public static void selectDropDownByIndex(WebDriver driver, WebElement webElement, int index, int moveToElementInSecs, String elementDescription)  {
		selectDropDownByIndex(driver, webElement, index, false, moveToElementInSecs, elementDescription);
	}

	/**
	 * Used to select drop down option using Select class and SelectByVisibleText method and verify the selected Text
	 * @param driver
	 * @param webElement
	 * @param optnSelect
	 * @param elementDescription
	 * @param timeOutArray
	 * @throws Exception
	 */
	public static void selectDropDownByIndex(WebDriver driver, WebElement webElement, int index, boolean waitForNetworkIdleState, int moveToElementInSecs, String elementDescription)  {
		int timeoutValue = Integer.parseInt(configProperty.getProperty("maxElementWait"));
		int retryCount =0;
		boolean staleElementException = false;
		do {
			try {
				if (!waitForElementToDisplay(driver, webElement, timeoutValue))
					throw new NoSuchElementException(elementDescription + " not found in page!!");
				waitForElementToBeClickable(driver, webElement, elementDescription);
				moveToElement(driver, webElement, moveToElementInSecs, elementDescription);
				Select select=new Select(webElement);
				select.selectByIndex(index);
				if(waitForNetworkIdleState)
					waitForNetworkIdleState(driver, 5);
				Log.event("DropDown value "+index+" is selected");
			}catch(NoSuchElementException e) {
				Log.fail("Element not found "+elementDescription+" "+e, driver);
			}
			catch(StaleElementReferenceException e1) {
				Log.event("click - Stale element exception "+elementDescription);
				staleElementException = true;
			}
			retryCount ++;
		} while(retryCount == 1 && staleElementException);
	}
	
	/**
	 * Used to click on the webelement
	 * @param driver
	 * @param btn - Element to be clicked
	 * @param elementDescription - Description of the element
	 * @param timeOutArray - custom timeout in secs
	 */
	public static void clear(WebDriver driver, WebElement webElement, String elementDescription)   {
		clear(driver, webElement, false, 1, elementDescription);
	}

	/**
	 * Used to click on the webelement
	 * @param driver
	 * @param webElement - Element to be clicked
	 * @param elementDescription - Description of the element to print in report
	 * @param waitForNetworkIdleState - To wait for page DOM load to complete after click
	 * @param timeout
	 */
	public static void clear(WebDriver driver, WebElement webElement, boolean waitForNetworkIdleState, int moveToElementWaitInSecs, String elementDescription)   {
		int timeoutValue = Integer.parseInt(configProperty.getProperty("maxElementWait"));
		try {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)							
					.withTimeout(Duration.ofSeconds(timeoutValue)) 			
					.pollingEvery(Duration.ofMillis(500)) 			
					.ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
					.ignoring(ElementClickInterceptedException.class);

			wait.until(new Function<WebDriver, Boolean>() {
				@Override
				public Boolean apply(WebDriver driver) {
					List<WebElement> webElements = locateElementsBy(driver, webElement);
					Boolean isElementDisplayed = false;
					if(webElements!= null) {
						Dimension dimension = webElements.get(0).getSize();
						if(dimension.getWidth() > 0 && dimension.getHeight() > 0) {
							moveToElement(driver, webElement, moveToElementWaitInSecs, elementDescription);
							webElements.get(0).clear();
							Log.event("Clear on "+elementDescription);
							if(waitForNetworkIdleState)
								waitForNetworkIdleState(driver, timeoutValue);
							isElementDisplayed = true;
						}
					}
					return isElementDisplayed;
				}
			});
		}catch(TimeoutException e){
			Log.fail("Failed to clear on "+elementDescription+" "+e, driver);	
		}
	}
	
	/**
	 * Used to verify Element Text Is Enabled
	 * @param driver
	 * @param webElement  
	 * @param elementDescription
	 * @param timeOutArray 
	 */
	public static void verifyElementIsEnabled(WebDriver driver, WebElement webElement, String elementDescription,
			int... timeOutArray) {
		int timeout = timeOutArray.length > 0 ? timeOutArray[0]
				: Integer.parseInt(configProperty.getProperty("maxElementWait"));
		try {
			if (!Utils.waitForElement(driver, webElement, timeout))
				throw new NoSuchElementException(elementDescription + " not found in page!!");
			Log.assertThat(webElement.isEnabled(), elementDescription + " is Enabled",
					elementDescription + " is displayed", driver);
		} catch (NoSuchElementException e) {
			Log.fail(elementDescription + " is not enabled" + e, driver);
		}
	}

	/**
		 * Used to verify Text Is Displayed For the selected Dropdown
		 * @param driver
		 * @param webElement
		 * @param expectedText
		 * @param waitForNetworkIdleState
		 * @param elementDescription
		 */
	public static void verifyElementTextIsDisplayedforSelcetedDropDown(WebDriver driver, WebElement webElement,String expectedText, boolean waitForNetworkIdleState,String elementDescription) {
		int timeoutValue = Integer.parseInt(configProperty.getProperty("maxElementWait"));
		int retryCount =0;
		boolean ElementNotInteractableException = false;
		do {
			try {
				Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)							
						.withTimeout(Duration.ofSeconds(timeoutValue)) 			
						.pollingEvery(Duration.ofMillis(500)) 			
						.ignoring(NoSuchElementException.class, StaleElementReferenceException.class)
						.ignoring(ElementNotInteractableException.class);

				wait.until(new Function<WebDriver, Boolean>() {
					@Override
					public Boolean apply(WebDriver driver) {
						List<WebElement> webElements = locateElementsBy(driver, webElement);
						Boolean isElementDisplayed = false;
						if(webElements!= null) {
							Dimension dimension = webElements.get(0).getSize();
							if(dimension.getWidth() > 0 && dimension.getHeight() > 0) {
								Select dropdown = new Select(webElement);
								WebElement selectedOption = dropdown.getFirstSelectedOption();

								Log.assertThat(selectedOption.getText().equals(expectedText), elementDescription+" is displayed", elementDescription+" is not displayed", driver);
								if(waitForNetworkIdleState)
									waitForNetworkIdleState(driver, timeoutValue);
								isElementDisplayed = true;
							}
						}
						return isElementDisplayed;
					}
				});
			}
			catch(ElementNotInteractableException e1) {
				Log.event("click - Element NotInteractableException "+elementDescription);
				ElementNotInteractableException = true;
			}
			retryCount ++;
		} while(retryCount == 1 && ElementNotInteractableException);
	}
	
	/**
	 * Window Navigation using Title and element is displayed
	 * @param driver
	 * @param childWindowNo
	 * @param element
	 * @param ChildwindowTitle
	 * @param timeOutArray
	 */
	public static void windowHandles(WebDriver driver,String ChildwindowTitle) {
		try {
			String parent=driver.getWindowHandle();
			Set<String>s=driver.getWindowHandles();
			Iterator<String> I1= s.iterator();
			while(I1.hasNext())
			{
				String child_window=I1.next();
				if(!parent.equals(child_window))
				{
					driver.switchTo().window(child_window);
					System.out.println(driver.switchTo().window(child_window).getTitle());
				}}
		}catch(NoSuchElementException e) {
			Log.fail(ChildwindowTitle+" is not displayed" +e, driver);	
		}
	}
	
	
	/**
	 * To get web elements size
	 * @param driver  - WebDriver
	 * @param webElementBy - By WebElement to get the size for
	 * @param timeout - Max wait duration
	 * @param description - wait until the number Of Elements To Be More Than zero
	 * @return element size - return zero if element not displayed else display the elements size
	 */
	public static int getWebElementSize(WebDriver driver, By webElementBy, String description, double... timeout) {
		double timeoutValue = timeout.length > 0 ? timeout[0]: Double.parseDouble(configProperty.getProperty("maxElementWait"));
		int elementSize = 0;
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds((long) timeoutValue));
			wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(webElementBy, 0));
			elementSize =  driver.findElements(webElementBy).size();
			Log.event(description +" elements displayed size: "+elementSize);
		}catch(TimeoutException e) {
			Log.event(description+" elements not displayed, size: 0");
		}
		return elementSize;
	}
	
	/**
	 * To get list of web elements
	 * @param driver  - WebDriver
	 * @param webElementBy - By WebElement to get the list of WebElements
	 * @param timeout - Max wait duration
	 * @param description - wait until the number Of Elements To Be More Than zero
	 * @return webElements - return a list of webElements
	 */
	public static List<WebElement> getWebElements(WebDriver driver, By webElementBy, String description, double... timeout) {
		double timeoutValue = timeout.length > 0 ? timeout[0]: Double.parseDouble(configProperty.getProperty("maxElementWait"));
		List<WebElement> webElements = null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds((long) timeoutValue));
			wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(webElementBy, 0));
			webElements =  driver.findElements(webElementBy);
			Log.event(description +" elements displayed: "+webElements.size());
		}catch(TimeoutException e) {
			Log.fail(description+" element not displayed");
		}
		return webElements;
	}
	
	public static String getElementText(WebDriver driver, WebElement webElement, String elementDescription) {
		String elementTextValue = null;
		try {
			if (!Utils.waitForElement(driver, webElement))
				elementTextValue =  webElement.getText();
			Log.event("Text value displayed for "+elementDescription+" : "+elementTextValue);
		}catch(TimeoutException e){
			Log.fail("Text value not displayed for "+elementDescription+" : "+elementTextValue);
		}
		return elementTextValue;
	}
	
	
	/**
	 * To wait for the specific element to be invisible
	 *
	 * @param driver  - WebDriver
	 * @param element - WebElement to wait for
	 * @param maxWait - Max wait duration
	 * @return elementStatus - return true if element is not present else
	 *         return false
	 */
	public static void waitForElementIsInvisible(WebDriver driver, WebElement webElement, int... timeout) {
		int timeoutValue = timeout.length > 0 ? timeout[0]: Integer.parseInt(configProperty.getProperty("maxElementWait"));
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutValue));
			wait.until(ExpectedConditions.invisibilityOf(webElement));
		}catch(TimeoutException e) {
			Log.fail("Page still loading - Interactions disabled", driver);
		}
	}


}