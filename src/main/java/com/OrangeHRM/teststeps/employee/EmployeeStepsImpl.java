package com.OrangeHRM.teststeps.employee;

import java.util.ArrayList;

import com.OrangeHRM.PageItem.OrangeHRMData.EmployeeContactDetails;
import com.OrangeHRM.PageItem.OrangeHRMData.EmployeeJobDetails;
import com.OrangeHRM.PageItem.OrangeHRMData.EmployeePersonalDetails;
import com.OrangeHRM.PageItem.OrangeHRMData.EmployeeReportTo;
import com.OrangeHRM.PageItem.OrangeHRMData.EnterEmpFullName;
import com.OrangeHRM.pages.DashboardPage;
import com.OrangeHRM.pages.LoginPage;
import com.OrangeHRM.pages.MyInfoPage;
import com.OrangeHRM.pages.PimPage;
import com.OrangeHRM.teststeps.employee.DashboardConstants.menuName;
import com.OrangeHRM.teststeps.employee.EditEmployeeConstants.ReportTo;
import com.OrangeHRM.teststeps.employee.PimConstants.NavName;
import com.OrangeHRM.teststeps.employee.PimConstants.PIMTabs;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.webdriverManager.DriverManager;

public class EmployeeStepsImpl implements EmployeeSteps{
	
	private static String webSite = DriverManager.getHRMWebsite();
	static ArrayList<String> employeeJobDetails = new ArrayList<String>();

	@Override
	public void loginToApplication(String username, String password) {
		LoginPage loginPage = new LoginPage(DriverManager.getDriver(), webSite);
		loginPage.enterUsername(DriverManager.getDriver(), username);
		loginPage.enterPassword(DriverManager.getDriver(), password);
		loginPage.clickLogin(DriverManager.getDriver());
		new DashboardPage(DriverManager.getDriver()).get();
		Log.messageStep("Logged into the OrangeHRM Application",DriverManager.getDriver());
	}

	@Override
	public void mainMenuSelection(menuName menu) {
		DashboardPage dashboardPage = new DashboardPage(DriverManager.getDriver());
		dashboardPage.mainMenuSelection(DriverManager.getDriver(), menu);
		Log.messageStep("Main menu: " + menu.getName()+ " selected" , DriverManager.getDriver());
	}

	@Override
	public String createEmployee(EnterEmpFullName enterEmpFullName) {
		PimPage pimPage = new PimPage(DriverManager.getDriver()).clickAddEmployee(DriverManager.getDriver());
		pimPage.addEmployeeName(DriverManager.getDriver(), enterEmpFullName);
		pimPage.enterEmployeeId(DriverManager.getDriver());
		String employeeId = pimPage.getEmployeeId(DriverManager.getDriver());
		pimPage.clickSavBtn(DriverManager.getDriver());
		pimPage.verifyToastMessage(DriverManager.getDriver());
		Log.messageStep("Created Employee with First Name: "+ enterEmpFullName.getFirstName() + " and Last Name: "+enterEmpFullName.getLastName() ,DriverManager.getDriver());
		return employeeId;
	}
	
	@Override
	public void subNavMenuSelection(NavName navMenu) {
		new PimPage(DriverManager.getDriver()).get().selectTopNavMenu(DriverManager.getDriver(), navMenu);
		Log.messageStep("SubNav menu: " + navMenu.getName()+ " selected" , DriverManager.getDriver());
	}

	@Override
	public void searchEmployee(EnterEmpFullName enterEmpFullName, String employeeId) {
		PimPage pimPage = new PimPage(DriverManager.getDriver()).enterEmployeeName(DriverManager.getDriver(), EnterEmpFullName.builder()
				.firstName(enterEmpFullName.getFirstName())
				.lastName(enterEmpFullName.getLastName())
				.build());
		pimPage.typeInTextFldByLabel(DriverManager.getDriver(), PimConstants.EmployeeInfo.EMPLOYEE_ID, employeeId);
		pimPage.clickSearchBtn(DriverManager.getDriver());
		Log.messageStep("Searched for Employee with Employee Id: "+employeeId, DriverManager.getDriver());
	}

	@Override
	public void verifyEmployeeAndClkEmp() {
		new PimPage(DriverManager.getDriver()).validateEmployeeListAndClick(DriverManager.getDriver());
	}

	@Override
	public void addEmpPersonalAndCustomDetails(EmployeePersonalDetails personalDetails) {
		MyInfoPage myInfoPage = new MyInfoPage(DriverManager.getDriver()).AddEmployeePersonalDetails(DriverManager.getDriver(), EmployeePersonalDetails.builder()
				.nationality(personalDetails.getNationality())
				.maritalStatus(personalDetails.getMaritalStatus())
				.currentDate(personalDetails.getCurrentDate())
				.gender(personalDetails.getGender())
				.build());
		myInfoPage.SavePersonalDetails(DriverManager.getDriver());
		myInfoPage.verifyToastMessage(DriverManager.getDriver());
		myInfoPage.addEmployeeCustomFields(DriverManager.getDriver(), EmployeePersonalDetails.builder()
				.bloodType(personalDetails.getBloodType())
				.build());
		myInfoPage.SaveEmpCustomField(DriverManager.getDriver());
		myInfoPage.verifyToastMessage(DriverManager.getDriver());	
		Log.messageStep("Added the Employee Personal and Custom Details", DriverManager.getDriver());
	}

	@Override
	public void addEmpContactDetails(PIMTabs tabName, EmployeeContactDetails contactDetails) {
		MyInfoPage myInfoPage = new PimPage(DriverManager.getDriver()).clickPIMTab(DriverManager.getDriver(), tabName);
		myInfoPage.AddEmployeeContactDetails(DriverManager.getDriver(), EmployeeContactDetails.builder()
				.street(contactDetails.getStreet())
				.city(contactDetails.getCity())
				.state(contactDetails.getState())
				.zipCode(contactDetails.getZipCode())
				.country(contactDetails.getCountry())
				.mobile(contactDetails.getMobile())
				.email(contactDetails.getEmail())
				.build());	
		myInfoPage.SaveEmpContactDetails(DriverManager.getDriver());
		myInfoPage.verifyToastMessage(DriverManager.getDriver());
		Log.messageStep("Added the Employee Contact Details", DriverManager.getDriver());
	}

	@Override
	public ArrayList<String> addEmpJobDetails(PIMTabs tabName, EmployeeJobDetails jobDetails) {
		MyInfoPage myInfoPage = new PimPage(DriverManager.getDriver()).clickPIMTab(DriverManager.getDriver(), tabName);
		employeeJobDetails = myInfoPage.addEmployeeJobDetails(DriverManager.getDriver() , EmployeeJobDetails.builder()
				.currentDate(jobDetails.getCurrentDate())
				.build());
		myInfoPage.SaveEmpJobDetails(DriverManager.getDriver()) ;
		myInfoPage.verifyToastMessage(DriverManager.getDriver());
		Log.messageStep("Added the Employee Job Details", DriverManager.getDriver());
		return employeeJobDetails;
	}

	@Override
	public String reportToSupervisior(PIMTabs tabName, String firstName, String lastName, EmployeeReportTo employeeReport) {
		MyInfoPage myInfoPage = new PimPage(DriverManager.getDriver()).clickPIMTab(DriverManager.getDriver(), tabName);
		myInfoPage.addSupervisorDetails(DriverManager.getDriver());
		String superVisiorName = myInfoPage.enterEmployeeName(DriverManager.getDriver(), firstName, lastName);
		myInfoPage.selectReportingMethod(DriverManager.getDriver(), ReportTo.REPORTING_METHOD, EmployeeReportTo.builder()
				.reportMethod(employeeReport.getReportMethod())
				.build());
		myInfoPage.saveSupervisorDetails(DriverManager.getDriver());
		myInfoPage.verifyToastMessage(DriverManager.getDriver());
		Log.messageStep("Added the Employee Reporting to the Supervisior with Supervisior Name: "+superVisiorName, DriverManager.getDriver());
		return superVisiorName;
	}

	@Override
	public void validateEmpAndDelete(ArrayList<String> empJobInfo, String employeeId) {
		PimPage pimPage = new PimPage(DriverManager.getDriver()).validateEmployeeList(DriverManager.getDriver(), empJobInfo);
		pimPage.deleteEmployee(DriverManager.getDriver(), employeeId);
		pimPage.verifyToastMessage(DriverManager.getDriver());
		Log.messageStep("Deleted Employee with Employee Id: "+employeeId, DriverManager.getDriver());
	}
}
