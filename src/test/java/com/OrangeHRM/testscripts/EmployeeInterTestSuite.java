package com.OrangeHRM.testscripts;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.OrangeHRM.PageItem.OrangeHRMData.EmployeeContactDetails;
import com.OrangeHRM.PageItem.OrangeHRMData.EmployeeJobDetails;
import com.OrangeHRM.PageItem.OrangeHRMData.EmployeePersonalDetails;
import com.OrangeHRM.PageItem.OrangeHRMData.EmployeeReportTo;
import com.OrangeHRM.PageItem.OrangeHRMData.EnterEmpFullName;
import com.OrangeHRM.PageItem.OrangeHRMDataEngine;
import com.OrangeHRM.teststeps.employee.DashboardConstants;
import com.OrangeHRM.teststeps.employee.EmployeeSteps;
import com.OrangeHRM.teststeps.employee.EmployeeStepsImpl;
import com.OrangeHRM.teststeps.employee.PimConstants;
import com.OrangeHRM.teststeps.employee.PimConstants.PIMTabs;
import com.OrangeHRM.utils.EnvironmentPropertiesReader;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.webdriverManager.DriverManager;

public class EmployeeInterTestSuite {

	String hRMWebSite, browser;

	private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();

	static ArrayList<String> employeeDetails = new ArrayList<String>();

	protected static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

	@BeforeTest(alwaysRun = true)
	public void init(ITestContext context) {

		hRMWebSite = (System.getProperty("hRMWebSite") != null ? System.getProperty("webSite"): context.getCurrentXmlTest().getParameter("hRMWebSite"));
		
		DriverManager.setHRMWebsite(hRMWebSite);	

		browser = (configProperty.getProperty("browserName")) != null ? (configProperty.getProperty("browserName")) : context.getCurrentXmlTest().getParameter("browserName");
	}


	/**
	 * Test Description: Add report details and verify Employee Table
	 * Test ID: TC007
	 * @author sathish.suresh
	 * @throws Exception 
	 */
	@Test(description = "Add report details and verify Employee Table")
	public void TC007() throws Exception {

		DriverManager.setDriver(browser);
		WebDriver driver = DriverManager.getDriver();
		Log.testCaseInfo("Add report details and verify Employee Table", driver);
		OrangeHRMDataEngine testData = new OrangeHRMDataEngine("OrangeHRM_TestData.xlsx", "OrangeHRMData", "TC007");

		try {
			
			EmployeeStepsImpl employeeInfo = EmployeeSteps.create();
			
			employeeInfo.loginToApplication("Admin", "admin123");
			
			employeeInfo.mainMenuSelection(DashboardConstants.menuName.PIM);
			
			String firstName1 = testData.setFirstName();
			String lastName1 = testData.setLastName();
			
			employeeInfo.createEmployee(EnterEmpFullName.builder()
					.firstName(firstName1)
					.lastName(lastName1)
					.build());
			
			employeeInfo.mainMenuSelection(DashboardConstants.menuName.PIM);
			
			String firstName2 = testData.setFirstName();
			String lastName2 = testData.setLastName();
			
			String employeeId = employeeInfo.createEmployee(EnterEmpFullName.builder()
					.firstName(firstName2)
					.lastName(lastName2)
					.build());
			
			employeeInfo.subNavMenuSelection(PimConstants.NavName.EMPLOYEE_LIST);
			
			employeeInfo.searchEmployee(EnterEmpFullName.builder()
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.build(), employeeId);
			
			employeeInfo.verifyEmployeeAndClkEmp();
			
			employeeInfo.addEmpPersonalAndCustomDetails(EmployeePersonalDetails.builder()
					.nationality(testData.getNationality())
					.maritalStatus(testData.getMaritalStatus())
					.currentDate(testData.getCurrentDate())
					.gender(testData.getGender())
					.bloodType(testData.getBloodType())
					.build());
			
			employeeInfo.addEmpContactDetails(PIMTabs.CONTACT_DETAILS, EmployeeContactDetails.builder()
					.street(testData.getStreet())
					.city(testData.getCity())
					.state(testData.getState())
					.zipCode(testData.getZipCode())
					.country(testData.getCountry())
					.mobile(testData.getMobile())
					.email(testData.getEmail())
					.build());
			
			ArrayList<String> jobDetails = employeeInfo.addEmpJobDetails(PIMTabs.JOB, EmployeeJobDetails.builder()
					.currentDate(testData.getCurrentDate())
					.build());
			
			String supervisiorName = employeeInfo.reportToSupervisior(PIMTabs.REPORT_TO, firstName1, lastName1, EmployeeReportTo.builder()
					.reportMethod(testData.getReportMethod())
					.build());
			
			employeeInfo.subNavMenuSelection(PimConstants.NavName.EMPLOYEE_LIST);

			employeeInfo.searchEmployee(EnterEmpFullName.builder()
					.firstName(firstName2)
					.lastName(lastName2)
					.build(), employeeId);
			
			//Adding employee job and report details
			employeeDetails.addAll(jobDetails);
			employeeDetails.add(supervisiorName);
			employeeDetails.add(testData.getFirstName());
			employeeDetails.add(testData.getLastName());
			employeeDetails.add(employeeId);
			
			employeeInfo.validateEmpAndDelete(employeeDetails, employeeId);

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
