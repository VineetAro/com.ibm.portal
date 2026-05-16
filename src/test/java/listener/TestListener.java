package listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import driver.DriverFactory;
import utils.ReportUtil;
import utils.ScreenshotUtil;

public class TestListener implements ITestListener {

	private static final Logger logger = LogManager.getLogger(TestListener.class);

	@Override
	public void onTestStart(ITestResult result) {
		// Called when test starts

		// Get test name and description
		String testName = result.getName();

		String description = result.getMethod().getDescription();

		// Create ExtentTest using ReportUtil
		ReportUtil.createTest(testName, description);

		// Log start message
		logger.info("==== TEST STARTED: " + result.getName() + " ====");
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		// Called when test passes

		// Log success message

		// Log test duration
		long durationMillis = result.getEndMillis() - result.getStartMillis();
		double durationSeconds = durationMillis / 1000.0;
		logger.info("==== TEST PASSED: " + result.getName() + " Duration: " + durationSeconds + " seconds");
		ReportUtil.logSuccess("Test Passed Successfully");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		logger.error("Test Failed: " + result.getName());
		logger.error("Failure reason: " + result.getThrowable().getMessage());

		WebDriver _driver = DriverFactory.getDriver();
		if (_driver != null) {
			// 1. Get the element that failed from our ElementListener
			WebElement failingElement = driver.ElementListener.getFailedElement();

			if (failingElement != null) {
				// 2. Highlight it using your JSExecutor utility
				utils.JSExecutor.highlightElement(failingElement);
			}

			// 3. Capture screenshot (now it will show the highlight)
			String screenshotPath = ScreenshotUtil.captureScreenshot(_driver, result.getName() + "_FAILED");

			if (screenshotPath != null) {
				ReportUtil.attachScreenshot(screenshotPath);
			}

			// 4. Clear the reference for the next test on this thread
			driver.ElementListener.clear();
		}

		ReportUtil.logFailure("Test failed: " + result.getName());
		ReportUtil.logFailure("Exception: " + result.getThrowable().toString());
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		// Called when test is skipped
		// Log skip message
		logger.warn("TEST SKIPPED: " + result.getName());
		ReportUtil.logAction("Test skipped: " + result.getSkipCausedBy());
	}

	@Override
	public void onStart(ITestContext context) {
		// Called once at start of all tests
		logger.info("====== TEST SUITE STARTED: " + context.getName() + " ======");
		// Initialize reports
		ReportUtil.initializeReports();

	}

	@Override
	public void onFinish(ITestContext context) {
		logger.info("====== TEST SUITE FINISHED: " + context.getName() + " ======");
		logger.info("Total tests: " + context.getAllTestMethods().length);
		logger.info("Passed: " + context.getPassedTests().size());
		logger.info("Failed: " + context.getFailedTests().size());
		logger.info("Skipped: " + context.getSkippedTests().size());

		// Flush reports
		ReportUtil.flushReports();
	}
}
