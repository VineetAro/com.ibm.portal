package tests.loginTests;

import org.testng.annotations.Test;

import base.BaseTest;
import driver.DriverFactory;
import pages.HomePage;
import pages.LoginPage;
import utils.JSExecutor;
import utils.LoggerUtil;
import utils.ScreenshotUtil;

/**
 * LoginTests - Verifies login functionality for NYC 311 Portal
 * 
 * This test class covers:
 * - Valid user login with correct credentials
 
 * 
 * 
 * Test Data: Retrieved from resources/testdata/login_credentials.xlsx
 * Dependencies: LoginPage, HomePage
 * Prerequisites: Application must be accessible and database populated
 * 
 * @author [Developer Name]
 * @version 1.0
 * @since 2026-05-16
 * @see LoginPage
 * @see HomePage
 */


public class LoginTests extends BaseTest {


	@Test(dataProvider = "loginDataFromExcel")
	public void loginWithExcelData(String username, String password, Boolean shouldSucceed) {
		String testName = "loginWithExcelData";
		LoggerUtil.testStart(testName);
		HomePage homePage = new HomePage();

		LoginPage loginPage = homePage.clickSignIn();
		loginPage.enterUsername(username);
		loginPage.enterPassword(password);

		if (shouldSucceed) {
			homePage = loginPage.clickLoginButton();
			LoggerUtil.actionSuccess("Performed Login Action");
			assertElementVisible(homePage.getLoggedInLocator());
		} else {
			assertPageLoaded(loginPage);
			LoggerUtil.actionSuccess("Action - Incorrect credentials");
		}

	}

	@Test(dataProvider = "generateRandomLoginData")
	public void loginWithFakerData(String username, String password, Boolean shouldSucceed) {
		String testName = "loginWithFAKERData";
		LoggerUtil.testStart(testName);

		HomePage homePage = new HomePage();

		LoginPage loginPage = homePage.clickSignIn();
		loginPage.enterUsername(username);
		loginPage.enterPassword(password);

		if (shouldSucceed) {
			homePage = loginPage.clickLoginButton();
			assertElementVisible(homePage.getLoggedInLocator());
			LoggerUtil.actionSuccess("Performed Login Action");
		} else {
			assertPageLoaded(loginPage);
			LoggerUtil.actionSuccess("Action - Incorrect credentials");
		}

	}

}
