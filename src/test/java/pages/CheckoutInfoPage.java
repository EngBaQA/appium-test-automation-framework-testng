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


public class CheckoutInfoPage extends BasePage {

    public CheckoutInfoPage(AppiumDriver driver) {
        super(driver);
    }

    public String getTitle() {
        By locator = (driver instanceof IOSDriver)
                ? By.xpath("//XCUIElementTypeStaticText[@label='CHECKOUT: INFORMATION']")
                : By.xpath("//android.widget.TextView[@text='CHECKOUT: INFORMATION']");
        WebElement el = new WebDriverWait(driver, Duration.ofSeconds(TestUtils.WAIT))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
        return getText(el);
    }

    public CheckoutInfoPage fillFirstName(String firstName) {
        log.info("Filling first name");
        WebElement el = new WebDriverWait(driver, Duration.ofSeconds(TestUtils.WAIT))
                .until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("test-First Name")));
        sendKeys(el, firstName);
        return this;
    }

    public CheckoutInfoPage fillLastName(String lastName) {
        log.info("Filling last name");
        WebElement el = new WebDriverWait(driver, Duration.ofSeconds(TestUtils.WAIT))
                .until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("test-Last Name")));
        sendKeys(el, lastName);
        return this;
    }

    public CheckoutInfoPage fillZip(String zip) {
        log.info("Filling zip code");
        WebElement el = new WebDriverWait(driver, Duration.ofSeconds(TestUtils.WAIT))
                .until(ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("test-Zip/Postal Code")));
        sendKeys(el, zip);
        return this;
    }

    public CheckoutOverviewPage tapContinue() {
        By continueButton = AppiumBy.accessibilityId("test-CONTINUE");

        log.info("Scrolling to CONTINUE button");
        swipeUpUntilVisible(continueButton);

        log.info("Tapping CONTINUE button");
        clickBy(continueButton);

        return new CheckoutOverviewPage(driver);
    }
}
