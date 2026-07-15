package tests;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductDetailPage;
import pages.ProductsPage;
import qa.BaseTest;
import utils.ExpectedTextReader;
import utils.TestDataReader;

@Epic("SwagLabs Mobile Regression")
@Feature("Product Catalog")
public class ProductTests extends BaseTest {

    ProductsPage productsPage;

    @BeforeMethod
    public void beforeMethod() {
        LoginPage loginPage = new LoginPage(getDriver());
        productsPage = loginPage
                .enterUsername(TestDataReader.getUsername("validUser"))
                .enterPassword(TestDataReader.getPassword("validUser"))
                .tapLoginButton();
    }

    @Test(priority = 1, groups = {"ios-only"})
    public void productCountTest() {
        Assert.assertEquals(productsPage.getProductCount(), ExpectedTextReader.getProductCount());
    }

    @Test(priority = 2, groups = {"ios-only"})
    public void productTitleTest() {
        int count = ExpectedTextReader.getProductCount();
        for (int i = 0; i < count; i++) {
            if (i == 4) productsPage.scrollUp();
            String actualTitle = productsPage.getProductTitleByIndex(i + 1);
            Assert.assertEquals(actualTitle, ExpectedTextReader.getProductTitle(i), "Title mismatch at index " + (i + 1));
        }
    }

    @Test(priority = 3, groups = {"ios-only"})
    public void productPriceTest() {
        int count = ExpectedTextReader.getProductCount();
        for (int i = 0; i < count; i++) {
            if (i == 2) productsPage.scrollUp();
            if (i == 4) productsPage.scrollUp();
            String actualPrice = productsPage.getProductPriceByIndex(i + 1);
            Assert.assertEquals(actualPrice, ExpectedTextReader.getProductPrice(i), "Price mismatch at index " + (i + 1));
        }
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 4)
    public void addToCartTest() {
        productsPage.tapBackpackAddToCart();
        Assert.assertTrue(productsPage.isRemoveButtonDisplayed());
    }

    @Test(priority = 5)
    public void productDetailTitleTest() {
        ProductDetailPage productDetailPage = productsPage.tapBoltTshirt();
        String actualTitle = productDetailPage.getProductTitle();
        Assert.assertEquals(actualTitle, ExpectedTextReader.getProductTitle(2));
    }

    @Test(priority = 6)
    public void productDetailPriceTest() {
        ProductDetailPage productDetailPage = productsPage.tapBoltTshirt();
        String actualPrice = productDetailPage.getPrice();
        Assert.assertEquals(actualPrice, ExpectedTextReader.getProductPrice(2));
    }
}
