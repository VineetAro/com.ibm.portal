package driver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.events.EventFiringDecorator;

import config.ConfigReader;

public class DriverFactory {
	
	private static final Logger logger = LogManager.getLogger(DriverFactory.class);

	// 1. The ThreadLocal "Container" (Every thread gets its own driver)
	private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

	/**
	 * This method initializes the driver based on the browser name
	 */
	private DriverFactory() {

	}

	public static WebDriver initDriver() {
		// get browser from config.properties
		  System.out.println("STEP 1: Entered initDriver");
		String browser = ConfigReader.getProperty("browser");

		if (browser == null) {
			throw new RuntimeException("Browser property is missing in config.properties");
		}

		// Read Headless from the config.properties
		String headless = ConfigReader.getProperty("headless");
		
		  System.out.println("STEP 2: Browser = " + browser);
		WebDriver localDriver;

		switch (browser.toLowerCase()) {
		case "chrome":
			System.out.println("STEP 3: Before ChromeDriver");
			localDriver = createChromeDriver(headless);
			System.out.println("STEP 4: After ChromeDriver");
			break;
			
			
		case "firefox":
			   System.out.println("STEP 3: Before FirefoxDriver");
			localDriver = createFirefoxDriver(headless);
		      System.out.println("STEP 4: After FirefoxDriver");
			break;

		default:
			throw new IllegalArgumentException("Browser not supported " + browser);
		}
		ElementListener listener = new ElementListener();
		// 2. Wrap/Decorate the localDriver
	    WebDriver decoratedDriver = new EventFiringDecorator<>(listener).decorate(localDriver);

	    // 3. Store the DECORATED driver in ThreadLocal
	    driver.set(decoratedDriver);
	    
		//driver.set(localDriver);

	    System.out.println("STEP 5: Driver set");
		
		
		getDriver().manage().deleteAllCookies();
		getDriver().manage().window().maximize();
		 
		 

		return getDriver();

	}

	private static WebDriver createChromeDriver(String headless) {
		ChromeOptions options = new ChromeOptions();
		// This tells Chrome to ignore the "Not Private" warning
		options.setAcceptInsecureCerts(true); 
		options.addArguments("--remote-allow-origins=*");
		if (Boolean.parseBoolean(headless)) {
			options.addArguments("--headless");
		}
		return new ChromeDriver(options);
	}
	
	private static WebDriver createFirefoxDriver(String headless) {
		FirefoxOptions options = new FirefoxOptions();
		// This tells Chrome to ignore the "Not Private" warning
		options.setAcceptInsecureCerts(true); 
		//options.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
	
		if (Boolean.parseBoolean(headless)) {
			options.addArguments("--headless");
		}
		return new FirefoxDriver(options);
	}

	/**
	 * This is the "Getter" to fetch the driver for the specific thread
	 */
	public static WebDriver getDriver() {
		return driver.get();
	}

	// Close the driver.

	public static void quitDriver() {
		
		if (getDriver() != null) {
			getDriver().quit();
			driver.remove();
		}

	}
}
