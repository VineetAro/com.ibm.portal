package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerUtil {

	private static final Logger logger = LogManager.getLogger(LoggerUtil.class);

	public static void testStart(String testName) {
		logger.info("****************************************************************************************");
		logger.info(">>>> TEST STARTED: " + testName);
		logger.info("****************************************************************************************");
	}

	public static void testEnd(String testName, boolean passed) {
		String status = passed ? "PASSED" : "FAILED";

		logger.info("****************************************************************************************");
		logger.info("<<<< TEST " + status + " : " + testName);
		logger.info("****************************************************************************************");
	}

	public static void actionSuccess(String action) {
		logger.info("✔️ ACTION SUCCESS: " + action);
	}

	public static void actionFailure(String action, Exception e) {
		logger.error("❌ ACTION FAILURE: " + action + " | Error: " + e.getMessage());
	}
	
	public static void debug(String message) {
	    logger.debug("🔍 DEBUG: " + message);
	}
	
	public static void info(String message) {
	    logger.info("🔍 INFO: " + message);
	}

	public static void warn(String message) {
	    logger.warn("⚠️ WARNING: " + message);
	}

	public static void assertion(String expected, String actual, boolean passed) {
		if (passed) {
			logger.info("✅ ASSERTION PASSED: Expected [" + expected + "] and got [" + actual + "]");
		} else {
			logger.error("🛑 ASSERTION FAILED: Expected [" + expected + "] but got [" + actual + "]");
		}
	}

}
