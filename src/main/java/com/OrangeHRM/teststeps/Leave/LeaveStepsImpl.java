package com.OrangeHRM.teststeps.Leave;

import java.util.ArrayList;

import com.OrangeHRM.PageItem.OrangeHRMData.EnterEmpFullName;
import com.OrangeHRM.PageItem.OrangeHRMData.LeaveDetails;
import com.OrangeHRM.PageItem.OrangeHRMData.UserDetails;
import com.OrangeHRM.pages.DashboardPage;
import com.OrangeHRM.pages.LeavePage;
import com.OrangeHRM.pages.LoginPage;
import com.OrangeHRM.pages.PimPage;
import com.OrangeHRM.teststeps.Leave.LeaveConstants.AssignLeave;
import com.OrangeHRM.teststeps.Leave.LeaveConstants.NavName;
import com.OrangeHRM.teststeps.employee.DashboardConstants.menuName;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.webdriverManager.DriverManager;

public class LeaveStepsImpl implements LeaveSteps{

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
	public void subNavMenuSelection(NavName navMenu) {
		new LeavePage(DriverManager.getDriver()).get().selectTopNavMenu(DriverManager.getDriver(), navMenu);
		Log.messageStep("Nav menu : " + navMenu.getName()+ " selected" , DriverManager.getDriver());
		
	}

	@Override
	public String assignLeaveToEmployee(EnterEmpFullName enterEmpFullName, LeaveDetails leaveDetails) {
		LeavePage leavePage = new LeavePage(DriverManager.getDriver());
		String leaveDate = leavePage.assignLeave(DriverManager.getDriver(), enterEmpFullName, leaveDetails);
		leavePage.clickAssignBtn(DriverManager.getDriver());
		leavePage.clickOkBtn(DriverManager.getDriver());
		leavePage.verifyToastMessage(DriverManager.getDriver());
		Log.messageStep("Assigned Leave to Employee with Leave Date: "+leaveDate, DriverManager.getDriver());
		return leaveDate;
	}

	@Override
	public void searchMyLeave(String leaveDate) {
		LeavePage leavePage = new LeavePage(DriverManager.getDriver()).enterDate(DriverManager.getDriver(), AssignLeave.FROM_DATE);
		leavePage.clickWorkingDayInCalender(DriverManager.getDriver(), leaveDate);
		leavePage.enterDate(DriverManager.getDriver(), AssignLeave.TO_DATE);
		leavePage.searchMyLeave(DriverManager.getDriver());
		Log.messageStep("Searched for Leave with Leave date: "+leaveDate, DriverManager.getDriver());
	}

	@Override
	public void verifyLeaveTakenDetails(ArrayList<String> leaveDetails) {
		new LeavePage(DriverManager.getDriver()).validateLeaveDetails(DriverManager.getDriver(), leaveDetails);
	}
	
	

}
