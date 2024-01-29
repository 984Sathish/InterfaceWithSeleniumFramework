package com.OrangeHRM.utils;

import static java.util.Objects.isNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Test;
import org.testng.xml.XmlSuite;

/**
 * This class will help to view real time execution report during script execution.
 * Add this listener to testng.xml or at class level to view the report.
 * HtmlCssDesignForRealTimeExecutionReport.css in src/main/resources will be utilized to format the report
 *
 */
public class RealTimeExecutionListener implements ITestListener, IAnnotationTransformer {

	static File realTimeExecutionReport = null;
	static File rawJsonTestResultsFile = null;
	private static String htmlCssDesignStart = null;
	private static String htmlCssDesignEnd = null;
	private static Date startTime = null;
	private static Date lastModifiedTime = null;
	private static String strStartTime = null;
	private static long longStartTime = 0;
	private static long longLastModifiedTime = 0;
	private static long totalTimeTaken = 0;
	private static String lastModified = null;
	private static int liveTestSuitesCount = 0;
	private static int running = 0;
	private static int passed = 0;
	private static int failed = 0;
	private static int skipped = 0;
	private static int total = 0;
	static int noOfMethods = 0;
	static int remaining = 0;
	static String startTestTitle = "<div class=\"test-title\"> <strong><font size = \"3\" color = \"#000080\">";
	static String endTestTitle = "</font> </strong></div>&emsp;<div><strong>Steps:</strong>";
	static String startTestCaseResult = "<div class=\"test-result\"><br><font color=\"red\"><strong>";
	static String endTestCaseResult = "</strong></font>";
	static JSONObject dynamicTestResults = new JSONObject();
	private static JSONObject classSuiteWiseResultJSONObject = new JSONObject();
	private static JSONObject moduleWiseResultJSONObject = new JSONObject();
	private static JSONArray completedTestsJSONArray = new JSONArray();
	private static JSONArray runningTestsJSONArray = new JSONArray();
	static JSONObject exceptionListJSONObject = new JSONObject();
	static JSONObject exceptionRootCauseJSONObject = new JSONObject();
//	private static final DateFormat df = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
	private static String htmlCssDesignFilePath = System.getProperty("user.dir")
			+ "/src/main/resources/HtmlCssDesignForRealTimeExecutionReport.txt";

	@Override
	public void onTestStart(ITestResult result) {
		try {
			writeReport("Running", true, result, new ArrayList<String>());
		} catch (Exception e) {
			try {
				Log.exception(e);
			} catch (Exception logException) {
				// TODO Auto-generated catch block
				System.err.println("Exception: " + e.getMessage());	
		}		}
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		try {
			Object params[] = Reporter.getCurrentTestResult().getParameters();
			if (params.length == 0) {
				writeReport("Pass", true, result, new ArrayList<String>());
				return;
			}
			if (params[0] instanceof String) {
				String bsUnformattedUrl = (String) params[0];
				ArrayList<String> extractedUrls = extractUrls(bsUnformattedUrl);
				writeReport("Pass", true, result, extractedUrls);
			}
		} catch (Exception e) {
			try {
				Log.exception(e);
			} catch (Exception logException) {
				// TODO Auto-generated catch block
				System.err.println("Exception: " + e.getMessage());	
		}		}
	}

	@Override
	public void onTestFailure(ITestResult result) {
		try {
			Object params[] = Reporter.getCurrentTestResult().getParameters();
			if (params.length == 0) {
				writeReport("Fail", true, result, new ArrayList<String>());
				return;
			}
			if (params[0] instanceof String) {
				String bsUnformattedUrl = (String) params[0];
				ArrayList<String> extractedUrls = extractUrls(bsUnformattedUrl);
				if (result.getMethod().getRetryAnalyzer(result) != null) {
					writeReport("Fail", true, result, extractedUrls, true);
				} else {
					writeReport("Fail", true, result, extractedUrls);
				}
			}
		} catch (Exception e) {
			try {
				Log.exception(e);
			} catch (Exception logException) {
				// TODO Auto-generated catch block
				System.err.println("Exception: " + e.getMessage());	
		}		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		try {
			ArrayList<String> bsUrlList = new ArrayList<String>();
			writeReport("Skip", true, result, bsUrlList);
		} catch (Exception e) {
			try {
				Log.exception(e);
			} catch (Exception logException) {
				// TODO Auto-generated catch block
				System.err.println("Exception: " + e.getMessage());	
		}		}
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
	}

	@Override
	public void onStart(ITestContext context) {
		initRealTimeExecutionReport(context);
	}

	@Override
	public void onFinish(ITestContext context) {
		try {
			liveTestSuitesCount++;
			XmlSuite suite = context.getSuite().getXmlSuite();
			if (liveTestSuitesCount == suite.getTests().size()) {
				writeReport("OnFinish", false, null, new ArrayList<String>());
			}
			if (rawJsonTestResultsFile == null) {
				File executionResultFolder = new File(context.getOutputDirectory());
				String executionResultFolderPath = executionResultFolder.getParent() + File.separator
						+ "RawJSONTestResults.json";
				rawJsonTestResultsFile = new File(executionResultFolderPath);

				try {
					if (!rawJsonTestResultsFile.getParentFile().exists())
						rawJsonTestResultsFile.getParentFile().mkdirs();

					if (!rawJsonTestResultsFile.exists()) {
						boolean isFileCreated = rawJsonTestResultsFile.createNewFile();
						if (isFileCreated)
							Log.event("File created successfully");	
					}

					String output = "";
					String executionDetailsJSONData = "var executionDetailsJson=".concat(dynamicTestResults.toString(2))
							.concat(";");
					String classSuiteWiseResultJSONData = "var classSuiteWiseResultJson="
							.concat(classSuiteWiseResultJSONObject.toString(2)).concat(";");
					String moduleWiseResultJSONData = "var moduleWiseResultJson="
							.concat(moduleWiseResultJSONObject.toString(2)).concat(";");
					String exceptionListJSONData = "var exceptionListJson=".concat(exceptionListJSONObject.toString(2))
							.concat(";");
					String masterTableData = "var testCaseResultData=".concat(completedTestsJSONArray.toString(2))
							.concat(";");
					String exceptionRootCauseJSONData = "var exceptionRootCauseJSONData="
							.concat(exceptionRootCauseJSONObject.toString(2)).concat(";");
					output = "{".concat(executionDetailsJSONData).concat(classSuiteWiseResultJSONData)
							.concat(moduleWiseResultJSONData).concat(exceptionListJSONData).concat(masterTableData)
							.concat(exceptionRootCauseJSONData).concat("}");
					FileUtils.writeStringToFile(rawJsonTestResultsFile, output, Charset.defaultCharset(), false);
				} catch (IOException e) {
					try {
						Log.exception(e);
					} catch (Exception logException) {
						// TODO Auto-generated catch block
						System.err.println("Exception: " + e.getMessage());	
				}				}
			}
		} catch (Exception e) {
			try {
				Log.exception(e);
			} catch (Exception logException) {
				// TODO Auto-generated catch block
				System.err.println("Exception: " + e.getMessage());	
		}		}
	}

	/**
	 * This method will initate the Real Time Execution Report Generation
	 * 
	 * @param context - ITestContext
	 */
	private void initRealTimeExecutionReport(ITestContext context) {
		if (realTimeExecutionReport == null) {
			File executionResultFolder = new File(context.getOutputDirectory());
			String executionResultFolderPath = executionResultFolder.getParent() + File.separator
					+ "RealTimeExecutionReport.html";
			realTimeExecutionReport = new File(executionResultFolderPath);

			try {
				if (!realTimeExecutionReport.getParentFile().exists())
					realTimeExecutionReport.getParentFile().mkdirs();

				if (!realTimeExecutionReport.exists()) {
					boolean isFileCreated = realTimeExecutionReport.createNewFile();
					if (isFileCreated)
						Log.event("File created successfully");	
				}				

				File htmlCssDesignFile = new File(htmlCssDesignFilePath);
				FileInputStream htmlCssDesignfs = new FileInputStream(htmlCssDesignFile);
				String fullHtmlCssDesign = IOUtils.toString(htmlCssDesignfs, Charset.defaultCharset());
				htmlCssDesignStart = fullHtmlCssDesign.split("LiveJSONDataInsert")[0].toString();
				htmlCssDesignEnd = fullHtmlCssDesign.split("LiveJSONDataInsert")[1].toString();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
				startTime = new Date();
				df.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getID()));
				strStartTime = df.format(startTime);
				longStartTime = startTime.getTime();
				dynamicTestResults.put("startTime", strStartTime);
				dynamicTestResults.put("executionSuiteName", context.getSuite().getName().toString());
				String environmentUrl = System.getProperty("webSite") != null ? System.getProperty("webSite")
						: context.getCurrentXmlTest().getParameter("webSite").toLowerCase();
				dynamicTestResults.put("environmentUrl", environmentUrl);
				dynamicTestResults.put("passedTestCount", passed);
				dynamicTestResults.put("failedTestCount", failed);
				dynamicTestResults.put("skippedTestCount", skipped);
				dynamicTestResults.put("totalTestCount", total);
				writeReport("OnStart", true, null, new ArrayList<String>());
				String output = generateHtmlCssDesignWithJSONData();
				FileUtils.writeStringToFile(realTimeExecutionReport, output, Charset.defaultCharset(), false);

			} catch (IOException e) {
				try {
					Log.exception(e);
				} catch (Exception logException) {
					// TODO Auto-generated catch block
					System.err.println("Exception: " + e.getMessage());	
			}			}
		}
	}

	/**
	 * This method is to write JSON Data of the test results as HTML Report file
	 * 
	 * @param testCaseStatus
	 * @param refresh
	 * @param tr
	 * @param bsUrlList
	 * @param tcHasRetry
	 */
	private static synchronized void writeReport(String testCaseStatus, boolean refresh, ITestResult tr,
			ArrayList<String> bsUrlList, boolean... tcHasRetry) {
		try {
			if (!testCaseStatus.equals("OnStart") && !testCaseStatus.equals("OnFinish")) {
				dynamicTestResults.put("testExecutionStatus", "running");
				String testCaseResult = "";
				String screenShot = "";
				String eMessage = "";
				String testCaseTitle = "";
				JSONObject jsonTestResult = new JSONObject();
				String testCaseId = tr.getMethod().getMethodName();
				String testScriptPath = tr.getMethod().getTestClass().getName();
				String testSuiteName = testScriptPath.split("\\.")[testScriptPath.split("\\.").length - 1];
				String testModuleName = testSuiteName.contains("Suite") ? testSuiteName.split("Suite")[0].toString()
						: testSuiteName.split("suite")[0].toString();
				if (tr.getMethod().getDescription() != null) {
					testCaseTitle = tr.getMethod().getDescription().toString();
					testCaseTitle = testCaseTitle.contains(":") ? testCaseTitle.split("\\:")[1] : testCaseTitle;
				} else {
					testCaseTitle = getTestTitle(Reporter.getOutput(tr).toString()).contains(":")
							? getTestTitle(Reporter.getOutput(tr).toString()).split("\\:")[1]
									: getTestTitle(Reporter.getOutput(tr).toString());
				}
				jsonTestResult.put("testCaseId", testCaseId);
				jsonTestResult.put("testScriptPath", testScriptPath);
				jsonTestResult.put("testSuiteName", testSuiteName);
				jsonTestResult.put("testModuleName", testModuleName);
				jsonTestResult.put("testCaseTitle", testCaseTitle);
				jsonTestResult.put("testCaseStatus", testCaseStatus);

				if (!moduleWiseResultJSONObject.has(testModuleName)) {
					JSONObject moduleResultJSONObject = new JSONObject();
					moduleResultJSONObject.put("passedTestCount", 0);
					moduleResultJSONObject.put("skippedTestCount", 0);
					moduleResultJSONObject.put("failedTestCount", 0);
					moduleWiseResultJSONObject.put(testModuleName, moduleResultJSONObject);
				}

				if (!classSuiteWiseResultJSONObject.has(testSuiteName)) {
					JSONObject suiteResultJSONObject = new JSONObject();
					suiteResultJSONObject.put("passedTestCount", 0);
					suiteResultJSONObject.put("skippedTestCount", 0);
					suiteResultJSONObject.put("failedTestCount", 0);
					classSuiteWiseResultJSONObject.put(testSuiteName, suiteResultJSONObject);
				}

				if (testCaseStatus.equals("Running")) {
					if (dynamicTestResults.has("runningTestCount")) {
						running = dynamicTestResults.getInt("runningTestCount");
						running++;
					} else {
						running = 1;
					}
					dynamicTestResults.put("runningTestCount", running);
					if (testCaseTitle.isEmpty()) {
						jsonTestResult.put("testCaseTitle", "NA");
					}
					testCaseResult = "";
					jsonTestResult.put("testCaseResult", testCaseResult);
					runningTestsJSONArray.put(jsonTestResult);
				} else if (testCaseStatus.equals("Pass")) {
					if (dynamicTestResults.has("passedTestCount")) {
						passed = dynamicTestResults.getInt("passedTestCount");
						passed++;
					} else {
						passed = 1;
					}
					dynamicTestResults.put("passedTestCount", passed);
					int moduleLevelCount = moduleWiseResultJSONObject.getJSONObject(testModuleName)
							.getInt("passedTestCount");
					moduleWiseResultJSONObject.getJSONObject(testModuleName).put("passedTestCount",
							(moduleLevelCount + 1));
					int suiteLevelCount = classSuiteWiseResultJSONObject.getJSONObject(testSuiteName)
							.getInt("passedTestCount");
					classSuiteWiseResultJSONObject.getJSONObject(testSuiteName).put("passedTestCount",
							(suiteLevelCount + 1));
					testCaseResult = "Test Passed.";
				} else if (testCaseStatus.equals("Fail")) {
					if (dynamicTestResults.has("failedTestCount")) {
						failed = dynamicTestResults.getInt("failedTestCount");
						failed++;
					} else {
						failed = 1;
					}
					dynamicTestResults.put("failedTestCount", failed);
					int moduleLevelCount = moduleWiseResultJSONObject.getJSONObject(testModuleName)
							.getInt("failedTestCount");
					moduleWiseResultJSONObject.getJSONObject(testModuleName).put("failedTestCount",
							(moduleLevelCount + 1));
					int suiteLevelCount = classSuiteWiseResultJSONObject.getJSONObject(testSuiteName)
							.getInt("failedTestCount");
					classSuiteWiseResultJSONObject.getJSONObject(testSuiteName).put("failedTestCount",
							(suiteLevelCount + 1));

					JSONObject jsonFailExceptionObject = new JSONObject();
					if (tr.getThrowable() != null) {
						eMessage = tr.getThrowable().getMessage();
						String eName = tr.getThrowable().getClass().getSimpleName();
						if (!isNull(eMessage) && eMessage.contains("\n")) {
							eMessage = eMessage.substring(0, eMessage.indexOf("\n"));
						}
						if (!eName.equals("")) {
							jsonFailExceptionObject.put("name", tr.getThrowable().getClass().getSimpleName());
						}
						jsonFailExceptionObject.put("message", eMessage);
						String exceptionStackTrace = ExceptionUtils.getStackTrace(tr.getThrowable());
						jsonFailExceptionObject.put("stackTrace", exceptionStackTrace);
						String highLevelStackTrace = exceptionStackTrace.split("\\n\\t")[0].concat("\n")
								.concat(StringUtils
										.join(Arrays.asList(ExceptionUtils.getStackFrames(tr.getThrowable())).stream()
												.filter(expectedStack -> expectedStack.contains("com.learningservices"))
												.collect(Collectors.joining("\r\n"))));
						jsonFailExceptionObject.put("highLevelStackTrace", highLevelStackTrace);
						String exceptionDetailForExceptionList = eMessage;
						int exceptionCount = 1;
						if (exceptionListJSONObject.has(exceptionDetailForExceptionList)) {
							exceptionCount = exceptionListJSONObject.getInt(exceptionDetailForExceptionList);
							exceptionCount++;
						} else {
							exceptionCount = 1;
						}
						exceptionListJSONObject.put(exceptionDetailForExceptionList, exceptionCount);
						jsonTestResult.put("testCaseException", jsonFailExceptionObject);
						JSONObject exceptionRootCauseObject = new JSONObject();
						exceptionRootCauseObject.put("exceptionName", tr.getThrowable().getClass().getSimpleName());
						exceptionRootCauseObject.put("exceptionMessage", eMessage);
						exceptionRootCauseObject.put("exceptionStackTrace", exceptionStackTrace);
						exceptionRootCauseObject.put("methodRootCause",
								new JSONArray(Arrays.asList(ExceptionUtils.getStackFrames(tr.getThrowable())).stream()
										.filter(expectedStack -> expectedStack.contains("com.learningservices"))
										.collect(Collectors.toList())));
						exceptionRootCauseJSONObject.put(testCaseId, exceptionRootCauseObject);
					} else {
						jsonTestResult.put("testCaseException", jsonFailExceptionObject);
					}
					testCaseResult = eMessage;
					if (testCaseResult.isEmpty()) {
						testCaseResult = "Test Failed.";
					}
				} else if (testCaseStatus.equals("Skip")) {
					if (dynamicTestResults.has("skippedTestCount")) {
						skipped = dynamicTestResults.getInt("skippedTestCount");
						skipped++;
					} else {
						skipped = 1;
					}
					dynamicTestResults.put("skippedTestCount", skipped);
					int moduleLevelCount = moduleWiseResultJSONObject.getJSONObject(testModuleName)
							.getInt("skippedTestCount");
					moduleWiseResultJSONObject.getJSONObject(testModuleName).put("skippedTestCount",
							(moduleLevelCount + 1));
					int suiteLevelCount = classSuiteWiseResultJSONObject.getJSONObject(testSuiteName)
							.getInt("skippedTestCount");
					classSuiteWiseResultJSONObject.getJSONObject(testSuiteName).put("skippedTestCount",
							(suiteLevelCount + 1));

					JSONObject jsonSkipExceptionObject = new JSONObject();
					if (tr.getMethod().getMethodsDependedUpon() != null) {
						jsonTestResult.put("testCaseDependsOnMethods",
								Arrays.toString(tr.getMethod().getMethodsDependedUpon()));
					} else {
						jsonTestResult.put("testCaseDependsOnMethods", "");
					}
					if (tr.getThrowable() != null) {
						eMessage = tr.getThrowable().getMessage();
						String eName = tr.getThrowable().getClass().getSimpleName();
						if (!isNull(eMessage) && eMessage.contains("\n")) {
							eMessage = eMessage.substring(0, eMessage.indexOf("\n"));
						}
						if (!eName.equals("")) {
							jsonSkipExceptionObject.put("name", tr.getThrowable().getClass().getSimpleName());
						}
						jsonSkipExceptionObject.put("message", eMessage);
						String exceptionStackTrace = ExceptionUtils.getStackTrace(tr.getThrowable());
						jsonSkipExceptionObject.put("stackTrace", exceptionStackTrace);
						String highLevelStackTrace = exceptionStackTrace.split("\\n\\t")[0].concat("\n")
								.concat(StringUtils
										.join(Arrays.asList(ExceptionUtils.getStackFrames(tr.getThrowable())).stream()
												.filter(expectedStack -> expectedStack.contains("com.learningservices"))
												.collect(Collectors.joining("\r\n"))));
						jsonSkipExceptionObject.put("highLevelStackTrace", highLevelStackTrace);
						String exceptionDetailForExceptionList = eMessage;
						int exceptionCount = 1;
						if (exceptionListJSONObject.has(exceptionDetailForExceptionList)) {
							exceptionCount = exceptionListJSONObject.getInt(exceptionDetailForExceptionList);
							exceptionCount++;
						} else {
							exceptionCount = 1;
						}
						exceptionListJSONObject.put(exceptionDetailForExceptionList, exceptionCount);
						jsonTestResult.put("testCaseException", jsonSkipExceptionObject);
						JSONObject exceptionRootCauseObject = new JSONObject();
						exceptionRootCauseObject.put("exceptionName", tr.getThrowable().getClass().getSimpleName());
						exceptionRootCauseObject.put("exceptionMessage", eMessage);
						exceptionRootCauseObject.put("exceptionStackTrace", exceptionStackTrace);
						exceptionRootCauseObject.put("methodRootCause",
								new JSONArray(Arrays.asList(ExceptionUtils.getStackFrames(tr.getThrowable())).stream()
										.filter(expectedStack -> expectedStack.contains("com.learningservices"))
										.collect(Collectors.toList())));
						exceptionRootCauseJSONObject.put(testCaseId, exceptionRootCauseObject);
					} else {
						jsonTestResult.put("testCaseException", "");
					}
					testCaseResult = "Test Skipped.";
				}
				if (!testCaseStatus.equals("Running")) {
					int count = 0;
					for (Object runningTestObj : runningTestsJSONArray) {
						JSONObject runningTestJSONObj = (JSONObject) runningTestObj;
						if (runningTestJSONObj.getString("testCaseId").equals(testCaseId)) {
							runningTestsJSONArray.remove(count);
							running = dynamicTestResults.getInt("runningTestCount");
							running--;
							dynamicTestResults.put("runningTestCount", running);
							break;
						}
						count++;
					}
					jsonTestResult.put("testCaseResult", testCaseResult);
					screenShot = getScreenShotHyperLink(Reporter.getOutput(tr).toString());
					if (screenShot.equals("")) {
						screenShot = "NA";
					} else {
						screenShot = ".\\ScreenShot".concat(screenShot).replaceAll("./", "").replaceAll("\"", "")
								.concat(".png");
					}
					jsonTestResult.put("testCaseScreenshot", screenShot);
					JSONArray bsUrlJSONArray = new JSONArray(bsUrlList);
					jsonTestResult.put("testCaseBSUrls", bsUrlJSONArray);
					completedTestsJSONArray.put(jsonTestResult);
				}

				lastModifiedTime = new Date();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
				lastModified = df.format(lastModifiedTime);
				longLastModifiedTime = lastModifiedTime.getTime();
				dynamicTestResults.put("lastModifiedTime", lastModified);
				totalTimeTaken = longLastModifiedTime - longStartTime;
			} else if (testCaseStatus.equals("OnFinish")) {
				dynamicTestResults.put("testExecutionStatus", "finished");
			}

			total = passed + skipped + failed;
			dynamicTestResults.put("totalTestCount", total);
			if (dynamicTestResults.has("pendingTestCount")) {
				if (noOfMethods > 0 && (noOfMethods - total) >= 0) {
					remaining = (noOfMethods - total);
				} else {
					remaining = -1;
				}
			} else {
				remaining = -1;
			}
			dynamicTestResults.put("pendingTestCount", remaining);
			dynamicTestResults.put("totalExecutionTime",
					(TimeUnit.MILLISECONDS.toHours(totalTimeTaken) % 24) + " hrs "
							+ (TimeUnit.MILLISECONDS.toMinutes(totalTimeTaken) % 60) + " mins "
							+ (TimeUnit.MILLISECONDS.toSeconds(totalTimeTaken) % 60) + " secs");

			String output = generateHtmlCssDesignWithJSONData();
			FileUtils.writeStringToFile(realTimeExecutionReport, output, Charset.defaultCharset(), false);
		} catch (Exception e) {
			try {
				Log.exception(e);
			} catch (Exception logException) {
				// TODO Auto-generated catch block
				System.err.println("Exception: " + e.getMessage());	
		}		}
	}

	/**
	 * This method will concat the JSON Data with HTML Code
	 * 
	 * @return HTML Report code with JSON Data
	 */
	private static String generateHtmlCssDesignWithJSONData() {
		String output = "";
		try {
			String executionDetailsJSONData = "var executionDetailsJson=".concat(dynamicTestResults.toString(2))
					.concat(";");
			String classSuiteWiseResultJSONData = "var classSuiteWiseResultJson="
					.concat(classSuiteWiseResultJSONObject.toString(2)).concat(";");
			String moduleWiseResultJSONData = "var moduleWiseResultJson=".concat(moduleWiseResultJSONObject.toString(2))
					.concat(";");
			String exceptionListJSONData = "var exceptionListJson=".concat(exceptionListJSONObject.toString(2))
					.concat(";");
			String masterTableData = "var testCaseResultData="
					.concat(StringUtils.chop(runningTestsJSONArray.toString(2)))
					.concat((runningTestsJSONArray.length() == 0 || completedTestsJSONArray.length() == 0) ? "" : ",")
					.concat(completedTestsJSONArray.toString(2).substring(1)).concat(";");
			String exceptionRootCauseJSONData = "var exceptionRootCauseJSONData="
					.concat(exceptionRootCauseJSONObject.toString(2)).concat(";");
			output = htmlCssDesignStart.concat(executionDetailsJSONData).concat(classSuiteWiseResultJSONData)
					.concat(moduleWiseResultJSONData).concat(exceptionListJSONData).concat(masterTableData)
					.concat(exceptionRootCauseJSONData).concat(htmlCssDesignEnd);
			return output;
		} catch (Exception e) {
			return output;
		}
	}

	/**
	 * This method is used to extract bs urls for the failed cases
	 * 
	 * @param text - String
	 * @return list of bsURLs
	 */
	public static ArrayList<String> extractUrls(String text) {
		ArrayList<String> containedUrls = new ArrayList<String>();
		final String urlRegex = "((https?|ftp|gopher|telnet|file):(\\/)+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
		Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
		Matcher urlMatcher = pattern.matcher(text);

		while (urlMatcher.find()) {
			containedUrls.add(text.substring(urlMatcher.start(0), urlMatcher.end(0)));
		}
		containedUrls.removeIf(s -> s.contains("</a"));
		return containedUrls;
	}

	/**
	 * getTestTitle gets the test/method description from the test case
	 * 
	 * @param content
	 */
	public static String getTestTitle(String content) {

		Pattern p = Pattern.compile(startTestTitle + "(.*)" + endTestTitle, Pattern.DOTALL);
		Matcher matcher = p.matcher(content);

		try {
			if (matcher.find()) {
				return matcher.group(1).contains("<small>") ? matcher.group(1).replaceAll("<small(.*)/small>", "")
						: matcher.group(1);
			} else {
				return "";
			}
		} catch (IllegalStateException e) {
			return "";
		}
	}

	/**
	 * getScreenShotHyperLink gets the screenshot from the test case
	 * 
	 * @param content
	 */
	public static String getScreenShotHyperLink(String content) {
		List<String> screenShot = new ArrayList<>();
		Pattern p = Pattern.compile("href=\"\\.\\\\ScreenShot(.*?)\\.png", Pattern.DOTALL);
		Matcher matcher = p.matcher(content);

		try {
			while (matcher.find()) {
				screenShot.add(matcher.group(1));
			}
			if (screenShot.size() != 0) {
				return screenShot.get(screenShot.size() - 1);
			} else {
				return "";
			}

		} catch (IllegalStateException e) {
			return "";
		}
	}

	/**
	 * This method is used to display the remaining number of cases need to be
	 * executed in the Real Time Execution Report
	 * 
	 * @param annotation      - ITestAnnotation
	 * @param testClass       - Class
	 * @param testConstructor - Constructor
	 * @param testMethod      - Method
	 */
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		try {
			if (testMethod != null && testMethod.getAnnotation(Test.class).enabled()) {
				noOfMethods++;
			}
		} catch (Exception e) {
		}
	}

}