package com.OrangeHRM.pages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.testng.Assert;

import com.OrangeHRM.PageItem.OrangeHRMData.EnterEmpFullName;
import com.OrangeHRM.PageItem.OrangeHRMData.UserDetails;
import com.OrangeHRM.teststeps.admin.AdminConstants.AddUser;
import com.OrangeHRM.utils.BrowserActions;
import com.OrangeHRM.utils.ElementLayer;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.utils.WaitUtils;

import ASPIREAI.customfactories.AjaxElementLocatorFactory;
import ASPIREAI.customfactories.IFindBy;
import ASPIREAI.customfactories.PageFactory;

public class AdminPage extends LoadableComponent<AdminPage>{

	WebDriver driver;
	private boolean isPageLoaded;
	public ElementLayer elementLayer;
	public static List<Object> pageFactoryKey = new ArrayList<Object>();
	public static List<String> pageFactoryValue = new ArrayList<String>();


	@IFindBy(how = How.CSS, using = "h5[class*='oxd-table-filter-title']", AI = false)
	public WebElement titleHeader;

	@IFindBy(how = How.CSS, using = "input[placeholder*='Type for hints']", AI = false)
	public WebElement fldEmployeeName;

	@IFindBy(how = How.CSS, using = "button[class*='oxd-button--secondary'][type='button']", AI = false)
	public WebElement btnAdd;

	@IFindBy(how = How.CSS, using = "button[type='submit']", AI = false)
	public WebElement btnSave;

	@IFindBy(how = How.CSS, using = "div[class*='oxd-toast--success']", AI = false)
	public WebElement toastMsgSuccess;

	@IFindBy(how = How.CSS, using = "button[type='submit']", AI = false)
	public WebElement btnSearch;

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
		if(isPageLoaded && !(BrowserActions.waitForElementToDisplay(driver,titleHeader))) {
			Log.fail("Page did not open up. Site might be down.", driver);
		}
		elementLayer = new ElementLayer(driver);	
	}

	public AdminPage() {

	}

	public AdminPage(WebDriver driver) {
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, 10);
		PageFactory.initElements(finder, this);
		elementLayer = new ElementLayer(driver);
	}

	public AdminPage(WebDriver driver,int timeout){
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, timeout);
		PageFactory.initElements(finder, this);
		elementLayer = new ElementLayer(driver);
	}

	/**
	 * Click Add new user
	 * @author sathish.suresh
	 * @param driver
	 * @return AdminPage
	 * @throws IOException 
	 */
	public AdminPage clickAddUser(WebDriver driver) {
		BrowserActions.click(driver, btnAdd, "Add button");
		return this;
	}

	/**
	 * Enter employee Name and click suggestion employee name option.
	 * @author sathish.suresh
	 * @param driver
	 * @param enterEmpFullName
	 * @return AdminPage
	 */
	public AdminPage enterEmployeeName(WebDriver driver, EnterEmpFullName enterEmpFullName) {
		Actions actions = new Actions(driver);
		boolean found = false;
		while(!found) {
			actions.click(fldEmployeeName).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.BACK_SPACE).build().perform();
			actions.sendKeys(fldEmployeeName, enterEmpFullName.getFirstName()+" ").pause(2*1000).build().perform();
			actions.sendKeys(fldEmployeeName, enterEmpFullName.getLastName()).pause(2*1000).build().perform();
			int size = driver.findElements(By.cssSelector("[role='listbox'] div")).size();
			if(size > 0) {
				String attribute = driver.findElement(By.cssSelector("[role='listbox'] div")).getText();
				if(! (attribute.equals("No Records Found"))) 
					found = true;
			}
		}
		By options = By.cssSelector("[role='listbox'] div");
		BrowserActions.waitForElementToDisplay(driver, options, "Options");
		WebElement btnOption = options.findElement(driver);
		BrowserActions.click(driver, btnOption, "Option field");
		Log.messageStep("Entered Employee Name: "+enterEmpFullName.getFirstName() +" "+ enterEmpFullName.getLastName(), driver);
		return this;
	}

	/**
	 * Click to Save admin details 
	 * @author sathish.suresh
	 * @param driver
	 * @return AdminPage
	 */
	public AdminPage clickSavBtn(WebDriver driver) {
		BrowserActions.click(driver, btnSave, "Save button");
		return this;
	}

	/**
	 * Select User role in role dropdown
	 * @param driver
	 * @param status
	 * @param userDetails
	 * @return AdminPage
	 */
	public  AdminPage selectUserRole(WebDriver driver, AddUser status, UserDetails userDetails) {
		MyInfoPage myInfoPage = new MyInfoPage(driver);
		myInfoPage.slectDropdownByLabel(driver, status.getName(), userDetails.getUserRole() );
		return this;
	}

	/**
	 * Select user status in staus dropdown
	 * @param driver
	 * @param status
	 * @param userDetails
	 * @return AdminPage
	 */
	public  AdminPage selectStatus(WebDriver driver, AddUser status, UserDetails userDetails) {
		MyInfoPage myInfoPage = new MyInfoPage(driver);
		myInfoPage.slectDropdownByLabel(driver, status.getName(), userDetails.getStatus() );
		return this;
	}

	/**
	 * Enter username in username field
	 * @param driver
	 * @param username
	 * @param userDetails
	 * @return AdminPage
	 */
	public AdminPage enterUserName(WebDriver driver, AddUser username, UserDetails userDetails) {
		MyInfoPage myInfoPage = new MyInfoPage(driver);
		myInfoPage.enterDataOnTextField(driver, username.getName(), userDetails.getUsername() );
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -300);");
		return this;
	}

	/**
	 * Enter Password in password field
	 * @param driver
	 * @param password
	 * @param userDetails
	 * @return AdminPage
	 */
	public AdminPage enterPassword(WebDriver driver, AddUser password, UserDetails userDetails) {
		MyInfoPage myInfoPage = new MyInfoPage(driver);
		myInfoPage.enterDataOnTextField(driver, password.getName(), userDetails.getPassword() );
		return this;
	}

	/**
	 * Enter confirm Password in confirm password field
	 * @param driver
	 * @param confirmPassword
	 * @param userDetails
	 * @return AdminPage
	 */
	public AdminPage enterConfirmPassword(WebDriver driver, AddUser confirmPassword, UserDetails userDetails) {
		MyInfoPage myInfoPage = new MyInfoPage(driver);
		myInfoPage.enterDataOnTextField(driver, confirmPassword.getName(), userDetails.getPassword() );
		return this;
	}

	/**
	 * Verify success message is displayed
	 * @author sathish.suresh
	 * @param driver
	 * @return
	 */
	public AdminPage verifyToastMessage(WebDriver driver) {
		BrowserActions.waitForElementToDisplay(driver, toastMsgSuccess);
		return this;
	}

	/**
	 * Click to Search user details
	 * @author sathish.suresh
	 * @param driver
	 * @return
	 */
	public AdminPage clickSearchBtn(WebDriver driver) {
		BrowserActions.click(driver, btnSearch, "Search button");
		return this;
	}	

	/**
	 * Delete user
	 * @author sathish.suresh
	 * @param driver
	 * @param userDetails
	 * @return AdminPage
	 * @throws InterruptedException 
	 */
	public AdminPage deleteUser(WebDriver driver, UserDetails userDetails) {
		PimPage pimPage = new PimPage(driver);
		pimPage.VerifyTableAndDelete(driver,userDetails.getUsername());
		return this;
	}

	/**
	 * Create new User(Enter username, employee name, password, confirm passowrd and select role, status.
	 * @author sathish.suresh
	 * @param driver
	 * @param userDetails
	 * @return AdminPage
	 */
	public AdminPage createUser(WebDriver driver, UserDetails userDetails) {
		selectUserRole(driver, AddUser.USER_ROLE, UserDetails.builder()
				.userRole(userDetails.getUserRole())
				.build());
		enterEmployeeName(driver, EnterEmpFullName.builder()
				.firstName(userDetails.getFirstName())
				.lastName(userDetails.getLastName())
				.build());
		selectStatus(driver, AddUser.STATUS, UserDetails.builder()
				.status(userDetails.getStatus())
				.build());
		enterUserName(driver, AddUser.USERNAME, UserDetails.builder()
				.username(userDetails.getUsername())
				.build());
		enterPassword(driver, AddUser.PASSWORD, UserDetails.builder()
				.password(userDetails.getPassword())
				.build());
		enterConfirmPassword(driver, AddUser.CONFIRM_PASSWORD, UserDetails.builder()
				.password(userDetails.getPassword())
				.build());
		return this;
	}



}
