package base;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import commons.BasePage;
import config.ConfigReader;
import driver.DriverFactory;
import pages.HomePage;
import pages.LoginPage;
import utils.ExcelUtil;
import utils.FakerUtil;
import utils.LoggerUtil;

public class BaseTest {

	// Implementing static getter for Thread local driver.
	// protected WebDriver driver;

	@BeforeMethod
	public void setUp(Method method) {

		// Init driver

		DriverFactory.initDriver();
		LoggerUtil.debug("Browser initialized: " + ConfigReader.getProperty("browser"));

		// Log start message
		LoggerUtil.testStart("==== TEST STARTED: " + method.getName() + " ====");

		// open url
		DriverFactory.getDriver().get(ConfigReader.getProperty("base.url"));

		// Clean browser state
		DriverFactory.getDriver().manage().deleteAllCookies();

	}

	protected void assertPageLoaded(BasePage page) {

		// Check if the object exists
		Assert.assertNotNull(page, "The Page Object was not initialized!");
		// Check if the actual website is displayed
		Assert.assertTrue(page.isPageLoaded(),
				"FAILED: " + page.getClass().getSimpleName() + " did not load on the browser.");
	}

	protected void assertElementVisible(By locator) {
		try {
			boolean isVisible = DriverFactory.getDriver().findElement(locator).isDisplayed();
			Assert.assertTrue(isVisible, "FAILED: Element with locator [" + locator + "] is not visible on the page.");
		} catch (NoSuchElementException e) {
			Assert.fail("FAILED: Element with locator [" + locator + "] was not found in the DOM.");
		}

	}

	protected void assertText(String actual, String expected) {
		Assert.assertEquals(actual, expected, "❌ Expected Value: " + expected + " but value found: " + actual);

	}

	protected void assertElementHasText(By locator, String expectedText) {
		try {
			// 1. Capture the actual text from the UI
			String actualText = DriverFactory.getDriver().findElement(locator).getText().trim();

			// 2. Compare and provide a meaningful error message for logs
			Assert.assertEquals(actualText, expectedText, "FAILED: Text mismatch for element [" + locator + "]");

			System.out.println("PASSED: Element contains expected text: " + expectedText);
		} catch (NoSuchElementException e) {
			Assert.fail("FAILED: Could not find element [" + locator + "] to verify text.");
		}
	}

	// Login Method
	protected HomePage loginAsStandardUser(String user, String password) {
		HomePage homePage = new HomePage();
		LoginPage loginPage = homePage.clickSignIn();
		LoggerUtil.testStart("Start Login process");

		return loginPage.loginAs(user, password);
	}

	@AfterMethod
	public void tearDown(ITestResult result) {
		// Determine status for LoggerUtil
		boolean passed = (result.getStatus() == ITestResult.SUCCESS);

		// Log the end of the test before quitting
		LoggerUtil.testEnd(result.getMethod().getMethodName(), passed);
		if (DriverFactory.getDriver() != null) {
			DriverFactory.quitDriver();
		}
	}

	// HELPER METHODS

	protected Map<String, String> parseExcelAssertColumn(String rawAssertStr) {
		Map<String, String> checklistMap = new HashMap<>();

		if (rawAssertStr == null || rawAssertStr.trim().isEmpty() || "-1".equals(rawAssertStr)) {
			return checklistMap;
		}

		// Clean out surrounding data debris like extra quotes or curly brackets if
		// present
		String sanitizedStr = rawAssertStr.replace("{", "").replace("}", "").replace("\"", "").trim();

		// Split fields by semicolon boundaries cleanly
		String[] pairs = sanitizedStr.split(";");
		for (String pair : pairs) {
			if (pair.contains("=")) {
				String[] splitPair = pair.split("=", 2);
				String labelKey = splitPair[0].trim();
				String expectedVal = splitPair[1].trim();

				if (!labelKey.isEmpty()) {
					checklistMap.put(labelKey, expectedVal);
				}
			}
		}
		return checklistMap;
	}

	// Data Providers

	@DataProvider(name = "loginDataFromExcel")
	public Object[][] getLoginDataFromExcel() {
		List<Map<String, String>> data = ExcelUtil.readExcelData("LoginTestData.xlsx", "logins");
		// Convert to Object[][]
		Object[][] result = new Object[data.size()][];
		for (int i = 0; i < data.size(); i++) {
			Map<String, String> row = data.get(i);
			result[i][0] = row.get("username");
			result[i][1] = row.get("password");
			result[i][2] = Boolean.parseBoolean(row.get("shouldSucceed"));
		}

		return result;
	}

	@DataProvider(name = "generateRandomLoginData")
	public Object[][] generateRandomLoginData() {
		Object[][] result = new Object[5][3];
		for (int i = 0; i < 5; i++) {
			result[i][0] = FakerUtil.generateEmail();
			result[i][1] = FakerUtil.generatedPassword();
			result[i][2] = false;

		}
		return result;
	}

	@DataProvider(name = "positiveSRDataFromExcel")
	public Object[][] getPositiveSRDataFromExcel() {
		// 1. Fetch data directly using your existing ExcelUtil utility
		List<Map<String, String>> data = ExcelUtil.readExcelData("PositiveSRTestData.xlsx", "noise");

		// 2. Dynamically size the matrix array based purely on row population count
		Object[][] result = new Object[data.size()][1];

		for (int i = 0; i < data.size(); i++) {
			// 3. Pass the entire column map for this row directly into the data matrix
			result[i][0] = data.get(i);
			System.out.println("DataProvider packaged row data: " + result[i][0]);
		}

		return result;
	}

	/**
	 * @DataProvider(name = "positiveSRDataFromExcel") public Object[][]
	 *                    getPositiveSRDataFromExcel() { List<Map<String, String>>
	 *                    data = ExcelUtil.readExcelData("PositiveSRTestData.xlsx",
	 *                    "noise"); // Convert to Object[][] System.out.println();
	 *                    Object[][] result = new Object[data.size()][]; for (int i
	 *                    = 0; i < data.size(); i++) { Map<String, String> row =
	 *                    data.get(i); result[i][0] = row.get("searchText");
	 *                    result[i][1] = row.get("kaLink"); result[i][2] =
	 *                    row.get("reportProblemLink"); result[i][3] =
	 *                    row.get("problem"); result[i][4] =
	 *                    row.get("problemDetails"); result[i][5] =
	 *                    row.get("additionalDetails"); result[i][6] =
	 *                    row.get("description"); result[i][7] = row.get("date");
	 *                    result[i][8] = row.get("time"); result[i][9] =
	 *                    row.get("isRecurring"); result[i][10] =
	 *                    row.get("recurringDesc"); result[i][11] =
	 *                    row.get("locationType"); result[i][12] =
	 *                    row.get("address"); result[i][13] = row.get("parkName");
	 *                    result[i][14] = row.get("parkAddress"); result[i][15] =
	 *                    row.get("firstName"); result[i][16] = row.get("lastName");
	 *                    result[i][17] = row.get("phone"); result[i][18] =
	 *                    row.get("email"); result[i][19] =
	 *                    row.get("hasAttachement"); result[i][20] =
	 *                    row.get("attachmentLink");
	 * 
	 *                    }
	 * 
	 *                    return result; }
	 * 
	 **/
}
