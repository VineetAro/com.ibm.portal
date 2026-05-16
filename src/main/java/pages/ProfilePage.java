package pages;

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
 * ProfilePage class represents the User Profile section of the NYC311 Portal.
 * It extends BasePage to leverage automatic synchronization and centralized utilities.
 */
public class ProfilePage extends BasePage {

    // --- Locators ---
    private static final By SIGNOUT_BTN = By.id("SignOutButton");
    private static final By FIRSTNAME_FIELD = By.id("firstname");
    private static final By LASTNAME_FIELD = By.id("lastname");
    private static final By PRIMARY_PHONE_NUMBER_FIELD = By.id("telephone1");
    private static final By SAVE_BUTTON = By.cssSelector("input[value='Save Changes']");
    
    // Additional locators for future use
    private static final By LANGUAGE_DROPDOWN = By.id("n311_selectedlanguage");
    private static final By NOTIFY_TEXT_CHKBX = By.id("n311_notifybytext");

    public ProfilePage() {
        super(); // Triggers isPageLoaded() synchronization
    }

    /**
     * Clears and enters the First Name.
     * @param fName String value for the first name field.
     */
    public void enterFirstName(String fName) {
        LoggerUtil.testStart("Entering First Name: " + fName);
        WebElement field = WaitUtil.waitForVisibilityOf(FIRSTNAME_FIELD);
        field.clear();
        field.sendKeys(fName);
        LoggerUtil.actionSuccess("First Name entered");
    }

    /**
     * Clears and enters the Last Name.
     * @param lName String value for the last name field.
     */
    public void enterLastName(String lName) {
        LoggerUtil.testStart("Entering Last Name: " + lName);
        WebElement field = WaitUtil.waitForVisibilityOf(LASTNAME_FIELD);
        field.clear();
        field.sendKeys(lName);
        LoggerUtil.actionSuccess("Last Name entered");
    }

    /**
     * Clears and enters the Primary Phone Number.
     * @param phoneNr String value for the phone number field.
     */
    public void enterPhoneNumber(String phoneNr) {
        LoggerUtil.testStart("Entering Phone Number: " + phoneNr);
        WebElement field = WaitUtil.waitForVisibilityOf(PRIMARY_PHONE_NUMBER_FIELD);
        field.clear();
        field.sendKeys(phoneNr); // Fixed: using parameter instead of hardcoded string
        LoggerUtil.actionSuccess("Phone Number entered");
    }

    /**
     * Scrolls to the Save button, performs an offset click using Actions,
     * and returns the resulting HomePage.
     * @return HomePage object
     */
    public HomePage saveProfile() {
        LoggerUtil.actionSuccess("Attempting to save profile changes...");
        
        // Ensure button is clickable before interaction
        WebElement saveBtn = WaitUtil.waitForClickable(SAVE_BUTTON);
        
        // Scroll to ensure element is in viewport for Actions
        JSExecutor.scrollToElement(saveBtn);
        
        Actions action = new Actions(DriverFactory.getDriver());
        // Using offset click to avoid potential overlay interference
        action.moveToElement(saveBtn, 0, 10).click().build().perform();
        
        LoggerUtil.actionSuccess("Save button clicked");
        
        // Return new HomePage - constructor will wait for HomePage anchor
        return new HomePage();
    }

    /**
     * Anchor Method: Validates that the Profile Page is fully rendered.
     * We use FIRSTNAME_FIELD instead of SIGNOUT_BTN because it's unique to this form.
     */
    @Override
    public boolean isPageLoaded() {
        try {
            return WaitUtil.waitForVisibilityOf(FIRSTNAME_FIELD).isDisplayed();
        } catch (Exception e) {
            LoggerUtil.actionFailure("ProfilePage failed to load: FIRSTNAME_FIELD not visible.",e);
            return false;
        }
    }
}