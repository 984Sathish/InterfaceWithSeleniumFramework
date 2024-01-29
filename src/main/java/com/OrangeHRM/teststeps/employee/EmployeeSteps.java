package com.OrangeHRM.teststeps.employee;

import java.util.ArrayList;

import com.OrangeHRM.PageItem.OrangeHRMData.EmployeeContactDetails;
import com.OrangeHRM.PageItem.OrangeHRMData.EmployeeJobDetails;
import com.OrangeHRM.PageItem.OrangeHRMData.EmployeePersonalDetails;
import com.OrangeHRM.PageItem.OrangeHRMData.EmployeeReportTo;
import com.OrangeHRM.PageItem.OrangeHRMData.EnterEmpFullName;
import com.OrangeHRM.teststeps.employee.DashboardConstants.menuName;
import com.OrangeHRM.teststeps.employee.PimConstants.NavName;
import com.OrangeHRM.teststeps.employee.PimConstants.PIMTabs;

public interface EmployeeSteps {
	
	public static EmployeeStepsImpl create() {
		return new EmployeeStepsImpl();
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
	public String createEmployee(EnterEmpFullName enterEmpFullName);

	/**
	 * @author sathish.suresh
	 * Click "Sub Nav Menu" to navigate nav menu page
	 * click sub nav menu based on menu name
	 * @param navMenu
	 */
	public void subNavMenuSelection(NavName navMenu);
	
	/**
	 * @author sathish.suresh
	 * Enter "Employee Name" in employe name field
	 * Enter "Employee Id" in employee id field
	 * Click "Search" button to search employee
	 * Search employee based on employee name and id
	 * @param enterEmpFullName
	 * @param employeeId
	 */
	public void searchEmployee(EnterEmpFullName enterEmpFullName, String employeeId);
	
	/**
	 * To Verify "Employee Name and Id" in employee's table
	 * Compare employee details based on list
	 */
	public void verifyEmployeeAndClkEmp();
	
	/**
	 * @author sathish.suresh
	 * Select "Nationality" in employee nationality dropdown
	 * Select "Marital Status" in employee marital status dropdown
	 * Enter "Date Of Birth" of employee in date field
	 * Select "Gender" in employee gender radio button
	 * Click "Save" button to save employee personal details
	 * To Verify "Success Message" that confirms employee personal details is saved
	 * Select "Blood Type" in employee blood type
	 * Click "Save" button to save employee custom details
	 * To Verify "Success Message" that confirms employee custom details is saved
	 * Add Employee personal and custom details
	 * @param employeePersonalDetails
	 */
	public void addEmpPersonalAndCustomDetails(EmployeePersonalDetails employeePersonalDetails);
	
	/**
	 * @author sathish.suresh
	 * Click "Contact" tab to navigate employee edit contact page
	 * Enter "Street" in employee street1 field
	 * Enter "City" in employee city field
	 * Enter "State" in employee state field
	 * Enter "Zipcode" in zipcode field
	 * Select country in employee country field
	 * Enter "Mobile" in employee mobile number field
	 * Enter "Email" in employee email field
	 * Add employee Contact details
	 * @param employeeContactDetails
	 * @param tabName
	 */
	public void addEmpContactDetails(PIMTabs tabName, EmployeeContactDetails employeeContactDetails);
	
	/**
	 * @author sathish.suresh
	 * Click "Job" tab to navigate employee edit employee job page
	 * Enter "Joined Date" in date field
	 * Select "Job Title" in employee job title dropdown
	 * Select "Job Category" in employee job category dropdown
	 * Select "Sub Unit" in job sub unit dropdown
	 * Select "Job Location" in employee job location dropdown
	 * Select "Employment Status" in employee status dropdown
	 * Click "Save" button to save employee job details
	 * To Verify "Success Message" that confirms employee job details is saved
	 * Add employee job deatils
	 * @param jobDetails
	 * @return employeeDetails
	 */
	public ArrayList<String> addEmpJobDetails(PIMTabs tabName, EmployeeJobDetails jobDetails);
	
	/**
	 * @author sathish.suresh
	 * Click "Report" tab to navigate employee edit employee job page
	 * Click "Add" button to navigate edit supervisior page
	 * Enter "Employee Name" in employee name field
	 * Select "Reporting Method" in reporting type dropdown
	 * Click "Save" button to save employee reporting details
	 * To Verify "Success Message" that confirms employee reporting details is saved
	 * Add employee reporting to supervisior
	 * @param firstName
	 * @param lastName
	 * @param employeeReportTo
	 * @return supervisiorName
	 */
	public String reportToSupervisior(PIMTabs tabName, String firstName, String lastName, EmployeeReportTo employeeReportTo);
	
	/**
	 * @author sathish.suresh
	 * To Verify "Employee Details" that confirm employee name and job details
	 * Click "Delete Icon" button to delete employee
	 * Click "Yes" button to confirm delete employee
	 * Verify employee details in employee's table and delete employee
	 * @param empJobInfo
	 * @param employeeId
	 */
	public void validateEmpAndDelete(ArrayList<String> empJobInfo, String employeeId);
}
