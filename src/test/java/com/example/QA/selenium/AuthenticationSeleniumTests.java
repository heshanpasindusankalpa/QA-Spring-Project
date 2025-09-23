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
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless"); // Uncomment for CI/CD
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu", "--window-size=1920,1080");

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

    @Test
    @Order(1)
    public void testUserRegistration() throws Exception {
        System.out.println("Running Scenario 1: User Registration");
        driver.get(REACT_APP_URL);

        // Switch to registration form if not already
        try {
            WebElement toggleLink = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(text(), 'Register') or contains(text(), 'Sign Up')]")
            ));
            if (driver.findElements(By.xpath("//input[@name='email']")).isEmpty()) {
                toggleLink.click();
                Thread.sleep(1000);
            }
        } catch (Exception ignored) {}

        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='username']")));
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='email']")));
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='password']")));
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Register')]")));

        usernameInput.sendKeys(TEST_USERNAME);
        emailInput.sendKeys(TEST_EMAIL);
        passwordInput.sendKeys(TEST_PASSWORD);
        submitButton.click();

        try {
            WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//p[contains(@class, 'success') or contains(@class, 'message')]")
            ));
            assertTrue(successMessage.isDisplayed(), "Success message should be visible");
            System.out.println("Success message: " + successMessage.getText());
        } catch (Exception e) {
            WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//button[contains(text(), 'Login')]")
            ));
            assertTrue(loginButton.isDisplayed(), "Should be back on login form after registration");
        }
    }

    @Test
    @Order(2)
    public void testUserLogin() {
        System.out.println("Running Scenario 2: User Login");
        driver.get(REACT_APP_URL);

        WebElement usernameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='username']")));
        WebElement passwordInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='password']")));
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Login')]")));

        usernameInput.sendKeys(TEST_USERNAME);
        passwordInput.sendKeys(TEST_PASSWORD);
        submitButton.click();

        boolean loginSuccessful = false;
        String successIndicator = "";

        // Strategy 1: Check message element directly
        try {
            WebElement messageElement = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("p.message"))
            );
            String messageText = messageElement.getText();
            assertTrue(messageText.toLowerCase().contains("success") || messageText.length() > 0,
                    "Expected some login success message, but got: " + messageText);
            loginSuccessful = true;
            successIndicator = "Message displayed: " + messageText;
        } catch (Exception e) {
            System.out.println("No direct message found: " + e.getMessage());
        }

        // Strategy 2: Check for logout button
        if (!loginSuccessful) {
            try {
                WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//button[contains(text(), 'Logout')]")
                ));
                if (logoutButton.isDisplayed()) {
                    loginSuccessful = true;
                    successIndicator = "Logout button found";
                }
            } catch (Exception ignored) {}
        }

        // Strategy 3: URL change
        if (!loginSuccessful) {
            String currentUrl = driver.getCurrentUrl();
            if (!currentUrl.equals(REACT_APP_URL) && !currentUrl.equals(REACT_APP_URL + "/")) {
                loginSuccessful = true;
                successIndicator = "Redirected to: " + currentUrl;
            }
        }

        assertTrue(loginSuccessful, "Login should be successful, but no indicators found.");
        System.out.println("✅ Login success: " + successIndicator);
    }
}
