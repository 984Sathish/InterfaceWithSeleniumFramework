package com.OrangeHRM.teststeps.admin;

import com.OrangeHRM.PageItem.OrangeHRMData.EnterEmpFullName;
import com.OrangeHRM.PageItem.OrangeHRMData.UserDetails;
import com.OrangeHRM.teststeps.employee.DashboardConstants.menuName;

public interface AdminSteps {

	public static AdminStepsImpl create() {
		return new AdminStepsImpl();
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
	 * Click "Save" button to save employee details
	 * Verify employee created "Success message" is displayed
	 * Add new Employee details, Save employee and verify success message.
	 */
	public void createEmployee(EnterEmpFullName enterEmpFullName);
	
	/**
	 * @author sathish.suresh
	 * Click "Add" button to navigate create admin page
	 * Enter "Admin Username" in username field
	 * Enter "Admin Password" in password field
	 * Select "User Status" in status dropdown
	 * Select "User Role" in role dropdown
	 * Enter Employee "first name, last name" and select employee name
	 * Click "Save" button to save admin details
	 * Verify admin created "Success message" is displayed
	 * Add new Admin details, Save employee and verify success message.
	 * @param userDetails
	 */
	public void CreateAdmin(UserDetails userDetails);
	
	/**
	 * @author sathish.suresh
	 * Enter "Username" in username field
	 * Click "Search" button to search admin
	 * Search Admin based on username
	 * @param userDetails
	 */
	public void searchAdmin(UserDetails userDetails);
	
	/**
	 * @author sathish.suresh
	 * Click "Delete" admin based on admin username
	 * Verify admin deleted success message
	 * Delete admin and verify deleted message.
	 * @param userDetails
	 */
	public void deleteAdmin(UserDetails userDetails);
	
}
