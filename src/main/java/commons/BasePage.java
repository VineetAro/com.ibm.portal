package commons;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import driver.DriverFactory;

public abstract class BasePage {

	private static final Logger logger = LogManager.getLogger(BasePage.class);

	// Constructor - all page objects call this
	public BasePage() {

		if (DriverFactory.getDriver() == null) {
			throw new RuntimeException(
					"Driver is null! Ensure DriverManager has initialized the driver before creating Page Objects.");
		}

		// Every page object created
		// will now automatically wait for its anchor element.
		waitForPageToLoad();

	}

	// New helper method
	private void waitForPageToLoad() {
		if (!isPageLoaded()) {
			throw new RuntimeException(
					"Page " + this.getClass().getSimpleName() + " failed to load its anchor element!");
		}
	}

	public abstract boolean isPageLoaded();

	public String getPageTitle() {
		return DriverFactory.getDriver().getTitle();
	}

	public String getPageUrl() {
		return DriverFactory.getDriver().getCurrentUrl();
	}
	
		
	public WebElement searchInTable(By resultsLocator, By nextButtonLocator, String value) {
	    WebElement targetElement = null;
	    boolean isFound = false;

	    while (!isFound) {
	        // 1. Get all elements on the current page matching your locator
	        List<WebElement> results = DriverFactory.getDriver().findElements(resultsLocator);

	        // 2. Check each element for the target value
	        for (WebElement element : results) {
	            if (element.getText().equalsIgnoreCase(value)) {
	                targetElement = element;
	                isFound = true;
	                break;
	            }
	        }

	        // 3. If not found, try to navigate to the next page
	        if (!isFound) {
	            try {
	                WebElement nextButton = DriverFactory.getDriver().findElement(nextButtonLocator);
	                if (nextButton.isDisplayed() && nextButton.isEnabled()) {
	                    nextButton.click();
	                    // 4. Wait for the page/table to refresh to avoid stale elements
	                    new WebDriverWait(DriverFactory.getDriver(), Duration.ofSeconds(10))
	                        .until(ExpectedConditions.stalenessOf(results.get(0)));
	                } else {
	                    break; // No more pages left
	                }
	            } catch (NoSuchElementException | IndexOutOfBoundsException e) {
	                System.out.println("Value not found after checking all pages.");
	                break;
	            }
	        }
	    }
	    return targetElement;
	}

	
	
	
	
	
}
