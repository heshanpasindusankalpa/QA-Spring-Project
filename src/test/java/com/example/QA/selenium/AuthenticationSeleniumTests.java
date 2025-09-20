package com.example.QA.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;


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
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        // CI-friendly Chrome options
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        // Unique, valid temp directory inside /tmp
        String tmpDir = "/tmp/chrome-user-data-" + System.currentTimeMillis();
        options.addArguments("--user-data-dir=" + tmpDir);

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIMEOUT));
    }





    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    public void testUserRegistration() {
        System.out.println("Running Scenario 1: User Registration");
        driver.get(REACT_APP_URL);

        // Switch to registration if needed
        try {
            WebElement toggleLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(), 'Register')]")
            ));
            toggleLink.click();
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Registration toggle not found: " + e.getMessage());
        }

        // Fill registration form
        WebElement usernameInput = driver.findElement(By.cssSelector("input[name='username']"));
        WebElement emailInput = driver.findElement(By.cssSelector("input[name='email']"));
        WebElement passwordInput = driver.findElement(By.cssSelector("input[name='password']"));
        WebElement submitButton = driver.findElement(By.xpath("//button[contains(text(), 'Register')]"));

        usernameInput.clear();
        usernameInput.sendKeys(TEST_USERNAME);
        emailInput.clear();
        emailInput.sendKeys(TEST_EMAIL);
        passwordInput.clear();
        passwordInput.sendKeys(TEST_PASSWORD);

        submitButton.click();

        // Verify successful registration
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(@class, 'success') or contains(@class, 'message')]")
        ));

        assertTrue(successMessage.isDisplayed(), "Success message should be visible");
        assertTrue(successMessage.getText().contains("successful"),
                "Success message should contain 'successful'");

        System.out.println("✅ Scenario 1 PASSED: User registered successfully");
    }

    @Test
    @Order(2)
    public void testUserLogin() {
        System.out.println("Running Scenario 2: User Login");
        driver.get(REACT_APP_URL);

        // Fill login form
        WebElement usernameInput = driver.findElement(By.cssSelector("input[name='username']"));
        WebElement passwordInput = driver.findElement(By.cssSelector("input[name='password']"));
        WebElement submitButton = driver.findElement(By.xpath("//button[contains(text(), 'Login')]"));

        usernameInput.clear();
        usernameInput.sendKeys(TEST_USERNAME);
        passwordInput.clear();
        passwordInput.sendKeys(TEST_PASSWORD);

        submitButton.click();

        // Verify successful login by checking for success message
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p[contains(@class, 'success') and contains(@class, 'message')]")
        ));

        assertTrue(successMessage.isDisplayed(), "Login success message should be visible");
        assertTrue(successMessage.getText().toLowerCase().contains("success"),
                "Login message should contain 'success'");

        System.out.println("Login successful: " + successMessage.getText());

        // Additional verification: Check if user-specific elements appear
        // This depends on how your app changes after login
        try {
            // Option 1: Check if user greeting appears
            WebElement userGreeting = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(), '" + TEST_USERNAME + "') or contains(text(), 'Welcome')]")
            ));
            System.out.println("User greeting found: " + userGreeting.getText());
        } catch (Exception e) {
            System.out.println("No user greeting found, but login was successful via message");
        }

        // Option 2: Check if the form inputs become disabled or read-only (some apps do this)
        try {
            String usernameState = usernameInput.getAttribute("disabled");
            String passwordState = passwordInput.getAttribute("disabled");
            if ("true".equals(usernameState) || "true".equals(passwordState)) {
                System.out.println("Form inputs disabled after login - indicates success");
            }
        } catch (Exception e) {
            // Inputs might be recreated, so this is optional
        }

        System.out.println("✅ Scenario 2 PASSED: User logged in successfully");
    }
}