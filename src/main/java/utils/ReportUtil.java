package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import config.ConfigReader;

public class ReportUtil {

	private static ExtentReports extentReports;
	private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

	// Initialize ExtentReports (call once at start)
	public static void initializeReports() {

		// Create ExtentSparkReporter with path to report file
		ExtentSparkReporter reporter = new ExtentSparkReporter("reports/extent-report.html");

		// TODO: Set report title, document title, theme
		reporter.config().setDocumentTitle("Test Execution Report");
		reporter.config().setTheme(Theme.DARK);
		reporter.config().setReportName("NYC311 Portal Automation");

		// Create ExtentReports instance

		extentReports = new ExtentReports();
		// Attach spark reporter
		extentReports.attachReporter(reporter);
		extentReports.setSystemInfo("Browser", ConfigReader.getProperty("browser"));
		extentReports.setSystemInfo("Env:", ConfigReader.getProperty("env"));
	}

	// Create test for current test method
	public static void createTest(String testName, String description) {
		// TODO: Create ExtentTest
		ExtentTest test = extentReports.createTest(testName, description);

		// Store in ThreadLocal
		extentTest.set(test);

	}

	// Log action
	public static void logAction(String message) {
		// Get test from ThreadLocal
		ExtentTest test = extentTest.get();
		// Log message
		if (test != null) {
			test.info(message);
		}
	}

	// Log success
	public static void logSuccess(String message) {
		// Log with PASS status
		ExtentTest test = extentTest.get();
		// Log message
		if (test != null) {
			test.pass(message);
		}
	}

	// Log failure
	public static void logFailure(String message) {
		// Log with FAIL status
		ExtentTest test = extentTest.get();
		// Log message
		if (test != null) {
			test.fail(message);
		}
	}

	// Attach screenshot
	public static void attachScreenshot(String screenshotPath) {
		// Get test from ThreadLocal
		ExtentTest test = extentTest.get();
		// Attach screenshot file
		if (test != null) {
			test.addScreenCaptureFromPath(screenshotPath);
		}
	}

	// Flush report to file
	public static void flushReports() {
		// Call extentReports.flush()
		extentReports.flush();
	}

	// Get current test
	public static ExtentTest getCurrentTest() {
		return extentTest.get();
	}

	// Remove test from ThreadLocal
	public static void removeTest() {
		extentTest.remove();

	}

}
