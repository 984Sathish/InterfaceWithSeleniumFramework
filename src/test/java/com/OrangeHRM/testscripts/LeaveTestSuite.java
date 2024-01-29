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
import com.OrangeHRM.pages.DashboardPage;
import com.OrangeHRM.pages.LeavePage;
import com.OrangeHRM.pages.LoginPage;
import com.OrangeHRM.pages.PimPage;
import com.OrangeHRM.teststeps.Leave.LeaveConstants;
import com.OrangeHRM.teststeps.Leave.LeaveConstants.AssignLeave;
import com.OrangeHRM.teststeps.admin.AdminConstants.AddUser;
import com.OrangeHRM.teststeps.employee.DashboardConstants;
import com.OrangeHRM.utils.EnvironmentPropertiesReader;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.webdriverManager.DriverManager;


public class LeaveTestSuite {

	String webSite, browser;
	private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();
	ArrayList<String> leaveDetails = new ArrayList<String>();; 

	@BeforeTest(alwaysRun = true)
	public void init(ITestContext context) {
		webSite = (System.getProperty("webSite") != null ? System.getProperty("webSite"): context.getCurrentXmlTest().getParameter("webSite"));
		//DriverManager.setWebsite(webSite);	

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

			LeavePage leavePage = (LeavePage) dashboardPage.mainMenuSelection(driver, DashboardConstants.menuName.LEAVE);

			leaveDetails.add(testData.getFirstName() + " "+ testData.getLastName());

			leavePage.selectTopNavMenu(driver, LeaveConstants.NavName.ASSIGN_LEAVE);

			leavePage.enterEmployeeName(driver, EnterEmpFullName.builder()
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.build());

			leaveDetails.add(testData.getLeaveType());

			leavePage.selectLeaveType(driver, AssignLeave.LEAVE_TYPE, LeaveDetails.builder()
					.leaveType(testData.getLeaveType())
					.build());

			leavePage.enterDate(driver, AssignLeave.FROM_DATE);

			String selectedDate = leavePage.clickWorkingDayInCalender(driver);

			leavePage.enterDate(driver, AssignLeave.TO_DATE);

			leavePage.clickAssignBtn(driver);

			leavePage.clickOkBtn(driver);

			leavePage.verifyToastMessage(driver);

			dashboardPage.clickOnUserProfile(driver);

			loginPage = dashboardPage.logOut(driver);

			dashboardPage = loginPage.LoginToApplication(driver, testData.getUsername(), testData.getPassword());

			leavePage = (LeavePage) dashboardPage.mainMenuSelection(driver, DashboardConstants.menuName.LEAVE);

			leavePage.enterDate(driver, AssignLeave.FROM_DATE);

			leavePage.clickWorkingDayInCalender(driver, selectedDate);

			leavePage.enterDate(driver, AssignLeave.TO_DATE);

			leavePage.searchMyLeave(driver);

			leavePage.validateLeaveDetails(driver, leaveDetails);
			
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
