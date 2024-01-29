package com.OrangeHRM.pages;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.simple.JSONObject;
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
import com.OrangeHRM.teststeps.employee.PimConstants.EmployeeInfo;
import com.OrangeHRM.teststeps.employee.PimConstants.NavName;
import com.OrangeHRM.teststeps.employee.PimConstants.PIMTabs;
import com.OrangeHRM.utils.BrowserActions;
import com.OrangeHRM.utils.ElementLayer;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.utils.WaitUtils;

import ASPIREAI.customfactories.AjaxElementLocatorFactory;
import ASPIREAI.customfactories.IFindBy;
import ASPIREAI.customfactories.PageFactory;

public class PimPage extends LoadableComponent<PimPage>{

	WebDriver driver;
	private boolean isPageLoaded;
	public ElementLayer elementLayer;
	public static List<Object> pageFactoryKey = new ArrayList<Object>();
	public static List<String> pageFactoryValue = new ArrayList<String>();
	static ArrayList<String> lstEmployeesInfo = new ArrayList<String>();


	@IFindBy(how = How.CSS, using = "h6[class*='oxd-topbar-header']", AI = false)
	public WebElement topHeader;

	@IFindBy(how = How.CSS, using = "button[class*='oxd-button--secondary'][type='button']", AI = false)
	public WebElement btnAdd;

	@IFindBy(how = How.CSS, using = "input[name='firstName']", AI = false)
	public WebElement fldFirstName;

	@IFindBy(how = How.CSS, using = "input[name='lastName']", AI = false)
	public WebElement fldLastName;

	@IFindBy(how = How.CSS, using = "button[type='submit']", AI = false)
	public WebElement btnSave;

	@IFindBy(how = How.CSS, using = "div.oxd-grid-2 input[class*='oxd-input oxd-input']", AI = false)
	public WebElement txtEmpId;

	@IFindBy(how = How.CSS, using = "input[placeholder*='Type for hints']", AI = false)
	public WebElement fldEmployeeName;

	@IFindBy(how = How.CSS, using = "div[class*='oxd-toast--success']", AI = false)
	public WebElement toastMsgSuccess;

	@IFindBy(how = How.CSS, using = "button[type='submit']", AI = false)
	public WebElement btnSearch;

	@IFindBy(how = How.XPATH, using = "//span[text()='(1) Record Found']", AI = false)
	public WebElement txtSearchResult;

	@IFindBy(how = How.CSS, using = "button[class*='oxd-table-cell-action-space']", AI = false)
	public WebElement btnIconDelete;

	@IFindBy(how = How.XPATH, using = "//button[text()=' Yes, Delete ']", AI = false)
	public WebElement btnYesDelete;

	@IFindBy(how = How.CSS, using = "span[class*='oxd-switch-input']", AI = false)
	public WebElement btnCreateLgnDetails;


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

	public PimPage() {

	}

	public PimPage(WebDriver driver) {
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, 10);
		PageFactory.initElements(finder, this);
		elementLayer = new ElementLayer(driver);
	}

	public PimPage(WebDriver driver,int timeout){
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, timeout);
		PageFactory.initElements(finder, this);
		elementLayer = new ElementLayer(driver);
	}


	/**
	 * Write json file to add employee details
	 * @author sathish.suresh
	 * @param empInfo
	 */
	@SuppressWarnings("unchecked")
	public void writeJsonFile(ArrayList<String> empInfo) {
		try {
			File file=new File("JsonTestDate\\EmployeeDetails.json");  
			file.createNewFile(); 
			FileWriter fileWriter = new FileWriter(file); 
			System.out.println("Writing JSON object to file");  
			System.out.println("-----------------------"); 
			JSONObject jsonEmpInfo = new JSONObject();  
			for (int i = 0; i < empInfo.size(); i++) {

				switch (i) {
				case 0:
					jsonEmpInfo.put("Id", empInfo.get(i));
				case 1:
					jsonEmpInfo.put("Firstname", empInfo.get(i));
				case 2:
					jsonEmpInfo.put("Lastname", empInfo.get(i));
				case 3:
					jsonEmpInfo.put("JobTitle", empInfo.get(i));
				case 4:
					jsonEmpInfo.put("EmploymentStatus", empInfo.get(i));
				case 5:
					jsonEmpInfo.put("subUnit", empInfo.get(i));
				case 6:
					jsonEmpInfo.put("Supervisor", empInfo.get(i));
				default:
					break;
				}

			}

			fileWriter.write(jsonEmpInfo.toJSONString());
			fileWriter.flush();  
			fileWriter.close(); 
		}
		catch (Exception e) {
			e.printStackTrace();  
		}
	}

	/**
	 * Click Add new employee
	 * @author sathish.suresh
	 * @param driver
	 * @return PimPage
	 */
	public PimPage clickAddEmployee(WebDriver driver) {
		BrowserActions.click(driver, btnAdd, "Add button");
		return this;
	}

	/**
	 * Click to save employee details
	 * @author sathish.suresh
	 * @param driver
	 * @return PimPage
	 */
	public PimPage clickSavBtn(WebDriver driver) {
		BrowserActions.click(driver, btnSave, "Save button");
		return this;
	}

	/**
	 * Verify success message
	 * @author sathish.suresh
	 * @param driver
	 * @return PimPage
	 */
	public PimPage verifyToastMessage(WebDriver driver) {
		BrowserActions.waitForElementToDisplay(driver, toastMsgSuccess);
		BrowserActions.waitForElementIsInvisible(driver, toastMsgSuccess);
		return this;
	}

	/**
	 * Enter firstName
	 * @author sathish.suresh
	 * @param driver
	 * @param firstName
	 * @return PimPage
	 */
	public PimPage enterFirstName(WebDriver driver, String firstName) {
		lstEmployeesInfo.add(firstName);
		BrowserActions.type(driver, fldFirstName, firstName, "FirstName");
		return this;
	}

	/**
	 * Enter lastName
	 * @author sathish.suresh
	 * @param driver
	 * @param lastName
	 * @return PimPage
	 */
	public PimPage enterLastName(WebDriver driver, String lastName) {
		lstEmployeesInfo.add(lastName);
		BrowserActions.type(driver, fldLastName, lastName, "LastName");
		return this;
	}

	/**
	 * Enter employee id
	 * @author sathish.suresh
	 * @param driver
	 * @return
	 */
	public PimPage enterEmployeeId(WebDriver driver) {
		int number = (int) (Math.floor(Math.random() * 99) + 00);
		//String value = String.valueOf( rand.nextInt(00, 99) );
		String value = String.valueOf( number );
		BrowserActions.type(driver, txtEmpId, value, "Employeee Id");
		return this;
	}

	/**
	 * get Unique Employee id
	 * @author sathish.suresh
	 * @param driver
	 * @return employee id
	 */
	public String getEmployeeId(WebDriver driver) {
		BrowserActions.waitForElementToDisplay(driver, txtEmpId);
		String empId = txtEmpId.getAttribute("value");
		lstEmployeesInfo.add(empId);
		//Log.messageStep("Employee Id : " + empId, driver);
		return empId;
	}

	/**
	 * Add employee full name
	 * @author sathish.suresh
	 * @param driver
	 * @param enterEmpFullName
	 * @return PimPage
	 */
	public PimPage addEmployeeName(WebDriver driver, EnterEmpFullName enterEmpFullName) {
		enterFirstName(driver, enterEmpFullName.getFirstName());
		enterLastName(driver, enterEmpFullName.getLastName());
		return this;
	}

	/**
	 * Enter value based on element label
	 * @author sathish.suresh
	 * @param driver
	 * @param empInfo
	 * @param text
	 * @return PimPage
	 */
	public PimPage typeInTextFldByLabel(WebDriver driver, EmployeeInfo empInfo, String text) {
		By txtfld = By.xpath("//label[text()='"+ empInfo.getName() +"']//parent::div //parent::div /div/input");
		BrowserActions.waitForElementToDisplay(driver, txtfld, empInfo.getName());
		WebElement element = txtfld.findElement(driver);
		BrowserActions.type(driver, element, text, empInfo.getName());
		return this;
	}

	/**
	 * Enter employee Name
	 * @author sathish.suresh
	 * @param driver
	 * @param enterEmpFullName
	 * @return PimPage
	 */
	public PimPage enterEmployeeName(WebDriver driver, EnterEmpFullName enterEmpFullName) {
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
					Log.messageStep("Employee Name: "+enterEmpFullName.getFirstName() +" "+ enterEmpFullName.getLastName() + " selected", driver);
					
			}
		}

		By options = By.cssSelector("[role='listbox'] div");
		BrowserActions.waitForElementToDisplay(driver, options, "Options");
		int size = BrowserActions.getWebElementSize(driver, options, "Options");
		if(size == 1) {
			WebElement btnOption = options.findElement(driver);
			BrowserActions.click(driver, btnOption, "Option field");
		}
		return this;
	}

	/**
	 * Select top nav menu
	 * @author sathish.suresh
	 * @param driver
	 * @param navMenu
	 * @return PimPage
	 */
	public PimPage selectTopNavMenu(WebDriver driver, NavName navMenu) {
		By navTab = By.xpath("//a[@class='oxd-topbar-body-nav-tab-item' and text()='"+ navMenu.getName() +"']");
		BrowserActions.waitForElementToDisplay(driver, navTab, navMenu.getName());
		WebElement topNavMenu = navTab.findElement(driver);
		BrowserActions.click(driver, topNavMenu, navMenu.getName());
		//Log.messageStep("Nav menu : " + navMenu.getName()+ " selected" , driver);
		return this;
	}

	/**
	 * Click to Search employee details
	 * @author sathish.suresh
	 * @param driver
	 * @return PimPage
	 */
	public PimPage clickSearchBtn(WebDriver driver) {
		BrowserActions.click(driver, btnSearch, "Search button");
		return this;
	}

	/**
	 * Validate employee details in table and click on employee.
	 * @author sathish.suresh
	 * @param driver
	 * @return MyInfoPage
	 * @throws InterruptedException 
	 */
	public MyInfoPage validateEmployeeListAndClick(WebDriver driver) {
		ArrayList<String> actList = new ArrayList<String>();
		By row = By.cssSelector("div[class='oxd-table-card']");
		BrowserActions.waitForElementToDisplay(driver, row, "Table rows");
		int rowSize = BrowserActions.getWebElementSize(driver, row, "Table rows");
		if(rowSize == 1 && (BrowserActions.waitForElementToDisplay(driver, txtSearchResult))){

			List<WebElement> columns = driver.findElements(By.cssSelector("div[class='oxd-table-cell oxd-padding-cell'] div"));
			for (WebElement col : columns) {
				if(!(col.getText().equals(""))  && col.getText().length() != 0) {
					String colText = BrowserActions.getText(driver, col, "Table field");
					actList.add(colText);
				}
			}
			Collections.sort(actList);
			Collections.sort(lstEmployeesInfo);
			boolean equal = lstEmployeesInfo.containsAll(actList);
			Log.assertThat(equal, "Employee Name and Id is Verified Successfully", "Failed to Verify Employee Name and Id", driver);
			if(equal) {
				WebElement rowEle = row.findElement(driver);
				BrowserActions.click(driver, rowEle, "Employee row");
			}

		}

		return new MyInfoPage(driver).get();
	}


	/**
	 * Click employee in table list
	 * @author sathish.suresh
	 * @param driver
	 * @return MyInfoPage
	 */
	public MyInfoPage ClickEmployeeInList(WebDriver driver) {
		By row = By.cssSelector("div[class='oxd-table-card']");
		BrowserActions.waitForElementToDisplay(driver, row, "Table rows");
		int rowSize = BrowserActions.getWebElementSize(driver, row, "Table rows");
		if(rowSize == 1 && (BrowserActions.waitForElementToDisplay(driver, txtSearchResult))){
			WebElement rowEle = row.findElement(driver);
			BrowserActions.click(driver, rowEle, "Employee row");
		}

		return new MyInfoPage(driver).get();
	}

	/**
	 * Click PIM tab
	 * @author sathish.suresh
	 * @param driver
	 * @param tabDetails
	 * @return MyInfoPage
	 */
	public MyInfoPage clickPIMTab(WebDriver driver, PIMTabs tabDetails ) {
		By tab = By.xpath("//a[contains(@class, 'orangehrm-tabs-item')][text()='"+ tabDetails.getName() +"']") ;
		BrowserActions.waitForElementToDisplay(driver, tab, tabDetails.getName());
		WebElement btnTab = tab.findElement(driver);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -1000);");
		BrowserActions.click(driver, btnTab, tabDetails.getName());
		Log.messageStep("PIM Tab : " + tabDetails.getName()+ " selected" , driver);
		return new MyInfoPage(driver).get();
	}

	/**
	 * Validate employee details list
	 * @author sathish.suresh
	 * @param driver
	 * @param empJobInfo
	 * @return PimPage
	 */
	public PimPage validateEmployeeList(WebDriver driver, ArrayList<String> empJobInfo) {
		ArrayList<String> actList = new ArrayList<String>();
		By row = By.cssSelector("div[class='oxd-table-card']");
		BrowserActions.waitForElementToDisplay(driver, row, "Table rows");
		int rowSize = BrowserActions.getWebElementSize(driver, row, "Table rows");
		if(rowSize == 1 && (BrowserActions.waitForElementToDisplay(driver, txtSearchResult))){

			List<WebElement> columns = driver.findElements(By.cssSelector("div[class='oxd-table-cell oxd-padding-cell'] div"));
			for (WebElement col : columns) {
				if(!(col.getText().equals(""))  && col.getText().length() != 0) {
					String colText = BrowserActions.getText(driver, col, "Table field");
					actList.add(colText);
				}
			}
			writeJsonFile(actList);
			Collections.sort(actList);
			Collections.sort(empJobInfo);
			boolean equal = actList.equals(empJobInfo);
			Log.assertThat(equal, "Employee Job Details is Verified Successfully", "Failed to Verify Employee Job Details", driver);

		}
		return this;
	}

	/**
	 * Verify details table and delete based value 
	 * @author sathish.suresh
	 * @param driver
	 * @param value
	 * @throws InterruptedException 
	 */
	public void VerifyTableAndDelete(WebDriver driver, String value) {
		ArrayList<String> actList = new ArrayList<String>();
		By row = By.cssSelector("div[class='oxd-table-card']");
		BrowserActions.waitForElementToDisplay(driver, row, "Table rows");
		int rowSize = BrowserActions.getWebElementSize(driver, row, "Table rows");
		if(rowSize == 1 && (BrowserActions.waitForElementToDisplay(driver, txtSearchResult))){

			List<WebElement> columns = driver.findElements(By.cssSelector("div[class='oxd-table-cell oxd-padding-cell'] div"));
			for (WebElement col : columns) {
				if(!(col.getText().equals(""))  && col.getText().length() != 0) {
					String colText = BrowserActions.getText(driver, col, "Table field");
					actList.add(colText);
				}
			}

			boolean equal = actList.contains(value);
			if(equal) {
				BrowserActions.click(driver, btnIconDelete, "Delete icon");
				BrowserActions.click(driver, btnYesDelete, "Yes, Delete");
			}

		}

	}

	/**
	 * Delete employee based on employee id
	 * @author sathish.suresh
	 * @param driver
	 * @param employeeId
	 * @return PimPage
	 * @throws InterruptedException 
	 */
	public PimPage deleteEmployee(WebDriver driver, String employeeId) {
		VerifyTableAndDelete(driver, employeeId);
		return this;
	}

	/**
	 * Click on create login button
	 * @author sathish.suresh
	 * @param driver
	 * @return PimPage
	 */
	public PimPage clickOncreateLogin(WebDriver driver) {
		BrowserActions.click(driver, btnCreateLgnDetails, "Create login details");
		return this;
	}

	/**
	 * Enter username
	 * @author sathish.suresh
	 * @param driver
	 * @param username
	 * @param userDetails
	 * @return PimPage
	 */
	public PimPage enterUserName(WebDriver driver, AddUser username, UserDetails userDetails) {
		MyInfoPage myInfoPage = new MyInfoPage(driver);
		myInfoPage.enterDataOnTextField(driver, username.getName(), userDetails.getUsername() );
		return this;
	}

	/**
	 * Enter password
	 * @param driver
	 * @param password
	 * @param userDetails
	 * @return PimPage
	 */
	public PimPage enterPassword(WebDriver driver, AddUser password, UserDetails userDetails) {
		MyInfoPage myInfoPage = new MyInfoPage(driver);
		myInfoPage.enterDataOnTextField(driver, password.getName(), userDetails.getPassword() );
		return this;
	}

	/**
	 * Enter confirm password
	 * @author sathish.suresh
	 * @param driver
	 * @param confirmPassword
	 * @param userDetails
	 * @return PimPage
	 */
	public PimPage enterConfirmPassword(WebDriver driver, AddUser confirmPassword, UserDetails userDetails) {
		MyInfoPage myInfoPage = new MyInfoPage(driver);
		myInfoPage.enterDataOnTextField(driver, confirmPassword.getName(), userDetails.getPassword() );
		return this;
	}
	
	/**
	 * Enter Employee login credentails
	 * @author sathish.suresh
	 * @param driver
	 * @param userDetails
	 * @return
	 */
	public PimPage EnterLoginCredentials(WebDriver driver, UserDetails userDetails ) {
		
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
