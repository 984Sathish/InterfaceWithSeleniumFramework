package com.OrangeHRM.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import org.testng.annotations.ITestAnnotation;
import org.testng.annotations.Test;
import org.testng.xml.XmlSuite;

public class LiveReportListener extends TestListenerAdapter implements IAnnotationTransformer {

	private static int liveTestSuitesCount = 0;
	static HashMap<String, Integer> hmDynamicResults = new HashMap<String, Integer>();
	static HashMap<String, String> hmExceptions = new HashMap<String, String>();
	static HashMap<String, String> hmBSLink = new HashMap<String, String>();
	static TreeSet<String> trRetryEnabledTestCases = new TreeSet<String>();
	static String startTestTitle = "<div class=\"test-title\"> <strong><font size = \"3\" color = \"#000080\">";
	static String endTestTitle = "</font> </strong></div>&emsp;<div><strong>Steps:</strong>";
	static File eReport = null;
	private static int passed = 0;
	private static int failed = 0;
	private static int skipped = 0;
	private static int total = 0;
	private static int passRate = 0;
	private static int skipRate = 0;
	private static int failRate = 0;
	private static Map<String, Integer> hmTotalCountClassWise = new HashMap<String, Integer>();
	private static Map<String, Integer> hmPassCountClassWise = new HashMap<String, Integer>();
	private static Map<String, Integer> hmFailCountClassWise = new HashMap<String, Integer>();
	private static Map<String, Integer> hmSkipCountClassWise = new HashMap<String, Integer>();
	private static Date startTime = null;
	private static String strStartTime = null;
	private static String lastModified = null;
	private static String remaining = "NA";
	private static String AutoRefreshCode = "<meta http-equiv='refresh' content='20' /> <div class='blink_me'><inp><u><b>Execution in progress. Auto refresh enabled</b></u></inp></div>";
	static Map<String, LinkedHashSet<String>> reportDataMain = new HashMap<String, LinkedHashSet<String>>();
	static Map<String, LinkedHashSet<String>> reportData = new HashMap<String, LinkedHashSet<String>>();
//	private DateFormat df = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
	static int noOfMethods = 0;

	@Override
	public void onTestFailure(ITestResult tr) {

		try {
			Object params[] = Reporter.getCurrentTestResult().getParameters();

			if (params.length == 0) {
				WriteFile("Fail", true, tr);
				return;
			}

			if (params[0] instanceof String) {
				String bsUnformattedUrl = (String) params[0];
				List<String> extractedUrls = extractUrls(bsUnformattedUrl);
				String bsLink = "";
				if (extractedUrls.size() > 0) {
					bsLink = extractedUrls.get(0);
				}
				if (tr.getMethod().getRetryAnalyzer(tr) != null) {
					WriteFile("Fail", true, tr, bsLink, "true");
				} else {
					WriteFile("Fail", true, tr, bsLink);
				}
			} else {
				WriteFile("Fail", true, tr);
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
	public void onTestSkipped(ITestResult tr) {

		try {
			WriteFile("Skip", true, tr);
		} catch (IOException e) {
			try {
				Log.exception(e);
			} catch (Exception logException) {
				// TODO Auto-generated catch block
				System.err.println("Exception: " + e.getMessage());	
		}		}
	}

	@Override
	public void onTestSuccess(ITestResult tr) {

		try {
			Object params[] = Reporter.getCurrentTestResult().getParameters();
			if (params.length == 0) {
				WriteFile("Pass", true, tr);
				return;
			}

			if (params[0] instanceof String) {
				String bsUnformattedUrl = (String) params[0];
				List<String> extractedUrls = extractUrls(bsUnformattedUrl);
				String bsLink = "";
				if (extractedUrls.size() > 0) {
					bsLink = extractedUrls.get(0);
				}
				if (tr.getMethod().getRetryAnalyzer(tr) != null) {
					WriteFile("Pass", true, tr, bsLink, "true");
				} else {
					WriteFile("Pass", true, tr, bsLink);
				}
			} else {
				WriteFile("Pass", true, tr);
			}

			// WriteFile("Pass", true,"", tr);
		} catch (IOException e) {
			try {
				Log.exception(e);
			} catch (Exception logException) {
				// TODO Auto-generated catch block
				System.err.println("Exception: " + e.getMessage());	
		}		}
	}

	@Override
	public void onStart(ITestContext testContext) {
		initLiveReport(testContext);
	}

	@Override
	public void onFinish(ITestContext testContext) {
		try {
			liveTestSuitesCount++;
			XmlSuite suite = testContext.getSuite().getXmlSuite();
			if (liveTestSuitesCount == suite.getTests().size())
				WriteFile("", false, null);
		} catch (IOException e) {
			try {
				Log.exception(e);
			} catch (Exception logException) {
				// TODO Auto-generated catch block
				System.err.println("Exception: " + e.getMessage());	
		}		}
	}

	/**
	 * @param status        Execution status of the current test case -
	 *                      Pass/Fail/Skip
	 * @param refresh       Whether to enable auto refresh or not
	 * @param exceptionName Exception details in case of failure
	 * @throws IOException
	 */
	private static synchronized void WriteFile(String status, boolean refresh, ITestResult tr,
			String... bsUrlAndIsRetryMethod) throws IOException {
		try {

			String output = "";
			String tempAutoRefreshCode = "";
			String bsLink = null;
			String screenShot = "";
			boolean retryMethod = false;
			if (status.length() > 0 && refresh) {
				tempAutoRefreshCode = AutoRefreshCode;

				if (bsUrlAndIsRetryMethod.length != 0 && bsUrlAndIsRetryMethod[0].trim().length() > 0) {
					bsLink = bsUrlAndIsRetryMethod[0];
				}
				if (bsUrlAndIsRetryMethod.length >= 2 && bsUrlAndIsRetryMethod[1].equals("true"))
					retryMethod = true;
				if (status.equals("Fail")) {
					if (hmDynamicResults.containsKey("Failed")) {
						failed = hmDynamicResults.get("Failed");
						failed++;
					} else
						failed = 1;
					hmDynamicResults.put("Failed", failed);

				} else if (status.equals("Skip")) {
					if (hmDynamicResults.containsKey("Skipped")) {
						skipped = hmDynamicResults.get("Skipped");
						skipped++;
					} else
						skipped = 1;
					hmDynamicResults.put("Skipped", skipped);
				} else if (status.equals("Pass")) {
					if (hmDynamicResults.containsKey("Passed")) {
						passed = hmDynamicResults.get("Passed");
						passed++;
					} else
						passed = 1;
					hmDynamicResults.put("Passed", passed);
				}

				// String testCaseName = Reporter.getCurrentTestResult().getName();
				// String testClassName =
				// Reporter.getCurrentTestResult().getTestClass().getName();
				String testCaseName = tr.getMethod().getMethodName();
				String testClassName = tr.getMethod().getTestClass().getName();
				String methodDescription = getTestTitle(Reporter.getOutput(tr).toString()).contains(":")
						? getTestTitle(Reporter.getOutput(tr).toString()).split("\\:")[1]
						: getTestTitle(Reporter.getOutput(tr).toString());
				if (methodDescription.isEmpty()) {
					methodDescription = tr.getMethod().getDescription().contains(":")
							? getTestTitle(Reporter.getOutput(tr).toString()).split("\\:")[1]
							: getTestTitle(Reporter.getOutput(tr).toString());
				}

				if (retryMethod) {
					if (trRetryEnabledTestCases.contains(testClassName + "." + testCaseName)) {
						failed = hmDynamicResults.get("Failed");
						failed--;
						hmDynamicResults.put("Failed", failed);
					} else {
						trRetryEnabledTestCases.add(testClassName + "." + testCaseName);
					}
				}

				if (hmDynamicResults.containsKey("Passed"))
					passed = hmDynamicResults.get("Passed");

				if (hmDynamicResults.containsKey("Skipped"))
					skipped = hmDynamicResults.get("Skipped");

				if (hmDynamicResults.containsKey("Failed"))
					failed = hmDynamicResults.get("Failed");

				total = passed + skipped + failed;
				passRate = (passed * 100) / total;
				skipRate = (skipped * 100) / total;
				failRate = (failed * 100) / total;

				LinkedHashSet<String> testCaseStatus = new LinkedHashSet<String>();
				testCaseStatus.add(testCaseName + "." + status + "|" + methodDescription);
				reportData.put(testClassName, testCaseStatus);

				if (status.equalsIgnoreCase("Pass")) {
					if (hmPassCountClassWise.containsKey(testClassName)) {
						hmPassCountClassWise.replace(testClassName, hmPassCountClassWise.get(testClassName),
								hmPassCountClassWise.get(testClassName) + 1);
					} else {
						hmPassCountClassWise.put(testClassName, 1);
					}
				} else if (status.equalsIgnoreCase("Fail")) {
					if (hmFailCountClassWise.containsKey(testClassName)) {
						hmFailCountClassWise.replace(testClassName, hmFailCountClassWise.get(testClassName),
								hmFailCountClassWise.get(testClassName) + 1);
					} else {
						hmFailCountClassWise.put(testClassName, 1);
					}
				} else if (status.equalsIgnoreCase("Skip")) {
					if (hmSkipCountClassWise.containsKey(testClassName)) {
						hmSkipCountClassWise.replace(testClassName, hmSkipCountClassWise.get(testClassName),
								hmSkipCountClassWise.get(testClassName) + 1);
					} else {
						hmSkipCountClassWise.put(testClassName, 1);
					}
				}
				if (status.equalsIgnoreCase("Pass") || status.equalsIgnoreCase("Fail")
						|| status.equalsIgnoreCase("Skip")) {
					if (hmTotalCountClassWise.containsKey(testClassName)) {
						hmTotalCountClassWise.replace(testClassName, hmTotalCountClassWise.get(testClassName),
								hmTotalCountClassWise.get(testClassName) + 1);
					} else {
						hmTotalCountClassWise.put(testClassName, 1);
					}
				}
				screenShot = getScreenShotHyperLink(Reporter.getOutput(tr).toString());
				if (screenShot == null) {
					screenShot = "";
				}
				hmBSLink.put(testCaseName, bsLink + "|" + screenShot);

				Iterator<String> keyIterator = reportData.keySet().iterator();
				LinkedHashSet<String> mapValues = new LinkedHashSet<String>();
				while (keyIterator.hasNext()) {
					String keyName = keyIterator.next();
					if (reportDataMain.containsKey(keyName)) {
						mapValues = reportDataMain.get(keyName);
						mapValues.addAll(reportData.get(keyName));
					} else {
						mapValues = reportData.get(keyName);
					}
					reportDataMain.put(keyName, mapValues);
				}
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
				lastModified = df.format(new Date());
				strStartTime = df.format(startTime);
			} else {
				tempAutoRefreshCode = "<com><u><b>Execution Completed.</b></u></com><br>";
			}
			if (noOfMethods > 0 && (noOfMethods - total) >= 0)
				remaining = "" + (noOfMethods - total);
			else
				remaining = "NA";

			output = getTotalSummaryHtmlFormat(passed, skipped, failed, total, remaining, passRate, skipRate, failRate,
					strStartTime, lastModified, tempAutoRefreshCode);
			FileUtils.writeStringToFile(eReport, output, Charset.defaultCharset(), false);

		} catch (Exception e) {
		}
	}

	/**
	 * @param passed          Count of passed test cases
	 * @param skipped         Count of skipped test cases
	 * @param failed          Count of failed test cases
	 * @param total           Count of total executed test cases
	 * @param passRate        Pass percentage
	 * @param skipRate        Skip percentage
	 * @param failRate        Fail percentage
	 * @param startTime       Execution start time
	 * @param lastModified    Last modified time (Report updated time)
	 * @param autoRefreshCode Code for auto refresh
	 * @return
	 */
	private static String getTotalSummaryHtmlFormat(int passed, int skipped, int failed, int total, String remaining,
			int passRate, int skipRate, int failRate, String startTime, String lastModified, String autoRefreshCode) {

		StringBuilder buf = new StringBuilder();
		buf.append("<html> <script type='text/javascript' src='https://www.google.com/jsapi'></script>"
				+ "<script type='text/javascript'>    google.load('visualization', '1.0', {'packages':['corechart']});  google.setOnLoadCallback(drawChart);function drawChart() {var data = new google.visualization.DataTable(); data.addColumn('string', 'Result'); data.addColumn('number', 'Count');data.addRows([['PASS', "
				+ passed + "],['FAIL', " + failed + "],['SKIP', " + skipped
				+ "]]); var options = {'title':'::: Execution Stats :::', 'width':500, 'height':300, 'backgroundColor':'#ffffff', 'colors': ['#009933', '#F00000', '#1e90ff'], 'fontSize': 15};var chart = new google.visualization.PieChart(document.getElementById('chart_div'));chart.draw(data, options);  }  </script>"
				+ "<title> Live Execution Report </title> "
				+ "<script> function showDiv(objectID) { var theElementStyle = document.getElementById(objectID);  if(theElementStyle.style.display == 'none')  { theElementStyle.style.display = 'block';  } else { theElementStyle.style.display = 'none'; }  }  </script> "
				+ "<body bgcolor='#ffffff'><table width='100%' border='0' margin='10,5,5,50'  ><tr width='100%'><th > Live Execution Report</th></tr>    </table><br /> <div id='chart_div' style='float:right'></div><br>"
				+ autoRefreshCode
				+ "<style> table, th, td {border: 1px solid black;} th, td {padding: 10px;} th{background-color: #BBBBBB;} tf{color:red;} ts{color:blue;} tp{color:green;} inp{background-color: #FFFF66;} com{background-color: #7CFC00;} .blink_me {animation: blinker 1s linear infinite;} @keyframes blinker {50% {opacity: 0;}} </style>"
				+ "<br></body>" + "<table> <tbody> <tr> <th colspan='7'> Summary </th> </tr>"
				+ "<tr> <th> Passed </th> <th> Skipped </th> <th> Failed </th> <th> Total </th> <th> Remaining </th> <th> Start Time </th> <th> Last Modified </th> </tr>"
				+ "<tr> <td>" + passed + "</td>" + "<td>" + skipped + "</td>" + "<td>" + failed + "</td>" + "<td>"
				+ total + "</td>" + "<td>" + remaining + "</td>" + "<td>" + startTime + "</td>" + "<td>" + lastModified
				+ "</td> </tr></table><br>");

		buf.append("<br><table>" + "<tr>" + "<th>TestCase ID</th>" + "<th>TestCase Description</th>"
				+ "<th>Result / Screenshot</th>" + "<th>Test Recording</th>" + "<th>Class</th>" + "<th>Passed</th>"
				+ "<th>Skipped</th>" + "<th>Failed</th>" + "<th>Total</th>" + "</tr>");
		for (Entry<String, LinkedHashSet<String>> entry : reportDataMain.entrySet()) {
			String className = entry.getKey();
			HashSet<String> mapValues = new HashSet<String>();
			mapValues = entry.getValue();
			Iterator<String> itr = mapValues.iterator();
			int itrCount = 1;
			while (itr.hasNext()) {
				buf.append("<tr>");
				String data = itr.next();
				String Testcase = data.substring(0, data.indexOf("."));
				String Description = data.substring(data.lastIndexOf("|") + 1, data.length());
				data = data.split("\\|")[0];
				String Result = data.substring(data.lastIndexOf(".") + 1, data.length());
				String bsResult = "Link";
				String bsLink = "";
				String screenshotLink = "";
				int screenShotCount = hmBSLink.get(Testcase).split("\\|").length;
				if (screenShotCount == 1) {
					bsLink = hmBSLink.get(Testcase).split("\\|")[0];
				} else if (screenShotCount == 2) {
					bsLink = hmBSLink.get(Testcase).split("\\|")[0];
					screenshotLink = ".\\ScreenShot" + hmBSLink.get(Testcase).split("\\|")[1];
				}
				String endscreenShotLink = "</font></b>";
				String screenShot = "";
				if (bsLink != null) {
					if (!bsLink.equals("null")) {
						bsResult = "<b> <a href='" + bsLink + "' target='_blank'>" + "Link" + "</a></b>";
					}
				}
				if (!screenshotLink.equals("")) {
					screenShot = ".\\ScreenShot"
							+ hmBSLink.get(Testcase).split("\\|")[1].replaceAll("./", "").replaceAll("\"", "") + ".png";
					screenshotLink = "<a href='" + screenShot + "' target='_blank'>";
					endscreenShotLink = "</font></a></b>";
				}
				if (Result.equalsIgnoreCase("Fail")) {
					buf.append("</td><td>").append("<tf>").append(Testcase).append("</tf>").append("</td><td>")
							.append("<tf>").append(Description).append("</tf>").append("</td><td>").append("<tf>")
							.append("<b>" + screenshotLink + " <font color='red'>" + Result + endscreenShotLink)
							.append("</tf>").append("</td><td>").append("<tf>").append(bsResult).append("</tf>")
							.append("</td>");
					if (itrCount == 1) {
						buf.append("<td rowspan='" + mapValues.size() + "'>")
								.append(className.split("\\.")[className.split("\\.").length - 1]);
						buf.append("</td><td rowspan='" + mapValues.size() + "'><center>")
								.append(hmPassCountClassWise.get(className) == null ? 0
										: hmPassCountClassWise.get(className) + "</center>");
						buf.append("</td><td rowspan='" + mapValues.size() + "'><center>")
								.append(hmSkipCountClassWise.get(className) == null ? 0
										: hmSkipCountClassWise.get(className) + "</center>");
						buf.append("</td><td rowspan='" + mapValues.size() + "'><center>")
								.append(hmFailCountClassWise.get(className) == null ? 0
										: hmFailCountClassWise.get(className) + "</center>");
						buf.append("</td><td rowspan='" + mapValues.size() + "'><center>")
								.append(hmTotalCountClassWise.get(className) == null ? 0
										: hmTotalCountClassWise.get(className) + "</center>");
						buf.append("</tr>");
					}

				} else if (Result.equalsIgnoreCase("Skip")) {
					buf.append("</td><td>").append("<ts>").append(Testcase).append("</ts>").append("</td><td>")
							.append("<ts>").append(Description).append("</ts>").append("</td><td>").append("<ts>")
							.append(Result).append("</ts>").append("</td><td>").append("<ts>").append(bsResult)
							.append("</ts>").append("</td>");
					if (itrCount == 1) {
						buf.append("<td rowspan='" + mapValues.size() + "'>")
								.append(className.split("\\.")[className.split("\\.").length - 1]);
						buf.append("</td><td rowspan='" + mapValues.size() + "'><center>")
								.append(hmPassCountClassWise.get(className) == null ? 0
										: hmPassCountClassWise.get(className) + "</center>");
						buf.append("</td><td rowspan='" + mapValues.size() + "'><center>")
								.append(hmSkipCountClassWise.get(className) == null ? 0
										: hmSkipCountClassWise.get(className) + "</center>");
						buf.append("</td><td rowspan='" + mapValues.size() + "'><center>")
								.append(hmFailCountClassWise.get(className) == null ? 0
										: hmFailCountClassWise.get(className) + "</center>");
						buf.append("</td><td rowspan='" + mapValues.size() + "'><center>")
								.append(hmTotalCountClassWise.get(className) == null ? 0
										: hmTotalCountClassWise.get(className) + "</center>");
						buf.append("</tr>");
					}

				} else {
					buf.append("</td><td>").append("<tp>").append(Testcase).append("</tp>").append("</td><td>")
							.append("<tp>").append(Description).append("</tp>").append("</td><td>").append("<tp>")
							.append("<b>" + screenshotLink + " <font color='green'>" + Result + endscreenShotLink)
							.append("</tp>").append("</td><td>").append("<tp>").append(bsResult).append("</tp>")
							.append("</td>");
					if (itrCount == 1) {
						buf.append("<td rowspan='" + mapValues.size() + "'>")
								.append(className.split("\\.")[className.split("\\.").length - 1]);
						buf.append("</td><td rowspan='" + mapValues.size() + "'><center>")
								.append(hmPassCountClassWise.get(className) == null ? 0
										: hmPassCountClassWise.get(className) + "</center>");
						buf.append("</td><td rowspan='" + mapValues.size() + "'><center>")
								.append(hmSkipCountClassWise.get(className) == null ? 0
										: hmSkipCountClassWise.get(className) + "</center>");
						buf.append("</td><td rowspan='" + mapValues.size() + "'><center>")
								.append(hmFailCountClassWise.get(className) == null ? 0
										: hmFailCountClassWise.get(className) + "</center>");
						buf.append("</td><td rowspan='" + mapValues.size() + "'><center>")
								.append(hmTotalCountClassWise.get(className) == null ? 0
										: hmTotalCountClassWise.get(className) + "</center>");
						buf.append("</tr>");
					}
				}
				itrCount++;
			}

		}
		buf.append("</table>" + "</body>" + "</html>");

		return buf.toString();
	}

	/**
	 * This method is used to extract bs urls for the failed cases
	 * 
	 * @param text - String
	 * @return list of bsURLs
	 */
	public static List<String> extractUrls(String text) {
		List<String> containedUrls = new ArrayList<String>();
		final String urlRegex = "((https?|ftp|gopher|telnet|file):(\\/)+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
		Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
		Matcher urlMatcher = pattern.matcher(text);

		while (urlMatcher.find()) {
			containedUrls.add(text.substring(urlMatcher.start(0), urlMatcher.end(0)));
		}
		return containedUrls;
	}

	/**
	 * This method is used to display the remaining number of cases need to be
	 * executed in the live Report
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
	 * Creating live Report file
	 * 
	 * @param context
	 */
	public static void initLiveReport(ITestContext context) {
		if (eReport == null) {
			File liveReportFolder = new File(context.getOutputDirectory());
			String liveReportFolderPath = liveReportFolder.getParent() + File.separator + "LiveExecutionReport.html";
			eReport = new File(liveReportFolderPath);

			try {

				if (!eReport.getParentFile().exists())
					eReport.getParentFile().mkdirs();
				
				if (!eReport.exists()) {
				    boolean fileCreated = eReport.createNewFile();
				    if (fileCreated) {
				    	Log.event("File created successfully");
				    }
				}
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
				startTime = new Date();
				df.setTimeZone(TimeZone.getTimeZone(TimeZone.getDefault().getID()));
				strStartTime = df.format(startTime);
				String output = getTotalSummaryHtmlFormat(passed, skipped, failed, total, remaining, passRate, skipRate,
						failRate, strStartTime, lastModified, AutoRefreshCode);
				FileUtils.writeStringToFile(eReport, output, Charset.defaultCharset(), false);

			} catch (IOException e) {
				try {
					Log.exception(e);
				} catch (Exception logException) {
					// TODO Auto-generated catch block
					System.err.println("Exception: " + e.getMessage());	
			}			}
		}

	}
}