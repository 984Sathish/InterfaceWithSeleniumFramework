package com.OrangeHRM.PageItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class OrangeHRMData {

	@Getter
	@Setter
	@Builder
	@AllArgsConstructor
	public static class EnterEmpFullName{

		private String firstName;
		private String lastName;
	}
	
	@Getter
	@Setter
	@Builder
	@AllArgsConstructor
	public static class EmployeePersonalDetails{

		private String nationality;
		private String maritalStatus;
		private String currentDate;
		private String gender;
		private String bloodType;
	}
	
	@Getter
	@Setter
	@Builder
	@AllArgsConstructor
	public static class EmployeeContactDetails{

		private String street;
		private String city;
		private String state;
		private String zipCode;
		private String mobile;
		private String email;
		private String country;
	}
	
	@Getter
	@Setter
	@Builder
	@AllArgsConstructor
	public static class EmployeeJobDetails{

		private String currentDate;
		private String jobTitle;
		private String jobpecification;
		private String jobCategory;
		private String subUnit;
		private String location;
		private String employmentStatus;
	}
	
	@Getter
	@Setter
	@Builder
	@AllArgsConstructor
	public static class EmployeeReportTo{

		private String reportMethod;
	}
	
	@Getter
	@Setter
	@Builder
	@AllArgsConstructor
	public static class UserDetails{

		private String username;
		private String password;
		private String userRole;
		private String status;
		private String firstName;
		private String lastName;
	}
	
	@Getter
	@Setter
	@Builder
	@AllArgsConstructor
	public static class LeaveDetails{

		private String leaveType;
		private String currentDate;
	}
	
	@Getter
	@Setter
	@Builder
	@AllArgsConstructor
	public static class CandidateDetails{

		private String vacancy;
		private String currentDate; //Date of application
		private String mobile;		//Contact number
		private String email;
	}

	@Getter
	@Setter
	@Builder
	@AllArgsConstructor
	public static class ScheduleInterviewDetails{

		private String title;
		private String interviewer;
		private String currentDate;
	}

	@Getter
	@Setter
	@Builder
	@AllArgsConstructor
	public static class CreateClaimDetails{

		private String firstName;
		private String lastName;
		private String event;
		private String currency;
	}
	
	@Getter
	@Setter
	@Builder
	@AllArgsConstructor
	public static class ExpenseDetails{

		private String expenseType;
		private String currentDate;
		private String amount;
		
	}
}
