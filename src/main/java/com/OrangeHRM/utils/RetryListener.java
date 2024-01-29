package com.OrangeHRM.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.annotations.ITestAnnotation;
import org.testng.IAnnotationTransformer;

public class RetryListener implements IAnnotationTransformer {

	@SuppressWarnings("rawtypes")
	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		annotation.setRetryAnalyzer(RetryAnalyzer.class);
	}

}
