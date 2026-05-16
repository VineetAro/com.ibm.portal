package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import commons.BasePage;
import utils.LoggerUtil;
import utils.WaitUtil;

public class KnowledgeArticlePage extends BasePage {

	private static final By ENTRY_TITLE = By.cssSelector(".entry-title");

	public KnowledgeArticlePage() {
		super();
	}

	@Override
	public boolean isPageLoaded() {
		String title = WaitUtil.waitForVisibilityOf(ENTRY_TITLE).getText();
		return !title.equalsIgnoreCase("search") || !title.equalsIgnoreCase("report problems");

	}

	public SRFormsPage clickToReportProblem(String text) {
		WebElement element = WaitUtil.waitForClickable(By.linkText(text));
		element.click();
		LoggerUtil.actionSuccess("SR successfully Created");
		return new SRFormsPage();
	}
}
