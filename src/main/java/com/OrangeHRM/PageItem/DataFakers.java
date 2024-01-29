package com.OrangeHRM.PageItem;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

import net.datafaker.Faker;

public class DataFakers {

	public static String firstName() {
		return new Faker().name().firstName();
	}

	public static String lastName() {
		return new Faker().name().lastName();
	}

	public static String nationality() {
		return new Faker().expression("#{options.option 'American', 'Australian', 'German', 'Indian', 'Sri Lankan', 'South African'}");
	}

	public static String maritalStatus() {
		return new Faker().expression("#{options.option 'Single', 'Married', 'Other'}");
	}

	public static String currentDate() {
		SimpleDateFormat dayformatter = new SimpleDateFormat("EEEE", Locale.US);
		SimpleDateFormat dateformatter = new SimpleDateFormat("yyyy-MM-dd");
		
		Faker faker = new Faker();
		Timestamp birthday = faker.date().birthday();
		boolean weekday = false;
		while(!weekday) {
		if( !(dayformatter.format(birthday).equals("Sunday")) && !(dayformatter.format(birthday).equals("Saturday"))) {
			weekday = true;
			}
		else {
		birthday = faker.date().birthday();
		}
		}
		String date = dateformatter.format(birthday);
		return date;
	}

	public static String gender() {
		return new Faker().expression("#{options.option 'Male', 'Female'}");
	}

	public static String bloodType() {
		return new Faker().expression("#{options.option 'A+', 'A-', 'B+', 'B-', 'O+', 'O-', 'AB+', 'AB-'}");
	}
	
	public static String street() {
		return new Faker().address().streetAddress();
	}
	
	public static String zipCode() {
		return new Faker().address().zipCode();
	}
	
	public static String city() {
		return new Faker().address().cityName();
	}
	
	public static String state() {
		return new Faker().address().state();
	}
	
	public static String mobile() {
		return new Faker().numerify("##########");
	}
	
	public static String email() {
		return new Faker().internet().emailAddress();
		
	}
	
	public static String country() {
		return new Faker().expression("#{options.option 'Afghanistan', 'Australia', 'India', 'Sri Lanka', 'South Africa', 'United States'}");
	}
	
	public static String reportMethod() {
		return new Faker().expression("#{options.option 'Direct', 'Indirect'}");
	}
	
	public static String username() {
		return new Faker().name().username();
	}
	
	public static String userRole() {
		return new Faker().expression("#{options.option 'Admin', 'ESS'}");
	}
	
	public static String status() {
		return new Faker().expression("#{options.option 'Enabled', 'Disabled'}");
	}
	
	public static String password() {
		return new Faker().internet().password(7, 10);
	}
	
	public static String leaveType() {
		return new Faker().expression("#{options.option 'CAN - Bereavement', 'CAN - FMLA', 'CAN - Matternity', 'US - Personal', 'US - Vacation'}");
	}

	public static String title() {
		return new Faker().name().title();
	}
	
	public static String event() {
		return new Faker().expression("#{options.option 'Accommodation', 'Medical Reimbursement', 'Travel Allowance'}");
	}
	
	public static String currency() {
		return new Faker().expression("#{options.option 'Indian Rupee', 'Singapore Dollar', 'Australian Dollar', 'Euro', 'United States Dollar'}");
	}
	
	public static String expenseType() {
		return new Faker().expression("#{options.option 'Accommodation', 'Fuel Allowance', 'Planned Surgery', 'Transport'}");
	}
	
	public static String amount() {
		return new Faker().commerce().price();
	}
}
