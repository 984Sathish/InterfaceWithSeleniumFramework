package ASPIREAI.customfactories;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.OrangeHRM.utils.Log;

import ASPIREAI.intelligentalgorithm.JsToString;
import edu.umd.cs.findbugs.annotations.SuppressWarnings;

public class WriteProperties {

	/*
	 * public static String basePath = null, locIAlgo = null, locIAlgoList = null;
	 * 
	 * static { try { basePath = new File(".").getCanonicalPath() + File.separator +
	 * "src" + File.separator + "main" + File.separator + "java" + File.separator +
	 * "ASPIREAI" + File.separator + "intelligentalgorithm" + File.separator;
	 * locIAlgoList = basePath + "StablizedListJS.js"; locIAlgo = basePath +
	 * "StablizedJS.js";
	 * 
	 * } catch (IOException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * }
	 */

	/**
	 * @param key
	 * @param fileName
	 * @throws IOException
	 */
	public static synchronized void storePropertyKey(String key, String fileName) throws IOException {
	    if (key == null || key.isEmpty())
	        throw new NullPointerException("Store properties \"key\" is null");

	    String value = "";
	    String configFilePath = new File(".").getCanonicalPath() + File.separator + "src" + File.separator + "main"
	            + File.separator + "resources" + File.separator + "properties" + File.separator + fileName + ".properties";

	    try (FileInputStream fileInput = new FileInputStream(configFilePath)) {

	        Properties prop = new Properties();
	        prop.load(fileInput);

	        try (FileOutputStream out = new FileOutputStream(configFilePath)) {
	            prop.setProperty(key, "");
	            prop.store(out, null);
	        }

	    } catch (IOException e) {
	    	try {
				Log.exception(e);
			} catch (Exception logException) {
				// TODO Auto-generated catch block
				System.err.println("Exception: " + e.getMessage());	
		}
	    }
	}

	/**
	 * @param key
	 * @param values
	 * @param fileName
	 * @throws IOException
	 */
	public static synchronized void storeProperties(String key, ArrayList values, String fileName) throws IOException {
	    if (key == null || key.isEmpty())
	        throw new NullPointerException("Store properties \"key\" is null");

	    String value = "";

	    String configFilePath = new File(".").getCanonicalPath() + File.separator + "src" + File.separator + "main"
	            + File.separator + "resources" + File.separator + "properties" + File.separator + fileName + ".properties";

	    try (FileInputStream fileInput = new FileInputStream(configFilePath)) {

	        Properties prop = new Properties();
	        prop.load(fileInput);

	        for (Object cssLocators : values)
	            value = value + cssLocators.toString() + ",";

	        try (FileOutputStream out = new FileOutputStream(configFilePath)) {
	            prop.setProperty(key, value.substring(0, value.length() - 1));
	            prop.store(out, null);
	        }

	        // System.out.println("properties values are updated for key " + key + " values are " + value);
	    } catch (FileNotFoundException e) {
	        File newFile = new File(configFilePath);
	        boolean fileCreated = newFile.createNewFile();
	        if (fileCreated) {
	            storeProperties(key, values, fileName); // Recursively call the method after creating the file
	        } else {
	            System.err.println("Failed to create file: " + configFilePath);
	        }
	    } catch (IOException e) {
	    	try {
				Log.exception(e);
			} catch (Exception logException) {
				// TODO Auto-generated catch block
				System.err.println("Exception: " + e.getMessage());	
		}
	    }
	}


	/**
	 * @param key
	 * @param values
	 * @param fileName
	 * @param child
	 * @throws IOException
	 */
	public static synchronized void storeProperties(String key, ArrayList values, String fileName, String child)
	        throws IOException {
	    if (key == null || key.isEmpty())
	        throw new NullPointerException("Store properties \"key\" is null");

	    String value = "";

	    String configFilePath = new File(".").getCanonicalPath() + File.separator + "src" + File.separator + "main"
	            + File.separator + "resources" + File.separator + "properties" + File.separator + fileName + ".properties";

	    try (FileInputStream fileInput = new FileInputStream(configFilePath)) {

	        Properties prop = new Properties();
	        prop.load(fileInput);

	        for (Object cssLocators : values)
	            value = value + cssLocators.toString() + " " + child + ",";

	        try (FileOutputStream out = new FileOutputStream(configFilePath)) {
	            prop.setProperty(key, value.substring(0, value.length() - 1));
	            prop.store(out, null);
	        }

	        // System.out.println("properties values are updated for key " + key + " values are " + value);
	    } catch (IOException e) {
	    	try {
				Log.exception(e);
			} catch (Exception logException) {
				// TODO Auto-generated catch block
				System.err.println("Exception: " + e.getMessage());	
		}
	    }
	}

	/**
	 * @param driver
	 * @param webelement
	 * @param propertyKey
	 * @param className
	 * @throws IOException
	 */
	public static void getLocatorForWebelement(WebDriver driver, WebElement webelement, String propertyKey,
			String className) throws IOException {

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		// @SuppressWarnings("deprecation")
		// String algoContent = com.google.common.io.Files.toString(new File(locIAlgo),
		// com.google.common.base.Charsets.UTF_8);
		String algoContent = JsToString.getStablizedJs();
		// @SuppressWarnings("rawtypes")
		ArrayList properties = (ArrayList) jse.executeScript(algoContent, webelement);
		WriteProperties.storeProperties(propertyKey, properties, className);

	}

	/**
	 * @param driver
	 * @param webelement
	 * @param propertyKey
	 * @param className
	 * @param child
	 * @throws IOException
	 */
	public static void getLocatorForWebelement(WebDriver driver, WebElement webelement, String propertyKey,
			String className, String child) throws IOException {

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		// @SuppressWarnings("deprecation")
		// String algoContent = com.google.common.io.Files.toString(new File(locIAlgo),
		// com.google.common.base.Charsets.UTF_8);
		String algoContent = JsToString.stablizedJs;
		// @SuppressWarnings("rawtypes")
		ArrayList properties = (ArrayList) jse.executeScript(algoContent, webelement);
		WriteProperties.storeProperties(propertyKey, properties, className, child);

	}

	/**
	 * @param driver
	 * @param webelement
	 * @param propertyKey
	 * @param className
	 * @throws IOException
	 */
	public static void getLocatorForListOfWebelement(WebDriver driver, List<WebElement> webelement, String propertyKey,
			String className) throws IOException {

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		// @SuppressWarnings("deprecation")
		// String algoContent = com.google.common.io.Files.toString(new
		// File(locIAlgoList),
		// com.google.common.base.Charsets.UTF_8);
		String algoContentList = JsToString.stabilizedListJs;
		@SuppressWarnings("rawtypes")
		ArrayList properties = (ArrayList) jse.executeScript(algoContentList, webelement);
		WriteProperties.storeProperties(propertyKey, properties, className);

	}

	/**
	 * @param driver
	 * @param webelement
	 * @throws IOException
	 */
	public static void callAlgo(WebDriver driver, WebElement webelement) throws IOException {

		JavascriptExecutor jse = (JavascriptExecutor) driver;
		// @SuppressWarnings("deprecation")
		// String algoContent = com.google.common.io.Files.toString(new File(locIAlgo),
		// com.google.common.base.Charsets.UTF_8);
		String algoContent = JsToString.stablizedJs;
		@SuppressWarnings("rawtypes")
		ArrayList properties = (ArrayList) jse.executeScript(algoContent, webelement);
		// System.out.println(properties);

	}
}
