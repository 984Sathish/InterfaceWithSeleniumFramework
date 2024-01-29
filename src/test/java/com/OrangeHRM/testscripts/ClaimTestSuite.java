package com.OrangeHRM.testscripts;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.OrangeHRM.PageItem.OrangeHRMData.CreateClaimDetails;
import com.OrangeHRM.PageItem.OrangeHRMData.EnterEmpFullName;
import com.OrangeHRM.PageItem.OrangeHRMData.ExpenseDetails;
import com.OrangeHRM.PageItem.OrangeHRMDataEngine;
import com.OrangeHRM.pages.ClaimPage;
import com.OrangeHRM.pages.DashboardPage;
import com.OrangeHRM.pages.LoginPage;
import com.OrangeHRM.pages.PimPage;
import com.OrangeHRM.teststeps.claim.ClaimConstants.AddExpense;
import com.OrangeHRM.teststeps.claim.ClaimConstants.CreateClaimRequest;
import com.OrangeHRM.teststeps.claim.ClaimConstants.NavName;
import com.OrangeHRM.teststeps.employee.DashboardConstants;
import com.OrangeHRM.utils.EnvironmentPropertiesReader;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.webdriverManager.DriverManager;


public class ClaimTestSuite {

	String webSite, browser;
	private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();
	ArrayList<String> claimDetails = new ArrayList<String>();; 

	@BeforeTest(alwaysRun = true)
	public void init(ITestContext context) {
		webSite = (System.getProperty("webSite") != null ? System.getProperty("webSite"): context.getCurrentXmlTest().getParameter("webSite"));
		//DriverManager.setWebsite(webSite);	

		browser = (configProperty.getProperty("browserName")) != null ? (configProperty.getProperty("browserName")) : context.getCurrentXmlTest().getParameter("browserName");
	}

	/**
	 * Test Description: Create a new Claim
	 * Test ID: TC301
	 * @author sathish.suresh
	 * @throws Exception 
	 */
	@Test(description = "Create a new Claim")
	public void TC501() throws Exception {
		
		DriverManager.setDriver(browser);
		WebDriver driver = DriverManager.getDriver();
		Log.testCaseInfo("Create a new Claim", driver);
		OrangeHRMDataEngine testData = new OrangeHRMDataEngine("OrangeHRM_TestData.xlsx", "OrangeHRMData", "TC501");

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

			dashboardPage.mainMenuSelection(driver, DashboardConstants.menuName.DASHBOARD);
			ClaimPage claimPage = (ClaimPage) dashboardPage.mainMenuSelection(driver, DashboardConstants.menuName.CLAIM);
			
			claimPage.selectTopNavMenu(driver, NavName.ASSIGN_CLAIM);
			
			claimPage.enterEmployeeName(driver, EnterEmpFullName.builder()
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.build());
			
			claimDetails.add(testData.getFirstName() +" "+ testData.getLastName());
			
			claimPage.selectEvent(driver, CreateClaimRequest.EVENT, CreateClaimDetails.builder()
					.event(testData.getEvent())
					.build());
			
			claimDetails.add(testData.getEvent());
			
			claimPage.selectCurrency(driver, CreateClaimRequest.CURRENCY, CreateClaimDetails.builder()
					.currency(testData.getCurrency())
					.build());
			
			claimDetails.add(testData.getCurrency());
			
			claimPage.clickCreateClaimRequest(driver);
			
			String referenceId = claimPage.getClaimReferenceId(driver);
			claimDetails.add(referenceId);
			
			claimPage.addExpense(driver);
			
			claimPage.selectExpenseType(driver, AddExpense.EXPENSE_TYPE, ExpenseDetails.builder()
					.expenseType(testData.getExpenseType())
					.build());
			
			claimPage.enterDate(driver, AddExpense.DATE, ExpenseDetails.builder()
					.currentDate(testData.getCurrentDate())
					.build());
			
			claimPage.enterAmount(driver, AddExpense.AMOUNT, ExpenseDetails.builder()
					.amount(testData.getAmount())
					.build());
			
			claimDetails.add(testData.getAmount());
			
			claimPage.saveExpense(driver);
			
			claimPage.verifyToastMessage(driver);
			
			claimPage.verifyTotalAmount(driver, testData.getCurrency(), testData.getAmount());
			
			claimPage.submitClaim(driver);
			
			claimPage.selectTopNavMenu(driver, NavName.EMPLOYEE_CLAIMS);
			
			claimPage.enterClaimReferenceId(driver, referenceId);
			
			claimPage.clickSearchBtn(driver);
			
			claimPage.validateClaimDetails(driver, claimDetails);
			
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
