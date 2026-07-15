package qa;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;

public final class AppiumServerManager {

    private static final Logger log = LogManager.getLogger(AppiumServerManager.class);
    private static AppiumDriverLocalService service;

    private AppiumServerManager() {}

    public static synchronized void startServer(String nodePath, String appiumMainJsPath) {
        if (service != null && service.isRunning()) {
            log.info("Appium server already running at {}", service.getUrl());
            return;
        }

        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1")
                .usingPort(4723)
                .usingDriverExecutable(new File(nodePath))
                .withAppiumJS(new File(appiumMainJsPath))
                .withLogFile(RunContext.getAppiumServerLog().toFile());

        service = AppiumDriverLocalService.buildService(builder);
        service.start();

        if (!service.isRunning()) {
            throw new IllegalStateException("Appium server could not be started on port 4723.");
        }

        log.info("Appium server started at {}", service.getUrl());
    }

    public static synchronized void stopServer() {
        if (service != null && service.isRunning()) {
            service.stop();
            log.info("Appium server stopped.");
        }
        service = null;
    }

    public static URL getServerUrl() {
        if (service == null || !service.isRunning()) {
            throw new IllegalStateException("Appium server is not running.");
        }
        return service.getUrl();
    }

    public static boolean isRunning() {
        return service != null && service.isRunning();
    }
}
