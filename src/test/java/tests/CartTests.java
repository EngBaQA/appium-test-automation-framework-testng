package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.LoginPage;
import pages.ProductsPage;
import qa.BaseTest;
import utils.ExpectedTextReader;
import utils.TestDataReader;

@Epic("SwagLabs Mobile Regression")
@Feature("Shopping Cart")
public class CartTests extends BaseTest {

    CartPage cartPage;
    String expectedItemPrice;

    @BeforeMethod
    public void beforeMethod() {
        LoginPage loginPage = new LoginPage(getDriver());
        ProductsPage productsPage = loginPage
                .enterUsername(TestDataReader.getUsername("validUser"))
                .enterPassword(TestDataReader.getPassword("validUser"))
                .tapLoginButton();
        expectedItemPrice = productsPage.getProductPriceByIndex(1);
        productsPage.tapBackpackAddToCart();
        cartPage = productsPage.tapCartIcon();
    }

    @Test(priority = 7)
    public void cartPageTitleTest() {
        String actualTitle = cartPage.getTitle();
        Assert.assertEquals(actualTitle, ExpectedTextReader.getPageTitle("cart"));
    }

    @Test(priority = 8)
    public void cartItemNameTest() {
        String actualName = cartPage.getFirstItemName();
        Assert.assertEquals(actualName, ExpectedTextReader.getCartItem("itemName"));
    }

    @Test(priority = 9)
    public void cartItemQuantityTest() {
        String actualQuantity = cartPage.getFirstItemQuantity();
        Assert.assertEquals(actualQuantity, ExpectedTextReader.getCartItem("itemQuantity"));
    }

    @Test(priority = 10)
    public void cartItemPriceTest() {
        String actualPrice = cartPage.getFirstItemPrice();
        Assert.assertEquals(actualPrice, expectedItemPrice);
    }
}
