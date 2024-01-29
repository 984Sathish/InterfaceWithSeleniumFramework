package com.OrangeHRM.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.testng.Assert;

import com.OrangeHRM.utils.BrowserActions;
import com.OrangeHRM.utils.ElementLayer;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.utils.WaitUtils;

import ASPIREAI.customfactories.AjaxElementLocatorFactory;
import ASPIREAI.customfactories.IFindBy;
import ASPIREAI.customfactories.PageFactory;

public class LoginPage extends LoadableComponent<LoginPage>{

	WebDriver driver;
	private String appURL;
	private boolean isPageLoaded;
	public ElementLayer elementLayer;
	public static List<Object> pageFactoryKey = new ArrayList<Object>();
	public static List<String> pageFactoryValue = new ArrayList<String>();

	@IFindBy(how = How.CSS, using = "button[class*='orangehrm-login-button']", AI = false)
	public WebElement btnLogin;

	@IFindBy(how = How.CSS, using = "input[name='username']", AI = false)
	public WebElement fldUsername;

	@IFindBy(how = How.CSS, using = "input[name='password']", AI = false)
	public WebElement fldPassword;


	@Override
	protected void load() {
		isPageLoaded = true;
		WaitUtils.waitForPageLoad(driver);
	}

	@Override
	protected void isLoaded() throws Error {
		if(!isPageLoaded) {
			Assert.fail();
		}
		if(isPageLoaded && !(BrowserActions.waitForElementToDisplay(driver,btnLogin))) {
			Log.fail("Page did not open up. Site might be down.", driver);
		}
		elementLayer = new ElementLayer(driver);	
	}

	public LoginPage() {

	}

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, 10);
		PageFactory.initElements(finder, this);
		elementLayer = new ElementLayer(driver);
	}

	public LoginPage(WebDriver driver, String url) {
		appURL = url;
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, 10);
		PageFactory.initElements(finder, this);
		driver.get(appURL);
	}

	public LoginPage(WebDriver driver, int timeout){
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, timeout);
		PageFactory.initElements(finder, this);
		elementLayer = new ElementLayer(driver);
	}

	/**
	 * Enters the username in username field
	 * @author sathish.suresh
	 * @param driver
	 * @param username
	 * @return LoginPage
	 */
	public LoginPage enterUsername(WebDriver driver, String username) {
		BrowserActions.type(driver, fldUsername, username, "Username field");
		return this;
	}

	/**
	 * Enters the password in password field
	 * @author sathish.suresh
	 * @param driver
	 * @param password
	 * @return LoginPage
	 */
	public LoginPage enterPassword(WebDriver driver, String password) {
		BrowserActions.type(driver, fldPassword, password, "Password field");
		return this;
	}

	/**
	 * Clicks on the login button
	 * @author sathish.suresh
	 * @param driver
	 * @return LoginPage
	 */
	public LoginPage clickLogin(WebDriver driver) {
		BrowserActions.click(driver, btnLogin, "Login button");
		return this;
	}

	/**
	 * Login to application with valid credential
	 * @author sathish.suresh
	 * @param driver
	 * @param username
	 * @param password
	 * @return DashboardPage
	 */
	public DashboardPage LoginToApplication(WebDriver driver, String username, String password) {
		enterUsername(driver, username);
		enterPassword(driver, password);
		clickLogin(driver);
		Log.messageStep("Login to Application", driver);
		return new DashboardPage(driver).get();
	}

}
