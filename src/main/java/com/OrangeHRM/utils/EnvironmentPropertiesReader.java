package com.OrangeHRM.utils;

//import java.io.File;
//import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.testng.log4testng.Logger;



/**
 * EnvironmentPropertiesReader is to set the environment variable declaration
 * mapping for config properties in the UI test
 */
public class EnvironmentPropertiesReader {

	private static final Logger log = Logger.getLogger(EnvironmentPropertiesReader.class);
	private static EnvironmentPropertiesReader envProperties;

	private Properties properties;

	private EnvironmentPropertiesReader() {
		properties = loadProperties();
	}
	
	private EnvironmentPropertiesReader(String fileName) {
		properties = loadProperties(fileName);
	}

	private Properties loadProperties() {

		Properties props = new Properties();
	
		try {
//			fileInput = new FileInputStream(file);			
//			props.load(fileInput);
//			fileInput.close();
			InputStream cpr = EnvironmentPropertiesReader.class.getResourceAsStream("/config.properties");		
			props.load(cpr);
			cpr.close();
			
		} catch (FileNotFoundException e) {
			log.error("config.properties is missing or corrupt : " + e.getMessage());
		} catch (IOException e) {
			log.error("read failed due to: " + e.getMessage());
		}

		return props;
	}
	
	private Properties loadProperties(String fileName) {

		Properties props = new Properties();
	
		try {
//			fileInput = new FileInputStream(file);			
//			props.load(fileInput);
//			fileInput.close();
			InputStream cpr = EnvironmentPropertiesReader.class.getResourceAsStream("/"+fileName+".properties");		
			props.load(cpr);
			cpr.close();
			
		} catch (FileNotFoundException e) {
			log.error("config.properties is missing or corrupt : " + e.getMessage());
		} catch (IOException e) {
			log.error("read failed due to: " + e.getMessage());
		}

		return props;
	}

	public static EnvironmentPropertiesReader getInstance() {
		//if (envProperties == null) {
			envProperties = new EnvironmentPropertiesReader();
	//	}
		return envProperties;
	}
	
	public static EnvironmentPropertiesReader getInstance(String fileName) {
		//if (envProperties == null) {
			envProperties = new EnvironmentPropertiesReader(fileName);
		//}
		return envProperties;
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	public boolean hasProperty(String key) {		
		return StringUtils.isNotBlank(properties.getProperty(key));
	}
}
