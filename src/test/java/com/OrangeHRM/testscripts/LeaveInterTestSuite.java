package com.OrangeHRM.testscripts;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.OrangeHRM.PageItem.OrangeHRMData.EnterEmpFullName;
import com.OrangeHRM.PageItem.OrangeHRMData.LeaveDetails;
import com.OrangeHRM.PageItem.OrangeHRMData.UserDetails;
import com.OrangeHRM.PageItem.OrangeHRMDataEngine;
import com.OrangeHRM.teststeps.Leave.LeaveConstants;
import com.OrangeHRM.teststeps.Leave.LeaveSteps;
import com.OrangeHRM.teststeps.Leave.LeaveStepsImpl;
import com.OrangeHRM.teststeps.employee.DashboardConstants;
import com.OrangeHRM.utils.EnvironmentPropertiesReader;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.webdriverManager.DriverManager;


public class LeaveInterTestSuite {

	String hRMWebSite, browser;

	private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();

	ArrayList<String> leaveDetails = new ArrayList<String>();

	protected static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

	@BeforeTest(alwaysRun = true)
	public void init(ITestContext context) {

		hRMWebSite = (System.getProperty("hRMWebSite") != null ? System.getProperty("webSite"): context.getCurrentXmlTest().getParameter("hRMWebSite"));
		
		DriverManager.setHRMWebsite(hRMWebSite);	

		browser = (configProperty.getProperty("browserName")) != null ? (configProperty.getProperty("browserName")) : context.getCurrentXmlTest().getParameter("browserName");
	}


	/**
	 * Test Description: Apply Leave and verify leave deatails
	 * Test ID: TC201
	 * @author sathish.suresh
	 * @throws Exception 
	 */
	@Test(description = "Apply Leave and verify leave deatails")
	public void TC201() throws Exception {

		DriverManager.setDriver(browser);
		WebDriver driver = DriverManager.getDriver();
		Log.testCaseInfo("Apply Leave and verify leave deatails", driver);
		OrangeHRMDataEngine testData = new OrangeHRMDataEngine("OrangeHRM_TestData.xlsx", "OrangeHRMData", "TC201");

		try {

			LeaveStepsImpl leave = LeaveSteps.create();

			leave.loginToApplication("Admin", "admin123");

			leave.mainMenuSelection(DashboardConstants.menuName.PIM);

			leave.createEmployee(EnterEmpFullName.builder()
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.build());

			leave.addLoginCredentials(UserDetails.builder()
					.username(testData.getUsername())
					.password(testData.getPassword())
					.build());

			leave.saveEmployeeAndVerify();

			leave.mainMenuSelection(DashboardConstants.menuName.LEAVE);

			leave.subNavMenuSelection(LeaveConstants.NavName.ASSIGN_LEAVE);

			String leaveDate = leave.assignLeaveToEmployee(EnterEmpFullName.builder()
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.build(), LeaveDetails.builder()
					.leaveType(testData.getLeaveType())
					.build());

			leave.logoutToApplication();

			leave.loginToApplication(testData.getUsername(), testData.getPassword());

			leave.mainMenuSelection(DashboardConstants.menuName.LEAVE);

			leave.searchMyLeave(leaveDate);

			//Adding the Employee Leave information to the list
			leaveDetails.add(testData.getFirstName() + " "+ testData.getLastName());
			leaveDetails.add(testData.getLeaveType());

			leave.verifyLeaveTakenDetails(leaveDetails);

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
