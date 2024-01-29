package com.OrangeHRM.pages;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.simple.JSONObject;
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

import com.OrangeHRM.PageItem.OrangeHRMData.CandidateDetails;
import com.OrangeHRM.PageItem.OrangeHRMData.EnterEmpFullName;
import com.OrangeHRM.PageItem.OrangeHRMData.ScheduleInterviewDetails;
import com.OrangeHRM.teststeps.recruitment.RecruitmentConstants.AddCandidate;
import com.OrangeHRM.teststeps.recruitment.RecruitmentConstants.ScheduleInterview;
import com.OrangeHRM.utils.BrowserActions;
import com.OrangeHRM.utils.ElementLayer;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.utils.WaitUtils;
import com.OrangeHRM.webdriverManager.DriverManager;

import ASPIREAI.customfactories.AjaxElementLocatorFactory;
import ASPIREAI.customfactories.IFindBy;
import ASPIREAI.customfactories.PageFactory;

public class RecruitmentPage extends LoadableComponent<RecruitmentPage>{

	WebDriver driver;
	private boolean isPageLoaded;
	public ElementLayer elementLayer;
	public static List<Object> pageFactoryKey = new ArrayList<Object>();
	public static List<String> pageFactoryValue = new ArrayList<String>();

	@IFindBy(how = How.XPATH, using = "//h6[text()='Recruitment']", AI = false)
	public WebElement lblRecruitment;

	@IFindBy(how = How.CSS, using = "button[class*='oxd-button--secondary'][type='button']", AI = false)
	public WebElement btnAdd;

	@IFindBy(how = How.CSS, using = "div[class*='oxd-toast--success']", AI = false)
	public WebElement toastMsgSuccess;

	@IFindBy(how = How.XPATH, using = "//h6[text()='Add Candidate'] /parent::div/parent::div//button[@type='submit']", AI = false)
	public WebElement btnSaveCandidate;

	@IFindBy(how = How.CSS, using = "button[class*='oxd-button--success']", AI = false)
	public WebElement btnShortlist;

	@IFindBy(how = How.XPATH, using = "//h6[text()='Shortlist Candidate'] /parent::div/parent::div//button[@type='submit']", AI = false)
	public WebElement btnSaveShortlistCandidate;

	@IFindBy(how = How.XPATH, using = "//p[text()='Status: Application Initiated']", AI = false)
	public WebElement fldAppInitiated;

	@IFindBy(how = How.XPATH, using = "//p[text()='Status: Shortlisted']", AI = false)
	public WebElement fldShortlisted;

	@IFindBy(how = How.CSS, using = "button[class*='oxd-button--success']", AI = false)
	public WebElement btnScheduleInterview;

	@IFindBy(how = How.CSS, using = "input[include-employees='onlyCurrent']", AI = false)
	public WebElement fldInterviewer;

	@IFindBy(how = How.XPATH, using = "//h6[text()='Schedule Interview'] /parent::div/parent::div//button[@type='submit']", AI = false)
	public WebElement btnSaveScheduleInterview;

	@IFindBy(how = How.CSS, using = "button[class*='oxd-button--success']", AI = false)
	public WebElement btnMarkInterviewPassed;

	@IFindBy(how = How.XPATH, using = "//p[text()='Status: Interview Scheduled']", AI = false)
	public WebElement fldInterviewScheduled;

	@IFindBy(how = How.XPATH, using = "//h6[text()='Mark Interview Passed'] /parent::div/parent::div//button[@type='submit']", AI = false)
	public WebElement btnInterviewPassed;

	@IFindBy(how = How.XPATH, using = "//p[text()='Status: Interview Passed']", AI = false)
	public WebElement fldInterviewPassed;

	@IFindBy(how = How.XPATH, using = "//button[text()=' Offer Job ']", AI = false)
	public WebElement btnOfferJob;

	@IFindBy(how = How.XPATH, using = "//h6[text()='Offer Job'] /parent::div/parent::div//button[@type='submit']", AI = false)
	public WebElement btnSaveOfferJob;

	@IFindBy(how = How.XPATH, using = "//p[text()='Status: Job Offered']", AI = false)
	public WebElement fldJobOffered;

	@IFindBy(how = How.CSS, using = "button[class*='oxd-button--success']", AI = false)
	public WebElement btnHire;

	@IFindBy(how = How.XPATH, using = "//h6[text()='Hire Candidate'] /parent::div/parent::div//button[@type='submit']", AI = false)
	public WebElement btnSaveHireCandidate;

	@IFindBy(how = How.XPATH, using = "//p[text()='Status: Hired']", AI = false)
	public WebElement fldHiredStatus;

	@IFindBy(how = How.XPATH, using = "//h6[text()='Mark Interview Passed'] /parent::div/parent::div//button[@type='submit']", AI = false)
	public WebElement btnSaveMarkInterview;

	@IFindBy(how = How.CSS, using = "button[type='submit']", AI = false)
	public WebElement btnSearch;	

	@IFindBy(how = How.CSS, using = "input[placeholder*='Type for hints']", AI = false)
	public WebElement fldCandidateName;

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
		if(isPageLoaded && !(BrowserActions.waitForElementToDisplay(driver, lblRecruitment))) {
			Log.fail("Page did not open up. Site might be down.", driver);
		}
		elementLayer = new ElementLayer(driver);	
	}

	public RecruitmentPage() {

	}

	public RecruitmentPage(WebDriver driver) {
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, 10);
		PageFactory.initElements(finder, this);
		elementLayer = new ElementLayer(driver);
	}

	public RecruitmentPage(WebDriver driver,int timeout){
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, timeout);
		PageFactory.initElements(finder, this);
		elementLayer = new ElementLayer(driver);
	}

	/**
	 * Click on add candidate
	 * @author sathish.suresh
	 * @param driver
	 * @return RecruitmentPage
	 */
	public RecruitmentPage clickAddCandidates(WebDriver driver) {
		BrowserActions.click(driver, btnAdd, "Add button");
		return this;
	}

	/**
	 * Enter candidate name
	 * @author sathish.suresh
	 * @param driver
	 * @param enterEmpFullName
	 * @return RecruitmentPage
	 */
	public RecruitmentPage addCandidateName(WebDriver driver, EnterEmpFullName enterEmpFullName) {
		PimPage pimPage = new PimPage(driver);
		pimPage.addEmployeeName(driver, enterEmpFullName);
		return this;
	}

	/**
	 * select vacancy 
	 * @author sathish.suresh
	 * @param driver
	 * @param vacancy
	 * @return slected vacancy(role)
	 */
	public String selectVacancy(WebDriver driver, AddCandidate vacancy) {
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -300);");
		MyInfoPage myInfoPage = new MyInfoPage(driver);
		Object role = myInfoPage.getDropdownValueAndSelect(driver, vacancy.getName());
		return role.toString();
	}

	/**
	 * Enter candidate email
	 * @author sathish.suresh
	 * @param driver
	 * @param email
	 * @param candidateDetails
	 * @return RecruitmentPage
	 */
	public RecruitmentPage enterCandidateEmail(WebDriver driver, AddCandidate email, CandidateDetails candidateDetails) {
		MyInfoPage myInfoPage = new MyInfoPage(driver);
		myInfoPage.enterDataOnTextField(driver, email.getName(), candidateDetails.getEmail() );
		return this;
	}

	/**
	 * Enter candidate contact number
	 * @author sathish.suresh
	 * @param driver
	 * @param contactNumber
	 * @param candidateDetails
	 * @return RecruitmentPage
	 */
	public RecruitmentPage enterCandidateContactNo(WebDriver driver, AddCandidate contactNumber, CandidateDetails candidateDetails) {
		MyInfoPage myInfoPage = new MyInfoPage(driver);
		myInfoPage.enterDataOnTextField(driver, contactNumber.getName(), candidateDetails.getMobile() );
		return this;
	}

	/**
	 * Enter application date
	 * @author sathish.suresh
	 * @param driver
	 * @param dateOfApplication
	 * @param candidateDetails
	 * @return RecruitmentPage
	 */
	public RecruitmentPage enterApplicationDate(WebDriver driver, AddCandidate dateOfApplication, CandidateDetails candidateDetails) {
		MyInfoPage myInfoPage = new MyInfoPage(driver);
		myInfoPage.enterDataOnTextField(driver, dateOfApplication.getName(), candidateDetails.getCurrentDate() );
		return this;
	}

	/**
	 * Save candidate details
	 * @author sathish.suresh
	 * @param driver
	 * @return RecruitmentPage
	 */
	public RecruitmentPage SaveCandidateDetails(WebDriver driver) {
		BrowserActions.click(driver, btnSaveCandidate, "Save candidate");
		return this;
	}

	/**
	 * Save to shortlist candidate
	 * @author sathish.suresh
	 * @param driver
	 * @return RecruitmentPage
	 */
	public RecruitmentPage SaveShortlistCandidate(WebDriver driver) {
		BrowserActions.click(driver, btnSaveShortlistCandidate, "Save candidate");
		return this;
	}

	/**
	 * Save to shedule interview
	 * @author sathish.suresh
	 * @param driver
	 * @return RecruitmentPage
	 */
	public RecruitmentPage SaveScheduleInterview(WebDriver driver) {
		BrowserActions.click(driver, btnSaveScheduleInterview, "Save Schedule Interview");
		return this;
	}

	/**
	 * Save to offer job
	 * @author sathish.suresh
	 * @param driver
	 * @return RecruitmentPage
	 */
	public RecruitmentPage SaveOfferJob(WebDriver driver) {
		BrowserActions.click(driver, btnSaveOfferJob, "Save Offer Job");
		return this;
	}

	/**
	 * Save to hire candidate
	 * @author sathish.suresh
	 * @param driver
	 * @return RecruitmentPage
	 */
	public RecruitmentPage SaveHireCandidate(WebDriver driver) {
		BrowserActions.click(driver, btnSaveHireCandidate, "Save Hire Candidate");
		return this;
	}

	/**
	 * Save to mark interview passed
	 * @author sathish.suresh
	 * @param driver
	 * @return RecruitmentPage
	 */
	public RecruitmentPage SaveMarkInterview(WebDriver driver) {
		BrowserActions.click(driver, btnSaveMarkInterview, "Save Mark Interview Passed");
		return this;
	}

	/**
	 * short list candidate
	 * @author sathish.suresh
	 * @param driver
	 * @return RecruitmentPage
	 */
	public RecruitmentPage shortListCandidate(WebDriver driver) {
		BrowserActions.waitForElementToDisplay(driver, fldAppInitiated);
		BrowserActions.click(driver, btnShortlist, "Shortlist Candidate");
		//Log.messageStep("Candidate :Shortlist in interview", driver);
		return this;
	}

	/**
	 * Click on schedule interview
	 * @author sathish.suresh 
	 * @param driver
	 * @return RecruitmentPage
	 */
	public RecruitmentPage ClickscheduleInterview(WebDriver driver) {
		BrowserActions.waitForElementToDisplay(driver, fldShortlisted);
		BrowserActions.click(driver, btnScheduleInterview, "Schedule Interview");
		//Log.messageStep("Candidate :Schedule in interview", driver);
		return this;
	}

	/**
	 * Click on Offer job
	 * @author sathish.suresh
	 * @param driver
	 * @return RecruitmentPage
	 */
	public RecruitmentPage offerJob(WebDriver driver) {
		BrowserActions.waitForElementToDisplay(driver, fldInterviewPassed);
		BrowserActions.click(driver, btnOfferJob, "Offer Job");
//		Log.messageStep("Candidate got offer", driver);
		return this;
	}

	/**
	 * Click on hire
	 * @author sathish.suresh
	 * @param driver
	 * @return RecruitmentPage
	 */
	public RecruitmentPage HireInJob(WebDriver driver) {
		BrowserActions.waitForElementToDisplay(driver, fldJobOffered);
		BrowserActions.click(driver, btnHire, "Hire");
		//Log.messageStep("Candidate Hired", driver);
		return this;
	}

	/**
	 * Enter interview name
	 * @author sathish.suresh
	 * @param driver
	 * @param interviewrName
	 * @return RecruitmentPage
	 */
	public RecruitmentPage enterInterviewName(WebDriver driver, String interviewrName) {
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
		Actions actions = new Actions(driver);
		boolean found = false;
		while(!found) {
			actions.click(fldInterviewer).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.BACK_SPACE).build().perform();
			actions.sendKeys(fldInterviewer, interviewrName).pause(2*1000).build().perform();
			int size = driver.findElements(By.cssSelector("[role='listbox'] div")).size();
			if(size > 0) {
				String attribute = driver.findElement(By.cssSelector("[role='listbox'] div")).getText();
				if(! (attribute.equals("No Records Found"))) 
					found = true;
			}
		}

		By options = By.cssSelector("[role='listbox'] div");
		BrowserActions.waitForElementToDisplay(driver, options, "Options");
		int size = BrowserActions.getWebElementSize(driver, options, "Options");
		if(size == 1) {
			WebElement btnOption = options.findElement(driver);
			BrowserActions.click(driver, btnOption, "Option field");
			Log.messageStep("Interviewer Name: "+interviewrName + " selected", driver);

		}
		return this;

	}

	/**
	 * Enter interview date
	 * @author sathish.suresh
	 * @param driver
	 * @param date
	 * @param scheduleInterviewDetails
	 * @return RecruitmentPage
	 */
	public RecruitmentPage enterInterviewDate(WebDriver driver, ScheduleInterview date, ScheduleInterviewDetails scheduleInterviewDetails) {
		MyInfoPage myInfoPage = new MyInfoPage(driver);
		myInfoPage.enterDataOnTextField(driver, date.getName(), scheduleInterviewDetails.getCurrentDate() );
		return this;
	}

	/**
	 * Enter interview title
	 * @author sathish.suresh
	 * @param driver
	 * @param interviewTitle
	 * @param scheduleInterviewDetails
	 * @return RecruitmentPage
	 */
	public RecruitmentPage enterInterviewTitle(WebDriver driver, ScheduleInterview interviewTitle, ScheduleInterviewDetails scheduleInterviewDetails) {
		MyInfoPage myInfoPage = new MyInfoPage(driver);
		myInfoPage.enterDataOnTextField(driver, interviewTitle.getName(), scheduleInterviewDetails.getTitle() );
		return this;
	}

	/**
	 * Click on mark interview passed
	 * @author sathish.suresh
	 * @param driver
	 * @return RecruitmentPage
	 */
	public RecruitmentPage markInterviewPassed(WebDriver driver) {
		BrowserActions.waitForElementToDisplay(driver, fldInterviewScheduled);
		BrowserActions.click(driver, btnMarkInterviewPassed, "Mark Interview Passed");
		return this;
	}

	/**
	 * Verify hired message is displayed
	 * @author sathish.suresh
	 * @param driver
	 * @return RecruitmentPage
	 */
	public RecruitmentPage verifyHiredMsg(WebDriver driver) {
		BrowserActions.waitForElementToDisplay(driver, fldHiredStatus);
		return this;
	}

	/**
	 * Enter candidate name and selected option
	 * @author sathish.suresh
	 * @param driver
	 * @param enterEmpFullName
	 * @return RecruitmentPage
	 */
	public RecruitmentPage enterCandidateName(WebDriver driver, EnterEmpFullName enterEmpFullName) {
		Actions actions = new Actions(driver);
		boolean found = false;
		while(!found) {
			actions.click(fldCandidateName).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.BACK_SPACE).build().perform();
			actions.sendKeys(fldCandidateName, enterEmpFullName.getFirstName()+" ").pause(3*1000).build().perform();
			//actions.sendKeys(fldCandidateName, enterEmpFullName.getLastName()).pause(2*1000).build().perform();
			int size = driver.findElements(By.cssSelector("[role='listbox'] div")).size();

			if(size > 0 ) {
				String attribute = driver.findElement(By.cssSelector("[role='listbox'] div")).getText();
				if(! (attribute.equals("No Records Found"))) 
					found = true;
			}
		}

		By options = By.cssSelector("[role='listbox'] div");
		BrowserActions.waitForElementToDisplay(driver, options, "Options");
		WebElement btnOption = options.findElement(driver);
		BrowserActions.click(driver, btnOption, "Option field");
		Log.messageStep("Employee Name: "+enterEmpFullName.getFirstName() +" "+ enterEmpFullName.getLastName() + " selected", driver);
		return this;

	}

	/**
	 * Click on search button
	 * @author sathish.suresh
	 * @param driver
	 * @return RecruitmentPage
	 */
	public RecruitmentPage clickSearchBtn(WebDriver driver) {
		BrowserActions.click(driver, btnSearch, "Search button");
		return this;
	}

	/**
	 * Verify success message
	 * @author sathish.suresh
	 * @param driver
	 * @return
	 */
	public RecruitmentPage verifyToastMessage(WebDriver driver) {
		BrowserActions.waitForElementToDisplay(driver, toastMsgSuccess);
		return this;
	}

	/**
	 * Validate candidate details
	 * @author sathish.suresh
	 * @param driver
	 * @param lstCandidatInfo
	 * @return RecruitmentPage
	 * @throws InterruptedException 
	 */
	public RecruitmentPage validateCandidateDetails(WebDriver driver, ArrayList<String> lstCandidatInfo) {
		ArrayList<String> actList = new ArrayList<String>();
		By row = By.cssSelector("div[class='oxd-table-card']");
		BrowserActions.waitForElementToDisplay(driver, row, "Table rows");
		int rowSize = BrowserActions.getWebElementSize(driver, row, "Table rows");
		if(rowSize == 1){
			List<WebElement> columns = driver.findElements(By.cssSelector("div[class='oxd-table-cell oxd-padding-cell'] div"));
			for (WebElement col : columns) {
				if(!(col.getText().equals(""))  && col.getText().length() != 0) {
					String colText = BrowserActions.getText(driver, col, "Table field");
					actList.add(colText);
				}
			}
			writeJsonFile(actList);
			Collections.sort(actList);
			Collections.sort(lstCandidatInfo);
			boolean equal = actList.containsAll(lstCandidatInfo);
			Log.assertThat(equal, "Candidate Details is verified successfully", "Failed to verify Candidate details", DriverManager.getDriver());
			lstCandidatInfo.clear();
		}
		return this;
	}

	/**
	 * Write json file to add candidate details
	 * @author sathish.suresh
	 * @param CandidateInfo
	 */
	@SuppressWarnings("unchecked")
	public void writeJsonFile(ArrayList<String> CandidateInfo) {
		try {
			File file=new File("JsonTestDate\\RecruitmentDetails.json");  
			file.createNewFile(); 
			FileWriter fileWriter = new FileWriter(file); 
			System.out.println("Writing JSON object to file");  
			System.out.println("-----------------------"); 
			JSONObject jsonCandidateInfo = new JSONObject();  
			for (int i = 0; i < CandidateInfo.size(); i++) {

				switch (i) {
				case 0:
					jsonCandidateInfo.put("Vacancy", CandidateInfo.get(i));
				case 1:
					jsonCandidateInfo.put("Candidate", CandidateInfo.get(i));
				case 2:
					jsonCandidateInfo.put("Hiring Manager", CandidateInfo.get(i));
				case 3:
					jsonCandidateInfo.put("Date of Application", CandidateInfo.get(i));
				case 4:
					jsonCandidateInfo.put("Status", CandidateInfo.get(i));
				default:
					break;
				}

			}

			fileWriter.write(jsonCandidateInfo.toJSONString());
			fileWriter.flush();  
			fileWriter.close(); 
		}
		catch (Exception e) {
			e.printStackTrace();  
		}
	}
	
	public String EnterCandidateDetails(WebDriver driver, EnterEmpFullName enterEmpFullName, CandidateDetails candidateDetails) {
		this.addCandidateName(driver, EnterEmpFullName.builder()
				.firstName(enterEmpFullName.getFirstName())
				.lastName(enterEmpFullName.getLastName())
				.build());

		String selectedVacancy = this.selectVacancy(driver, AddCandidate.VACANCY);

		this.enterCandidateEmail(driver, AddCandidate.EMAIL, CandidateDetails.builder()
				.email(candidateDetails.getEmail())
				.build());

		this.enterApplicationDate(driver, AddCandidate.DATE_OF_APPLICATION, CandidateDetails.builder()
				.currentDate(candidateDetails.getCurrentDate())
				.build());

		this.enterCandidateContactNo(driver, AddCandidate.CONTACT_NUMBER, CandidateDetails.builder()
				.mobile(candidateDetails.getMobile())
				.build());
		
		return selectedVacancy;
	}
	
	public  RecruitmentPage EnterInterviewTitleAndDate(WebDriver driver, ScheduleInterviewDetails scheduleInterviewDetails ) {
		this.enterInterviewTitle(driver, ScheduleInterview.INTERVIEW_TITLE, ScheduleInterviewDetails.builder()
				.title(scheduleInterviewDetails.getTitle())
				.build());

		this.enterInterviewDate(driver, ScheduleInterview.DATE, ScheduleInterviewDetails.builder()
				.currentDate(scheduleInterviewDetails.getCurrentDate())
				.build());
		return this;
	}

	public RecruitmentPage enterCandidateForSearch(WebDriver driver, EnterEmpFullName enterEmpFullName) {
		this.enterCandidateName(driver, EnterEmpFullName.builder()
				.firstName(enterEmpFullName.getFirstName())
				.lastName(enterEmpFullName.getLastName())
				.build());
		return this;
	}

}

