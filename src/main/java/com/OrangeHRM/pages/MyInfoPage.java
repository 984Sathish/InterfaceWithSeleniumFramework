package com.OrangeHRM.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.testng.Assert;

import com.OrangeHRM.PageItem.OrangeHRMData.EmployeeContactDetails;
import com.OrangeHRM.PageItem.OrangeHRMData.EmployeeJobDetails;
import com.OrangeHRM.PageItem.OrangeHRMData.EmployeePersonalDetails;
import com.OrangeHRM.PageItem.OrangeHRMData.EmployeeReportTo;
import com.OrangeHRM.teststeps.employee.EditEmployeeConstants.ContactDetails;
import com.OrangeHRM.teststeps.employee.EditEmployeeConstants.JobDetails;
import com.OrangeHRM.teststeps.employee.EditEmployeeConstants.PersonalDetails;
import com.OrangeHRM.teststeps.employee.EditEmployeeConstants.ReportTo;
import com.OrangeHRM.PageItem.OrangeHRMDataEngine;
import com.OrangeHRM.utils.BrowserActions;
import com.OrangeHRM.utils.ElementLayer;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.utils.WaitUtils;

import ASPIREAI.customfactories.AjaxElementLocatorFactory;
import ASPIREAI.customfactories.IFindBy;
import ASPIREAI.customfactories.PageFactory;

public class MyInfoPage extends LoadableComponent<MyInfoPage>{

	WebDriver driver;
	private boolean isPageLoaded;
	public ElementLayer elementLayer;
	public static List<Object> pageFactoryKey = new ArrayList<Object>();
	public static List<String> pageFactoryValue = new ArrayList<String>();


	@IFindBy(how = How.CSS, using
			= "div[class='orangehrm-edit-employee-name']", AI = false)
	public WebElement fldEmpName;

	@IFindBy(how = How.XPATH, using = "//h6[text()='Personal Details'] /parent::div/parent::div//button[@type='submit']", AI = false)
	public WebElement btnSavePersonalDetails;

	@IFindBy(how = How.XPATH, using = "//h6[text()='Custom Fields'] /parent::div/parent::div//button[@type='submit']", AI = false)
	public WebElement btnSaveCustomFlds;

	@IFindBy(how = How.CSS, using = "div[class*='oxd-toast--success']", AI = false)
	public WebElement toastMsgSuccess;

	@IFindBy(how = How.XPATH, using = "//h6[text()='Contact Details'] /parent::div/parent::div//button[@type='submit']", AI = false)
	public WebElement btnSaveContactDetails;

	@IFindBy(how = How.XPATH, using = "//h6[text()='Job Details'] /parent::div/parent::div//button[@type='submit']", AI = false)
	public WebElement btnSaveJobDetails;

	@IFindBy(how = How.XPATH, using = "//h6[text()='Assigned Supervisors'] /parent::div/parent::div//button[@type='button']", AI = false)
	public WebElement btnAddSupervisors;

	@IFindBy(how = How.CSS, using = "input[placeholder*='Type for hints']", AI = false)
	public WebElement fldEmployeeName;

	@IFindBy(how = How.XPATH, using = "//h6[text()='Add Supervisor'] /parent::div/parent::div//button[@type='submit']", AI = false)
	public WebElement btnSaveSupervisor;



	@Override
	protected void load() {
		isPageLoaded = true;
		WaitUtils.waitForPageLoad(driver);
	}

	@Override
	protected void isLoaded() throws Error {
		if(!isPageLoaded) {
			Assert.fail();
		}
		if(isPageLoaded && !(BrowserActions.waitForElementToDisplay(driver, fldEmpName))) {
			Log.fail("Page did not open up. Site might be down.", driver);
		}
		elementLayer = new ElementLayer(driver);	
	}

	public MyInfoPage() {

	}

	public MyInfoPage(WebDriver driver) {
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, 10);
		PageFactory.initElements(finder, this);
		elementLayer = new ElementLayer(driver);
	}

	public MyInfoPage(WebDriver driver,int timeout){
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, timeout);
		PageFactory.initElements(finder, this);
		elementLayer = new ElementLayer(driver);
	}

	/**
	 * Select option in dropdown based on dropdown element label
	 * @author sathish.suresh
	 * @param driver
	 * @param txtLabel
	 * @param optValue
	 */
	public void slectDropdownByLabel(WebDriver driver, String txtLabel, String optValue) {
		boolean found = false;
		By drpdwn = By.xpath("//label[text()='"+ txtLabel +"'] //parent::div //parent::div //div[@class='oxd-select-wrapper']");
		BrowserActions.waitForElementToDisplay(driver, drpdwn, txtLabel);
		WebElement drpdwnElement = drpdwn.findElement(driver);
		BrowserActions.click(driver, drpdwnElement, txtLabel);
		By options = By.cssSelector("div[role='option'] span");
		List<WebElement> optionElements = BrowserActions.getWebElements(driver, options, "Options");
		int size = BrowserActions.getWebElementSize(driver, options, "Options");
		for (int i = 0; i < size; i++) {
			String textOpt = optionElements.get(i).getText();
			if(optValue.equals(textOpt)) {
				BrowserActions.click(driver, optionElements.get(i), textOpt);
				found = true;
				break;
			}
		}
		Log.assertThat(found,"Selected "+txtLabel +": "+optValue, txtLabel +": "+ "Failed to select "+optValue, driver);	
	
	}


	/**
	 * Select option in Employee Nationality dropdown
	 * @author sathish.suresh
	 * @param driver
	 * @param personalDetails
	 * @param employeePersonalDetails
	 * @return MyInfoPage
	 */
	public MyInfoPage selectNationality(WebDriver driver, PersonalDetails personalDetails, EmployeePersonalDetails employeePersonalDetails) {
		slectDropdownByLabel(driver, personalDetails.getName(), employeePersonalDetails.getNationality() );
		return this;
	}

	/**
	 * Select option in Employee Marital status dropdown
	 * @author sathish.suresh
	 * @param driver
	 * @param personalDetails
	 * @param employeePersonalDetails
	 * @return MyInfoPage
	 */
	public MyInfoPage selectMaritalStatus(WebDriver driver, PersonalDetails personalDetails, EmployeePersonalDetails employeePersonalDetails) {
		slectDropdownByLabel(driver, personalDetails.getName(), employeePersonalDetails.getMaritalStatus() );
		return this;
	}

	/**
	 * Enter Date in date field
	 * @author sathish.suresh
	 * @param driver
	 * @param txtLabel
	 * @param optionValue
	 */
	public void enterDataOnTextField(WebDriver driver, String txtLabel, String optionValue ) {
		By txtfld = By.xpath("//label[text()='"+ txtLabel +"'] //parent::div //parent::div //input");
		BrowserActions.waitForElementToDisplay(driver, txtfld, txtLabel);
		WebElement txtfldelement = txtfld.findElement(driver);
		Actions actions = new Actions(driver);
		actions.click(txtfldelement).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.BACK_SPACE).build().perform();
		BrowserActions.type(driver, txtfldelement, optionValue, txtLabel);
		Log.messageStep("Entered "+txtLabel +": "+optionValue, driver);
	}

	/**
	 * Enter employee dob in text field
	 * @author sathish.suresh 
	 * @param driver
	 * @param personalDetails
	 * @param employeePersonalDetails
	 * @return MyInfoPage
	 */
	public MyInfoPage enterEmployeeDOB(WebDriver driver, PersonalDetails personalDetails, EmployeePersonalDetails employeePersonalDetails) {
		enterDataOnTextField(driver, personalDetails.getName(), employeePersonalDetails.getCurrentDate() );
		return this;
	}

	/**
	 * Select radio button based on element label
	 * @author sathish.suresh
	 * @param driver
	 * @param txtLabel
	 * @param optionValue
	 * @return MyInfoPage
	 */
	public MyInfoPage selectRadioBtn(WebDriver driver, String txtLabel, String optionValue) {

		By rdBtn = By.xpath("//label[text()='"+ txtLabel +"'] //parent::div //parent::div //label[text()='"+ optionValue +"'] //span");
		BrowserActions.waitForElementToDisplay(driver, rdBtn, txtLabel);
		WebElement btnRdSlected = rdBtn.findElement(driver);

		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -200);");
		BrowserActions.click(driver, btnRdSlected, txtLabel);
		return this;
	}

	/**
	 * Select employee Gender 
	 * @author sathish.suresh
	 * @param driver
	 * @param personalDetails
	 * @param employeePersonalDetails
	 * @return MyInfoPage
	 */
	public MyInfoPage selectGender(WebDriver driver, PersonalDetails personalDetails, EmployeePersonalDetails employeePersonalDetails) {
		selectRadioBtn(driver, personalDetails.getName(), employeePersonalDetails.getGender() );
		return this;
	}

	/**
	 * Save employee personal details
	 * @author sathish.suresh
	 * @param driver
	 * @return MyInfoPage
	 */
	public MyInfoPage SavePersonalDetails(WebDriver driver) {
		BrowserActions.click(driver, btnSavePersonalDetails, "Save Employee details");
		return this;
	}

	/**
	 * Save employee custom details
	 * @author sathish.suresh
	 * @param driver
	 * @return MyInfoPage
	 */
	public MyInfoPage SaveEmpCustomField(WebDriver driver) {
		BrowserActions.click(driver, btnSaveCustomFlds, "Save Employee Custom fields");
		return this;
	}

	/**
	 * Select value in Employee blood type dropdwon field
	 * @author sathish.suresh
	 * @param driver
	 * @param personalDetails
	 * @param employeePersonalDetails
	 * @return MyInfoPage
	 */
	public MyInfoPage selectBloodType(WebDriver driver, PersonalDetails personalDetails, EmployeePersonalDetails employeePersonalDetails) {
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
		slectDropdownByLabel(driver, personalDetails.getName(), employeePersonalDetails.getBloodType() );
		return this;
	}

	/**
	 * Verify success message is displayed
	 * @author sathish.suresh
	 * @param driver
	 * @return PimPage
	 */
	public PimPage verifyToastMessage(WebDriver driver) {
		BrowserActions.waitForElementToDisplay(driver, toastMsgSuccess);
		return new PimPage(driver).get();
	}

	/**
	 * Enter employee street
	 * @author sathish.suresh
	 * @param driver
	 * @param street1
	 * @param employeeContactDetails
	 * @return MyInfoPage
	 */
	public MyInfoPage enterStreet1(WebDriver driver, ContactDetails street1, EmployeeContactDetails employeeContactDetails) {
		enterDataOnTextField(driver, street1.getName(), employeeContactDetails.getStreet() );
		return this;
	}

	/**
	 * Enter employee city
	 * @author sathish.suresh
	 * @param driver
	 * @param city
	 * @param employeeContactDetails
	 * @return MyInfoPage
	 */
	public MyInfoPage enterCity(WebDriver driver, ContactDetails city, EmployeeContactDetails employeeContactDetails) {
		enterDataOnTextField(driver, city.getName(), employeeContactDetails.getCity() );
		return this;
	}

	/**
	 * Enter employee state
	 * @author sathish.suresh
	 * @param driver
	 * @param state
	 * @param employeeContactDetails
	 * @return MyInfoPage
	 */
	public MyInfoPage enterState(WebDriver driver, ContactDetails state, EmployeeContactDetails employeeContactDetails) {
		enterDataOnTextField(driver, state.getName(), employeeContactDetails.getState() );
		return this;
	}

	/**
	 * Enter zipcode
	 * @author sathish.suresh
	 * @param driver
	 * @param zipCode
	 * @param employeeContactDetails
	 * @return MyInfoPage
	 */
	public MyInfoPage enterZipCode(WebDriver driver, ContactDetails zipCode, EmployeeContactDetails employeeContactDetails) {
		enterDataOnTextField(driver, zipCode.getName(), employeeContactDetails.getZipCode() );
		return this;
	}

	/**
	 * slect value in employee country dropdown
	 * @author sathish.suresh
	 * @param driver
	 * @param country
	 * @param employeeContactDetails
	 * @return MyInfoPage
	 */
	public MyInfoPage selectCountry(WebDriver driver, ContactDetails country, EmployeeContactDetails employeeContactDetails) {
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -200);");
		slectDropdownByLabel(driver, country.getName(), employeeContactDetails.getCountry() );
		return this;
	}

	/**
	 * enter employee mobile number
	 * @author sathish.suresh
	 * @param driver
	 * @param mobile
	 * @param employeeContactDetails
	 * @return MyInfoPage
	 */
	public MyInfoPage enterMobileNumber(WebDriver driver, ContactDetails mobile, EmployeeContactDetails employeeContactDetails) {
		enterDataOnTextField(driver, mobile.getName(), employeeContactDetails.getMobile() );
		return this;
	}

	/**
	 * enter employee email address
	 * @author sathish.suresh
	 * @param driver
	 * @param workEmail
	 * @param employeeContactDetails
	 * @return MyInfoPage
	 */
	public MyInfoPage enterEmailAddress(WebDriver driver, ContactDetails workEmail, EmployeeContactDetails employeeContactDetails) {
		enterDataOnTextField(driver, workEmail.getName(), employeeContactDetails.getEmail() );
		return this;
	}

	/**
	 * Save employee contact details
	 * @author sathish.suresh
	 * @param driver
	 * @return MyInfoPage
	 */
	public MyInfoPage SaveEmpContactDetails(WebDriver driver) {
		BrowserActions.click(driver, btnSaveContactDetails, "Save Employee contact details");
		return this;
	}

	/**
	 * Add employee personal Details (nationality, marital status, dob and gender)
	 * @author sathish.suresh
	 * @param driver
	 * @param employeePersonalDetails
	 * @return MyInfoPage
	 */
	public MyInfoPage AddEmployeePersonalDetails(WebDriver driver, EmployeePersonalDetails employeePersonalDetails) {
		selectNationality(driver, PersonalDetails.NATIONALITY, EmployeePersonalDetails.builder()
				.nationality(employeePersonalDetails.getNationality())
				.build());

		selectMaritalStatus(driver, PersonalDetails.MARITAL_STATUS, EmployeePersonalDetails.builder()
				.maritalStatus(employeePersonalDetails.getMaritalStatus())
				.build());

		enterEmployeeDOB(driver, PersonalDetails.DOB, EmployeePersonalDetails.builder()
				.currentDate(employeePersonalDetails.getCurrentDate())
				.build());

		selectGender(driver, PersonalDetails.GENDER, EmployeePersonalDetails.builder()
				.gender(employeePersonalDetails.getGender())
				.build());

		return this;
	}

	/**
	 * Add employee custom field (blood type)
	 * @author sathish.suresh
	 * @param driver
	 * @param employeePersonalDetails
	 * @return MyInfoPage
	 */
	public MyInfoPage addEmployeeCustomFields(WebDriver driver, EmployeePersonalDetails employeePersonalDetails) {
		selectBloodType(driver, PersonalDetails.BLOOD_TYPE, EmployeePersonalDetails.builder()
				.bloodType(employeePersonalDetails.getBloodType())
				.build());
		return this;
	}

	/**
	 * Add employee contact details (street, city, state, zipcode, mobile no, email)
	 * @author sathish.suresh
	 * @param driver
	 * @param employeeContactDetails
	 * @return MyInfoPage
	 */
	public MyInfoPage AddEmployeeContactDetails(WebDriver driver, EmployeeContactDetails employeeContactDetails ) {
		enterStreet1(driver, ContactDetails.STREET_1, EmployeeContactDetails.builder()
				.street(employeeContactDetails.getStreet())
				.build());

		enterCity(driver, ContactDetails.CITY, EmployeeContactDetails.builder()
				.city(employeeContactDetails.getCity())
				.build());

		enterState(driver, ContactDetails.STATE, EmployeeContactDetails.builder()
				.state(employeeContactDetails.getState())
				.build());

		enterZipCode(driver, ContactDetails.ZIP_CODE, EmployeeContactDetails.builder()
				.zipCode(employeeContactDetails.getZipCode())
				.build());

		selectCountry(driver, ContactDetails.COUNTRY, EmployeeContactDetails.builder()
				.country(employeeContactDetails.getCountry())
				.build());

		enterMobileNumber(driver, ContactDetails.MOBILE, EmployeeContactDetails.builder()
				.mobile(employeeContactDetails.getMobile())
				.build());

		enterEmailAddress(driver, ContactDetails.WORK_EMAIL, EmployeeContactDetails.builder()
				.email(employeeContactDetails.getEmail())
				.build());

		return this;

	}

	/**
	 * Enter employee join date
	 * @author sathish.suresh 
	 * @param driver
	 * @param joinedDate
	 * @param employeeDetails
	 * @return MyInfoPage
	 */
	public MyInfoPage enterJoinedDate(WebDriver driver, JobDetails joinedDate, EmployeeJobDetails employeeJobDetails ) {
		enterDataOnTextField(driver, joinedDate.getName(), employeeJobDetails.getCurrentDate() );
		return this;
	}

	/**
	 * Get dropdown value and select any of them
	 * @author sathish.suresh
	 * @param driver
	 * @param txtLabel
	 * @return MyInfoPage
	 */
	public Object getDropdownValueAndSelect(WebDriver driver, String txtLabel) {
		ArrayList<String> drpValues = new ArrayList<String> ();
		By drpdwn = By.xpath("//label[text()='"+ txtLabel +"'] //parent::div //parent::div //div[@class='oxd-select-wrapper']");
		BrowserActions.waitForElementToDisplay(driver, drpdwn, txtLabel);
		WebElement drpdwnElement = drpdwn.findElement(driver);
		BrowserActions.click(driver, drpdwnElement, txtLabel);
		By options = By.cssSelector("div[role='option'] span");
		List<WebElement> optionElements = BrowserActions.getWebElements(driver, options, "Options");
		int size = BrowserActions.getWebElementSize(driver, options, "Options");
		for (int i = 0; i < size; i++) {
			drpValues.add( optionElements.get(i).getText() );	
		}
		Random rand = new Random(); 
		String drpValue = drpValues.get(rand.nextInt(drpValues.size()));

		By drpdwnValue = By.xpath("//div[@role='option'] //span[text()='"+ drpValue +"']");
		BrowserActions.waitForElementToDisplay(driver, drpdwnValue, txtLabel);
		WebElement elementDrpVal = drpdwnValue.findElement(driver);
		BrowserActions.click(driver, elementDrpVal, drpValue);
		Log.messageStep(txtLabel +": "+drpValue+ " selected", driver);
		if(txtLabel.equals("Job Title") || txtLabel.equals("Sub Unit") || txtLabel.equals("Employment Status") || txtLabel.equals("Vacancy"))  {
			return drpValue;
		}
		return this;
	}

	/**
	 * Select employee job details
	 * @author sathish.suresh
	 * @param driver
	 * @param label
	 * @return jobInfoList
	 */
	public Object selectJobDetails(WebDriver driver, JobDetails label ) {
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -300);");
		Object jobInfoList = getDropdownValueAndSelect(driver, label.getName());
		return jobInfoList;
	}

	/**
	 * Save employee job details
	 * @author sathish.suresh
	 * @param driver
	 * @return MyInfoPage
	 */
	public MyInfoPage SaveEmpJobDetails(WebDriver driver) {
		BrowserActions.click(driver, btnSaveJobDetails, "Save Employee Job details");
		return this;
	}

	/**
	 * Add employee Job details( joined date, job title, job category, sub unit, location, status)
	 * @author sathish.suresh
	 * @param driver
	 * @param employeeDetails
	 * @return selectJobDetails
	 */
	public ArrayList<String> addEmployeeJobDetails(WebDriver driver, EmployeeJobDetails employeeJobDetails) {

		ArrayList<String> selectJobDetails = new ArrayList<String>(); 
		enterJoinedDate(driver, JobDetails.JOINED_DATE, EmployeeJobDetails.builder()
				.currentDate(employeeJobDetails.getCurrentDate())
				.build());

		selectJobDetails.add( (String) selectJobDetails(driver, JobDetails.JOB_TITLE));

		selectJobDetails(driver, JobDetails.JOB_CATEGORY);

		selectJobDetails.add( (String) selectJobDetails(driver, JobDetails.SUB_UNIT) );

		selectJobDetails(driver, JobDetails.LOCATION);

		selectJobDetails.add( (String) selectJobDetails(driver, JobDetails.EMPLOYMENT_STATUS) );

		return selectJobDetails;
	}

	/**
	 * Enter employee name and select option 
	 * @author sathish.suresh
	 * @param driver
	 * @param firstName
	 * @param lastName
	 * @return employee fullname ( firstname + lastname)
	 */
	public String enterEmployeeName(WebDriver driver, String firstName, String lastName) {
		Actions actions = new Actions(driver);
		boolean found = false;
		while(!found) {
			actions.click(fldEmployeeName).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.BACK_SPACE).build().perform();
			actions.sendKeys(fldEmployeeName, firstName+" ").pause(2*1000).build().perform();
			actions.sendKeys(fldEmployeeName, lastName).pause(2*1000).build().perform();
			int size = driver.findElements(By.cssSelector("[role='listbox'] div")).size();
			if(size > 0) {
				String attribute = driver.findElement(By.cssSelector("[role='listbox'] div")).getText();
				if(! (attribute.equals("No Records Found"))) 
					found = true;
			}
		}
		By options = By.cssSelector("[role='listbox'] div");
		BrowserActions.waitForElementToDisplay(driver, options, "Options");
		WebElement btnOption = options.findElement(driver);
		BrowserActions.click(driver, btnOption, "Option field");
		return firstName+" "+lastName;
	}

	/**
	 * Select option in report method dropdown
	 * @author sathish.suresh
	 * @param driver
	 * @param reportingMethod
	 * @param employeeReportTo
	 * @return MyInfoPage
	 */
	public MyInfoPage selectReportingMethod(WebDriver driver, ReportTo reportingMethod, EmployeeReportTo employeeReportTo) {
		slectDropdownByLabel(driver, reportingMethod.getName(), employeeReportTo.getReportMethod() );
		return this;
	}

	/**
	 * click to Add supervisior details
	 * @author sathish.suresh
	 * @param driver
	 * @return MyInfoPage
	 */
	public MyInfoPage addSupervisorDetails(WebDriver driver) {
		BrowserActions.click(driver, btnAddSupervisors, "Add Supervisor details");
		return this;

	}

	/**
	 * Save supervisior details
	 * @author sathish.suresh
	 * @param driver
	 * @return MyInfoPage
	 */
	public MyInfoPage saveSupervisorDetails(WebDriver driver) {
		BrowserActions.click(driver, btnSaveSupervisor, "Save Supervisor details");
		return this;

	}


}
