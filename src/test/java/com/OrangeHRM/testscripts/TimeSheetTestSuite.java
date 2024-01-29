package com.OrangeHRM.testscripts;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.OrangeHRM.PageItem.OrangeHRMData.EnterEmpFullName;
import com.OrangeHRM.PageItem.OrangeHRMData.UserDetails;
import com.OrangeHRM.PageItem.OrangeHRMDataEngine;
import com.OrangeHRM.pages.DashboardPage;
import com.OrangeHRM.pages.LoginPage;
import com.OrangeHRM.pages.PimPage;
import com.OrangeHRM.pages.TimePage;
import com.OrangeHRM.teststeps.admin.AdminConstants.AddUser;
import com.OrangeHRM.teststeps.employee.DashboardConstants;
import com.OrangeHRM.utils.EnvironmentPropertiesReader;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.webdriverManager.DriverManager;


public class TimeSheetTestSuite {

	String webSite, browser;
	private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();
	ArrayList<String> timeSheetDetails = new ArrayList<String>();; 

	protected static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();
	
	@BeforeTest(alwaysRun = true)
	public void init(ITestContext context) {
		webSite = (System.getProperty("webSite") != null ? System.getProperty("webSite"): context.getCurrentXmlTest().getParameter("webSite"));
		//DriverManager.setWebsite(webSite);	

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
			LoginPage loginPage = new LoginPage(driver, webSite);
			DashboardPage dashboardPage = loginPage.LoginToApplication(driver, "Admin", "admin123");

			PimPage pimPage = (PimPage) dashboardPage.mainMenuSelection(driver, DashboardConstants.menuName.PIM);

			pimPage.clickAddEmployee(driver);

			pimPage.addEmployeeName(driver, EnterEmpFullName.builder()
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.build());

			pimPage.enterEmployeeId(driver);

			pimPage.clickOncreateLogin(driver);

			pimPage.enterUserName(driver, AddUser.USERNAME, UserDetails.builder()
					.username(testData.getUsername())
					.build());

			pimPage.enterPassword(driver, AddUser.PASSWORD, UserDetails.builder()
					.password(testData.getPassword())
					.build());

			pimPage.enterConfirmPassword(driver, AddUser.CONFIRM_PASSWORD, UserDetails.builder()
					.password(testData.getPassword())
					.build());

			pimPage.clickSavBtn(driver);

			pimPage.verifyToastMessage(driver);

			dashboardPage.clickOnUserProfile(driver);

			loginPage = dashboardPage.logOut(driver);

			dashboardPage = loginPage.LoginToApplication(driver, testData.getUsername(), testData.getPassword());

			TimePage timePage = (TimePage) dashboardPage.mainMenuSelection(driver, DashboardConstants.menuName.TIME);

			timePage.submitTimesheet(driver);

			timePage.verifyToastMessage(driver);

			timePage.verifyTimesheetIsSubmited(driver);

			timePage.validateTimesheetDetails(driver, testData.getFirstName()+" "+testData.getLastName());
			
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
