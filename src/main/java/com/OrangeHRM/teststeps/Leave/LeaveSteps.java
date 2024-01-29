package com.OrangeHRM.teststeps.Leave;

import java.util.ArrayList;

import com.OrangeHRM.PageItem.OrangeHRMData.EnterEmpFullName;
import com.OrangeHRM.PageItem.OrangeHRMData.LeaveDetails;
import com.OrangeHRM.PageItem.OrangeHRMData.UserDetails;
import com.OrangeHRM.teststeps.Leave.LeaveConstants.NavName;
import com.OrangeHRM.teststeps.employee.DashboardConstants.menuName;

public interface LeaveSteps {
	
	public static LeaveStepsImpl create() {
		return new LeaveStepsImpl();
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
	 * Click "Sub Nav Menu" to navigate nav menu page
	 * click sub nav menu based on menu name
	 * @param navMenu
	 */
	public void subNavMenuSelection(NavName navMenu);
	
	/**
	 * @author sathish.suresh
	 * Enter "Employee Full Name" to employee name field
	 * Select "Leave Type" in leave type dropdown
	 * Enter "Working day" in "From Date" And "To Date" field
	 * Click "Assign" button to assign leave
	 * Click "Ok" button to confirm the leave
	 * Verify "Success Message" is displayed
	 * Assign leave to the employee.
	 */
	public String assignLeaveToEmployee(EnterEmpFullName enterEmpFullName, LeaveDetails leaveDetails);
	
	/**
	 * @author sathish.suresh
	 * Enter "Working day" in "From Date" And "To Date" field
	 * Click "Search" button to searching for leave
	 * @param leaveDate
	 */
	public void searchMyLeave(String leaveDate);
	
	/**
	 * @author sathish.suresh
	 * Verify "Leave Data" for taken leave
	 * Compare taken leave data based on given list.
	 * @param leaveDetails - include leave type, employee name
	 */
	public void verifyLeaveTakenDetails(ArrayList<String> leaveDetails);
	
}
