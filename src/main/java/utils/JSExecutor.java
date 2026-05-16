package utils;

import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import driver.DriverFactory;

public class JSExecutor {

	// Scroll to Element

	public static void scrollToElement( WebElement element) {
		WebDriver driver = DriverFactory.getDriver();
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
	}
	
	public static void scrollToElements(List<WebElement> elements) {
		WebDriver driver = DriverFactory.getDriver();
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", elements);
	}
	
	

	// Highlight element
	public static void highlightElement(WebElement element) {
		WebDriver driver = DriverFactory.getDriver();
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;

			// Apply a thick red border and yellow background
			js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 4px solid red;');",
					element);
			Thread.sleep(500);
		} catch (Exception e) {
			System.err.println("Could not highlight element: " + e.getMessage());
		}
	}

}
