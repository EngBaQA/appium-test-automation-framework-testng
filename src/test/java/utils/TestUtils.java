package utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import qa.RunContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

public class TestUtils {

    private static final Logger log = LogManager.getLogger(TestUtils.class);
    public static final long WAIT = 15;

    public static Path takeScreenshot(AppiumDriver driver, String testName) {
        File source = driver.getScreenshotAs(OutputType.FILE);

        String platform = getPlatform(driver);
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));

        // Prevents filename collisions when the same test fails multiple times
        String fileName = sanitize(testName) + "_" + sanitize(platform) + "_" + timestamp + ".png";
        Path destination = RunContext.getScreenshotsDirectory().resolve(sanitize(platform)).resolve(fileName);

        try {
            Files.createDirectories(destination.getParent());
            Files.copy(source.toPath(), destination);
        } catch (IOException e) {
            throw new RuntimeException("Screenshot could not be saved", e);
        }
        return destination;
    }

    public static void startRecording(AppiumDriver driver) {
        try {
            if (driver instanceof AndroidDriver androidDriver) {
                androidDriver.startRecordingScreen(
                        new AndroidStartScreenRecordingOptions().withTimeLimit(java.time.Duration.ofMinutes(3))
                );
            } else if (driver instanceof IOSDriver iosDriver) {
                iosDriver.startRecordingScreen(
                        new IOSStartScreenRecordingOptions().withTimeLimit(java.time.Duration.ofMinutes(3))
                );
            }
        } catch (Exception e) {
            log.warn("Screen recording could not be started: {}", e.getMessage());
        }
    }

    public static String stopRecording(AppiumDriver driver) {
        if (driver instanceof AndroidDriver androidDriver) {
            return androidDriver.stopRecordingScreen();
        } else if (driver instanceof IOSDriver iosDriver) {
            return iosDriver.stopRecordingScreen();
        }
        return null;
    }

    public static Path saveRecording(AppiumDriver driver, String base64, String testName) {
        if (base64 == null || base64.isEmpty()) return null;

        String platform = getPlatform(driver);
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));

        // Prevents filename collisions when the same test fails multiple times
        String fileName = sanitize(testName) + "_" + sanitize(platform) + "_" + timestamp + ".mp4";
        Path destination = RunContext.getVideosDirectory().resolve(sanitize(platform)).resolve(fileName);

        try {
            Files.createDirectories(destination.getParent());
            Files.write(destination, Base64.getDecoder().decode(base64));
        } catch (IOException e) {
            throw new RuntimeException("Video could not be saved", e);
        }
        return destination;
    }

    private static String getPlatform(AppiumDriver driver) {
        return driver.getCapabilities()
                .getPlatformName()
                .toString()
                .toLowerCase();
    }

    // Ensures safe filenames across all OS and CI environments
    private static String sanitize(String value) {
        return value.replaceAll("[^a-zA-Z0-9\\-_]", "_");
    }
}
