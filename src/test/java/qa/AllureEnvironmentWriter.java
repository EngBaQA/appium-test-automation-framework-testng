package qa;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public final class AllureEnvironmentWriter {

    private static final Logger log = LogManager.getLogger(AllureEnvironmentWriter.class);
    private static final AtomicBoolean written = new AtomicBoolean(false);

    private AllureEnvironmentWriter() {}

    public static void write(URL appiumServerUrl) {
        if (!written.compareAndSet(false, true)) {
            log.warn("AllureEnvironmentWriter already written for this run; skipping duplicate call.");
            return;
        }

        Properties config = loadConfig();

        // Property keys must not contain spaces: java.util.Properties ends a
        // key at the first unescaped space, which collapses "Configured X"
        // entries into a single "Configured" key in the report.
        Map<String, String> env = new LinkedHashMap<>();
        env.put("RunID", RunContext.getRunId());
        env.put("Execution.Mode", "Local Parallel");
        env.put("Java", System.getProperty("java.version"));
        env.put("OS", System.getProperty("os.name") + " " + System.getProperty("os.arch"));
        env.put("Appium", fetchAppiumVersion(appiumServerUrl));
        env.put("Configured.Platforms", "Android, iOS");
        env.put("Configured.Android.Device", config.getProperty("androidUdid", "unknown"));
        env.put("Configured.iOS.Device", config.getProperty("iOSUdid", "unknown"));

        Path target = RunContext.getAllureResultsDirectory().resolve("environment.properties");

        try (BufferedWriter writer = Files.newBufferedWriter(target)) {
            for (Map.Entry<String, String> entry : env.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            log.warn("environment.properties could not be written: {}", e.getMessage());
            return;
        }

        log.info("Allure environment written | runId: {}", RunContext.getRunId());
    }

    private static Properties loadConfig() {
        Properties props = new Properties();
        for (String file : new String[]{"config.properties", "secrets.properties"}) {
            try (InputStream in = AllureEnvironmentWriter.class.getClassLoader().getResourceAsStream(file)) {
                if (in != null) props.load(in);
            } catch (IOException e) {
                log.warn("{} could not be loaded: {}", file, e.getMessage());
            }
        }
        return props;
    }

    private static String fetchAppiumVersion(URL serverUrl) {
        try {
            URL statusUrl = new URL(serverUrl.toString() + "status");
            HttpURLConnection conn = (HttpURLConnection) statusUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                return new JSONObject(sb.toString())
                        .getJSONObject("value")
                        .getJSONObject("build")
                        .getString("version");
            }
        } catch (Exception e) {
            log.warn("Appium version could not be fetched from /status: {}", e.getMessage());
            return "Unknown";
        }
    }
}
