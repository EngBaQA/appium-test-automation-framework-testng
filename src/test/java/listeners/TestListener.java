package listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;
import qa.driver.DriverManager;
import utils.TestUtils;

public class TestListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        log.info("Test started: {}", result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("Test passed: {} | duration: {}ms", result.getName(), getDuration(result));
        stopAndDiscardVideo();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("Test failed: {} | duration: {}ms", result.getName(), getDuration(result));

        if (result.getThrowable() != null) {
            log.error("Failure cause: ", result.getThrowable());
        }
        // Failure evidence (screenshot + video) is captured and attached
        // to the Allure result by AllureRunListener.onTestFailure().
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("Test skipped: {}", result.getName());
        stopAndDiscardVideo();
    }

    private long getDuration(ITestResult result) {
        return result.getEndMillis() - result.getStartMillis();
    }

    private void stopAndDiscardVideo() {
        if (!DriverManager.hasDriver()) return;
        try {
            TestUtils.stopRecording(DriverManager.getDriver());
        } catch (Exception e) {
            log.warn("Recording could not be stopped: {}", e.getMessage());
        }
    }
}
