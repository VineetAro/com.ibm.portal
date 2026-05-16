package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import commons.BasePage;
import io.github.cdimascio.dotenv.Dotenv;
import utils.LoggerUtil;

/**
 * This class reads the configuration from the config.properties file.
 */
public class ConfigReader {
	private static final Logger logger = LogManager.getLogger(ConfigReader.class);

	private static Properties prop;
	private static Dotenv dotenv;
	static {
		prop = new Properties();
		loadProperties("src/main/resources/config/config.properties");
		
		// Load the .env file
        dotenv = Dotenv.configure().ignoreIfMissing().load();
		
		
		String env = System.getProperty("environment");
		

		if (env == null || env.isEmpty()) {
			env = prop.getProperty("environment","staging"); // If the test is not triggered via command line - default config file is red.
		}
		
		
		loadProperties("src/main/resources/config/" + env + ".properties");
		
		validateProperties();
	}

	// Read property value
	public static String getProperty(String key) {
		
		// Check .env first (Priority for secrets)
	    if (dotenv != null && dotenv.get(key) != null) {
	        return dotenv.get(key);
	    }
		
	    //Fallback
		String value = prop.getProperty(key);
		
		if (value == null || value.trim().isEmpty()) {
			throw new RuntimeException("CRITICAL ERROR: Key '" + key + "' not found in config.properties!");

		}
		return value;
	}

	// Get property with default value
	public static String getProperty(String key, String defaultValue) {
		//Return property or return defaultValue if not found
		String value = prop.getProperty(key);
	
		if (value == null || value.trim().isEmpty()) {
			return defaultValue;

		}
		return value;
	}

	// Get property as integer (useful for timeouts)
	public static int getIntProperty(String key) {
		String value = getProperty(key);

		try {
			// Convert the String to an Integer
			return Integer.parseInt(value.trim());
		} catch (NumberFormatException e) {
			// Handle cases where the config has non-numeric text (e.g., timeout=abc)
			throw new RuntimeException(
					"CRITICAL ERROR: Key '" + key + "' with value '" + value + "' is not a valid integer!");
		}

	}
	
	// Get property as boolean (useful for headless mode)
	public static boolean getBooleanProperty(String key) {
		String value = getProperty(key);

		if (value.equals("true")) {
			return true;
		} else if (value.equals("false")) {
			return false;
		} else {
			// Since Boolean.parseBoolean doesn't throw exceptions, we throw our own
			throw new RuntimeException("CRITICAL ERROR: Key '" + key + "' has value '" + value
					+ "', which is not a valid boolean (true/false)!");
		}
	}

	// Load specific properties file
	private static void loadProperties(String filepath) {
		// TODO: Read from filepath and load into properties object

		try {
			FileInputStream fis = new FileInputStream(filepath);
			prop.load(fis);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load config.properties file");
		}
	}
	

	// Validate Properties
	private static void validateProperties() {
		  // Check browser is valid
		  String browser = getProperty("browser");
		  if (!browser.matches("chrome|firefox|edge")) {
		    throw new IllegalArgumentException("Invalid browser: " + browser);
		  }
		  
		  // Check base.url is not empty
		  String baseUrl = getProperty("base.url");
		  if (baseUrl == null || baseUrl.isEmpty()) {
		    throw new RuntimeException("base.url property not found!");
		  }
		  
	  }
	
}