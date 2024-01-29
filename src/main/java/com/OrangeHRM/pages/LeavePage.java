package com.OrangeHRM.pages;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
import com.OrangeHRM.PageItem.OrangeHRMData.LeaveDetails;
import com.OrangeHRM.teststeps.Leave.LeaveConstants.AssignLeave;
import com.OrangeHRM.teststeps.Leave.LeaveConstants.NavName;
import com.OrangeHRM.utils.BrowserActions;
import com.OrangeHRM.utils.ElementLayer;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.utils.WaitUtils;

import ASPIREAI.customfactories.AjaxElementLocatorFactory;
import ASPIREAI.customfactories.IFindBy;
import ASPIREAI.customfactories.PageFactory;

public class LeavePage extends LoadableComponent<LeavePage>{

	WebDriver driver;
	private boolean isPageLoaded;
	public ElementLayer elementLayer;
	public static List<Object> pageFactoryKey = new ArrayList<Object>();
	public static List<String> pageFactoryValue = new ArrayList<String>();


	@IFindBy(how = How.XPATH, using = "//a[text()='My Leave']", AI = false)
	public WebElement txtMyLeave;

	@IFindBy(how = How.CSS, using = "input[placeholder*='Type for hints']", AI = false)
	public WebElement fldEmployeeName;

	@IFindBy(how = How.CSS, using = "button[type='submit']", AI = false)
	public WebElement btnAssign;

	@IFindBy(how = How.CSS, using = "div[class*='oxd-toast--success']", AI = false)
	public WebElement toastMsgSuccess;

	@IFindBy(how = How.XPATH, using = "//button[text()=' Ok ']", AI = false)
	public WebElement btnOk;

	@IFindBy(how = How.CSS, using = "button[type='submit']", AI = false)
	public WebElement btnSearch;

	@IFindBy(how = How.XPATH, using = "//span[text()='(1) Record Found']", AI = false)
	public WebElement txtSearchResult;

	@IFindBy(how = How.XPATH, using = "//label[text()='From Date']/parent::div/parent::div //input", AI = false)
	public WebElement fldInputDate;

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
		if(isPageLoaded && !(BrowserActions.waitForElementToDisplay(driver, txtMyLeave))) {
			Log.fail("Page did not open up. Site might be down.", driver);
		}
		elementLayer = new ElementLayer(driver);	
	}

	public LeavePage() {

	}

	public LeavePage(WebDriver driver) {
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, 10);
		PageFactory.initElements(finder, this);
		elementLayer = new ElementLayer(driver);
	}

	public LeavePage(WebDriver driver,int timeout){
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, timeout);
		PageFactory.initElements(finder, this);
		elementLayer = new ElementLayer(driver);
	}

	/**
	 * Select top nav menu based on nav menu name
	 * @author sathish.suresh
	 * @param driver
	 * @param navMenu
	 * @return LeavePage
	 */
	public LeavePage selectTopNavMenu(WebDriver driver, NavName navMenu) {
		By navTab = By.xpath("//a[@class='oxd-topbar-body-nav-tab-item' and text()='"+ navMenu.getName() +"']");
		BrowserActions.waitForElementToDisplay(driver, navTab, navMenu.getName());
		WebElement topNavMenu = navTab.findElement(driver);
		BrowserActions.click(driver, topNavMenu, navMenu.getName());
		//Log.messageStep("Nav menu : " + navMenu.getName()+ " selected" , driver);
		return this;
	}

	/**
	 * Select Leave type in leave type dropdown
	 * @author sathish.suresh
	 * @param driver
	 * @param leaveType
	 * @param leaveDetails
	 * @return LeavePage
	 */
	public LeavePage selectLeaveType(WebDriver driver, AssignLeave leaveType, LeaveDetails leaveDetails) {
		MyInfoPage myInfoPage = new MyInfoPage(driver);
		myInfoPage.slectDropdownByLabel(driver, leaveType.getName(), leaveDetails.getLeaveType() );
		return this;
	}

	/**
	 * Enter from date in date field
	 * @author sathish.suresh
	 * @param driver
	 * @param fromDate
	 * @param leaveDetails
	 * @return LeavePage
	 */
	public LeavePage enterFromDate(WebDriver driver, AssignLeave fromDate, LeaveDetails leaveDetails) {
		MyInfoPage myInfoPage = new MyInfoPage(driver);
		myInfoPage.enterDataOnTextField(driver, fromDate.getName(), leaveDetails.getCurrentDate() );
		return this;
	} 

	/**
	 * Enter date based on element label
	 * @author sathish.suresh
	 * @param driver
	 * @param lblName
	 * @return LeavePage
	 */
	public LeavePage enterDate(WebDriver driver, AssignLeave lblName) {
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -200);");
		By txtfld = By.xpath("//label[text()='"+ lblName.getName() +"'] //parent::div //parent::div //input");
		BrowserActions.waitForElementToDisplay(driver, txtfld, lblName.getName());
		WebElement txtfldelement = txtfld.findElement(driver);
		BrowserActions.click(driver, txtfldelement, lblName.getName());	
		return this;
	}  

	/**
	 * Enter employee Name
	 * @author sathish.suresh
	 * @param driver
	 * @param enterEmpFullName
	 * @return LeavePage
	 */
	public LeavePage enterEmployeeName(WebDriver driver, EnterEmpFullName enterEmpFullName) {
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
		return this;
	}

	/**
	 * Cick on assign button
	 * @author sathish.suresh
	 * @param driver
	 * @return LeavePage
	 */
	public LeavePage clickAssignBtn(WebDriver driver) {
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
		BrowserActions.click(driver, btnAssign, "Assign button");
		return this;
	}

	/**
	 * click on 'ok' button 
	 * @author sathish.suresh
	 * @param driver
	 * @return LeavePage
	 */
	public LeavePage clickOkBtn(WebDriver driver) {
		BrowserActions.click(driver, btnOk, "Ok button");
		return this;
	}

	/**
	 * Verify success message
	 * @author sathish.suresh
	 * @param driver
	 * @return LeavePage
	 */
	public LeavePage verifyToastMessage(WebDriver driver) {
		BrowserActions.waitForElementToDisplay(driver, toastMsgSuccess);
		return this;
	}

	/**
	 * click on search button
	 * @author sathish.suresh
	 * @param driver
	 * @return LeavePage
	 */
	public LeavePage searchMyLeave(WebDriver driver) {
		BrowserActions.click(driver, btnSearch, "Search button");
		return this;
	}

	/**
	 * Validate leave details
	 * @author sathish.suresh
	 * @param driver
	 * @param leaveDetails
	 * @return LeavePage
	 */
	public LeavePage validateLeaveDetails(WebDriver driver, ArrayList<String> leaveDetails) {
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
			Collections.sort(leaveDetails);
			boolean equal = actList.containsAll(leaveDetails);
			Log.assertThat(equal, "Leave Details is verified successfully", "Failed to verify Leave details", driver);
			leaveDetails.clear();

		}
		return this;
	}

	/**
	 * Write json file to add leave details
	 * @param leaveInfo
	 */
	@SuppressWarnings("unchecked")
	public void writeJsonFile(ArrayList<String> leaveInfo) {
		try {
			File file=new File("JsonTestDate\\LeaveDetails.json");  
			file.createNewFile(); 
			FileWriter fileWriter = new FileWriter(file); 
			System.out.println("Writing JSON object to file");  
			System.out.println("-----------------------"); 
			JSONObject jsonLeaveInfo = new JSONObject();  
			for (int i = 0; i < leaveInfo.size(); i++) {

				switch (i) {
				case 0:
					jsonLeaveInfo.put("date", leaveInfo.get(i));
				case 1:
					jsonLeaveInfo.put("EmployeeName", leaveInfo.get(i));
				case 2:
					jsonLeaveInfo.put("LeaveTaken", leaveInfo.get(i));
				case 3:
					jsonLeaveInfo.put("LeaveBalance", leaveInfo.get(i));
				case 4:
					jsonLeaveInfo.put("NumberOfDays", leaveInfo.get(i));
				case 5:
					jsonLeaveInfo.put("Status", leaveInfo.get(i));
				default:
					break;
				}

			}

			fileWriter.write(jsonLeaveInfo.toJSONString());
			fileWriter.flush();  
			fileWriter.close(); 
		}
		catch (Exception e) {
			e.printStackTrace();  
		}
	}

	/**
	 * select workday in calender
	 * @param driver
	 * @param txtDate
	 * @return selected date
	 */
	public String clickWorkingDayInCalender(WebDriver driver, String... txtDate) {
		Random rand = new Random();  
		int date;
		By dates = By.cssSelector("div[class='oxd-calendar-date-wrapper'] div[class='oxd-calendar-date']");	
		BrowserActions.waitForElementToDisplay(driver, dates, "Dates field");
		if(txtDate.length > 0) {
			String valDate = txtDate[0].split("-")[2] ;
			List<WebElement> lstDates = driver.findElements(By.xpath("//div[@class='oxd-calendar-date-wrapper'] //div[@class='oxd-calendar-date'][text()='"+valDate+"']"));
			if(lstDates.size() == 1) {
				BrowserActions.click(driver, lstDates.get(0) , "Date: "+valDate);
			}
		}
		else {

			while(true) {
				date = rand.nextInt(30);
				List<WebElement> lstDates = driver.findElements(By.xpath("//div[@class='oxd-calendar-date-wrapper'] //div[@class='oxd-calendar-date'][text()='"+date+"']"));
				if(lstDates.size() == 1) {
					BrowserActions.click(driver, lstDates.get(0) , "Date: "+date);
					break;
				}
			}
		}

		BrowserActions.waitForElementToDisplay(driver, fldInputDate);
		return fldInputDate.getAttribute("value");
	}
	
	public String assignLeave(WebDriver driver, EnterEmpFullName enterEmpFullName, LeaveDetails leaveDetails) {
		enterEmployeeName(driver, EnterEmpFullName.builder()
				.firstName(enterEmpFullName.getFirstName())
				.lastName(enterEmpFullName.getLastName())
				.build());

		selectLeaveType(driver, AssignLeave.LEAVE_TYPE, LeaveDetails.builder()
				.leaveType(leaveDetails.getLeaveType())
				.build());

		enterDate(driver, AssignLeave.FROM_DATE);

		String selectedDate = clickWorkingDayInCalender(driver);

		enterDate(driver, AssignLeave.TO_DATE);
		
		return selectedDate;
	}

}

