package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.TestUtils;

import java.time.Duration;
import java.util.List;

public class ProductsPage extends BasePage {

    public ProductsPage(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='PRODUCTS']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@label='PRODUCTS']")
    private WebElement productPageTitle;

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc='test-Item']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeOther[@name='test-Item']")
    private List<WebElement> productItems;

    @AndroidFindBy(xpath = "//android.widget.TextView[@content-desc='test-Item title' and @text='Sauce Labs Bolt T-Shirt']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@name='test-Item title' and @label='Sauce Labs Bolt T-Shirt']")
    private WebElement boltTshirtTitle;

    public String getTitle() {
        return getText(productPageTitle);
    }

    public int getProductCount() {
        waitForVisibility(productItems.get(0));
        return productItems.size();
    }

    public String getProductTitleByIndex(int index) {
        String xpath = (driver instanceof IOSDriver)
                ? "(//XCUIElementTypeStaticText[@name='test-Item title'])[" + index + "]"
                : "(//android.widget.TextView[@content-desc='test-Item title'])[" + index + "]";
        WebElement el = new WebDriverWait(driver, Duration.ofSeconds(TestUtils.WAIT))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        return (driver instanceof IOSDriver) ? el.getAttribute("label") : el.getAttribute("text");
    }

    public String getProductPriceByIndex(int index) {
        String xpath = (driver instanceof IOSDriver)
                ? "(//XCUIElementTypeStaticText[@name='test-Price'])[" + index + "]"
                : "(//android.widget.TextView[@content-desc='test-Price'])[" + index + "]";
        WebElement el = new WebDriverWait(driver, Duration.ofSeconds(TestUtils.WAIT))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        return (driver instanceof IOSDriver) ? el.getAttribute("label") : el.getAttribute("text");
    }

    public ProductsPage tapBackpackAddToCart() {
        By locator = (driver instanceof IOSDriver)
                ? By.xpath("//XCUIElementTypeOther[@name='test-Item'][.//XCUIElementTypeStaticText[@label='Sauce Labs Backpack']]//XCUIElementTypeButton[@name='test-ADD TO CART']")
                : By.xpath("//android.view.ViewGroup[@content-desc='test-Item'][.//android.widget.TextView[@text='Sauce Labs Backpack']]//android.view.ViewGroup[@content-desc='test-ADD TO CART']");
        clickBy(locator);
        return this;
    }

    public boolean isRemoveButtonDisplayed() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(TestUtils.WAIT))
                    .until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.accessibilityId("test-REMOVE")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ProductDetailPage tapBoltTshirt() {
        click(boltTshirtTitle);
        return new ProductDetailPage(driver);
    }

    public ProductsPage tapOnesieAddToCart() {
        log.info("Scrolling to find Onesie ADD TO CART");
        By locator = (driver instanceof IOSDriver)
                ? By.xpath("//XCUIElementTypeOther[@name='test-Item'][.//XCUIElementTypeStaticText[@label='Sauce Labs Onesie']]//XCUIElementTypeButton[@name='test-ADD TO CART']")
                : By.xpath("//android.view.ViewGroup[@content-desc='test-Item'][.//android.widget.TextView[@text='Sauce Labs Onesie']]//android.view.ViewGroup[@content-desc='test-ADD TO CART']");
        swipeUpUntilVisible(locator);
        clickBy(locator);
        return this;
    }

    public void scrollUp() {
        swipeUp();
    }

    public MenuPage tapMenuIcon() {
        clickBy(AppiumBy.accessibilityId("test-Menu"));
        return new MenuPage(driver);
    }

    public CartPage tapCartIcon() {
        clickBy(AppiumBy.accessibilityId("test-Cart"));
        return new CartPage(driver);
    }
}
