package com.OrangeHRM.PageItem;

import java.util.HashMap;

import com.OrangeHRM.utils.TestDataUtils;

import lombok.Getter;

@Getter
public class OrangeHRMDataEngine {

	HashMap<String,String> data;

	private String firstName;

	private String lastName;

	private String nationality;

	private String maritalStatus;

	private String currentDate;
	
	private String gender;
	
	private String bloodType;
	
	private String street;
	
	private String zipCode;
	
	private String city;
	
	private String state;
	
	private String mobile;
	
	private String email;
	
	private String country;

	private String reportMethod;
	
	private String username;
	
	private String password;
	
	private String userRole;
	
	private String status;
	
	private String leaveType;
		
	private String title;
	
	private String event;
	
	private String currency;
	
	private String expenseType;
	
	private String amount;
	
	public OrangeHRMDataEngine(String workBook,String sheetName,String testcaseID) {

		this.data=TestDataUtils.getTestData(workBook, sheetName, testcaseID);
		this.firstName = setFirstName();
		this.lastName = setLastName();
		this.nationality = setNationality();
		this.maritalStatus = setMaritalStatus();
		this.currentDate = setCurrentDate();
		this.gender = setGender();
		this.bloodType = setBloodType();
		this.street = setStreet();
		this.zipCode = setZipCode();
		this.city = setCity();
		this.state = setState();
		this.mobile = setMobile();
		this.email = setEmail();
		this.country = setCountry();
		this.reportMethod = setReportMethod();
		this.username = setUsername();
		this.password = setPassword();
		this.userRole = setUserRole();
		this.status = setStatus();
		this.leaveType = setLeaveType();
		this.title = setTitle();
		this.event = setEvent();
		this.currency = setCurrency();
		this.expenseType = setExpenseType();
		this.amount = setAmount();
		
	}

	public String setFirstName() {
		return firstName = data.get("FIRST_NAME").trim().equals("X") || data.get("FIRST_NAME").trim().length() == 0 ? DataFakers.firstName() :
			data.get("FIRST_NAME").trim();
	}

	public String setLastName() {
		return lastName = data.get("LAST_NAME").trim().equals("X") || data.get("LAST_NAME").trim().length() == 0 ? DataFakers.lastName() :
			data.get("LAST_NAME").trim();
	}

	public String setNationality() {
		return nationality = data.get("NATIONALITY").trim().equals("X") || data.get("NATIONALITY").trim().length() == 0 ? DataFakers.nationality() :
			data.get("NATIONALITY").trim();
	}

	public String setMaritalStatus() {
		return maritalStatus = data.get("MARITAL_STATUS").trim().equals("X") || data.get("MARITAL_STATUS").trim().length() == 0 ? DataFakers.maritalStatus() :
			data.get("MARITAL_STATUS").trim();
	}

	public String setCurrentDate() {
		return currentDate = data.get("DOB").trim().equals("X") || data.get("DOB").trim().length() == 0 ? DataFakers.currentDate() :
			data.get("DOB").trim();
	}

	public String setGender() {
		return gender = data.get("GENDER").trim().equals("X") || data.get("GENDER").trim().length() == 0 ? DataFakers.gender() :
			data.get("GENDER").trim();
	}

	public String setBloodType() {
		return bloodType = data.get("BLOOD_TYPE").trim().equals("X") || data.get("BLOOD_TYPE").trim().length() == 0 ? DataFakers.bloodType() :
			data.get("BLOOD_TYPE").trim();
	}
	
	private String setStreet() {
		return street = data.get("STREET_1").trim().equals("X") || data.get("STREET_1").trim().length() == 0 ? DataFakers.street() :
			data.get("STREET_1").trim();
	}
	
	private String setZipCode() {
		return zipCode = data.get("ZIPCODE").trim().equals("X") || data.get("ZIPCODE").trim().length() == 0 ? DataFakers.zipCode() :
			data.get("ZIPCODE").trim();
	}
	
	private String setCity() {
		return city = data.get("CITY").trim().equals("X") || data.get("CITY").trim().length() == 0 ? DataFakers.city() :
			data.get("CITY").trim();
	}

	private String setState() {
		return state = data.get("STATE").trim().equals("X") || data.get("STATE").trim().length() == 0 ? DataFakers.state() :
			data.get("STATE").trim();
	}
	
	private String setMobile() {
		return mobile = data.get("MOBILE").trim().equals("X") || data.get("MOBILE").trim().length() == 0 ? DataFakers.mobile() :
			data.get("MOBILE").trim();
	}
	
	private String setEmail() {
		return email = data.get("EMAIL").trim().equals("X") || data.get("EMAIL").trim().length() == 0 ? DataFakers.email() :
			data.get("EMAIL").trim();
	}
	
	private String setCountry() {
		return country = data.get("COUNTRY").trim().equals("X") || data.get("COUNTRY").trim().length() == 0 ? DataFakers.country() :
			data.get("COUNTRY").trim();
	}
	
	private String setReportMethod() {
		return reportMethod = data.get("REPORT_METHOD").trim().equals("X") || data.get("REPORT_METHOD").trim().length() == 0 ? DataFakers.reportMethod() :
			data.get("REPORT_METHOD").trim();
	}
	
	private String setUsername() {
		return username = data.get("USERNAME").trim().equals("X") || data.get("REPORT_METHOD").trim().length() == 0 ? DataFakers.username() :
			data.get("USERNAME").trim();
	}
	
	private String setPassword() {
		return password = data.get("PASSWORD").trim().equals("X") || data.get("PASSWORD").trim().length() == 0 ? DataFakers.password() :
			data.get("PASSWORD").trim();
	}
	
	private String setUserRole() {
		return userRole = data.get("USER_ROLE").trim().equals("X") || data.get("USER_ROLE").trim().length() == 0 ? DataFakers.userRole() :
			data.get("USER_ROLE").trim();
	}
	
	private String setStatus() {
		return status = data.get("STATUS").trim().equals("X") || data.get("STATUS").trim().length() == 0 ? DataFakers.status() :
			data.get("STATUS").trim();
	}
	
	private String setLeaveType() {
		return leaveType = data.get("LEAVE_TYPE").trim().equals("X") || data.get("LEAVE_TYPE").trim().length() == 0 ? DataFakers.leaveType() :
			data.get("LEAVE_TYPE").trim();
	}
	
	private String setTitle() {
		return title = data.get("TITLE").trim().equals("X") || data.get("TITLE").trim().length() == 0 ? DataFakers.title() :
			data.get("TITLE").trim();
	}

	private String setEvent() {
		return event = data.get("EVENT").trim().equals("X") || data.get("EVENT").trim().length() == 0 ? DataFakers.event() :
			data.get("EVENT").trim();
	}
	
	private String setCurrency() {
		return currency = data.get("CURRENCY").trim().equals("X") || data.get("CURRENCY").trim().length() == 0 ? DataFakers.currency() :
			data.get("CURRENCY").trim();
	}
	
	private String setExpenseType() {
		return expenseType = data.get("EXPENSE_TYPE").trim().equals("X") || data.get("EXPENSE_TYPE").trim().length() == 0 ? DataFakers.expenseType() :
			data.get("EXPENSE_TYPE").trim();
	}
	
	private String setAmount() {
		return amount = data.get("EXPENSE_AMOUNT").trim().equals("X") || data.get("EXPENSE_AMOUNT").trim().length() == 0 ? DataFakers.amount() :
			data.get("EXPENSE_AMOUNT").trim();
	}
	
}
