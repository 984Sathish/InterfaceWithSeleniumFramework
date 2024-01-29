package com.OrangeHRM.teststeps.admin;

import com.OrangeHRM.PageItem.OrangeHRMData.EnterEmpFullName;
import com.OrangeHRM.PageItem.OrangeHRMData.UserDetails;
import com.OrangeHRM.pages.AdminPage;
import com.OrangeHRM.pages.DashboardPage;
import com.OrangeHRM.pages.LoginPage;
import com.OrangeHRM.pages.PimPage;
import com.OrangeHRM.teststeps.admin.AdminConstants.AddUser;
import com.OrangeHRM.teststeps.employee.DashboardConstants.menuName;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.webdriverManager.DriverManager;

public class AdminStepsImpl implements AdminSteps{
	
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
		pimPage.clickSavBtn(DriverManager.getDriver());
		pimPage.verifyToastMessage(DriverManager.getDriver());
		Log.messageStep("Created Employee with First Name: "+ enterEmpFullName.getFirstName() + " and Last Name: "+enterEmpFullName.getLastName() ,DriverManager.getDriver());
	}

	@Override
	public void CreateAdmin(UserDetails userDetails) {
		AdminPage adminPage = new AdminPage(DriverManager.getDriver()).clickAddUser(DriverManager.getDriver());
		adminPage.createUser(DriverManager.getDriver(), userDetails);
		adminPage.clickSavBtn(DriverManager.getDriver());
		adminPage.verifyToastMessage(DriverManager.getDriver());
		Log.messageStep("Created Admin with User Name: "+ userDetails.getUsername(), DriverManager.getDriver());

	}

	@Override
	public void searchAdmin(UserDetails userDetails) {
		AdminPage adminPage = new AdminPage(DriverManager.getDriver()).get();
		adminPage.enterUserName(DriverManager.getDriver(),  AddUser.USERNAME, userDetails);
		adminPage.clickSearchBtn(DriverManager.getDriver());
		Log.messageStep("Searched for Admin with User Name: "+ userDetails.getUsername(), DriverManager.getDriver());

	}

	@Override
	public void deleteAdmin(UserDetails userDetails) {
		AdminPage adminPage = new AdminPage(DriverManager.getDriver()).deleteUser(DriverManager.getDriver(), userDetails);
		adminPage.verifyToastMessage(DriverManager.getDriver());
		Log.messageStep("Deleted Admin with username: "+ userDetails.getUsername(), DriverManager.getDriver());

	}


}
