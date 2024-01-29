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

import com.OrangeHRM.PageItem.OrangeHRMData.CreateClaimDetails;
import com.OrangeHRM.PageItem.OrangeHRMData.EnterEmpFullName;
import com.OrangeHRM.PageItem.OrangeHRMData.ExpenseDetails;
import com.OrangeHRM.teststeps.claim.ClaimConstants.AddExpense;
import com.OrangeHRM.teststeps.claim.ClaimConstants.CreateClaimRequest;
import com.OrangeHRM.teststeps.claim.ClaimConstants.NavName;
import com.OrangeHRM.utils.BrowserActions;
import com.OrangeHRM.utils.ElementLayer;
import com.OrangeHRM.utils.Log;
import com.OrangeHRM.utils.WaitUtils;

import ASPIREAI.customfactories.AjaxElementLocatorFactory;
import ASPIREAI.customfactories.IFindBy;
import ASPIREAI.customfactories.PageFactory;

public class ClaimPage extends LoadableComponent<ClaimPage>{

	WebDriver driver;
	private boolean isPageLoaded;
	public ElementLayer elementLayer;
	public static List<Object> pageFactoryKey = new ArrayList<Object>();
	public static List<String> pageFactoryValue = new ArrayList<String>();


	@IFindBy(how = How.XPATH, using = "//h6[text()='Claim']", AI = false)
	public WebElement headerClaim;

	@IFindBy(how = How.XPATH, using = "//h6[text()='Create Claim Request'] /parent::div/parent::div//button[@type='submit']", AI = false)
	public WebElement btnCreate;

	@IFindBy(how = How.XPATH, using = "//h6[text()='Expenses'] /parent::div //button", AI = false)
	public WebElement btnAddExpense;

	@IFindBy(how = How.XPATH, using = "//label[text()='Reference Id']/parent::div/parent::div //input", AI = false)
	public WebElement fldReferenceId;

	@IFindBy(how = How.CSS, using = "button[type='submit']", AI = false)
	public WebElement btnSaveExpense;

	@IFindBy(how = How.CSS, using = "button[type='submit']", AI = false)
	public WebElement btnSearch;

	@IFindBy(how = How.CSS, using = "button[class*='oxd-button--secondary'][type='button']", AI = false)
	public WebElement btnSubmit;

	@IFindBy(how = How.CSS, using = "p[class='oxd-text oxd-text--p']", AI = false)
	public WebElement fldTotalAmt;

	@IFindBy(how = How.CSS, using = "div[class*='oxd-toast--success']", AI = false)
	public WebElement toastMsgSuccess;

	@IFindBy(how = How.XPATH, using = "//span[text()='(1) Record Found']", AI = false)
	public WebElement txtSearchResult;


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
		if(isPageLoaded && !(BrowserActions.waitForElementToDisplay(driver, headerClaim))) {
			Log.fail("Page did not open up. Site might be down.", driver);
		}
		elementLayer = new ElementLayer(driver);	
	}

	public ClaimPage() {

	}

	public ClaimPage(WebDriver driver) {
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, 10);
		PageFactory.initElements(finder, this);
		elementLayer = new ElementLayer(driver);
	}

	public ClaimPage(WebDriver driver,int timeout){
		this.driver = driver;
		ElementLocatorFactory finder = new AjaxElementLocatorFactory(driver, timeout);
		PageFactory.initElements(finder, this);
		elementLayer = new ElementLayer(driver);
	}


	/**
	 * Select top nav menu based on nav menu name
	 * @author sathish.suresh
	 * @param driver
	 * @param navMenu
	 * @return ClaimPage
	 */
	public ClaimPage selectTopNavMenu(WebDriver driver, NavName navMenu) {
		By navTab = By.xpath("//a[@class='oxd-topbar-body-nav-tab-item' and text()='"+ navMenu.getName() +"']");
		BrowserActions.waitForElementToDisplay(driver, navTab, navMenu.getName());
		WebElement topNavMenu = navTab.findElement(driver);
		BrowserActions.click(driver, topNavMenu, navMenu.getName());
		//Log.messageStep("Nav menu : " + navMenu.getName()+ " selected" , driver);
		return this;
	}

	/**
	 * Enter Employee name
	 * @author sathish.suresh
	 * @param driver
	 * @param enterEmpFullName
	 * @return ClaimPage
	 */
	public ClaimPage enterEmployeeName(WebDriver driver, EnterEmpFullName enterEmpFullName) {
		PimPage pimPage = new PimPage(driver);
		pimPage.enterEmployeeName(driver, enterEmpFullName);
		return this;
	}

	/**
	 * click on create new claim request
	 * @author sathish.suresh
	 * @param driver
	 * @return ClaimPage
	 */
	public ClaimPage clickCreateClaimRequest(WebDriver driver) {
		BrowserActions.click(driver, btnCreate, "Create claim request button");
		return this;
	}

	public String getClaimReferenceId(WebDriver driver) {
		BrowserActions.waitForElementToDisplay(driver, fldReferenceId);
		String refId;
		while(true) {
			refId = fldReferenceId.getAttribute("value");
			if(refId.length() != 0)
				break;
		}
		//return BrowserActions.getAttribute(driver, fldReferenceId, "value");
		return refId;
	}

	/**
	 * Click on Add expense
	 * @author sathish.suresh
	 * @param driver
	 * @return ClaimPage
	 */
	public ClaimPage addExpense(WebDriver driver) {
		BrowserActions.click(driver, btnAddExpense, "Add expense button");
		return this;
	}

	/**
	 * Click on Save expense
	 * @author sathish.suresh
	 * @param driver
	 * @return ClaimPage
	 */
	public ClaimPage saveExpense(WebDriver driver) {
		BrowserActions.click(driver, btnSaveExpense, "Save expense button");
		return this;
	}

	/**
	 * Click to Search Employee details
	 * @author sathish.suresh
	 * @param driver
	 * @return ClaimPage
	 */
	public ClaimPage clickSearchBtn(WebDriver driver) {
		BrowserActions.click(driver, btnSearch, "Search button");
		return this;
	}	

	/**
	 * Click on Save expense
	 * @author sathish.suresh
	 * @param driver
	 * @return ClaimPage
	 */
	public ClaimPage submitClaim(WebDriver driver) {
		BrowserActions.click(driver, btnSubmit, "Submit claim button");
		return this;
	}

	/**
	 * Select option in event dropdown
	 * @param driver
	 * @param createClaimDetails 
	 * @param event 
	 * @return ClaimPage
	 */
	public ClaimPage selectEvent(WebDriver driver, CreateClaimRequest event, CreateClaimDetails createClaimDetails) {
		MyInfoPage myInfoPage = new MyInfoPage(driver);
		myInfoPage.slectDropdownByLabel(driver, event.getName(), createClaimDetails.getEvent() );
		return this;
	}

	/**
	 * Select option in currency dropdown
	 * @param driver
	 * @param createClaimDetails 
	 * @param currency 
	 * @return ClaimPage
	 */
	public ClaimPage selectCurrency(WebDriver driver, CreateClaimRequest currency, CreateClaimDetails createClaimDetails) {
		MyInfoPage myInfoPage = new MyInfoPage(driver);
		myInfoPage.slectDropdownByLabel(driver, currency.getName(), createClaimDetails.getCurrency() );
		return this;
	}

	/**
	 * Select option in expense type dropdown
	 * @param driver
	 * @param expenseDetails 
	 * @param expenseType 
	 * @return ClaimPage
	 */
	public ClaimPage selectExpenseType(WebDriver driver, AddExpense expenseType, ExpenseDetails expenseDetails) {
		MyInfoPage myInfoPage = new MyInfoPage(driver);
		myInfoPage.slectDropdownByLabel(driver, expenseType.getName(), expenseDetails.getExpenseType() );
		return this;
	}

	/**
	 * enter expense date
	 * @param driver
	 * @param expenseDetails 
	 * @param date 
	 * @return ClaimPage
	 */
	public ClaimPage enterDate(WebDriver driver, AddExpense date, ExpenseDetails expenseDetails) {
		MyInfoPage myInfoPage = new MyInfoPage(driver);
		myInfoPage.enterDataOnTextField(driver, date.getName(), expenseDetails.getCurrentDate() );
		return this;
	}

	/**
	 * Enter expense amount
	 * @param driver
	 * @param expenseDetails 
	 * @param amount 
	 * @return ClaimPage
	 */
	public ClaimPage enterAmount(WebDriver driver, AddExpense amount, ExpenseDetails expenseDetails) {
		MyInfoPage myInfoPage = new MyInfoPage(driver);
		myInfoPage.enterDataOnTextField(driver, amount.getName(), expenseDetails.getAmount() );
		return this;
	}


	/**
	 * Enter claim reference id
	 * @param driver
	 * @param referenceId 
	 * @return ClaimPage
	 */
	public ClaimPage enterClaimReferenceId(WebDriver driver, String referenceId) {
		Actions actions = new Actions(driver);
		boolean found = false;
		while(!found) {
			actions.click(fldReferenceId).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.BACK_SPACE).build().perform();
			actions.sendKeys(fldReferenceId, referenceId).pause(2*1000).build().perform();
			int size = driver.findElements(By.cssSelector("[role='listbox'] div")).size();
			if(size > 0) {
				found = true;
			}
		}

		By options = By.cssSelector("[role='listbox'] div");
		BrowserActions.waitForElementToDisplay(driver, options, "Options");
		int size = BrowserActions.getWebElementSize(driver, options, "Options");
		if(size == 1) {
			WebElement btnOption = options.findElement(driver);
			BrowserActions.click(driver, btnOption, "Option field");
		}
		return this;
	}

	public ClaimPage verifyTotalAmount(WebDriver driver, String currency, String amount) {
		BrowserActions.waitForElementToDisplay(driver, txtSearchResult);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0, 300);");
		String txtTotalAmt = BrowserActions.getText(driver, fldTotalAmt, "Total amount");
		String expText = "Total Amount ("+currency+") : "+amount;  //Total Amount (Afghanistan Afghani) : 123.00
		Log.assertThat(expText.equals(txtTotalAmt), "Successfully verified expense amount", "Failed to veriyfy expense amount", driver);
		return this;
	}

	/**
	 * Verify success message is displayed
	 * @author sathish.suresh
	 * @param driver
	 * @return TimePage
	 */
	public ClaimPage verifyToastMessage(WebDriver driver) {
		BrowserActions.waitForElementToDisplay(driver, toastMsgSuccess);
		return this;
	}

	/**
	 * Validate timesheet details
	 * @author sathish.suresh
	 * @param driver
	 * @param claimDetails
	 * @return TimePage
	 * @throws InterruptedException 
	 */
	public ClaimPage validateClaimDetails(WebDriver driver, ArrayList<String> claimDetails) {
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
			Collections.sort(claimDetails);
			boolean equal = actList.containsAll(claimDetails);
			Log.assertThat(equal, "Claim Details is verified successfully", "Failed to verify Claim details", driver);
		}
		return this;
	}


	/**
	 * Write json file to add timesheet information
	 * @author sathish.suresh
	 * @param claimInfo
	 */
	@SuppressWarnings("unchecked")
	public void writeJsonFile(ArrayList<String> claimInfo) {
		try {
			File file=new File("JsonTestDate\\ClaimDetails.json");  
			file.createNewFile(); 
			FileWriter fileWriter = new FileWriter(file); 
			System.out.println("Writing JSON object to file");  
			System.out.println("-----------------------"); 
			JSONObject jsonClaimInfo = new JSONObject();  
			for (int i = 0; i < claimInfo.size(); i++) {

				switch (i) {
				case 0:
					jsonClaimInfo.put("Reference Id", claimInfo.get(i));
				case 1:
					jsonClaimInfo.put("Employee Name", claimInfo.get(i));
				case 2:
					jsonClaimInfo.put("Event Name", claimInfo.get(i));
				case 3:
					jsonClaimInfo.put("Currency", claimInfo.get(i));
				case 4:
					jsonClaimInfo.put("Submitted Date", claimInfo.get(i));
				case 5:
					jsonClaimInfo.put("Status", claimInfo.get(i));
				case 6:
					jsonClaimInfo.put("Amount", claimInfo.get(i));
				default:
					break;
				}
			}

			fileWriter.write(jsonClaimInfo.toJSONString());
			fileWriter.flush();  
			fileWriter.close(); 
		}
		catch (Exception e) {
			e.printStackTrace();  
		}
	}

	public ClaimPage createClaimRequest(WebDriver driver, EnterEmpFullName enterEmpFullName,  CreateClaimDetails createClaimDetails) {

		enterEmployeeName(driver, EnterEmpFullName.builder()
				.firstName(enterEmpFullName.getFirstName())
				.lastName(enterEmpFullName.getLastName())
				.build());

		selectEvent(driver, CreateClaimRequest.EVENT, CreateClaimDetails.builder()
				.event(createClaimDetails.getEvent())
				.build());

		selectCurrency(driver, CreateClaimRequest.CURRENCY, CreateClaimDetails.builder()
				.currency(createClaimDetails.getCurrency())
				.build());

		return this;

	}

	public void addNewExpense(WebDriver driver, ExpenseDetails expenseDetails) {

		selectExpenseType(driver, AddExpense.EXPENSE_TYPE, ExpenseDetails.builder()
				.expenseType(expenseDetails.getExpenseType())
				.build());

		enterDate(driver, AddExpense.DATE, ExpenseDetails.builder()
				.currentDate(expenseDetails.getCurrentDate())
				.build());

		enterAmount(driver, AddExpense.AMOUNT, ExpenseDetails.builder()
				.amount(expenseDetails.getAmount())
				.build());
	}


}

