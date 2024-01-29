package com.OrangeHRM.teststeps.employee;

public class EditEmployeeConstants {

	public enum PersonalDetails{

		NATIONALITY ("Nationality"),
		MARITAL_STATUS ("Marital Status"),
		BLOOD_TYPE ("Blood Type"),
		DOB ("Date of Birth"),
		GENDER ("Gender");
		
		private String name;

		PersonalDetails(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}
	
	public enum ContactDetails{

		STREET_1 ("Street 1"),
		BLOOD_TYPE ("Blood Type"),
		CITY ("City"),
		STATE ("State/Province"),
		ZIP_CODE ("Zip/Postal Code"),
		COUNTRY ("Country"),
		MOBILE ("Mobile"),
		WORK_EMAIL ("Work Email");
		
		private String name;

		ContactDetails(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}
	
	public enum JobDetails{

		JOINED_DATE ("Joined Date"),
		JOB_TITLE ("Job Title"),
		JOB_SPECIFICATION ("Job Specification"),
		JOB_CATEGORY ("Job Category"),
		SUB_UNIT ("Sub Unit"),
		LOCATION ("Location"),
		EMPLOYMENT_STATUS ("Employment Status");
		
		private String name;

		JobDetails(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}
	
	public enum ReportTo{

		REPORTING_METHOD ("Reporting Method");
		
		private String name;

		ReportTo(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}

}
