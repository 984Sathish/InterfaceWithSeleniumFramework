package com.OrangeHRM.teststeps.claim;

import java.util.ArrayList;

import com.OrangeHRM.PageItem.OrangeHRMData.CreateClaimDetails;
import com.OrangeHRM.PageItem.OrangeHRMData.EnterEmpFullName;
import com.OrangeHRM.PageItem.OrangeHRMData.ExpenseDetails;
import com.OrangeHRM.pages.ClaimPage;
import com.OrangeHRM.pages.DashboardPage;
import com.OrangeHRM.pages.LoginPage;
import com.OrangeHRM.pages.PimPage;
import com.OrangeHRM.teststeps.claim.ClaimConstants.NavName;
import com.OrangeHRM.teststeps.employee.DashboardConstants.menuName;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.webdriverManager.DriverManager;

public class ClaimStepsImpl implements ClaimSteps {

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
	public void subNavMenuSelection(NavName navMenu) {
		new ClaimPage(DriverManager.getDriver()).get().selectTopNavMenu(DriverManager.getDriver(), navMenu);
		Log.messageStep("SubNav menu: " + navMenu.getName()+ " selected" , DriverManager.getDriver());
	}

	@Override
	public String createNewClaimRequest(EnterEmpFullName enterEmpFullName, CreateClaimDetails createClaimDetails) {
		ClaimPage claimPage = new ClaimPage(DriverManager.getDriver()).createClaimRequest(DriverManager.getDriver(), enterEmpFullName, createClaimDetails);
		claimPage.clickCreateClaimRequest(DriverManager.getDriver());
		String referenceId = claimPage.getClaimReferenceId(DriverManager.getDriver());
		Log.messageStep("Created Claim Request with Reference Id: "+ referenceId, DriverManager.getDriver());
		return referenceId;
	}

	@Override
	public void addNewExpense(ExpenseDetails expenseDetails) {
		ClaimPage claimPage = new ClaimPage(DriverManager.getDriver()).addExpense(DriverManager.getDriver());
		claimPage.addNewExpense(DriverManager.getDriver(), expenseDetails);
		claimPage.saveExpense(DriverManager.getDriver());
		claimPage.verifyToastMessage(DriverManager.getDriver());
		Log.messageStep("Add Expense with Expense Amount: "+expenseDetails.getAmount() , DriverManager.getDriver());
	}

	@Override
	public void verifyExpenseAmtAndSubmit(String currency, String amount) {
		ClaimPage claimPage = new ClaimPage(DriverManager.getDriver()).verifyTotalAmount(DriverManager.getDriver(), currency, amount);
		claimPage.submitClaim(DriverManager.getDriver());
		Log.messageStep("Submited Claim with Expenses", DriverManager.getDriver());
	}

	@Override
	public void searchClaim(String referenceId) {
		ClaimPage claimPage = new ClaimPage(DriverManager.getDriver()).enterClaimReferenceId(DriverManager.getDriver(), referenceId);
		claimPage.clickSearchBtn(DriverManager.getDriver());
		Log.messageStep("Searched for Claim with Reference Id: "+ referenceId, DriverManager.getDriver());
	}

	@Override
	public void verifyClaimDetails(ArrayList<String> claimDetails) {
		new ClaimPage(DriverManager.getDriver()).validateClaimDetails(DriverManager.getDriver(), claimDetails);

	}


}
