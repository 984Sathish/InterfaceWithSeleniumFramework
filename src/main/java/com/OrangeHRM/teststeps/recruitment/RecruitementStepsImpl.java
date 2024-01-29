package com.OrangeHRM.teststeps.recruitment;

import java.util.ArrayList;

import com.OrangeHRM.PageItem.OrangeHRMData.CandidateDetails;
import com.OrangeHRM.PageItem.OrangeHRMData.EnterEmpFullName;
import com.OrangeHRM.PageItem.OrangeHRMData.ScheduleInterviewDetails;
import com.OrangeHRM.pages.DashboardPage;
import com.OrangeHRM.pages.LoginPage;
import com.OrangeHRM.pages.RecruitmentPage;
import com.OrangeHRM.teststeps.employee.DashboardConstants.menuName;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.webdriverManager.DriverManager;

public class RecruitementStepsImpl implements RecruitmentSteps{

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
	public String getProfileName() {
		String profileName = new DashboardPage(DriverManager.getDriver()).get().getProfileName(DriverManager.getDriver());
		return profileName;
	}

	@Override
	public void mainMenuSelection(menuName menu) {
		DashboardPage dashboardPage = new DashboardPage(DriverManager.getDriver());
		dashboardPage.mainMenuSelection(DriverManager.getDriver(), menu);
		Log.messageStep("Main menu: " + menu.getName()+ " selected" , DriverManager.getDriver());
	}

	@Override
	public String addCandidateForRecruitment(EnterEmpFullName enterEmpFullName, CandidateDetails candidateDetails) {
		RecruitmentPage recruitmentPage = new RecruitmentPage(DriverManager.getDriver()).clickAddCandidates(DriverManager.getDriver());
		String vacancy = recruitmentPage.EnterCandidateDetails(DriverManager.getDriver(), enterEmpFullName, candidateDetails);
		recruitmentPage.SaveCandidateDetails(DriverManager.getDriver());
		recruitmentPage.verifyToastMessage(DriverManager.getDriver());
		Log.messageStep("Added Candidate with First Name: "+ enterEmpFullName.getFirstName() + " and Last Name: "+enterEmpFullName.getLastName() ,DriverManager.getDriver());

		return vacancy;
	}

	@Override
	public void shortListCandidate() {
		RecruitmentPage recruitmentPage = new RecruitmentPage(DriverManager.getDriver()).shortListCandidate(DriverManager.getDriver());
		recruitmentPage.SaveShortlistCandidate(DriverManager.getDriver());
		recruitmentPage.verifyToastMessage(DriverManager.getDriver());
		Log.messageStep("The Candidate has been Shortlisted", DriverManager.getDriver());
	}

	@Override
	public void sheduleInterviewToCandidate(ScheduleInterviewDetails scheduleInterviewDetails, String interviewerName) {
		RecruitmentPage recruitmentPage = new RecruitmentPage(DriverManager.getDriver()).ClickscheduleInterview(DriverManager.getDriver());
		recruitmentPage.EnterInterviewTitleAndDate(DriverManager.getDriver(), scheduleInterviewDetails);
		recruitmentPage.enterInterviewName(DriverManager.getDriver(), interviewerName);
		recruitmentPage.SaveScheduleInterview(DriverManager.getDriver());
		recruitmentPage.verifyToastMessage(DriverManager.getDriver());
		Log.messageStep("Successfully Scheduled an Interview for the Candidate.", DriverManager.getDriver());
	}

	@Override
	public void markInterviewResult() {
		RecruitmentPage recruitmentPage = new RecruitmentPage(DriverManager.getDriver()).markInterviewPassed(DriverManager.getDriver());
		recruitmentPage.SaveMarkInterview(DriverManager.getDriver());
		recruitmentPage.verifyToastMessage(DriverManager.getDriver());
		Log.messageStep("Mark that the Candidate Passed the Interview", DriverManager.getDriver());
	}

	@Override
	public void offerJobToCandidate() {
		RecruitmentPage recruitmentPage = new RecruitmentPage(DriverManager.getDriver()).offerJob(DriverManager.getDriver());
		recruitmentPage.SaveOfferJob(DriverManager.getDriver());
		recruitmentPage.verifyToastMessage(DriverManager.getDriver());
		Log.messageStep("The Candidate was Offered a Job.", DriverManager.getDriver());
	}

	@Override
	public void hireCandidateInJob() {
		RecruitmentPage recruitmentPage = new RecruitmentPage(DriverManager.getDriver()).HireInJob(DriverManager.getDriver());
		recruitmentPage.SaveHireCandidate(DriverManager.getDriver());
		recruitmentPage.verifyToastMessage(DriverManager.getDriver());
		recruitmentPage.verifyHiredMsg(DriverManager.getDriver());
		Log.messageStep("Hired a Candidate for the Position.", DriverManager.getDriver());
	}

	@Override
	public void searchCandidate(EnterEmpFullName enterEmpFullName) {
		RecruitmentPage recruitmentPage = new RecruitmentPage(DriverManager.getDriver()).enterCandidateForSearch(DriverManager.getDriver() ,enterEmpFullName);
		recruitmentPage.clickSearchBtn(DriverManager.getDriver());
		String candidatName = enterEmpFullName.getFirstName() +" "+ enterEmpFullName.getLastName();
		Log.messageStep("Searched for Candidate with Candiate Name: "+candidatName, DriverManager.getDriver());
	}

	@Override
	public void validateCandidateDetails(ArrayList<String> lstCandidatInfo) {
		new RecruitmentPage(DriverManager.getDriver()).validateCandidateDetails(DriverManager.getDriver(), lstCandidatInfo);

	}




}
