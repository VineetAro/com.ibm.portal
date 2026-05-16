package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;


import commons.BasePage;
import driver.DriverFactory;
import utils.ActionUtils;
import utils.JSExecutor;
import utils.LoggerUtil;
import utils.WaitUtil;

public class WhereSectionPage extends BasePage {
	
	
	//Locators
	
	private static final By LOCATION_TYPE_LABEL = By.xpath("//label[@for='n311_locationtypeid']");
	
	
	private static final By PROGRESS_BAR_SECTION= By.cssSelector("#pb-bar .active a:nth-of-type(2)");
	
	private static final By LOCATION_TYPE_DD_BTN = By.cssSelector("#n311_locationtypeid .multiselect__select");

	private static final By LOCATION_TYPE_DD_LST = By.cssSelector("#listbox-n311_locationtypeid .multiselect__option span");
	
	private static final By ADDRESS_BTN	= By.id("SelectAddressWhere");
	private static final By ADDRESS_SEARCH_FIELD = By.id("address-search-box-input");
	
	private static final By ADDRESS_SEARCH_RESULT_LST = By.cssSelector("li.vue-address-search-results-item");
	private static final By ADDRESS_SELECTION_CANCEL_BTN	= By.cssSelector("input[type='button'][value='Cancel']");
	private static final By ADDRESS_SELECTION_CONFIRM_BTN	= By.cssSelector("input[type='button'][value='Select Address']");
	
	private static final By SELECT_MAP_BTN = By.cssSelector("button.address-picker-btn");
	
	private static final By ENTER_PARK_NAME_FIELD = By.id("searchByAsset-input");
	
	private static final By PARK_ASSET_LIST = By.cssSelector(".asset-list-li");
	
	private static final By CHOOSE_PARK_BTN = By.cssSelector(".esri-popup__button[aria-label='Choose Park']");
	
	private static final By NEXT_BTN = By.id("NextStepBtn");
	
	
	private static final By PROGRESS_BAR  = By.cssSelector("#pb-bar .active + .step .pb-label");
	
	
	
	

	
	public WhereSectionPage() {
		super();

		System.out.println("HURRAY WHERE SECTION");
	}
	@Override
	public boolean isPageLoaded() {
		
		return WaitUtil.waitForVisibilityOf(PROGRESS_BAR_SECTION).isDisplayed();
	}
	public boolean isLocationTypeRequired() {
		return DriverFactory.getDriver().findElement(LOCATION_TYPE_LABEL).getAttribute("class")
				.equalsIgnoreCase("vrequired");
	}

	
	public void selectLocationType(String locationType) {
		WebElement lTypeLabel = WaitUtil.waitForVisibilityOf(LOCATION_TYPE_LABEL);
		List<WebElement> defaultValue = lTypeLabel.findElements(By.xpath("/following-sibling::span"));
		if(!locationType.equals("-1")&&defaultValue.isEmpty()) {
			ActionUtils.clickSelectDropDownList(LOCATION_TYPE_DD_BTN, LOCATION_TYPE_DD_LST, locationType);
		}
		else {
			return;
		}
	}
	
	
	public void enterStreetAddress(String streetAddress) {
		if(!streetAddress.equals("-1")) {
			//Click Address selection field
			ActionUtils.click(ADDRESS_BTN, "Address field");
			
			ActionUtils.clickAndEnterValue(ADDRESS_SEARCH_FIELD, streetAddress, "Enter Address Field");
			
			ActionUtils.clickValueInList(ADDRESS_SEARCH_RESULT_LST, streetAddress.toUpperCase(), "Address Suggestions");
			WaitUtil.waitForVisibilityOf(ADDRESS_SELECTION_CONFIRM_BTN);
			ActionUtils.click(ADDRESS_SELECTION_CONFIRM_BTN, "Select button");
			
			
		}else {
			return;
		}
	}
	
	
	public void clickSelectParkBtn(String locationType) {
		if(locationType.toLowerCase().contains("park")) {
		WaitUtil.waitForInvisibilityOf(SELECT_MAP_BTN, "Loading...");
		ActionUtils.click(SELECT_MAP_BTN, "Choose Park");
	}else {
		return;
	}
		}
	
	public void enterParkName(String parkName) {
		if(!parkName.equals("-1")) {
		ActionUtils.clickAndEnterValue(ENTER_PARK_NAME_FIELD, parkName, "Park Name Field");
		}
		else {
			return;
		}
	}
	
	public void selectParkFromList(String targetParkName) {
	    if(!targetParkName.equals("-1")) {
		Actions action = new Actions(DriverFactory.getDriver());
	    
	    int initialCount = DriverFactory.getDriver().findElements(PARK_ASSET_LIST).size();
	    LoggerUtil.debug("Initial item count before filtering: " + initialCount);
	    WaitUtil.waitForFilters(PARK_ASSET_LIST, initialCount);

	    // Wait for the asset list rows to become visible after typing
	    WaitUtil.waitForVisibilityOf(PARK_ASSET_LIST);
	    int itemCount = DriverFactory.getDriver().findElements(PARK_ASSET_LIST).size();
	  
	    
	    // Clean up the target text: lowercase and remove all whitespace formatting
	    String cleanTarget = targetParkName.toLowerCase().replaceAll("\\s+", "");
	  
	    for (int i = 0; i < itemCount; i++) {
	        try {
	            // Re-query elements fresh inside the loop to avoid StaleElementReferenceException
	            List<WebElement> freshList = DriverFactory.getDriver().findElements(PARK_ASSET_LIST);
	            WebElement row = freshList.get(i);
	            
	            // Extract text from the entire row wrapper node (both b and span together)
	            String rawRowText = row.getText().toLowerCase();
	  
	            String cleanRowText = rawRowText.replaceAll("\\s+", "");
	  
	            
	            // Compare the fully sanitized strings
	            if (cleanRowText.contains(cleanTarget)) {
	                JSExecutor.scrollToElement(row);
	                
	                // Native Action engine click sent directly to the layout container node
	                action.moveToElement(row)
	                      .click()
	                      .build()
	                      .perform();
	                
	                LoggerUtil.info("✔️ Selected value from list matching: " + row.getText().trim());
	                return; // Success exit
	            }
	        } catch (StaleElementReferenceException e) {
	            // Fallback if DOM tree refreshes instantly mid-evaluation
	            i--;
	            itemCount = DriverFactory.getDriver().findElements(PARK_ASSET_LIST).size();
	        }
	    }
	    throw new org.openqa.selenium.NoSuchElementException("Could not locate park matching text: " + targetParkName);
	}else {
		return;
	}
	}
	public void clickChooseParkBtn(String parkName) {
		if(!parkName.equals("-1")) {
		ActionUtils.click(CHOOSE_PARK_BTN, "Choose Park");
	}
	}
	public <T extends BasePage> T clickNextBtn() {
		String section = getNextStep().trim().toLowerCase();
		LoggerUtil.info(section);
		ActionUtils.click(NEXT_BTN, "Next Button");
		WaitUtil.waitForStaleElement(DriverFactory.getDriver().findElement(NEXT_BTN));
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
	    default -> throw new RuntimeException("Unexpected section: " + getNextStep());
	};
	}

	
}

