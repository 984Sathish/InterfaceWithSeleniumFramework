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
import com.OrangeHRM.pages.DashboardPage;
import com.OrangeHRM.pages.LoginPage;
import com.OrangeHRM.pages.MyInfoPage;
import com.OrangeHRM.pages.PimPage;
import com.OrangeHRM.teststeps.employee.DashboardConstants;
import com.OrangeHRM.teststeps.employee.PimConstants;
import com.OrangeHRM.teststeps.employee.EditEmployeeConstants.ContactDetails;
import com.OrangeHRM.teststeps.employee.EditEmployeeConstants.JobDetails;
import com.OrangeHRM.teststeps.employee.EditEmployeeConstants.PersonalDetails;
import com.OrangeHRM.teststeps.employee.EditEmployeeConstants.ReportTo;
import com.OrangeHRM.teststeps.employee.PimConstants.PIMTabs;
import com.OrangeHRM.utils.EnvironmentPropertiesReader;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.webdriverManager.DriverManager;


public class EmployeeTestSuite {

	String webSite, browser;
	private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();
	static ArrayList<String> employeeJobDetails = new ArrayList<String>();

	@BeforeTest(alwaysRun = true)
	public void init(ITestContext context) {
		webSite = (System.getProperty("webSite") != null ? System.getProperty("webSite"): context.getCurrentXmlTest().getParameter("webSite"));
		//DriverManager.setWebsite(webSite);	

		browser = (configProperty.getProperty("browserName")) != null ? (configProperty.getProperty("browserName")) : context.getCurrentXmlTest().getParameter("browserName");
	}

	/**
	 * Test Description: Login to OrangeHRM application
	 * Test ID: TC001
	 * @author sathish.suresh
	 * @throws Exception 
	 */
	@Test(description = "Login to OrangeHRM application")
	public void TC001() throws Exception {

		DriverManager.setDriver(browser);
		WebDriver driver = DriverManager.getDriver();
		Log.testCaseInfo("Login to OrangeHRM application", driver);
		OrangeHRMDataEngine testData = new OrangeHRMDataEngine("OrangeHRM_TestData.xlsx", "OrangeHRMData", "TC001");

		try {
			LoginPage loginPage = new LoginPage(driver, webSite);
			
			loginPage.LoginToApplication(driver, "Admin", "admin123");
			
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
	 * Test Description: Create new Employee and verify success message
	 * Test ID: TC002
	 * @author sathish.suresh
	 * @throws Exception 
	 */
	@Test(description = "Create new Employee and verify success message")
	public void TC002() throws Exception {

		DriverManager.setDriver(browser);
		WebDriver driver = DriverManager.getDriver();
		Log.testCaseInfo("Create new Employee and verify success message", driver);
		OrangeHRMDataEngine testData = new OrangeHRMDataEngine("OrangeHRM_TestData.xlsx", "OrangeHRMData", "TC002");

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

			String employeeId = pimPage.getEmployeeId(driver);

			pimPage.clickSavBtn(driver);

			pimPage.verifyToastMessage(driver);
			
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
	 * Test Description: Create new Employee and Verify In Employee Table
	 * Test ID: TC003
	 * @author sathish.suresh
	 * @throws Exception 
	 */
	@Test(description = "Create new Employee and Verify In Employee Table")
	public void TC003() throws Exception {

		DriverManager.setDriver(browser);
		WebDriver driver = DriverManager.getDriver();
		Log.testCaseInfo("Create new Employee and Verify In Employee Table", driver);
		OrangeHRMDataEngine testData = new OrangeHRMDataEngine("OrangeHRM_TestData.xlsx", "OrangeHRMData", "TC003");

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

			String employeeId = pimPage.getEmployeeId(driver);

			pimPage.clickSavBtn(driver);

			pimPage.verifyToastMessage(driver);

			pimPage.selectTopNavMenu(driver, PimConstants.NavName.EMPLOYEE_LIST);
			pimPage.enterEmployeeName(driver, EnterEmpFullName.builder()
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.build());

			pimPage.typeInTextFldByLabel(driver, PimConstants.EmployeeInfo.EMPLOYEE_ID, employeeId);

			pimPage.clickSearchBtn(driver);

			MyInfoPage myInfoPage = pimPage.validateEmployeeListAndClick(driver);
			
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
	 * Test Description: Add employee personal details
	 * Test ID: TC004
	 * @author sathish.suresh
	 * @throws Exception 
	 */
	@Test(description = "Add employee personal details")
	public void TC004() throws Exception {

		DriverManager.setDriver(browser);
		WebDriver driver = DriverManager.getDriver();
		Log.testCaseInfo("Add employee personal details", driver);
		OrangeHRMDataEngine testData = new OrangeHRMDataEngine("OrangeHRM_TestData.xlsx", "OrangeHRMData", "TC004");

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

			String employeeId = pimPage.getEmployeeId(driver);

			pimPage.clickSavBtn(driver);

			pimPage.verifyToastMessage(driver);

			pimPage.selectTopNavMenu(driver, PimConstants.NavName.EMPLOYEE_LIST);
			pimPage.enterEmployeeName(driver, EnterEmpFullName.builder()
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.build());

			pimPage.typeInTextFldByLabel(driver, PimConstants.EmployeeInfo.EMPLOYEE_ID, employeeId);

			pimPage.clickSearchBtn(driver);

			MyInfoPage myInfoPage = pimPage.validateEmployeeListAndClick(driver);

			myInfoPage.selectNationality(driver, PersonalDetails.NATIONALITY, EmployeePersonalDetails.builder()
					.nationality(testData.getNationality())
					.build());

			myInfoPage.selectMaritalStatus(driver, PersonalDetails.MARITAL_STATUS, EmployeePersonalDetails.builder()
					.maritalStatus(testData.getMaritalStatus())
					.build());

			myInfoPage.enterEmployeeDOB(driver, PersonalDetails.DOB, EmployeePersonalDetails.builder()
					.currentDate(testData.getCurrentDate())
					.build());

			myInfoPage.selectGender(driver, PersonalDetails.GENDER, EmployeePersonalDetails.builder()
					.gender(testData.getGender())
					.build());

			myInfoPage.SavePersonalDetails(driver);

			myInfoPage.verifyToastMessage(driver);

			myInfoPage.selectBloodType(driver, PersonalDetails.BLOOD_TYPE, EmployeePersonalDetails.builder()
					.bloodType(testData.getBloodType())
					.build());

			myInfoPage.SaveEmpCustomField(driver);

			myInfoPage.verifyToastMessage(driver);
			
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
	 * Test Description: Add employee contact details
	 * Test ID: TC005
	 * @author sathish.suresh
	 * @throws Exception 
	 */
	@Test(description = "Add employee contact details")
	public void TC005() throws Exception {

		DriverManager.setDriver(browser);
		WebDriver driver = DriverManager.getDriver();
		Log.testCaseInfo("Add employee contact details", driver);
		OrangeHRMDataEngine testData = new OrangeHRMDataEngine("OrangeHRM_TestData.xlsx", "OrangeHRMData", "TC005");

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

			String employeeId = pimPage.getEmployeeId(driver);

			pimPage.clickSavBtn(driver);

			pimPage.verifyToastMessage(driver);

			pimPage.selectTopNavMenu(driver, PimConstants.NavName.EMPLOYEE_LIST);

			pimPage.enterEmployeeName(driver, EnterEmpFullName.builder()
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.build());

			pimPage.typeInTextFldByLabel(driver, PimConstants.EmployeeInfo.EMPLOYEE_ID, employeeId);

			pimPage.clickSearchBtn(driver);

			MyInfoPage myInfoPage = pimPage.validateEmployeeListAndClick(driver);

			myInfoPage.AddEmployeePersonalDetails(driver, EmployeePersonalDetails.builder()
					.nationality(testData.getNationality())
					.maritalStatus(testData.getMaritalStatus())
					.currentDate(testData.getCurrentDate())
					.gender(testData.getGender())
					.build());

			myInfoPage.SavePersonalDetails(driver);

			myInfoPage.verifyToastMessage(driver);

			myInfoPage.addEmployeeCustomFields(driver, EmployeePersonalDetails.builder()
					.bloodType(testData.getBloodType())
					.build());

			myInfoPage.SaveEmpCustomField(driver);

			pimPage = myInfoPage.verifyToastMessage(driver);

			myInfoPage = pimPage.clickPIMTab(driver, PIMTabs.CONTACT_DETAILS);

			myInfoPage.enterStreet1(driver, ContactDetails.STREET_1, EmployeeContactDetails.builder()
					.street(testData.getStreet())
					.build());

			myInfoPage.enterCity(driver, ContactDetails.CITY, EmployeeContactDetails.builder()
					.city(testData.getCity())
					.build());

			myInfoPage.enterState(driver, ContactDetails.STATE, EmployeeContactDetails.builder()
					.state(testData.getState())
					.build());

			myInfoPage.enterZipCode(driver, ContactDetails.ZIP_CODE, EmployeeContactDetails.builder()
					.zipCode(testData.getZipCode())
					.build());

			myInfoPage.selectCountry(driver, ContactDetails.COUNTRY, EmployeeContactDetails.builder()
					.country(testData.getCountry())
					.build());

			myInfoPage.enterMobileNumber(driver, ContactDetails.MOBILE, EmployeeContactDetails.builder()
					.mobile(testData.getMobile())
					.build());

			myInfoPage.enterEmailAddress(driver, ContactDetails.WORK_EMAIL, EmployeeContactDetails.builder()
					.email(testData.getEmail())
					.build());

			myInfoPage.SaveEmpContactDetails(driver);

			myInfoPage.verifyToastMessage(driver);
			
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
	 * Test Description: Add employee job details
	 * Test ID: TC00
	 * @author sathish.suresh
	 * @throws Exception 
	 */
	@Test(description = "Add employee job details")
	public void TC006() throws Exception {

		DriverManager.setDriver(browser);
		WebDriver driver = DriverManager.getDriver();
		Log.testCaseInfo("Add employee job details", driver);
		OrangeHRMDataEngine testData = new OrangeHRMDataEngine("OrangeHRM_TestData.xlsx", "OrangeHRMData", "TC006");

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

			String employeeId = pimPage.getEmployeeId(driver);

			pimPage.clickSavBtn(driver);

			pimPage.verifyToastMessage(driver);

			pimPage.selectTopNavMenu(driver, PimConstants.NavName.EMPLOYEE_LIST);

			pimPage.enterEmployeeName(driver, EnterEmpFullName.builder()
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.build());

			pimPage.typeInTextFldByLabel(driver, PimConstants.EmployeeInfo.EMPLOYEE_ID, employeeId);

			pimPage.clickSearchBtn(driver);

			MyInfoPage myInfoPage = pimPage.validateEmployeeListAndClick(driver);

			myInfoPage.AddEmployeePersonalDetails(driver, EmployeePersonalDetails.builder()
					.nationality(testData.getNationality())
					.maritalStatus(testData.getMaritalStatus())
					.currentDate(testData.getCurrentDate())
					.gender(testData.getGender())
					.build());

			myInfoPage.SavePersonalDetails(driver);

			myInfoPage.verifyToastMessage(driver);

			myInfoPage.addEmployeeCustomFields(driver, EmployeePersonalDetails.builder()
					.bloodType(testData.getBloodType())
					.build());

			myInfoPage.SaveEmpCustomField(driver);

			pimPage = myInfoPage.verifyToastMessage(driver);

			myInfoPage = pimPage.clickPIMTab(driver, PIMTabs.CONTACT_DETAILS);

			myInfoPage.AddEmployeeContactDetails(driver, EmployeeContactDetails.builder()
					.street(testData.getStreet())
					.city(testData.getCity())
					.state(testData.getState())
					.zipCode(testData.getZipCode())
					.country(testData.getCountry())
					.mobile(testData.getMobile())
					.email(testData.getEmail())
					.build());	

			myInfoPage.SaveEmpContactDetails(driver);

			pimPage = myInfoPage.verifyToastMessage(driver);

			myInfoPage = pimPage.clickPIMTab(driver, PIMTabs.JOB);

			myInfoPage.enterJoinedDate(driver, JobDetails.JOINED_DATE, EmployeeJobDetails.builder()
					.currentDate(testData.getCurrentDate())
					.build());

			myInfoPage.selectJobDetails(driver, JobDetails.JOB_TITLE);

			myInfoPage.selectJobDetails(driver, JobDetails.JOB_CATEGORY);

			myInfoPage.selectJobDetails(driver, JobDetails.SUB_UNIT);

			myInfoPage.selectJobDetails(driver, JobDetails.LOCATION);

			myInfoPage.selectJobDetails(driver, JobDetails.EMPLOYMENT_STATUS);

			myInfoPage.SaveEmpJobDetails(driver);

			pimPage = myInfoPage.verifyToastMessage(driver);
			
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
			LoginPage loginPage = new LoginPage(driver, webSite);
			DashboardPage dashboardPage = loginPage.LoginToApplication(driver, "Admin", "admin123");

			PimPage pimPage = (PimPage) dashboardPage.mainMenuSelection(driver, DashboardConstants.menuName.PIM);

			pimPage.clickAddEmployee(driver);

			String firstName1 = testData.setFirstName();
			String lastName1 = testData.setLastName();

			pimPage.addEmployeeName(driver, EnterEmpFullName.builder()
					.firstName(firstName1)
					.lastName(lastName1)
					.build());

			pimPage.enterEmployeeId(driver);

			pimPage.clickSavBtn(driver);

			pimPage.verifyToastMessage(driver);

			String firstName2 = testData.setFirstName();
			String lastName2 = testData.setLastName();

			pimPage = (PimPage) dashboardPage.mainMenuSelection(driver, DashboardConstants.menuName.PIM);
			pimPage.clickAddEmployee(driver);
			pimPage.addEmployeeName(driver, EnterEmpFullName.builder()
					.firstName(firstName2)
					.lastName(lastName2)
					.build());

			pimPage.enterEmployeeId(driver);

			String employeeId = pimPage.getEmployeeId(driver);

			pimPage.clickSavBtn(driver);

			pimPage.verifyToastMessage(driver);

			pimPage.selectTopNavMenu(driver, PimConstants.NavName.EMPLOYEE_LIST);

			pimPage.enterEmployeeName(driver, EnterEmpFullName.builder()
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.build());

			pimPage.typeInTextFldByLabel(driver, PimConstants.EmployeeInfo.EMPLOYEE_ID, employeeId);

			pimPage.clickSearchBtn(driver);

			MyInfoPage myInfoPage = pimPage.validateEmployeeListAndClick(driver);

			myInfoPage.AddEmployeePersonalDetails(driver, EmployeePersonalDetails.builder()
					.nationality(testData.getNationality())
					.maritalStatus(testData.getMaritalStatus())
					.currentDate(testData.getCurrentDate())
					.gender(testData.getGender())
					.build());

			myInfoPage.SavePersonalDetails(driver);

			myInfoPage.verifyToastMessage(driver);

			myInfoPage.addEmployeeCustomFields(driver, EmployeePersonalDetails.builder()
					.bloodType(testData.getBloodType())
					.build());

			myInfoPage.SaveEmpCustomField(driver);

			pimPage = myInfoPage.verifyToastMessage(driver);

			myInfoPage = pimPage.clickPIMTab(driver, PIMTabs.CONTACT_DETAILS);

			myInfoPage.AddEmployeeContactDetails(driver, EmployeeContactDetails.builder()
					.street(testData.getStreet())
					.city(testData.getCity())
					.state(testData.getState())
					.zipCode(testData.getZipCode())
					.country(testData.getCountry())
					.mobile(testData.getMobile())
					.email(testData.getEmail())
					.build());	

			myInfoPage.SaveEmpContactDetails(driver);

			pimPage = myInfoPage.verifyToastMessage(driver);

			myInfoPage = pimPage.clickPIMTab(driver, PIMTabs.JOB);

			employeeJobDetails = myInfoPage.addEmployeeJobDetails(driver, EmployeeJobDetails.builder()
					.currentDate(testData.getCurrentDate())
					.build());

			myInfoPage.SaveEmpJobDetails(driver);

			pimPage = myInfoPage.verifyToastMessage(driver);

			myInfoPage = pimPage.clickPIMTab(driver, PIMTabs.REPORT_TO);

			myInfoPage.addSupervisorDetails(driver);

			String superVisiorName = myInfoPage.enterEmployeeName(driver, firstName1, lastName1);

			employeeJobDetails.add(superVisiorName);

			myInfoPage.selectReportingMethod(driver, ReportTo.REPORTING_METHOD, EmployeeReportTo.builder()
					.reportMethod(testData.getReportMethod())
					.build());

			myInfoPage.saveSupervisorDetails(driver);

			pimPage = myInfoPage.verifyToastMessage(driver);

			pimPage.selectTopNavMenu(driver, PimConstants.NavName.EMPLOYEE_LIST);

			employeeJobDetails.add(testData.getFirstName());
			employeeJobDetails.add(testData.getLastName());
			employeeJobDetails.add(employeeId);

			pimPage.enterEmployeeName(driver, EnterEmpFullName.builder()
					.firstName(firstName2)
					.lastName(lastName2)
					.build());

			pimPage.typeInTextFldByLabel(driver, PimConstants.EmployeeInfo.EMPLOYEE_ID, employeeId);

			pimPage.clickSearchBtn(driver);

			pimPage.validateEmployeeList(driver, employeeJobDetails);

			pimPage.deleteEmployee(driver, employeeId);

			pimPage.verifyToastMessage(driver);
			
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
