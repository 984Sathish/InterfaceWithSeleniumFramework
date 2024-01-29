package com.OrangeHRM.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * ElementLayer page is used to verify each page elements.
 * 
 * We can declare and initialize this class on each page object classes
 */
public class ElementLayer {

	private final WebDriver driver;

	/**
	 * Constructor class for ElementLayer, here we initializing the driver for page
	 * 
	 * @param driver -
	 */
	public ElementLayer(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Verify if expected page WebElements are present
	 * <p>
	 * If expected element is present on the current page, add to list of
	 * value/fields to actualElement list and then compare to expectedElements
	 * 
	 * @param expectedElements - expected WebElement values
	 * @param obj              - the page object the elements are on
	 * @return true if both the lists are equal, else returns false
	 * @throws Exception - one of 4 possibilities
	 */
	public boolean verifyPageElements(List<String> expectedElements, Object obj) throws Exception {
		List<String> actual_elements = new ArrayList<String>();
		for (String expEle : expectedElements) {
			Field f = null;
			try {
				f = obj.getClass().getDeclaredField(expEle);
				f.setAccessible(true);
			} catch (NoSuchFieldException | SecurityException e1) {
				throw new Exception(
						"No such a field present on this page, Please check the expected list values:: " + expEle);
			}
			WebElement element = null;
			try {
				element = ((WebElement) f.get(obj));
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				Log.exception(e1);
			}
			if (Utils.waitForElement(driver, element, 2)) {
				actual_elements.add(expEle);
			}
		}
		return Utils.compareTwoList(expectedElements, actual_elements);
	}

	/**
	 * Verify if expected page WebElements are disabled
	 * <p>
	 * If expected element is present on the current page, add to list of
	 * value/fields to actualElement list and then compare to expectedElements
	 * 
	 * @param expectedElements - expected WebElement values
	 * @param obj              - the page object the elements are on
	 * @return true if both the lists are equal, else returns false
	 * @throws Exception - one of 4 possibilities
	 */
	public boolean verifyPageElementsDisabled(List<String> expectedElements, Object obj) throws Exception {
		List<String> actual_elements = new ArrayList<String>();
		for (String expEle : expectedElements) {
			Field f = null;
			try {
				f = obj.getClass().getDeclaredField(expEle);
				f.setAccessible(true);
			} catch (NoSuchFieldException | SecurityException e1) {
				throw new Exception(
						"No such a field present on this page, Please check the expected list values:: " + expEle);
			}
			WebElement element = null;
			try {
				element = ((WebElement) f.get(obj));
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				Log.exception(e1);
			}
			if (Utils.waitForDisabledElement(driver, element, Utils.maxElementWait)) {
				actual_elements.add(expEle);
			}
		}
		return Utils.compareTwoList(expectedElements, actual_elements);
	}

	/**
	 * Verify if expected page WebElements are checked/selected
	 * <p>
	 * If expected element checked/selected on the current page, add to list of
	 * value/fields to actualElement list and then compare to expectedElements
	 * 
	 * @param expectedElements - expected WebElement values
	 * @param obj              - the page object the elements are on
	 * @return true if both the lists are equal, else returns false
	 * @throws Exception - one of 4 possibilities
	 */
	public boolean verifyPageElementsChecked(List<String> expectedElements, Object obj) throws Exception {
		List<String> actual_elements = new ArrayList<String>();
		for (String expEle : expectedElements) {
			Field f = null;
			try {
				f = obj.getClass().getDeclaredField(expEle);
				f.setAccessible(true);
			} catch (NoSuchFieldException | SecurityException e1) {
				throw new Exception(
						"No such field present on this page, Please check the expected list values:: " + expEle);
			}
			WebElement element = null;
			try {
				element = ((WebElement) f.get(obj));
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				Log.exception(e1);
			}
			/*
			 * (new WebDriverWait(driver, 5).pollingEvery(250,
			 * TimeUnit.MILLISECONDS).ignoring( NoSuchElementException.class)
			 * .withMessage("Creat an Event mobel box did not open"))
			 * .until(ExpectedConditions.visibilityOf(element));
			 */
			if(element != null) {
			if (element.isSelected()) {
				actual_elements.add(expEle);
			}
			}
		}
		return Utils.compareTwoList(expectedElements, actual_elements);
	}

	/**
	 * Verify the lack of presence of page WebElements
	 * <p>
	 * If expected element is NOT present on this current page, add to list of
	 * value/fields to actualElement list and then compare to expectedElements
	 * 
	 * @param expectedNotToSee - expected non-existing WebElement values
	 * @param obj              - the page object the elements should not be on
	 * @return true if both the lists are equal, else returns false
	 * @throws Exception - one of 2 possibilities
	 */
	public boolean verifyPageElementsDoNotExist(List<String> expectedNotToSee, Object obj) throws Exception {
		List<String> nonexisting_elements = new ArrayList<String>();
		for (String expEle : expectedNotToSee) {
			Field f = null;
			WebElement element = null;
			try {
				f = obj.getClass().getDeclaredField(expEle);
				f.setAccessible(true);
				element = ((WebElement) f.get(obj));
			} catch (NoSuchFieldException | SecurityException e1) {
				throw new Exception(
						"No such field present on this page, Please check the expected list values:: " + expEle);
			}
			if (!Utils.waitForElement(driver, element, 2)) {
				nonexisting_elements.add(expEle);
			}
		}
		return Utils.compareTwoList(expectedNotToSee, nonexisting_elements);
	}

	/**
	 * Verify if expected page List WebElements are present
	 * <p>
	 * If size of the list element is greater than zero and first element from
	 * expected list elements present on the current page, add to list of
	 * value/fields to actualElement list and then compare to expectedElements
	 * 
	 * @param expectedElements - expected List WebElement values
	 * @param obj              - the page object the elements are on
	 * @return true if both the lists are equal, else returns false
	 * @throws Exception - one of 5 possibilities
	 */
	@SuppressWarnings("unchecked")
	public boolean verifyPageListElements(List<String> expectedElements, Object obj) throws Exception {
		List<String> actual_elements = new ArrayList<String>();
		for (String expEle : expectedElements) {
			Field f = null;
			try {
				f = obj.getClass().getDeclaredField(expEle);
				f.setAccessible(true);
			} catch (NoSuchFieldException | SecurityException e1) {
				throw new Exception(
						"No such fields present on this page, Please check the expected list values:: " + expEle);
			}
			List<WebElement> elements = null;
			try {
				elements = ((List<WebElement>) f.get(obj));
			} catch (ClassCastException | IllegalArgumentException | IllegalAccessException e1) {
				Log.exception(e1);
			}
			if(elements != null) {
			if (elements.size() > 0 && Utils.waitForElement(driver, elements.get(0))) {
				actual_elements.add(expEle);
			}
			}
		}
		return Utils.compareTwoList(expectedElements, actual_elements);
	}

	/**
	 * Verify the lack of presence of page WebElements
	 * <p>
	 * If expected element is NOT present on this current page, add to list of
	 * value/fields to actualElement list and then compare to expectedElements
	 * 
	 * @param expectedNotToSee - expected non-existing WebElement values
	 * @param obj              - the page object the elements should not be on
	 * @return true if both the lists are equal, else returns false
	 * @throws Exception - one of 2 possibilities
	 */
	@SuppressWarnings("unchecked")
	public boolean verifyPageListElementsDoNotExist(List<String> expectedNotToSee, Object obj) throws Exception {
		List<String> actual_elements = new ArrayList<String>();
		for (String expEle : expectedNotToSee) {
			Field f = null;
			try {
				f = obj.getClass().getDeclaredField(expEle);
				f.setAccessible(true);
			} catch (NoSuchFieldException | SecurityException e1) {
				throw new Exception(
						"No such fields present on this page, Please check the expected list values:: " + expEle);
			}
			List<WebElement> elements = null;
			try {
				elements = ((List<WebElement>) f.get(obj));
			} catch (ClassCastException | IllegalArgumentException | IllegalAccessException e1) {
				Log.exception(e1);
			}
			if(elements != null) {
			if (elements.size() == 0) {
				actual_elements.add(expEle);
			}
			}
		}
		return Utils.compareTwoList(expectedNotToSee, actual_elements);
	}

	/**
	 * 
	 * 
	 * @param expectedElements - expected WebElement values
	 * @param obj              - the page object for the elements presence
	 * @return true if both the lists are equal, else returns false
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean verifyPageListElementsDisabled(List<String> expectedElements, Object obj) throws Exception {
		List<String> actual_elements = new ArrayList<String>();
		for (String expEle : expectedElements) {
			Field f = null;
			try {
				f = obj.getClass().getDeclaredField(expEle);
				f.setAccessible(true);
			} catch (NoSuchFieldException | SecurityException e1) {
				throw new Exception(
						"No such a field present on this page, Please check the expected list values:: " + expEle);
			}
			List<WebElement> elements = null;
			try {
				elements = ((List<WebElement>) f.get(obj));
			} catch (ClassCastException | IllegalArgumentException | IllegalAccessException e1) {
				Log.exception(e1);
			}
			if(elements != null) {
			if (elements.size() > 0 && Utils.waitForDisabledElement(driver, elements.get(0), Utils.maxElementWait)) {
				actual_elements.add(expEle);
			}
			}
		}
		return Utils.compareTwoList(expectedElements, actual_elements);
	}
}