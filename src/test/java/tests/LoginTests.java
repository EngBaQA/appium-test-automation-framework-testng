package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.*;
import pages.LoginPage;
import pages.ProductsPage;
import qa.BaseTest;
import utils.ExpectedTextReader;
import utils.TestDataReader;

@Epic("SwagLabs Mobile Regression")
@Feature("Authentication")
public class LoginTests extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;

    @BeforeClass
    public void beforeClass() {
    }

    @AfterClass
    public void afterClass() {
    }

    @BeforeMethod
    public void beforeMethod() {
        loginPage = new LoginPage(getDriver());
    }

    @AfterMethod
    public void afterMethod() {
    }

    @Test(priority = 1)
    public void invalidUsernameTest() {
        loginPage.enterUsername(TestDataReader.getUsername("invalidUser"));
        loginPage.enterPassword(TestDataReader.getPassword("invalidUser"));
        loginPage.tapLoginExpectingError();
        String actualError = loginPage.getErrorMessage();
        Assert.assertEquals(actualError, ExpectedTextReader.getErrorMessage("invalidCredentials"));
    }

    @Test(priority = 2)
    public void invalidPasswordTest() {
        loginPage.enterUsername(TestDataReader.getUsername("invalidPassword"));
        loginPage.enterPassword(TestDataReader.getPassword("invalidPassword"));
        loginPage.tapLoginExpectingError();
        String actualError = loginPage.getErrorMessage();
        Assert.assertEquals(actualError, ExpectedTextReader.getErrorMessage("invalidCredentials"));
    }

    @Test(priority = 3)
    public void emptyUsernameTest() {
        loginPage.enterUsername(TestDataReader.getUsername("emptyUsername"));
        loginPage.enterPassword(TestDataReader.getPassword("emptyUsername"));
        loginPage.tapLoginExpectingError();
        String actualError = loginPage.getErrorMessage();
        Assert.assertEquals(actualError, ExpectedTextReader.getErrorMessage("usernameRequired"));
    }

    @Test(priority = 4)
    public void emptyPasswordTest() {
        loginPage.enterUsername(TestDataReader.getUsername("emptyPassword"));
        loginPage.enterPassword(TestDataReader.getPassword("emptyPassword"));
        loginPage.tapLoginExpectingError();
        String actualError = loginPage.getErrorMessage();
        Assert.assertEquals(actualError, ExpectedTextReader.getErrorMessage("passwordRequired"));
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 5)
    public void lockedOutUserTest() {
        loginPage.enterUsername(TestDataReader.getUsername("lockedOutUser"));
        loginPage.enterPassword(TestDataReader.getPassword("lockedOutUser"));
        loginPage.tapLoginExpectingError();
        String actualError = loginPage.getErrorMessage();
        Assert.assertEquals(actualError, ExpectedTextReader.getErrorMessage("lockedOut"));
    }

    @Severity(SeverityLevel.BLOCKER)
    @Test(priority = 6)
    public void loginTest() {
        loginPage.enterUsername(TestDataReader.getUsername("validUser"));
        loginPage.enterPassword(TestDataReader.getPassword("validUser"));
        productsPage = loginPage.tapLoginButton();
        String actualText = productsPage.getTitle();
        String expectedText = ExpectedTextReader.getPageTitle("products");
        Assert.assertEquals(actualText, expectedText);
    }
}
