package com.OrangeHRM.teststeps.employee;

public class PimConstants {

	public enum NavName{

		EMPLOYEE_LIST ("Employee List"),
		ADD_EMPLOYEE ("Add Employee"),
		REPORTS ("Reports");

		private String name;

		NavName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}

	public enum EmployeeInfo{

		EMPLOYEE_ID ("Employee Id");

		private String name;

		EmployeeInfo(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}
	
	public enum PIMTabs{

		PERSONAL_DETAILS ("Personal Details"),
		CONTACT_DETAILS ("Contact Details"),
		EMERGENCY_CONTACTS ("Emergency Contacts"),
		DEPENDENTS ("Dependents"),
		IMMIGRATION ("Immigration"),
		JOB ("Job"),
		SALARY ("Salary"),
		TAX_EXEMPTIONS ("Tax Exemptions"),
		REPORT_TO ("Report-to"),
		QUALIFICATIONS ("Qualifications"),
		MEMBERSHIPS ("Memberships");

		private String name;

		PIMTabs(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}

}
