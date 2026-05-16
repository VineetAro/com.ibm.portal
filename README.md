🚨Disclaimer

> ⚠️ **Educational Purpose Only**
> This is a self-learning project demonstrating Test Automation patterns and best practices. 
> Not intended for production use.


# 🗽 NYC 311 Service Request Automation Framework

[![Java Version](https://shields.io)](https://oracle.com)
[![Selenium](https://shields.io)](https://selenium.dev)
[![TestNG](https://shields.io)](https://testng.org)

A robust, enterprise-grade test automation ecosystem engineered in **Java** and **Selenium WebDriver** for the **NYC 311 portal**. Architected around the **Page Object Model (POM)** design pattern, this framework handles dynamic web components, multi-environment profiles, and data-driven verification engines.

---

## 🗺️ Quick Navigation
* [🚀 Key Architecture Pillars](#-key-architecture-pillars)
* [📂 Interactive Directory Tree](#-interactive-directory-tree)
* [⚙️ Multi-Environment Profiles](#%EF%B8%8F-multi-environment-profiles)
* [🧪 Test Data Engine Config](#-test-data-engine-config)
* [💻 Test Execution Hub](#-test-execution-hub)
* [🛠️ Real-World Troubleshooting Guide](#%EF%B8%8F-real-world-troubleshooting-guide)

---

## 🚀 Key Architecture Pillars

*   **🔒 Thread-Safe Factory Engine:** Managed via `DriverFactory`, preventing state corruption during parallel test executions.
*   **🔄 Dynamic Sync Architecture:** Zero flaky `Thread.sleep()` statements. Built with smart synchronization guards inside `WaitUtil`.
*   **📊 Multi-Source Data Injection:** Dynamically processes parameters from binary Microsoft Excel arrays (`.xlsx`) and structured `JSON` matrices.
*   **⚡ JavaScript Dom Manipulators:** Custom injection engine (`JSExecutor`) to cleanly bring obscured dropdown items into view.
*   **🎭 Custom Test Listeners:** TestNG listeners (`TestListener`) override lifecycle steps to automatically capture failure diagnostics.

---

## 📂 Interactive Directory Tree

<details open>
<summary><b>▶ Click to Expand / Collapse Project Workspace</b></summary>

```text
├── src/main/java
│   ├── commons
│   │   └── BasePage.java             # Common structural web-driver interaction layer
│   ├── config
│   │   └── ConfigReader.java         # Singleton engine mapping environment matrices
│   ├── driver
│   │   ├── DriverFactory.java        # Thread-local browser initialization container
│   │   └── ElementListener.java      # Automation interceptor tracking active element actions
│   ├── pages
│   │   ├── HomePage.java             # Portal landing gateway components
│   │   ├── LoginPage.java            # Secure user profile authorization components
│   │   ├── ReportProblem.java        # Service request routing index page
│   │   ├── WhatSectionPage.java      # Form step 1: Problem details component map
│   │   ├── WhereSectionPage.java     # Form step 2: Geo-location & address verification maps
│   │   ├── WhoSectionPage.java       # Form step 3: Contact profiling components
│   │   └── ReviewSectionPage.java    # Form step 4: Global validation review matrix
│   └── utils
│       ├── ActionUtils.java          # Wrapper methods handling lists and select controls
│       ├── ExcelUtil.java            # Multi-sheet .xlsx test matrix parsing core
│       ├── JsonUtil.java             # JSON string serializer/deserializer data tool
│       ├── JSExecutor.java           # JavaScript script execution engine
│       ├── LoggerUtil.java           # Formatted runtime execution logger
│       ├── ScreenshotUtil.java       # Failed context runtime screenshot compiler
│       └── WaitUtil.java             # Explicit element sync interceptors
│
├── src/main/resources
│   ├── config
│   │   ├── config.properties         # System driver global property defaults
│   │   ├── dev.properties            # Sandbox / Dev tier execution endpoints
│   │   ├── staging.properties        # Staging / Pre-prod system properties
│   │   └── prod.properties           # Live environment smoke check properties
│   └── log4j2.xml                    # Extensible log logging layout specifications
│
└── src/test
    ├── java
    │   ├── base
    │   │   └── BaseTest.java         # Master context initializer & parsing validator
    │   ├── listener
    │   │   └── TestListener.java     # TestNG runtime state listener engine
    │   └── tests
    │       ├── createServiceRequest  
    │       │   └── NoiseSRTest.java  # Noise validation suites (Commercial & Park complaints)
    │       └── loginTests            
    │           ├── LoginTests.java   # Account portal authentication tests
    │           └── ProfilePageTests.java
    └── resources
        ├── testdata
        │   ├── LoginTestData.xlsx    # Data matrix feeding user verification suites
        │   ├── PositiveSRTestData.xlsx # Complete data repository for valid complaint scenarios
        │   └── users.json            # Dynamic user profiles dataset
        └── testngsuites
            ├── smoke.xml             # Targeted critical-path validation test suites
            └── regression.xml        # Comprehensive system coverage execution rules
```
</details>

---

## ⚙️ Multi-Environment Profiles

The workspace manages target platform settings inside `src/main/resources/config/`. Toggle the main environment profile flag to steer execution destinations instantly:

📁 **`config.properties`**
```properties
env=staging
browser=chrome
implicit.wait=0
explicit.wait=20
```

📁 **`staging.properties`**
```properties
url=https://nyc.gov
db.connection=jdbc:oracle:thin:@//stage-db:1521/311
```

---

## 🧪 Test Data Engine Config

The automated frameworks feed parameters to `@DataProvider` configurations using dual-format arrays.

### 1. Excel Parameter Slicing
Ensure your spreadsheet row keys strictly match the schema expected by the validation layer:
```text
┌─────────────────┬───────────────────┬───────────────────────────────┐
│ searchText      │ problem           │ problemDetails                │
├─────────────────┼───────────────────┼───────────────────────────────┤
│ Noise Commercial│ Noise - Commercial│ Banging/Pounding              │
└─────────────────┴───────────────────┴───────────────────────────────┘
```

### 2. Structured String Splitting Pattern
When parsing text properties dynamically mapped as key-value pairs (e.g., `Assert={Problem=Noise, Detail=Loud}`), the parser uses specific length limits to remain safe:
```java
// Limit '2' ensures strings are only split on the FIRST '=' sign
// This prevents breaking valid data payloads that contain secondary '=' symbols
String[] splitPair = pair.split("=", 2);
```

---

## 💻 Test Execution Hub

Execute your test suites using your terminal via standard Maven commands:

```bash
# Run the critical path Smoke Test Suite
mvn clean test -DsuiteXmlFile=src/test/resources/testngsuites/smoke.xml

# Run the full regression coverage sweep
mvn clean test -DsuiteXmlFile=src/test/resources/testngsuites/regression.xml

# Run tests targeting specific browser types via terminal override
mvn test -DsuiteXmlFile=src/test/resources/testngsuites/smoke.xml -Dbrowser=edge
```

---

## 🛠️ Real-World Troubleshooting Guide

### 1. Stale / Zero-Length Sibling Elements (`Default Value 0`)
*   **Issue:** Searching relative paths using absolute notation tags overrides local element focuses.
*   **Fix:** Always precede relative XPath lookups with a dot locator (`.`) to lock scope context inside the parent element:
    ```java
    // ❌ Error: searches globally from document root
    List<WebElement> list = element.findElements(By.xpath("/following-sibling::span")); 

    //  Fix: searches locally relative to current element
    List<WebElement> list = element.findElements(By.xpath("./following-sibling::span")); 
    ```

### 2. Flaky Custom Dropdown Selections
*   **Issue:** Missing explicit clicks or text spaces inside target arrays causing dropdown fields to hang or time out.
*   **Fix:** Clean array target parameters with `.trim()` and execute `.click()` inside custom option lists:
    ```java
    if (option.getText().trim().equals(value)) {
        JSExecutor.scrollToElement(option);
        new Actions(driver).moveToElement(option).perform();
        option.click(); // <--- Critical click execution
        break;
    }
    ```

### 3. Assertion Failures Finding `null`
*   **Issue:** Typos in keys between the test data files and the UI text maps (e.g., `Problem Details` vs `Problem Detail`).
*   **Fix:** Ensure your data sheets match your UI element parser exactly, or use safe fallbacks in your base validation methods to parse singular and plural spelling variations.
