package tests.loginTests;

import org.testng.annotations.Test;

import base.BaseTest;
import driver.DriverFactory;
import pages.HomePage;
import pages.LoginPage;
import utils.JSExecutor;
import utils.LoggerUtil;
import utils.ScreenshotUtil;

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
