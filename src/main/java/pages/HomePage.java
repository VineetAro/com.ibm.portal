package pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import commons.BasePage;
import driver.DriverFactory;
import utils.JSExecutor;
import utils.LoggerUtil;
import utils.WaitUtil;

/**
 * HomePage class handles the main landing dashboard of the NYC311 portal. It
 * manages both Guest (Sign In) and Authenticated (User Dropdown) states.
 */
public class HomePage extends BasePage {


	// --- Locators ---
	private static final By SIGN_IN_BTN = By.linkText("Sign In");
	private static final By LOGGED_IN_USER = By.cssSelector(".username");
	private static final By USER_DROPDOWN = By.cssSelector("#fullScreenUserDropdown");
	private static final By DROPDOWN_OPTIONS = By.cssSelector(".dropdown-menu a");
	private static final By SEARCH_FIELD_INPUT = By.name("q");
	private static final By REPORT_PROBLEM_BTN = By.cssSelector("#homePageSideNav a[title*=\"Report Problems\"]");

	public HomePage() {
		super(); // Triggers automatic page synchronization
	}

	/**
	 * Navigates to the Login Page by clicking the Sign In link.
	 */
	public LoginPage clickSignIn() {
		LoggerUtil.testStart("Clicking Sign In button");
		WebElement element = WaitUtil.waitForVisibilityOf(SIGN_IN_BTN);
		JSExecutor.scrollToElement(element);
		element.click();
		return new LoginPage();
	}

	/**
	 * Opens the User Profile dropdown menu using Actions.
	 */
	public void clickUserProfile() {
		LoggerUtil.testStart("Opening User Dropdown menu");
		WebElement element = WaitUtil.waitForClickable(USER_DROPDOWN);
		Actions actions = new Actions(DriverFactory.getDriver());
		actions.moveToElement(element).click().build().perform();
		LoggerUtil.actionSuccess("User Dropdown opened");
	}

	/**
	 * Helper to find a specific option in the user dropdown. Uses trim() to handle
	 * potential HTML whitespace.
	 */
	private WebElement getOption(String optionName) {
		// We wait for the dropdown options list to ensure the menu actually opened
		List<WebElement> options = WaitUtil.waitForPresenceElements(DROPDOWN_OPTIONS);

		for (WebElement element : options) {
			if (optionName.equalsIgnoreCase(element.getText().trim())) {
				return element;
			}
		}
		LoggerUtil.testStart("Dropdown option not found: " + optionName);
		throw new RuntimeException("Could not find dropdown option: " + optionName);
	}

	/**
	 * Navigates to the Profile Page via the user dropdown.
	 */
	public ProfilePage navigateToProfilePage() {
		LoggerUtil.testStart("Navigating to Profile Page...");
		clickUserProfile(); // Ensure menu is open
		WebElement profileOption = getOption("My Profile");
		profileOption.click();

		LoggerUtil.actionSuccess("Navigated to Profile Page");
		return new ProfilePage();
	}

	/**
	 * Retrieves the username of the logged-in user.
	 */
	public String getLoggedInUsername() {
		try {
			WebElement user = WaitUtil.waitForVisibilityOf(LOGGED_IN_USER);
			String name = user.getText().trim();
			LoggerUtil.testStart("Logged in user detected: " + name);
			return name;
		} catch (Exception e) {
			return "";
		}
	}
	
	public By getLoggedInLocator() {
		return this.LOGGED_IN_USER;
	}
	/**
	 * Anchor Method: Validates the state of the Home Page. The page is 'loaded' if
	 * either the Sign In button is present (Guest) or the Logged In username is
	 * present (Member).
	 */
	@Override
	public boolean isPageLoaded() {
		LoggerUtil.testStart("Validating HomePage load status...");
		try {
			// First check if user is logged in (highest priority for SDET tests)
			return WaitUtil.waitForVisibilityOf(LOGGED_IN_USER).isDisplayed();
		} catch (Exception e) {
			// Fallback: If not logged in, check for the Sign In button
			try {
				return WaitUtil.waitForVisibilityOf(SIGN_IN_BTN).isDisplayed();
			} catch (Exception e2) {
				LoggerUtil.testStart("HomePage failed to load: Neither Sign In nor Username found.");
				return false;
			}
		}
	}
	
	public ReportProblem navigateToReportProblems() {
		WaitUtil.waitForClickable(REPORT_PROBLEM_BTN).click();
		return new ReportProblem();
		
	}
}