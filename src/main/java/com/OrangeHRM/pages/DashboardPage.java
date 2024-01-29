package com.OrangeHRM.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.testng.Assert;

import com.OrangeHRM.teststeps.employee.DashboardConstants.menuName;
import com.OrangeHRM.utils.BrowserActions;
import com.OrangeHRM.utils.ElementLayer;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.utils.WaitUtils;
import com.OrangeHRM.webdriverManager.DriverManager;

import ASPIREAI.customfactories.AjaxElementLocatorFactory;
import ASPIREAI.customfactories.IFindBy;
import ASPIREAI.customfactories.PageFactory;

public class DashboardPage extends LoadableComponent<DashboardPage>{

	WebDriver driver;
	private boolean isPageLoaded;
	public ElementLayer elementLayer;
	public static List<Object> pageFactoryKey = new ArrayList<Object>();
	public static List<String> pageFactoryValue = new ArrayList<String>();


	@IFindBy(how = How.CSS, using = "h6[class*='oxd-topbar-header']", AI = false)
	public WebElement topHeader;

	@IFindBy(how = How.CSS, using = "span[class*='oxd-main-menu-item--name']", AI = false)
	public WebElement fldMenu;

	@IFindBy(how = How.CSS, using = "li.oxd-userdropdown", AI = false)
	public WebElement btnUserProfile;

	@IFindBy(how = How.CSS, using = "a[href*='logout']", AI = false)
	public WebElement btnLogout;

	@IFindBy(how = How.CSS, using = "p.oxd-userdropdown-name", AI = false)
	public WebElement fldProfileName;

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
		if(isPageLoaded && !(BrowserActions.waitForElementToDisplay(driver, topHeader))) {
			Log.fail("Page did not open up. Site might be down.", driver);
		}
		elementLayer = new ElementLayer(driver);	
	}

	public DashboardPage() {

	}

	public DashboardPage(WebDriver driver) {
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, 10);
		PageFactory.initElements(finder, this);
		elementLayer = new ElementLayer(driver);
	}

	public DashboardPage(WebDriver driver,int timeout){
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, timeout);
		PageFactory.initElements(finder, this);
		elementLayer = new ElementLayer(driver);
	}

	/**
	 * Click 'Main menu' based on menuName
	 * @author sathish.suresh
	 * @param driver
	 * @param menu
	 * @return 
	 */
	public menuName selectMenu(WebDriver driver, menuName menu) {
		BrowserActions.waitForElementToDisplay(driver, fldMenu);
		By txtMenu = By.xpath("//a[contains(@class,'oxd-main-menu-item')] //span[text()='"+menu.getName()+"']");
		BrowserActions.waitForElementToDisplay(driver, txtMenu, menu.getName());
		WebElement btnMenu = txtMenu.findElement(driver);
		BrowserActions.waitForElementToBeClickable(driver, btnMenu,  menu.getName());
		BrowserActions.click(driver, btnMenu, menu.getName());	
		return menu;
	}

	/**
	 * Select menu based on menu name
	 * @author sathish.suresh
	 * @param driver
	 * @param menu
	 * @return selected menu page
	 */
	public Object mainMenuSelection(WebDriver driver, menuName menu) {
		menuName selectedMenu = selectMenu(driver, menu);
		switch (selectedMenu) {
		case ADMIN:
			return new AdminPage(driver).get();
		case PIM:
			return new PimPage(driver).get();
		case LEAVE:
			return new LeavePage(driver).get();
		case TIME:
			return new TimePage(driver).get();
		case RECRUITMENT:
			return new RecruitmentPage(driver).get();
		case CLAIM:
			return new ClaimPage(driver).get();
		default:
			return null;
		}
	}

	/**
	 * click on user profile in dashboard page
	 * @author sathish.suresh
	 * @param driver
	 * @return DashboardPage
	 */
	public DashboardPage clickOnUserProfile(WebDriver driver) {
		BrowserActions.click(driver, btnUserProfile, "User profile");
		return this;
	}

	/**
	 * Logout to OrangeHRM application
	 * @author sathish.suresh
	 * @param driver
	 * @return LoginPage
	 */
	public LoginPage logOut(WebDriver driver) {
		BrowserActions.click(driver, btnLogout, "Logout button");
		return new LoginPage(driver).get();
	}

	/**
	 * get profile name from OrangeHRM application
	 * @author sathish.suresh
	 * @param driver
	 * @return profile name
	 */
	public String getProfileName(WebDriver driver) {
		BrowserActions.waitForElementToDisplay(driver, fldProfileName);
		return fldProfileName.getText();
	}

}
