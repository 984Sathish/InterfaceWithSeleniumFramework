package com.OrangeHRM.webdriverManager;

import java.net.MalformedURLException;
import org.openqa.selenium.WebDriver;

import com.OrangeHRM.utils.Log;
import com.OrangeHRM.utils.WebDriverFactory;

public class DriverManager {

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static String hrmWebSite = "";

	
	public static void setDriver(String browser) {
		try {
			driver.set(WebDriverFactory.get(browser));
		} catch (MalformedURLException e) {
			try {
				Log.exception(e);
			} catch (Exception logException) {
				// TODO Auto-generated catch block
				System.err.println("Exception: " + e.getMessage());	
		}		}
	}
	
	public static WebDriver getDriver() {
		Log.event("WebDriver session: "+driver.get());
		return driver.get();
	}

	public static void quitDriver() {
		getDriver().quit();
		driver.remove();
	}
	
	public static void quitDriver(WebDriver driver) {
		if(driver!=null)
		driver.close();
		//driver.quit();
		DriverManager.driver.remove();
	}
		
	public static synchronized String getHRMWebsite() {
		return hrmWebSite;
	}
	
	
	public static synchronized void setHRMWebsite(String HRMwebsite) {
		DriverManager.hrmWebSite = HRMwebsite;
	}

}
