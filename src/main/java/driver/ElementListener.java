package driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;

import commons.BasePage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ElementListener implements WebDriverListener {
	
	private static final Logger logger = LogManager.getLogger(ElementListener.class);
    // ThreadLocal ensures it works with your parallel="methods" setup
    private static ThreadLocal<WebElement> lastFailedElement = new ThreadLocal<>();

    @Override
    public void onError(Object target, Method method, Object[] args, InvocationTargetException e) {
        // If the error happened on a WebElement (like a failed click)
        if (target instanceof WebElement) {
            lastFailedElement.set((WebElement) target);
        } 
        // If the element was passed as an argument (like findElement failing)
        else if (args != null && args.length > 0 && args[0] instanceof WebElement) {
            lastFailedElement.set((WebElement) args[0]);
        }
    }

    public static WebElement getFailedElement() {
        return lastFailedElement.get();
    }

    public static void clear() {
        lastFailedElement.remove();
    }
}
