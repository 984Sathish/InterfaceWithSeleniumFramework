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
import com.OrangeHRM.teststeps.recruitment.RecruitmentConstants.AddCandidate;
import com.OrangeHRM.teststeps.recruitment.RecruitmentConstants.ScheduleInterview;
import com.OrangeHRM.utils.EnvironmentPropertiesReader;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.webdriverManager.DriverManager;


public class RecruitmentTestSuite {

	String webSite, browser;
	private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();
	ArrayList<String> lstCandidateDetails = new ArrayList<String>();; 
	
	protected static ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();

	@BeforeTest(alwaysRun = true)
	public void init(ITestContext context) {
		webSite = (System.getProperty("webSite") != null ? System.getProperty("webSite"): context.getCurrentXmlTest().getParameter("webSite"));
		//DriverManager.setWebsite(webSite);	

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
			LoginPage loginPage = new LoginPage(driver, webSite);
			DashboardPage dashboardPage = loginPage.LoginToApplication(driver, "Admin", "admin123");

			String profileName = dashboardPage.getProfileName(driver);

			RecruitmentPage recruitmentPage = (RecruitmentPage) dashboardPage.mainMenuSelection(driver, DashboardConstants.menuName.RECRUITMENT);

			recruitmentPage.clickAddCandidates(driver);

			recruitmentPage.addCandidateName(driver, EnterEmpFullName.builder()
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.build());

			lstCandidateDetails.add(testData.getFirstName() + " "+ testData.getLastName());

			String selectedVacancy = recruitmentPage.selectVacancy(driver, AddCandidate.VACANCY);

			lstCandidateDetails.add(selectedVacancy);

			recruitmentPage.enterCandidateEmail(driver, AddCandidate.EMAIL, CandidateDetails.builder()
					.email(testData.getEmail())
					.build());

			//lstCandidateDetails.add(testData.getCurrentDate());

			recruitmentPage.enterApplicationDate(driver, AddCandidate.DATE_OF_APPLICATION, CandidateDetails.builder()
					.currentDate(testData.getCurrentDate())
					.build());

			recruitmentPage.enterCandidateContactNo(driver, AddCandidate.CONTACT_NUMBER, CandidateDetails.builder()
					.mobile(testData.getMobile())
					.build());

			recruitmentPage.SaveCandidateDetails(driver);

			recruitmentPage.verifyToastMessage(driver);

			recruitmentPage.shortListCandidate(driver);

			recruitmentPage.SaveShortlistCandidate(driver);

			recruitmentPage.verifyToastMessage(driver);

			recruitmentPage.ClickscheduleInterview(driver);

			recruitmentPage.enterInterviewTitle(driver, ScheduleInterview.INTERVIEW_TITLE, ScheduleInterviewDetails.builder()
					.title(testData.getTitle())
					.build());

			recruitmentPage.enterInterviewDate(driver, ScheduleInterview.DATE, ScheduleInterviewDetails.builder()
					.currentDate(testData.getCurrentDate())
					.build());

			recruitmentPage.enterInterviewName(driver, profileName);

			recruitmentPage.SaveScheduleInterview(driver);

			recruitmentPage.verifyToastMessage(driver);

			recruitmentPage.markInterviewPassed(driver);

			recruitmentPage.SaveMarkInterview(driver);

			recruitmentPage.verifyToastMessage(driver);

			recruitmentPage.offerJob(driver);

			recruitmentPage.SaveOfferJob(driver);

			recruitmentPage.verifyToastMessage(driver);

			recruitmentPage.HireInJob(driver);

			recruitmentPage.SaveHireCandidate(driver);

			recruitmentPage.verifyToastMessage(driver);
			
			recruitmentPage.verifyHiredMsg(driver);

			recruitmentPage = (RecruitmentPage) dashboardPage.mainMenuSelection(driver, DashboardConstants.menuName.RECRUITMENT);

			recruitmentPage.enterCandidateName(driver, EnterEmpFullName.builder()
					.firstName(testData.getFirstName())
					.lastName(testData.getLastName())
					.build());

			recruitmentPage.clickSearchBtn(driver);

			recruitmentPage.validateCandidateDetails(driver, lstCandidateDetails);
			
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
