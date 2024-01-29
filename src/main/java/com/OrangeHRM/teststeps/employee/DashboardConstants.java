package com.OrangeHRM.teststeps.employee;

public class DashboardConstants {

	public enum menuName{

		ADMIN ("Admin"),
		PIM ("PIM"),
		LEAVE ("Leave"),
		TIME ("Time"),
		RECRUITMENT ("Recruitment"),
		MY_INFO ("My Info"),
		PERFORMANCE ("Performance"),
		DASHBOARD ("Dashboard"),
		DIRECTORY ("Directory"),
		MAINTENANCE ("Maintenance"),
		CLAIM ("Claim"),
		BUZZ ("Buzz");

		private String name;

		menuName(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}


	}



}
