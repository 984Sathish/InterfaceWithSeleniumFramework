package com.OrangeHRM.teststeps.admin;

public class AdminConstants {

	public enum AddUser{

		USER_ROLE ("User Role"),
		STATUS ("Status"),
		PASSWORD ("Password"),
		CONFIRM_PASSWORD ("Confirm Password"),
		USERNAME ("Username");

		private String name;

		AddUser(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}
}
