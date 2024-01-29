package com.OrangeHRM.pages;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.simple.JSONObject;
import org.openqa.selenium.By;
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
import com.OrangeHRM.webdriverManager.DriverManager;

import ASPIREAI.customfactories.AjaxElementLocatorFactory;
import ASPIREAI.customfactories.IFindBy;
import ASPIREAI.customfactories.PageFactory;

public class TimePage extends LoadableComponent<TimePage>{

	WebDriver driver;
	private boolean isPageLoaded;
	public ElementLayer elementLayer;
	public static List<Object> pageFactoryKey = new ArrayList<Object>();
	public static List<String> pageFactoryValue = new ArrayList<String>();


	@IFindBy(how = How.XPATH, using = "//h6[text()='My Timesheet']", AI = false)
	public WebElement lblMyTimeSheet;

	@IFindBy(how = How.CSS, using = "i[class='oxd-icon bi-calendar oxd-date-input-icon']", AI = false)
	public WebElement fldTimeSheetPeriod;

	@IFindBy(how = How.XPATH, using = "//button[text()=' Create Timesheet ']", AI = false)
	public WebElement btnCreateTimesheet;

	@IFindBy(how = How.XPATH, using = "//button[text()=' Submit ']", AI = false)
	public WebElement btnSubmit;

	@IFindBy(how = How.XPATH, using = "//p[text()='Status: Submitted']", AI = false)
	public WebElement txtSubmitted;

	@IFindBy(how = How.CSS, using = "div[class*='oxd-toast--success']", AI = false)
	public WebElement toastMsgSuccess;


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
		if(isPageLoaded && !(BrowserActions.waitForElementToDisplay(driver, lblMyTimeSheet))) {
			Log.fail("Page did not open up. Site might be down.", driver);
		}
		elementLayer = new ElementLayer(driver);	
	}

	public TimePage() {

	}

	public TimePage(WebDriver driver) {
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, 10);
		PageFactory.initElements(finder, this);
		elementLayer = new ElementLayer(driver);
	}

	public TimePage(WebDriver driver,int timeout){
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, timeout);
		PageFactory.initElements(finder, this);
		elementLayer = new ElementLayer(driver);
	}

	/**
	 * Submit timesheet
	 * @author sathish.suresh
	 * @param driver
	 * @return TimePage
	 */
	public TimePage submitTimesheet(WebDriver driver) {
		BrowserActions.click(driver, btnSubmit, "Submit button");
		return this;
	}

	/**
	 * Create Timesheet
	 * @author sathish.suresh
	 * @param driver
	 * @return TimePage
	 */
	public TimePage CreateTimesheet(WebDriver driver) {
		BrowserActions.click(driver, btnCreateTimesheet, "Create timesheet button");
		return this;
	}

	/**
	 * Verify timesheet is submitted 
	 * @author sathish.suresh
	 * @param driver
	 * @return TimePage
	 */
	public TimePage verifyTimesheetIsSubmited(WebDriver driver) {
		BrowserActions.verifyElementDisplayed(driver, txtSubmitted, "Submitted Label");
		return this;
	}

	/**
	 * Select timesheet period in calender
	 * @author sathish.suresh
	 * @param driver
	 * @return TimePage
	 */
	public TimePage selectTimesheetPeriod(WebDriver driver) {
		BrowserActions.click(driver, fldTimeSheetPeriod, "Timesheet Period");
		LeavePage leavePage = new LeavePage(driver);
		leavePage.clickWorkingDayInCalender(driver);
		return this;
	}

	/**
	 * Verify success message is displayed
	 * @author sathish.suresh
	 * @param driver
	 * @return TimePage
	 */
	public TimePage verifyToastMessage(WebDriver driver) {
		BrowserActions.waitForElementToDisplay(driver, toastMsgSuccess);
		return this;
	}

	/**
	 * Validate timesheet details
	 * @author sathish.suresh
	 * @param driver
	 * @param userName
	 * @return TimePage
	 */
	public TimePage validateTimesheetDetails(WebDriver driver, String userName) {
		ArrayList<String> actList = new ArrayList<String>();
		By row = By.cssSelector("div[class='oxd-table-card']");
		BrowserActions.waitForElementToDisplay(driver, row, "Table rows");
		int rowSize = BrowserActions.getWebElementSize(driver, row, "Table rows");
		if(rowSize == 1){
			List<WebElement> columns = driver.findElements(By.cssSelector("div[class='oxd-table-cell oxd-padding-cell'] div"));
			for (WebElement col : columns) {
				if(!(col.getText().equals(""))  && col.getText().length() != 0) {
					String colText = BrowserActions.getText(driver, col, "Table field");
					actList.add(colText);
				}
			}
			writeJsonFile(actList);
			Collections.sort(actList);
			boolean equal = actList.contains(userName);
			Log.assertThat(equal, "Timesheet Details is verified successfully", "Failed to verify Timesheet details", DriverManager.getDriver());
		}
		return this;
	}


	/**
	 * Write json file to add timesheet information
	 * @author sathish.suresh
	 * @param timesheetInfo
	 */
	@SuppressWarnings("unchecked")
	public void writeJsonFile(ArrayList<String> timesheetInfo) {
		try {
			File file=new File("JsonTestDate\\TimesheetDetails.json");  
			file.createNewFile(); 
			FileWriter fileWriter = new FileWriter(file); 
			System.out.println("Writing JSON object to file");  
			System.out.println("-----------------------"); 
			JSONObject jsonTimesheetInfo = new JSONObject();  
			for (int i = 0; i < timesheetInfo.size(); i++) {

				switch (i) {
				case 0:
					jsonTimesheetInfo.put("Status", timesheetInfo.get(i));
				case 1:
					jsonTimesheetInfo.put("Performed By", timesheetInfo.get(i));
				case 2:
					jsonTimesheetInfo.put("Timesheet Date", timesheetInfo.get(i));
				default:
					break;
				}

			}

			fileWriter.write(jsonTimesheetInfo.toJSONString());
			fileWriter.flush();  
			fileWriter.close(); 
		}
		catch (Exception e) {
			e.printStackTrace();  
		}
	}


}

