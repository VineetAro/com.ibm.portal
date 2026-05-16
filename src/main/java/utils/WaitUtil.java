package utils;

import java.time.Duration;
import java.util.List;

import org.jspecify.annotations.NonNull;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import config.ConfigReader;
import driver.DriverFactory;

/**
 * Author: Arora, Vineet
 * 
 * This class is a helper class which has various explicit Waits and fluent wait
 * defined. It can be extended to include custom waits if needed.
 */
public class WaitUtil {


	// Wait for element to be visible
	public static WebElement waitForVisibilityOf(By locator) {
		WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(),
				Duration.ofSeconds(ConfigReader.getIntProperty("explicit.wait")));
		wait.ignoring(NoSuchElementException.class);
		wait.ignoring(StaleElementReferenceException.class);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	// Wait for element to be invisibility
		public static Boolean waitForInvisibilityOf(By locator, String text) {
			WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(),
					Duration.ofSeconds(ConfigReader.getIntProperty("explicit.wait")));
			wait.ignoring(NoSuchElementException.class);
			wait.ignoring(StaleElementReferenceException.class);
			return wait.until(ExpectedConditions.invisibilityOfElementWithText(locator, text));
		}

	// Wait for element to be clickable
	public static WebElement waitForClickable(By locator) {
		WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(),
				Duration.ofSeconds(ConfigReader.getIntProperty("explicit.wait")));
		wait.ignoring(NoSuchElementException.class);
		wait.ignoring(StaleElementReferenceException.class);
		return wait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	// Wait for element to contain text
	public static boolean waitForElementText(By locator, String text) {
		WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(),
				Duration.ofSeconds(ConfigReader.getIntProperty("explicit.wait")));
		wait.ignoring(NoSuchElementException.class);
		wait.ignoring(StaleElementReferenceException.class);
		return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
	}

	// Wait for element to disappear
	public static boolean waitForElementToDisappear(By locator) {
		WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(),
				Duration.ofSeconds(ConfigReader.getIntProperty("explicit.wait")));
		wait.ignoring(NoSuchElementException.class);
		wait.ignoring(StaleElementReferenceException.class);

		return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	public static List<WebElement> waitForPresenceElements(By locator) {
		WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(),
				Duration.ofSeconds(ConfigReader.getIntProperty("explicit.wait")));
		wait.ignoring(NoSuchElementException.class);
		wait.ignoring(StaleElementReferenceException.class);
		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
	}
	
	public static boolean waitForFilters(By locator, int initialCount) {
	    WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(),
	            Duration.ofSeconds(ConfigReader.getIntProperty("explicit.wait")));
	    
	    wait.ignoring(NoSuchElementException.class);
	    wait.ignoring(StaleElementReferenceException.class);
	    
	    // Dynamically wait until the current list size is no longer equal to the initial massive list size
	    return wait.until(d -> d.findElements(locator).size() != initialCount);
	}
	
	public static List<WebElement> waitForVisibleElements(By locator) {
		WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(),
				Duration.ofSeconds(ConfigReader.getIntProperty("explicit.wait")));
		wait.ignoring(NoSuchElementException.class);
		wait.ignoring(StaleElementReferenceException.class);
		return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}
	
	public static WebElement waitForPresence(By locator) {
		WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(),
				Duration.ofSeconds(ConfigReader.getIntProperty("explicit.wait")));
		wait.ignoring(NoSuchElementException.class);
		wait.ignoring(StaleElementReferenceException.class);

		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	// Fluent wait with polling interval
	public static WebElement Wait(By locator, int timeoutSeconds, int pollingMillis) {
		WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(),
				Duration.ofSeconds(ConfigReader.getIntProperty("explicit.wait")));

		wait.pollingEvery(Duration.ofMillis(pollingMillis));
		wait.ignoring(NoSuchElementException.class);
		wait.ignoring(StaleElementReferenceException.class);
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}
	
	public static boolean waitForStaleElement(WebElement element) {
		WebDriverWait wait = new WebDriverWait(DriverFactory.getDriver(),
				Duration.ofSeconds(ConfigReader.getIntProperty("explicit.wait")));
		wait.ignoring(NoSuchElementException.class);
		wait.ignoring(StaleElementReferenceException.class);
		return wait.until(ExpectedConditions.stalenessOf(element));
		
	}

}
