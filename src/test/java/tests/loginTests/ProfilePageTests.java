package tests.loginTests;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import base.BaseTest;
import config.ConfigReader;
import pages.HomePage;
import pages.ProfilePage;
import utils.LoggerUtil;

public class ProfilePageTests extends BaseTest {

	@Test
	public void updateFirstName() {
		// 1. Start Logging
		LoggerUtil.testStart("Update First Name");

		// 2. Perform login and get the HomePage
		// The loginAsStandardUser() in BaseTest should use DriverManager.getDriver()
		HomePage homePage =  loginAsStandardUser(ConfigReader.getProperty("user"), ConfigReader.getProperty("password"));
		LoggerUtil.actionSuccess("Login Successful");

		// 3. Navigate - The ProfilePage constructor will now auto-wait for its anchor
		ProfilePage profilePage = homePage.navigateToProfilePage();
		LoggerUtil.actionSuccess("User Navigates to Profile page");

		// 4. Act
		profilePage.enterFirstName("TEST_USER");

		// 5. Save - The HomePage constructor will auto-wait for the dashboard anchor
		homePage = profilePage.saveProfile();
		LoggerUtil.actionSuccess("First name updated and navigated to HomePage");

		// 6. Assert - This failure will be caught by the TestListener automatically
		String actualUsername = homePage.getLoggedInUsername();
		assertTrue(actualUsername.contains("TEST_USER"),
				"Expected 'TEST_USER' in username but found: " + actualUsername);
		
		//Revert the changes
		
		profilePage = homePage.navigateToProfilePage();
		LoggerUtil.actionSuccess("User Navigates to Profile page");
		
		profilePage.enterFirstName("Vineet");
		homePage = profilePage.saveProfile();
		
		String updatedUsername = homePage.getLoggedInUsername();
		assertTrue(updatedUsername.contains("Vineet"),
				"Expected 'Vineet' in username but found: " + actualUsername);
	}
	
	
}