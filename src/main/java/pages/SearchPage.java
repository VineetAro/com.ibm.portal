package pages;

import java.util.List;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import commons.BasePage;
import utils.JSExecutor;
import utils.WaitUtil;

public class SearchPage extends BasePage {

	// Locators

	private static final By ENTRY_TITLE = By.cssSelector(".entry-title");
	private static final By SEARCH_RESULTS = By.cssSelector(".search-results > li> h3 > a");
	private static final By NEXT_PAGE_BTN = By.cssSelector(".pagination > a");

	public SearchPage() {
		super();
	}

	@Override
	public boolean isPageLoaded() {
		String title = WaitUtil.waitForVisibilityOf(ENTRY_TITLE).getText();
		return title.equals("Search");
	}

	public WebElement findElementInResuts(String text) {

		while (true) {
			List<WebElement> results = WaitUtil.waitForPresenceElements(SEARCH_RESULTS);

			if (results.isEmpty()) {
				throw new RuntimeException("No Serach results found.");
			}
			// Filters the list of elements for the first match that exactly equals the
			// target text.
			// Returns an Optional to avoid NoSuchElementException if no match is found.
			Optional<WebElement> match = results.stream().peek(el -> {
				JSExecutor.scrollToElement(el);
				// Debug: Print the text of every element as we scroll to it
				System.out.println("Checking element text: [" + el.getText() + "]");
			}).filter(el -> el.getText().trim().equalsIgnoreCase(text)).findFirst();

			if (match.isPresent()) {
				return match.get();
			}

			// Match not found. click next

			List<WebElement> nextBtn = WaitUtil.waitForPresenceElements((NEXT_PAGE_BTN));
			JSExecutor.scrollToElements(nextBtn);

			int index_nextBtn = nextBtn.size() - 2;

			if (nextBtn.get(index_nextBtn).getAttribute("class").contains("disabled")) {
				throw new RuntimeException("Results not found: " + text);
			}

			nextBtn.get(index_nextBtn).click();
			WaitUtil.waitForStaleElement(results.get(0));

		}

	}

	public KnowledgeArticlePage clickOnSearchResult(String titleText) {
		WebElement result = findElementInResuts(titleText);
		result.click();
		return new KnowledgeArticlePage();

	}

}
