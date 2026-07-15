package listeners;

import io.appium.java_client.AppiumDriver;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.FileSystemResultsWriter;
import io.qameta.allure.model.Parameter;
import io.qameta.allure.testng.AllureTestNg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.testng.IInvokedMethod;
import org.testng.ITestResult;
import qa.RunContext;
import qa.driver.DriverManager;
import utils.TestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class AllureRunListener extends AllureTestNg {

    private static final Logger log = LogManager.getLogger(AllureRunListener.class);

    private static volatile AllureLifecycle sharedLifecycle;

    public AllureRunListener() {
        this(new AllureLifecycle(new FileSystemResultsWriter(RunContext.getAllureResultsDirectory())));
    }

    private AllureRunListener(AllureLifecycle lifecycle) {
        super(lifecycle);
        sharedLifecycle = lifecycle;
    }

    public static AllureLifecycle getSharedLifecycle() {
        return sharedLifecycle;
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        super.beforeInvocation(method, testResult);
        if (!method.isTestMethod()) {
            return;
        }
        String deviceId = ThreadContext.get("deviceId");
        String sessionId = ThreadContext.get("sessionId");
        if (sessionId == null) {
            return;
        }
        getLifecycle().updateTestCase(result -> {
            String platformValue = "";
            String osVersionValue = "";
            for (Parameter p : result.getParameters()) {
                if ("platformName".equals(p.getName())) platformValue = p.getValue();
                else if ("platformVersion".equals(p.getName())) osVersionValue = p.getValue();
            }

            result.getParameters().clear();
            result.getParameters().add(param("Platform", platformValue));
            result.getParameters().add(param("Device", deviceId != null ? deviceId : "unknown"));
            result.getParameters().add(param("OS Version", osVersionValue));
            result.getParameters().add(param("Session ID", sessionId));
        });
    }

    @Override
    public void onTestFailure(ITestResult result) {
        // Attachments must be added BEFORE super.onTestFailure(),
        // because super stops and writes the test case result.
        attachFailureEvidence(result);
        super.onTestFailure(result);
    }

    private void attachFailureEvidence(ITestResult result) {
        if (!DriverManager.hasDriver()) return;

        AppiumDriver driver = DriverManager.getDriver();

        try {
            Path screenshot = TestUtils.takeScreenshot(driver, result.getName());
            attach("Screenshot", "image/png", "png", screenshot);
        } catch (Exception e) {
            log.warn("Screenshot could not be attached: {}", e.getMessage());
        }

        try {
            String base64 = TestUtils.stopRecording(driver);
            Path video = TestUtils.saveRecording(driver, base64, result.getName());
            attach("Video", "video/mp4", "mp4", video);
        } catch (Exception e) {
            log.warn("Video could not be attached: {}", e.getMessage());
        }
    }

    private void attach(String name, String mimeType, String extension, Path file) {
        if (file == null || !Files.exists(file)) return;
        try (InputStream is = Files.newInputStream(file)) {
            getLifecycle().addAttachment(name, mimeType, extension, is);
        } catch (IOException e) {
            log.warn("Allure attachment could not be added [{}]: {}", name, e.getMessage());
        }
    }

    private static Parameter param(String name, String value) {
        Parameter p = new Parameter();
        p.setName(name);
        p.setValue(value);
        return p;
    }
}
