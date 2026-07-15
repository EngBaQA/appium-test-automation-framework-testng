package pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.TestUtils;

import java.time.Duration;

public class CheckoutCompletePage extends BasePage {

    public CheckoutCompletePage(AppiumDriver driver) {
        super(driver);
    }

    public String getTitle() {
        By locator = (driver instanceof IOSDriver)
                ? By.xpath("//XCUIElementTypeStaticText[@name='CHECKOUT: COMPLETE!']")
                : By.xpath("//android.widget.TextView[@text='CHECKOUT: COMPLETE!']");
        WebElement el = new WebDriverWait(driver, Duration.ofSeconds(TestUtils.WAIT))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
        return getText(el);
    }

    public String getThankYouText() {
        By locator = (driver instanceof IOSDriver)
                ? By.xpath("//XCUIElementTypeStaticText[@name='THANK YOU FOR YOU ORDER']")
                : By.xpath("//android.widget.TextView[@text='THANK YOU FOR YOU ORDER']");
        WebElement el = new WebDriverWait(driver, Duration.ofSeconds(TestUtils.WAIT))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
        return getText(el);
    }
}
