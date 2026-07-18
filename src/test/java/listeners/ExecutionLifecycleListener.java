package listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.testng.IExecutionListener;
import qa.RunContext;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ExecutionLifecycleListener implements IExecutionListener {

    @Override
    public void onExecutionStart() {
        RunContext.initializeDirectories();

        System.setProperty("runId", RunContext.getRunId());

        ((LoggerContext) LogManager.getContext(false)).reconfigure();

        Logger log = LogManager.getLogger(ExecutionLifecycleListener.class);
        log.info("Test execution started | runId: {}", RunContext.getRunId());

        copyAllureCategories(log);
    }

    @Override
    public void onExecutionFinish() {
        Logger log = LogManager.getLogger(ExecutionLifecycleListener.class);
        log.info("Test execution finished | runId: {}", RunContext.getRunId());
    }

    private void copyAllureCategories(Logger log) {
        Path target = RunContext.getAllureResultsDirectory().resolve("categories.json");
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("allure/categories.json")) {
            if (in == null) {
                log.warn("allure/categories.json not found on classpath; failure categories will be unavailable.");
                return;
            }
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            log.warn("categories.json could not be copied: {}", e.getMessage());
        }
    }
}
