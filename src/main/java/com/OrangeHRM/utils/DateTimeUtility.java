package com.OrangeHRM.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * DateTimeUtility consists Calendar/Date/Time format related manipulations
 */
public class DateTimeUtility {

	static String sampleDateFormat1 = "yyyy-MM-dd";
	static String sampleDateFormat2 = "MM/dd/yyyy";

	/**
	 * Method returns current date and time in format ddMMyyHHmmss
	 * 
	 * @return date - in the above mentioned format
	 */
	public static String getCurrentDateAndTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmss");
		Date date = new Date();
		String time = sdf.format(date);
		return time;
	}

	/**
	 * Method returns current date and time in logger format dd-MMM-yyyy HH-mm-ss
	 * 
	 * @return date - in the above mentioned format
	 */
	public static String getCurrentDateAndTimeInLoggerFormat() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH-mm-ss");
		Date date = new Date();
		String time = sdf.format(date);
		return time;
	}

	/**
	 * Method returns current date in format dd/MM/yyyy
	 * 
	 * @return currentdate - in the above mentioned format
	 */
	public static String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String currentdate = sdf.format(date);
		return currentdate;
	}

	/**
	 * Method returns current date and time in format dd-MMM-yy hh.mm.ss.SSSSSSSSS a
	 * 
	 * @return date - in the above mentioned format
	 */
	public static String getCurrentDateAndTimeDDMMYY() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy hh.mm.ss.SSSSSSSSS a");
		Date date = new Date();
		String time = sdf.format(date);
		return time;
	}

	/**
	 * Method returns current year in format yyyy
	 * 
	 * @return year - in the above mentioned format
	 */
	public static String getCurrentYear() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Date date = new Date();
		String time = sdf.format(date);
		return time;
	}

	/**
	 * Method returns current date in format MM/dd/yyyy
	 * 
	 * @return date - in the above mentioned format
	 */
	public static String getToday() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
		String today = dateFormat.format(date.getTime());
		return today;
	}

	/**
	 * Method gets current date from the calendar in the format MM/dd/yy
	 * 
	 * @param day current day
	 * @return expDate - in the above mentioned format
	 */
	public static String getDate(int day) {
		int millsInDay = 1000 * 60 * 60 * 24;
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
		String expDate = dateFormat.format(date.getTime() + (day * millsInDay));
		return expDate;
	}

	/**
	 * Method gets current time in format hhmm
	 * 
	 * @return date - in the above mentioned format
	 */
	public static String getTime() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("hhmm");
		String today = dateFormat.format(date.getTime());
		return today;
	}

	/**
	 * Method gets yesterday date in format MM/dd/yyyy
	 * 
	 * @return yesterday date - in the above mentioned format
	 */
	public static String getYesterday() {
		int millsInDay = 1000 * 60 * 60 * 24;
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(sampleDateFormat2);
		String yesterday = dateFormat.format(date.getTime() - (1 * millsInDay));
		return yesterday;
	}

	/**
	 * Method gets previous day date in format MM/dd/yyyy from the data parameter
	 * passed
	 * 
	 * @param testDate current date
	 * @return prevDate - in format MM/dd/yyyy
	 */
	@SuppressWarnings("deprecation")
	public static String getPrevday(String testDate) {
		int millsInDay = 1000 * 60 * 60 * 24;
		Date date = new Date(testDate);
		SimpleDateFormat dateFormat = new SimpleDateFormat(sampleDateFormat2);
		String yesterday = dateFormat.format(date.getTime() - (1 * millsInDay));
		return yesterday;
	}

	/**
	 * Method gets next day date in format MM/dd/yyyy from the data parameter passed
	 * 
	 * @param testDate current date
	 * @return nextDate - in format MM/dd/yyyy
	 */
	@SuppressWarnings("deprecation")
	public static String getNextday(String testDate) {
		int millsInDay = 1000 * 60 * 60 * 24;
		Date date = new Date(testDate);
		SimpleDateFormat dateFormat = new SimpleDateFormat(sampleDateFormat2);
		String yesterday = dateFormat.format(date.getTime() + (1 * millsInDay));
		return yesterday;
	}

	/**
	 * Method formats the date from MM/dd/yyyy to dd-MMM-yy
	 * 
	 * @param dateToformat current date
	 * @return date - in format dd-MMM-yy
	 * @throws ParseException
	 */
	public static String formatDateToddMMMyy(String dateToformat) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat(sampleDateFormat2);
		SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yy");
		Date date = format1.parse(dateToformat);
		return format2.format(date);
	}

	/**
	 * Method converts/formats the date from MM/dd/yyyy to yyyy-MM-dd
	 * 
	 * @param dateToformat date
	 * @return date - in format DD-MMM-yyyy
	 * @throws ParseException
	 * 
	 */
	public static String formatDateToyyyyMMdd(String dateToformat) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat(sampleDateFormat2);
		SimpleDateFormat format2 = new SimpleDateFormat(sampleDateFormat1);
		Date date = format1.parse(dateToformat);
		return format2.format(date);
	}

	/**
	 * Method converts/formats the date from MM/dd/yyyy to dd-MM-yy
	 * 
	 * @param dateToformat date
	 * @return date - in format dd/MM/yy
	 * @throws ParseException
	 * 
	 */
	public static String formatDateToDDMMYY(String dateToformat) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat(sampleDateFormat2);
		SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yy");
		Date date = format1.parse(dateToformat);
		return format2.format(date);
	}

	/**
	 * Method converts/formats the date from yyyd-dd-mm to dd/mm/yyyy
	 * 
	 * @param dateToformat testDate
	 * @return nextDate - in format dd/mm/yyyy
	 */
	public static String formatDateToDDMMYYYY(String dateToformat) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-dd-mm");
		SimpleDateFormat format2 = new SimpleDateFormat("dd/mm/yyyy");
		Date date = format1.parse(dateToformat);
		return format2.format(date);
	}

	/**
	 * Method converts/formats the date from yyyy-MM-dd to dd/MM/yyyy
	 * 
	 * @param dateToformat testDate
	 * @return nextDate - in format dd/MM/yyyy
	 */
	public static String formatDateToddMMYYYY(String dateToformat) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat(sampleDateFormat1);
		SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
		Date date = format1.parse(dateToformat);
		return format2.format(date);
	}

	/**
	 * Method converts/formats the date from yyyy-MM-dd to MM/dd/yyyy
	 * 
	 * @param dateToformat testDate
	 * @return date - in format MM/dd/yyyy
	 * @throws ParseException
	 */
	public static String formatDateToMMDDYYYY(String dateToformat) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat(sampleDateFormat1);
		SimpleDateFormat format2 = new SimpleDateFormat(sampleDateFormat2);
		Date date = format1.parse(dateToformat);
		return format2.format(date);
	}

	/**
	 * Method converts/formats the date from yyyy-MM-dd to dd/MMM/yy
	 * 
	 * @param dateToformat testDate
	 * @return date - in format dd/MMM/yy
	 * @throws ParseException
	 */
	public static String formatDateToddMMMYY(String dateToformat) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat(sampleDateFormat1);
		SimpleDateFormat format2 = new SimpleDateFormat("dd/MMM/yy");
		Date date = format1.parse(dateToformat);
		return format2.format(date);
	}

	/**
	 * Method gets the current today date in format dd/MM/yy
	 * 
	 * @return today date - in the above mentioned format
	 */
	public static String getTodayddMMyyyy() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String today = dateFormat.format(date.getTime());
		return today;
	}

	/**
	 * Method formats the date from yyyy-MM-dd to dd.MM.yyyy
	 * 
	 * @param dateToformat testDate
	 * @return date - in format dd.MM.yyyy
	 * @throws ParseException
	 */
	public static String formatDateToddMMyyyy(String dateToformat) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat(sampleDateFormat1);
		SimpleDateFormat format2 = new SimpleDateFormat("dd.MM.yyyy");
		Date date = format1.parse(dateToformat);
		return format2.format(date);
	}

	/**
	 * Method formats the date from dd-MM-yy to MM/dd/yyyy
	 * 
	 * @param dateToformat testDate
	 * @return date - in format MM/dd/yyyy
	 * @throws ParseException
	 */
	public static String formatDateToMMddyyyy(String dateToformat) throws ParseException {
		SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yy");
		SimpleDateFormat format2 = new SimpleDateFormat(sampleDateFormat2);
		Date date = format1.parse(dateToformat);
		return format2.format(date);
	}

	/**
	 * Method verifies the given date and time in format dd.MM.yyyy hh:mma
	 * 
	 * @param dateToVerify
	 * @param datetimeFormat
	 * @return true/false
	 */
	public static boolean verifyDateTimeFormatddmmyy(String dateToVerify, String datetimeFormat) {
		try {
			String dateTimeFormatToVerify = datetimeFormat.substring(0, 16);
			SimpleDateFormat dateFormat = new SimpleDateFormat(dateTimeFormatToVerify);
			dateFormat.parse(dateToVerify);
			if (datetimeFormat.length() > 16) {
				String am = dateToVerify.substring(16, 17);
				return ("p".equals(am)) || ("a".equals(am));
			} else {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Method gets the current time from the web clock
	 * 
	 * @param time
	 * 
	 * @return time
	 */
	public static ArrayList<String> getTimeForWebclock(String time) {

		String[] st1 = time.split(":");
		String st2 = st1[0];
		String st3 = st1[1];
		int j = Integer.parseInt(st3);
		int s = j - 1;
		int d = j + 1;
		String befTime = "" + s;
		String afTime = "" + d;
		if (j == 59) {
			afTime = "00";
		}
		if (j == 0) {
			befTime = "59";
		}
		ArrayList<String> data = new ArrayList<String>();
		data.add(st2);
		data.add(st3);
		data.add(befTime);
		data.add(afTime);
		return data;

	}
}