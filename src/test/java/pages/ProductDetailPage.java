package pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
public class ProductDetailPage extends BasePage {

    public ProductDetailPage(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @AndroidFindBy(xpath = "//android.widget.TextView[@text='Sauce Labs Bolt T-Shirt']")
    @iOSXCUITFindBy(xpath = "//XCUIElementTypeStaticText[@label='Sauce Labs Bolt T-Shirt']")
    private WebElement productTitle;

    @AndroidFindBy(accessibility = "test-Price")
    @iOSXCUITFindBy(accessibility = "test-Price")
    private WebElement productPrice;

    public String getProductTitle() {
        return getText(productTitle);
    }

    public String getPrice() {
        return getText(productPrice);
    }
}
