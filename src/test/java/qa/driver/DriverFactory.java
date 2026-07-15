package qa.driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.net.URL;
import java.time.Duration;
import java.util.Properties;

public final class DriverFactory {

    private DriverFactory() {}

    public static AppiumDriver createDriver(
            String platform,
            String platformVersion,
            URL appiumServerUrl,
            Properties props
    ) {
        return switch (platform.toLowerCase()) {
            case "android" -> createAndroidDriver(platformVersion, appiumServerUrl, props);
            case "ios"     -> createIOSDriver(platformVersion, appiumServerUrl, props);
            default        -> throw new IllegalArgumentException("Unsupported platform: " + platform);
        };
    }

    private static AndroidDriver createAndroidDriver(
            String platformVersion,
            URL appiumServerUrl,
            Properties props
    ) {
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setPlatformVersion(platformVersion)
                .setAutomationName(props.getProperty("androidAutomationName"))
                .setAppPackage(props.getProperty("androidAppPackage"))
                .setAppActivity(props.getProperty("androidAppActivity"))
                .setUdid(props.getProperty("androidUdid"))
                .setAppWaitActivity("com.swaglabsmobileapp.MainActivity")
                .setNewCommandTimeout(Duration.ofSeconds(300));

        options.setCapability("appium:systemPort",
                Integer.parseInt(props.getProperty("androidSystemPort", "8200")));

        AndroidDriver driver = new AndroidDriver(appiumServerUrl, options);
        driver.setSetting("allowInvisibleElements", true);
        return driver;
    }

    private static IOSDriver createIOSDriver(
            String platformVersion,
            URL appiumServerUrl,
            Properties props
    ) {
        XCUITestOptions options = new XCUITestOptions()
                .setPlatformName("iOS")
                .setPlatformVersion(platformVersion)
                .setAutomationName(props.getProperty("iOSAutomationName"))
                .setUdid(props.getProperty("iOSUdid"))
                .setShowXcodeLog(true)
                .setNewCommandTimeout(Duration.ofSeconds(300));

        options.setCapability("appium:bundleId",       props.getProperty("iOSBundleId"));
        options.setCapability("appium:xcodeSigningId", props.getProperty("iOSXcodeSigningId"));
        options.setCapability("appium:xcodeOrgId",     props.getProperty("iOSXcodeOrgId"));
        options.setCapability("appium:wdaLocalPort",
                Integer.parseInt(props.getProperty("iOSWdaLocalPort", "8100")));
        options.setCapability("appium:mjpegServerPort",
                Integer.parseInt(props.getProperty("iOSMjpegServerPort", "9100")));

        return new IOSDriver(appiumServerUrl, options);
    }
}
