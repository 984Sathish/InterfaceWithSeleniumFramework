package com.OrangeHRM.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * CommandPrompt class used to execute the specified string command in a
 * separate process.
 * 
 */
public class CommandPrompt {
	static Process p;

	/**
	 * Method to execute the specified command
	 * 
	 * @param command
	 * @return String
	 * @exception InterruptedException,IOException
	 */
	public String runCommand(String command) throws InterruptedException, IOException {
		p = Runtime.getRuntime().exec(command);

		BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = "";
		String allLine = "";
		while ((line = r.readLine()) != null) {
			allLine = allLine + "" + line + "\n";
		}
		return allLine;
	}

	/**
	 * This method works only for linux with nested commands
	 * 
	 * @param command
	 * @return String
	 * @exception InterruptedException,IOException
	 */
	public String runNestedCommand(String command) throws InterruptedException, IOException {
		List<String> commands = new ArrayList<String>();
		commands.add("/bin/sh");
		commands.add("-c");
		commands.add(command);
		ProcessBuilder builder = new ProcessBuilder(commands);

		Process process = builder.start();
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		String allLine = "";
		String line;
		while ((line = br.readLine()) != null) {
			allLine = allLine + "" + line + "\n";
		}
		return allLine;
	}
}
