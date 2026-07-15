package listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import qa.RunContext;

public class SuiteListener implements ISuiteListener {

    private static final Logger log = LogManager.getLogger(SuiteListener.class);
    private long startTime;

    @Override
    public void onStart(ISuite suite) {
        startTime = System.currentTimeMillis();
        log.info("Suite started | name: {} | runId: {}", suite.getName(), RunContext.getRunId());
    }

    @Override
    public void onFinish(ISuite suite) {
        int passed = 0;
        int failed = 0;
        int skipped = 0;

        for (ISuiteResult suiteResult : suite.getResults().values()) {
            ITestContext context = suiteResult.getTestContext();
            passed += context.getPassedTests().size();
            failed += context.getFailedTests().size();
            skipped += context.getSkippedTests().size();
        }

        long durationMs = System.currentTimeMillis() - startTime;
        long minutes = durationMs / 60000;
        long seconds = (durationMs % 60000) / 1000;

        log.info("Suite finished | name: {} | passed: {} | failed: {} | skipped: {} | duration: {}m {}s",
                suite.getName(), passed, failed, skipped, minutes, seconds);
    }
}
