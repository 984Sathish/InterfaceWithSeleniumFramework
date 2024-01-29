package com.OrangeHRM.teststeps.timesheet;

import com.OrangeHRM.PageItem.OrangeHRMData.EnterEmpFullName;
import com.OrangeHRM.PageItem.OrangeHRMData.UserDetails;
import com.OrangeHRM.pages.DashboardPage;
import com.OrangeHRM.pages.LoginPage;
import com.OrangeHRM.pages.PimPage;
import com.OrangeHRM.pages.TimePage;
import com.OrangeHRM.teststeps.employee.DashboardConstants.menuName;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.webdriverManager.DriverManager;

public class TimesheetStepsImpl implements TimesheetSteps{

	private static String webSite = DriverManager.getHRMWebsite();

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
	public void createEmployee(EnterEmpFullName enterEmpFullName) {
		PimPage pimPage = new PimPage(DriverManager.getDriver()).clickAddEmployee(DriverManager.getDriver());
		pimPage.addEmployeeName(DriverManager.getDriver(), enterEmpFullName);
		pimPage.enterEmployeeId(DriverManager.getDriver());
		Log.messageStep("Added Employee with First Name: "+ enterEmpFullName.getFirstName() + " and Last Name: "+enterEmpFullName.getLastName() ,DriverManager.getDriver());
	}


	@Override
	public void addLoginCredentials(UserDetails userDetails) {
		PimPage pimPage = new PimPage(DriverManager.getDriver()).clickOncreateLogin(DriverManager.getDriver());
		pimPage.EnterLoginCredentials(DriverManager.getDriver(), userDetails);
		Log.messageStep("Added Employee Login Credentials with Username: "+ userDetails.getUsername() + " and Password: "+userDetails.getPassword() ,DriverManager.getDriver());

	}

	@Override
	public void saveEmployeeAndVerify() {
		PimPage pimPage = new PimPage(DriverManager.getDriver()).clickSavBtn(DriverManager.getDriver());
		pimPage.verifyToastMessage(DriverManager.getDriver());
		Log.messageStep("Saved Employee Information and Verified Success message", DriverManager.getDriver());
	}

	@Override
	public void logoutToApplication() {
		DashboardPage dashboardPage = new DashboardPage(DriverManager.getDriver()).clickOnUserProfile(DriverManager.getDriver());
		dashboardPage.logOut(DriverManager.getDriver());
		Log.messageStep("Logout to OrangeHRM application", DriverManager.getDriver());
	}

	@Override
	public void submitTimesheetAndVerify() {
		TimePage timePage = new TimePage(DriverManager.getDriver()).submitTimesheet(DriverManager.getDriver());
		timePage.verifyToastMessage(DriverManager.getDriver());
		timePage.verifyTimesheetIsSubmited(DriverManager.getDriver());
		Log.messageStep("Submitted Timesheet Successfully", DriverManager.getDriver());
	}

	@Override
	public void verifyTimesheetDetails(String empName) {
		new TimePage(DriverManager.getDriver()).validateTimesheetDetails(DriverManager.getDriver(), empName);
	}


}
