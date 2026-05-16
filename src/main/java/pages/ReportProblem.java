package pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import commons.BasePage;
import utils.LoggerUtil;
import utils.WaitUtil;

public class ReportProblem extends BasePage {

	// private static final Logger logger =
	// LogManager.getLogger(ReportProblem.class);

	// locators

	private static final By ENTRY_TITLE = By.cssSelector(".entry-title");
	private static final By SEARCH_FIELD = By.id("q");
	private static final By SEARCH_BTN = By.cssSelector("[aria-label='Search']");

	public ReportProblem() {
		super();

	}

	@Override
	public boolean isPageLoaded() {
		String title = WaitUtil.waitForVisibilityOf(ENTRY_TITLE).getText();
		return title.equals("Report Problems");
	}

	public void enterSearchText(String searchText) {
		
		WebElement element =  WaitUtil.waitForVisibilityOf(SEARCH_FIELD);
		element.clear();
		element.sendKeys(searchText);
		LoggerUtil.actionSuccess("Entered: " + searchText);
	}
	
	
	public SearchPage clickSearchButton() {
		WebElement element = WaitUtil.waitForClickable(SEARCH_BTN);
		element.click();
		LoggerUtil.actionSuccess("Clicked Search");
		return new SearchPage();
	}
	
	public SearchPage enterProblemAndClick(String searchText) {
		enterSearchText(searchText);
		clickSearchButton();
		return new SearchPage();
	}
	
}
