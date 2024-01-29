package com.OrangeHRM.testscripts;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.OrangeHRM.PageItem.OrangeHRMData.EnterEmpFullName;
import com.OrangeHRM.PageItem.OrangeHRMData.UserDetails;
import com.OrangeHRM.PageItem.OrangeHRMDataEngine;
import com.OrangeHRM.teststeps.admin.AdminSteps;
import com.OrangeHRM.teststeps.admin.AdminStepsImpl;
import com.OrangeHRM.teststeps.employee.DashboardConstants;
import com.OrangeHRM.utils.EnvironmentPropertiesReader;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.webdriverManager.DriverManager;


public class AdminInterTestSuite {

	String hRMWebSite, browser;
	
	private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();

	static ArrayList<String> employeeJobDetails = new ArrayList<String>();

	protected static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

	@BeforeTest(alwaysRun = true)
	public void init(ITestContext context) {

		hRMWebSite = (System.getProperty("hRMWebSite") != null ? System.getProperty("webSite"): context.getCurrentXmlTest().getParameter("hRMWebSite"));
		DriverManager.setHRMWebsite(hRMWebSite);	

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

			AdminStepsImpl admin = AdminSteps.create();

			admin.loginToApplication("Admin", "admin123");

			admin.mainMenuSelection(DashboardConstants.menuName.PIM);

			admin.createEmployee(EnterEmpFullName.builder()
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.build());

			admin.mainMenuSelection(DashboardConstants.menuName.ADMIN);

			admin.CreateAdmin(UserDetails.builder()
					.userRole(testData.getUserRole())
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.status(testData.getStatus())
					.username(testData.getUsername())
					.password(testData.getPassword())
					.build());

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

			AdminStepsImpl admin = AdminSteps.create();

			admin.loginToApplication("Admin", "admin123");

			admin.mainMenuSelection(DashboardConstants.menuName.PIM);

			admin.createEmployee(EnterEmpFullName.builder()
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.build());

			admin.mainMenuSelection(DashboardConstants.menuName.ADMIN);

			admin.CreateAdmin(UserDetails.builder()
					.userRole(testData.getUserRole())
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.status(testData.getStatus())
					.username(testData.getUsername())
					.password(testData.getPassword())
					.build());
			
			admin.searchAdmin(UserDetails.builder()
					.username(testData.getUsername())
					.build());

			admin.deleteAdmin(UserDetails.builder()
					.username(testData.getUsername())
					.build());

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
