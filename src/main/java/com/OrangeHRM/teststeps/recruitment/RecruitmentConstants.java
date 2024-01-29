package com.OrangeHRM.teststeps.recruitment;

public class RecruitmentConstants {

	public enum AddCandidate{

		VACANCY ("Vacancy"),
		CONTACT_NUMBER ("Contact Number"),
		DATE_OF_APPLICATION ("Date of Application"),
		EMAIL ("Email");

		private String name;

		AddCandidate(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}	

	public enum AssignLeave{

		LEAVE_TYPE ("Leave Type"),
		FROM_DATE ("From Date"),
		TO_DATE ("To Date");

		private String name;

		AssignLeave(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}	
	
	public enum ScheduleInterview{

		INTERVIEW_TITLE ("Interview Title"),
		INTERVIEWER ("Interviewer"),
		DATE ("Date");

		private String name;

		ScheduleInterview(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}	

}
