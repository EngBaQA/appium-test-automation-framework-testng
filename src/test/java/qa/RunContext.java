package qa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class RunContext {

    private static final String RUN_ID =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

    private static final Path RUN_DIRECTORY = Paths.get("test-output", RUN_ID);

    private RunContext() {}

    public static String getRunId() {
        return RUN_ID;
    }

    public static Path getRunDirectory() {
        return RUN_DIRECTORY;
    }

    public static Path getLogsDirectory() {
        return RUN_DIRECTORY.resolve("logs");
    }

    public static Path getScreenshotsDirectory() {
        return RUN_DIRECTORY.resolve("screenshots");
    }

    public static Path getVideosDirectory() {
        return RUN_DIRECTORY.resolve("videos");
    }

    public static Path getAppiumServerLog() {
        return getLogsDirectory().resolve("appium-server.log");
    }

    public static Path getAllureResultsDirectory() {
        return RUN_DIRECTORY.resolve("allure-results");
    }

    public static Path getAllureReportDirectory() {
        return RUN_DIRECTORY.resolve("allure-report");
    }

    public static void initializeDirectories() {
        try {
            Files.createDirectories(getLogsDirectory());
            Files.createDirectories(getScreenshotsDirectory());
            Files.createDirectories(getVideosDirectory());
            Files.createDirectories(getAllureResultsDirectory());
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Run directories could not be created: " + RUN_DIRECTORY, e);
        }
    }
}
