package tests.loginTests;

import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import base.BaseTest;
import config.ConfigReader;
import driver.DriverFactory;
import pages.HomePage;
import utils.JsonUtil;
import utils.LoggerUtil;

public class HomePageTest extends BaseTest{
	
	
	
	
	@Test(enabled = true)
	public void verifyHomePageloggedInUser() {
		LoggerUtil.testStart("Verify Home Page");
		HomePage homePage = new HomePage();
		homePage = loginAsStandardUser(ConfigReader.getProperty("user"), ConfigReader.getProperty("password"));
		LoggerUtil.actionSuccess("Logged in Successfully");
		homePage.isPageLoaded();
		String loggedInUsername = homePage.getLoggedInUsername();
		LoggerUtil.assertion(loggedInUsername, "Vineet Arora", true);
	}
	
	
	@Test(enabled = false)
	public void testJson() {
		 List<Map<String, String>> data =JsonUtil.readJsonArray("users.json", "users");
		 for (Map<String, String> m : data) {
			 	System.out.println(m);
		 }
		
	}
	

}
