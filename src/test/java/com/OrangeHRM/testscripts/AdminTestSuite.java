package com.OrangeHRM.testscripts;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.OrangeHRM.PageItem.OrangeHRMData.EnterEmpFullName;
import com.OrangeHRM.PageItem.OrangeHRMData.UserDetails;
import com.OrangeHRM.PageItem.OrangeHRMDataEngine;
import com.OrangeHRM.pages.AdminPage;
import com.OrangeHRM.pages.DashboardPage;
import com.OrangeHRM.pages.LoginPage;
import com.OrangeHRM.pages.PimPage;
import com.OrangeHRM.teststeps.admin.AdminConstants.AddUser;
import com.OrangeHRM.teststeps.employee.DashboardConstants;
import com.OrangeHRM.utils.EnvironmentPropertiesReader;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.webdriverManager.DriverManager;


public class AdminTestSuite {

	String webSite, browser;
	private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();
	static ArrayList<String> employeeJobDetails = new ArrayList<String>();; 

	@BeforeTest(alwaysRun = true)
	public void init(ITestContext context) {
		webSite = (System.getProperty("webSite") != null ? System.getProperty("webSite"): context.getCurrentXmlTest().getParameter("webSite"));
		DriverManager.setHRMWebsite(webSite);	

		browser = (configProperty.getProperty("browserName")) != null ? (configProperty.getProperty("browserName")) : context.getCurrentXmlTest().getParameter("browserName");
	}


	/**
	 * Test Description: Create a System User(Admin)
	 * Test ID: TC101
	 * @author sathish.suresh
	 * @throws Exception 
	 */
	@Test(description = "Create a System User(Admin)")
	public void TC101() throws Exception {

		DriverManager.setDriver(browser);
		WebDriver driver = DriverManager.getDriver();
		Log.testCaseInfo("Create a System User(Admin)", driver);
		OrangeHRMDataEngine testData = new OrangeHRMDataEngine("OrangeHRM_TestData.xlsx", "OrangeHRMData", "TC101");

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

			pimPage.clickSavBtn(driver);

			pimPage.verifyToastMessage(driver);

			AdminPage adminPage = (AdminPage) dashboardPage.mainMenuSelection(driver, DashboardConstants.menuName.ADMIN);

			adminPage.clickAddUser(driver);

			adminPage.selectUserRole(driver, AddUser.USER_ROLE, UserDetails.builder()
					.userRole(testData.getUserRole())
					.build());

			adminPage.enterEmployeeName(driver, EnterEmpFullName.builder()
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.build());

			adminPage.selectStatus(driver, AddUser.STATUS, UserDetails.builder()
					.status(testData.getStatus())
					.build());

			adminPage.enterUserName(driver, AddUser.USERNAME, UserDetails.builder()
					.username(testData.getUsername())
					.build());

			adminPage.enterPassword(driver, AddUser.PASSWORD, UserDetails.builder()
					.password(testData.getPassword())
					.build());

			adminPage.enterConfirmPassword(driver, AddUser.CONFIRM_PASSWORD, UserDetails.builder()
					.password(testData.getPassword())
					.build());

			adminPage.clickSavBtn(driver);

			adminPage.verifyToastMessage(driver);
			
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

	/**
	 * Test Description: Verify and Delete user(Admin)
	 * Test ID: TC102
	 * @author sathish.suresh
	 * @throws Exception 
	 */
	@Test(description = "Verify and Delete user(Admin)")
	public void TC102() throws Exception {

		DriverManager.setDriver(browser);
		WebDriver driver = DriverManager.getDriver();
		Log.testCaseInfo("Verify and Delete user(Admin)", driver);
		OrangeHRMDataEngine testData = new OrangeHRMDataEngine("OrangeHRM_TestData.xlsx", "OrangeHRMData", "TC102");

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

			pimPage.clickSavBtn(driver);

			pimPage.verifyToastMessage(driver);

			AdminPage adminPage = (AdminPage) dashboardPage.mainMenuSelection(driver, DashboardConstants.menuName.ADMIN);

			adminPage.clickAddUser(driver);

			adminPage.createUser(driver,  UserDetails.builder()
					.userRole(testData.getUserRole())
					.username(testData.getUsername())
					.password(testData.getPassword())
					.status(testData.getStatus())
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.build());

			adminPage.clickSavBtn(driver);

			adminPage.verifyToastMessage(driver);

			adminPage = (AdminPage) dashboardPage.mainMenuSelection(driver, DashboardConstants.menuName.ADMIN);

			adminPage.enterUserName(driver, AddUser.USERNAME, UserDetails.builder()
					.username(testData.getUsername())
					.build());

			adminPage.clickSearchBtn(driver);

			adminPage.deleteUser(driver, UserDetails.builder()
					.username(testData.getUsername())
					.build());

			adminPage.verifyToastMessage(driver);
			
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
