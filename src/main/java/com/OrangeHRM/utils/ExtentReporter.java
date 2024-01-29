package com.OrangeHRM.utils;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * 
 * ExtentReports Generator (Works with @Listeners(EmailReport.class))
 *
 */
@Test
public class ExtentReporter {
	
	private static ExtentReports report = null;
	private static HashMap<Integer, ExtentTest> tests = new HashMap<Integer, ExtentTest>();
	private static File configFile = new File(System.getProperty("user.dir") + File.separator + "ReportConfig.xml");
	private static boolean isReportClosed = false;

	/**
	 * To form a unique test name in the format "PackageName.ClassName#MethodName"
	 * 
	 * @param iTestResult
	 * @return String - test name
	 */
	private static String getTestName(ITestResult iTestResult) {
		String testClassName = iTestResult.getTestClass().getRealClass().getName();
		String testMethodName = iTestResult.getName();
		return testClassName + "#" + testMethodName;
	}

	
	/**
	 * To convert milliseconds to Date object
	 * 
	 * @param millis
	 * @return Date
	 */
	private static Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}

	/**
	 * To set run status for interrupted tests
	 * 
	 * @param iTestResult
	 * @param extentTest
	 */
	private static void setInterruptedTestStatus(ITestResult iTestResult, ExtentTest extentTest) {
		if (!(extentTest.getRunStatus().equals(LogStatus.UNKNOWN)
				|| extentTest.getRunStatus().equals(LogStatus.PASS))) {
			return;
		}
		switch (iTestResult.getStatus()) {
		case 2:
			if (iTestResult.getThrowable() == null)
				extentTest.log(LogStatus.FAIL, "<font color=\"red\">Test Failed</font>");
			else
				extentTest.log(LogStatus.FAIL, "<div class=\"stacktrace\">"
						+ ExceptionUtils.getStackTrace(iTestResult.getThrowable()) + "</div>");
			break;
		case 3:
			if (iTestResult.getThrowable() == null)
				extentTest.log(LogStatus.SKIP, "<font color=\"orange\">Test Skipped</font>");
			else
				extentTest.log(LogStatus.SKIP, "<div class=\"stacktrace\">"
						+ ExceptionUtils.getStackTrace(iTestResult.getThrowable()) + "</div>");
			break;
		}
	}

	/**
	 * Returns an ExtentReports instance if already exists. Creates new and returns
	 * otherwise.
	 * 
	 * @param iTestResult
	 * @return {@link ExtentReports} - Extent report instance
	 */
	private static synchronized ExtentReports getReportInstance(ITestResult iTestResult) {
		if (report == null) {
			String reportFilePath = new File(iTestResult.getTestContext().getOutputDirectory()).getParent()
					+ File.separator + "ExtentReport.html";
			report = new ExtentReports(reportFilePath, true);
			if (configFile.exists()) {
				report.loadConfig(configFile);
			}
		}
		return report;
	}

	/**
	 * To start and return a new extent test instance with given test case
	 * description. Returns the test instance if the test has already been started
	 * 
	 * @param description - test case description
	 * @return {@link ExtentTest} - ExtentTest Instance
	 */
	private static ExtentTest startTest(String description) {
		ExtentTest test = null;
		ITestResult iTestResult = Reporter.getCurrentTestResult();
		String testName = iTestResult != null ? getTestName(iTestResult) : Thread.currentThread().getName();
		Integer hashCode = iTestResult != null ? iTestResult.hashCode() : Thread.currentThread().hashCode();
		if (tests.containsKey(hashCode)) {
			test = tests.get(hashCode);
			if (description != null && !description.isEmpty()) {
				test.setDescription(description);
			}
		} else {
			if (iTestResult == null || !iTestResult.getMethod().isTest()) {
				test = new ExtentTest(testName, description);
			} else {
				test = getReportInstance(iTestResult).startTest(testName, description)
						.assignCategory(iTestResult.getMethod().getGroups());
				tests.put(hashCode, test);
			}
		}
		return test;
	}

	/**
	 * Returns the test instance if the test has already been started. Else creates
	 * a new test with empty description
	 * 
	 * @return {@link ExtentTest} - ExtentTest Instance
	 */
	private static ExtentTest getTest() {
		return startTest("");
	}

	/**
	 * To start a test with given test case info
	 * 
	 * @param testCaseInfo
	 */
	public static void testCaseInfo(String testCaseInfo) {
		startTest("<strong><font size = \"4\" color = \"#000080\">" + testCaseInfo + "</font></strong>");
	}

	/**
	 * To log the given message to the reporter at INFO level
	 * 
	 * @param message
	 */
	
	public static void info(String message) {
	    getTest().log(LogStatus.INFO, message);
	}
	

	/**
	 * To log the given message to the reporter at DEBUG level
	 * 
	 * @param event
	 */
	public static void debug(String event) {
		getTest().log(LogStatus.UNKNOWN, event);
	}

	/**
	 * To log the given message to the reporter at PASS level
	 * 
	 * @param passMessage
	 */
	public static void pass(String passMessage) {
		getTest().log(LogStatus.PASS, "<font color=\"green\">" + passMessage + "</font>");
	}

	/**
	 * To log the given message to the reporter at FAIL level
	 * 
	 * @param failMessage
	 */
	public static void fail(String failMessage) {
		getTest().log(LogStatus.FAIL, "<font color=\"red\">" + failMessage + "</font>");
	}

	/**
	 * To log the given message to the reporter at SKIP level
	 * 
	 * @param message
	 */
	public static void skip(String message) {
		getTest().log(LogStatus.SKIP, "<font color=\"orange\">" + message + "</font>");
	}

	/**
	 * To print the stack trace of the given error/exception
	 * 
	 * @param t
	 */
	public static void logStackTrace(Throwable t) {
		if (t instanceof SkipException) {
			getTest().log(LogStatus.SKIP, "<div class=\"stacktrace\">" + ExceptionUtils.getStackTrace(t) + "</div>");
		} else {
			getTest().log(LogStatus.FAIL, "<div class=\"stacktrace\">" + ExceptionUtils.getStackTrace(t) + "</div>");
		}
	}

	/**
	 * To add attributes to a extent test instance
	 * 
	 * @param attribs
	 */
	public static void addAttribute(String... attribs) {
		getTest().assignAuthor(attribs);
	}

	/**
	 * To end an extent test instance
	 */
	public static void endTest() {
		getReportInstance(Reporter.getCurrentTestResult()).endTest(getTest());
	}

	/**
	 * To change the test run status to SKIP (to be used with retry analyzer)
	 * 
	 * @param result
	 */
	public static void setTestStatusAsSkip(ITestResult result) {
		try {
			ExtentTest test = tests.get(result.hashCode());
			test.getTest().getLogList().forEach(log -> {
				if (log.getLogStatus() == LogStatus.ERROR || log.getLogStatus() == LogStatus.FAIL
						|| log.getLogStatus() == LogStatus.FATAL) {
					log.setLogStatus(LogStatus.SKIP);
				}
			});
			test.getTest().setStatus(LogStatus.SKIP);
		} catch (Exception e) {
			try {
				Log.exception(e);
			} catch (Exception logException) {
				// TODO Auto-generated catch block
				System.err.println("Exception: " + e.getMessage());	
		}		}
	}

	/**
	 * To flush and close the report instance
	 * 
	 * @param allTestCaseResults
	 * @param outdir
	 */
	public static void closeReport(List<ITestResult> allTestCaseResults, String outdir) {
		if (isReportClosed) {
			return;
		}
		if (report == null && allTestCaseResults.size() > 0) {
			getReportInstance(allTestCaseResults.get(0));
		} else if (report == null && allTestCaseResults.size() == 0) {
			report = new ExtentReports(outdir + File.separator + "ExtentReport.html", true);
			if (configFile.exists()) {
				report.loadConfig(configFile);
			}
			report.endTest(report.startTest("Empty TestNG Suite",
					"To run tests, please add '@Test' annotation to your test methods"));
		}
		if (report != null) {
			String testName = null;
			ExtentTest extentTest = null;
			Integer hashCode = 0;
			for (ITestResult iTestResult : allTestCaseResults) {
				testName = getTestName(iTestResult);
				hashCode = iTestResult.hashCode();
				if (!tests.containsKey(hashCode)) {
					extentTest = report.startTest(testName, iTestResult.getMethod().getDescription() == null ? ""
							: iTestResult.getMethod().getDescription());
					extentTest.setStartedTime(getTime(iTestResult.getStartMillis()));
					extentTest.assignCategory(iTestResult.getMethod().getGroups());
					List<String> output = Reporter.getOutput(iTestResult);
					for (String step : output) {
						if (step.contains("test-message")) {
							extentTest.log(LogStatus.INFO, step);
						} else {
							extentTest.log(LogStatus.UNKNOWN, step);
						}
					}
					setInterruptedTestStatus(iTestResult, extentTest);
					extentTest.setEndedTime(getTime(iTestResult.getEndMillis()));
					tests.put(hashCode, extentTest);
				} else {
					extentTest = tests.get(hashCode);
					if (extentTest.getEndedTime() == null) {
						setInterruptedTestStatus(iTestResult, extentTest);
						extentTest.setEndedTime(getTime(iTestResult.getEndMillis()));
					}
				}
			}
			for (ExtentTest eTest : tests.values()) {
				report.endTest(eTest);
			}
			report.flush();
			isReportClosed = true;
			report.close();
		}
	}
	
	/**
	 * To log the given message to the reporter at INFO level
	 * 
	 * @param message
	 */
	
	public static void infoStep(String message, int steps) {
	    getTest().log(LogStatus.INFO, null, message);
	    //getTest().log(LogStatus.INFO, "Step " + steps + ": " + message);
	}


}