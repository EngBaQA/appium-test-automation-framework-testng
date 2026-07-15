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

public class CheckoutOverviewPage extends BasePage {

    public CheckoutOverviewPage(AppiumDriver driver) {
        super(driver);
    }

    public String getTitle() {
        By locator = (driver instanceof IOSDriver)
                ? By.xpath("//XCUIElementTypeStaticText[@label='CHECKOUT: OVERVIEW']")
                : By.xpath("//android.widget.TextView[@text='CHECKOUT: OVERVIEW']");
        WebElement el = new WebDriverWait(driver, Duration.ofSeconds(TestUtils.WAIT))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
        return getText(el);
    }

    public String getTotal() {
        By locator = (driver instanceof IOSDriver)
                ? By.xpath("//XCUIElementTypeStaticText[contains(@name,'Total:')]")
                : By.xpath("//android.widget.TextView[contains(@text,'Total:')]");
        WebElement el = new WebDriverWait(driver, Duration.ofSeconds(TestUtils.WAIT))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
        return getText(el);
    }

    public CheckoutCompletePage tapFinish() {
        By finishButton = AppiumBy.accessibilityId("test-FINISH");
        log.info("Scrolling to FINISH button");
        swipeUpUntilVisible(finishButton);
        log.info("Tapping FINISH button");
        clickBy(finishButton);
        return new CheckoutCompletePage(driver);
    }
}
