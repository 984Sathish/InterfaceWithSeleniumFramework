
package com.OrangeHRM.teststeps.timesheet;

import com.OrangeHRM.PageItem.OrangeHRMData.EnterEmpFullName;
import com.OrangeHRM.PageItem.OrangeHRMData.UserDetails;
import com.OrangeHRM.teststeps.employee.DashboardConstants.menuName;

public interface TimesheetSteps {
	
	public static TimesheetStepsImpl create() {
		return new TimesheetStepsImpl();
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
	 * Click "Add" button to navigate create employee page
	 * Enter "Employee first name"
	 * Enter "Employee last name"
	 * Enter "Employee Id"
	 * Add new Employee details
	 */
	public void createEmployee(EnterEmpFullName enterEmpFullName);
	
	/**
	 * Enter "Username" in employee username field
	 * Enter "Password" in employee password field
	 * Enter "Confirm Password" 
	 */
	public void addLoginCredentials(UserDetails userDetails);
	
	/**
	 * @author sathish.suresh
	 * Click "Save" button to save employee details
	 * Verify employee created "Success message" is displayed
	 * Save employee and check success message
	 */
	public void saveEmployeeAndVerify();
	
	/**
	 * @author sathish.suresh
	 * Click "User Profile" to naviagate profile option
	 * Click "Logout" button to logout to application
	 * Click user profile and logout.
	 */
	public void logoutToApplication();
	
	/**
	 * @author sathish.suresh
	 * Click "Submit" button to submit timesheet
	 * Check "Success Message" 
	 * Check "Submitted Status" to verify timesheet submited successfully
	 * Submit timesheet and verify timesheet is submitted.
	 */
	public void submitTimesheetAndVerify();
	
	
	/**
	 * @author sathish.suresh
	 * Verify "TimeSheet Data" for submitted timesheet
	 * Compare submitted timesheet data based on given name.
	 * @param userName - timesheet submitted employee name
	 */
	public void verifyTimesheetDetails(String empName);

}
