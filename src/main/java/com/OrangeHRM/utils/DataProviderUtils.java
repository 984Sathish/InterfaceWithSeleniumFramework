package com.OrangeHRM.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

public class DataProviderUtils {
	private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();

	@DataProvider(parallel = true)
	public static Iterator<Object[]> parallelTestDataProvider(ITestContext context) throws IOException {

		List<Object[]> dataToBeReturned = new ArrayList<Object[]>();
		List<String> browsers = null;
		List<String> platforms = null;
		List<String> browserVersions = null;
		List<String> deviceNames = null;
		String driverInitilizeInfo = null;
		String browser = null;
		String platform = null;
		String browserVersion = null;
		String deviceName = null;
		Iterator<String> browsersIt = null;
		Iterator<String> browserVersionsIt = null;
		Iterator<String> platformsIt = null;
		Iterator<String> deviceNameIt = null;

		// From local to sauce lab for browser test
		if (configProperty.hasProperty("runSauceLabFromLocal")
				&& configProperty.getProperty("runSauceLabFromLocal").equalsIgnoreCase("true")) {
			browser = configProperty.hasProperty("browserName") ? configProperty.getProperty("browserName") : null;
			System.out.println(configProperty.hasProperty("browserName"));
			System.out.println(configProperty.getProperty("browserName"));
			browserVersion = configProperty.hasProperty("browserVersion") ? configProperty.getProperty("browserVersion")
					: null;
			platform = configProperty.hasProperty("platform") ? configProperty.getProperty("platform") : null;
			
			 if (browser != null && browserVersion != null && platform != null ) {
			browsers = Arrays.asList(browser.split("\\|"));
			browserVersions = Arrays.asList(browserVersion.split("\\|"));
			platforms = Arrays.asList(platform.split("\\|"));

			browsersIt = browsers.iterator();
			browserVersionsIt = browserVersions.iterator();
			platformsIt = platforms.iterator();

			// From local to sauce lab for device test
			if (configProperty.hasProperty("runUserAgentDeviceTest")
					&& configProperty.getProperty("runUserAgentDeviceTest").equalsIgnoreCase("true")) {
				// handling parallel device test inputs
				deviceName = configProperty.hasProperty("deviceName") ? configProperty.getProperty("deviceName") : null;
				if (deviceName != null) {
				deviceNames = Arrays.asList(deviceName.split("\\|"));
				deviceNameIt = deviceNames.iterator();

				while (browsersIt.hasNext() && browserVersionsIt.hasNext() && platformsIt.hasNext()
						&& deviceNameIt.hasNext()) {
					browser = browsersIt.next();
					browserVersion = browserVersionsIt.next();
					platform = platformsIt.next();
					deviceName = deviceNameIt.next();
					driverInitilizeInfo = browser + "&" + browserVersion + "&" + platform + "&" + deviceName;
					dataToBeReturned.add(new Object[] { driverInitilizeInfo });
				}
				}
			} else {
				// handling parallel browser test inputs
				while (browsersIt.hasNext() && browserVersionsIt.hasNext() && platformsIt.hasNext()) {
					browser = browsersIt.next();
					browserVersion = browserVersionsIt.next();
					platform = platformsIt.next();
					driverInitilizeInfo = browser + "&" + browserVersion + "&" + platform;
					dataToBeReturned.add(new Object[] { driverInitilizeInfo });
				}
			}
			 }
		} else {
			// local to local test execution and also handling parallel support
			browsers = Arrays.asList(context.getCurrentXmlTest().getParameter("browserName").split(","));
			for (String b : browsers) {
				dataToBeReturned.add(new Object[] { b });
			}
		}
		return dataToBeReturned.iterator();
	}

	@DataProvider(parallel = true)
	public static Iterator<Object[]> multiDataIterator(ITestContext context) throws IOException {

		File dir1 = new File(".");
		String strBasePath = null;
		String browserInputFile = null;
		String paymentTypeInputFile = null;
		strBasePath = dir1.getCanonicalPath();

		List<String> websites = Arrays.asList(context.getCurrentXmlTest().getParameter("webSite").split(","));
		browserInputFile = strBasePath + File.separator + "src" + File.separator + "main" + File.separator + "resources"
				+ File.separator + "DataProviders" + File.separator
				+ context.getCurrentXmlTest().getParameter("browserDataProvider");
		paymentTypeInputFile = strBasePath + File.separator + "src" + File.separator + "main" + File.separator
				+ "resources" + File.separator + "DataProviders" + File.separator
				+ context.getCurrentXmlTest().getParameter("paymentDataProvider");

		// Get a list of String file content (line items) from the test file.
		List<String> browserList = getFileContentList(browserInputFile);
		List<String> paymentTypes = getFileContentList(paymentTypeInputFile);

		// We will be returning an iterator of Object arrays so create that
		// first.
		List<Object[]> dataToBeReturned = new ArrayList<Object[]>();

		// Populate our List of Object arrays with the file content.
		for (String website : websites) {
			for (String browser : browserList) {
				for (String payment : paymentTypes)
					dataToBeReturned.add(new Object[] { browser, website, payment });
			}
		}

		// return the iterator - testng will initialize the test class and calls
		// the
		// test method with each of the content of this iterator.
		return dataToBeReturned.iterator();

	}// multiDataIterator

	/**
	 * Utility method to get the file content in UTF8
	 * 
	 * @param filenamePath
	 * @return
	 */
	private static List<String> getFileContentList(String filenamePath) {
		List<String> lines = new ArrayList<String>();
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filenamePath), "UTF8"))) {

			String strLine;
			while ((strLine = br.readLine()) != null) {
				lines.add(strLine);
			}
			br.close();
		} catch (FileNotFoundException e) {
			try {
				Log.exception(e);
			} catch (Exception logException) {
				// TODO Auto-generated catch block
				System.err.println("Exception: " + e.getMessage());	
		}		} catch (IOException e) {
			try {
				Log.exception(e);
			} catch (Exception logException) {
				// TODO Auto-generated catch block
				System.err.println("Exception: " + e.getMessage());	
		}		}
		return lines;
	}

}