package pages;

import java.lang.System.Logger;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import commons.BasePage;
import utils.ActionUtils;
import utils.WaitUtil;

public class ReviewSectionPage extends BasePage{
	
	private static final By CURRENT_STEP  = By.cssSelector("#pb-bar .active .pb-label");
	
	private static final By FORM_LABELS = By.cssSelector(".form-group label");
	
	private static final By HAVE_ATTACHMENTS_YES_RADIO_BTN = By.cssSelector("input[type='radio'][value='YES']");
	
	private static final By HAVE_ATTACHMENTS_NO_RADIO_BTN = By.cssSelector("input[type='radio'][value='NO']");
	
	private static final By ATTACHEMENT_MESSAGE = By.cssSelector(".op + label");
	
	public ReviewSectionPage() {
		super();
	}
	
	public HashMap<String, String> getSrFormSelectedValues(){
		
		HashMap<String, String> map = new HashMap<>();
		List<WebElement> labels = WaitUtil.waitForPresenceElements(FORM_LABELS);
		for(WebElement label : labels) {
			String value = label.findElement(By.xpath("./following-sibling::span")).getText();
			map.put(label.getText(), value);
		}
		
		return map;
	}
	
	public void printHashMap() {
		HashMap<String, String> map = getSrFormSelectedValues();
		
		for(String key: map.keySet()) {
			System.out.println(key+" == "+map.get(key));
		}
	}
	public String getValueFromHashMap(String key) {
		HashMap<String, String> map = getSrFormSelectedValues();
		return map.get(key);
	}
	@Override
	public boolean isPageLoaded() {
		System.out.println("HURRRRRRAYYYYY REVIEWWWW PAGE FINALLY");
		return ActionUtils.getLabelText(CURRENT_STEP).equalsIgnoreCase("review");
	}
	
	

}
