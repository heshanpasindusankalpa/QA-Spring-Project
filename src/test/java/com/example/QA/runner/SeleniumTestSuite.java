package com.example.QA.runner;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        com.example.QA.selenium.AuthenticationSeleniumTests.class
})
public class SeleniumTestSuite {
    // This class serves as a test suite to run all Selenium tests
}