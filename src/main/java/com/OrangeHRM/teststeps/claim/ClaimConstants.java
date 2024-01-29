package com.OrangeHRM.teststeps.claim;

public class ClaimConstants {

	public enum NavName{

		ASSIGN_CLAIM ("Assign Claim"),
		EMPLOYEE_CLAIMS ("Employee Claims");

		private String name;

		NavName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}	

	public enum CreateClaimRequest{

		EVENT ("Event"),
		CURRENCY ("Currency");

		private String name;

		CreateClaimRequest(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}
		
	public enum AddExpense{

		EXPENSE_TYPE ("Expense Type"),
		DATE ("Date"),
		AMOUNT ("Amount");

		private String name;

		AddExpense(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}
}
