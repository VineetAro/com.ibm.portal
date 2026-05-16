package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import commons.BasePage;
import driver.DriverFactory;
import utils.JSExecutor;
import utils.LoggerUtil;
import utils.WaitUtil;

public class LoginPage extends BasePage {

	// locators

	private static final By USERNAME_FIELD = By.name("username");
	private static final By PASSWORD_FIELD = By.name("password");
	private static final By LOGIN_BTN = By.cssSelector("input[type='submit'][value='Login']");

	public LoginPage() {
		super();
	}

	public void enterUsername(String username) {
		LoggerUtil.debug("Enter username: " + username);
		WebElement field = WaitUtil.waitForVisibilityOf(USERNAME_FIELD);
		field.sendKeys(username);
		LoggerUtil.info("Username entered successfully");
	}

	public void enterPassword(String password) {
		LoggerUtil.debug("Entering password");
		WebElement field = WaitUtil.waitForVisibilityOf(PASSWORD_FIELD);
		field.sendKeys(password);
		LoggerUtil.info("Password entered successfully");
	}

	public HomePage clickLoginButton() {
		LoggerUtil.debug("Clicking login button");
		try {
			WebElement button = WaitUtil.waitForClickable(LOGIN_BTN);
			JSExecutor.scrollToElement( button);
			button.click();
			LoggerUtil.info("Login button clicked successfully");
			return new HomePage();
		} catch (Exception e) {
			LoggerUtil.actionFailure("failed to click login button", e);
			throw e;
		}

	}

	public HomePage loginAs(String username, String password) {
		enterUsername(username);
		enterPassword(password);
		// clickLoginButton already returns a validated HomePage!
		return clickLoginButton();
	}

	public boolean isUsernameFieldVisible() {
		// TODO: Check if username field is visible
		return DriverFactory.getDriver().findElement(USERNAME_FIELD).isDisplayed();
	}

	public By getLoginButton() {
		return LOGIN_BTN;
	}

	@Override
	public boolean isPageLoaded() {
		// Check if login button is visible
		return WaitUtil.waitForVisibilityOf(LOGIN_BTN).isDisplayed();

	}

}
