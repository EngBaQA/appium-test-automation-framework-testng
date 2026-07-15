package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
public class MenuPage extends BasePage {

    public MenuPage(AppiumDriver driver) {
        super(driver);
    }

    public ProductsPage tapAllItems() {
        clickBy(AppiumBy.accessibilityId("test-ALL ITEMS"));
        return new ProductsPage(driver);
    }

    public void tapLogout() {
        clickBy(AppiumBy.accessibilityId("test-LOGOUT"));
    }

    public MenuPage tapResetAppState() {
        clickBy(AppiumBy.accessibilityId("test-RESET APP STATE"));
        return this;
    }

    public void tapClose() {
        clickBy(AppiumBy.accessibilityId("test-Close"));
    }
}
