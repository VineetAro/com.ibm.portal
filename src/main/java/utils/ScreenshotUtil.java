package utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

public class ScreenshotUtil {

	// Capture screenshot with timestamp
	public static String captureScreenshot(WebDriver driver, String testName) {

		// Create a DT format
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

		// Get current Date Time
		LocalDateTime dt = LocalDateTime.now();

		// Convert Date Time to required formate
		String timeStamp = dt.format(format);

		// Create file Name

		String fileName = testName + "_" + timeStamp + ".png";
		String directoryPath = "./reports/screenshots/";
		File destinationFile = new File(directoryPath + fileName);

		try {

			// Ensure the directory exists
			File directory = new File(directoryPath);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			// Take Screenshot
			File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			// Save ScreenShot
			FileHandler.copy(srcFile, destinationFile);

			return destinationFile.getAbsolutePath();
		}

		catch (IOException e) {
			// Handle failure
			System.err.println("Failed to capture screenshot: " + e.getMessage());
			throw new RuntimeException("Screenshot failed", e);
		}

	}

	// Capture screenshot on failure
	public static String captureScreenshotOnFailure(WebDriver driver, String testName) {
		String failedTestName = testName + "_FAILED";

		// call capture Screenshot

		String absolutePath = captureScreenshot(driver, failedTestName);

		return absolutePath;

	}

}