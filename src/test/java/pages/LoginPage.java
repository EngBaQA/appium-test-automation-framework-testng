package pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
public class LoginPage extends BasePage {

    public LoginPage(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @AndroidFindBy(accessibility = "test-Username")
    @iOSXCUITFindBy(accessibility = "test-Username")
    private WebElement userNameTxtFld;

    @AndroidFindBy(accessibility = "test-Password")
    @iOSXCUITFindBy(accessibility = "test-Password")
    private WebElement passwordTxtFld;

    @AndroidFindBy(accessibility = "test-LOGIN")
    @iOSXCUITFindBy(accessibility = "test-LOGIN")
    private WebElement loginBtn;

    @AndroidFindBy(accessibility = "test-Error message")
    @iOSXCUITFindBy(accessibility = "test-Error message")
    private WebElement errorMessage;

    public LoginPage enterUsername(String username) {
        sendKeys(userNameTxtFld, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        sendKeys(passwordTxtFld, password);
        return this;
    }

    public ProductsPage tapLoginButton() {
        click(loginBtn);
        return new ProductsPage(driver);
    }

    public LoginPage tapLoginExpectingError() {
        click(loginBtn);
        return this;
    }

    public String getErrorMessage() {
        if (driver instanceof IOSDriver) {
            return getText(errorMessage.findElement(By.className("XCUIElementTypeStaticText")));
        } else {
            return getText(errorMessage.findElement(By.className("android.widget.TextView")));
        }
    }
}
