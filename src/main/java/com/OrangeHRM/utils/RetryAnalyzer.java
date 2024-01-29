package com.OrangeHRM.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer Class to retry Analyzer
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private static EnvironmentPropertiesReader configProperty = EnvironmentPropertiesReader.getInstance();
    private int maxRetryCount = configProperty.hasProperty("MaxRetryCount")?Integer.parseInt(configProperty.getProperty("MaxRetryCount")):1;
    		

    /**
     * retry method returns 'true' if the test method has to be retried else 'false'
     * and it takes the 'Result' as parameter of the test method that just ran
     * 
     * @param result
     * @return boolean - True or False
     */
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            System.out.println("Retrying test " + result.getName() + " with status "
                    + getResultStatusName(result.getStatus()) + " for the " + (retryCount + 1) + " time(s).");
            retryCount++;
            return true;
        }
        return false;
    }

    /**
     * To get the result from the status
     * 
     * @param status
     * @return resultName - Success, Failure or Skip
     */
    public String getResultStatusName(int status) {
        String resultName = null;
        if (status == 1)
            resultName = "SUCCESS";
        if (status == 2)
            resultName = "FAILURE";
        if (status == 3)
            resultName = "SKIP";
        return resultName;
    }
}