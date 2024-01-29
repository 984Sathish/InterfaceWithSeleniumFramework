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
import com.OrangeHRM.teststeps.claim.ClaimConstants.NavName;
import com.OrangeHRM.teststeps.employee.DashboardConstants;
import com.OrangeHRM.teststeps.claim.ClaimSteps;
import com.OrangeHRM.teststeps.claim.ClaimStepsImpl;
import com.OrangeHRM.utils.EnvironmentPropertiesReader;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.webdriverManager.DriverManager;


public class ClaimInterTestSuite {

	String hRMWebSite, browser;

	private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();

	ArrayList<String> claimDetails = new ArrayList<String>();

	protected static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

	@BeforeTest(alwaysRun = true)
	public void init(ITestContext context) {
		
		hRMWebSite = (System.getProperty("hRMWebSite") != null ? System.getProperty("webSite"): context.getCurrentXmlTest().getParameter("hRMWebSite"));
		DriverManager.setHRMWebsite(hRMWebSite);	

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

			ClaimStepsImpl claim = ClaimSteps.create();

			claim.loginToApplication("Admin", "admin123");

			claim.mainMenuSelection(DashboardConstants.menuName.PIM);

			claim.createEmployee(EnterEmpFullName.builder()
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.build());

			claim.mainMenuSelection(DashboardConstants.menuName.DASHBOARD);
			
			claim.mainMenuSelection(DashboardConstants.menuName.CLAIM);

			claim.subNavMenuSelection(NavName.ASSIGN_CLAIM);

			String referenceId = claim.createNewClaimRequest(EnterEmpFullName.builder()
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.build(), 
					CreateClaimDetails.builder()
					.event(testData.getEvent())
					.currency(testData.getCurrency())
					.build());

			claim.addNewExpense(ExpenseDetails.builder()
					.expenseType(testData.getExpenseType())
					.currentDate(testData.getCurrentDate())
					.amount(testData.getAmount())
					.build());

			claim.verifyExpenseAmtAndSubmit(testData.getCurrency(), testData.getAmount());
			
			claim.subNavMenuSelection(NavName.EMPLOYEE_CLAIMS);
			
			claim.searchClaim(referenceId);

			//Adding the claim information to the list
			claimDetails.add(testData.getFirstName() +" "+ testData.getLastName());
			claimDetails.add(testData.getEvent());
			claimDetails.add(testData.getCurrency());
			claimDetails.add(referenceId);
			claimDetails.add(testData.getAmount());

			claim.verifyClaimDetails(claimDetails);

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
