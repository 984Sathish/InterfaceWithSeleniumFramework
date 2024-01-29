package com.OrangeHRM.teststeps.claim;

import java.util.ArrayList;

import com.OrangeHRM.PageItem.OrangeHRMData.CreateClaimDetails;
import com.OrangeHRM.PageItem.OrangeHRMData.EnterEmpFullName;
import com.OrangeHRM.PageItem.OrangeHRMData.ExpenseDetails;
import com.OrangeHRM.teststeps.claim.ClaimConstants.NavName;
import com.OrangeHRM.teststeps.employee.DashboardConstants.menuName;

public interface ClaimSteps {
	
	public static ClaimStepsImpl create() {
		return new ClaimStepsImpl();
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
	 * Click "Sub Nav Menu" to navigate nav menu page
	 * click sub nav menu based on menu name
	 * @param navMenu
	 */
	public void subNavMenuSelection(NavName navMenu);
	
	/**
	 * @author sathish.suresh
	 * Enter "Employee full Name"
	 * Select "Event" claim event dropdown
	 * Select "Currency" claim currency dropdown
	 * Click "Create Claim" button
	 * Create new claim request to assign employee
	 * @param enterEmpFullName
	 * @param createClaimDetails
	 * @return
	 */
	public String createNewClaimRequest(EnterEmpFullName enterEmpFullName, CreateClaimDetails createClaimDetails);
	
	/**
	 * @author sathish.suresh
	 * Click "Add Expense" to navigate expense selection page
	 * Select "Expense type" in expense type dropdown
	 * Enter "Date" in expense date field
	 * Enter "Amount" in expense amount field
	 * Click "Save" to save expense details
	 * Check "Success" message is displayed
	 * Add new expense for created employee claim request.
	 * @param expenseDetails
	 */
	public void addNewExpense(ExpenseDetails expenseDetails);
	
	
	/**
	 * @author sathish.suresh
	 * Verify "Total Expense Amount" with actual expense amount
	 * Click "Submit Cliam" to submit the cliam
	 * Check total expense amount and submit claim
	 * @param currency - expense currency type
	 * @param amount - expense amount
	 */
	public void verifyExpenseAmtAndSubmit(String currency, String amount);
	
	/**
	 * @author sathish.suresh
	 * Enter "Reference Id" to search claims 
	 * Click "Search" button to search claims
	 * Search cliam based on reference id
	 * @param referenceId - created claim reference id
	 */
	public void searchClaim(String referenceId);
	
	/**
	 * @author sathish.suresh
	 * Verify "Claim Data" for created claim
	 * Compare created cliam data based on given list.
	 * @param claimDetails - created cliam details(currency, reference id, employee name, event name, amount)
	 */
	public void verifyClaimDetails(ArrayList<String> claimDetails);
}
