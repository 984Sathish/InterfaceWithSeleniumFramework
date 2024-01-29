package com.OrangeHRM.utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;

import com.epam.reportportal.service.ReportPortal;

/**
 * Log class consists capturing and printing screenshots, methods for writing
 * log status as pass/fail logs, methods to print test case info and messages

 */
public class Log  {

	public static ThreadLocal<Integer> steps = ThreadLocal.withInitial(() -> 0);
	public static boolean printconsoleoutput;
	private static String screenShotFolderPath;
	private static File screens;
	private static AtomicInteger screenShotIndex = new AtomicInteger(0);
	private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();
	private static String reportPortal;

	static final String TEST_TITLE_HTML_BEGIN = "&emsp;<div class=\"test-title\"> <strong><font size = \"3\" color = \"#000080\">";
	static final String TEST_TITLE_HTML_END = "</font> </strong> </div>&emsp;<div><strong>Steps:</strong></div>";

	static final String TEST_COND_HTML_BEGIN = "&emsp;<div class=\"test-title\"> <strong><font size = \"3\" color = \"#0000FF\">";
	static final String TEST_COND_HTML_END = "</font> </strong> </div>&emsp;";

	static final String MESSAGE_HTML_BEGIN = "<div class=\"test-message\">&emsp;";
	static final String MESSAGE_HTML_END = "</div>";

	static final String PASS_HTML_BEGIN = "<div class=\"test-result\"><br><font color=\"green\"><strong> ";
	static final String PASS_HTML_END1 = " </strong></font> ";
	static final String PASS_HTML_END2 = "</div>&emsp;";

	static final String FAIL_HTML_BEGIN = "<div class=\"test-result\"><br><font color=\"red\"><strong> ";
	static final String FAIL_HTML_END1 = " </strong></font> ";
	static final String FAIL_HTML_END2 = "</div>&emsp;";

	static final String SKIP_EXCEPTION_HTML_BEGIN = "<div class=\"test-result\"><br><font color=\"orange\"><strong> ";
	static final String SKIP_HTML_END1 = " </strong></font> ";
	static final String SKIP_HTML_END2 = " </strong></font> ";

	static final String EVENT_HTML_BEGIN = "<div class=\"test-event\"> <font color=\"maroon\"> <small> &emsp; &emsp;--- ";
	static final String EVENT_HTML_END = "</small> </font> </div>";

	static final String TRACE_HTML_BEGIN = "<div class=\"test-event\"> <font color=\"brown\"> <small> &emsp; &emsp;--- ";
	static final String TRACE_HTML_END = "</small> </font> </div>";

	static final String PERFORMANCE_HTML_BEGIN = "<script src=\"";
	static final String PERFORMANCE_HTML_END = "\"></script>";

	/**
	 * Static block clears the screenshot folder if any in the output during
	 * every suite execution and also sets up the print console flag for the run
	 */
	static {

		try {
			InputStream cpr = Log.class.getResourceAsStream("/log4j.xml");
			ConfigurationSource source = new ConfigurationSource(cpr);
			Configurator.initialize(null, source);
		} catch (IOException e) {
			try {
				Log.exception(e);
			} catch (Exception logException) {
				// TODO Auto-generated catch block
				System.err.println("Exception: " + e.getMessage());	
		}		}	
		XmlTest xmlParameters = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest();
		//PropertyConfigurator.configure("./src/main/resources/log4j.properties");
		File screenShotFolder = new File(Reporter.getCurrentTestResult().getTestContext().getOutputDirectory());
		screenShotFolderPath = screenShotFolder.getParent() + File.separator + "ScreenShot" + File.separator;
		screenShotFolder = new File(screenShotFolderPath);
		reportPortal = (xmlParameters.getParameter("reportPortal") != null) ? xmlParameters.getParameter("reportPortal") : "No";
		if (!screenShotFolder.exists()) {
			screenShotFolder.mkdir();
		}

		File[] screenShots = screenShotFolder.listFiles();
		screens = screenShotFolder;
		// delete files if the folder has any
		if (screenShots != null && screenShots.length > 0) {
			for (File screenShot : screenShots) {
				boolean deletedFile = screenShot.delete();
				if (!deletedFile) {
		            System.err.println("Failed to delete file: " + screenShot.getName());
		        }
			}
		}

		final Map<String, String> params = Reporter.getCurrentTestResult().getTestContext().getCurrentXmlTest().getAllParameters();

		if (params.containsKey("printconsoleoutput")) {
			Log.printconsoleoutput = Boolean.parseBoolean(params.get("printconsoleoutput"));
		}

	} // static block

	/**
	 * takeScreenShot will take the screenshot by sending driver as parameter in the
	 * log and puts in the screenshot folder
	 *
	 * depends on system variable isTakeScreenShot status, if true it will take the
	 * screenshot, else return the empty string
	 *
	 * @param driver
	 *            to take screenshot
	 */
	public static String takeScreenShot(WebDriver driver) {
		String inputFile = "";
		inputFile = Reporter.getCurrentTestResult().getName() + "_" + screenShotIndex.incrementAndGet() + ".png";
		ScreenshotManager.takeScreenshot(driver, screenShotFolderPath + inputFile);
		return inputFile;
	}

	/**
	 * getScreenShotHyperLink will convert the log status to hyper link
	 *
	 * depends on system variable isTakeScreenShot status, if true it will take the
	 * screenshot, else return the empty string
	 *
	 * @param inputFile
	 *            converts log status to hyper link
	 */
	public static String getScreenShotHyperLink(String inputFile) {
		String screenShotLink = "";
		screenShotLink = "<a href=\"." + File.separator + "ScreenShot" + File.separator + inputFile
				+ "\" target=\"_blank\" >[ScreenShot]</a>";
		return screenShotLink;
	}

	//    /**
	//     * addTestRunMachineInfo will get the information of Hub/Node, browser details
	//     * if executing through Grid
	//     * 
	//     * @param driver
	//     */
	//    public static void addTestRunMachineInfo(WebDriver driver, String sauceLink) {
	//    	Object params[] = Reporter.getCurrentTestResult().getParameters();
	//        String testMachine = "";
	//        String hub = "localhost";
	//        
	//        String userAgentString = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;");
	//        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);
	//        String osDetails =userAgent.getOperatingSystem().getName();
	//        
	//        if (System.getProperty("SELENIUM_DRIVER") != null || System.getenv("SELENIUM_DRIVER") != null) {
	//        	 if (params == null || params.length == 0) {
	//                 params = new Object[1];
	//             }
	//             params[0] = (params[0] == null || !params[0].toString().contains("SauceLab link"))
	//                     ? "SauceLab link: <a href='" + sauceLink + "' target='_blank'>" + sauceLink + "</a>"+ "<div id='browserandplatform' style='display:none'>" + ((RemoteWebDriver) driver).getCapabilities().getBrowserName() + "_"+ ((RemoteWebDriver) driver).getCapabilities().getVersion() + "_"+ osDetails + "_"+"</div>"
	//                     : params[0] + "<br>SauceLab link: <a href='" + sauceLink + "' target='_blank'>" + sauceLink + "</a>"+ "<div id='browserandplatform' style='display:none'>" + ((RemoteWebDriver) driver).getCapabilities().getBrowserName() + "_"+ ((RemoteWebDriver) driver).getCapabilities().getVersion() + "_"+ osDetails + "</div>";
	//             Reporter.getCurrentTestResult().setParameters(params);
	//             ExtentReporter.addAttribute("SauceLab link: <a href='" + sauceLink + "' target='_blank'>" + sauceLink + "</a>");
	//        }else {
	//        try {
	//            hub = (Reporter.getCurrentTestResult().getHost() == null) ? Inet4Address.getLocalHost().getHostName()
	//                    : Reporter.getCurrentTestResult().getHost();
	//        } catch (UnknownHostException e) {
	//            e.printStackTrace();
	//        }
	//        try {
	//            testMachine = "(Browser: " + ((RemoteWebDriver) driver).getCapabilities().getBrowserName() + ", Hub: " + hub
	//                    + ", Node: " + WebDriverFactory.getTestSessionNodeIP(driver).toUpperCase() + ")";
	//        } catch (Exception e) {
	//            e.printStackTrace();
	//        }
	//        if (params == null || params.length == 0) {
	//            params = new Object[1];
	//        }
	//        params[0] = testMachine + ", " + ((RemoteWebDriver) driver).getCapabilities().getBrowserName() + "_"
	//                + ((RemoteWebDriver) driver).getCapabilities().getPlatform() + "_"
	//                        + ((RemoteWebDriver) driver).getCapabilities().getVersion()+ "<div id='browserandplatform' style='display:none'>" + ((RemoteWebDriver) driver).getCapabilities().getBrowserName() + "_"+ ((RemoteWebDriver) driver).getCapabilities().getVersion() + "_"+ osDetails + "</div>";
	//        Reporter.getCurrentTestResult().setParameters(params);
	//        ExtentReporter.addAttribute(params[0].toString());        
	//        }        
	//    }

	/**
	 * lsLog4j returns name of the logger from the current thread
	 * 
	 */
	public static Logger lsLog4j() {
		return LogManager.getLogger(Thread.currentThread().getName());
	}

	/**
	 * callerClass method used to retrieve the Class Name
	 *
	 */
	public static String callerClass() {
		return Thread.currentThread().getStackTrace()[2].getClassName();
	}

	/**
	 * testCaseInfo method print the description of the test case in the log
	 * (level=info)
	 *
	 * @param description
	 *            test case
	 */
	public static void testCaseInfo(String description) {
		lsLog4j().info("");
		lsLog4j().log(Level.INFO, "****             " + description + "             *****");
		lsLog4j().info("");

		if (Reporter.getOutput(Reporter.getCurrentTestResult()).toString().contains("<div class=\"test-title\">")) {
			Reporter.log(TEST_TITLE_HTML_BEGIN + description + TEST_TITLE_HTML_END + "&emsp;");
		} else {
			Reporter.log(TEST_TITLE_HTML_BEGIN + description + TEST_TITLE_HTML_END + "<!-- Report -->&emsp;");
		}
		ExtentReporter.testCaseInfo(description);
	}

	/**
	 * testCaseInfo method print the description of the test case in the log
	 * (level=info)
	 *
	 * @param description
	 *            test case
	 */
	public static void testCaseInfo(String description, WebDriver driver) {
		lsLog4j().info("");
		lsLog4j().log(Level.INFO, "****             " + description + "             *****");
		lsLog4j().info("");
		if(configProperty.getProperty("runInLambdaTestTunnel").equalsIgnoreCase("true"))
			((JavascriptExecutor) driver).executeScript("lambda-name="+description);
		if (Reporter.getOutput(Reporter.getCurrentTestResult()).toString().contains("<div class=\"test-title\">")) {
			Reporter.log(TEST_TITLE_HTML_BEGIN + description + TEST_TITLE_HTML_END + "&emsp;");
		} else {
			Reporter.log(TEST_TITLE_HTML_BEGIN + description + TEST_TITLE_HTML_END + "<!-- Report -->&emsp;");
		}
		ExtentReporter.testCaseInfo(description);
	}
	public static void assertThatExtentReport(boolean status, String passmsg, String failmsg, WebDriver driver) {
		if (!status) {
			Log.fail(failmsg, driver);
			// extentReporterTest.log(LogStatus.FAIL, failmsg);
		} else {
			Log.messageStep(passmsg, driver);
			// extentReporterTest.log(LogStatus.INFO, passmsg);
		}
	}

	/**
	 * testCaseInfo method print the testdata of the test case in the log
	 * (level=info)
	 *
	 * @param testdata
	 *            test data
	 */
	public static void testCaseInfo(HashMap<String, String> testdata) {
		String description = testdata.containsKey("Description") ? testdata.get("Description").trim() : null;
		String bugId = testdata.containsKey("BugId") ? testdata.get("BugId") : null;
		String comments = testdata.containsKey("Comments") ? testdata.get("Comments") : null;
		lsLog4j().info("");
		lsLog4j().log(Level.INFO, "****             " + description + "             *****");
		lsLog4j().info("");

		if (Reporter.getOutput(Reporter.getCurrentTestResult()).toString().contains("<div class=\"test-title\">")) {
			if (bugId != null && comments != null)
				Reporter.log(TEST_TITLE_HTML_BEGIN + description + " BUGID:" + bugId + " COMMENTS:" + comments
						+ TEST_TITLE_HTML_END + "&emsp;");
			else if (bugId != null && comments == null)
				Reporter.log(TEST_TITLE_HTML_BEGIN + description + " BUGID:" + bugId + TEST_TITLE_HTML_END + "&emsp;");
			else if (bugId == null && comments != null)
				Reporter.log(
						TEST_TITLE_HTML_BEGIN + description + " COMMENTS:" + comments + TEST_TITLE_HTML_END + "&emsp;");
			else
				Reporter.log(TEST_TITLE_HTML_BEGIN + description + TEST_TITLE_HTML_END + "&emsp;");
		} else {
			if (bugId != null && comments != null)
				Reporter.log(TEST_TITLE_HTML_BEGIN + description + " BUGID:" + bugId + " COMMENTS:" + comments
						+ TEST_TITLE_HTML_END + "<!-- Report -->&emsp;");
			else if (bugId != null && comments == null)
				Reporter.log(TEST_TITLE_HTML_BEGIN + description + " BUGID:" + bugId + TEST_TITLE_HTML_END
						+ "<!-- Report -->&emsp;");
			else if (bugId == null && comments != null)
				Reporter.log(TEST_TITLE_HTML_BEGIN + description + " COMMENTS:" + comments + TEST_TITLE_HTML_END
						+ "<!-- Report -->&emsp;");
			else
				Reporter.log(TEST_TITLE_HTML_BEGIN + description + TEST_TITLE_HTML_END + "<!-- Report -->&emsp;");
		}
		ExtentReporter.testCaseInfo(description);
	}

	/**
	 * testConditionInfo method print the info of the test case in the log
	 *
	 * @param description
	 *            test case
	 */
	public static void testConditionInfo(String description) {
		Reporter.log(TEST_COND_HTML_BEGIN + description + TEST_COND_HTML_END);
	}

	/**
	 * testCaseResult method print the outputs Pass/Fail message in the test log as
	 * per the test step outcome
	 */
	public static void testCaseResult() {
		if (Reporter.getOutput(Reporter.getCurrentTestResult()).toString().contains("FAILSOFT")) {
			fail("Test Failed. Check the steps above in red color.");
		} else if (Reporter.getOutput(Reporter.getCurrentTestResult()).toString().contains("FAIL")) {
			fail("Test Failed. Check the steps above in red color.");
		} else {
			pass("Test Passed.");
		}
	}

	/**
	 * testCaseResult method print the outputs Pass/Fail message in the test log as
	 * per the test step outcome
	 */
	public static void testCaseResult(WebDriver driver) {
		if (Reporter.getOutput(Reporter.getCurrentTestResult()).toString().contains("FAILSOFT") ||
				Reporter.getOutput(Reporter.getCurrentTestResult()).toString().contains("FAIL")) {
			if(configProperty.getProperty("runInLambdaTestTunnel").equalsIgnoreCase("true"))
				((JavascriptExecutor) driver).executeScript("lambda-status=failed");
			fail("Test Failed. Check the steps above in red color.");
		} else {
			if(configProperty.getProperty("runInLambdaTestTunnel").equalsIgnoreCase("true"))
				((JavascriptExecutor) driver).executeScript("lambda-status=passed");
			pass("Test Passed.");
		}
	}

	/**
	 * endTestCase to print log in the console as a part of ending the test case
	 */
	public static void endTestCase() {
		lsLog4j().info("****             " + "-E---N---D-" + "             *****");
		ExtentReporter.endTest();
	}

	/**
	 * message print the test case custom message in the log (level=info)
	 *
	 * @param description
	 *            test case
	 */
	public static void message(String description) {
		Reporter.log(MESSAGE_HTML_BEGIN + description + MESSAGE_HTML_END);
		lsLog4j().log(Level.INFO, description);
		ExtentReporter.info(description);
		if (reportPortal.equalsIgnoreCase("YES"))
			ReportPortal.emitLog(description, "Debug", Calendar.getInstance().getTime());
	}

	/**
	 * message print the test case message in the log (level=info)
	 *
	 * @param description
	 *            test case
	 */
	public static void message1(String description) {
		Reporter.log(description);
		lsLog4j().log(Level.INFO, description);
		ExtentReporter.info(description);
	}


	/**
	 * message print the test case custom message in the log with screenshot
	 * (level=info)
	 *
	 * @param description
	 *            test case
	 * @param driver
	 *            to take screenshot
	 * @param takeScreenShot
	 *            [OPTIONAL] flag to override taking screenshot
	 */
	@Test
	public static void message(String description, WebDriver driver, Boolean... takeScreenShot) {
		boolean finalDecision = true;
		if (takeScreenShot.length > 0 && takeScreenShot[0].equals(Boolean.FALSE)) {
			finalDecision = false;
		}

		//  
		if (configProperty.getProperty("isTakeScreenShot") != null
				&& configProperty.getProperty("isTakeScreenShot").equalsIgnoreCase("false")) {
			finalDecision = false;
		}
		if (finalDecision) {
			String inputFile = takeScreenShot(driver);
			if (reportPortal.equalsIgnoreCase("YES"))
				ReportPortal.emitLog(description, "Debug", Calendar.getInstance().getTime(), new File(screenShotFolderPath + File.separator + inputFile));
			Reporter.log(
					MESSAGE_HTML_BEGIN + description + "&emsp;" + getScreenShotHyperLink(inputFile) + MESSAGE_HTML_END);
			ExtentReporter.info(description + " " + getScreenShotHyperLink(inputFile));
		} else {
			Reporter.log(MESSAGE_HTML_BEGIN + description + MESSAGE_HTML_END);
			ExtentReporter.info(description);
		}
		lsLog4j().log(Level.INFO, description);
	}



	/**
	 * message print test case description along with hyper link provided to test
	 * case status linked to respective screenshot (level=info)
	 *
	 * @param driver
	 * @param description
	 * @param screenshotfolderpath
	 * @param screenshotfileName
	 * @throws IOException
	 */
	public static void message(WebDriver driver, String description, String screenshotFolderPath,
			String screenshotFileName) throws IOException {

		String inputFile = screenshotFileName + ".png";
		WebDriver augmented = new Augmenter().augment(driver);
		File screenshot = ((TakesScreenshot) augmented).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshot, new File(screenshotFolderPath + File.separator + inputFile));
		FileUtils.copyFile(screenshot, new File(screenShotFolderPath + File.separator + inputFile));
		Reporter.log(MESSAGE_HTML_BEGIN + String.format("%-75s", description).replace(" ", "&nbsp;") + "---&emsp;"
				+ getScreenShotHyperLink(inputFile) + MESSAGE_HTML_END);
		ExtentReporter.info(String.format("%-75s", description).replace(" ", "&nbsp;") + "---&emsp;"
				+ getScreenShotHyperLink(inputFile));
		lsLog4j().log(Level.INFO, description);
	}

	/**
	 * buildLogMessage print test case description along with hyper link provided to
	 * test case status linked to respective screenshot
	 *
	 * @param driver
	 * @param description
	 * @param screenshotfolderpath
	 * @param screenshotfileName
	 * @throws IOException
	 */
	public static String buildLogMessage(WebDriver driver, String description, String screenshotFolderPath,
			String screenshotFileName) throws IOException {
		String inputFile = screenshotFileName + ".png";
		WebDriver augmented = new Augmenter().augment(driver);
		File screenshot = ((TakesScreenshot) augmented).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(screenshot, new File(screenshotFolderPath + File.separator + inputFile));
		FileUtils.copyFile(screenshot, new File(screenShotFolderPath + inputFile));
		return MESSAGE_HTML_BEGIN + String.format("%-75s", description).replace(" ", "&nbsp;") + "---&emsp;"
		+ getScreenShotHyperLink(inputFile) + MESSAGE_HTML_END;
	}



	/**
	 * message print custom test case status message with screenshot (level=info)
	 *
	 * @param description
	 *            custom message in the test case
	 * @param screenshot
	 *            to take screenshot
	 * @throws IOException
	 */
	public static void message(String description, String screenshot) throws IOException {
		try {
			String inputFile = Reporter.getCurrentTestResult().getName() + "_" + screenShotIndex.incrementAndGet()
			+ ".png";
			FileUtils.copyFile(new File(screenshot), new File(screenShotFolderPath + inputFile));
			Reporter.log(MESSAGE_HTML_BEGIN + description + getScreenShotHyperLink(inputFile) + MESSAGE_HTML_END);
			ExtentReporter.info(description + " " + getScreenShotHyperLink(inputFile));
			lsLog4j().log(Level.INFO, description);
		} catch (FileNotFoundException e) {
			Reporter.log(MESSAGE_HTML_BEGIN + description + MESSAGE_HTML_END);
			ExtentReporter.info(description);
		}
	}

	/**
	 * event print the page object custom message in the log which can be seen
	 * through short cut keys used during debugging (level=info)
	 *
	 * @param description
	 *            test case
	 */
	public static void event(String description) {
		String currDate = new SimpleDateFormat("dd MMM HH:mm:ss SSS").format(Calendar.getInstance().getTime());
		Reporter.log(EVENT_HTML_BEGIN + currDate + " - " + description + EVENT_HTML_END);
		lsLog4j().log(Level.DEBUG, description);
		ExtentReporter.debug(description);
	}

	/**
	 * event print the page object custom message in the log which can be seen
	 * through short cut keys used during debugging along with duration of the
	 * particular action (level=debug)
	 *
	 * @param description
	 *            test case
	 * @param duration
	 *            to print the time taken
	 */
	public static void event(String description, long duration) {
		String currDate = new SimpleDateFormat("dd MMM HH:mm:ss SSS").format(Calendar.getInstance().getTime());
		Reporter.log(EVENT_HTML_BEGIN + currDate + " - <b>" + duration + "</b> - " + description + " - "
				+ Thread.currentThread().getStackTrace()[2].toString() + EVENT_HTML_END);
		lsLog4j().log(Level.DEBUG, description);
		ExtentReporter.debug(currDate + " - <b>" + duration + "</b> - " + description + " - "
				+ Thread.currentThread().getStackTrace()[2].toString());
	}

	/**
	 * trace print the page object custom message in the log which can be seen
	 * through short cut keys used during debugging (level=info)
	 *
	 * @param description
	 *            test case
	 */
	public static void trace(String description) {
		String currDate = new SimpleDateFormat("dd MMM HH:mm:ss SSS").format(Calendar.getInstance().getTime());
		Reporter.log(TRACE_HTML_BEGIN + currDate + " - " + description + TRACE_HTML_END);
		lsLog4j().log(Level.TRACE, description);

	}

	/**
	 * trace print the page object custom message in the log which can be seen
	 * through short cut keys used during debugging along with duration of the
	 * particular action (level=debug)
	 *
	 * @param description
	 *            test case
	 * @param duration
	 *            to print the time taken
	 */
	public static void trace(String description, long duration) {
		String currDate = new SimpleDateFormat("dd MMM HH:mm:ss SSS").format(Calendar.getInstance().getTime());
		Reporter.log(TRACE_HTML_BEGIN + currDate + " - <b>" + duration + "</b> - " + description + " - "
				+ Thread.currentThread().getStackTrace()[2].toString() + TRACE_HTML_END);
		lsLog4j().log(Level.TRACE, description);

	}

	/**
	 * pass print test case status as Pass with custom message (level=info)
	 *
	 * @param description
	 *            custom message in the test case
	 */
	public static void pass(String description) {
		Reporter.log(PASS_HTML_BEGIN + description + PASS_HTML_END1 + PASS_HTML_END2);
		lsLog4j().log(Level.INFO, description);
		ExtentReporter.pass(description);
		if (reportPortal.equalsIgnoreCase("YES"))
			ReportPortal.emitLog(description, "Info", Calendar.getInstance().getTime());
	}

	/**
	 * pass print test case status as Pass with custom message and take screenshot
	 * (level=info)
	 *
	 * @param description
	 *            custom message in the test case
	 * @param driver
	 *            to take screenshot
	 */
	public static void pass(String description, WebDriver driver) {
		String inputFile = "";
		if (configProperty.getProperty("isTakeScreenShot") != null
				&& configProperty.getProperty("isTakeScreenShot").equalsIgnoreCase("true")) {
			inputFile = takeScreenShot(driver);
			Reporter.log(PASS_HTML_BEGIN + description + PASS_HTML_END1 + getScreenShotHyperLink(inputFile)
			+ PASS_HTML_END2);
			ExtentReporter.pass(description + " " + getScreenShotHyperLink(inputFile));
		} else {
			Reporter.log(PASS_HTML_BEGIN + description + PASS_HTML_END1 + PASS_HTML_END2);
			ExtentReporter.pass(description);
		}
		if (reportPortal.equalsIgnoreCase("YES"))
			ReportPortal.emitLog(description, "Info", Calendar.getInstance().getTime(), new File(screenShotFolderPath + File.separator + inputFile));
		lsLog4j().log(Level.INFO, description);
	}

	/**
	 * fail print test case status as Fail with custom message and take screenshot
	 * (level=error)
	 *
	 * @param description
	 *            custom message in the test case
	 * @param driver
	 *            to take screenshot
	 */
	public static void fail(String description, WebDriver driver) {
		String inputFile = takeScreenShot(driver);
		Reporter.log("<!--FAIL-->");
		Reporter.log(
				FAIL_HTML_BEGIN + description + FAIL_HTML_END1 + getScreenShotHyperLink(inputFile) + FAIL_HTML_END2);
		ExtentReporter.fail(description + " " + getScreenShotHyperLink(inputFile));
		ExtentReporter.logStackTrace(new AssertionError(description));
		lsLog4j().log(Level.ERROR, description);
		if (reportPortal.equalsIgnoreCase("YES"))
			ReportPortal.emitLog(description, "Error", Calendar.getInstance().getTime(), new File(screenShotFolderPath + File.separator + inputFile));
		if(configProperty.getProperty("runInLambdaTestTunnel").equalsIgnoreCase("true"))
			((JavascriptExecutor) driver).executeScript("lambda-status=failed");
		Assert.fail(description);
	}

	/**
	 * fail print test case status as Fail with custom message (level=error)
	 *
	 * @param description
	 *            custom message in the test case
	 */
	public static void fail(String description) {
		lsLog4j().log(Level.ERROR, description);
		Reporter.log("<!--FAIL-->");
		Reporter.log(FAIL_HTML_BEGIN + description + FAIL_HTML_END1 + FAIL_HTML_END2);
		ExtentReporter.fail(description);
		ExtentReporter.logStackTrace(new AssertionError(description));
		Assert.fail(description);
		if (reportPortal.equalsIgnoreCase("YES"))
			ReportPortal.emitLog(description, "Error", Calendar.getInstance().getTime());
	}

	/**
	 * hasFailSofts returns true if the test steps contains any fail
	 *
	 * @return boolean
	 */
	public static boolean hasFailSofts() {
		return Reporter.getOutput(Reporter.getCurrentTestResult()).toString().contains("FAILSOFT");
	}

	/**
	 * failsoft print test case step failure as fail with screenshot and let
	 * execution continue (level=error)
	 *
	 * @param description
	 *            custom message in the test case
	 * @param driver
	 *            to take screenshot
	 */
	public static void failsoft(String description, WebDriver driver) {
		String inputFile = takeScreenShot(driver);
		Reporter.log("<div class=\"test-result\"><font color=\"red\">&emsp;" + description + "</font>"
				+ getScreenShotHyperLink(inputFile) + "</div>");
		Reporter.log("<!--FAILSOFT-->");
		ExtentReporter.fail(description + " " + getScreenShotHyperLink(inputFile));
		lsLog4j().log(Level.ERROR, description);
	}

	/**
	 * failsoft print test case step failure as fail and let execution continue
	 * (level=error)
	 *
	 * @param description
	 *            custom message in the test case
	 */
	public static void failsoft(String description) {
		Reporter.log("<!--FAILSOFT-->");
		Reporter.log("<div class=\"test-result\"><font color=\"red\">&emsp;" + description + "</font></div>");
		ExtentReporter.fail(description);
		lsLog4j().log(Level.ERROR, description);
	}

	/**
	 * failsoft print test case step failure as fail in red color with screenshot
	 * (level=error)
	 *
	 * @param description
	 *            custom message in the test case
	 * @param driver
	 *            to take screenshot
	 */
	public static String buildfailsoftMessage(String description, WebDriver driver) {
		String inputFile = takeScreenShot(driver);
		return "<div class=\"test-result\">&emsp; <font color=\"red\"><strong>" + description + " </strong> </font>"
		+ getScreenShotHyperLink(inputFile) + "</div>&emsp;";
	}

	/**
	 * exception prints the exception message as fail/skip in the log with
	 * screenshot (level=fatal)
	 *
	 * @param e
	 *            exception message
	 * @param driver
	 *            to take screenshot
	 * @throws Exception
	 */
	public static void exception(Exception e, WebDriver driver) throws Exception {

		String screenShotLink = "";
		String inputFile = "";
		try {
			inputFile = takeScreenShot(driver);
			screenShotLink = getScreenShotHyperLink(inputFile);
		} catch (Exception ex) {

		}
		String eMessage = e.getMessage();
		if (eMessage != null && eMessage.contains("\n")) {
			eMessage = eMessage.substring(0, eMessage.indexOf("\n"));
		}
		lsLog4j().log(Level.FATAL, eMessage, e);
		if (e instanceof SkipException) {
			Reporter.log(SKIP_EXCEPTION_HTML_BEGIN + eMessage + SKIP_HTML_END1 + screenShotLink + SKIP_HTML_END2);
			ExtentReporter.skip(eMessage + " " + screenShotLink);
			ExtentReporter.logStackTrace(e);
			if (reportPortal.equalsIgnoreCase("YES"))
			ReportPortal.emitLog(Arrays.toString(e.getStackTrace()), "Fatal", Calendar.getInstance().getTime(), new File(screenShotFolderPath + File.separator + inputFile));
		} else {
			Reporter.log("<!--FAIL-->");
			Reporter.log(FAIL_HTML_BEGIN + eMessage + FAIL_HTML_END1 + screenShotLink + FAIL_HTML_END2);
			ExtentReporter.fail(eMessage + " " + screenShotLink);
			ExtentReporter.logStackTrace(e);
			if (reportPortal.equalsIgnoreCase("YES"))
			    ReportPortal.emitLog(Arrays.toString(e.getStackTrace()), "Fatal", Calendar.getInstance().getTime(), new File(screenShotFolderPath + File.separator + inputFile));
		}
		if(configProperty.getProperty("runInLambdaTestTunnel").equalsIgnoreCase("true"))
			((JavascriptExecutor) driver).executeScript("lambda-status=failed");
		throw e;
	}

	/**
	 * exception prints the exception message as fail/skip in the log (level=fatal)
	 *
	 * @param e
	 *            exception message
	 * @throws Exception
	 */
	public static void exception(Exception e) throws Exception {
		String eMessage = e.getMessage();

		if (eMessage != null && eMessage.contains("\n")) {
			eMessage = eMessage.substring(0, eMessage.indexOf("\n"));
		}
		lsLog4j().log(Level.FATAL, eMessage, e);
		if (e instanceof SkipException) {
			Reporter.log(SKIP_EXCEPTION_HTML_BEGIN + eMessage + SKIP_HTML_END1 + SKIP_HTML_END2);
			ExtentReporter.skip(eMessage);
			ExtentReporter.logStackTrace(e);
			if (reportPortal.equalsIgnoreCase("YES"))
			    ReportPortal.emitLog(Arrays.toString(e.getStackTrace()), "Fatal", Calendar.getInstance().getTime());

		} else {
			Reporter.log("<!--FAIL-->");
			Reporter.log(FAIL_HTML_BEGIN + eMessage + FAIL_HTML_END1 + FAIL_HTML_END2);
			ExtentReporter.fail(eMessage);
			ExtentReporter.logStackTrace(e);
			if (reportPortal.equalsIgnoreCase("YES"))
			    ReportPortal.emitLog(Arrays.toString(e.getStackTrace()), "Fatal", Calendar.getInstance().getTime());
		
		}
		throw e;
	}

	/**
	 * Asserts that a condition is true or false, depends upon the status. Then it
	 * will print the verified message if status is true, else stop the script and
	 * print the failed message
	 *
	 * @param b
	 *            - boolean or expression returning boolean
	 * @param passmsg
	 *            -message to be logged when assert status is true
	 * @param failmsg
	 *            -message to be logged when assert status is false
	 */
	public static void assertThat(boolean b, String string, String failMsg) {
		if (!b) {
			Log.fail(failMsg);
		} else {
			Log.message(string);
		}
	}

	/**
	 * Asserts that a condition is true or false, depends upon the status. Then it
	 * will print the verified message with screen shot if status is true, else stop
	 * the script and print the failed message with screen shot
	 *
	 * @param status
	 *            - boolean or expression returning boolean
	 * @param passmsg
	 *            -message to be logged when assert status is true
	 * @param failmsg
	 *            -message to be logged when assert status is false
	 * @param driver
	 *            - WebDriver, using this driver will taking the screen shot and
	 *            mapping to log report
	 */
	public static void assertThat(boolean status, String passMsg, String failMsg, WebDriver driver) {
		if (!status) {
			Log.fail(failMsg, driver);
		} else {
			Log.messageStep(passMsg, driver);
		}
	}

	/**
	 * Asserts that a condition is true or false, depends upon the status. Then it
	 * will print the verified message if status is true, else print the failed
	 * message in red color and continue the next step(not stopping/breaking the
	 * test script)
	 *
	 * @param status
	 *            - boolean or expression returning boolean
	 * @param passmsg
	 *            -message to be logged when assert status is true
	 * @param failmsg
	 *            -message to be logged when assert status is false
	 */
	public static void softAssertThat(boolean status, String passMsg, String failMsg) {
		if (!status) {
			Log.failsoft(failMsg);
		} else {
			Log.message(passMsg);
		}
	}

	/**
	 * Asserts that a condition is true or false, depends upon the status. Then it
	 * will print the verified message with screen shot if status is true, else
	 * print the failed message in red color with screen shot and continue the next
	 * step(not stopping/breaking the test script)
	 *
	 * @param status
	 *            - boolean or expression returning boolean
	 * @param passmsg
	 *            -message to be logged when assert status is true
	 * @param failmsg
	 *            -message to be logged when assert status is false
	 * @param driver
	 *            - WebDriver, using this driver will taking the screen shot and
	 *            mapping to log report
	 */
	public static void softAssertThat(boolean status, String passMsg, String failMsg, WebDriver driver) {
		if (!status) {
			Log.failsoft(failMsg, driver);
		} else {
			Log.messageStep(passMsg, driver);
		}
	}

	/**
	 * addSauceJobUrlToReportÃ‚Â will add the sauce lab job URL( video, selenium log,
	 * commands and meta data details)
	 *
	 * @param driver
	 *            - web driver object
	 *
	 * @param sauceLink
	 *            - corresponding test's sauce lab job URL
	 */
	public static void addSauceJobUrlToReport(WebDriver driver, String sauceLink) {
		Object params[] = Reporter.getCurrentTestResult().getParameters();
		if (params == null || params.length == 0) {
			params = new Object[1];
		}
		params[0] = (params[0] == null || !params[0].toString().contains("SauceLab link"))
				? "SauceLab link: <a href='" + sauceLink + "' target='_blank'>" + sauceLink + "</a>"
						: params[0] + "<br>SauceLab link: <a href='" + sauceLink + "' target='_blank'>" + sauceLink + "</a>";
		Reporter.getCurrentTestResult().setParameters(params);
		ExtentReporter.addAttribute("SauceLab link: <a href='" + sauceLink + "' target='_blank'>" + sauceLink + "</a>");
	}

	public static void addExecutionEnvironmentToReport(WebDriver driver, String browserAndPlatform) {
		Object[] obj=browserAndPlatform.split(",");
		Reporter.getCurrentTestResult().setParameters(obj);
	}

	/**
	 * pass print test case status as Pass with custom message (level=info)
	 *
	 * @param description
	 *            custom message in the test case
	 */
	public static void performaceReport(String script) {
		Reporter.log(PERFORMANCE_HTML_BEGIN + script + PERFORMANCE_HTML_END);
		ExtentReporter.addAttribute(PERFORMANCE_HTML_BEGIN + script + PERFORMANCE_HTML_END);
	}

	public static void addLambdaJobUrlToReport(WebDriver driver, String LambdaLink) {
		Object params[] = Reporter.getCurrentTestResult().getParameters();
		if (params == null || params.length == 0) {
			params = new Object[1];
		}
		params[0] = (params[0] == null || !params[0].toString().contains("LambdaTest link"))
				? "LambdaTest link: <a href='" + LambdaLink + "' target='_blank'>" + LambdaLink + "</a>"
						: params[0] + "<br>LambdaTest link: <a href='" + LambdaLink + "' target='_blank'>" + LambdaLink + "</a>";
		Reporter.getCurrentTestResult().setParameters(params);
		ExtentReporter.addAttribute("LambdaTest link: <a href='" + LambdaLink + "' target='_blank'>" + LambdaLink + "</a>");
	}


	/**
	 * message print the test case custom message in the log with screenshot
	 * (level=info)
	 *
	 * @param description
	 *            test case
	 * @param driver
	 *            to take screenshot
	 * @param takeScreenShot
	 *            [OPTIONAL] flag to override taking screenshot
	 */
	@Test
	public static void messageStep(String description, WebDriver driver, Boolean... takeScreenShot) {
		int stepNumber = steps.get();
		stepNumber++;
		steps.set(stepNumber);
		boolean finalDecision = true;
		if (takeScreenShot.length > 0 && takeScreenShot[0].equals(Boolean.FALSE)) {
			finalDecision = false;
		}

		if (configProperty.getProperty("isTakeScreenShot") != null
				&& configProperty.getProperty("isTakeScreenShot").equalsIgnoreCase("false")) {
			finalDecision = false;
		}
		if (finalDecision) {
			String inputFile = takeScreenShot(driver);
			if (reportPortal.equalsIgnoreCase("YES"))
				ReportPortal.emitLog(description, "Debug", Calendar.getInstance().getTime(), new File(screenShotFolderPath + File.separator + inputFile));
			Reporter.log(
					MESSAGE_HTML_BEGIN + description + "&emsp;" + getScreenShotHyperLink(inputFile) + MESSAGE_HTML_END);
			ExtentReporter.infoStep(description + " " + getScreenShotHyperLink(inputFile),steps.get());
		} else {
			Reporter.log(MESSAGE_HTML_BEGIN + description + MESSAGE_HTML_END);
			ExtentReporter.infoStep(description,steps.get());
		}
		lsLog4j().log(Level.INFO, description);
	}

} // Log
