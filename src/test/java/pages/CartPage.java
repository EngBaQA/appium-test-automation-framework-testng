package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.TestUtils;

import java.time.Duration;

public class CartPage extends BasePage {

    public CartPage(AppiumDriver driver) {
        super(driver);
    }

    private WebElement findByLocator(By iosLocator, By androidLocator) {
        By locator = (driver instanceof IOSDriver) ? iosLocator : androidLocator;
        return new WebDriverWait(driver, Duration.ofSeconds(TestUtils.WAIT))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public String getTitle() {
        return getText(findByLocator(
                By.xpath("//XCUIElementTypeStaticText[@label='YOUR CART']"),
                By.xpath("//android.widget.TextView[@text='YOUR CART']")
        ));
    }

    public String getFirstItemName() {
        WebElement el = findByLocator(
                By.xpath("//XCUIElementTypeOther[@name='test-Description']//XCUIElementTypeStaticText[1]"),
                By.xpath("//android.view.ViewGroup[@content-desc='test-Description']//android.widget.TextView[1]")
        );
        return getText(el);
    }

    public String getFirstItemQuantity() {
        WebElement el = findByLocator(
                By.xpath("//XCUIElementTypeOther[@name='test-Amount']//XCUIElementTypeStaticText"),
                By.xpath("//android.view.ViewGroup[@content-desc='test-Amount']//android.widget.TextView")
        );
        return getText(el);
    }

    public String getFirstItemPrice() {
        WebElement el = findByLocator(
                By.xpath("//XCUIElementTypeOther[@name='test-Price']//XCUIElementTypeStaticText[1]"),
                By.xpath("//android.view.ViewGroup[@content-desc='test-Price']//android.widget.TextView[1]")
        );
        return getText(el);
    }

    public CheckoutInfoPage tapCheckout() {
        log.info("Tapping CHECKOUT button");
        clickBy(AppiumBy.accessibilityId("test-CHECKOUT"));
        return new CheckoutInfoPage(driver);
    }
}
