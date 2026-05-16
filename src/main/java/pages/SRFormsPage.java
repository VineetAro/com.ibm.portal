package pages;

import org.openqa.selenium.By;

import commons.BasePage;
import utils.WaitUtil;

public class SRFormsPage extends BasePage {

	private Object activeSection;
	// Common Locators
	private static final By PROGRESS_BAR_SECTION_1 = By.cssSelector("#pb-step-1 .pb-bar + a");

	public SRFormsPage() {
		super();
		this.activeSection = detectAndAssignSection();
	}

	@Override
	public boolean isPageLoaded() {

		return WaitUtil.waitForVisibilityOf(PROGRESS_BAR_SECTION_1).isDisplayed();

	}

	private Object detectAndAssignSection() {
		if (WaitUtil.Wait(PROGRESS_BAR_SECTION_1, 40, 500).getText().equalsIgnoreCase("what")){
			return new WhatSectionPage();
		} else {
			return new WhereSectionPage();
		}
	}

	// Helper to get the active section without the test knowing which one it is
	public <T extends BasePage> T getActiveSection() {
		return (T) activeSection;
	}

}