package qa;

import io.appium.java_client.AppiumDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.testng.annotations.*;
import qa.driver.DriverFactory;
import qa.driver.DriverManager;
import utils.TestUtils;

import java.io.InputStream;
import java.util.Properties;

public class BaseTest {

    private static final Logger log = LogManager.getLogger(BaseTest.class);

    protected Properties props;
    private String platformName;
    private String platformVersion;

    @BeforeSuite(alwaysRun = true)
    public void startAppiumServer() throws Exception {
        Properties suiteProps = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) throw new IllegalStateException("config.properties could not be found");
            suiteProps.load(in);
        }
        AppiumServerManager.startServer(
                suiteProps.getProperty("nodePath"),
                suiteProps.getProperty("appiumMainJsPath")
        );
        AllureEnvironmentWriter.write(AppiumServerManager.getServerUrl());
    }

    @AfterSuite(alwaysRun = true)
    public void stopAppiumServer() {
        AppiumServerManager.stopServer();
    }

    @Parameters({"platformName", "platformVersion"})
    @BeforeClass(alwaysRun = true)
    public void beforeTest(
            @Optional("") String platformName,
            @Optional("") String platformVersion
    ) throws Exception {
        this.platformName = platformName;
        this.platformVersion = platformVersion;

        props = new Properties();
        try (InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (inputStream == null) throw new IllegalStateException("config.properties could not be found");
            props.load(inputStream);
        }
        try (InputStream secretsStream = getClass()
                .getClassLoader()
                .getResourceAsStream("secrets.properties")) {
            if (secretsStream == null) throw new IllegalStateException("secrets.properties could not be found");
            props.load(secretsStream);
        }

        log.debug("Test class initialized | platform: {} | class: {}", platformName, getClass().getSimpleName());
    }

    @BeforeMethod(alwaysRun = true)
    public void startDriver() {
        AppiumDriver driver = DriverFactory.createDriver(platformName, platformVersion, AppiumServerManager.getServerUrl(), props);
        DriverManager.setDriver(driver);

        String platform = platformName.toLowerCase();
        String udid = "android".equals(platform)
                ? props.getProperty("androidUdid", "unknown")
                : props.getProperty("iOSUdid", "unknown");
        String deviceId = udid.replaceAll("[^a-zA-Z0-9\\-_]", "_");

        ThreadContext.put("platform", platform);
        ThreadContext.put("deviceId", deviceId);
        ThreadContext.put("sessionId", driver.getSessionId().toString());

        log.info("Driver started");
        TestUtils.startRecording(driver);
    }

    public AppiumDriver getDriver() {
        return DriverManager.getDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void stopDriver() {
        log.debug("Driver stopping | session: {}",
                DriverManager.hasDriver() ? DriverManager.getDriver().getSessionId() : "none");
        try {
            DriverManager.quitDriver();
        } finally {
            ThreadContext.clearAll();
        }
    }

    @AfterClass(alwaysRun = true)
    public void afterTest() {
        if ("ios".equalsIgnoreCase(platformName)) {
            try {
                String udid = props.getProperty("iOSUdid");
                new ProcessBuilder(
                        "xcrun", "devicectl", "device", "process", "terminate",
                        "--device", udid,
                        "--bundle-id", props.getProperty("iOSWdaBundleId")
                ).start().waitFor();
                log.info("WDA terminated successfully.");
            } catch (Exception e) {
                log.warn("WDA termination failed: {}", e.getMessage());
            }
        }
    }
}
