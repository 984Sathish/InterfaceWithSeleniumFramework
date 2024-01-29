package com.OrangeHRM.testscripts;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.OrangeHRM.PageItem.OrangeHRMDataEngine;
import com.OrangeHRM.PageItem.OrangeHRMData.EnterEmpFullName;
import com.OrangeHRM.PageItem.OrangeHRMData.UserDetails;
import com.OrangeHRM.teststeps.employee.DashboardConstants;
import com.OrangeHRM.teststeps.timesheet.TimesheetSteps;
import com.OrangeHRM.teststeps.timesheet.TimesheetStepsImpl;
import com.OrangeHRM.utils.EnvironmentPropertiesReader;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.webdriverManager.DriverManager;


public class TimeSheetInterTestSuite {

	String hRMWebSite, browser;

	private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();
	
	protected static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();
	
	@BeforeTest(alwaysRun = true)
	public void init(ITestContext context) {
		
		hRMWebSite = (System.getProperty("hRMWebSite") != null ? System.getProperty("webSite"): context.getCurrentXmlTest().getParameter("hRMWebSite"));
		DriverManager.setHRMWebsite(hRMWebSite);	

		browser = (configProperty.getProperty("browserName")) != null ? (configProperty.getProperty("browserName")) : context.getCurrentXmlTest().getParameter("browserName");
	}

	/**
	 * Test Description: create timesheet and submit
	 * Test ID: TC301
	 * @author sathish.suresh
	 * @throws Exception 
	 */
	@Test(description = "create timesheet and submit")
	public void TC301() throws Exception {

		DriverManager.setDriver(browser);
		WebDriver driver = DriverManager.getDriver();
		Log.testCaseInfo("create timesheet and submit", driver);
		OrangeHRMDataEngine testData = new OrangeHRMDataEngine("OrangeHRM_TestData.xlsx", "OrangeHRMData", "TC301");

		try {
			
			TimesheetStepsImpl timesheet = TimesheetSteps.create();
			
			timesheet.loginToApplication("Admin", "admin123");

			timesheet.mainMenuSelection(DashboardConstants.menuName.PIM);

			timesheet.createEmployee(EnterEmpFullName.builder()
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.build());
			
			timesheet.addLoginCredentials(UserDetails.builder()
					.username(testData.getUsername())
					.password(testData.getPassword())
					.build());
			
			timesheet.saveEmployeeAndVerify();
			
			timesheet.logoutToApplication();
			
			timesheet.loginToApplication(testData.getUsername(), testData.getPassword());
			
			timesheet.mainMenuSelection(DashboardConstants.menuName.TIME);
			
			timesheet.submitTimesheetAndVerify();
			
			String empFullName = testData.getFirstName()+" "+testData.getLastName();
			
			timesheet.verifyTimesheetDetails(empFullName);
			
			Log.testCaseResult(driver);
		}

		catch (Exception e) {
			Log.exception(e, DriverManager.getDriver());
		}
		finally {
			DriverManager.quitDriver();
			Log.endTestCase();
		}

	}



}
