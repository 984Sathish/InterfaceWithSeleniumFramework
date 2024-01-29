package com.OrangeHRM.testscripts;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.OrangeHRM.PageItem.OrangeHRMData.CandidateDetails;
import com.OrangeHRM.PageItem.OrangeHRMData.EnterEmpFullName;
import com.OrangeHRM.PageItem.OrangeHRMData.ScheduleInterviewDetails;
import com.OrangeHRM.PageItem.OrangeHRMDataEngine;
import com.OrangeHRM.pages.DashboardPage;
import com.OrangeHRM.pages.LoginPage;
import com.OrangeHRM.pages.RecruitmentPage;
import com.OrangeHRM.teststeps.employee.DashboardConstants;
import com.OrangeHRM.teststeps.recruitment.RecruitementStepsImpl;
import com.OrangeHRM.teststeps.recruitment.RecruitmentConstants.AddCandidate;
import com.OrangeHRM.teststeps.recruitment.RecruitmentConstants.ScheduleInterview;
import com.OrangeHRM.teststeps.recruitment.RecruitmentSteps;
import com.OrangeHRM.utils.EnvironmentPropertiesReader;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.webdriverManager.DriverManager;


public class RecruitmentInterTestSuite {

	String hRMWebSite, browser;

	private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();

	ArrayList<String> lstCandidateDetails = new ArrayList<String>();

	protected static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

	@BeforeTest(alwaysRun = true)
	public void init(ITestContext context) {

		hRMWebSite = (System.getProperty("hRMWebSite") != null ? System.getProperty("webSite"): context.getCurrentXmlTest().getParameter("hRMWebSite"));
		DriverManager.setHRMWebsite(hRMWebSite);	

		browser = (configProperty.getProperty("browserName")) != null ? (configProperty.getProperty("browserName")) : context.getCurrentXmlTest().getParameter("browserName");
	}

	/**
	 * Test Description: Add new candidate and Hire
	 * Test ID: TC401
	 * @author sathish.suresh
	 * @throws Exception 
	 */
	@Test(description = "Add new candidate and Hire")
	public void TC401() throws Exception {

		DriverManager.setDriver(browser);
		WebDriver driver = DriverManager.getDriver();
		Log.testCaseInfo("Add new candidate and Hire", driver);
		OrangeHRMDataEngine testData = new OrangeHRMDataEngine("OrangeHRM_TestData.xlsx", "OrangeHRMData", "TC401");

		try {
			
			RecruitementStepsImpl recruitment = RecruitmentSteps.create();
			
			recruitment.loginToApplication("Admin", "admin123");
			
			String profileName = recruitment.getProfileName();
			
			recruitment.mainMenuSelection(DashboardConstants.menuName.RECRUITMENT);
			
			String selectedVacancy = recruitment.addCandidateForRecruitment( EnterEmpFullName.builder()
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.build(), CandidateDetails.builder()
					.email(testData.getEmail())
					.currentDate(testData.getCurrentDate())
					.mobile(testData.getMobile())
					.build());
			
			recruitment.shortListCandidate();
			
			recruitment.sheduleInterviewToCandidate(ScheduleInterviewDetails.builder()
					.title(testData.getTitle())
					.currentDate(testData.getCurrentDate())
					.build(), profileName);
			
			recruitment.markInterviewResult();
			
			recruitment.offerJobToCandidate();
			
			recruitment.hireCandidateInJob();
			
			recruitment.mainMenuSelection(DashboardConstants.menuName.RECRUITMENT);
			
			recruitment.searchCandidate(EnterEmpFullName.builder()
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.build());
			
			//Adding the Candidate information to the list
			lstCandidateDetails.add(testData.getFirstName() + " "+ testData.getLastName());
			lstCandidateDetails.add(selectedVacancy);
			
			recruitment.validateCandidateDetails(lstCandidateDetails);
			
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
