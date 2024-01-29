package com.OrangeHRM.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.testng.ISuite;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.reporters.EmailableReporter2;
import org.testng.xml.XmlSuite;

/**
 * EmailReport will generate HTML report and zip the report with screenshot and
 * send email
 */
public class EmailReport extends EmailableReporter2 {

	PrintWriter pWriter;
	String fileName;
	private static boolean isReportClosed = false;

	static String unescapePattern = "\\<div\\sclass=\"messages\">(.*)\\<\\/div\\>";
	static String unescapeSauceLink = "\\<tr\\sclass=\"param\\sstripe\">(.*)\\<\\/tr\\>";
	static String startTestTitle = "<div class=\"test-title\"> <strong><font size = \"3\" color = \"#000080\">";
	static String endTestTitle = "</font> </strong></div>&emsp;<div><strong>Steps:</strong>";
	static Boolean ignoreMethodeName = false;
	public static List<String> bets = new ArrayList<String>();

	@Override
	public void generateReport(List<XmlSuite> xml, List<ISuite> suites, String outdir) {

		if (isReportClosed) {
			return;
		}
		//Log.message("Generating report...");

		super.generateReport(xml, suites, outdir);
		File eScripts = new File("jsscripts.txt");
		File eCSS = new File("ReportCSS.txt");

		try {

			File eReport = new File(outdir + File.separator + "TestAutomationResults.html");
			File eReport1 = new File(outdir + File.separator + "emailable-report.html");

			FileUtils.copyFile(eReport, eReport1);
			String eContent = FileUtils.readFileToString(eReport, "UTF-8");

			Pattern p = Pattern.compile(unescapePattern, Pattern.DOTALL);
			Matcher matcher = p.matcher(eContent);
			int matchCount = 0;

			while (matcher.find()) {
				matchCount++;
			}

			matcher = p.matcher(eContent);

			for (int i = 0; i < matchCount; i++) {
				matcher.find();
				String unEscapePart = matcher.group(1);
				unEscapePart = unEscapePart.replace("&lt;", "<");
				unEscapePart = unEscapePart.replace("&gt;", ">");
				unEscapePart = unEscapePart.replace("&quot;", "\"");
				unEscapePart = unEscapePart.replace("&apos;", "'");
				unEscapePart = unEscapePart.replace("&amp;", "&");
				eContent = eContent.replace(matcher.group(1), unEscapePart);
			}

			p = Pattern.compile(unescapeSauceLink, Pattern.DOTALL);
			matcher = p.matcher(eContent);
			matchCount = 0;

			while (matcher.find()) {
				matchCount++;
			}

			matcher = p.matcher(eContent);

			for (int i = 0; i < matchCount; i++) {
				matcher.find();
				String unEscapePart = matcher.group(1);
				unEscapePart = unEscapePart.replace("\"", "");
				unEscapePart = unEscapePart.replace("&lt;", "<");
				unEscapePart = unEscapePart.replace("&gt;", ">");
				unEscapePart = unEscapePart.replace("&quot;", "\"");
				unEscapePart = unEscapePart.replace("&apos;", "'");
				unEscapePart = unEscapePart.replace("&amp;", "&");
				eContent = eContent.replace(matcher.group(1), unEscapePart);
			}

			long minStartTime = 0;
			long maxEndTime = 0;
			long temp = 0;

			// Adding Test method - description to Summary Table (i.e)Test case
			// title
			for (SuiteResult suiteResult : super.suiteResults) {

				String ignoreMethodeNameParameter = suites.get(suiteResults.indexOf(suiteResult))
						.getParameter("ignoreMethodeName");

				if (ignoreMethodeNameParameter != null && ignoreMethodeNameParameter.contains("true")) {
					ignoreMethodeName = true;
				}

				for (TestResult testResult : suiteResult.getTestResults()) {

					for (ClassResult classResult : testResult.getFailedTestResults()) {

						for (MethodResult methodResult : classResult.getMethodResults()) {

							for (ITestResult tResult : methodResult.getResults()) {

								temp = tResult.getStartMillis();

								String exceptionReplacement = tResult.getThrowable().getMessage(); // Replace
																									// stake
																									// trace
																									// with
																									// original
																									// unescape
																									// them

								if (!(tResult.getThrowable() instanceof java.lang.AssertionError)
										&& exceptionReplacement != null && !exceptionReplacement.isEmpty()) {

									if (exceptionReplacement.indexOf("(Session") > 0) {
										exceptionReplacement = exceptionReplacement
												.substring(0, exceptionReplacement.indexOf("(Session") - 1).trim();
									}

									String exceptionToReplace = exceptionReplacement;
									exceptionReplacement = exceptionReplacement.replace("&", "&amp;");
									exceptionReplacement = exceptionReplacement.replace("<", "&lt;");
									exceptionReplacement = exceptionReplacement.replace(">", "&gt;");
									exceptionReplacement = exceptionReplacement.replace("\"", "&quot;");
									exceptionReplacement = exceptionReplacement.replace("'", "&apos;");
									eContent = eContent.replace(exceptionToReplace, exceptionReplacement);
								}

								if (minStartTime == 0 || temp < minStartTime) {
									minStartTime = temp;
								}

								temp = tResult.getEndMillis();

								if (maxEndTime == 0 || temp > maxEndTime) {
									maxEndTime = temp;
								}

								if (!tResult.getMethod().isTest()) {
									continue;
								}

								String methodDescription = getTestTitle(Reporter.getOutput(tResult).toString());
								String methodName = tResult.getMethod().getMethodName();

								if (methodDescription.isEmpty()) {
									methodDescription = tResult.getMethod().getDescription();
								}

								String toReplace = "<a href=\"#m([0-9]{1,4})\">" + methodName + "</a>";

								String toReplaceBy = "";

								if (ignoreMethodeName) {
									toReplaceBy = "<a href=\"#m$1\">" + methodDescription + "</a>";
								} else {
									toReplaceBy = "<a href=\"#m$1\">" + methodName + ": " + methodDescription + "</a>";
								}

								eContent = eContent.replaceFirst(toReplace, toReplaceBy);

							}

						}

					}

					for (ClassResult classResult : testResult.getSkippedTestResults()) {

						for (MethodResult methodResult : classResult.getMethodResults()) {

							for (ITestResult tResult : methodResult.getResults()) {

								temp = tResult.getStartMillis();

								if (minStartTime == 0 || temp < minStartTime) {
									minStartTime = temp;
								}

								temp = tResult.getEndMillis();

								if (maxEndTime == 0 || temp > maxEndTime) {
									maxEndTime = temp;
								}

								if (!tResult.getMethod().isTest()) {
									continue;
								}

								String methodName = tResult.getMethod().getMethodName();
								String methodDescription = getTestTitle(Reporter.getOutput(tResult).toString());

								if (methodDescription.isEmpty()) {
									methodDescription = tResult.getMethod().getDescription();
								}

								String toReplace = "<a href=\"#m([0-9]{1,4})\">" + methodName + "</a>";

								String toReplaceBy = "";

								if (ignoreMethodeName) {
									toReplaceBy = "<a href=\"#m$1\">" + methodDescription + "</a>";
								} else {
									toReplaceBy = "<a href=\"#m$1\">" + methodName + ": " + methodDescription + "</a>";
								}

								eContent = eContent.replaceFirst(toReplace, toReplaceBy);
							}

						}

					}

					for (ClassResult classResult : testResult.getPassedTestResults()) {

						for (MethodResult methodResult : classResult.getMethodResults()) {

							for (ITestResult tResult : methodResult.getResults()) {

								temp = tResult.getStartMillis();

								if (minStartTime == 0 || temp < minStartTime) {
									minStartTime = temp;
								}

								temp = tResult.getEndMillis();

								if (maxEndTime == 0 || temp > maxEndTime) {
									maxEndTime = temp;
								}

								if (!tResult.getMethod().isTest()) {
									continue;
								}

								String methodName = tResult.getMethod().getMethodName();
								String methodDescription = getTestTitle(Reporter.getOutput(tResult).toString());

								if (methodDescription.isEmpty()) {
									methodDescription = tResult.getMethod().getDescription();
								}

								String toReplace = "<a href=\"#m([0-9]{1,4})\">" + methodName + "</a>";

								String toReplaceBy = "";

								if (ignoreMethodeName) {
									toReplaceBy = "<a href=\"#m$1\">" + methodDescription + "</a>";
								} else {
									toReplaceBy = "<a href=\"#m$1\">" + methodName + ": " + methodDescription + "</a>";
								}

								eContent = eContent.replaceFirst(toReplace, toReplaceBy);
							}
						}
					}

					for (ClassResult classResult : testResult.getRetriedTestResults()) {

						for (MethodResult methodResult : classResult.getMethodResults()) {

							for (ITestResult tResult : methodResult.getResults()) {

								temp = tResult.getStartMillis();

								if (minStartTime == 0 || temp < minStartTime) {
									minStartTime = temp;
								}

								temp = tResult.getEndMillis();

								if (maxEndTime == 0 || temp > maxEndTime) {
									maxEndTime = temp;
								}

								if (!tResult.getMethod().isTest()) {
									continue;
								}

								String methodName = tResult.getMethod().getMethodName();
								String methodDescription = getTestTitle(Reporter.getOutput(tResult).toString());

								if (methodDescription.isEmpty()) {
									methodDescription = tResult.getMethod().getDescription();
								}

								String toReplace = "<a href=\"#m([0-9]{1,4})\">" + methodName + "</a>";

								String toReplaceBy = "";

								if (ignoreMethodeName) {
									toReplaceBy = "<a href=\"#m$1\">" + methodDescription + "</a>";
								} else {
									toReplaceBy = "<a href=\"#m$1\">" + methodName + ": " + methodDescription + "</a>";
								}

								eContent = eContent.replaceFirst(toReplace, toReplaceBy);
							}

						}

					}
				}
			}

			eContent = eContent.replace("</head>", "\r</head>\r");
			eContent = eContent.replace("<table", "\r\t<table");
			eContent = eContent.replace("</table>", "\r\t</table>\r");
			eContent = eContent.replaceFirst("<table>",
					"<table id='suitesummary' title=\"Filters results based on cell clicked/Shows all result on double-click\">");
			eContent = eContent.replaceFirst("<table>", "<table id='summary'>");

			eContent = eContent.replace("<thead>", "\r\t<thead>\r");
			eContent = eContent.replace("</thead>", "\r\t</thead>\r");
			eContent = eContent.replace("<tbody>", "\r\t<tbody>\r");
			eContent = eContent.replace("</tbody>", "\r\t</tbody>\r");

			eContent = eContent.replace("<h2", "\r\t\t<h2");
			eContent = eContent.replace("<tr", "\r\t\t<tr");
			eContent = eContent.replace("</tr>", "\r\t\t</tr>\r");
			eContent = eContent.replace("<td>", "\r\t\t\t<td>");
			eContent = eContent.replace("</td>", "\r\t\t\t</td>\r");
			eContent = eContent.replace("<th", "\r\t\t\t<th");
			eContent = eContent.replace("</th>", "\r\t\t\t</th>");
			eContent = eContent.replace("<br/>", "");
			eContent = eContent.replaceAll("<style(.*)</style>",
					"\r" + FileUtils.readFileToString(eCSS, Charset.defaultCharset()) + "\r");
			eContent = eContent.replace("<head>",
					"<head>" + "\r" + FileUtils.readFileToString(eScripts, Charset.defaultCharset()) + "\r");
			eContent = eContent.replace("<head>",
					"<head>" + "\r<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\" />\r");

			if (!bets.isEmpty()) {
				eContent = eContent.replace("</body>",
						"<br><b>Bets Placed:-</b><br>"
								+ "<table><tr><th>UserName</th><th>Bet Ref#</th><th>Time</th></tr>"
								+ bets.toString().replace(",", "\n") + "</table></body>");
			}

			eContent = eContent.replaceFirst(
					"<table id='suitesummary' title=\"Filters results based on cell clicked/Shows all result on double-click\">",
					"<table id='suitesummary' title=\"Filters results based on cell clicked/Shows all result on double-click\" duration=\""
							+ (maxEndTime - minStartTime) + "\">");

			FileUtils.writeStringToFile(eReport, eContent, Charset.defaultCharset(), false);

		} catch (IOException e) {
			try {
				Log.exception(e);
			} catch (Exception logException) {
				// TODO Auto-generated catch block
				System.err.println("Exception: " + e.getMessage());	
		}		} catch (Exception e) {
			try {
				Log.exception(e);
			} catch (Exception logException) {
				// TODO Auto-generated catch block
				System.err.println("Exception: " + e.getMessage());	
		}		}

		try {

			FileReader fr = null;
			fr = new FileReader(outdir + File.separator + "TestAutomationResults.html");
			BufferedReader br = new BufferedReader(fr);
			StringBuilder content = new StringBuilder(10000);
			String s;
			int tableCount = 0;

			String hub = "localhost";

			try {
				hub = (suites.get(0).getHost() == null) ? Inet4Address.getLocalHost().getHostName()
						: suites.get(0).getHost();
			} catch (UnknownHostException e) {
				// e.printStackTrace();
				Log.event("Getting some host issue..");
			}

			while ((s = br.readLine()) != null) {

				content.append(s + "\n");
				if (s.trim().contains("</table>")) {
					tableCount++;
				}

				if (s.startsWith("<body")) {

					content.append("<p> Hi, </p>" + "\n"
							+ "<p> Test automation scripts execution completed. Find the results summary below. </p>"
							+ "\n" + "<p> <u><h3> Test Run Details: </h3> </u>"
							+ "\r<table  bordercolor=\"#FFFFF\"> </u></h3> </p>\r" +

							"\r<pre style=\"font-size: 1.2em;\">\r" + "   <b>Suite Name</b>: "
							+ ((suiteResults.size() > 0) ? suiteResults.get(0).getSuiteName() : "Deafult Suite") + "\r"
							+ "   <b>Run Date</b>  : " + (new Date()).toString() + "\r" + "   <b>Run By</b>    : " + hub
							+ "\r" + "</pre>" + "<br><br>\n");
				}

				if (tableCount == 1) {
					content.append("\n</body>\n</html>");
					break;
				}

			}

			String emailContent = content.toString();
			File emailMsg = new File(outdir + File.separator + "AutomationTestResultsEmail.html");
			FileUtils.writeStringToFile(emailMsg, emailContent, Charset.defaultCharset(), false);

			br.close();
			fr.close();
			try {
				List<ITestResult> allTests = new ArrayList<ITestResult>();
				for (SuiteResult suiteResult : super.suiteResults) {
					for (TestResult testResult : suiteResult.getTestResults()) {
						for (ClassResult classResult : testResult.getFailedTestResults()) {
							for (MethodResult methodResult : classResult.getMethodResults()) {
								allTests.addAll(methodResult.getResults());
							}
						}
						for (ClassResult classResult : testResult.getFailedConfigurationResults()) {
							for (MethodResult methodResult : classResult.getMethodResults()) {
								allTests.addAll(methodResult.getResults());
							}
						}
						for (ClassResult classResult : testResult.getSkippedConfigurationResults()) {
							for (MethodResult methodResult : classResult.getMethodResults()) {
								allTests.addAll(methodResult.getResults());
							}
						}
						for (ClassResult classResult : testResult.getSkippedTestResults()) {
							for (MethodResult methodResult : classResult.getMethodResults()) {
								allTests.addAll(methodResult.getResults());
							}
						}
						for (ClassResult classResult : testResult.getPassedTestResults()) {
							for (MethodResult methodResult : classResult.getMethodResults()) {
								allTests.addAll(methodResult.getResults());
							}
						}
					}
				}
				ExtentReporter.closeReport(allTests, outdir);
			} catch (Throwable t) {
				//t.printStackTrace();
			}

			// adding files/folders to be added on zip folder
			List<String> filename = new ArrayList<String>();
			filename.add(outdir + File.separator + "TestAutomationResults.html");
			filename.add(outdir + File.separator + "ScreenShot");
			filename.add(outdir + File.separator + "ExtentReport.html");

			String ouputFile = outdir + File.separator + "AutomationTestSummaryReport.zip";
			FolderZiper folderZiper = new FolderZiper();
			folderZiper.zipFolder(filename, ouputFile);

		} catch (Exception e) {
			try {
				Log.exception(e);
			} catch (Exception logException) {
				// TODO Auto-generated catch block
				System.err.println("Exception: " + e.getMessage());	
		}		}
		isReportClosed = true;
		//Log.message("Report generation completed");
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
				return matcher.group(1);
			} else {
				return "";
			}
		} catch (IllegalStateException e) {
			return "";
		}

	}

	/**
	 * createWrites creates a new HTML TestAutomationResults file
	 * 
	 * @param outdir
	 * @throws IOException
	 */
	@Override
	protected PrintWriter createWriter(String outdir) throws IOException {
		new File(outdir).mkdirs();
		pWriter = new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, "TestAutomationResults.html"))));
		return pWriter;
	}

}
