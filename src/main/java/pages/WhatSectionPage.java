package pages;

import java.sql.Driver;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;

import commons.BasePage;
import driver.DriverFactory;
import utils.ActionUtils;
import utils.JSExecutor;
import utils.WaitUtil;

public class WhatSectionPage extends BasePage {
	
	private Object activeSection;

	
	private static final By ENTRY_TITLE = By.cssSelector(".entry-title");

	// Locators
	// Anchor
	private static final By PROGRESS_BAR_SECTION_1 = By.cssSelector("#pb-step-1 .pb-bar + a");

	// WHAT SECTION
	private static final By PROBLEM_LABEL = By.xpath("//label[@for='n311_problemid']");
	private final By PROBLEM_DETAIL_LABEL = By.xpath("//label[@for='n311_problemdetailid']");
	private final By DATE_TIME_LABEL = By.xpath("//label[@for='n311_datetimeobserved']");
	private final By DESCRIPTION_LABEL = By.xpath("//label[@for='n311_description']");
	private final By RECURRING_PROBLEM_LABEL = By.xpath("//label[@for='n311_isthisarecurringproblem']");

	private static final By PROBLEM_DETAIL_DD_BTN = By.cssSelector("#n311_problemdetailid .multiselect__select");
	private static final By PROBLEM_DETAIL_DD_LIST = By.cssSelector("#listbox-n311_problemdetailid li");
	
	private static final By DATE_TIME_OBSERVED = By.cssSelector("#n311_datetimeobserved_datepicker input");
	private static final By DATE_LIST = By.
			cssSelector(".dp__calendar_item .dp__cell_inner:not(.dp__cell_offset):not(.dp__cell_disabled)");

	private static final By DATE_PICKER_SELECT_BTN = By.cssSelector(".dp__action_buttons > span:nth-child(2)");
	
	private static final By DESC_FREE_TEXT_FIELD = By.id("n311_description");
	
	private static final By RECURRING_RADIO_BUTTON_OPTIONS = By.cssSelector(".op .flex-child span");
	
	private static final By PROGRESS_BAR  = By.cssSelector("#pb-bar .active + .step .pb-label");
	
	private static final By NEXT_BTN = By.id("NextStepBtn");
	
	private static final By DESCRIBE_RECURRING_FIELD = By.id("n311_describethedaysandtimestheproblemhappens");
	

	public WhatSectionPage() {
		super();
		
	}
	

	@Override
	public boolean isPageLoaded() {

		return WaitUtil.waitForVisibilityOf(PROGRESS_BAR_SECTION_1).isDisplayed();

	}

	public boolean isProblemRequired() {
	
		return DriverFactory.getDriver().findElement(PROBLEM_LABEL).getAttribute("class").equalsIgnoreCase("vrequired");
	}

	public boolean isProblemDetailsRequired() {
		return DriverFactory.getDriver().findElement(PROBLEM_DETAIL_LABEL).getAttribute("class")
				.equalsIgnoreCase("vrequired");
	}

	public boolean isDateTimeRequired() {
		return DriverFactory.getDriver().findElement(DATE_TIME_LABEL).getAttribute("class")
				.equalsIgnoreCase("vrequired");
	}

	public boolean isDescriptionRequired() {
		return DriverFactory.getDriver().findElement(DESCRIPTION_LABEL).getAttribute("class")
				.equalsIgnoreCase("vrequired");
	}

	public boolean isRecurringRequired() {
		return DriverFactory.getDriver().findElement(RECURRING_PROBLEM_LABEL).getAttribute("class")
				.equalsIgnoreCase("vrequired");
	}

	public void selectProblemDetail(String problemDetail) {
		System.out.println("SelectProblemStail START");
		WebElement pdLabel = WaitUtil.waitForVisibilityOf(PROBLEM_DETAIL_LABEL);
		List<WebElement> defaultValue = pdLabel.findElements(By.xpath("./following-sibling::span"));
		
		System.out.println("Default Value" + defaultValue.size());
		
		if(!problemDetail.equals("-1") && defaultValue.size()<1) {
			ActionUtils.clickSelectDropDownList(PROBLEM_DETAIL_DD_BTN, PROBLEM_DETAIL_DD_LIST, problemDetail);
		}
		else {
			return;
		}
		
//		dropdownButton.click()
//
//		List<WebElement> options = DriverFactory.getDriver().findElements(PROBLEM_DETAIL_DD_LIST);
//
//		for (WebElement option : options) {
//
//			if (option.getText().equals(problemDetail)) {
//				
//				JSExecutor.scrollToElement(option);
//				option.click();
//
//				break;
//			}
//			}
		}
	
	

	public void selectDateTimeObserved() {
	    // 1. Get the day number for yesterday (e.g., "10")
	    String targetDay = String.valueOf(LocalDate.now().minusDays(1).getDayOfMonth()); 
	    
	    Actions action = new Actions(DriverFactory.getDriver()); 
	    
	    // 2. Open the picker
	    WebElement dateInput = DriverFactory.getDriver().findElement(DATE_TIME_OBSERVED);
	    action.moveToElement(dateInput).click().perform();

	    // 3. Find only VALID days (ignoring offsets and future dates)
	    List<WebElement> days = DriverFactory.getDriver().findElements(DATE_LIST);

	    // 4. Match the text exactly
	    Optional<WebElement> match = days.stream()
	        .filter(el -> el.getText().trim().equals(targetDay))
	        .findFirst();

	    if (match.isPresent()) {
	        WebElement dayToClick = match.get();
	        // Use Actions to ensure we bypass any tiny SVG icons inside the cell
	        action.moveToElement(dayToClick).click().perform();
	        System.out.println("✔️ Successfully clicked day: " + targetDay);
	    } else {
	        System.out.println("❌ Could not find an active calendar cell with text: " + targetDay);
	        // Fallback: If stream fails, list all found text for debugging
	        days.forEach(d -> System.out.println("Found visible day: " + d.getText()));
	    }

	    // 5. Click Select
	    WebElement select_btn = WaitUtil.waitForClickable(DATE_PICKER_SELECT_BTN);
	    action.moveToElement(select_btn).click().perform();
	}
	
	
	public void selectRecurringProblem(String isRecurring) {
	    // 1. Find the span that contains the text (e.g., "Yes" or "No/Unknown")
		// if isRecurring == '-1' then skip else perform the operation.
		
		if(!isRecurring.equals("-1")) {
	    Optional<WebElement> textSpan = DriverFactory.getDriver()
	            .findElements(RECURRING_RADIO_BUTTON_OPTIONS)
	            .stream()
	            .filter(el -> el.getText().trim().equalsIgnoreCase(isRecurring))
	            .findFirst();

	    textSpan.ifPresent(span -> {
	        // Find the specific radio input associated with this text
	        WebElement radioInput = span.findElement(By.xpath("./preceding-sibling::input[@type='radio']"));
	        
	        // Use Actions to click to handle any 'hidden' input styling
	        new Actions(DriverFactory.getDriver())
	            .moveToElement(radioInput)
	            .click()
	            .perform();
	            
	        System.out.println("✔️ Selected recurring option: " + isRecurring);
	    });
	    }
	    else {
	    	return;
		}
	}
	
	public void enterDescription(String desc_text) {
		if(!desc_text.equals("-1")) {
		WebElement descField = DriverFactory.getDriver().findElement(DESC_FREE_TEXT_FIELD);
		descField.clear();
		descField.click();
		descField.sendKeys(desc_text);	
		}
	
	else 
	{
		return;
	}
}
	
	public void enterDescribeHowManyTime(String recc_text) {
		if(!recc_text.equals("-1")) {
		WebElement descField = DriverFactory.getDriver().findElement(DESCRIBE_RECURRING_FIELD);
		descField.clear();
		descField.click();
		descField.sendKeys(recc_text);	
		}else {
			return;
		}
	}
	
	public <T extends BasePage> T clickNext() {

		String section = getNextStep().trim().toLowerCase();
		
		WaitUtil.waitForClickable(NEXT_BTN).click();
		
		
		return (T) detectAndAssignSection(section);
		
	}
	/*
	private String getNextStep() {
	    // 1. Specifically look for the 'active' step
	    
	    
	    try {
	        // Wait a brief moment for the DOM to update after the click
	        WebElement activeLabel = WaitUtil.waitForVisibilityOf(PROGRESS_BAR);
	        String sectionName = activeLabel.getText().trim();
	        System.out.println("DEBUG: Detected Active Section: " + sectionName);
	        //if(sectionName.equalsIgnoreCase("what")) {
	        	//return "who";
	        //}
	        return sectionName;
	    } catch (Exception e) {
	        System.out.println("ERROR: Could not detect active progress bar step.");
	        return "Unknown Section";
	    }
	}
	*/
	
	private String getNextStep() {
		return DriverFactory.getDriver().findElement(PROGRESS_BAR).getText();
		
	}
	private Object detectAndAssignSection(String section) {
		return switch (section) {
	    
	    case "where" -> new WhereSectionPage();
	    case "who" -> new WhoSectionPage();
	    default -> throw new RuntimeException("Unexpected section: " + getNextStep());
	};
	}

	// Helper to get the active section without the test knowing which one it is
	//not needed here/
	public <T extends BasePage> T getActiveSection() {
		return (T) activeSection;
	}
	
}
