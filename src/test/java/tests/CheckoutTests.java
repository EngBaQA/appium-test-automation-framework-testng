package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutCompletePage;
import pages.CheckoutInfoPage;
import pages.CheckoutOverviewPage;
import pages.LoginPage;
import pages.ProductsPage;
import qa.BaseTest;
import utils.ExpectedTextReader;
import utils.TestDataReader;

@Epic("SwagLabs Mobile Regression")
@Feature("Checkout")
public class CheckoutTests extends BaseTest {

    CheckoutInfoPage checkoutInfoPage;
    CheckoutOverviewPage checkoutOverviewPage;
    CheckoutCompletePage checkoutCompletePage;

    @BeforeMethod
    public void beforeMethod() {
        LoginPage loginPage = new LoginPage(getDriver());
        ProductsPage productsPage = loginPage
                .enterUsername(TestDataReader.getUsername("validUser"))
                .enterPassword(TestDataReader.getPassword("validUser"))
                .tapLoginButton();
        productsPage.tapOnesieAddToCart();
        CartPage cartPage = productsPage.tapCartIcon();
        checkoutInfoPage = cartPage.tapCheckout();
    }

    @Test(priority = 10)
    public void checkoutInfoPageTitleTest() {
        String actualTitle = checkoutInfoPage.getTitle();
        Assert.assertEquals(actualTitle, ExpectedTextReader.getPageTitle("checkoutInfo"));
    }

    @Test(priority = 11)
    public void checkoutOverviewPageTitleTest() {
        checkoutOverviewPage = checkoutInfoPage
                .fillFirstName("John")
                .fillLastName("Doe")
                .fillZip("12345")
                .tapContinue();
        String actualTitle = checkoutOverviewPage.getTitle();
        Assert.assertEquals(actualTitle, ExpectedTextReader.getPageTitle("checkoutOverview"));
    }

    @Severity(SeverityLevel.BLOCKER)
    @Test(priority = 12)
    public void checkoutCompleteTest() {
        checkoutCompletePage = checkoutInfoPage
                .fillFirstName("John")
                .fillLastName("Doe")
                .fillZip("12345")
                .tapContinue()
                .tapFinish();
        Assert.assertEquals(checkoutCompletePage.getTitle(), ExpectedTextReader.getPageTitle("checkoutComplete"));
        Assert.assertEquals(checkoutCompletePage.getThankYouText(), ExpectedTextReader.getCheckoutComplete("thankYouText"));
    }
}
