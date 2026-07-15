package pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.TestUtils;

import java.time.Duration;
import java.util.List;

public abstract class BasePage {

    protected final Logger log = LogManager.getLogger(getClass());
    protected final AppiumDriver driver;

    protected BasePage(AppiumDriver driver) {
        this.driver = driver;
    }

    protected void clickBy(By locator) {
        log.debug("Clicking element: {}", locator);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestUtils.WAIT));
        StaleElementReferenceException lastException = null;
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
                return;
            } catch (StaleElementReferenceException e) {
                log.warn("Stale element on attempt {}: {}", attempt, locator);
                lastException = e;
            }
        }
        throw new StaleElementReferenceException(
                "Element remained stale after 3 attempts: " + locator, lastException
        );
    }

    public void waitForVisibility(WebElement e) {
        new WebDriverWait(driver, Duration.ofSeconds(TestUtils.WAIT))
                .until(ExpectedConditions.visibilityOf(e));
    }

    public void sendKeys(WebElement e, String txt) {
        waitForVisibility(e);
        e.sendKeys(txt);
    }

    public void click(WebElement e) {
        waitForVisibility(e);
        e.click();
    }

    public String getAttribute(WebElement e, String attribute) {
        waitForVisibility(e);
        return e.getAttribute(attribute);
    }

    public String getText(WebElement e) {
        waitForVisibility(e);
        if (driver instanceof IOSDriver) {
            return e.getAttribute("label");
        } else {
            return e.getAttribute("text");
        }
    }

    protected void tapAt(int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 0);
        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(List.of(tap));
    }

    protected void swipeUpUntilVisible(By locator) {
        log.debug("Swiping up until visible: {}", locator);
        for (int i = 0; i < 5; i++) {
            try {
                if (driver.findElement(locator).isDisplayed()) return;
            } catch (org.openqa.selenium.NoSuchElementException ignored) {
            }
            log.debug("Swipe attempt {}/5", i + 1);
            swipeUp();
        }
        throw new org.openqa.selenium.TimeoutException(
                "Element was not visible after 5 swipes: " + locator
        );
    }

    protected void swipeUp() {
        int centerX = driver.manage().window().getSize().getWidth() / 2;
        int startY  = (int) (driver.manage().window().getSize().getHeight() * 0.50);
        int endY    = (int) (driver.manage().window().getSize().getHeight() * 0.3);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 0);
        swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), centerX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), centerX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(List.of(swipe));
    }
}
