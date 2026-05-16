package tests.createServiceRequest;

import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;
import base.BaseTest;
import pages.HomePage;
import pages.KnowledgeArticlePage;
import pages.ReportProblem;
import pages.ReviewSectionPage;
import pages.SearchPage;
import pages.WhatSectionPage;
import pages.WhereSectionPage;
import pages.WhoSectionPage;
import utils.LoggerUtil;

public class NoiseSRTest extends BaseTest {

	@Test(dataProvider = "positiveSRDataFromExcel")
	public void createNoiseSR(Map<String, String> testData) throws InterruptedException {

		HomePage home = new HomePage();
		
		// Extract variables dynamically from the testData map object container
		String kaLink = testData.get("kaLink");
		String searchText = testData.get("searchText");
		String problem = testData.get("problem");
		String problemDetails = testData.get("problemDetails");
		String description = testData.get("description");
		String isRecurring = testData.get("isRecurring");
		String recurringDesc = testData.get("recurringDesc");
		String locationType = testData.get("locationType");
		String parkName = testData.get("parkName");
		String reportProblemLink = testData.get("reportProblemLink");
		String firstName = testData.get("firstName");
		String lastName = testData.get("lastName");
		String phone = testData.get("phone");
		String email = testData.get("email");
		String rawSRData = testData.get("assert");
		String address = testData.get("address");
		
		
		LoggerUtil.testStart("Create a " + kaLink + " Service request");
		ReportProblem reportProblem = home.navigateToReportProblems();
		LoggerUtil.info("Navigate to Report Problem");
		
		SearchPage searchPage = reportProblem.enterProblemAndClick(searchText);
		LoggerUtil.actionSuccess("Entered " + problem + " and clicked search");
		
		KnowledgeArticlePage kaPage = searchPage.clickOnSearchResult(kaLink);
		WhatSectionPage whatSection = kaPage.clickToReportProblem(reportProblemLink).getActiveSection();
		
		whatSection.selectProblemDetail(problemDetails);
		whatSection.selectDateTimeObserved();
		whatSection.enterDescription(description);
		whatSection.selectRecurringProblem(isRecurring);
		whatSection.enterDescribeHowManyTime(recurringDesc);
		
		WhereSectionPage whereSection = whatSection.clickNext();
		whereSection.selectLocationType(locationType);
		whereSection.enterStreetAddress(address);
		whereSection.clickSelectParkBtn(locationType);
		whereSection.enterParkName(parkName);
		whereSection.selectParkFromList(parkName);
		whereSection.clickChooseParkBtn(parkName); // Note: Removed 'parkName' parameter to match your updated page method
		
		WhoSectionPage whoSection = whereSection.clickNextBtn();
		whoSection.enterFirstName(firstName);
		whoSection.enterLastName(lastName);
		whoSection.enterPhone(phone);
		whoSection.enterEmail(email);
		
		ReviewSectionPage reviewPage = whoSection.clickNextBtn();
		reviewPage.printHashMap();
		
		//Create Hashmap of expected values. 
		Map<String, String> expectedMap = parseExcelAssertColumn(rawSRData);
		System.out.println("ExpectedMap: "+ expectedMap.get("Problem Detail"));
		
		Map<String, String> actualMap = reviewPage.getSrFormSelectedValues();
		System.out.println("ACTUAL Map: "+ actualMap.get("Problem Detail"));
		//Assert the data in Review Page
		
		for(String key : expectedMap.keySet()) {
		
			assertText(actualMap.get(key), expectedMap.get(key));
			
		}
		
		
	}
}
