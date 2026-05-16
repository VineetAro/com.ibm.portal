package utils;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import driver.DriverFactory;

public class ActionUtils {

	private ActionUtils() {

	}

	public static void click(By locator, String elementName) {

		WebElement element = WaitUtil.waitForClickable(locator);

		try {
			LoggerUtil.debug("Attempting to click on: " + elementName);
			JSExecutor.scrollToElement(element);
			element.click();
			LoggerUtil.info("Successfully clicked on: " + elementName);
		} catch (Exception e) {
			LoggerUtil.actionFailure("Click", e);
			throw e; // Re-throw so the test still fails
		}
	}

	public static void clickAndEnterValue(By locator, String value, String elementName) {
		WebElement element = WaitUtil.waitForPresence(locator);
		try {

			LoggerUtil.debug("Attempting to click on: " + elementName);
			JSExecutor.scrollToElement(element);
			element.click();
			LoggerUtil.info("Successfully clicked on: " + elementName);
			element.clear();
			element.sendKeys(value);
			LoggerUtil.info("Successfully cleared field and enter " + value + " on: " + elementName);
		} catch (Exception e) {
			LoggerUtil.actionFailure("Click", e);
			throw e; // Re-throw so the test still fails
		}
	}

	public static void selectValueFromList(By locator, String value, String elementName) {

		List<WebElement> options = WaitUtil.waitForVisibleElements(locator);
		int itemCount = DriverFactory.getDriver().findElements(locator).size();

		for (int i = 0; i < itemCount; i++) {
			List<WebElement> freshList = DriverFactory.getDriver().findElements(locator);
			WebElement option = freshList.get(i);

			if (option.getText().equals(value)) {
				System.out.println("MATCH FOUND FOR PARK");

				JSExecutor.scrollToElement(option);

				option.click();
				LoggerUtil.debug(" === Selected value from list === " + option);
				break;

			}

		}
	}

	public static void clickValueInList(By locator, String value, String elementName) {

		Actions action = new Actions(DriverFactory.getDriver());

		List<WebElement> options = WaitUtil.waitForVisibleElements(locator);
		int itemCount = DriverFactory.getDriver().findElements(locator).size();

		for (int i = 0; i < itemCount; i++) {
			List<WebElement> freshList = DriverFactory.getDriver().findElements(locator);
			WebElement option = freshList.get(i);

			if (option.getText().equals(value)) {

				JSExecutor.scrollToElement(option);

				action.moveToElement(option).perform();
				option.click();

				LoggerUtil.debug(" === Selected value from list === " + option);
				break;

			}

		}
	}

	public static String getLabelText(By locator) {
		return WaitUtil.waitForVisibilityOf(locator).getText();
	}

	public static void clickSelectDropDownList(By locatorField, By locatorOption, String value) {

		WebElement element = WaitUtil.waitForClickable(locatorField);

		element.click();

		LoggerUtil.debug(" === Clicked on dropdown === " + locatorField);

		List<WebElement> options = DriverFactory.getDriver().findElements(locatorOption);

		for (WebElement option : options) {

			if (option.getText().equals(value)) {

				JSExecutor.scrollToElement(option);
				option.click();
				LoggerUtil.debug(" === Selected Dropdown option === " + option);
				break;

			}
		}

	}

}
