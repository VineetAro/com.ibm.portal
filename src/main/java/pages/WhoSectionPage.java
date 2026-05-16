package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import ch.qos.logback.core.joran.action.ActionUtil;
import commons.BasePage;
import driver.DriverFactory;
import utils.ActionUtils;
import utils.JSExecutor;
import utils.LoggerUtil;
import utils.WaitUtil;

public class WhoSectionPage extends BasePage {
	//LOCATORS
	
	private static final By FIRST_NAME_FIELD = By.id("n311_contactfirstname");
	private static final By LAST_NAME_FIELD = By.id("n311_contactlastname");
	private static final By EMAIL_FIELD = By.id("n311_contactemail");
	private static final By PHONE_FIELD = By.id("n311_contactphone");
	private static final By NEXT_BTN = By.id("NextStepBtn");
	
	private static final By CURRENT_STEP  = By.cssSelector("#pb-bar .active .pb-label");
	
	private static final By ERRORS_LIST = By.cssSelector(".alert-danger li span");
	
	private static final By PROGRESS_BAR  = By.cssSelector("#pb-bar .active+.step .pb-label");
	
	
	public WhoSectionPage() {
		super();
		
		System.out.println("HURRAY WHO SECTION");
	}

	@Override
	public boolean isPageLoaded() {
		
	
		return ActionUtils.getLabelText(CURRENT_STEP).equalsIgnoreCase("who");
	}
	
	public void enterFirstName(String fName) {
		if(!fName.equals("-1")) {
		ActionUtils.clickAndEnterValue(FIRST_NAME_FIELD, fName, "First Name");
	}
		else {
			return;
		}
	}
	public void enterLastName(String lName) {
		if(!lName.equals("-1")) {
		ActionUtils.clickAndEnterValue(LAST_NAME_FIELD, lName, "Last Name");
	}else {
		return;
	}
		}
	
	
	public void enterEmail(String email) {
		if(!email.equals("-1")) {
		ActionUtils.clickAndEnterValue(EMAIL_FIELD, email, "Email Name");
	}
		else {
			return;
		}
	}
	
	public void enterPhone(String phone) {
		if(!phone.equals("-1")) {
		ActionUtils.clickAndEnterValue(PHONE_FIELD, phone, "Phone");
	}else {
		return;
	}
	}
	
	public <T extends BasePage> T clickNextBtn() {
		String section = getNextStep().trim().toLowerCase();
		ActionUtils.click(NEXT_BTN, "Next Button");
		WaitUtil.waitForStaleElement(DriverFactory.getDriver().findElement(NEXT_BTN));
		LoggerUtil.actionSuccess("++++ WHO Section StaleElement ++++ ");
		return (T) detectAndAssignSection(section);
	}

	private String getNextStep() {
		JSExecutor.scrollToElement(DriverFactory.getDriver().findElement(PROGRESS_BAR));
		
		return ActionUtils.getLabelText(PROGRESS_BAR);
		
		
	}
	private Object detectAndAssignSection(String section) {
		return switch (section) {
	    
	    case "where" -> new WhereSectionPage();
	    case "who" -> new WhoSectionPage();
	    case "what" -> new WhatSectionPage();
	    case "review" -> new ReviewSectionPage();
	    default -> throw new RuntimeException("Unexpected section: " + getNextStep());
	   
	};
	
	}

	
	
}
