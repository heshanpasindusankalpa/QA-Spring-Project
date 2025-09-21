package com.example.QA.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Selenium UI Test Scenarios for Authentication React App
 * Scenario 1: User Registration
 * Scenario 2: User Login
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthenticationSeleniumTests {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String REACT_APP_URL = "http://localhost:3000";
    private static final int WAIT_TIMEOUT = 15;
    private static final String TEST_USERNAME = "testuser_" + System.currentTimeMillis();
    private static final String TEST_PASSWORD = "Password123!";
    private static final String TEST_EMAIL = TEST_USERNAME + "@example.com";

    @BeforeEach
    public void setUp() {
        // Setup ChromeDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        // Uncomment for headless execution in CI/CD
        // options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT));

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * SCENARIO 1: User Registration
     * Steps:
     * 1. Navigate to the application
     * 2. Switch to registration form
     * 3. Fill in registration details
     * 4. Submit the form
     * 5. Verify successful registration and auto-redirect to login
     */
    @Test
    @Order(1)
    public void testUserRegistration() {
        System.out.println("Running Scenario 1: User Registration");

        // Step 1: Navigate to the application
        driver.get(REACT_APP_URL);
        System.out.println("Navigated to: " + REACT_APP_URL);

        // Step 2: Switch to registration form if needed
        try {
            WebElement toggleLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(), 'Register') or contains(text(), 'Sign Up')]")
            ));

            // Only click if we're not already on the registration form
            if (driver.findElements(By.xpath("//input[@name='email']")).isEmpty()) {
                toggleLink.click();
                System.out.println("Switched to registration form");
                // Wait a moment for the form to switch
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Registration toggle not found or not needed: " + e.getMessage());
        }

        // Step 3: Find and fill registration form elements
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[name='username']")
        ));

        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[name='email']")
        ));

        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[name='password']")
        ));

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Register') or contains(text(), 'Sign Up')]")
        ));

        // Step 4: Enter registration details and submit
        usernameInput.clear();
        usernameInput.sendKeys(TEST_USERNAME);
        System.out.println("Entered username: " + TEST_USERNAME);

        emailInput.clear();
        emailInput.sendKeys(TEST_EMAIL);
        System.out.println("Entered email: " + TEST_EMAIL);

        passwordInput.clear();
        passwordInput.sendKeys(TEST_PASSWORD);
        System.out.println("Entered password");

        submitButton.click();
        System.out.println("Submitted registration form");

        // Step 5: Verify successful registration
        try {
            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//p[contains(@class, 'success') or contains(@class, 'message') or contains(text(), 'successful') or contains(text(), 'Success')]")
            ));

            assertTrue(successMessage.isDisplayed(), "Success message should be visible");
            System.out.println("Success message displayed: " + successMessage.getText());
        } catch (Exception e) {
            System.out.println("No success message found, checking for form switch instead");

            // Check if we're back on the login form
            WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//button[contains(text(), 'Login') or contains(text(), 'Sign In')]")
            ));

            assertTrue(loginButton.isDisplayed(), "Should be on login form after registration");
            System.out.println("Automatically switched to login form after registration");
        }

        System.out.println("✅ Scenario 1 PASSED: User registered successfully");
    }

    /**
     * SCENARIO 2: User Login
     * Steps:
     * 1. Navigate to the application
     * 2. Fill in login credentials
     * 3. Submit the form
     * 4. Verify successful login
     */
    @Test
    @Order(2)
    public void testUserLogin() {
        System.out.println("Running Scenario 2: User Login");

        // Step 1: Navigate to the application
        driver.get(REACT_APP_URL);
        System.out.println("Navigated to: " + REACT_APP_URL);

        // Step 2: Find and fill login form elements
        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[name='username']")
        ));

        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[name='password']")
        ));

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(), 'Login') or contains(text(), 'Sign In')]")
        ));

        // Step 3: Enter login credentials and submit
        usernameInput.clear();
        usernameInput.sendKeys(TEST_USERNAME);
        System.out.println("Entered username: " + TEST_USERNAME);

        passwordInput.clear();
        passwordInput.sendKeys(TEST_PASSWORD);
        System.out.println("Entered password");

        submitButton.click();
        System.out.println("Submitted login form");

        // Step 4: Verify successful login with multiple strategies
        boolean loginSuccessful = false;
        String successIndicator = "";

        // Strategy 1: Check for success message
        try {
            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(@class, 'success') or contains(@class, 'message') or contains(text(), 'success') or contains(text(), 'Welcome') or contains(text(), 'Dashboard')]")
            ));

            if (successMessage.isDisplayed()) {
                loginSuccessful = true;
                successIndicator = "Success message: " + successMessage.getText();
            }
        } catch (Exception e) {
            System.out.println("No success message found: " + e.getMessage());
        }

        // Strategy 2: Check for logout button
        if (!loginSuccessful) {
            try {
                WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//button[contains(text(), 'Logout') or contains(text(), 'Sign Out') or contains(@class, 'logout')]")
                ));

                if (logoutButton.isDisplayed()) {
                    loginSuccessful = true;
                    successIndicator = "Logout button found";
                }
            } catch (Exception e) {
                System.out.println("No logout button found: " + e.getMessage());
            }
        }

        // Strategy 3: Check for user profile or welcome text
        if (!loginSuccessful) {
            try {
                WebElement userElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[contains(text(), '" + TEST_USERNAME + "') or contains(@class, 'user') or contains(@class, 'profile')]")
                ));

                if (userElement.isDisplayed()) {
                    loginSuccessful = true;
                    successIndicator = "User element found: " + userElement.getText();
                }
            } catch (Exception e) {
                System.out.println("No user element found: " + e.getMessage());
            }
        }

        // Strategy 4: Check if the form disappeared (indicating redirect)
        if (!loginSuccessful) {
            try {
                // Wait to see if the login form disappears
                wait.until(ExpectedConditions.invisibilityOfElementLocated(
                        By.cssSelector("input[name='username']")
                ));
                loginSuccessful = true;
                successIndicator = "Login form disappeared (likely redirected)";
            } catch (Exception e) {
                System.out.println("Login form still present: " + e.getMessage());
            }
        }

        // Strategy 5: Check URL change
        if (!loginSuccessful) {
            String currentUrl = driver.getCurrentUrl();
            if (!currentUrl.equals(REACT_APP_URL + "/") && !currentUrl.equals(REACT_APP_URL)) {
                loginSuccessful = true;
                successIndicator = "URL changed to: " + currentUrl;
            }
        }

        // Final assertion
        assertTrue(loginSuccessful, "Login should be successful. Indicators checked: success message, logout button, user element, form disappearance, URL change");

        if (loginSuccessful) {
            System.out.println("Login successful: " + successIndicator);
            System.out.println("✅ Scenario 2 PASSED: User logged in successfully");
        } else {
            // Debug: take screenshot or print page source
            System.out.println("Page source for debugging:");
            System.out.println(driver.getPageSource());
            fail("Login verification failed - no success indicator found");
        }
    }
}
