package com.OrangeHRM.teststeps.Leave;

public class LeaveConstants {

	public enum NavName{

		ASSIGN_LEAVE ("Assign Leave");

		private String name;

		NavName(String name) {
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
}
