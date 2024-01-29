package com.OrangeHRM.teststeps.recruitment;

import java.util.ArrayList;

import com.OrangeHRM.PageItem.OrangeHRMData.CandidateDetails;
import com.OrangeHRM.PageItem.OrangeHRMData.EnterEmpFullName;
import com.OrangeHRM.PageItem.OrangeHRMData.ScheduleInterviewDetails;
import com.OrangeHRM.teststeps.employee.DashboardConstants.menuName;

public interface RecruitmentSteps {

	public static RecruitementStepsImpl create() {
		return new RecruitementStepsImpl();
	}
	
	/**
	 * @author sathish.suresh
	 * Enter "User name" in the User name Field 
	 * Enter "Password" in the Password Field 
	 * Click "Login" Button
	 * Login to application with valid credentials
	 * @param username
	 * @param password
	 */
	public void loginToApplication(String username, String password);
	
	/**
	 * Get "Profile Name" from dashboard profile name field
	 * @return profile name
	 */
	public String getProfileName();
	
	/**
	 * @author sathish.suresh
	 * Click "Main menu" to naviagate clicked menu page
	 * return selected menu page
	 * Select main menu based on menu name
	 * @param menu
	 * @return 
	 */
	public void mainMenuSelection(menuName menu);
	
	/**
	 * @author sathish.suresh
	 * Click "Add" button to navigate add candidate page
	 * Enter "Candidate first name"
	 * Enter "Candidate last name"
	 * Select "Vacancy" in job vacancy dropdown
	 * Enter "Email" in candidate email field
	 * Enter "Date of Application" in application date field
	 * Enter "Contact Number" in candidate contact number field
	 * Click "Save" to save candidate details
	 * To Verify "Success Message" that confirms that candidate is added for recruitment
	 * Add new Candidate for recuritement
	 */
	public String addCandidateForRecruitment(EnterEmpFullName enterEmpFullName, CandidateDetails candidateDetails);
	
	/**
	 * @author sathish.suresh
	 * Click "Short List Candidate" button to shortlist candidate for interview
	 * Click "Save" button to save shortlist candidate details 
	 * To Verify "Success Message" that confirms shortlist candidate details is saved
	 * Short list candidate for interview 
	 */
	public void shortListCandidate();
	
	/**
	 * @author sathish.suresh
	 * Click "Shedule Interview" button for interview shedule to candidate
	 * Enter "Interview Date" in date field
	 * Enter "Interview Title" in interview title field
	 * Enter "Interviewr Name" in interviewer field
	 * Click "Save" buton to save interview shedule details
	 * To verify the 'Success Message' that confirms interview schedule details is saved.
	 * Shedule interview to candidate
	 * @param scheduleInterviewDetails
	 * @param interviewerName
	 */
	public void sheduleInterviewToCandidate(ScheduleInterviewDetails scheduleInterviewDetails, String interviewerName);
	
	/**
	 * @author sathish.suresh
	 * Click "Mark Interview Passed" button to candidate passed the interivew
	 * Click "Save" button to save interview result 
	 * To Verify "Success Message" that confirms interview result is saved 
	 * Mark interview result as passed  
	 */
	public void markInterviewResult();
	
	/**
	 * @author sathish.suresh
	 * Click "Offer Job" button to offer job for candidate
	 * Click "Save" button to save job offer
	 * To Verify "Success Message" that confirms job offer is saved
	 * Offer job to candidate
	 */
	public void offerJobToCandidate();
	
	/**
	 * @author sathish.suresh
	 * Click "Hire" button to hire candidate
	 * Click "Save" button to save the candidate hiring 
	 * To Verify "Success Message" that confirms hire details is saved
	 * Verify "Hired" text thats confirms candidate hired successfully
	 * Hire candidate in recruitement
	 */
	public void hireCandidateInJob();
	
	/**
	 * @author sathish.suresh
	 * Enter "Candidate Name" in candidate name field
	 * Click "Search" button to searching candidate 
	 * Search candidate based on name
	 * @param enterEmpFullName
	 */
	public void searchCandidate(EnterEmpFullName enterEmpFullName);
	
	/**
	 * @author sathish.suresh
	 * Verify "Candidate Information" from the searched candidate list.
	 * Compare candidate data based on given list.
	 * @param lstCandidatInfo
	 */
	public void validateCandidateDetails(ArrayList<String> lstCandidatInfo);
	
}
