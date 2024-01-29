package com.OrangeHRM.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class BandwidthAnalyzer.
 */
public class BandwidthAnalyzer {

	/** The tasks. */
	List<Task> tasks = new ArrayList<Task>();

	/** The task name. */
	String testCaseName;

	/** The scenario name. */
	String Scenario;

	/** The start time. */
	long startTime;

	/** The end time. */
	long endTime;

	/** The is bandwidth test enabled. */
	boolean isBandwidthTestEnabled;

	/**
	 * Instantiates a new bandwidth analyzer.
	 *
	 * @param testCaseName the test case name
	 */
	public BandwidthAnalyzer(String testCaseName) {
		this.testCaseName = testCaseName;
		isBandwidthTestEnabled = Boolean
				.valueOf(EnvironmentPropertiesReader.getInstance().getProperty("BandwidthTest"));
	}

	/**
	 * Starts the timer.
	 *
	 * @param taskName    the task name
	 * @param osVersion
	 * @param browserName
	 */
	public void start(String taskName) {
		if (isBandwidthTestEnabled) {
			this.Scenario = taskName;
			startTime = StopWatch.startTime();
		}
	}

	/**
	 * Stops the timer and adds the details to the task.
	 */
	public void stop() {
		if (isBandwidthTestEnabled) {
			endTime = StopWatch.elapsedTime(startTime);
			Task temp = new Task();
			temp.setBandwidth(System.getProperty("bandwidth"));
			temp.setPlatform(System.getProperty("browserName"));
			temp.setBrowser(System.getProperty("os.name"));
			temp.setoS_Version(System.getProperty("os.version"));
			temp.setTimeTaken(endTime);
			temp.setTaskName(Scenario);
			tasks.add(temp);
			temp = null;
		}
	}

	/**
	 * Prints the bandwidth data for the whole test case.
	 */
	public String printBandwidthData() {
		if (isBandwidthTestEnabled) {
			Log.message("Test case Name = " + Scenario);
			String rowValues = "";
			String rowValuesInCSV = "";
			for (Task task : tasks) {
				rowValues = rowValues + task.toString();
				rowValuesInCSV = rowValuesInCSV + task.toCSVFormat();
			}
			String resultBandwidthTable = "<table>" + "<tr>" + "<th>Bandwidth(Kbps)/User</th>" + "<th>Product</th>"
					+ "<th>Platform</th>" + "<th>OS Version</th>" + "<th>OS Browser</th>" + "<th>Scenario</th>"
					+ "<th>Response Time (in Secs)</th>" + "</tr>" + rowValues + "</table>";
			Log.message(resultBandwidthTable);
			return rowValuesInCSV;
		} else {
			return "Bandwidth Analyzer is disabled!";
		}
	}

	/**
	 * The Class Task.
	 */
	class Task {

		String taskName;
		String dataDownloaded;
		long timeTaken;
		String Scenario;
		String bandwidth;
		String product = "Realize";
		String platform;
		String oS_Version;
		String Browser;

		public String getBandwidth() {
			return bandwidth;
		}

		public void setBandwidth(String bandwidth) {
			this.bandwidth = bandwidth;
		}

		public String getPlatform() {
			return platform;
		}

		public void setPlatform(String platform) {
			this.platform = platform;
		}

		public String getoS_Version() {
			return oS_Version;
		}

		public void setoS_Version(String oS_Version) {
			this.oS_Version = oS_Version;
		}

		public String getBrowser() {
			return Browser;
		}

		public void setBrowser(String browser) {
			Browser = browser;
		}

		/**
		 * Sets the time taken.
		 *
		 * @param timeTaken the new time taken
		 */
		void setTimeTaken(long timeTaken) {
			this.timeTaken = timeTaken;
		}

		/**
		 * Sets the task name.
		 *
		 * @param taskName the new task name
		 */
		void setTaskName(String taskName) {
			this.taskName = taskName;
		}

		/**
		 * Sets the data downloaded.
		 *
		 * @param dataDownloaded the new data downloaded
		 */
		void setDataDownloaded(String dataDownloaded) {
			this.dataDownloaded = dataDownloaded;
		}

		/**
		 * Gets the task name.
		 *
		 * @return the task name
		 */
		String getTaskName() {
			return taskName;
		}

		/**
		 * Gets the time taken.
		 *
		 * @return the time taken
		 */
		long getTimeTaken() {
			return timeTaken;
		}

		/**
		 * Gets the data downloaded.
		 *
		 * @return the data downloaded
		 */
		String getDataDownloaded() {
			return dataDownloaded;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "<tr>" + "<td>" + this.getBandwidth() + "</td>" + "<td>" + this.product + "</td>" + "<td>"
					+ this.getPlatform() + "</td>" + "<td>" + this.getoS_Version() + "</td>" + "<td>"
					+ this.getBrowser() + "</td>" + "<td align=center>" + this.getTaskName() + "</td>"
					+ "<td align=center>" + this.getTimeTaken() + "</td>" + "</tr>";
		}

		/**
		 * To csv format.
		 *
		 * @return the string
		 */
		public String toCSVFormat() {
			return this.getBandwidth() + "," + product + "," + this.getPlatform() + "," + this.getoS_Version() + ","
					+ this.getBrowser() + "," + this.getTaskName() + "," + this.getTimeTaken() + "\n";
		}
	}
}